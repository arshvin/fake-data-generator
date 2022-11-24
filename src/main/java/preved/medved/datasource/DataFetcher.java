package preved.medved.datasource;

import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class DataFetcher implements Metadata, DataRetriever {
  private static ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
  protected Queue<List<String>> queue = new ConcurrentLinkedQueue<>();
  protected Runnable fetchTask;
  protected String[] header;

  protected void requestNewData() {
    log.debug("Submitting the Runnable task");
    executor.submit(fetchTask);
  }
  ;

  protected List<String> retrieveData() throws InterruptedException {
    while (queue.isEmpty()) {
      log.debug("Waiting data from the queue...");
    }
    log.debug("Getting data from the queue");
    return queue.poll();
  }
  ;

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
  ;
}
