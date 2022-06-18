/*
StockClient.java
This class's purpose is for the client to call the required functions
needed to graph the desired stock data and make trend lines from that data
 */
public class StockClient {
    // calls necessary methods to visualize and analyze stock
    public static void main(String[] args) {
        // stores String name of .csv stock data file from command line
        String fileName = args[0];
        // stores time period in days from command line for calculating
        // moving averages of stock
        int period = Integer.parseInt(args[1]);

        // sets window size for stock visualization
        StdDraw.setCanvasSize(1200, 850);
        // calls method to sets x/y scales for graphing stock data and trends
        StockGraph.setScales(fileName);
        // calls method to draw a candlestick chart for each date for stock
        StockGraph.drawCandlesticks(fileName);
        // calls method to draw a line graph (trend line) for stock's SMAs
        StockGraph.drawSMA(period, fileName);
        // calls method to draw a line graph (trend line) for stock's EMAs
        StockGraph.drawEMA(period, fileName);
    }
}
