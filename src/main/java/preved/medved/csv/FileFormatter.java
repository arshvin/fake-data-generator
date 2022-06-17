package preved.medved.csv;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import com.beust.jcommander.Strings;
import lombok.extern.log4j.Log4j2;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;

import com.github.javafaker.Faker;

import lombok.Getter;
import preved.medved.producers.BeerFaker;
import preved.medved.producers.BookFaker;
import preved.medved.producers.CatFaker;
import preved.medved.producers.DogFaker;
import preved.medved.producers.FinanceFaker;
import preved.medved.producers.Header;
import preved.medved.producers.Producer;

@Log4j2
public class FileFormatter {

  private LinkedHashMap<Class<Producer>, Producer> fakerRegistry = new LinkedHashMap<>();

  @Getter private ArrayList<String> headers = new ArrayList<>();

  @Getter private ArrayList<CellProcessor> cellProcessors = new ArrayList<>();
  private Faker fakerInstance = new Faker();

  private void addFaker(Class faker)
      throws NoSuchMethodException, SecurityException, InstantiationException,
          IllegalAccessException, IllegalArgumentException, InvocationTargetException {

    if (!fakerRegistry.keySet().contains(faker)) {
      Class[] parameterType = {Faker.class};
      Constructor<Producer> constructor = faker.getConstructor(parameterType);
      Object[] obj = { fakerInstance};
      Producer instance = constructor.newInstance(obj);

      List<String> header = ((Header) instance).getHeader();

      log.info("Adding column names: {}", Strings.join("|", header));

      headers.addAll(header);
      fakerRegistry.put(faker, instance);
    }
  }

  public FileFormatter addBeer()
      throws InvocationTargetException, NoSuchMethodException, InstantiationException,
          IllegalAccessException {
    addFaker(BeerFaker.class);
    return this;
  }

  public FileFormatter addBook()
      throws InvocationTargetException, NoSuchMethodException, InstantiationException,
          IllegalAccessException {
    addFaker(BookFaker.class);
    return this;
  }

  public FileFormatter addCat()
      throws InvocationTargetException, NoSuchMethodException, InstantiationException,
          IllegalAccessException {
    addFaker(CatFaker.class);
    return this;
  }

  public FileFormatter addDog()
      throws InvocationTargetException, NoSuchMethodException, InstantiationException,
          IllegalAccessException {
    addFaker(DogFaker.class);

    return this;
  }

  public FileFormatter addFinance()
      throws InvocationTargetException, NoSuchMethodException, InstantiationException,
          IllegalAccessException {
    addFaker(FinanceFaker.class);
    return this;
  }

  public FileFormatter build() {
    if (headers.size() == 0) {
      throw new RuntimeException("Headers list should not be empty!");
    }
    headers.forEach((i) -> cellProcessors.add(new NotNull()));
    return this;
  }

  public List<String> produceData() {
    if (cellProcessors.size() == 0) {
      throw new RuntimeException(
          "Perhaps the execution of build() method in client code has been missed");
    }

    List<String> record = new ArrayList<>();
    for (Producer item : fakerRegistry.values()) {
      record.addAll(item.produceData());
    }

    return record;
  }
}
