package preved.medved.generator.source.faikers;

import com.github.javafaker.Faker;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import preved.medved.generator.source.ColumnHeader;
import preved.medved.generator.source.DataProducer;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Log4j2
public class Book extends AbstractFaiker implements DataProducer, ColumnHeader {
  private int bufferSize = 10;
  private com.github.javafaker.Book book = new Faker().book();

  private BlockingQueue<String> author = new LinkedBlockingQueue<>();
  private BlockingQueue<String> title = new LinkedBlockingQueue<>();
  private BlockingQueue<String> publisher = new LinkedBlockingQueue<>();
  private BlockingQueue<String> genre = new LinkedBlockingQueue<>();

  public Book(ExecutorService executor) {
    this.executor = executor;

    this.dataFetchers.add(
        new Runnable() {
          @SneakyThrows(InterruptedException.class)
          @Override
          public void run() {
            log.debug("Author queue size before element inserting: {}", author.size());
            author.put(book.author());
          }
        });

    this.dataFetchers.add(
        new Runnable() {
          @SneakyThrows(InterruptedException.class)
          @Override
          public void run() {
            log.debug("Title queue size before element inserting: {}", title.size());
            title.put(book.title());
          }
        });

    this.dataFetchers.add(
        new Runnable() {
          @SneakyThrows(InterruptedException.class)
          @Override
          public void run() {
            log.debug("Publisher queue size before element inserting: {}", publisher.size());
            publisher.put(book.publisher());
          }
        });

    this.dataFetchers.add(
        new Runnable() {
          @SneakyThrows(InterruptedException.class)
          @Override
          public void run() {
            log.debug("Genre queue size before element inserting: {}", publisher.size());
            genre.put(book.genre());
          }
        });

    this.fillBuffer(bufferSize);
    log.debug("Waiting until the buffer is filled");
    while (author.size() < bufferSize ){
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
  }

  @Override
  public List<String> getHeaders() {
    return null;
  }

  @Override
  public List<String> produce() {
    this.generateData();
    return Stream.of(author, title, publisher, genre)
        .map(
            queue -> {
              try {
                log.debug("Queue size before element getting: {}", queue.size());
                return queue.take();
              } catch (InterruptedException e) {
                e.printStackTrace();
              }
              return null;
            })
        .collect(Collectors.toList());
  }
}
