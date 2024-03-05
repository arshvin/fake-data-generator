package preved.medved.cli.converters;

import com.beust.jcommander.IStringConverter;
import com.beust.jcommander.ParameterException;
import preved.medved.generator.source.AvailableFakers;

public class InputNameToClassMapper implements IStringConverter<AvailableFakers> {
  @Override
  public AvailableFakers convert(String value) {
    try {
      return AvailableFakers.valueOf(value.toUpperCase());
    } catch (IllegalArgumentException ex) {
      throw new ParameterException(String.format("Faker name '%s' could not be found", value));
    }
  }
}
