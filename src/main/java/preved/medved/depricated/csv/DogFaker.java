package preved.medved.csv;

import java.util.Arrays;
import java.util.List;

import com.github.javafaker.Dog;
import com.github.javafaker.Faker;

public class DogFaker implements CsvFaker {
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

}
