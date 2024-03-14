package preved.medved.cli.converters;

import com.beust.jcommander.IStringConverter;
import com.beust.jcommander.ParameterException;
import preved.medved.generator.ThreadPoolTypes;

public class InputNameToThreadPool implements IStringConverter<ThreadPoolTypes> {
  @Override
  public ThreadPoolTypes convert(String value) {
    try {
      return ThreadPoolTypes.valueOf(value);
    } catch (IllegalArgumentException ex) {
      throw new ParameterException(
          String.format(
              "Thread pool '%s' can not be found. Available values are: CachedThread, WorkStealing,"
                  + " SingleThread",
              value));
    }
  }
}
