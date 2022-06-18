package preved.medved.producers;

import java.util.Arrays;
import java.util.List;

import com.github.javafaker.Cat;
import com.github.javafaker.Faker;
import lombok.extern.log4j.Log4j2;
import preved.medved.BackgroundFetcher;

@Log4j2
public class CatFaker extends BackgroundFetcher implements Producer, Header {
  private Cat cat;

  public CatFaker(Faker faker) {
    cat = faker.cat();

    fetchTask =
        () -> {
          log.trace("Generating data from Runnable task");
          queue.add(Arrays.asList(cat.name(), cat.breed(), cat.registry()));
        };

    requestNewData();
  }

  @Override
  public List<String> getHeader() {
    return Arrays.asList("cat.name", "cat.breed", "cat.registry");
  }
}
