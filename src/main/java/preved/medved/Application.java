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

  public static Application INSTANCE;

  public static void main(final String[] args) {
    final DefaultArgs defaultArgs = DefaultArgs.builder().build();

    final JCommander jc =
        JCommander.newBuilder().addObject(defaultArgs).programName("Fake data generator").build();

    try {
      commander.parse(args);

//        if (!(defaultArgs.isBeers()
//            || defaultArgs.isCat()
//            || defaultArgs.isDog()
//            || defaultArgs.isBooks()
//            || defaultArgs.isFinance())) {
//          throw new ParameterException("At least 1 faker must be chosen");
//        }

        assignInstance(new Application()).run(defaultArgs);

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

  public void run(final CommandLineArguments arguments)
      throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException,
          IllegalAccessException {
    log.info("Started application");

    new FakeDataGenerator(arguments).process();

    DataCollector dataCollector = new DefaultCollector();
    DefaultPipeline dataPipeline = new DefaultPipeline();
    dataPipeline.setDataCollector(dataCollector);

    ExecutorService executor = Executors.newCachedThreadPool();

    arguments.getFakers().forEach(new Consumer<AvailableFakers>() {
      @SneakyThrows
      @Override
      public void accept(AvailableFakers faker) {
        dataCollector.appendSource(faker.instantiate(executor));
      }
    });

    List<String> headers = ((RecordDescriptor) dataCollector).retrieveHeaders();

    for (int i = 0; i < arguments.getAmountFiles(); i++) {
      HashSet<DataWriter> dataWriters = new HashSet<DataWriter>();

      String uuid = UUID.randomUUID().toString();

      if (arguments.isCsvOutput()) {
        Path fullName = Paths.get(arguments.getPath(), uuid + ".csv");
        dataWriters.add(new CsvFileTargetWriter(fullName, headers));

        log.info("Record column headers: {}", Strings.join(", ", headers));
      }

      if (arguments.isParquetOutput()) {
        Path fullName = Paths.get(arguments.getPath(), uuid + ".parquet");
        dataWriters.add(new ParquetFileTargetWriter(fullName, headers));
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
}
