package preved.medved;

import lombok.extern.log4j.Log4j2;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.*;

@Log4j2
public class BackgroundFetcher {
  private static ThreadPoolExecutor excecutor =
      (ThreadPoolExecutor) Executors.newCachedThreadPool();
  protected Queue<List<String>> queue = new ConcurrentLinkedQueue<>();
  protected Runnable fetchTask;

  protected void requestNewData() {
    log.trace("Submitting the Runnable task");
    excecutor.submit(fetchTask);
  }
  ;

  protected List<String> retrieveData() throws InterruptedException {
    while (queue.isEmpty()) {
      log.trace("Waiting data from the queue");
    }
    log.trace("Getting data from the queue");
    return queue.poll();
  };

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
}
