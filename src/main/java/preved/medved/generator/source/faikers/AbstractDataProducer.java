package preved.medved.generator.source.faikers;

import lombok.SneakyThrows;
import org.apache.logging.log4j.Logger;
import preved.medved.generator.source.DataProducer;
import preved.medved.generator.source.RecordDescriptor;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class AbstractDataProducer implements DataProducer, RecordDescriptor {
  private final Logger log;
  protected ExecutorService executor;
  protected String[] headers;

  protected List<Callable<String[]>> dataFetchers = new ArrayList<>();
  protected Queue<Future<String[]>> generatedDataResult = new LinkedList<>();

  public AbstractDataProducer(Logger log) {
    this.log = log;
  }

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

  @SneakyThrows
  public List<String> produce() {
    ArrayList<String> result = new ArrayList<String>();

    for (int i = 0; i < dataFetchers.size(); i++) {
      Future<String[]> taskResult = generatedDataResult.poll();

      Instant before = Instant.now();
      final String[] faikerTaskResult = taskResult.get();
      Instant after = Instant.now();
      log.debug("Result retrieved during {} milliseconds", Duration.between(before,after).toMillis());

      for (int j = 0; j < faikerTaskResult.length; j++) {
        result.add(faikerTaskResult[j]);
      }
    }

    generateData();
    return result;
  }
  ;

  protected void fillBuffer(int cyclesNumber) {
    log.debug("Creating and filling buffer for read with {} available records", cyclesNumber);
    for (int i = 0; i < cyclesNumber; i++) {
      generateData();
    }
  }

  public List<String> retrieveHeaders() {
    return Arrays.stream(headers).toList();
  }
}
