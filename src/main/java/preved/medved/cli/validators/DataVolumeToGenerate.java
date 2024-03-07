package preved.medved.cli.validators;

import com.beust.jcommander.IParametersValidator;
import com.beust.jcommander.ParameterException;

import java.util.Map;

public class DataVolumeToGenerate implements IParametersValidator {
    /**
     * Validate all parameters.
     *
     * @param parameters Name-value-pairs of all parameters (e.g. "-host":"localhost").
     * @throws ParameterException Thrown if validation of the parameters fails.
     */
    @Override
    public void validate(Map<String, Object> parameters) throws ParameterException {
        if (parameters.get("--file-size") == null
                || parameters.get("--amount-files") == null) {
            throw new ParameterException(
                    "Nothing to do. It's required to specify correct values with --file-size and --amount-files");
        }
    }
}
