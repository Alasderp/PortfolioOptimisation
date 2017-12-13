
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author Alasdair Stuart 1306348
 */
public class StockWeightingSolution {
    
    //FOR TESTING PURPOSES ONLY
    //ASSIGNMENT IS NOT GUARANTEED AND IS ONLY TEMPORARY
    public int id = 0;
    private double SOfitness = 0;
    
    public double solutionReturn;
    public double solutionRisk;
    
    //Stores the details of the solution i.e. weightings
    public ArrayList<Double> alleles;
    //Fitness of the solution
    public double fitness = 0.0;
    //Maximum number of stocks that can be used in a solution
    private int length = 15;
    
    //The number of solutions which this solution is dominated by
    private int dominatedBy;
    private int trueDomCount;
    //The individuals which this solution dominates
    private ArrayList<StockWeightingSolution> dominates = new ArrayList<>();
    
    //Used to hold the crowding distance along individual axis
    private double returnDistance = 0;
    private double riskDistance = 0;
    //Holds the overall crowding distance i.e. the sum of the two variables above
    private double crowdingDistance = 0;
    
    /**
     * Create a solution with randomly assigned weightings. Evaluate whether it breaks constraints on creation.
     */
    public StockWeightingSolution(boolean generatingEmpty){
        
        //Indicates whether constraint is violated
        boolean violation = false;
        
        double tempValue;
        
        //Represents the weighting of a stock, ranges from 0 - 100
        ArrayList<Double> weightings = new ArrayList<>();
        double weighting = 0;
       
        alleles = new ArrayList<>();
        
        if((Evaluator.floor * length) > 100){
            System.out.println("Either there are too many shares or the floor value is too large. Floor * num shares exceeds 100");
            System.exit(0);
        }
        
        
        do{
        
            violation = false;
            weighting = 0;
            weightings.clear();
            alleles.clear();
            
            Evaluator.totalSolutions++;          
            
            //Generate a random number for each of the stocks. 
            //Calculate what percentage of the total each number is. 
            //Use this value as the weighting
            for(int x = 0; x < length; x++){
    	        do{
    	        	tempValue= Math.random();
    	        }while(tempValue < Evaluator.floor || tempValue > Evaluator.ceiling);
                weightings.add(x, tempValue);
                weighting += weightings.get(x);
            }
        
	        if(!generatingEmpty){
	        	
	            for(int x = 0; x < length; x++){            
	                alleles.add((weightings.get(x) / weighting));
	                ///If a constraint is violated flag this up and re-generate the solution
	                if (alleles.get(x) < Evaluator.floor || alleles.get(x) > Evaluator.ceiling){
	                    violation = true;
	                    break;
	                }
	            }	        	
	            if(violation != true){
			        this.solutionReturn = Evaluator.theReturn(this);
			        this.solutionRisk = Evaluator.theVolatility(this);
			        this.setSOfitness(Evaluator.evaluateFitness(this));
	            }
	        }
	        else{
	        	for(int x = 0; x < length; x++){
	        		alleles.add(0.0);
	        	}
	        }
        
    } while(violation == true);
        
        //System.out.println("Generating: " + Evaluator.numberIndvls);
        Evaluator.numberIndvls++;
        
    }

    /**
     * @param partner The solution to compare against
     * @return A number indicating the outcome
     * Return 0 if solution dominates the partner
     * Return 1 if mutually non-dominated - Compare crowding distance in this event
     * Return 2 if solution is dominated by partner
     */
    public int isBetter(StockWeightingSolution partner){
        if(this.solutionReturn > partner.solutionReturn && this.solutionRisk < partner.solutionRisk){
            return 0;
        }
        else if(this.solutionReturn < partner.solutionReturn && this.solutionRisk > partner.solutionRisk){
            return 2;
        }
        else{
            return 1;
        }
    }
    
