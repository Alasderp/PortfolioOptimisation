

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 *
 * @author Alasdair Stuart 1306348
 */
public class Population {
    
    
    //Size of the population
    private int populationSize;
    //Stores or the individuals in the population
    private ArrayList<StockWeightingSolution> theIndividuals;
    //Stores the various fronts in a 2D ArrayList to separate them
    private ArrayList<ArrayList<StockWeightingSolution>> frontList;
    
    public Population(int size){
        populationSize = size;
        theIndividuals = new ArrayList<>();           
    }
    
    /**
     * Randomly generate the individuals in the population
     */
    public void initialisation(){
        for(int x = 0; x < populationSize; x++){
            theIndividuals.add(new StockWeightingSolution(false));
            theIndividuals.get(x).id = x;
            frontList = new ArrayList<>();
        }
    }
    
    /**
     * This is used to find our pareto front of non-dominated solutions
     */
    public void nonDominatedSorting(int popSize){
        
        //Stores the current front of non-dominated solutions
        ArrayList<StockWeightingSolution> currentFront = new ArrayList<>();
        //While ignoring the current front, stores the next set of non-dominated solutions
        ArrayList<StockWeightingSolution> nextFront = new ArrayList<>();
        //Indicates which front is the next one to be created
        int frontCounter = 1;
        
        frontList.clear();
        frontList.add(new ArrayList<StockWeightingSolution>());
        
        System.out.println("NON-DOMINATED SORTING TAKING PLACE");
        
        for(int x = 0; x < popSize; x++){
            
            theIndividuals.get(x).setDominatedBy(0);
            theIndividuals.get(x).clearDominations();
            theIndividuals.get(x).fitness = 0;
            
            for(int y = 0; y < popSize; y++){
                
                if(theIndividuals.get(x).isBetter(theIndividuals.get(y)) == 0){
                    theIndividuals.get(x).addDomination(theIndividuals.get(y));
                }
                else if(theIndividuals.get(x).isBetter(theIndividuals.get(y)) == 2){
                    theIndividuals.get(x).incrementDominatedBy();
                }              
            }
            //If the solution is non-dominated set fitness to 0 - it belongs to the first front i.e. the pareto front
            if(theIndividuals.get(x).getDominatedBy() == 0){
                theIndividuals.get(x).fitness = 0;
                currentFront.add(theIndividuals.get(x));
                frontList.get(0).add(theIndividuals.get(x));
            }
        }

        //Now that the pareto front has been found, we must find the subsequent fronts
        while(!currentFront.isEmpty()){
        
            for(int x = 0; x < currentFront.size(); x++){

                //For every solution in the curret front, loop through the solutions it dominates
                //Decrement the domination count for each of these solutions
                //If this domination count reaches 0, this means they belong to the next front
                for(int y = 0; y < currentFront.get(x).returnDominates().size(); y++){
                    currentFront.get(x).returnDominates().get(y).decrementDominatedBy();
                    if(currentFront.get(x).returnDominates().get(y).getDominatedBy() == 0){
                        nextFront.add(currentFront.get(x).returnDominates().get(y));
                        currentFront.get(x).returnDominates().get(y).fitness = frontCounter;
                    }
                }
                
            }
            
            frontList.add(frontCounter, new ArrayList<StockWeightingSolution>());
            frontList.get(frontCounter).addAll(nextFront);
            
            frontCounter++;
            currentFront.clear();
            currentFront.addAll(nextFront);
            nextFront.clear();
        }
               
    }
    
