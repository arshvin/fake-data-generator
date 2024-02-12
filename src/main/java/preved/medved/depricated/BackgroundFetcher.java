package preved.medved.depricated;

import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.*;
import lombok.extern.log4j.Log4j2;
import preved.medved.depricated.fileWriter.Header;
import preved.medved.depricated.fileWriter.Producer;

@Log4j2
public class BackgroundFetcher implements Producer, Header {
  private static final ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
  protected Queue<List<String>> queue = new ConcurrentLinkedQueue<>();
  protected Runnable fetchTask;
  protected String[] header;

  protected void requestNewData() {
    log.debug("Submitting the Runnable task");
    executor.submit(fetchTask);
  }

  protected List<String> retrieveData() throws InterruptedException {
    while (queue.isEmpty()) {
      log.debug("Waiting data from the queue...");
    }
    log.debug("Getting data from the queue");
    return queue.poll();
  }

  public List<String> produceData() {
    log.debug("Retrieving data");
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
    executor.shutdown();
  }

  @Override
  public List<String> getHeader() {
    return Arrays.stream(header).toList();
  }
}