    /**
     * Used to cut individuals in two and re-combine the pieces to create children
     * @param partner The second parent
     * @return The children bred
     */
    public ArrayList<StockWeightingSolution> singlePointCrossover(StockWeightingSolution partner){
        
    	boolean violation = false;
        ArrayList<StockWeightingSolution> offspring = new ArrayList<>();
        StockWeightingSolution childOne = new StockWeightingSolution(true);
        StockWeightingSolution childTwo = new StockWeightingSolution(true);
        	
        int crossoverPoint = (int)(Math.random() * length);
        
        if(crossoverPoint == 0){
            crossoverPoint++;
        }
        if(crossoverPoint == length){
            crossoverPoint--;
        }
        
         //Breeding the first child
        for(int x = 0; x < crossoverPoint; x++){
            childOne.alleles.set(x, this.alleles.get(x));
        }
        for(int x = crossoverPoint; x < length; x++){
            childOne.alleles.set(x, partner.alleles.get(x));
        }
        
        //Need to normalise the portfolios so all weightings sum to one
        double totalOne = 0;
        
        for(int x = 0; x < length; x++){
            totalOne += childOne.alleles.get(x);
        }
        
        for(int x = 0; x < length; x++){
            childOne.alleles.set(x, childOne.alleles.get(x) / totalOne);
        }
        
        for(int x = 0; x < length; x++){            
            ///If a constraint is violated flag this up and re-generate the solution
            if (childOne.alleles.get(x) < Evaluator.floor || childOne.alleles.get(x) > Evaluator.ceiling){
                violation = true;
                break;
            }
        }
                
        //Breeding the second child
        for(int x = 0; x < crossoverPoint; x++){
            childTwo.alleles.set(x, partner.alleles.get(x));
        }
        for(int x = crossoverPoint; x < length; x++){
            childTwo.alleles.set(x, this.alleles.get(x));
        }
        
        double totalTwo = 0;
        
        for(int x = 0; x < length; x++){
            totalTwo += childTwo.alleles.get(x);
        }
        
        for(int x = 0; x < length; x++){
            childTwo.alleles.set(x, childTwo.alleles.get(x) / totalTwo);
        }
        
        for(int x = 0; x < length; x++){            
            ///If a constraint is violated flag this up and re-generate the solution
            if (childTwo.alleles.get(x) < Evaluator.floor || childTwo.alleles.get(x) > Evaluator.ceiling){
                violation = true;
                break;
            }
        }
        
        if(violation == true){
        	return null;
        }
        
	        
        /**
        System.out.println("Crossover Point: Stock " + crossoverPoint);
        System.out.println("");
        
        System.out.println("Parents");
        this.printDetails();
        System.out.println("");
        partner.printDetails();
        
        System.out.println("");
        System.out.println("Children");
        childOne.printDetails();
        System.out.println("");
        childTwo.printDetails();
        System.out.println("");
        **/
        
        
        //There is a chance a single allele will be set to a random number between the floor and ceiling values
        int mutatePointOne;
        int mutatePointTwo;
        double mutationChanceOne = Math.random();
        double mutationChanceTwo = Math.random();
        double mutationValueOne = 0;
        double mutationValueTwo = 0;
        
        StockWeightingSolution tempChildOne = new StockWeightingSolution(true);
        StockWeightingSolution tempChildTwo = new StockWeightingSolution(true);
        
        for(int x = 0; x < length; x++){
        	tempChildOne.alleles.set(x, childOne.alleles.get(x));
        	tempChildTwo.alleles.set(x, childTwo.alleles.get(x));
        }
        
        do{
        	
            for(int x = 0; x < length; x++){
            	childOne.alleles.set(x, tempChildOne.alleles.get(x));
            	childTwo.alleles.set(x, tempChildTwo.alleles.get(x));
            }
        	       	
        	mutatePointOne = (int)(Math.random() * length);
        	
        	violation = false;
        
	        mutationValueOne = Math.random();
	        
	        boolean mutateOne = false;
	        boolean mutateTwo = false;
	        
	        if(mutationChanceOne > Evaluator.publicMutationRate){
	            childOne.alleles.set(mutatePointOne, mutationValueOne);
	            mutateOne = true;
	        }
	        
	        mutationValueTwo = Math.random();
	        
	        mutatePointTwo = (int)(Math.random() * length);
	        
	        if(mutationChanceTwo > Evaluator.publicMutationRate){
	            childTwo.alleles.set(mutatePointTwo, mutationValueTwo);
	            mutateTwo = true;
	        }
	        
	        //Need to normalise the portfolios so all weightings sum to one
	        totalOne = 0;
	        totalTwo = 0;
	        
	        for(int x = 0; x < length; x++){
	            totalOne += childOne.alleles.get(x);
	            totalTwo += childTwo.alleles.get(x);
	        }
	        
	        //Check to see if the applied mutation breaks any constraints 
	        for(int x = 0; x < length; x++){
	            childOne.alleles.set(x, childOne.alleles.get(x) / totalOne);
	            childTwo.alleles.set(x, childTwo.alleles.get(x) / totalTwo);
                if (childOne.alleles.get(x) < Evaluator.floor || childOne.alleles.get(x) > Evaluator.ceiling || 
                	childTwo.alleles.get(x) < Evaluator.floor || childTwo.alleles.get(x) > Evaluator.ceiling){
                    violation = true;
                    break;
                }
	        }	
	        
	       /**
	       if(violation != true){
		        if(mutateOne){
		            System.out.println("Child One Mutated at Point: " + mutatePointOne);
		            System.out.println("New Value: " + mutationValueOne);
		            System.out.println("");
		            childOne.printDetails();
		            System.out.println("");
		        }
		        if(mutateTwo){
		            System.out.println("Child Two Mutated at Point: " + mutatePointTwo);
		            System.out.println("New Value: " + mutationValueTwo);
		            System.out.println("");
		            childTwo.printDetails();
		            System.out.println("");
		        }
	       }
	       **/
	        
        
        }while(violation == true);      
        
        offspring.add(childOne);
        offspring.add(childTwo);       
        
        //Now that the weightings have been finalised assign the return and risk 
        for(int x = 0; x < offspring.size(); x++){
            offspring.get(x).solutionReturn = Evaluator.theReturn(offspring.get(x));
            offspring.get(x).solutionRisk = Evaluator.theVolatility(offspring.get(x));
            
            offspring.get(x).setSOfitness(Evaluator.evaluateFitness(offspring.get(x)));
            
        }
        
        return offspring;
               
    }
    
