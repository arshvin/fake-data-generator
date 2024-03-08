package preved.medved.generator.sink;

import lombok.extern.log4j.Log4j2;
import org.supercsv.io.CsvListWriter;
import org.supercsv.prefs.CsvPreference;

import java.io.*;
import java.nio.file.Path;
import java.util.List;

@Log4j2
public class CsvFileTargetWriter implements DataWriter{
    private final CsvListWriter csvListWriter;

    public CsvFileTargetWriter(Path output) throws FileNotFoundException {
        log.info("Opening file: {}", output);

        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(output.toFile()));
        this.csvListWriter =
                new CsvListWriter(outputStreamWriter, CsvPreference.STANDARD_PREFERENCE);
    }

    @Override
    public void writeRecord(List<String> data) throws IOException {
        csvListWriter.write(data);
    }

    @Override
    public void close() throws IOException {
        log.info("Closing CSV file");
        csvListWriter.close();
    }
}
