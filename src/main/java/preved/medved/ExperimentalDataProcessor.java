package preved.medved;

import com.beust.jcommander.Strings;
import com.github.javafaker.Book;
import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import me.tongfei.progressbar.ProgressBar;
import me.tongfei.progressbar.ProgressBarBuilder;
import me.tongfei.progressbar.ProgressBarStyle;
import preved.medved.cli.DebugArgs;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.concurrent.*;

@Log4j2
@RequiredArgsConstructor
public class ExperimentalDataProcessor {
  private final DebugArgs debugArgs;

  void run() throws IOException, ExecutionException, InterruptedException {
    Faker faker = Faker.instance();

    Path fullName = Paths.get(debugArgs.getPath(), UUID.randomUUID().toString() + ".csv");
    OutputStreamWriter output = new OutputStreamWriter(new FileOutputStream(fullName.toFile()));

    Book dataSource = faker.book();

    if (!debugArgs.isUseConcurrency()) {
      log.info("Sequential execution of task");
      try (ProgressBar pb =
          new ProgressBarBuilder()
              .setStyle(ProgressBarStyle.COLORFUL_UNICODE_BLOCK)
              .setInitialMax(debugArgs.getAmountOfRecords())
              .setTaskName("Fake data generation")
              .showSpeed()
              .build()) {
        for (long i = 0; i < debugArgs.getAmountOfRecords(); i++) {
          String record =
              Strings.join(
                  ",",
                  new String[] {
                    String.format("author:%s", dataSource.author()),
                    String.format("title:%s", dataSource.title()),
                    String.format("publisher:%s", dataSource.publisher()),
                    String.format("genre:%s", dataSource.genre()),
                    "\n"
                  });

          output.write(record);

          pb.stepTo(i);
        }
      }
    } else {
      log.info("Concurrent execution of tasks");
      ExecutorService executor = Executors.newCachedThreadPool();
//      Queue<String> queue = new ConcurrentLinkedQueue<String>();
      BlockingQueue<String> authorQueue = new LinkedBlockingQueue<>();
        BlockingQueue<String> titleQueue = new LinkedBlockingQueue<>();
        BlockingQueue<String> publisherQueue = new LinkedBlockingQueue<>();
        BlockingQueue<String> genreQueue = new LinkedBlockingQueue<>();

      Callable author =
          new Callable() {
            @Override
            public String call() throws Exception {
              String value = String.format("author:%s", dataSource.author());
                authorQueue.add(value);
              return value;
            }
          };
      Callable title =
          new Callable() {
            @Override
            public String call() throws Exception {
              String value = String.format("title:%s", dataSource.title());
                titleQueue.add(value);
              return value;
            }
          };
      Callable publisher =
          new Callable() {
            @Override
            public String call() throws Exception {
              String value = String.format("publisher:%s", dataSource.publisher());
                publisherQueue.add(value);
              return value;
            }
          };
      Callable genre =
          new Callable() {
            @Override
            public String call() throws Exception {
              String value = String.format("genre:%s", dataSource.genre());
                genreQueue.add(value);
              return value;
            }
          };

      for (long i = 0; i < 3; i++) {
        executor.submit(author);
        executor.submit(title);
        executor.submit(publisher);
        executor.submit(genre);
      }

      try (ProgressBar pb =
          new ProgressBarBuilder()
              .setStyle(ProgressBarStyle.COLORFUL_UNICODE_BLOCK)
              .setInitialMax(debugArgs.getAmountOfRecords())
              .setTaskName("Fake data generation")
              .showSpeed()
              .build()) {
        for (long i = 0; i < debugArgs.getAmountOfRecords(); i++) {
          executor.submit(author);
          executor.submit(title);
          executor.submit(publisher);
          executor.submit(genre);

          String record =
              Strings.join(
                  ",", new String[] {
                          authorQueue.take(), titleQueue.take(), publisherQueue.take(), genreQueue.take(), "\n"});
          //          new String[] {
          //                    String.valueOf(authorF.get()),
          //                    String.valueOf(titleF.get()),
          //                    String.valueOf(publisherF.get()),
          //                    String.valueOf(genreF.get()),
          //                    "\n"
          //                  });

          output.write(record);

          pb.stepTo(i);
        }
      }
      executor.shutdown();
    }

    output.close();
  }
}
