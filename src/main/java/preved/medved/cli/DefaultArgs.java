package preved.medved.cli;

import com.beust.jcommander.Parameter;
import lombok.Builder;
import lombok.Data;

/** Command line arguments passed to the application. */
@Builder(toBuilder = true)
@Data
public class DefaultArgs {
  @Parameter(
      names = {"--destination-folder", "-p"},
      description = "Where put the files")
  private String path = ".";

  @Parameter(
      names = {"--amount-files", "-n"},
      description = "Amount of data files for generating")
  private Integer amountFiles = 1;

  @Parameter(
      names = {"--file-size", "-s"},
      description = "Minimum size of data file for generating (in GiB)")
  private Integer sizeGiBiBytes = 1;

  @Parameter(names = "--book-faker", description = "Book faker will be used")
  private boolean books;

  @Parameter(names = "--beer-faker", description = "Beer faker will be used")
  private boolean beers;

  @Parameter(names = "--cat-faker", description = "Cat faker will be used")
  private boolean cat;

  @Parameter(names = "--dog-faker", description = "Dog faker will be used")
  private boolean dog;

  @Parameter(names = "--finance-faker", description = "Finance faker will be used")
  private boolean finance;

  @Parameter(names = "--csv", description = "CSV format will be used as output")
  private boolean csvOutput;

  @Parameter(names = "--parquet", description = "Parquet format will be used as output")
  private boolean parquetOutput;
}