    /**
     * This function is used to estimate how densely populated the area surrounding an individual is
     * The higher the crowding distance, the more sparsely populated the area
     * @param front The set of individuals whose crowding distance is to be calculated
     */
    public void crowdingDistance(ArrayList<StockWeightingSolution> front){
        
        System.out.println("CROWDING DISTANCE CALCULATION TAKING PLACE");
        
        for(int x = 0; x < front.size(); x++){
            front.get(x).setReturnDistance(0);
            front.get(x).setRiskDistance(0);
            front.get(x).setCrowdingDistance(0);
        }
        
        //First sort by return
        Collections.sort(front, new ReturnComparator());
        
        //The crowding distance of boundary points is set to be infinite
        front.get(0).setCrowdingDistance(Double.POSITIVE_INFINITY);
        front.get(front.size() - 1).setCrowdingDistance(Double.POSITIVE_INFINITY);
        
        //Need this for normalisation, otherwise the risk distance wouldn't be comparable to the return distance
        double maxMin = front.get(0).solutionReturn - front.get(front.size() - 1).solutionReturn;
        
        //For all individuals between the boundary points, find the difference in return of their immediate neighbours
        for(int x = 1; x < front.size() - 1; x++){
            front.get(x).setReturnDistance((front.get(x - 1).solutionReturn - front.get(x + 1).solutionReturn) / maxMin);
        }
        
        //Order by risk
        Collections.sort(front, new RiskComparator());
        
        //Boundary points are infinite
        front.get(0).setCrowdingDistance(Double.POSITIVE_INFINITY);
        front.get(front.size() - 1).setCrowdingDistance(Double.POSITIVE_INFINITY);
        
        //Once again needed for normalisation to scale the distances appropriately
        maxMin = front.get(0).solutionRisk - front.get(front.size() - 1).solutionRisk;
        
        //For all individuals between the boundary points, find the difference in risk of their immediate neighbours
        for(int x = 1; x < front.size() - 1; x++){
            front.get(x).setRiskDistance((front.get(x - 1).solutionRisk - front.get(x + 1).solutionRisk) / maxMin);
        }
        
        //Sum the risk and return distance to calculate the crowding distance for an individual
        for(int x = 0; x < front.size(); x++){
            front.get(x).incrementCrowdingDistance(front.get(x).getReturnDistance() + front.get(x).getRiskDistance());
        }
        
    }
    
    /**
     * This is where evolution takes place and one generation transitions to the next
     */
    public void evolvePopulation(){
        
    	System.out.println("Evolution Cycle: " + Evaluator.evolutionCycle);
    	Evaluator.evolutionCycle++;
    	
        ArrayList<StockWeightingSolution> offSpring = new ArrayList<>();
        
        //Binary tournament selection is used to pick parents
        int randNumOne = 0;
        int randNumTwo = 0;
        
        StockWeightingSolution parentOne = new StockWeightingSolution(true);
        StockWeightingSolution parentTwo = new StockWeightingSolution(true);
        
        ArrayList<StockWeightingSolution> tempArray = new ArrayList<>();
        
        while(offSpring.size() < populationSize){
            
        	//Do this until children are created without violating constraints
            do{
            	
            	if(tempArray != null){
            		tempArray.clear();
            	}
        	
	        	//Randomly pick two individuals, the superior one is selected as the first parent
	            randNumOne = (int)(Math.random() * populationSize);
	            randNumTwo = (int)(Math.random() * populationSize);
	            
	            //System.out.println("CANDIDATE ONE");
	            //theIndividuals.get(randNumOne).printFullDetails();
	            
	            //System.out.println("CANDIDATE TWO");
	            //theIndividuals.get(randNumTwo).printFullDetails();
	            
	            if(theIndividuals.get(randNumOne).fitness < theIndividuals.get(randNumTwo).fitness){
	                parentOne = theIndividuals.get(randNumOne);
	            }
	            else if(theIndividuals.get(randNumOne).fitness > theIndividuals.get(randNumTwo).fitness){
	                parentOne = theIndividuals.get(randNumTwo);
	            }
	            //USE CROWDING COMPARISON HERE AS TIEBREAKER      
	            else if(theIndividuals.get(randNumOne).getCrowdingDistance() > theIndividuals.get(randNumTwo).getCrowdingDistance()){
	                parentOne = theIndividuals.get(randNumOne);             
	            }
	            
	            else{
	                parentOne = theIndividuals.get(randNumTwo);
	            }
	            
	            //System.out.println("WINNER");
	            //parentOne.printDetails();
	            
	            //Make sure same solution isn't selected twice
	            do{
	            
		            //Randomly pick two individuals, the superior one is selected as the partner
		            randNumOne = (int)(Math.random() * populationSize);
		            randNumTwo = (int)(Math.random() * populationSize);
		            
		            //System.out.println("CANDIDATE ONE");
		            //theIndividuals.get(randNumOne).printFullDetails();
		            
		            //System.out.println("CANDIDATE TWO");
		            //theIndividuals.get(randNumTwo).printFullDetails();
		            
		            if(theIndividuals.get(randNumOne).fitness < theIndividuals.get(randNumTwo).fitness){
		                parentTwo = theIndividuals.get(randNumOne);
		            }
		            else if(theIndividuals.get(randNumOne).fitness > theIndividuals.get(randNumTwo).fitness){
		                parentTwo = theIndividuals.get(randNumTwo);
		            }
		            //USE CROWDING COMPARISON HERE ONCE IMPLEMENTED AS TIEBREAKER
		            
		            else if((theIndividuals.get(randNumOne).getCrowdingDistance() > theIndividuals.get(randNumTwo).getCrowdingDistance())){
		                parentTwo = theIndividuals.get(randNumOne);
		            }
		            
		            else {
		                parentTwo = theIndividuals.get(randNumTwo);
		            }
		            
		            //System.out.println("WINNER");
		            //parentTwo.printDetails();
	            
	            }while(parentOne == parentTwo);
            
	            tempArray = parentOne.singlePointCrossover(parentTwo);
	            
            }while(tempArray == null);
            
            //Breed children and add them into the child population
            offSpring.addAll(tempArray);
            
        }
        
        //System.out.println("!!!!!!CHILD POPULATION!!!!!!");
        //this.printPopDetails(offSpring);
        
        //Combine the main population with the child population
        this.theIndividuals.addAll(offSpring);
        
        for(int x = 0; x < this.theIndividuals.size(); x++){
            this.theIndividuals.get(x).id = x;
        }
        
        //Re-sort the populuation
        this.nonDominatedSorting(theIndividuals.size());
        this.crowdingDistance(theIndividuals);
        
        //System.out.println("!!!!!!COMBINED POPULATION!!!!!!");
        //this.printPopDetails(theIndividuals);   
        
        ArrayList<StockWeightingSolution> nextGeneration = new ArrayList<>();
        
        //Now truncate the population keeping only the best individuals
        int counter = 0;
        for(int x = 0; x < this.frontList.size(); x++){
            
            if(counter + this.frontList.get(x).size() > this.populationSize){
                //System.out.println("LAST FRONT TOO LARGE, ORDERING BY CROWDING DISTANCE");
                //System.out.println("UNORDERED FRONT");
                //this.printPopDetails(this.frontList.get(x));
                Collections.sort(this.frontList.get(x), new CrowdingDistanceComparator());
                //System.out.println("ORDERED FRONT");
                //this.printPopDetails(this.frontList.get(x));           
            }
            
            for(int y = 0; y < this.frontList.get(x).size(); y++){
                if(counter == this.populationSize){
                    break;
                }
                nextGeneration.add(this.frontList.get(x).get(y));
                counter++;
            }
            
            if(counter == this.populationSize){
                    break;
            }
        }
    
        
        this.theIndividuals.clear();
        this.theIndividuals.addAll(nextGeneration);
        
        //Re-sort the populuation
        this.nonDominatedSorting(theIndividuals.size());
        this.crowdingDistance(theIndividuals);
        
        for(int x = 0; x < this.theIndividuals.size(); x++){
            this.theIndividuals.get(x).id = x;
        }
        
        //System.out.println("!!!!!!NEXT GENERATION!!!!!!");
        //this.printPopDetails(theIndividuals);
        
        
    }
    
