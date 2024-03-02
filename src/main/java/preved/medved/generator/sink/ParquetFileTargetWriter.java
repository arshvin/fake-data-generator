package preved.medved.generator.sink;

import lombok.extern.log4j.Log4j2;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.hadoop.conf.Configuration;
import org.apache.parquet.avro.AvroParquetWriter;
import org.apache.parquet.hadoop.ParquetWriter;
import org.apache.parquet.hadoop.metadata.CompressionCodecName;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

@Log4j2
public class ParquetFileTargetWriter implements DataWriter {
    private final Schema schema;
    private final String[] headers = new String[] {"book_author", "book_title", "book_publisher", "book_genre"};
    private final ParquetWriter<GenericData.Record> writer;

    public ParquetFileTargetWriter(Path output) throws IOException {
        AvroSchemaGenerator schemaGenerator = new AvroSchemaGenerator("Books");
        List<AvroSchemaGenerator.Field> fields = Arrays.stream(headers).map(item -> new AvroSchemaGenerator.Field(item, "string")).toList();
        schemaGenerator.setFields(fields);

        schema = new Schema.Parser().parse(new ObjectMapper().writeValueAsString(schemaGenerator));

        log.info("Opening file: {}", output);
        writer = AvroParquetWriter.<GenericData.Record>builder(new org.apache.hadoop.fs.Path(output.toUri()))
                .withSchema(schema)
                .withConf(new Configuration())
                .withCompressionCodec(CompressionCodecName.UNCOMPRESSED)
                .build();
    }

    @Override
    public void writeRecord(List<String> data) throws IOException {
        GenericData.Record record = new GenericData.Record(schema);
        Object[] dataSource = data.toArray();

        for (int i=0; i<headers.length; i++){
            record.put(headers[i],dataSource[i]);
        }
        writer.write(record);
    }

    @Override
    public void close() throws IOException {
        log.info("Closing Parquet file");
        writer.close();
    }
}
