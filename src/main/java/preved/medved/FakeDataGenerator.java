package preved.medved;

import com.beust.jcommander.Strings;
import lombok.extern.log4j.Log4j2;
import me.tongfei.progressbar.ProgressBar;
import me.tongfei.progressbar.ProgressBarBuilder;
import me.tongfei.progressbar.ProgressBarStyle;
import org.apache.commons.io.FileUtils;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvListWriter;
import org.supercsv.prefs.CsvPreference;
import preved.medved.csv.FileFormatter;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

@Log4j2
public class FakeDataGenerator {
  private CommandLineArguments arguments;

  public FakeDataGenerator(CommandLineArguments arguments) {
    this.arguments = arguments;
  }

  public void process()
      throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException,
          IllegalAccessException {

    FileFormatter fileFormatter = new FileFormatter();

    if (arguments.isBeers()) {
      fileFormatter.addBeer();
    }
    if (arguments.isBooks()) {
      fileFormatter.addBook();
    }
    if (arguments.isCat()) {
      fileFormatter.addCat();
    }
    if (arguments.isDog()) {
      fileFormatter.addDog();
    }
    if (arguments.isFinance()) {
      fileFormatter.addFinance();
    }

    fileFormatter.build();
    final CellProcessor[] cellProcessor =
        fileFormatter.getCellProcessors().toArray(new CellProcessor[0]);
    final String[] header = fileFormatter.getHeaders().toArray(new String[0]);

    log.info("Column names are:\n{}", Strings.join("|", header));

    Long minSizeLimit = Long.valueOf(arguments.getSizeGiBiBytes()) * 1024 * 1024 * 1024;
    String minSizeLimitHuman = FileUtils.byteCountToDisplaySize(minSizeLimit).toString();

      for (int i = 0; i < arguments.getAmountFiles(); i++) {
        Long fileSizeCounter = Long.valueOf(0);

        String filePath = getTargetFileName().toFile().toString();
        log.info("Writing data to file: {}", filePath);

        CsvListWriter targetFileWriter =
            new CsvListWriter(new FileWriter(filePath), CsvPreference.STANDARD_PREFERENCE);
        targetFileWriter.writeHeader(header);

        fileSizeCounter += Long.valueOf(Strings.join(",", header).length());

        try (ProgressBar pb2 =
            new ProgressBarBuilder()
                .setStyle(ProgressBarStyle.COLORFUL_UNICODE_BLOCK)
                .setUnit("Bytes", 1)
                .setInitialMax(minSizeLimit)
                .setTaskName("Fake data generation")
                .showSpeed()
                .build()) {
          while (fileSizeCounter.compareTo(minSizeLimit) < 0) {
            List<String> currentLine = fileFormatter.produceData();
            targetFileWriter.write(currentLine, cellProcessor);

            fileSizeCounter += Long.valueOf(Strings.join(",", currentLine).length());
            pb2.stepTo(fileSizeCounter);
          }
        }

        targetFileWriter.close();
        log.info("Closed file: {}", filePath);

    }
  }

  private Path getTargetFileName() {
    String basename =
        new StringBuilder().append(UUID.randomUUID().toString()).append(".csv").toString();
    Path path = Path.of(arguments.getPath(), basename);
    return path;
  }
}