    /**
     * Print the weightings of the solution and tally them up
     */
    public void printDetails(){
        double total = 0;
        for(int x = 0; x < length; x++){
            System.out.println("Stock:" + x + " " + alleles.get(x) * 100);
            total += alleles.get(x) * 100;
        }
        System.out.println("Total: " + total);
    }
    
    
    public void printFullDetails(){
        
        String dominations = "";
        //Holds the list of what solutions a specific solution dominates
        ArrayList<StockWeightingSolution> dominatedList = new ArrayList<>();
        dominations = "Dominates Portfolios: ";
        dominatedList.clear();
        System.out.println("ID: " + this.id);
        this.printDetails();
        System.out.println("Return: " + Evaluator.theReturn(this) * 100);
        System.out.println("Risk: " + Evaluator.theVolatility(this));
        System.out.println("True Domination Count : " + this.getTrueDomCount());
        dominatedList.addAll(this.returnDominates());
        for(int y = 0; y < this.returnDominates().size(); y++){
            dominations += dominatedList.get(y).id + " ";
        }
        System.out.println(dominations);
        System.out.println("Fitness: " + this.fitness);
        System.out.println("Return Distance: " + this.getReturnDistance());
        System.out.println("Risk Distance: " + this.getRiskDistance());
        System.out.println("Crowding Distance: " + this.getCrowdingDistance());
        System.out.println("\n");
    
    }
    
    public int getLength(){
        return this.length;
    }
    
    /**
     * Set the number of solutions this particular solution is dominated by
     * @param newNum The number of solutions this solution is dominated by
     */
    public void setDominatedBy(int newNum){
        dominatedBy = newNum;
        trueDomCount = newNum;
    }
    
    /**
     * Increment the number of solutions this one is dominated by
     */
    public void incrementDominatedBy(){
        dominatedBy++;
        trueDomCount++;
    }
    
    /**
     * Decrement the number of solutions this one is dominated by
     */
    public void decrementDominatedBy(){
        dominatedBy--;
    }
    
    /**
     * 
     * @return The number of solutions this one is dominated by
     */
    public int getDominatedBy(){
        return dominatedBy;
    }
    
    /**
     * During non-dominated sorting the domination counter is decremented.
     * Therefore for testing purposes the true count is retained
     * @return The number of solutions this one is dominated by
     */
    public int getTrueDomCount(){
        return trueDomCount;
    }
    
    /**
     * Add a solution to the set of solutions dominated by this one
     * @param newDomination The solution dominated by this one
     */
    public void addDomination(StockWeightingSolution newDomination){
        dominates.add(newDomination);
    }
    
    /**
     * Set the array of solutions dominated by this solution
     * @param newArray The solutions dominated by this solution
     */
    public void setDominations(ArrayList<StockWeightingSolution> newArray){
        dominates.clear();
        for(int x = 0; x < newArray.size();x++){
            dominates.add(newArray.get(x));
        }
    }
    
    /**
     * 
     * @return The array of solutions dominated by this one
     */
    public ArrayList<StockWeightingSolution> returnDominates(){
        return dominates;
    }
    
    /**
     * Empty the array of solutions dominated by this one
     */
    public void clearDominations(){
        dominates.clear();
    }
    
    public double getReturnDistance(){
        return this.returnDistance;
    }
    
    public void setReturnDistance(double newDistance){
        this.returnDistance = newDistance;
    }
    
    public double getRiskDistance(){
        return this.riskDistance;
    }
    
    public void setRiskDistance(double newDistance){
        this.riskDistance = newDistance;
    }
    
    public double getCrowdingDistance(){
        return this.crowdingDistance;
    }
    
    public void setCrowdingDistance(double newDistance){
        this.crowdingDistance = newDistance;
    }
    
    public void incrementCrowdingDistance(double distance){
        this.crowdingDistance += distance;
    }
    
    public void setSOfitness(double newFitness){
    	this.SOfitness = newFitness;
    }
    
    public double getSOfitness(){
    	return this.SOfitness;
    }
    
}
