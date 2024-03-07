package preved.medved.cli;

import com.beust.jcommander.Parameter;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class ParquetRelatedArgs {
    @Parameter(names = "--parquet", description = "Parquet format will be used as output", order = 5)
    private boolean parquetOutput;
}
