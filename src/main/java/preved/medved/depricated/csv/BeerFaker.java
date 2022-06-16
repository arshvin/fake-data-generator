package preved.medved.csv;

import java.util.Arrays;
import java.util.List;

import com.github.javafaker.Beer;
import com.github.javafaker.Faker;

public class BeerFaker implements CsvFaker {
    private Beer beer;

    public BeerFaker(Faker faker) {
        beer = faker.beer();
    }

    public List<String> produceData() {
        List<String> items = Arrays.asList(
                beer.name(),
                beer.style(),
                beer.hop(),
                beer.yeast(),
                beer.malt());

        return items;
    }

}
