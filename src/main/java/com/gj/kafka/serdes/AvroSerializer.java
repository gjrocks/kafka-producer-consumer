package com.gj.kafka.serdes;

import com.gj.kafka.model.AvroHttpRequest;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.Encoder;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class AvroSerializer {

    private static final Logger logger = LoggerFactory.getLogger(AvroSerializer.class);

    public byte[] serializeAvroHttpRequestJSON(AvroHttpRequest request) {
        DatumWriter<AvroHttpRequest> writer = new SpecificDatumWriter<>(AvroHttpRequest.class);
        byte[] data = new byte[0];
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Encoder jsonEncoder = null;
        try {
            jsonEncoder = EncoderFactory.get()
                .jsonEncoder(AvroHttpRequest.getClassSchema(), stream);
            writer.write(request, jsonEncoder);
            jsonEncoder.flush();
            data = stream.toByteArray();
        } catch (IOException e) {
            logger.error("Serialization error " + e.getMessage());
        }
        return data;
    }

    /**
     *
     *DatumWriter<User> userDatumWriter = new SpecificDatumWriter<User>(User.class);
     * DataFileWriter<User> dataFileWriter = new DataFileWriter<User>(userDatumWriter);
     * dataFileWriter.create(user1.getSchema(), new File("users.avro"));
     * dataFileWriter.append(user1);
     * dataFileWriter.append(user2);
     * dataFileWriter.append(user3);
     * dataFileWriter.close();<span style="font-weight: 400">
     */
    public File serializeAvroHttpRequestJSONToFile(AvroHttpRequest request,String fileName) throws Exception{
        File file=new File(fileName);
        DatumWriter<AvroHttpRequest> writer = new SpecificDatumWriter<>(AvroHttpRequest.class);
        DataFileWriter<AvroHttpRequest> dataFileWriter = new DataFileWriter<AvroHttpRequest>(writer);
        dataFileWriter.create(request.getSchema(), new File(fileName));
        dataFileWriter.append(request);
        dataFileWriter.close();

        return file;
    }

    public byte[] serializeAvroHttpRequestBinary(AvroHttpRequest request) {
        DatumWriter<AvroHttpRequest> writer = new SpecificDatumWriter<>(AvroHttpRequest.class);
        byte[] data = new byte[0];
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Encoder jsonEncoder = EncoderFactory.get()
            .binaryEncoder(stream, null);
        try {
            writer.write(request, jsonEncoder);
            jsonEncoder.flush();
            data = stream.toByteArray();
        } catch (IOException e) {
            logger.error("Serialization error " + e.getMessage());
        }

        return data;
    }

}
