package preved.medved.producers;

import com.github.javafaker.Book;
import com.github.javafaker.Faker;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import lombok.extern.log4j.Log4j2;
import preved.medved.BackgroundFetcher;

@Log4j2
public class BookFaker extends BackgroundFetcher implements Producer, Header {
  private Book book;

  public BookFaker(Faker faker) {
    book = faker.book();

    fetchTask =
        () -> {
          log.trace("Generating data from Runnable task");
          queue.add(Arrays.asList(book.author(), book.title(), book.publisher(), book.genre()));
        };

    IntStream.range(0, 4)
        .forEach(
            (int i) -> {
              requestNewData();
            });
  }

  @Override
  public List<String> getHeader() {
    return Arrays.asList("book.author", "book.title", "book.publisher", "book.genre");
  }

  @Override
  public void close() {
    shutdown();
  }
}
