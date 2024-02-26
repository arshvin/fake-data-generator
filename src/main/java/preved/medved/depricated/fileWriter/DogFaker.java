package preved.medved.depricated.fileWriter;

import com.github.javafaker.Dog;
import com.github.javafaker.Faker;
import java.util.Arrays;
import java.util.stream.IntStream;
import lombok.extern.log4j.Log4j2;
import preved.medved.depricated.BackgroundFetcher;

@Log4j2
public class DogFaker extends BackgroundFetcher {
  private final Dog dog;

  public DogFaker(Faker faker) {
    header =
        new String[] {
          "dog.name",
          "dog.breed",
          "dog.sound",
          "dog.meme_phrase",
          "dog.age",
          "dog.coat_length",
          "dog.gender",
          "dog.size"
        };
    dog = faker.dog();

    fetchTask =
        () -> {
          log.debug("Generating data from Runnable task");
          queue.add(
              Arrays.asList(
                  dog.name(),
                  dog.breed(),
                  dog.sound(),
                  dog.memePhrase(),
                  dog.age(),
                  dog.coatLength(),
                  dog.gender(),
                  dog.size()));
        };

    IntStream.range(0, 3)
        .forEach(
            (int i) -> {
              requestNewData();
            });
  }
}