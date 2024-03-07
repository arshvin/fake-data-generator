package preved.medved.cli;

import com.beust.jcommander.Parameter;
import lombok.Builder;
import lombok.Data;
import preved.medved.cli.converters.InputNameToClassMapper;
import preved.medved.cli.splitters.FakerListSplitter;
import preved.medved.generator.source.AvailableFakers;

import java.util.List;

@Builder(toBuilder = true)
@Data
public class FakersRelatedArgs {
    @Parameter(
            names = "--fakers",
            description =
                    "Space- or comma-separated list of fakers which will be used in specified order. Current available list of fakers: book, beer, cat, dog, finance",
            required = true,
            converter = InputNameToClassMapper.class,
            splitter = FakerListSplitter.class,
            order = 0)
    private List<AvailableFakers> fakers;
}
