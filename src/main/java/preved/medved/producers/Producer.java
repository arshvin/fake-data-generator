package preved.medved.producers;

import java.util.List;

public interface Producer {
    List<String> produceData();
    void close();
}
