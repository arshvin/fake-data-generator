package preved.medved;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ExecutionException;

import lombok.extern.log4j.Log4j2;
import preved.medved.cli.DefaultArgs;
import preved.medved.cli.DebugArgs;
import preved.medved.depricated.FakeDataGenerator;

/** Application main class. */
@Log4j2
public class Application {

  public static Application INSTANCE;

  public static void main(final String[] args) {
    log.info("Starting application...");
    final DefaultArgs defaultArgs = DefaultArgs.builder().build();
    final DebugArgs debugArgs = DebugArgs.builder().build();

    final JCommander jc =
        JCommander.newBuilder().
                addObject(defaultArgs).
                addCommand("debug", debugArgs).
                programName("Fake data generator").build();

    try {
      jc.parse(args);

      if (jc.getParsedCommand() != "debug") {
        if (!(defaultArgs.isBeers()
            || defaultArgs.isCat()
            || defaultArgs.isDog()
            || defaultArgs.isBooks()
            || defaultArgs.isFinance())) {
          throw new ParameterException("At least 1 faker must be chosen");
        }
        assignInstance(new Application()).run(defaultArgs);
      } else {
        new ExperimentalDataProcessor(debugArgs).run();
      }
    } catch (final ParameterException ex) {
      log.error("Error parsing arguments: {}", args, ex);
      jc.usage();
    } catch (IOException | InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException | ExecutionException | InterruptedException e) {
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

  public void run(final DefaultArgs arguments)
      throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException,
          IllegalAccessException {
    log.info("Started application");

    new FakeDataGenerator(arguments).process();

    log.info("Exiting application...");
  }
}
