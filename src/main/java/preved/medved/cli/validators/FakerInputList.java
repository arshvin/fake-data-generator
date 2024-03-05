package preved.medved.cli.validators;

import com.beust.jcommander.IParametersValidator;
import com.beust.jcommander.ParameterException;

import java.util.Map;
import java.util.Set;

public class FakerInputList implements IParametersValidator {
    @Override
    public void validate(Map<String, Object> parameters) throws ParameterException {
        Set faker = (Set) parameters.get("--fakers");
        if (faker.isEmpty()){
            throw new ParameterException(
                    "At least 1 faker should be specified in --fakers parameter");
        }
    }
}
