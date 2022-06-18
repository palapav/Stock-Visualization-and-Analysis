/*
StockGraph.java
This class's purpose is to graph the stock data(open, high, low, close)
and to create line graphs for the trend lines of the stock (simple moving
average and exponential moving average)
 */
// importing ArrayList library

import java.util.ArrayList;

public class StockGraph {
    // given a file name of .csv stock data, sets the x and y scales for the
    // graph, graphs tick marks for intervals on x and y axes
    public static void setScales(String fileName) {
        ST<String, double[]> dataset = CSVReader.parseCSV(fileName);
        double highest = Double.NEGATIVE_INFINITY;
        // traverses through each date in dataset and updates highest value
        // if the high value for current stock day's date is greater than
        // previous
        for (String date : dataset.keys()) {
            int size = dataset.get(date).length;
            for (int i = 0; i < size; i++) {
                // to access the high values for stock data
                if (i == 1) {
                    double current = dataset.get(date)[i];
                    if (current > highest) highest = current;
                }
            }
        }

        // sets the y-axis scaling for stock
        // Note: lowest price value for a stock can only be zero
        StdDraw.setYscale(0, highest);
        int NUM_POINTS = StockData.getNumDates(fileName);
        // sets x-axis scaling, set highest X scale to NUM_POINTS + 1
        // to easily set last candlestick/trend line in dataset
        StdDraw.setXscale(0, NUM_POINTS + 1);


        // desired pen radius for dash marks along axes
        StdDraw.setPenRadius(0.0005);

        // below code for setting up y axis tick mark intervals
        // drawing y axis
        // desired number of y axis intervals for placing tick marks
        double INTERVAL = highest / 10;
        // x coordinate for ending position of actual tick mark line on y axis
        double SCALED_YDASH = 0.02 * NUM_POINTS;
        // the position of where price of stock will be at each interval
        double PRICE_POSITION = 1.004 * SCALED_YDASH;
        // iterates through range of y axis stock price, placing tick marks
        // and stock prices at each interval
        for (int i = 0; i <= highest; i += INTERVAL) {
            if (i == 0) {
                // creates y axis tick mark and positions price next to tick
                // at beginning of y axis
                StdDraw.line(0, i, SCALED_YDASH, i);
                // y value price positioning at first tick slightly above edge
                // of window screen
                StdDraw.textLeft(PRICE_POSITION, 0.01 * highest,
                                 String.valueOf(i));

                // creates y axis tick mark and positions price next to tick
                // at end of y axis
                StdDraw.line(0, highest, SCALED_YDASH, highest);
                // y value price positioning at last tick slightly below edge
                // of window screen
                StdDraw.textLeft(PRICE_POSITION, highest - 0.012 * highest,
                                 String.valueOf((int) highest));
            }
            else if (highest - i >= INTERVAL) {
                // draws intermediate tick marks and prices for intervals
                StdDraw.line(0, i, SCALED_YDASH, i);
                StdDraw.textLeft(PRICE_POSITION, i, String.valueOf(i));
            }
        }

        // below code for setting up x axis tick mark intervals
        // drawing x axis
        int counter = 0;
        // precise time interval between x axis tick marks
        double SCALED_INTERVAL = 0.15 * NUM_POINTS;
        // rounded time interval between x axis tick marks
        int ROUNDED_INTERVAL = (int) Math.ceil(SCALED_INTERVAL);
        // y coordinate for ending position of actual tick mark line on x axis
        double SCALED_XDASH = 0.05 * highest;
        // the position of where the stock date will be at each interval
        double DATE_POSITION = 0.05 * highest + 5;
        // traverses through dates in dataset and draws x axis tick marks
        // and date at each interval
        for (String date : dataset.keys()) {
            // updates positioning of date along x axis
            counter++;
            // drawing first date when counter is 1 to easily see the first
            // tick mark on the window screen
            if (counter == 1) {
                // draws first x axis tick mark and date next to tick mark
                StdDraw.line(counter, 0, counter, SCALED_XDASH);
                StdDraw.textLeft(counter, DATE_POSITION, date);
            }
            else if (counter == NUM_POINTS) {
                // draws last x axis tick mark and date next to tick mark
                StdDraw.line(NUM_POINTS, 0, NUM_POINTS, SCALED_XDASH);
                StdDraw.textRight(NUM_POINTS, DATE_POSITION, date);
            }
            else {
                // draws intermediate tick marks at specified rounded intervals
                // and does not draw when less than ROUNDED_INTERVAL away from
                // last tick mark
                if (counter % ROUNDED_INTERVAL == 0 &&
                        NUM_POINTS - counter >=
                                ROUNDED_INTERVAL) {
                    StdDraw.line(counter, 0, counter, SCALED_XDASH);
                    StdDraw.text(counter, DATE_POSITION, date);
                }
            }
        }
        // resets pen radius
        StdDraw.setPenRadius();
    }

