package preved.medved.generator.sink;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.hadoop.conf.Configuration;
import org.apache.parquet.avro.AvroParquetWriter;
import org.apache.parquet.hadoop.ParquetWriter;
import org.apache.parquet.hadoop.metadata.CompressionCodecName;
import org.codehaus.jackson.map.ObjectMapper;

@Log4j2
public class ParquetFileTargetWriter implements DataWriter {
  private final Schema schema;
  private final ParquetWriter<GenericData.Record> writer;
  private final String[] headers;

  public ParquetFileTargetWriter(Path output, List<String> headers) throws IOException {
    this.headers = headers.toArray(new String[0]);

    log.debug("Avro record schema preparation");
    AvroSchemaGenerator schemaGenerator = new AvroSchemaGenerator("Books");
    List<AvroSchemaGenerator.Field> fields =
        Arrays.stream(this.headers)
            .map(item -> new AvroSchemaGenerator.Field(item, "string"))
            .toList();
    schemaGenerator.setFields(fields);

    log.debug("Avro record schema generation");
    schema = new Schema.Parser().parse(new ObjectMapper().writeValueAsString(schemaGenerator));

    log.info("Opening file: {}", output);
    writer =
        AvroParquetWriter.<GenericData.Record>builder(new org.apache.hadoop.fs.Path(output.toUri()))
            .withSchema(schema)
            .withConf(new Configuration())
            .withCompressionCodec(CompressionCodecName.UNCOMPRESSED)
            .build();
  }

  @Override
  public void writeRecord(List<String> data) throws IOException {
    GenericData.Record record = new GenericData.Record(schema);
    Object[] dataSource = data.toArray();

    log.debug("Building the record");
    for (int i = 0; i < headers.length; i++) {
      record.put(headers[i], dataSource[i]);
    }
    log.debug("Writing the record");
    writer.write(record);
  }

  @Override
  public void close() throws IOException {
    log.info("Closing Parquet file");
    writer.close();
  }
}
