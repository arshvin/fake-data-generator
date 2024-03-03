package preved.medved.generator.source.faikers;

import com.google.common.collect.Streams;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import preved.medved.generator.source.DataProducer;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Log4j2
public class AbstractFaiker implements DataProducer {
  protected ExecutorService executor;
  protected List<Callable<String>> dataFetchers = new ArrayList<>();
  protected Queue<Future<String>> generatedDataResult = new LinkedList<Future<String>>();

  protected void generateData() {
    generatedDataResult.addAll(
        this.dataFetchers.stream()
            .map(
                task -> {
                  return executor.submit(task);
                })
            .toList());
  }
  ;

  public List<String> produce() {
    ArrayList<String> result = new ArrayList<String>();

    for (int i = 0; i < dataFetchers.size(); i++) {
      Future<String> item = generatedDataResult.poll();
      int counter = 0;
      while (!item.isDone()) {
        log.debug("Waiting result from faiker. Iteration: {}", counter);
      }

      String faikerTaskResult = null;
      try {
        faikerTaskResult = item.get();
      } catch (InterruptedException e) {
        e.printStackTrace();
      } catch (ExecutionException e) {
        e.printStackTrace();
      }

      log.debug("Retrieved result: {}", faikerTaskResult);
      result.add(faikerTaskResult);
    }
    generateData();
    return result;
  }
  ;

  protected void fillBuffer(int cyclesNumber) {
    for (int i = 0; i < cyclesNumber; i++) {
      generateData();
    }
  }
}
