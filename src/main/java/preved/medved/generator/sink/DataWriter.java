package preved.medved.generator.sink;

import java.io.IOException;
import java.util.List;

public interface DataWriter {
    void writeRecord(List<String> data) throws IOException;
    void close() throws IOException;
}
