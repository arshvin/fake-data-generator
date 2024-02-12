package preved.medved.datasource.fakerwrappers;

import lombok.extern.log4j.Log4j2;

import java.util.List;

@Log4j2
public class Beer implements FakeSource {

    @Override
    public List<String> getFake() {
        return null;
    }
}
