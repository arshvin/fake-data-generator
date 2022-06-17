package preved.medved;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

import com.beust.jcommander.Strings;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FileUtils;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvListWriter;
import org.supercsv.prefs.CsvPreference;
import preved.medved.csv.FileFormatter;

@Log4j2
public class FakeDataGenerator {
    private CommandLineArguments arguments;

    public FakeDataGenerator(CommandLineArguments arguments){
        this.arguments = arguments;
    }

    public void process() throws IOException {

        FileFormatter fileFormatter = new FileFormatter();
    
        if (arguments.isBeers()){
            fileFormatter.addBeer();
        }
        if (arguments.isBooks()){
            fileFormatter.addBook();
        }
        if (arguments.isCat()){
            fileFormatter.addCat();
        }
        if (arguments.isDog()){
            fileFormatter.addDog();
        }
        if (arguments.isFinance()){
            fileFormatter.addFinance();
        }

        fileFormatter.build();
        final CellProcessor[] cellProcessor = fileFormatter.getCellProcessors().toArray(new CellProcessor[0]);
        final String[] header = fileFormatter.getHeaders().toArray(new String[0]);

        log.info("Column names are:\n{}", Strings.join("|",header));

        BigInteger minSizeLimit = BigInteger.valueOf(arguments.getSizeGibiBytes()*1024*1024*1024);

        for (int i=0; i<arguments.getAmountFiles(); i++){
            BigInteger fileSizeCounter = BigInteger.valueOf(0);

            String filePath = getTargetFileName().toFile().toString();
            log.info("Writing data to file: {}",filePath);

            CsvListWriter targetFileWriter = new CsvListWriter(new FileWriter(filePath), CsvPreference.STANDARD_PREFERENCE);
            targetFileWriter.writeHeader(header);

            fileSizeCounter.add(BigInteger.valueOf(Strings.join(",",header).length()));

            while (fileSizeCounter.compareTo(minSizeLimit) < 0 ){
                List<String> currentLine = fileFormatter.produceData();
                targetFileWriter.write(currentLine,cellProcessor);

                fileSizeCounter = fileSizeCounter.add(BigInteger.valueOf(Strings.join(",",currentLine).length()));
            }

            targetFileWriter.close();
            log.info("Closed file: {}",filePath);
            log.info("Amount of data has been written: {}", FileUtils.byteCountToDisplaySize(fileSizeCounter));
        }
    }

    private Path getTargetFileName(){
        String basename = new StringBuilder().append(UUID.randomUUID().toString()).append(".csv").toString();
        Path path = Path.of(arguments.getPath(),basename);
        return path;
    }
}
