package preved.medved.cli.validators;

import com.beust.jcommander.IParametersValidator;
import com.beust.jcommander.ParameterException;
import java.util.List;
import java.util.Map;

public class FakerInputList implements IParametersValidator {
  /**
   * Validate all parameters.
   *
   * @param parameters Name-value-pairs of all parameters (e.g. "-host":"localhost").
   * @throws ParameterException Thrown if validation of the parameters fails.
   */
  @Override
  public void validate(Map<String, Object> parameters) throws ParameterException {
    List faker = (List) parameters.get("--fakers");
    if (faker.isEmpty()) {
      throw new ParameterException("At least 1 faker should be specified in --fakers parameter");
    }
  }
}
