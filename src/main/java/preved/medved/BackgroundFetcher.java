package preved.medved;

import lombok.extern.log4j.Log4j2;

import java.util.List;
import java.util.concurrent.*;

@Log4j2
public class BackgroundFetcher {
    private static ThreadPoolExecutor excecutor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
    protected BlockingQueue<List<String>> queue = new ArrayBlockingQueue(3);
    protected Runnable fetchTask;

    protected void requestNewData(){
        log.trace("Submitting the Runnable task");
        excecutor.submit(fetchTask);
    };

    protected List<String> retrieveData() throws InterruptedException {
        log.trace("Getting data from the queue");
        return queue.take();
    };
}
