package preved.medved;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;

import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * Application main class.
 */
@Log4j2
public class Application {


    public static Application INSTANCE;

    public static void main(final String[] args) {
        log.info("Starting application...");
        final CommandLineArguments arguments = CommandLineArguments.builder().build();
        final JCommander commander = JCommander.newBuilder()
                .addObject(arguments)
                .programName("Fake data generator")
                .build();

        try {
            commander.parse(args);
            assignInstance(new Application()).run(arguments);
        } catch (final ParameterException ex) {
            log.error("Error parsing arguments: {}", args, ex);
            System.err.println(ex.getMessage());
            commander.usage();
        } catch (IOException | InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
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

    public void run(final CommandLineArguments arguments) throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        log.info("Started application");

        new FakeDataGenerator(arguments).process();

        log.info("Exiting application...");
    }
}
