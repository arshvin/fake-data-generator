package preved.medved.csv;

import java.util.Arrays;
import java.util.List;

import com.github.javafaker.Cat;
import com.github.javafaker.Faker;

public class CatFaker implements CsvFaker {
    private Cat cat;

    public CatFaker(Faker faker) {
        cat = faker.cat();
    }

    public List<String> produceData() {
        List<String> items = Arrays.asList(
            cat.name(),
            cat.breed(),
            cat.registry());

        return items;
    }

}
