package preved.medved.cli.splitters;

import com.beust.jcommander.converters.IParameterSplitter;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;

public class FakerListSplitter implements IParameterSplitter {
    @Override
    public List<String> split(String value) {
        return new LinkedHashSet<String>(Arrays.asList(value.split(","))).stream().toList();
    }
}