    public ArrayList<StockWeightingSolution> returnPop(){
        return theIndividuals;
    }
    
    public ArrayList<ArrayList<StockWeightingSolution>> getFrontList(){
        return frontList;
    }
    
    public double getAverageSOfitness(){
    	double soFitness = 0;
    	for(int x = 0; x < this.populationSize; x++){
    		soFitness += this.theIndividuals.get(x).getSOfitness();
    	}
    	return soFitness / this.populationSize;
    }
    
    public void printPopDetails(ArrayList<StockWeightingSolution> popArray){
        
        String dominations = "";
        //Holds the list of what solutions a specific solution dominates
        ArrayList<StockWeightingSolution> dominatedList = new ArrayList<>();
        for(int x = 0; x < popArray.size(); x++){
            dominations = "Dominates Portfolios: ";
            dominatedList.clear();
            Collections.sort(popArray, new FitnessComparator());
            System.out.println("ID: " + popArray.get(x).id);
            popArray.get(x).printDetails();
            System.out.println("Return: " + popArray.get(x).solutionReturn * 100);
            System.out.println("Risk: " + popArray.get(x).solutionRisk);
            System.out.println("True Domination Count : " + popArray.get(x).getTrueDomCount());
            dominatedList.addAll(popArray.get(x).returnDominates());
            for(int y = 0; y < popArray.get(x).returnDominates().size(); y++){
                dominations += dominatedList.get(y).id + " ";
            }
            System.out.println(dominations);
            System.out.println("Fitness: " + popArray.get(x).fitness);
            System.out.println("Return Distance: " + popArray.get(x).getReturnDistance());
            System.out.println("Risk Distance: " + popArray.get(x).getRiskDistance());
            System.out.println("Crowding Distance: " + popArray.get(x).getCrowdingDistance());
            System.out.println("\n");
            
        }
        
        
    }
    
}
