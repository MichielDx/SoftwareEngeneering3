package main.be.kdg.bagageafhandeling.transport.adapters.in;

import main.be.kdg.bagageafhandeling.transport.exceptions.RecordReaderException;

import java.io.*;

/**
 * Created by Michiel on 3/11/2016.
 */
public class RecordReader {
    private File file;
    private BufferedReader bufferedReader;
    private FileReader fileReader;

    public RecordReader(String path) {
        file = new File(path);
    }

    public String initialize() throws RecordReaderException{
        try {
            fileReader = new FileReader(file);
        } catch (FileNotFoundException e) {
            throw new RecordReaderException("Unable to read file", e);
        }
        bufferedReader = new BufferedReader(fileReader);
        return Read();
    }

    public String Read() throws RecordReaderException {
        StringBuilder str = new StringBuilder();
        String line = "";
        if (!file.exists()) {
            throw new RecordReaderException("File does not exist", new Exception(""));
        }
        try {
            String inputStr;
            while ((inputStr = bufferedReader.readLine()) != null)
                str.append(inputStr);

        } catch (IOException e) {
            throw new RecordReaderException("Unable to read in file", e);
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException ex) {
                throw new RecordReaderException("Unable to close reader", ex);
            }
        }
        return str.toString();
    }
}
