package preved.medved.depricated.datasource;

import java.util.List;

public interface DataRetriever {
  List<String> retrieveData();
  void addFaker(FakerType faker);

  void terminate();
}
