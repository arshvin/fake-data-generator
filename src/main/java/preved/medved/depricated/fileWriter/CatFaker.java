package preved.medved.depricated.fileWriter;

import com.github.javafaker.Cat;
import com.github.javafaker.Faker;
import java.util.Arrays;
import java.util.stream.IntStream;
import lombok.extern.log4j.Log4j2;
import preved.medved.depricated.BackgroundFetcher;

@Log4j2
public class CatFaker extends BackgroundFetcher {
  private final Cat cat;

  public CatFaker(Faker faker) {
    header = new String[] {"cat.name", "cat.breed", "cat.registry"};
    cat = faker.cat();

    fetchTask =
        () -> {
          log.debug("Generating data from Runnable task");
          queue.add(Arrays.asList(cat.name(), cat.breed(), cat.registry()));
        };

    IntStream.range(0, 2)
        .forEach(
            (int i) -> {
              requestNewData();
            });
  }
}