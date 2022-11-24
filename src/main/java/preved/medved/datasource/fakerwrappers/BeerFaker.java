package preved.medved.datasource.fakerwrappers;

import com.github.javafaker.Beer;
import com.github.javafaker.Faker;
import java.util.Arrays;
import java.util.stream.IntStream;
import lombok.extern.log4j.Log4j2;
import preved.medved.depricated.BackgroundFetcher;

@Log4j2
public class BeerFaker extends BackgroundFetcher {
  private Beer beer;

  public BeerFaker(Faker faker) {
    header = new String[] {"beer.name", "beer.style", "beer.hop", "beer.yeast", "beer.malt"};
    beer = faker.beer();

    fetchTask =
        () -> {
          log.debug("Generating data from Runnable task");
          queue.add(
              Arrays.asList(beer.name(), beer.style(), beer.hop(), beer.yeast(), beer.malt()));
        };

    IntStream.range(0, 3)
        .forEach(
            (int i) -> {
              requestNewData();
            });
  }
}
