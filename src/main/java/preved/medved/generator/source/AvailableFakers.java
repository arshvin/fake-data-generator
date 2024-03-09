package preved.medved.generator.source;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ExecutorService;
import lombok.extern.log4j.Log4j2;
import preved.medved.generator.source.faikers.*;

@Log4j2
public enum AvailableFakers {
  BOOK {
    @Override
    protected Class getClazz() {
      return Book.class;
    }
  },
  BEER {
    @Override
    protected Class getClazz() {
      return Beer.class;
    }
  },
  CAT {
    @Override
    protected Class getClazz() {
      return Cat.class;
    }
  },
  DOG {
    @Override
    protected Class getClazz() {
      return Dog.class;
    }
  },
  FINANCE {
    @Override
    protected Class getClazz() {
      return Finance.class;
    }
  };

  protected Class getClazz() {
    return Class.class;
  }

  public DataProducer instantiate(ExecutorService executor)
      throws NoSuchMethodException,
          InvocationTargetException,
          InstantiationException,
          IllegalAccessException {
    Class faker = this.getClazz();
    Class[] parameters = {ExecutorService.class};
    Constructor<DataProducer> constructor = faker.getConstructor(parameters);

    log.debug("Instantiating of class: {}", faker);
    return constructor.newInstance(executor);
  }
}
