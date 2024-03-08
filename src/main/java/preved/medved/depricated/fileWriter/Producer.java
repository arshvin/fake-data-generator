package preved.medved.depricated.fileWriter;

import java.util.List;

public interface Producer {
  List<String> produceData();

  void close();
}
