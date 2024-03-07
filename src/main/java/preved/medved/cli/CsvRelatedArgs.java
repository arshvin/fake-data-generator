package preved.medved.cli;

import com.beust.jcommander.Parameter;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class CsvRelatedArgs {
    @Parameter(names = "--csv", description = "CSV format will be used as output", order = 4)
    private boolean csvOutput;
}
