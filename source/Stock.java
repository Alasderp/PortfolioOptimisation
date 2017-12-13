import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

/**
 * @author Alasdair Stuart 1306348
 */

public class Stock {
    
    //The shorthand name of a stock
    private String symbol;
    //Date of a closing price
    public ArrayList<String> date;
    //The closing price on a particular day
    public ArrayList<Double> closePrice;
    //The weighting of a stock for when it is added to a portfolio
    private double weighting;
    
    /**
     * Creates a stock object that will hold the name of a stock
     * and closing price on individual days
     * @param name The shorthand name of a stock
     * @param proportion The weighting this stock will have in a portfolio, ranges from 0 - 1
     */
    public Stock(String name, double proportion){
    
        symbol = name;
        date = new ArrayList<>();
        closePrice = new ArrayList<>();
        weighting = proportion;
    
    }
    
    /**
     * Load data from CSV file into the stock object
     */
    public void loadData(){
               
        try{
            
            Scanner scnr = new Scanner(new File("C:\\Users\\Alasdairp\\Desktop\\Honours Project\\Java\\jfreechart-1.0.19\\source\\CSV/" + this.symbol + ".csv"));
            scnr.useDelimiter(",|\n");              
            String padding = "";
            
            while (scnr.hasNext()){
                
                this.date.add(scnr.next());
                
                //NEED TO FIGURE OUT HOW TO IGNORE COLUMNS AND ROWS
                padding = scnr.next();
                padding = scnr.next();
                padding = scnr.next();
                
                this.closePrice.add(scnr.nextDouble());
                
                padding = scnr.next();
                            
            }           
        }
        catch(Exception e){
            System.out.println(e);
            System.exit(1);
        }
    }
    
    /**
     * @return The shorthand name of the stock
     */
    public String getName(){
        return this.symbol;
    }
    
    /**
     * @return An array of all the dates used as data points
     */
    public ArrayList<String> getDates(){
        //SimpleDateFormat ft = new SimpleDateFormat("dd-MMM-yy");
        return this.date;
    }
    
    /**
     * @return An array of all the closing prices of every day
     */
    public ArrayList<Double> getPrices(){
        return this.closePrice;
    }
    
    /**
     * @return The weighting of this stock in a portfolio
     */
    public double getWeighting(){
        return this.weighting;
    }
    
    public void setWeighting(Double newWeighting){
        this.weighting = newWeighting;
    }
    
    /**
     * @param startDate The start point for values to be taken from
     * @param endDate The end point where values should stop being taken
     * @param percentage Set to true if the return of the stock should be calculated as a percentage
     * @return The return of the stock over the stated period of time
     */
    public double stockReturn(String startDate, String endDate, Boolean percentage){
        
        double startPrice = 0;
        double endPrice = 0;
        
        //Find where to start and end in the array
        for(int x = 0; x < this.closePrice.size(); x++){
            
            if(this.date.get(x).equals(startDate)){
                startPrice = this.closePrice.get(x);
            }
            
            if(this.date.get(x).endsWith(endDate)){
                endPrice = this.closePrice.get(x);
            }
        } 
        
        if(percentage){
            return (endPrice - startPrice) / startPrice;
        }
        else{
            return endPrice - startPrice;
        }
        
    }
    
    /**
     * The full formula is not always needed so split into two halves
     * Also useful for debugging and checking results by hand
     * @param startDate The starting date for this calculation
     * @param endDate The end date for this calculation
     * @param timePeriod The period of time returns are calculated over
     * @return The standard deviation of the returns of this portfolio
     */
    public ArrayList<Double> stockPartialVolatility(String startDate, String endDate, int timePeriod){
        
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
        
        //Find where to start and end in the array
        for(int x = 0; x < this.closePrice.size(); x++){
            
            if(this.date.get(x).equals(startDate)){
                EarlyDate = x;
            }
            
            if(this.date.get(x).equals(endDate)){
                LateDate = x;
            }
        }
 
        //Need to loop through backwards as more recent dates are in the start of the arrays
        for(int x = EarlyDate - (timePeriod - 1); x >= LateDate; x-= timePeriod){
                 
            individualReturn = this.stockReturn(this.date.get(x + (timePeriod - 1)), this.date.get(x), true);          
            returnValues.add(individualReturn);
            averageReturn += individualReturn;
                     
        }
        
        averageReturn = (averageReturn / returnValues.size());
        
        for(int x = 0; x < returnValues.size(); x++){
            returnValues.set(x, (returnValues.get(x) - averageReturn));
        }
        
        return returnValues;
            
    }
    
    /**
     * @param returns The array provided from the partial volatility calculation
     * @return The standard deviation of the values contained in the returns array
     */
    public double stockFullVolatility(ArrayList<Double> returns){
        
        double totalReturn = 0;
        
        for(int x = 0; x < returns.size();x++){
            totalReturn  += returns.get(x) * returns.get(x);
        }
        
        totalReturn = totalReturn / returns.size();
        return Math.sqrt(totalReturn);
    }
    
    /**
     * Loops through the date and price arrays and prints all details
     */
    public void printDetails(){
        
        for(int x = 0;x < this.date.size();x++){        
            System.out.println(this.symbol + ", " + this.date.get(x) + ", " + this.closePrice.get(x) + "\n");
        }                 
    }
    
}
