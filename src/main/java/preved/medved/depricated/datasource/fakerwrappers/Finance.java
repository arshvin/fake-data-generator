package preved.medved.depricated.datasource.fakerwrappers;

import java.util.List;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class Finance implements FakeSource {

    @Override
    public List<String> getFake() {
        return null;
    }
}
