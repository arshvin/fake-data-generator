package preved.medved.cli.validators;

import com.beust.jcommander.IParametersValidator;
import com.beust.jcommander.ParameterException;
import java.util.Map;

public class OutputDataFormats implements IParametersValidator {
  /**
   * Validate all parameters.
   *
   * @param parameters Name-value-pairs of all parameters (e.g. "-host":"localhost").
   * @throws ParameterException Thrown if validation of the parameters fails.
   */
  @Override
  public void validate(Map<String, Object> parameters) throws ParameterException {
    if (parameters.get("--csv") == null && parameters.get("--parquet") == null) {
      throw new ParameterException(
          "At least 1 output file format should be specified: --csv or --parquet");
    }
  }
}
