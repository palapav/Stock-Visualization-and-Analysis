/*
Indicators.java
This class is used to build financial indicators such as simple
moving average and exponential moving average that are used to show
the trend lines of stock data.
 */
// importing ArrayList library

import java.util.ArrayList;

public class Indicators {
    // returns an ArrayList of the closing prices for the stock given
    // a fileName that is parsed to create a dataset for the stock
    private static ArrayList<Double> extractClosing(String fileName) {
        ST<String, double[]> dataset = CSVReader.parseCSV(fileName);
        ArrayList<Double> closingPrices = new ArrayList<Double>();
        // iterates through ST and adds the closing price for each day
        // in stock data to ArrayList
        for (String date : dataset.keys()) {
            closingPrices.add(dataset.get(date)[3]);
        }
        return closingPrices;
    }

    // prints the closing prices of a stock given the name of the .csv file
    // for stock; Note: test method for checking if we extracted closingPrices
    // correctly
    private static void printClosingPrices(String fileName) {
        ArrayList<Double> closingPrices = extractClosing(fileName);
        // iterates through ArrayList and prints the daily closing price
        for (double closing : closingPrices) {
            StdOut.println(closing);
        }
    }

    // computes the average closing price for given fileName per each specified
    // time interval (days) in range of daily closing prices; returns arr
    // of SMAs
    public static double[] simpMovingAvgs(int period, String fileName) {
        ArrayList<Double> closingPrices = extractClosing(fileName);
        int length = closingPrices.size();
        if (period > length) {
            // throws exception if specified time period larger than number
            // of daily closing prices in stock file
            throw new IllegalArgumentException("given time period larger than "
                                                       + "# of days of data");
        }

        int endPoint = period;
        // SMA arr needs length starting from date of end of first time period
        // to last day for stock data
        int SIZE = length - period + 1;
        double[] simpMovingAvgs = new double[SIZE];
        // outer array iterates until each SMA is added to arr of SMA values
        // for stock data
        for (int i = 0; i < SIZE; i++) {
            double periodTotal = 0.0;
            if (endPoint <= length) {
                // since moving average, initial date (i) and final date
                // endPoint(shift) shift for each iteration of outer loop
                for (int j = i; j < endPoint; j++) {
                    // computes total amount of closing Prices for each
                    // shifted time interval
                    periodTotal += closingPrices.get(j);
                }
                // simple moving average for current interval in range
                double sma = periodTotal / period;
                simpMovingAvgs[i] = sma;
                endPoint++;
            }
        }
        return simpMovingAvgs;
    }

    // given a time period (days) and fileName, prints values in arr of simple
    // moving averages, test simpleMovingAvgs method
    private static void printSMA(int period, String fileName) {
        double[] simpMovingAvgs = simpMovingAvgs(period, fileName);
        int length = simpMovingAvgs.length;
        // iterates through array to print each simple moving average
        for (int i = 0; i < length; i++) {
            StdOut.println(simpMovingAvgs[i]);
        }
    }

    // given a time period (days) and fileName, prints the number of simple
    // moving averages in array, test for simpleMovingAvgs method
    private static int numSMA(int period, String fileName) {
        double[] simpMovingAvgs = simpMovingAvgs(period, fileName);
        int counter = 0;
        int length = simpMovingAvgs.length;
        for (int i = 0; i < length; i++) {
            // update number of SMAs in arr
            counter++;
        }
        return counter;
    }

    // given a time period (days) and fileName, calculates and returns
    // first simple moving average at end of first time period for stock
    private static double firstSMA(int period, String fileName) {
        ArrayList<Double> closingPrices = extractClosing(fileName);
        double periodTotal = 0.0;
        // iterates through first (non shifted) time period and calculates
        // total sum of closing prices
        for (int i = 0; i < period; i++) {
            periodTotal += closingPrices.get(i);
        }
        return periodTotal / period;
    }

    // given a specified time interval and fileName, computes exponential
    // moving average (more weight to recent closing prices in interval) for
    // each shifting time interval; returns ArrayList of EMAs for stock
    public static ArrayList<Double> expMovingAverage(int period, String fileName) {
        double prevEMA = firstSMA(period, fileName);
        // smoothing allows for better trend line for stock data graph
        double SMOOTHING_CONSTANT = 2.0 / (period + 1);
        ArrayList<Double> closingPrices = extractClosing(fileName);
        int length = closingPrices.size();
        ArrayList<Double> expMovingAvgs = new ArrayList<Double>();
        expMovingAvgs.add(prevEMA);
        // computes exp moving avg for each shifted time period in range
        for (int i = period; i < length; i++) {
            double currentEMA = (closingPrices.get(i) - prevEMA) *
                    SMOOTHING_CONSTANT + prevEMA;
            prevEMA = currentEMA;
            expMovingAvgs.add(currentEMA);
        }
        return expMovingAvgs;
    }

    // given time period and file name, prints each exp moving avg in ArrayList
    private static void printEMA(int period, String fileName) {
        ArrayList<Double> expMovingAvgs = expMovingAverage(period, fileName);
        // iterates through ArrayList to print
        for (double eMA : expMovingAvgs) {
            StdOut.println(eMA);
        }
    }

    // given a time period and fileName, returns the number of exponential
    // moving averages in arrayList
    private static int numEMA(int period, String fileName) {
        ArrayList<Double> expMovingAvgs = expMovingAverage(period, fileName);
        int length = expMovingAvgs.size();
        int counter = 0;
        for (int i = 0; i < length; i++) {
            // updates number of EMAs in ArrayList
            counter++;
        }
        return counter;
    }

    // tests methods in Indicators class
    public static void main(String[] args) {
        // stores name of .csv file from command line
        String fileName = args[0];
        // stores specified time period from command line for calculating
        // simple moving avg and exp moving avg
        int period = Integer.parseInt(args[1]);

        // tests extractClosing method
        Indicators.printClosingPrices(fileName);

        // tests simpMovingAvgs method by printing all SMAs to StdOut
        Indicators.printSMA(period, fileName);
        // tests simpMovingAvgs method by printing number of SMAs
        StdOut.println("# of SMA: " + Indicators.numSMA(period, fileName));
        StdOut.println("First SMA: " + Indicators.firstSMA(period, fileName));

        // tests expMovingAverage method by printing all EMAs to StdOut
        Indicators.printEMA(period, fileName);
        // tests expMovingAvg method by printing number of EMAs
        StdOut.println("# of EMA: " + Indicators.numEMA(period, fileName));
    }
}
