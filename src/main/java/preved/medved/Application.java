package preved.medved;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import com.beust.jcommander.Strings;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import me.tongfei.progressbar.ProgressBar;
import me.tongfei.progressbar.ProgressBarBuilder;
import me.tongfei.progressbar.ProgressBarStyle;
import preved.medved.cli.DefaultArgs;
import preved.medved.generator.pipelines.DefaultPipeline;
import preved.medved.generator.sink.CsvFileTargetWriter;
import preved.medved.generator.sink.DataWriter;
import preved.medved.generator.sink.ParquetFileTargetWriter;
import preved.medved.generator.source.DataCollector;
import preved.medved.generator.source.AvailableFakers;
import preved.medved.generator.source.collectors.DefaultCollector;
import preved.medved.generator.source.RecordDescriptor;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

/** Application main class. */
@Log4j2
public class Application {
  static final DefaultArgs defaultArgs = DefaultArgs.builder().build();

  public static Application INSTANCE;

  public static void main(final String[] args) {

    final JCommander jc =
        JCommander.newBuilder().addObject(defaultArgs).programName("Fake data generator").build();

    try {
      commander.parse(args);

      if (defaultArgs.isHelp()) {
        jc.usage();
        System.exit(0);
      }

      assignInstance(new Application()).run();

    } catch (final ParameterException ex) {
      log.error("Error parsing arguments: {}", args, ex);
      ex.usage();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  protected static Application assignInstance(final Application instance) {
    if (INSTANCE == null) {
      INSTANCE = instance;
    }
    return INSTANCE;
  }

  public void run() throws IOException {
    log.info("Started application");

    Long minSizeLimit = Long.valueOf(defaultArgs.getSizeMiBiBytes()) * 1024 * 1024;

    DataCollector dataCollector = new DefaultCollector();
    DefaultPipeline dataPipeline = new DefaultPipeline();
    dataPipeline.setDataCollector(dataCollector);

//    ExecutorService executor = Executors.newCachedThreadPool();  // 100/100MiBytes (0:01:16 / 0:00:00) 1.3MiBytes/s
//    ExecutorService executor = Executors.newWorkStealingPool();  // 100/100MiBytes (0:01:02 / 0:00:00) 1.6MiBytes/s
    ExecutorService executor = Executors.newSingleThreadScheduledExecutor();  // 100/100MiBytes (0:04:28 / 0:00:00) .4MiBytes/s

    defaultArgs
        .getFakers()
        .forEach(
            new Consumer<AvailableFakers>() {
              @SneakyThrows
              @Override
              public void accept(AvailableFakers faker) {
                dataCollector.appendSource(faker.instantiate(executor));
              }
            });

    List<String> headers = ((RecordDescriptor) dataCollector).retrieveHeaders();

    for (int i = 0; i < defaultArgs.getAmountFiles(); i++) {
      HashSet<DataWriter> dataWriters = new HashSet<DataWriter>();

      String uuid = UUID.randomUUID().toString();

      if (defaultArgs.isCsvOutput()) {
        Path fullName = Paths.get(defaultArgs.getPath(), uuid + ".csv");
        dataWriters.add(new CsvFileTargetWriter(fullName, headers));

        log.info("Record column headers: {}", Strings.join(", ", headers));
      }

      if (defaultArgs.isParquetOutput()) {
        Path fullName = Paths.get(defaultArgs.getPath(), uuid + ".parquet");
        dataWriters.add(new ParquetFileTargetWriter(fullName, headers));
      }

      dataPipeline.setDataWriters(dataWriters);
      Long fileSizeCounter = 0L;

      try (ProgressBar pb =
          new ProgressBarBuilder()
              .setStyle(ProgressBarStyle.COLORFUL_UNICODE_BLOCK)
              .setInitialMax(minSizeLimit)
              .setUnit(getPbUnitName(defaultArgs), getPbUnitSize(defaultArgs))
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
    if (arguments.getSizeMiBiBytes() < 10) {
      return "Bytes";
    }
    if (arguments.getSizeMiBiBytes() < 100) {
      return "KiBytes";
    }
    return "MiBytes";
  }

  private Long getPbUnitSize(final DefaultArgs arguments) {
    if (arguments.getSizeMiBiBytes() < 10) {
      return 1L;
    }
    if (arguments.getSizeMiBiBytes() < 100) {
      return (long) (1024);
    }
    return (long) (1024 * 1024);
  }
}
