package preved.medved.generator.source.faikers;

import com.github.javafaker.Faker;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

@Log4j2
public class Dog extends AbstractDataProducer {
  private int bufferSize = 5;

  public Dog(ExecutorService executor) {
    super(log);
    this.executor = executor;
    this.headers =
        new String[] {
          "dog_name",
          "dog_breed",
          "dog_sound",
          "dog_meme_phrase",
          "dog_age",
          "dog_coat_length",
          "dog_gender",
          "dog_size"
        };

    this.dataFetchers.add(
        new Callable<String[]>() {
          private com.github.javafaker.Dog dog = new Faker().dog();

          @Override
          public String[] call() throws Exception {
            log.debug("Requesting of Dog name, Dog breed, Dog sound, Dog meme phrase");
            return new String[] {
              dog.name(), dog.breed(), dog.sound(), dog.memePhrase(),
            };
          }
        });
    this.dataFetchers.add(
        new Callable<String[]>() {
          private com.github.javafaker.Dog dog = new Faker().dog();

          @Override
          public String[] call() throws Exception {
            log.debug("Requesting of Dog age, Dog coat length, Dog gender, Dog size");
            return new String[] {dog.age(), dog.coatLength(), dog.gender(), dog.size()};
          }
        });

    this.fillBuffer(bufferSize);
  }
}
