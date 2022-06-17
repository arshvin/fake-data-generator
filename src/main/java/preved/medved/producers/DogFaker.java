package preved.medved.producers;

import java.util.Arrays;
import java.util.List;

import com.github.javafaker.Dog;
import com.github.javafaker.Faker;

public class DogFaker implements Producer, Header {
    private Dog dog;

    public DogFaker(Faker faker) {
        dog = faker.dog();
    }

    public List<String> produceData() {
        List<String> items = Arrays.asList(
                dog.name(),
                dog.breed(),
                dog.sound(),
                dog.memePhrase(),
                dog.age(),
                dog.coatLength(),
                dog.gender(),
                dog.size());

        return items;
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

}
