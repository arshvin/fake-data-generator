package preved.medved;

import com.beust.jcommander.Parameter;
import lombok.Builder;
import lombok.Data;

/** Command line arguments passed to the application. */
@Builder(toBuilder = true)
@Data
public class CommandLineArguments {
  @Builder.Default
  @Parameter(
      names = {"--destination-folder", "-p"},
      description = "Where put the files")
  private String path = ".";

  @Builder.Default
  @Parameter(
      names = {"--amount-files", "-n"},
      description = "Amount of data files for generating")
  private Integer amountFiles = 1;

  @Builder.Default
  @Parameter(
      names = {"--file-size", "-s"},
      description = "Minimum size of data file for generating (in GiB)")
  private Integer sizeGiBiBytes = 1;

  @Parameter(names = "--book-faker", description = "Book faker'll be used")
  private boolean books;

  @Parameter(names = "--beer-faker", description = "Beer faker'll be used")
  private boolean beers;

  @Parameter(names = "--cat-faker", description = "Cat faker'll be used")
  private boolean cat;

  @Parameter(names = "--dog-faker", description = "Dog faker'll be used")
  private boolean dog;

  @Parameter(names = "--finance-faker", description = "Finance faker'll be used")
  private boolean finance;
}
