package preved.medved.cli;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import lombok.Builder;
import lombok.Data;
import preved.medved.cli.validators.DataVolumeToGenerate;
import preved.medved.cli.validators.FakerInputList;
import preved.medved.cli.validators.OutputDataFormats;

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
      description = "Where put the files",
      order = 1)
  private String path;

  @Parameter(
      names = {"--file-size", "-s"},
      description = "Minimum size of data file for generating (in MiB)",
      required = true,
      order = 2)
  private int sizeMiBiBytes;

  @Parameter(
      names = {"--amount-files", "-n"},
      description = "Amount of data files for generating",
      required = true,
      order = 3)
  private int amountFiles;

  @Parameter(names = "--help", description = "Shows this help message", help = true)
  private boolean help;
}
