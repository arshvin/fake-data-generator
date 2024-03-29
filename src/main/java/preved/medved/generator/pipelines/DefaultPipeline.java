package preved.medved.generator.pipelines;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import lombok.Setter;
import preved.medved.generator.Pipeline;
import preved.medved.generator.sink.DataWriter;
import preved.medved.generator.source.DataCollector;

public class DefaultPipeline implements Pipeline {
  @Setter private DataCollector dataCollector;
  @Setter private Set<DataWriter> dataWriters = new LinkedHashSet<>();

  @Override
  public Integer pumpUp() {
    List<String> record = this.dataCollector.buildRecord();

    this.dataWriters.forEach(
        dataWriter -> {
          try {
            dataWriter.writeRecord(record);
          } catch (IOException e) {
            e.printStackTrace();
          }
        });

    return record.stream().map(String::length).reduce(0, Integer::sum);
  }
}
