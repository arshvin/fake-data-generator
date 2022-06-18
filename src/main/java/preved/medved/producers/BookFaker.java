package preved.medved.producers;

import java.util.Arrays;
import java.util.List;

import com.github.javafaker.Book;
import com.github.javafaker.Faker;
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

    requestNewData();
  }

  public List<String> produceData() {
    log.trace("Retrieving data");
    List<String> items = null;
    try {
      items = retrieveData();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    requestNewData();
    return items;
  }

  @Override
  public List<String> getHeader() {
    return Arrays.asList("book.author", "book.title", "book.publisher", "book.genre");
  }
}