    // given a file name for .csv stock data, draws a candlestick chart (open,
    // high, low, close) to StdDraw for each date in dataset
    public static void drawCandlesticks(String fileName) {
        ST<String, double[]> dataset = CSVReader.parseCSV(fileName);
        int counter = 0;
        // iterates through dates in datasets, pulling open, how, low, close
        // stock prices at each date and computes candlestick chart
        for (String date : dataset.keys()) {
            counter++;
            double open = dataset.get(date)[0];
            double high = dataset.get(date)[1];
            double low = dataset.get(date)[2];
            double close = dataset.get(date)[3];
            StdDraw.setPenColor(StdDraw.BLACK);
            // originally draws stock price line as BLACK from low to high
            // at date
            StdDraw.line(counter, low, counter, high);
            // draws portion of black line as GREEN if closing price
            // greater than open price at that date
            if (close > open) {
                StdDraw.setPenColor(StdDraw.GREEN);
                StdDraw.line(counter, open, counter, close);
            }
            // draws portion of black line as RED if closing price
            // less than open price at that date
            else {
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.line(counter, close, counter, open);
            }
        }
    }

    // given a file name and specified time interval, draws a line graph
    // of all the simple moving averages computed for stock data to StdDraw
    public static void drawSMA(int period, String fileName) {
        double[] simpleMA = Indicators.simpMovingAvgs(period, fileName);
        int length = simpleMA.length;
        // sets simple moving avg line graph as MAGENTA color
        StdDraw.setPenColor(StdDraw.MAGENTA);
        // iterates through simpleMA connecting the current simpleMA value
        // to the next one
        for (int i = 0; i < length - 1; i++) {
            StdDraw.line(period, simpleMA[i], period + 1, simpleMA[i + 1]);
            period++;
        }
    }

    // given a file name and specified time interval, draws a line graph
    // of all the exponential moving averages computed for stock data to StdDraw
    public static void drawEMA(int period, String fileName) {
        ArrayList<Double> expMA = Indicators.expMovingAverage(period, fileName);
        int length = expMA.size();
        // sets simple moving avg line graph as PRINCETON_ORANGE color
        StdDraw.setPenColor(StdDraw.PRINCETON_ORANGE);
        // iterates through expMA connecting the current expMA value
        // to the next one
        for (int i = 0; i < length - 1; i++) {
            StdDraw.line(period, expMA.get(i), period + 1, expMA.get(i + 1));
            period++;
        }
    }

    // tests methods in StockGraph class
    public static void main(String[] args) {
        // stores name of .csv stock data file from command line
        String fileName = args[0];
        // stores time period in days from command line for calculating
        // moving averages of stock
        int period = Integer.parseInt(args[1]);

        // sets size of StdDraw window
        StdDraw.setCanvasSize(1500, 850);

        // tests setScales method with given fileName
        StockGraph.setScales(fileName);
        // tests drawCandlesticks method with given fileName
        StockGraph.drawCandlesticks(fileName);

        // tests drawSMA and drawEMA method with given fileName and specified
        // time period for computing each moving average as 3 days
        StockGraph.drawSMA(period, fileName);
        StockGraph.drawEMA(period, fileName);
    }
}
