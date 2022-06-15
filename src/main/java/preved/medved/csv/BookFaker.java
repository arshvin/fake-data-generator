package preved.medved.csv;

import java.util.Arrays;
import java.util.List;

import com.github.javafaker.Book;
import com.github.javafaker.Faker;

public class BookFaker implements CsvFaker {
    private Book book;

    public BookFaker(Faker faker) {
        book = faker.book();
    }

    public List<String> produceData() {
        List<String> items = Arrays.asList(
                book.author(),
                book.title(),
                book.publisher(),
                book.genre());

        return items;
    }

}
