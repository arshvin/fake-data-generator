package preved.medved.producers;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import com.github.javafaker.Dog;
import com.github.javafaker.Faker;
import lombok.extern.log4j.Log4j2;
import preved.medved.BackgroundFetcher;

@Log4j2
public class DogFaker extends BackgroundFetcher implements Producer, Header {
  private Dog dog;

  public DogFaker(Faker faker) {
    dog = faker.dog();

    fetchTask =
        () -> {
          log.trace("Generating data from Runnable task");
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

      IntStream.range(0,3).forEach((int i) -> {
          requestNewData();
      });
  }

  @Override
  public List<String> getHeader() {
    return Arrays.asList(
        "dog.name",
        "dog.breed",
        "dog.sound",
        "dog.meme_phrase",
        "dog.age",
        "dog.coat_length",
        "dog.gender",
        "dog.size");
  }

    @Override
    public void close() {
        shutdown();
    }
}
