package preved.medved.producers;

import java.util.Arrays;
import java.util.List;

import com.github.javafaker.Finance;
import com.github.javafaker.Faker;

public class FinanceFaker implements Producer, Header {
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

    @Override
    public List<String> getHeader() {
        return Arrays.asList(
                "finance.bic",
                "finance.creaditCard",
                "finance.iban");
    }

}
