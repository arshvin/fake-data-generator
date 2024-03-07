package preved.medved.cli;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import lombok.Builder;
import lombok.Data;
import preved.medved.cli.converters.InputNameToClassMapper;
import preved.medved.cli.splitters.FakerListSplitter;
import preved.medved.cli.validators.FakerInputList;
import preved.medved.cli.validators.OutputDataFormats;
import preved.medved.generator.source.AvailableFakers;

import java.util.List;

/** Command line arguments passed to the application. */
@Builder(toBuilder = true)
@Data
@Parameters(parametersValidators = {OutputDataFormats.class, FakerInputList.class})
public class DefaultArgs {
  @Parameter(
      names = "--fakers",
      description =
          "Space- or comma-separated list of fakers which will be used in specified order. Current available list of fakers: book, beer, cat, dog, finance",
      required = true,
      converter = InputNameToClassMapper.class,
      splitter = FakerListSplitter.class,
      order = 0)
  private List<AvailableFakers> fakers;

  @Parameter(
      names = {"--destination-folder", "-p"},
      description = "Where put the files",
      order = 1)
  private String path;

  @Parameter(
      names = {"--file-size", "-s"},
      description = "Minimum size of data file for generating (in MiB)",
      order = 2)
  private int sizeMiBiBytes;

  @Parameter(
      names = {"--amount-files", "-n"},
      description = "Amount of data files for generating",
      order = 3)
  private int amountFiles;

  @Parameter(names = "--csv", description = "CSV format will be used as output", order = 4)
  private boolean csvOutput;

  @Parameter(names = "--parquet", description = "Parquet format will be used as output", order = 5)
  private boolean parquetOutput;

  @Parameter(names = "--help", description = "Shows this help message", help = true)
  private boolean help;
}
