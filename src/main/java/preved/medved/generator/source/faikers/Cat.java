package preved.medved.generator.source.faikers;

import com.github.javafaker.Faker;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class Cat extends AbstractDataProducer {
  private int bufferSize = 5;

  public Cat(ExecutorService executor) {
    super(log);
    this.executor = executor;
    this.headers = new String[] {"cat_name", "cat_breed", "cat_registry"};

    this.dataFetchers.add(
        new Callable<String[]>() {
          private com.github.javafaker.Cat cat = new Faker().cat();

          @Override
          public String[] call() throws Exception {
            log.debug("Requesting of Cat name, Cat breed, Cat registry");
            return new String[] {cat.name(), cat.breed(), cat.registry()};
          }
        });

    this.fillBuffer(bufferSize);
  }
}
