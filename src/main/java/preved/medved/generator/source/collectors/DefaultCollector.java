package preved.medved.generator.source.collectors;

import preved.medved.generator.source.DataCollector;
import preved.medved.generator.source.DataProducer;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class DefaultCollector implements DataCollector {
    private Set<DataProducer> dataProducers = new LinkedHashSet<>();

    @Override
    public List<String> buildRecord() {
        return dataProducers.stream().map(dataProducer -> dataProducer.produce()).reduce(
                new LinkedList<String>(),(a,b) -> {
                    a.addAll(b);
                    return a;
                }
        );
    }

    @Override
    public void appendSource(DataProducer dataProducer) {
        this.dataProducers.add(dataProducer);
    }
}
