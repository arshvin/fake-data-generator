package preved.medved.datasource;

import java.util.List;

public interface DataRetriever {
  List<String> produceData();

  void close();
}
