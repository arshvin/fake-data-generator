package preved.medved.cli;

import com.beust.jcommander.IDefaultProvider;

public class DefaultArgsProvider implements IDefaultProvider {
  /**
   * @param optionName The name of the option as specified in the names() attribute of
   *     the @Parameter option (e.g. "-file").
   * @return the default value for this option.
   */
  @Override
  public String getDefaultValueFor(String optionName) {

    switch (optionName) {
      case "--amount-files":
        return "1";
      case "--file-size":
        return "10";
      case "--thread-pool-type":
        return "CachedThread";
      default:
        return null;
    }
  }
}
