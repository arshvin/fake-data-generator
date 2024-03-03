package preved.medved.generator.sink;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.List;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class AvroSchemaGenerator {
    private final String type = "record";
    private final String name;
    private List<Field> fields;

    public AvroSchemaGenerator(String name){
        this.name = name;
    }

    @Data
    @RequiredArgsConstructor()
    public static class Field {
        private final String name;
        private final String type;

        @JsonProperty("default")
        private String defaultValue = "null";
    }
}
