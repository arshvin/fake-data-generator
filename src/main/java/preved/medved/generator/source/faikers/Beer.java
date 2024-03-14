package preved.medved.generator.source.faikers;

import com.github.javafaker.Faker;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class Beer extends AbstractDataProducer {
  private int bufferSize = 5;

  public Beer(ExecutorService executor) {
    super(log);
    this.executor = executor;
    this.headers = new String[] {"beer_name", "beer_style", "beer_hop", "beer_yeast", "beer_malt"};

    this.dataFetchers.add(
        new Callable<String[]>() {
          private com.github.javafaker.Beer beer = new Faker().beer();

          @Override
          public String[] call() throws Exception {
            log.debug("Requesting of Beer name, Beer style, Beer hop, Beer yeast, Beer malt");
            return new String[] {beer.name(), beer.style(), beer.hop(), beer.yeast(), beer.malt()};
          }
        });

    this.fillBuffer(bufferSize);
  }
}
