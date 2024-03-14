package preved.medved.generator.source.faikers;

import com.github.javafaker.Faker;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class Book extends AbstractDataProducer {
  private int bufferSize = 5;

  public Book(ExecutorService executor) {
    super(log);
    this.executor = executor;
    this.headers = new String[] {"book_author", "book_title", "book_publisher", "book_genre"};

    this.dataFetchers.add(
        new Callable<String[]>() {
          private com.github.javafaker.Book book = new Faker().book();

          @Override
          public String[] call() throws Exception {
            log.debug("Requesting of Book Author");
            return new String[] {this.book.author()};
          }
        });
    this.dataFetchers.add(
        new Callable<String[]>() {
          private com.github.javafaker.Book book = new Faker().book();

          @Override
          public String[] call() throws Exception {
            log.debug("Requesting of Book Title, Book Publisher, Book Genre");
            return new String[] {book.title(), book.publisher(), book.genre()};
          }
        });

    this.fillBuffer(bufferSize);
  }
}
