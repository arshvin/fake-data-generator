package preved.medved.generator.source.faikers;

import com.github.javafaker.Faker;
import lombok.extern.log4j.Log4j2;
import preved.medved.generator.source.DataProducer;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

@Log4j2
public class Book extends AbstractFaiker implements DataProducer, RecordDescriptor {
  private int bufferSize = 5;
  private com.github.javafaker.Book book = new Faker().book();

  public Book(ExecutorService executor) {
    super(log);
    this.executor = executor;

    this.dataFetchers.add(
        new Callable<String>() {
          @Override
          public String call() throws Exception {
            log.debug("Book author looking task was triggered");
            return book.author();
          }
        });
    this.dataFetchers.add(
        new Callable<String>() {
          @Override
          public String call() throws Exception {
            log.debug("Book title looking task was triggered");
            return book.title();
          }
        });
    this.dataFetchers.add(
        new Callable<String>() {
          @Override
          public String call() throws Exception {
            log.debug("Book publisher looking task was triggered");
            return book.publisher();
          }
        });
    this.dataFetchers.add(
        new Callable<String>() {
          @Override
          public String call() throws Exception {
            log.debug("Book genre looking task was triggered");
            return book.genre();
          }
        });

    this.fillBuffer(bufferSize);
  }

    @Override
  public List<String> retrieveHeaders() {
    return Arrays.stream(new String[] {"book_author", "book_title", "book_publisher", "book_genre"})
        .toList();
  }
}
