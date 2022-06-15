package preved.medved.csv;

import java.util.Arrays;
import java.util.List;

import com.github.javafaker.Finance;
import com.github.javafaker.Faker;

public class FinanceFaker implements CsvFaker {
    private Finance finance;

    public FinanceFaker(Faker faker) {
        finance = faker.finance();
    }

    public List<String> produceData() {
        List<String> items = Arrays.asList(
                finance.bic(),
                finance.creditCard(),
                finance.iban());

        return items;
    }

}
