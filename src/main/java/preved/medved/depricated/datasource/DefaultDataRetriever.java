package preved.medved.depricated.datasource;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.List;

@Log4j2
@RequiredArgsConstructor
public class DefaultDataRetriever implements Metadata, DataRetriever {
//  private final Set<FakerType> fakers;

  @Override
  public List<String> retrieveData() {
    return null;
  }

  @Override
  public void addFaker(FakerType faker) {

  }


  @Override
  public void terminate() {

  }

  @Override
  public List<String> retrieveHeader() {
    return null;
  }
}
