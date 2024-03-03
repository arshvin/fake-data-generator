package preved.medved.generator.sink;

import com.beust.jcommander.Strings;
import lombok.extern.log4j.Log4j2;
import org.supercsv.io.CsvListWriter;
import org.supercsv.prefs.CsvPreference;

import java.io.*;
import java.nio.file.Path;
import java.util.List;

@Log4j2
public class CsvFileTargetWriter implements DataWriter {
  private final CsvListWriter csvListWriter;

  public CsvFileTargetWriter(Path output, List<String> headers) throws IOException {
    log.info("Opening file: {}", output);

    OutputStreamWriter outputStreamWriter =
        new OutputStreamWriter(new FileOutputStream(output.toFile()));
    this.csvListWriter = new CsvListWriter(outputStreamWriter, CsvPreference.STANDARD_PREFERENCE);

    log.debug("Writing headers: {}", Strings.join(",", headers));
    writeRecord(headers);
  }

  @Override
  public void writeRecord(List<String> data) throws IOException {
    log.debug("Writing the record");
    csvListWriter.write(data);
  }

  @Override
  public void close() throws IOException {
    log.info("Closing CSV file");
    csvListWriter.close();
  }
}
