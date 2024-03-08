package preved.medved.generator.source;

import java.util.List;

/** This interface is intended for gathering data from instances of {@link DataProducer} and preparing the result
 * record*/
public interface DataCollector {
    List<String> buildRecord();
    void appendSource(DataProducer dataProducer);
}
