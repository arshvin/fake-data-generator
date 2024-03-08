package preved.medved;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import lombok.extern.log4j.Log4j2;
import me.tongfei.progressbar.ProgressBar;
import me.tongfei.progressbar.ProgressBarBuilder;
import me.tongfei.progressbar.ProgressBarStyle;
import preved.medved.cli.DebugArgs;
import preved.medved.cli.DefaultArgs;
import preved.medved.generator.pipelines.DefaultPipeline;
import preved.medved.generator.source.DataCollector;
import preved.medved.generator.source.collectors.DefaultCollector;
import preved.medved.generator.source.faikers.Book;
import preved.medved.generator.sink.CsvFileTargetWriter;
import preved.medved.generator.sink.DataWriter;
import preved.medved.generator.sink.ParquetFileTargetWriter;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/** Application main class. */
@Log4j2
public class Application {

  public static Application INSTANCE;

  public static void main(final String[] args) {
    log.info("Starting application...");
    final DefaultArgs defaultArgs = DefaultArgs.builder().build();
    final DebugArgs debugArgs = DebugArgs.builder().build();

    final JCommander jc =
        JCommander.newBuilder().addObject(defaultArgs).programName("Fake data generator").build();

    try {
      jc.parse(args);

      if (jc.getParsedCommand() != "debug") {
        if (!(defaultArgs.isBeers()
            || defaultArgs.isCat()
            || defaultArgs.isDog()
            || defaultArgs.isBooks()
            || defaultArgs.isFinance())) {
          throw new ParameterException("At least 1 faker must be chosen");
        }

        if (!(defaultArgs.isCsvOutput() || defaultArgs.isParquetOutput())) {
          throw new ParameterException("At least 1 output file format should be chosen");
        }

        assignInstance(new Application()).run(defaultArgs);
      } else {
        new ExperimentalDataProcessor(debugArgs).run();
      }
    } catch (final ParameterException ex) {
      log.error("Error parsing arguments: {}", args, ex);
      jc.usage();
    } catch (IOException | ExecutionException | InterruptedException e) {
      e.printStackTrace();
    }
    log.info("Exited application");
  }

  protected static Application assignInstance(final Application instance) {
    if (INSTANCE == null) {
      INSTANCE = instance;
    }
    return INSTANCE;
  }

  public void run(final DefaultArgs arguments) throws IOException {
    log.info("Started application");

    Long minSizeLimit = Long.valueOf(arguments.getSizeGiBiBytes()) * 1024 * 1024 * 1024;

    DataCollector dataCollector = new DefaultCollector();
    DefaultPipeline dataPipeline = new DefaultPipeline();
    dataPipeline.setDataCollector(dataCollector);

    ExecutorService executor = Executors.newCachedThreadPool();

    if (arguments.isBooks()) {
      Book book = new Book(executor);
      dataCollector.appendSource(book);
    }

    for (int i = 0; i < arguments.getAmountFiles(); i++) {
      HashSet<DataWriter> dataWriters = new HashSet<DataWriter>();

      String uuid = UUID.randomUUID().toString();

      if (arguments.isCsvOutput()) {
        Path fullName = Paths.get(arguments.getPath(), uuid + ".csv");
        dataWriters.add(new CsvFileTargetWriter(fullName));
      }

      if (arguments.isParquetOutput()) {
        Path fullName = Paths.get(arguments.getPath(), uuid + ".parquet");
        dataWriters.add(new ParquetFileTargetWriter(fullName));
      }

      dataPipeline.setDataWriters(dataWriters);

      Long fileSizeCounter = 0L;

      try (ProgressBar pb =
          new ProgressBarBuilder()
              .setStyle(ProgressBarStyle.COLORFUL_UNICODE_BLOCK)
              .setInitialMax(minSizeLimit)
              .setUnit(getPbUnitName(arguments), getPbUnitSize(arguments))
              .setTaskName("Fake data generation")
              .showSpeed()
              .build()) {
        while (fileSizeCounter.compareTo(minSizeLimit) < 0) {
          Integer recordLength = dataPipeline.pumpUp();
          fileSizeCounter += recordLength;

          pb.stepBy(recordLength);
        }
      }

      dataWriters.forEach(
          dataWriter -> {
            try {
              dataWriter.close();
            } catch (IOException e) {
              e.printStackTrace();
            }
          });
    }

    executor.shutdown();
    log.info("Exiting application...");
  }

  private String getPbUnitName(final DefaultArgs arguments) {
    if (arguments.getSizeGiBiBytes() < 10) {
      return "KiBytes";
    }
    if (arguments.getSizeGiBiBytes() < 100) {
      return "MiBytes";
    }
    return "GiBytes";
  }

  private Long getPbUnitSize(final DefaultArgs arguments) {
    if (arguments.getSizeGiBiBytes() < 10) {
      return 1024L;
    }
    if (arguments.getSizeGiBiBytes() < 100) {
      return (long) (1024 * 1024);
    }
    return (long) (1024 * 1024 * 1024);
  }
}
