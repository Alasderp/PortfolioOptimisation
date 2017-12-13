import java.util.ArrayList;

/**
 * @author Alasdair Stuart 1306348
 */
public class Portfolio {
    
    private ArrayList<Stock> stocks;
    
    public Portfolio(){
        
        stocks = new ArrayList<>();
        
    }
    
    /**
     * Add a stock to a portfolio
     * @param stock The stock to be added to the portfolio
     */
    public void addStock(Stock stock){
        stocks.add(stock);
    }
    
    /**
     * Returns an ArrayList of all the stocks in a portfolio
     * @return The array containing all the stocks in this portfolio
     */
    public ArrayList getStocks(){
        return this.stocks;
    }
    
    /**
     * Retrieve a single stock from the portfolio
     * @param x The index number of the stock to be returned
     * @return The individual stock in the portfolio at the specified index
     */
    public Stock getStock(int x){
        return this.stocks.get(x);
    }
    
    /**
     * @param startDate The start of the window where values are taken from
     * @param endDate The end of the time frame where values are taken from
     * @return The percentage return of the portfolio
     */
    public double getReturn(String startDate, String endDate){
        double totalReturn = 0;
        for(int x = 0;x < this.stocks.size();x++){
            totalReturn += this.stocks.get(x).stockReturn(startDate, endDate, true) * this.stocks.get(x).getWeighting();
        }
        
        return totalReturn;
        
    }
    
    /**
     * 
     * @param startDate The date from which values will start to be recorded
     * @param endDate The date at which values will cease to be recorded
     * @param timePeriod The time window across which calculations are made
     * @return The average return of the portfolio
     */
    public double getAverageReturn(String startDate, String endDate, int timePeriod){
        
        //Holds he start and end pints for the date and price arrays
        //Remember the starts of the price/date arrays are more recent
        int EarlyDate = 0;
        int LateDate = 0;
        
        //Holds the returns of all the time periods
        ArrayList<Double> returnValues = new ArrayList<>();
        //Holds the return of an individual time period
        double individualReturn = 0;
        //Holds the average of all the returns
        double averageReturn = 0;        
        
        double portfolioAverageReturn = 0;
        
        //Find where to start and end in the array
        for(int i = 0; i < this.stocks.size(); i++){
            
            averageReturn = 0; 
            individualReturn = 0;
            returnValues.clear();
         
            for(int x = 0; x < this.stocks.get(i).closePrice.size(); x++){

                if(this.stocks.get(i).date.get(x).equals(startDate)){
                    EarlyDate = x;
                }

                if(this.stocks.get(i).date.get(x).equals(endDate)){
                    LateDate = x;
                }
            }

            //Need to loop through backwards as more recent dates are in the start of the arrays
            for(int x = EarlyDate - (timePeriod - 1); x >= LateDate; x-= timePeriod){

                individualReturn = this.stocks.get(i).stockReturn(this.stocks.get(i).date.get(x + (timePeriod - 1)), this.stocks.get(i).date.get(x), true);          
                returnValues.add(individualReturn);
                averageReturn += individualReturn;

            }

            portfolioAverageReturn += (averageReturn / returnValues.size()) * this.stocks.get(i).getWeighting();
        
        }
        
        //Multiply by 100 to get percentage return
        return portfolioAverageReturn * 100;
        
    }
    
    /**
     * @param stockA The first stock to be used in the calculation
     * @param stockB The second stock to be used in the pair
     * @param startDate The start date for data to be collected from
     * @param endDate The point where data should stop being collected
     * @param timePeriod Specifies how often data should be sampled
     * @return The correlation coefficient of the two stocks provided
     */
    public double correlationCoefficient(Stock stockA, Stock stockB, String startDate, String endDate, int timePeriod){
        
        ArrayList<Double> returnsA = stockA.stockPartialVolatility(startDate, endDate, timePeriod);
        ArrayList<Double> returnsB = stockB.stockPartialVolatility(startDate, endDate, timePeriod);
        
        double covariance = 0;
        double sdA = 0;
        double sdB = 0;
        
        for(int x = 0;x < returnsA.size();x++){
            covariance += returnsA.get(x) * returnsB.get(x);
        }
              
        covariance = covariance / returnsA.size();
        
        sdA = stockA.stockFullVolatility(returnsA);
        sdB = stockB.stockFullVolatility(returnsB);
        
        covariance = covariance / (sdA * sdB);
        
        return covariance;
        
    }
    
    /**
     * 
     * @param startDate The date from which values will start to be recorded
     * @param endDate The date at which values will cease to be recorded
     * @param timePeriod The time window across which calculations are made
     * @return The volatility of the portfolio
     */
    public double portfolioVolatility(String startDate, String endDate, int timePeriod){
        
        double firstHalf = 0;
        double secondHalf = 0;
        
        for(int x = 0; x < this.stocks.size(); x++){
            
            firstHalf += (this.stocks.get(x).getWeighting() * this.stocks.get(x).getWeighting()) * (this.stocks.get(x).stockFullVolatility(this.stocks.get(x).stockPartialVolatility(startDate, endDate, timePeriod)));
            
        }
           
        for(int x = 0; x < (this.stocks.size() - 1); x++){
            for(int y = x + 1; y < this.stocks.size(); y++){
                    
                secondHalf += (2 * ((this.stocks.get(x).getWeighting() * this.stocks.get(y).getWeighting())
                              * (this.stocks.get(x).stockFullVolatility(this.stocks.get(x).stockPartialVolatility(startDate, endDate, timePeriod)) * this.stocks.get(y).stockFullVolatility(this.stocks.get(y).stockPartialVolatility(startDate, endDate, timePeriod)))
                              * (this.correlationCoefficient(this.stocks.get(x), this.stocks.get(y), startDate, endDate, timePeriod))));
            
            }
        }
        
        return Math.sqrt(firstHalf + secondHalf);
                  
    }
    
}