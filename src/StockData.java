/*
StockData.java
This class's purpose is to test the symbol table dataset created
from reading the .csv stock data file
 */
public class StockData {
    // given a file name for .csv data, prints daily date from stock data
    // test method for checking dates in dataset symbol table
    private static void printDates(String fileName) {
        ST<String, double[]> dataset = CSVReader.parseCSV(fileName);
        // iterates through keys in dataset and prints daily date
        for (String date : dataset.keys()) {
            StdOut.println(date);
        }
    }

    // given a file name for .csv data, returns number of dates in stock
    // data; test method for checking # of dates in dataset and also used
    // in StockGraph.java for graphing
    public static int getNumDates(String fileName) {
        ST<String, double[]> dataset = CSVReader.parseCSV(fileName);
        int counter = 0;
        for (String date : dataset.keys()) {
            // updates number of dates in stock data
            counter++;
        }
        return counter;
    }

    // given a file name for .csv data, prints the open, high, low, close
    // data values for each date; test method for dataset symbol table
    private static void printStockInfo(String fileName) {
        ST<String, double[]> dataset = CSVReader.parseCSV(fileName);
        // outer for loop iterates through each date in dataset
        for (String date : dataset) {
            int dayData = dataset.get(date).length;
            // for each date in dataset, prints open, high, low, close stock
            // values to StdOut
            for (int i = 0; i < dayData; i++) {
                StdOut.print(dataset.get(date)[i] + " ");
            }
            StdOut.println();
        }
    }

    // tests methods in StockData class
    public static void main(String[] args) {
        // stores String name of .csv file data from command line
        String fileName = args[0];

        // tests dataset symbol table to see if I gathered all dates from
        // stock data in correct order
        StockData.printDates(fileName);

        // tests dataset symbol table to see if I gathered correct # of dates
        // in the stock data
        int numDays = StockData.getNumDates(fileName);
        StdOut.println("number of days in dataset: " + numDays);

        // tests dataset symbol table to see if I collected the correct open,
        // high, low, close values per each date in dataset
        StockData.printStockInfo(fileName);
    }
}
