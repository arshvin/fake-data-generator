package preved.medved.depricated.fileWriter;

import com.github.javafaker.Faker;
import com.github.javafaker.Finance;
import java.util.Arrays;
import java.util.stream.IntStream;
import lombok.extern.log4j.Log4j2;
import preved.medved.depricated.BackgroundFetcher;

@Log4j2
public class FinanceFaker extends BackgroundFetcher {
  private Finance finance;

  public FinanceFaker(Faker faker) {
    header = new String[] {"finance.bic", "finance.creaditCard", "finance.iban"};
    finance = faker.finance();

    fetchTask =
        () -> {
          log.debug("Generating data from Runnable task");
          queue.add(Arrays.asList(finance.bic(), finance.creditCard(), finance.iban()));
        };

    IntStream.range(0, 30)
        .forEach(
            (int i) -> {
              requestNewData();
            });
  }
}
