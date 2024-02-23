package preved.medved.depricated;

import com.beust.jcommander.Strings;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import lombok.extern.log4j.Log4j2;
import me.tongfei.progressbar.ProgressBar;
import me.tongfei.progressbar.ProgressBarBuilder;
import me.tongfei.progressbar.ProgressBarStyle;
import org.apache.commons.io.FileUtils;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvListWriter;
import org.supercsv.prefs.CsvPreference;
import preved.medved.cli.DefaultArgs;
import preved.medved.depricated.csv.FileFormatter;

@Log4j2
public class FakeDataGenerator {
  private final DefaultArgs arguments;

  public FakeDataGenerator(DefaultArgs arguments) {
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
    String minSizeLimitHuman = FileUtils.byteCountToDisplaySize(minSizeLimit);

    for (int i = 0; i < arguments.getAmountFiles(); i++) {
      Long fileSizeCounter = 0L;

      String filePath = getTargetFileName().toFile().toString();
      log.info("Writing data to file: {}", filePath);

      CsvListWriter targetFileWriter =
          new CsvListWriter(new FileWriter(filePath), CsvPreference.STANDARD_PREFERENCE);
      targetFileWriter.writeHeader(header);

      fileSizeCounter += (long) Strings.join(",", header).length();

      try (ProgressBar pb2 =
          new ProgressBarBuilder()
              .setStyle(ProgressBarStyle.COLORFUL_UNICODE_BLOCK)
              .setUnit(getPbUnitName(), getPbUnitSize())
              .setInitialMax(minSizeLimit)
              .setTaskName("Fake data generation")
              .showSpeed()
              .build()) {
        while (fileSizeCounter.compareTo(minSizeLimit) < 0) {
          List<String> currentLine = fileFormatter.produceData();
          targetFileWriter.write(currentLine, cellProcessor);

          fileSizeCounter += (long) Strings.join(",", currentLine).length();
          pb2.stepTo(fileSizeCounter);
        }
      }

      targetFileWriter.close();
      log.info("Closed file: {}", filePath);
    }
  }

  private Path getTargetFileName() {
    String basename = UUID.randomUUID() + ".csv";
    return Paths.get(arguments.getPath(), basename);
  }

  private String getPbUnitName() {
    if (arguments.getSizeGiBiBytes() < 10) {
      return "KiBytes";
    }
    if (arguments.getSizeGiBiBytes() < 100) {
      return "MiBytes";
    }
    return "GiBytes";
  }

  private Long getPbUnitSize() {
    if (arguments.getSizeGiBiBytes() < 10) {
      return 1024L;
    }
    if (arguments.getSizeGiBiBytes() < 100) {
      return (long) (1024 * 1024);
    }
    return (long) (1024 * 1024 * 1024);
  }
}
