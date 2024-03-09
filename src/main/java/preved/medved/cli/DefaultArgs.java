package preved.medved.cli;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import lombok.Builder;
import lombok.Data;
import preved.medved.cli.converters.InputNameToThreadPool;
import preved.medved.cli.validators.DataVolumeToGenerate;
import preved.medved.cli.validators.FakerInputList;
import preved.medved.cli.validators.OutputDataFormats;
import preved.medved.generator.ThreadPoolTypes;

/** Command line arguments passed to the application. */
@Builder(toBuilder = true)
@Data
@Parameters(
    parametersValidators = {
      OutputDataFormats.class,
      FakerInputList.class,
      DataVolumeToGenerate.class
    })
public class DefaultArgs {
  @Parameter(
      names = {"--destination-folder", "-p"},
      description = "Where to put of generated files",
      order = 1)
  private String path;

  @Parameter(
      names = {"--file-size", "-s"},
      description = "Minimum size of data file for generating (in MiB). Default: 10",
      order = 2)
  private int sizeMiBiBytes = 10; //

  @Parameter(
      names = {"--amount-files", "-n"},
      description = "Amount of data files for generating. Default: 1",
      order = 3)
  private int amountFiles = 1;

  @Parameter(names = "--help", description = "Shows this help message", help = true)
  private boolean help;

  @Parameter(names = "--thread-pool-type", hidden = true, converter = InputNameToThreadPool.class)
  private ThreadPoolTypes threadPoolType;
}
