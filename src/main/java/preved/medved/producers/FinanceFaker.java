package preved.medved.producers;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.github.javafaker.Finance;
import com.github.javafaker.Faker;
import lombok.extern.log4j.Log4j2;
import preved.medved.BackgroundFetcher;

@Log4j2
public class FinanceFaker extends BackgroundFetcher implements Producer, Header {
  private Finance finance;

  public FinanceFaker(Faker faker) {
    finance = faker.finance();

    fetchTask =
        () -> {
          log.trace("Generating data from Runnable task");
          queue.add(Arrays.asList(finance.bic(), finance.creditCard(), finance.iban()));
        };

      IntStream.range(0,30).forEach((int i) -> {
          requestNewData();
      });
  }

  @Override
  public List<String> getHeader() {
    return Arrays.asList("finance.bic", "finance.creaditCard", "finance.iban");
  }

    @Override
    public void close() {
        shutdown();
    }
}
