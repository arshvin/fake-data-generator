package preved.medved.producers;

import java.util.Arrays;
import java.util.List;

import com.github.javafaker.Cat;
import com.github.javafaker.Faker;

public class CatFaker implements Producer, Header {
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

    @Override
    public List<String> getHeader() {
        return Arrays.asList(
                "cat.name",
                "cat.breed",
                "cat.registry");
    }

}
