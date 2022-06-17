package preved.medved.producers;

import java.util.Arrays;
import java.util.List;

import com.github.javafaker.Beer;
import com.github.javafaker.Faker;

public class BeerFaker implements Producer,Header {
    private Beer beer;

    public BeerFaker(Faker faker) {
        beer = faker.beer();
    }

    @Override
    public List<String> produceData() {
        List<String> items = Arrays.asList(
                beer.name(),
                beer.style(),
                beer.hop(),
                beer.yeast(),
                beer.malt());

        return items;
    }

    @Override
    public List<String> getHeader() {
        return Arrays.asList(
            "beer.name",
            "beer.style",
            "beer.hop",
            "beer.yeast",
            "beer.malt"
        );
    }
}
