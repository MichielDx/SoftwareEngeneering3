package main.be.kdg.bagageafhandeling.transport.adapters.out;

import main.be.kdg.bagageafhandeling.transport.exceptions.RecordWriterException;
import org.apache.log4j.Logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Arthur Haelterman on 3/11/2016.
 */
public class RecordWriter {
    private File file;
    private BufferedWriter bufferedWriter;
    private FileWriter fileWriter;
    private Logger logger = Logger.getLogger(RecordWriter.class);
    public RecordWriter(String path) {
        file = new File(path);
    }

    public void initialize() throws RecordWriterException {
        try {
            fileWriter = new FileWriter(file);
        } catch (IOException e) {
            throw new RecordWriterException("Unable to create file", e);
        }
        bufferedWriter = new BufferedWriter(fileWriter);
    }

    public void write(String serializedString) throws RecordWriterException {
        logger.info("Attempting to write file: " + file.getAbsolutePath());
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            throw new RecordWriterException("Unable to create file", e);
        }
        try {
            bufferedWriter.write(serializedString);
            bufferedWriter.flush();

        } catch (IOException e) {
            throw new RecordWriterException("Unable to write to file", e);
        }
        finally {
            try {
                if (bufferedWriter != null) {
                    bufferedWriter.close();
                }
            } catch (IOException ex) {
                throw new RecordWriterException("Unable to close writer", ex);
            }
        }
        logger.info("Succesfully written file: " + file.getAbsolutePath());
    }
}
