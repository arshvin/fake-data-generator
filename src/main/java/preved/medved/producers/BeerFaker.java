package preved.medved.producers;

import com.github.javafaker.Beer;
import com.github.javafaker.Faker;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import lombok.extern.log4j.Log4j2;
import preved.medved.BackgroundFetcher;

@Log4j2
public class BeerFaker extends BackgroundFetcher implements Producer, Header {
  private Beer beer;

  public BeerFaker(Faker faker) {
    beer = faker.beer();

    fetchTask =
        () -> {
          log.trace("Generating data from Runnable task");
          queue.add(
              Arrays.asList(beer.name(), beer.style(), beer.hop(), beer.yeast(), beer.malt()));
        };

    IntStream.range(0, 3)
        .forEach(
            (int i) -> {
              requestNewData();
            });
  }

  @Override
  public List<String> getHeader() {
    return Arrays.asList("beer.name", "beer.style", "beer.hop", "beer.yeast", "beer.malt");
  }

  @Override
  public void close() {
    shutdown();
  }
}
