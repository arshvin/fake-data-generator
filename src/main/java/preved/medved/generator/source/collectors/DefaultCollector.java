package preved.medved.generator.source.collectors;

import lombok.extern.log4j.Log4j2;
import preved.medved.generator.source.DataCollector;
import preved.medved.generator.source.DataProducer;
import preved.medved.generator.source.faikers.RecordDescriptor;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Log4j2
public class DefaultCollector implements DataCollector, RecordDescriptor {
  private Set<DataProducer> dataProducers = new LinkedHashSet<>();

  @Override
  public List<String> buildRecord() {
    log.debug("Consolidating data for output record preparation");
    return dataProducers.stream()
        .map(dataProducer -> dataProducer.produce())
        .reduce(
            new LinkedList<String>(),
            (a, b) -> {
              a.addAll(b);
              return a;
            });
  }

  @Override
  public void appendSource(DataProducer dataProducer) {
    this.dataProducers.add(dataProducer);
  }

    @Override
  public List<String> retrieveHeaders() {
    log.debug("Requesting of records header");
    return dataProducers.stream()
        .map(
            item -> {
              return ((RecordDescriptor) item).retrieveHeaders();
            })
        .reduce(
            new LinkedList<String>(),
            (a, b) -> {
              a.addAll(b);
              return a;
            });
  }
}
