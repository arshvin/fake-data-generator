package preved.medved;

import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.*;
import lombok.extern.log4j.Log4j2;
import preved.medved.producers.Header;
import preved.medved.producers.Producer;

@Log4j2
public class BackgroundFetcher implements Producer, Header {
  private static ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
  protected Queue<List<String>> queue = new ConcurrentLinkedQueue<>();
  protected Runnable fetchTask;
  protected String[] header;

  protected void requestNewData() {
    log.trace("Submitting the Runnable task");
    executor.submit(fetchTask);
  }
  ;

  protected List<String> retrieveData() throws InterruptedException {
    while (queue.isEmpty()) {
      log.trace("Waiting data from the queue...");
    }
    log.trace("Getting data from the queue");
    return queue.poll();
  }
  ;

  public List<String> produceData() {
    log.trace("Retrieving data");
    List<String> items = null;
    try {
      items = retrieveData();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    requestNewData();
    return items;
  }

  @Override
  public void close() {
    shutdown();
  }

  protected void shutdown() {
    executor.shutdown();
  }

  @Override
  public List<String> getHeader() {
    return Arrays.stream(header).toList();
  }
  ;
}
