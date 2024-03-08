package preved.medved.cli;

import com.beust.jcommander.Parameter;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DebugArgs {
  //    @Parameter(names = "--book-faker", description = "Book faker will be used")
  //    private boolean books;

  //    @Parameter(names = "--beer-faker", description = "Beer faker will be used")
  //    private boolean beers;
  //
  //    @Parameter(names = "--cat-faker", description = "Cat faker will be used")
  //    private boolean cat;
  //
  //    @Parameter(names = "--dog-faker", description = "Dog faker will be used")
  //    private boolean dog;
  //
  //    @Parameter(names = "--finance-faker", description = "Finance faker will be used")
  //    private boolean finance;

  @Parameter(
      names = {"--destination-folder", "-p"},
      description = "Where put the files")
  private String path = ".";

  @Parameter(
      names = {"--records-amount", "-n"},
      description = "Amount of generated records before stop")
  private long amountOfRecords;

  @Parameter(names = "--concurrency", description = "Whether use of concurrency ")
  private boolean useConcurrency;
}
