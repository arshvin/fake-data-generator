package preved.medved.csv;

import java.util.Arrays;
import java.util.List;

import com.github.javafaker.Beer;
import com.github.javafaker.Book;
import com.github.javafaker.Cat;
import com.github.javafaker.Dog;
import com.github.javafaker.Faker;
import com.github.javafaker.Finance;

public class DataFaker {
    private static Faker faker;

    private static List<Object> getBook() {
        Book book = faker.book();

        List<Object> items = Arrays.asList(
                book.author(),
                book.title(),
                book.publisher(),
                book.genre());

        return items;
    }

    private static List<Object> getBeer() {
        Beer beer = faker.beer();

        List<Object> items = Arrays.asList(
                beer.name(),
                beer.style(),
                beer.hop(),
                beer.yeast(),
                beer.malt());

        return items;
    }

    private static List<Object> getCat() {
        Cat cat = faker.cat();

        List<Object> items = Arrays.asList(
                cat.name(),
                cat.breed(),
                cat.registry());

        return items;
    }

    private static List<Object> getDog() {
        Dog dog = faker.dog();

        List<Object> items = Arrays.asList(
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

    private static List<Object> getFinance() {
        Finance finance = faker.finance();

        List<Object> items = Arrays.asList(
                finance.bic(),
                finance.creditCard(),
                finance.iban());

        return items;
    }
}
