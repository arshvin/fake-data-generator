package preved.medved.generator.source.faikers;

import com.github.javafaker.Faker;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class Finance extends AbstractDataProducer {
  private int bufferSize = 5;

  public Finance(ExecutorService executor) {
    super(log);
    this.executor = executor;
    this.headers = new String[] {"finance_bic", "finance_credit_card", "finance_iban"};
    this.dataFetchers.add(
        new Callable<String[]>() {
          private com.github.javafaker.Finance finance = new Faker().finance();

          @Override
          public String[] call() throws Exception {
            log.debug("Requesting of Finance BIC");
            return new String[] {
              finance.bic(),
            };
          }
        });
    this.dataFetchers.add(
        new Callable<String[]>() {
          private com.github.javafaker.Finance finance = new Faker().finance();

          @Override
          public String[] call() throws Exception {
            log.debug("Requesting of Finance credit card");
            return new String[] {
              finance.creditCard(),
            };
          }
        });
    this.dataFetchers.add(
        new Callable<String[]>() {
          private com.github.javafaker.Finance finance = new Faker().finance();

          @Override
          public String[] call() throws Exception {
            log.debug("Requesting of Finance IBAN");
            return new String[] {
              finance.iban(),
            };
          }
        });

    this.fillBuffer(bufferSize);
  }
}
