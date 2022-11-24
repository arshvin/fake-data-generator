package preved.medved;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import lombok.extern.log4j.Log4j2;
import preved.medved.depricated.FakeDataGenerator;

/** Application main class. */
@Log4j2
public class Application {

  public static Application INSTANCE;

  public static void main(final String[] args) {
    log.info("Starting application...");
    final CliArgs opts = CliArgs.builder().build();
    final JCommander jc =
        JCommander.newBuilder().addObject(opts).programName("Fake data generator").build();

    try {
      jc.parse(args);

      if (!(opts.isBeers() || opts.isCat() || opts.isDog() || opts.isBooks() || opts.isFinance())) {
        throw new ParameterException("At least 1 faker must be chosen");
      }

      assignInstance(new Application()).run(opts);
    } catch (final ParameterException ex) {
      log.error("Error parsing arguments: {}", args, ex);
      jc.usage();
    } catch (IOException
        | InvocationTargetException
        | NoSuchMethodException
        | InstantiationException
        | IllegalAccessException e) {
      e.printStackTrace();
    }
    log.info("Exited application");
  }

  protected static Application assignInstance(final Application instance) {
    if (INSTANCE == null) {
      INSTANCE = instance;
    }
    return INSTANCE;
  }

  public void run(final CliArgs arguments)
      throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException,
          IllegalAccessException {
    log.info("Started application");

    new FakeDataGenerator(arguments).process();

    log.info("Exiting application...");
  }
}
