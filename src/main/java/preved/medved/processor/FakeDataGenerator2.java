package preved.medved.processor;

import lombok.RequiredArgsConstructor;
import preved.medved.CliArgs;
import preved.medved.datasource.DataRetriever;
import preved.medved.datasource.DefaultDataRetriever;
import preved.medved.datasource.FakerType;

import java.util.LinkedHashSet;

@RequiredArgsConstructor
public class FakeDataGenerator2 {
  private final CliArgs opts;
    private DataRetriever dataRetriever;


    public void process() {
    processFakersRelatedArgs();

  }

  private void processTargetRelatedArgs(){

  }

  private void processFakersRelatedArgs() {
    dataRetriever = new DefaultDataRetriever();

    if (opts.isBeers()) {
      dataRetriever.addFaker(FakerType.BEER);
    }
    if (opts.isBooks()) {
      dataRetriever.addFaker(FakerType.BOOK);
    }
    if (opts.isDog()) {
      dataRetriever.addFaker(FakerType.DOG);
    }
    if (opts.isCat()) {
      dataRetriever.addFaker(FakerType.CAT);
    }
    if (opts.isFinance()) {
      dataRetriever.addFaker(FakerType.FINANCE);
    }
  }
}
