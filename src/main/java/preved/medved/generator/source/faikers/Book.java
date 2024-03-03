package preved.medved.generator.source.faikers;

import com.github.javafaker.Faker;
import lombok.extern.log4j.Log4j2;
import preved.medved.generator.source.ColumnHeader;
import preved.medved.generator.source.DataProducer;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Log4j2
public class Book extends AbstractFaiker implements DataProducer, ColumnHeader {
  private int bufferSize = 10;
  private com.github.javafaker.Book book = new Faker().book();

  public Book(ExecutorService executor) {
    this.executor = executor;

    this.dataFetchers.add(
        new Callable<String>() {
          @Override
          public String call() throws Exception {
            return book.author();
          }
        });
    this.dataFetchers.add(
        new Callable<String>() {
          @Override
          public String call() throws Exception {
            return book.title();
          }
        });
    this.dataFetchers.add(
        new Callable<String>() {
          @Override
          public String call() throws Exception {
            return book.publisher();
          }
        });
    this.dataFetchers.add(
        new Callable<String>() {
          @Override
          public String call() throws Exception {
            return book.genre();
          }
        });

    this.fillBuffer(bufferSize);
  }

  @Override
  public List<String> getHeaders() {
    return null;
  }
}
