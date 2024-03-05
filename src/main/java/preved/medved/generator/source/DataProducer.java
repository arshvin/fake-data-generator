package preved.medved.generator.source;

import java.util.List;

/** This interface is intended for data miners, or actual {@link com.github.javafaker.Faker} instance wrapper */
public interface DataProducer {
    List<String> produce();
}
