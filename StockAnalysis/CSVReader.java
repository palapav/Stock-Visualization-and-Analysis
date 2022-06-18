/*
CSVReader.java:
This class is used to implement a static method that reads in historical
csv data from a stock (open, high, low etc) on Yahoo Finance and
returns a dataset containing the desired data for the client

The following external online libraries are required:
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
 */

// importing external libraries

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;

public class CSVReader {
    // returns a symbol table of desired stock data for graphing and calculation
    // (open, close,high, low) after external libraries reads in a csv stock file
    public static ST<String, double[]> parseCSV(String path) {
        // Citation: the following code is partially copied and adapted from
        // the COS126 staff's provided starter code for parsing .csv files
        Reader in;
        CSVParser parser;
        List<CSVRecord> list = null;
        try {
            in = new FileReader(path);
            parser = new CSVParser(in, CSVFormat.DEFAULT);
            list = parser.getRecords();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        // Prepare to store dataset in symbol table
        int numRows;
        int numCols;
        if (list != null) {
            numRows = list.size();
            numCols = list.get(0).size();
        }
        else {
            // throws exception if no content in .csv file
            throw new RuntimeException("Parser did not find any contents in "
                                               + ".csv file. Double-check the "
                                               + ".csv file path and contents.");
        }
        // dataset refers to newly created symbol table
        ST<String, double[]> dataset = new ST<String, double[]>();
        // iterates through 2D array, ignoring first row of String values
        for (int r = 1; r < numRows; r++) {
            CSVRecord row = list.get(r);
            for (int c = 0; c < numCols; c++) {
                if (c == 0) {
                    dataset.put(row.get(0), new double[4]);
                }
                else {
                    // only assigning arr of open, high, low, close data values
                    // to each key date in symbol table
                    if (c <= 4) {
                        dataset.get(row.get(0))[c - 1] =
                                Double.parseDouble(row.get(c));
                    }
                }
            }
        }
        return dataset;
    }

    // tests methods in CSVReader class
    public static void main(String[] args) {
        // filepath stores .csv file from command line
        String filepath = args[0];
        // dataset refers to newly created symbol table with parsed in .csv
        // data
        ST<String, double[]> dataset = CSVReader.parseCSV(filepath);
        // ensures desired stock data values (open, close, high, low)
        // are in symbol table
        for (String date : dataset) {
            int length = dataset.get(date).length;
            for (int i = 0; i < length; i++) {
                StdOut.print(dataset.get(date)[i] + " ");
            }
            StdOut.println();
        }
    }
}
