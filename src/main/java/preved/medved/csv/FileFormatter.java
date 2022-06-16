package preved.medved.csv;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;

import com.github.javafaker.Book;
import com.github.javafaker.Faker;
import com.google.common.collect.ImmutableMap;

import lombok.Data;
import lombok.Getter;

public class FileFormatter {
    private boolean books;
    private boolean beer;
    private boolean cat;
    private boolean dog;
    private boolean finance;

    private List<Object> fakerOrdrer = new ArrayList<>();  

    @Getter
    private ArrayList<String> headers = new ArrayList<>();

    @Getter
    private ArrayList<CellProcessor> cellProcessors = new ArrayList<>();

    public FileFormatter addBook() {
        if (!books) {
            this.headers.add("book.author");
            this.headers.add("book.title");
            this.headers.add("book.publisher");
            this.headers.add("book.genre");

            fakerOrdrer.add(getBook)

            books = true;
        }

        return this;
    }

    public FileFormatter addBeer() {
        if (!beer) {
            this.headers.add("beer.name");
            this.headers.add("beer.style");
            this.headers.add("beer.hop");
            this.headers.add("beer.yeast");
            this.headers.add("beer.malt");
            
            beer = true;
        }

        return this;
    }

    public FileFormatter addCat() {
        if (!cat) {
            this.headers.add("cat.name");
            this.headers.add("cat.breed");
            this.headers.add("cat.registry");
            
            cat = true;
        }

        return this;
    }

    public FileFormatter addFinance() {
        if (!finance) {
            this.headers.add("finance.bic");
            this.headers.add("finance.creaditCard");
            this.headers.add("finance.iban");
            
            finance = true;
        }

        return this;
    }

    public FileFormatter addDog() {
        if (!dog) {
            this.headers.add("dog.name");
            this.headers.add("dog.breed");
            this.headers.add("dog.sound");
            this.headers.add("dog.meme_phrase");
            this.headers.add("dog.age");
            this.headers.add("dog.coat_length");
            this.headers.add("dog.gender");
            this.headers.add("dog.size");
            
            dog = true;
        }

        return this;
    }

    public FileFormatter build() {
        headers.forEach((i) -> cellProcessors.add(new NotNull()));

        return this;
    }

    public List<Object> produceData() {
        if (cellProcessors.size() == 0){
            throw ExceptionInInitializerError
        }
        return null;
    }
}
