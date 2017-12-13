
import java.util.ArrayList;
import java.awt.Shape;
import java.util.Arrays;
import java.util.Collections;
import java.awt.Color;

/**
import java.io.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.chart.ChartUtilities; 
**/

import java.io.*;
import org.jfree.chart.*;
import org.jfree.chart.*;
import org.jfree.data.xy.*;
import org.jfree.util.ShapeUtilities;
import org.jfree.chart.plot.*;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.*;
import org.jfree.chart.*; 
/**
 * @author Alasdair Stuart 1306348
 */
public class PortfolioOptimisation {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)  throws Exception {
        
        Evaluator.initialise();
        
        //Stock aal = new Stock("aal", 0.5);       
        //aal.loadData();
        //aal.printDetails();
        //System.out.println(aal.stockReturn("4-Jan-10", "8-Nov-16", true) * 100);
        //System.out.println(aal.stockFullVolatility(aal.stockPartialVolatility("4-Jan-10", "8-Nov-16", 21)));
        
        //Stock barc = new Stock("barc", 0.9);       
        //barc.loadData();
        
        //Stock rr = new Stock("rr", 0.05);
        //rr.loadData();
        
        //Stock cine = new Stock("cine", 0.05);
        //cine.loadData();
        
        //barc.printDetails();
        //System.out.println(barc.stockReturn("4-Jan-10", "8-Nov-16", true) * 100);
        //System.out.println(barc.stockFullVolatility(barc.stockPartialVolatility("4-Jan-10", "8-Nov-16", 21)));
        //System.out.println(barc.stockPartialVolatility("4-Jan-10", "8-Nov-16", 5));
        
        //rr.printDetails();
        //System.out.println(rr.stockReturn("4-Jan-10", "8-Nov-16", true));
        //System.out.println(rr.stockFullVolatility(rr.stockPartialVolatility("4-Jan-10", "8-Nov-16", 21)));
        //System.out.println(rr.stockPartialVolatility("4-Jan-10", "8-Nov-16", 5));
        
        //System.out.println(cine.stockReturn("4-Jan-10", "8-Nov-16", true));
        //System.out.println(cine.stockFullVolatility(cine.stockPartialVolatility("4-Jan-10", "8-Nov-16", 21)));
        
        //Portfolio portfolio = new Portfolio();
        //portfolio.addStock(barc);
        //portfolio.addStock(rr);
        //portfolio.addStock(cine);
        //System.out.println(portfolio.getReturn("4-Jan-10", "8-Nov-16"));
        //System.out.println(portfolio.correlationCoefficient(barc, rr, "4-Jan-10", "8-Nov-16", 5));
        //System.out.println(portfolio.portfolioVolatility("4-Jan-10", "8-Nov-16", 5));
               
        
        /**
        
        StockWeightingSolution newIndividual = new StockWeightingSolution();
        newIndividual.printDetails();
        
        Evaluator.initialise();
        
        Evaluator.evaluateFitness(newIndividual);
        System.out.println(newIndividual.fitness);
        
        StockWeightingSolution newIndividual = new StockWeightingSolution();
        StockWeightingSolution bestIndividual = new StockWeightingSolution();
        
        for(int x = 0; x < 500; x++){
            
            Evaluator.evaluateFitness(newIndividual);
            Evaluator.evaluateFitness(bestIndividual);
            
            if(newIndividual.fitness > bestIndividual.fitness){
                bestIndividual = newIndividual;
            }
            
            newIndividual = new StockWeightingSolution();
            
        }
        
        bestIndividual.printDetails();
        System.out.println(bestIndividual.fitness);
        System.out.println(Evaluator.totalSolutions);
        
        **/

//Make a new population
Evaluator.floor = 0.00;
Evaluator.ceiling = 1.0;
Evaluator.startDate = "4-Jan-10";
Evaluator.endDate = "31-Dec-10";
Evaluator.publicMutationRate = 0.8;

Population pop = new Population(10);  
pop.initialisation();
ArrayList<StockWeightingSolution> popArray = pop.returnPop();
pop.nonDominatedSorting(popArray.size());
pop.crowdingDistance(popArray);

System.out.println("!!!!!!!INITIAL POPULATION!!!!!!");

//Collections.sort(popArray, new ReturnComparator());
//Collections.sort(popArray, new RiskComparator());
//Collections.sort(popArray, new CrowdingDistanceComparator());

pop.printPopDetails(popArray);
XYSeries GenZero = new XYSeries( "Gen Zero" );
XYSeries RunOne = new XYSeries("2010", false);
XYSeries ParetoOne = new XYSeries("2010");
popArray = pop.returnPop();
ArrayList<ArrayList<StockWeightingSolution>> frontArray = pop.getFrontList();
ArrayList<StockWeightingSolution> originalParetoReused = new ArrayList<>();
for(int x = 0; x < frontArray.get(0).size(); x++){
	GenZero.add(frontArray.get(0).get(x).solutionReturn * 100, frontArray.get(0).get(x).solutionRisk);
}

//for(int y = 0; y < popArray.size(); y++){
//	GenZero.add(popArray.get(y).solutionReturn * 100, popArray.get(y).solutionRisk);
//}


RunOne.add(pop.getAverageSOfitness(), 0);

ArrayList<XYSeries> graphArray = new ArrayList<>();
for(int x = 0; x < 2; x++){
		graphArray.add(new XYSeries("Gen " + (x + 1)));
}

for(int x = 0; x < graphArray.size(); x++){
        pop.evolvePopulation(); 
        popArray = pop.returnPop();
        frontArray = pop.getFrontList();
	        for(int y = 0; y < frontArray.get(0).size(); y++){
	        	graphArray.get(x).add(frontArray.get(0).get(y).solutionReturn * 100, frontArray.get(0).get(y).solutionRisk);
        }
    RunOne.add(pop.getAverageSOfitness(), x + 1);
}

for(int x = 0; x < frontArray.get(0).size(); x++){
	ParetoOne.add(frontArray.get(0).get(x).solutionReturn * 100, frontArray.get(0).get(x).solutionRisk);
	System.out.println("Pareto Front");
	frontArray.get(0).get(x).printFullDetails();
	originalParetoReused.add(frontArray.get(0).get(x));
}

XYSeriesCollection dataset = new XYSeriesCollection( );
dataset.addSeries(GenZero);
for(int x = 0; x < graphArray.size(); x++){
	if((x + 1) % 3 == 0){
		dataset.addSeries(graphArray.get(x));
	}
}

XYSeriesCollection runLengthDistribution = new XYSeriesCollection( );
runLengthDistribution.addSeries(RunOne);

XYSeriesCollection paretoComparison = new XYSeriesCollection( );
paretoComparison.addSeries(ParetoOne);

JFreeChart xylineChart = ChartFactory.createXYLineChart(
        "Generations of Algorithm", 
        "Return",
        "Volatility", 
        dataset,
        PlotOrientation.VERTICAL, 
        true, true, false);

XYPlot plot = xylineChart.getXYPlot();
XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
plot.setRenderer(renderer);
plot.setBackgroundPaint(Color.DARK_GRAY);
     
     int width = 1280;
     int height = 960;
     File XYChart = new File( "2010.jpeg" ); 
     ChartUtilities.saveChartAsJPEG( XYChart, xylineChart, width, height);
  

//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!NEW RUN OF ALGORITHM!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 pop = new Population(100);  
 //Evaluator.ceiling = 0.25;
 //Evaluator.publicMutationRate = 0.9;
 Evaluator.startDate = "4-Jan-11";
 Evaluator.endDate = "30-Dec-11";
 pop.initialisation();
 
 for(int x = 0; x < 15; x++){
	   pop.returnPop().get(x).alleles.set(x, 0.986);
	   for(int y = 0; y < 15; y++){
		   if(y != x){
			   pop.returnPop().get(x).alleles.set(y, 0.001);
		   }
	   }
	   pop.returnPop().get(x).solutionReturn = Evaluator.theReturn(pop.returnPop().get(x));
	   pop.returnPop().get(x).solutionRisk = Evaluator.theVolatility(pop.returnPop().get(x));
	   pop.returnPop().get(x).setSOfitness(Evaluator.evaluateFitness( pop.returnPop().get(x)));
 }
 
 pop.printPopDetails(pop.returnPop());
 
 popArray = pop.returnPop();
 pop.nonDominatedSorting(popArray.size());
 pop.crowdingDistance(popArray);
 
 System.out.println("!!!!!!!INITIAL POPULATION!!!!!!");
 
 //Collections.sort(popArray, new ReturnComparator());
 //Collections.sort(popArray, new RiskComparator());
 //Collections.sort(popArray, new CrowdingDistanceComparator());
 
 pop.printPopDetails(popArray);
 GenZero = new XYSeries( "Generation Zero" );
 XYSeries RunTwo = new XYSeries("2011", false);
 XYSeries ParetoTwo = new XYSeries("Front Generated using 2011 Data");
 //XYSeries EqualPortfolioIn2011 = new XYSeries("Equally Weighted Portfolio");
 //XYSeries originalParetoIn2011 = new XYSeries("Performance of 2010 Pareto Front Portfolios in 2011");
 //StockWeightingSolution equalPortfolio = new StockWeightingSolution(true);
 
 XYSeries BARC = new XYSeries("BARC", false);
 XYSeries RR = new XYSeries("RR", false);
 XYSeries CINE= new XYSeries("CINE", false);
 XYSeries AAL = new XYSeries("AAL", false);
 XYSeries BLT = new XYSeries("BLT", false);
 XYSeries RDSA = new XYSeries("RDSA", false);
 XYSeries RRS = new XYSeries("RRS", false);
 XYSeries IGG = new XYSeries("IGG", false);
 XYSeries SGC = new XYSeries("SGC", false);
 XYSeries TATE = new XYSeries("TATE", false);
 XYSeries GRG = new XYSeries("GRG", false);
 XYSeries GNK = new XYSeries("GNK", false);
 XYSeries BBY = new XYSeries("BBY", false);
 XYSeries ADN = new XYSeries("ADN", false);
 XYSeries RBS = new XYSeries("RBS", false);
 
 Stock barc1 = new Stock("barc", 1.0);       
 barc1.loadData();
 Stock rr1 = new Stock("rr", 1.0);
 rr1.loadData();
 Stock cine1 = new Stock("cine", 1.0);
 cine1.loadData();
 Stock aal1 = new Stock("aal", 1.0);
 aal1.loadData();
 Stock blt1 = new Stock("blt", 1.0);
 blt1.loadData();
 Stock rdsa1 = new Stock("rdsa", 1.0);
 rdsa1.loadData(); 
 Stock rrs1 = new Stock("rrs", 1.0);
 rrs1.loadData(); 
 Stock igg1 = new Stock("igg", 1.0);
 igg1.loadData(); 
 Stock sgc1 = new Stock("sgc", 1.0);
 sgc1.loadData();
 Stock tate1 = new Stock("tate", 1.0);
 tate1.loadData();
 Stock grg1 = new Stock("grg", 1.0);
 grg1.loadData();
 Stock gnk1 = new Stock("gnk", 1.0);
 gnk1.loadData(); 
 Stock bby1 = new Stock("bby", 1.0);
 bby1.loadData(); 
 Stock adn1 = new Stock("adn", 1.0);
 adn1.loadData();
 Stock rbs1 = new Stock("rbs", 1.0);
 rbs1.loadData();

 Stock barc2 = new Stock("barc", 0.0);       
 barc2.loadData();
 Stock rr2 = new Stock("rr", 0.0);
 rr2.loadData();
 Stock cine2 = new Stock("cine", 0.0);
 cine2.loadData();
 Stock aal2 = new Stock("aal", 0.0);
 aal2.loadData();
 Stock blt2 = new Stock("blt", 0.0);
 blt2.loadData();
 Stock rdsa2 = new Stock("rdsa", 0.0);
 rdsa2.loadData(); 
 Stock rrs2 = new Stock("rrs", 0.0);
 rrs2.loadData(); 
 Stock igg2 = new Stock("igg", 0.0);
 igg2.loadData(); 
 Stock sgc2 = new Stock("sgc", 0.0);
 sgc2.loadData();
 Stock tate2 = new Stock("tate", 0.0);
 tate2.loadData();
 Stock grg2 = new Stock("grg", 0.0);
 grg2.loadData();
 Stock gnk2 = new Stock("gnk", 0.0);
 gnk2.loadData(); 
 Stock bby2 = new Stock("bby", 0.0);
 bby2.loadData(); 
 Stock adn2 = new Stock("adn", 0.0);
 adn2.loadData();
 Stock rbs2 = new Stock("rbs", 0.0);
 rbs2.loadData();
 
 Portfolio barc = new Portfolio();
 barc.addStock(barc1);
 barc.addStock(barc2);
 
 Portfolio rr = new Portfolio();
 rr.addStock(rr1);
 rr.addStock(rr2);
 
 Portfolio cine = new Portfolio();
 cine.addStock(cine1);
 cine.addStock(cine2);
 
 Portfolio aal = new Portfolio();
 aal.addStock(aal1);
 aal.addStock(aal2);
 
 Portfolio blt = new Portfolio();
 blt.addStock(blt1);
 blt.addStock(blt2);
 
 Portfolio rdsa = new Portfolio();
 rdsa.addStock(rdsa1);
 rdsa.addStock(rdsa2);
 
 Portfolio rrs = new Portfolio();
 rrs.addStock(rrs1);
 rrs.addStock(rrs2);
 
 Portfolio igg = new Portfolio();
 igg.addStock(igg1);
 igg.addStock(igg2);
 
 Portfolio sgc = new Portfolio();
 sgc.addStock(sgc1);
 sgc.addStock(sgc2);
 
 Portfolio tate = new Portfolio();
 tate.addStock(tate1);
 tate.addStock(tate2);
 
 Portfolio grg = new Portfolio();
 grg.addStock(grg1);
 grg.addStock(grg2);
 
 Portfolio gnk = new Portfolio();
 gnk.addStock(gnk1);
 gnk.addStock(gnk2);
 
 Portfolio bby = new Portfolio();
 bby.addStock(bby1);
 bby.addStock(bby2);
 
 Portfolio adn = new Portfolio();
 adn.addStock(adn1);
 adn.addStock(adn2);
 
 Portfolio rbs = new Portfolio();
 rbs.addStock(rbs1);
 rbs.addStock(rbs2);

 
 
 /**
 for(int x = 0; x < equalPortfolio.alleles.size(); x++){
	 equalPortfolio.alleles.set(x, (100.0/15.0)/100.0);
 }
 **/
 
 popArray = pop.returnPop();
frontArray = pop.getFrontList();
for(int x = 0; x < frontArray.get(0).size(); x++){
	GenZero.add(frontArray.get(0).get(x).solutionReturn * 100, frontArray.get(0).get(x).solutionRisk);
}
 
 RunTwo.add(pop.getAverageSOfitness(), 0);
 
 
graphArray = new ArrayList<>();

for(int x = 0; x < 21; x++){
	graphArray.add(new XYSeries("Gen " + (x + 1)));
}

for(int x = 0; x < graphArray.size(); x++){
        pop.evolvePopulation(); 
        popArray = pop.returnPop();
        frontArray = pop.getFrontList();
        for(int y = 0; y < frontArray.get(0).size(); y++){
        	graphArray.get(x).add(frontArray.get(0).get(y).solutionReturn * 100, frontArray.get(0).get(y).solutionRisk);
        }
    RunTwo.add(pop.getAverageSOfitness(), x + 1);
}

for(int x = 0; x < frontArray.get(0).size(); x++){
	ParetoTwo.add(frontArray.get(0).get(x).solutionReturn * 100, frontArray.get(0).get(x).solutionRisk);
	//originalParetoIn2011.add(Evaluator.theReturn(originalParetoReused.get(x)) * 100, Evaluator.theVolatility(originalParetoReused.get(x)));
}

for(int x = 0; x < originalParetoReused.size(); x++){
	//originalParetoIn2011.add(Evaluator.theReturn(originalParetoReused.get(x)) * 100, Evaluator.theVolatility(originalParetoReused.get(x)));
}


//EqualPortfolioIn2011.add(Evaluator.theReturn(equalPortfolio) * 100, Evaluator.theVolatility(equalPortfolio));

dataset = new XYSeriesCollection( );
dataset.addSeries(GenZero);
for(int x = 0; x < graphArray.size(); x++){
	if((x + 1) % 3 == 0){
		dataset.addSeries(graphArray.get(x));
	}
}
 
 runLengthDistribution.addSeries(RunTwo);
 
 paretoComparison.addSeries(ParetoTwo);
 
 xylineChart = ChartFactory.createXYLineChart(
         "Generations of Algorithm", 
         "Return",
         "Volatility", 
         dataset,
         PlotOrientation.VERTICAL, 
         true, true, false);
 
 plot = xylineChart.getXYPlot();
 renderer = new XYLineAndShapeRenderer();
 plot.setRenderer(renderer);
 plot.setBackgroundPaint(Color.DARK_GRAY);
      
width = 1280; //Width of the image     
height = 960; //Height of the image  
XYChart = new File( "2011.jpeg" ); 
      ChartUtilities.saveChartAsJPEG( XYChart, xylineChart, width, height);
         
  BARC.add(barc.getReturn(Evaluator.startDate, Evaluator.endDate) * 100, barc.portfolioVolatility(Evaluator.startDate, Evaluator.endDate, 21));
  RR.add(rr.getReturn(Evaluator.startDate, Evaluator.endDate) * 100, rr.portfolioVolatility(Evaluator.startDate, Evaluator.endDate, 21));
  CINE.add(cine.getReturn(Evaluator.startDate, Evaluator.endDate) * 100, cine.portfolioVolatility(Evaluator.startDate, Evaluator.endDate, 21));
  AAL.add(aal.getReturn(Evaluator.startDate, Evaluator.endDate) * 100, aal.portfolioVolatility(Evaluator.startDate, Evaluator.endDate, 21));
  BLT.add(blt.getReturn(Evaluator.startDate, Evaluator.endDate) * 100, blt.portfolioVolatility(Evaluator.startDate, Evaluator.endDate, 21));
  RDSA.add(rdsa.getReturn(Evaluator.startDate, Evaluator.endDate) * 100, rdsa.portfolioVolatility(Evaluator.startDate, Evaluator.endDate, 21));
  RRS.add(rrs.getReturn(Evaluator.startDate, Evaluator.endDate) * 100, rrs.portfolioVolatility(Evaluator.startDate, Evaluator.endDate, 21));
  IGG.add(igg.getReturn(Evaluator.startDate, Evaluator.endDate) * 100 , igg.portfolioVolatility(Evaluator.startDate, Evaluator.endDate, 21));
  SGC.add(sgc.getReturn(Evaluator.startDate, Evaluator.endDate) * 100 , sgc.portfolioVolatility(Evaluator.startDate, Evaluator.endDate, 21));
  TATE.add(tate.getReturn(Evaluator.startDate, Evaluator.endDate) * 100 , tate.portfolioVolatility(Evaluator.startDate, Evaluator.endDate, 21));
  GRG.add(grg.getReturn(Evaluator.startDate, Evaluator.endDate) * 100 , grg.portfolioVolatility(Evaluator.startDate, Evaluator.endDate, 21));
  GNK.add(gnk.getReturn(Evaluator.startDate, Evaluator.endDate) * 100 , gnk.portfolioVolatility(Evaluator.startDate, Evaluator.endDate, 21));
  BBY.add(bby.getReturn(Evaluator.startDate, Evaluator.endDate) * 100 , bby.portfolioVolatility(Evaluator.startDate, Evaluator.endDate, 21));
  ADN.add(adn.getReturn(Evaluator.startDate, Evaluator.endDate) * 100 , adn.portfolioVolatility(Evaluator.startDate, Evaluator.endDate, 21));
  RBS.add(rbs.getReturn(Evaluator.startDate, Evaluator.endDate) * 100 , rbs.portfolioVolatility(Evaluator.startDate, Evaluator.endDate, 21)); 
       
      
 dataset = new XYSeriesCollection( );
 //dataset.addSeries(originalParetoIn2011);
// dataset.addSeries(EqualPortfolioIn2011);
 dataset.addSeries(ParetoTwo);
 dataset.addSeries(BARC);
 dataset.addSeries(RR);
 dataset.addSeries(CINE);
 dataset.addSeries(AAL);
 dataset.addSeries(BLT);
 dataset.addSeries(RDSA);
 dataset.addSeries(RRS);
 dataset.addSeries(IGG);
 dataset.addSeries(SGC);
 dataset.addSeries(TATE);
 dataset.addSeries(GRG);
 dataset.addSeries(GNK);
 dataset.addSeries(BBY);
 dataset.addSeries(ADN);
 dataset.addSeries(RBS);
 
 
 xylineChart = ChartFactory.createXYLineChart(
		 "Performance of Optimised Portfolios In 2011 and Individual Shares",
         "Return",
         "Volatility", 
         dataset,
         PlotOrientation.VERTICAL, 
         true, true, false);
 
 Shape cross = ShapeUtilities.createDiagonalCross(5, 2);
 plot = xylineChart.getXYPlot();
 renderer = new XYLineAndShapeRenderer();
 for(int x = 1; x < 16; x++){
	 renderer.setSeriesShape(x, cross);
 }
 renderer.setSeriesPaint(14, Color.WHITE);
 renderer.setSeriesPaint(15, Color.BLACK);
 //renderer.setSeriesPaint(1, Color.YELLOW);
 plot.setRenderer(renderer);
 plot.setBackgroundPaint(Color.DARK_GRAY);
 
 width = 1280; //Width of the image     
 height = 960; //Height of the image  
 XYChart = new File( "2011IndSeed.jpeg" ); 
       ChartUtilities.saveChartAsJPEG( XYChart, xylineChart, width, height);
 

                    
 //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!NEW RUN OF ALGORITHM!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
   //Evaluator.ceiling = 0.5;
   //Evaluator.publicMutationRate = 0.75;
   pop = new Population(100);  
   Evaluator.startDate = "3-Jan-12";
   Evaluator.endDate = "31-Dec-12";
   pop.initialisation();
   for(int x = 0; x < 15; x++){
	   pop.returnPop().get(x).alleles.set(x, 0.986);
	   for(int y = 0; y < 15; y++){
		   if(y != x){
			   pop.returnPop().get(x).alleles.set(y, 0.001);
		   }
	   }
	   pop.returnPop().get(x).solutionReturn = Evaluator.theReturn(pop.returnPop().get(x));
	   pop.returnPop().get(x).solutionRisk = Evaluator.theVolatility(pop.returnPop().get(x));
	   pop.returnPop().get(x).setSOfitness(Evaluator.evaluateFitness( pop.returnPop().get(x)));
 }
 
   popArray = pop.returnPop();
   pop.nonDominatedSorting(popArray.size());
   pop.crowdingDistance(popArray);
   
   //XYSeries EqualPortfolioIn2012 = new XYSeries("Equally Weighted Portfolio");
  // XYSeries originalParetoIn2012 = new XYSeries("Performance of 2010 Pareto Front Portfolios in 2012");
   
   XYSeries BARC12 = new XYSeries("BARC", false);
   XYSeries RR12 = new XYSeries("RR", false);
   XYSeries CINE12= new XYSeries("CINE", false);
   XYSeries AAL12 = new XYSeries("AAL", false);
   XYSeries BLT12 = new XYSeries("BLT", false);
   XYSeries RDSA12 = new XYSeries("RDSA", false);
   XYSeries RRS12 = new XYSeries("RRS", false);
   XYSeries IGG12 = new XYSeries("IGG", false);
   XYSeries SGC12 = new XYSeries("SGC", false);
   XYSeries TATE12 = new XYSeries("TATE", false);
   XYSeries GRG12 = new XYSeries("GRG", false);
   XYSeries GNK12 = new XYSeries("GNK", false);
   XYSeries BBY12 = new XYSeries("BBY", false);
   XYSeries ADN12 = new XYSeries("ADN", false);
   XYSeries RBS12 = new XYSeries("RBS", false);
   
   System.out.println("!!!!!!!INITIAL POPULATION!!!!!!");
   
   //Collections.sort(popArray, new ReturnComparator());
   //Collections.sort(popArray, new RiskComparator());
   //Collections.sort(popArray, new CrowdingDistanceComparator());
   
   pop.printPopDetails(popArray);
   GenZero = new XYSeries( "Generation Zero" );
   XYSeries RunThree = new XYSeries("2012", false);
   XYSeries ParetoThree = new XYSeries("Front Generated using 2012 Data");
   popArray = pop.returnPop();
	frontArray = pop.getFrontList();
	for(int x = 0; x < frontArray.get(0).size(); x++){
		GenZero.add(frontArray.get(0).get(x).solutionReturn * 100, frontArray.get(0).get(x).solutionRisk);
	}
   
   RunThree.add(pop.getAverageSOfitness(), 0);
   
   
   
   
graphArray = new ArrayList<>();

for(int x = 0; x < 21; x++){
	graphArray.add(new XYSeries("Gen " + (x + 1)));
}

for(int x = 0; x < graphArray.size(); x++){
        pop.evolvePopulation(); 
        popArray = pop.returnPop();
        frontArray = pop.getFrontList();
        for(int y = 0; y < frontArray.get(0).size(); y++){
        	graphArray.get(x).add(frontArray.get(0).get(y).solutionReturn * 100, frontArray.get(0).get(y).solutionRisk);
        }
    RunThree.add(pop.getAverageSOfitness(), x + 1);
}

for(int x = 0; x < frontArray.get(0).size(); x++){
	ParetoThree.add(frontArray.get(0).get(x).solutionReturn * 100, frontArray.get(0).get(x).solutionRisk);
}

for(int x = 0; x < originalParetoReused.size(); x++){
	//originalParetoIn2012.add(Evaluator.theReturn(originalParetoReused.get(x)) * 100, Evaluator.theVolatility(originalParetoReused.get(x)));
}

//EqualPortfolioIn2012.add(Evaluator.theReturn(equalPortfolio) * 100, Evaluator.theVolatility(equalPortfolio));

dataset = new XYSeriesCollection( );
dataset.addSeries(GenZero);
for(int x = 0; x < graphArray.size(); x++){
	if((x + 1) % 3 == 0){
		dataset.addSeries(graphArray.get(x));
	}
}
   
   runLengthDistribution.addSeries(RunThree);
   paretoComparison.addSeries(ParetoThree);
   
   xylineChart = ChartFactory.createXYLineChart(
       "Generations of Algorithm", 
       "Return",
       "Volatility", 
        dataset,
        PlotOrientation.VERTICAL, 
        true, true, false);
   
   plot = xylineChart.getXYPlot();
   renderer = new XYLineAndShapeRenderer();
   plot.setRenderer(renderer);
   plot.setBackgroundPaint(Color.DARK_GRAY);
        
    width = 1280; //Width of the image 
    height = 960; //Height of the image 
    XYChart = new File( "2012.jpeg" ); 
    ChartUtilities.saveChartAsJPEG( XYChart, xylineChart, width, height);
    
    
    BARC12.add(barc.getReturn(Evaluator.startDate, Evaluator.endDate) * 100 , barc.portfolioVolatility(Evaluator.startDate, Evaluator.endDate, 21));
    RR12.add(rr.getReturn(Evaluator.startDate, Evaluator.endDate) * 100 , rr.portfolioVolatility(Evaluator.startDate, Evaluator.endDate, 21));
    CINE12.add(cine.getReturn(Evaluator.startDate, Evaluator.endDate) * 100 , cine.portfolioVolatility(Evaluator.startDate, Evaluator.endDate, 21));
    AAL12.add(aal.getReturn(Evaluator.startDate, Evaluator.endDate) * 100 , aal.portfolioVolatility(Evaluator.startDate, Evaluator.endDate, 21));
    BLT12.add(blt.getReturn(Evaluator.startDate, Evaluator.endDate) * 100 , blt.portfolioVolatility(Evaluator.startDate, Evaluator.endDate, 21));
    RDSA12.add(rdsa.getReturn(Evaluator.startDate, Evaluator.endDate) * 100 , rdsa.portfolioVolatility(Evaluator.startDate, Evaluator.endDate, 21));
    RRS12.add(rrs.getReturn(Evaluator.startDate, Evaluator.endDate) * 100 , rrs.portfolioVolatility(Evaluator.startDate, Evaluator.endDate, 21));
    IGG12.add(igg.getReturn(Evaluator.startDate, Evaluator.endDate) * 100 , igg.portfolioVolatility(Evaluator.startDate, Evaluator.endDate, 21));
    SGC12.add(sgc.getReturn(Evaluator.startDate, Evaluator.endDate) * 100 , sgc.portfolioVolatility(Evaluator.startDate, Evaluator.endDate, 21));
    TATE12.add(tate.getReturn(Evaluator.startDate, Evaluator.endDate) * 100 , tate.portfolioVolatility(Evaluator.startDate, Evaluator.endDate, 21));
    GRG12.add(grg.getReturn(Evaluator.startDate, Evaluator.endDate) * 100 , grg.portfolioVolatility(Evaluator.startDate, Evaluator.endDate, 21));
    GNK12.add(gnk.getReturn(Evaluator.startDate, Evaluator.endDate) * 100 , gnk.portfolioVolatility(Evaluator.startDate, Evaluator.endDate, 21));
    BBY12.add(bby.getReturn(Evaluator.startDate, Evaluator.endDate) * 100 , bby.portfolioVolatility(Evaluator.startDate, Evaluator.endDate, 21));
    ADN12.add(adn.getReturn(Evaluator.startDate, Evaluator.endDate) * 100 , adn.portfolioVolatility(Evaluator.startDate, Evaluator.endDate, 21));
    RBS12.add(rbs.getReturn(Evaluator.startDate, Evaluator.endDate) * 100 , rbs.portfolioVolatility(Evaluator.startDate, Evaluator.endDate, 21));
    
   
    
    
    dataset = new XYSeriesCollection( );
    //dataset.addSeries(originalParetoIn2012);
   // dataset.addSeries(EqualPortfolioIn2012);
    dataset.addSeries(ParetoThree);
    
    dataset.addSeries(BARC12);
    dataset.addSeries(RR12);
    dataset.addSeries(CINE12);
    dataset.addSeries(AAL12);
    dataset.addSeries(BLT12);
    dataset.addSeries(RDSA12);
    dataset.addSeries(RRS12);
    dataset.addSeries(IGG12);
    dataset.addSeries(SGC12);
    dataset.addSeries(TATE12);
    dataset.addSeries(GRG12);
    dataset.addSeries(GNK12);
    dataset.addSeries(BBY12);
    dataset.addSeries(ADN12);
    dataset.addSeries(RBS12);
    
    xylineChart = ChartFactory.createXYLineChart(
    		"Performance of Optimised Portfolios In 2012 and Individual Shares",
            "Return",
            "Volatility", 
            dataset,
            PlotOrientation.VERTICAL, 
            true, true, false);
    
    plot = xylineChart.getXYPlot();
    renderer = new XYLineAndShapeRenderer();
    for(int x = 1; x < 16; x++){
   	 renderer.setSeriesShape(x, cross);
    }
    renderer.setSeriesPaint(14, Color.WHITE);
    renderer.setSeriesPaint(15, Color.BLACK);
    //renderer.setSeriesPaint(1, Color.YELLOW);
    plot.setRenderer(renderer);
    plot.setBackgroundPaint(Color.DARK_GRAY);
    
    width = 1280; //Width of the image     
    height = 960; //Height of the image  
    XYChart = new File( "2012IndSeed.jpeg" ); 
          ChartUtilities.saveChartAsJPEG( XYChart, xylineChart, width, height);

 
 //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!NEW RUN OF ALGORITHM!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 //Evaluator.ceiling = 0.75;
 //Evaluator.publicMutationRate = 0.5;
 pop = new Population(100);  
 Evaluator.startDate = "2-Jan-13";
 Evaluator.endDate = "31-Dec-13";
 pop.initialisation();

 for(int x = 0; x < 15; x++){
	   pop.returnPop().get(x).alleles.set(x, 0.986);
	   for(int y = 0; y < 15; y++){
		   if(y != x){
			   pop.returnPop().get(x).alleles.set(y, 0.001);
		   }
	   }
	   pop.returnPop().get(x).solutionReturn = Evaluator.theReturn(pop.returnPop().get(x));
	   pop.returnPop().get(x).solutionRisk = Evaluator.theVolatility(pop.returnPop().get(x));
	   pop.returnPop().get(x).setSOfitness(Evaluator.evaluateFitness( pop.returnPop().get(x)));
}

 popArray = pop.returnPop();
 pop.nonDominatedSorting(popArray.size());
 pop.crowdingDistance(popArray);

 
// XYSeries EqualPortfolioIn2013 = new XYSeries("Equally Weighted Portfolio");
// XYSeries originalParetoIn2013 = new XYSeries("Performance of 2010 Pareto Front Portfolios in 2013");
 
 XYSeries BARC13 = new XYSeries("BARC", false);
 XYSeries RR13 = new XYSeries("RR", false);
 XYSeries CINE13= new XYSeries("CINE", false);
 XYSeries AAL13 = new XYSeries("AAL", false);
 XYSeries BLT13 = new XYSeries("BLT", false);
 XYSeries RDSA13 = new XYSeries("RDSA", false);
 XYSeries RRS13 = new XYSeries("RRS", false);
 XYSeries IGG13 = new XYSeries("IGG", false);
 XYSeries SGC13 = new XYSeries("SGC", false);
 XYSeries TATE13 = new XYSeries("TATE", false);
 XYSeries GRG13 = new XYSeries("GRG", false);
 XYSeries GNK13 = new XYSeries("GNK", false);
 XYSeries BBY13 = new XYSeries("BBY", false);
 XYSeries ADN13 = new XYSeries("ADN", false);
 XYSeries RBS13 = new XYSeries("RBS", false);
 
 System.out.println("!!!!!!!INITIAL POPULATION!!!!!!");
 
 //Collections.sort(popArray, new ReturnComparator());
 //Collections.sort(popArray, new RiskComparator());
 //Collections.sort(popArray, new CrowdingDistanceComparator());
 
 pop.printPopDetails(popArray);
 GenZero = new XYSeries( "Generation Zero" );
 XYSeries RunFour = new XYSeries("2013", false);
 XYSeries ParetoFour = new XYSeries("Front Generated using 2013 Data");
 popArray = pop.returnPop();
frontArray = pop.getFrontList();
for(int x = 0; x < frontArray.get(0).size(); x++){
	GenZero.add(frontArray.get(0).get(x).solutionReturn * 100, frontArray.get(0).get(x).solutionRisk);
}
 
 RunFour.add(pop.getAverageSOfitness(), 0);
 
 
graphArray = new ArrayList<>();

for(int x = 0; x < 21; x++){
	graphArray.add(new XYSeries("Gen " + (x + 1)));
}

for(int x = 0; x < graphArray.size(); x++){
        pop.evolvePopulation(); 
        popArray = pop.returnPop();
        frontArray = pop.getFrontList();
        for(int y = 0; y < frontArray.get(0).size(); y++){
        	graphArray.get(x).add(frontArray.get(0).get(y).solutionReturn * 100, frontArray.get(0).get(y).solutionRisk);
        }
    RunFour.add(pop.getAverageSOfitness(), x + 1);
}

dataset = new XYSeriesCollection( );
dataset.addSeries(GenZero);
for(int x = 0; x < graphArray.size(); x++){
	if((x + 1) % 3 == 0){
		dataset.addSeries(graphArray.get(x));
	}
}

for(int x = 0; x < frontArray.get(0).size(); x++){
	ParetoFour.add(frontArray.get(0).get(x).solutionReturn * 100, frontArray.get(0).get(x).solutionRisk);
}	

for(int x = 0; x < originalParetoReused.size(); x++){
	//originalParetoIn2013.add(Evaluator.theReturn(originalParetoReused.get(x)) * 100, Evaluator.theVolatility(originalParetoReused.get(x)));
}


//EqualPortfolioIn2013.add(Evaluator.theReturn(equalPortfolio) * 100, Evaluator.theVolatility(equalPortfolio));
 
 
 runLengthDistribution.addSeries(RunFour);
 paretoComparison.addSeries(ParetoFour);
 
 xylineChart = ChartFactory.createXYLineChart(
     "Generations of Algorithm", 
     "Return",
     "Volatility", 
      dataset,
      PlotOrientation.VERTICAL, 
      true, true, false);
 
 plot = xylineChart.getXYPlot();
 renderer = new XYLineAndShapeRenderer();
 plot.setRenderer(renderer);
 plot.setBackgroundPaint(Color.DARK_GRAY);
      
  width = 1280; //Width of the image 
  height = 960; //Height of the image
  XYChart = new File( "2013.jpeg" ); 
  ChartUtilities.saveChartAsJPEG( XYChart, xylineChart, width, height);
  
  BARC13.add(barc.getReturn(Evaluator.startDate, Evaluator.endDate) * 100 , barc.portfolioVolatility(Evaluator.startDate, Evaluator.endDate, 21));
  RR13.add(rr.getReturn(Evaluator.startDate, Evaluator.endDate) * 100 , rr.portfolioVolatility(Evaluator.startDate, Evaluator.endDate, 21));
  CINE13.add(cine.getReturn(Evaluator.startDate, Evaluator.endDate) * 100 , cine.portfolioVolatility(Evaluator.startDate, Evaluator.endDate, 21));
  AAL13.add(aal.getReturn(Evaluator.startDate, Evaluator.endDate) * 100 , aal.portfolioVolatility(Evaluator.startDate, Evaluator.endDate, 21));
  BLT13.add(blt.getReturn(Evaluator.startDate, Evaluator.endDate) * 100 , blt.portfolioVolatility(Evaluator.startDate, Evaluator.endDate, 21));
  RDSA13.add(rdsa.getReturn(Evaluator.startDate, Evaluator.endDate) * 100 , rdsa.portfolioVolatility(Evaluator.startDate, Evaluator.endDate, 21));
  RRS13.add(rrs.getReturn(Evaluator.startDate, Evaluator.endDate) * 100 , rrs.portfolioVolatility(Evaluator.startDate, Evaluator.endDate, 21));
  IGG13.add(igg.getReturn(Evaluator.startDate, Evaluator.endDate) * 100 , igg.portfolioVolatility(Evaluator.startDate, Evaluator.endDate, 21));
  SGC13.add(sgc.getReturn(Evaluator.startDate, Evaluator.endDate) * 100 , sgc.portfolioVolatility(Evaluator.startDate, Evaluator.endDate, 21));
  TATE13.add(tate.getReturn(Evaluator.startDate, Evaluator.endDate) * 100 , tate.portfolioVolatility(Evaluator.startDate, Evaluator.endDate, 21));
  GRG13.add(grg.getReturn(Evaluator.startDate, Evaluator.endDate) * 100 , grg.portfolioVolatility(Evaluator.startDate, Evaluator.endDate, 21));
  GNK13.add(gnk.getReturn(Evaluator.startDate, Evaluator.endDate) * 100 , gnk.portfolioVolatility(Evaluator.startDate, Evaluator.endDate, 21));
  BBY13.add(bby.getReturn(Evaluator.startDate, Evaluator.endDate) * 100 , bby.portfolioVolatility(Evaluator.startDate, Evaluator.endDate, 21));
  ADN13.add(adn.getReturn(Evaluator.startDate, Evaluator.endDate) * 100 , adn.portfolioVolatility(Evaluator.startDate, Evaluator.endDate, 21));
  RBS13.add(rbs.getReturn(Evaluator.startDate, Evaluator.endDate) * 100 , rbs.portfolioVolatility(Evaluator.startDate, Evaluator.endDate, 21));
 
  

  
  dataset = new XYSeriesCollection( );
  //dataset.addSeries(originalParetoIn2013);
  //dataset.addSeries(EqualPortfolioIn2013);
  dataset.addSeries(ParetoFour);
  
  dataset.addSeries(BARC13);
  dataset.addSeries(RR13);
  dataset.addSeries(CINE13);
  dataset.addSeries(AAL13);
  dataset.addSeries(BLT13);
  dataset.addSeries(RDSA13);
  dataset.addSeries(RRS13);
  dataset.addSeries(IGG13);
  dataset.addSeries(SGC13);
  dataset.addSeries(TATE13);
  dataset.addSeries(GRG13);
  dataset.addSeries(GNK13);
  dataset.addSeries(BBY13);
  dataset.addSeries(ADN13);
  dataset.addSeries(RBS13);
  
  
  xylineChart = ChartFactory.createXYLineChart(
          "Performance of Optimised Portfolios In 2013 and Individual Shares",
          "Return",
          "Volatility", 
          dataset,
          PlotOrientation.VERTICAL, 
          true, true, false);
  
  plot = xylineChart.getXYPlot();
  renderer = new XYLineAndShapeRenderer();
  for(int x = 1; x < 16; x++){
		 renderer.setSeriesShape(x, cross);
	 }
  renderer.setSeriesPaint(14, Color.WHITE);
  renderer.setSeriesPaint(15, Color.BLACK);
	 //renderer.setSeriesPaint(1, Color.YELLOW);
  plot.setRenderer(renderer);
  plot.setBackgroundPaint(Color.DARK_GRAY);
  
  width = 1280; //Width of the image     
  height = 960; //Height of the image  
  XYChart = new File( "2013IndSeed.jpeg" ); 
        ChartUtilities.saveChartAsJPEG( XYChart, xylineChart, width, height);

//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!NEW RUN OF ALGORITHM!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//Evaluator.ceiling = 1.0;
//Evaluator.publicMutationRate = 0.0;
pop = new Population(100);  
Evaluator.startDate = "2-Jan-14";
Evaluator.endDate = "31-Dec-14";
pop.initialisation();

for(int x = 0; x < 15; x++){
	   pop.returnPop().get(x).alleles.set(x, 0.986);
	   for(int y = 0; y < 15; y++){
		   if(y != x){
			   pop.returnPop().get(x).alleles.set(y, 0.001);
		   }
	   }
	   pop.returnPop().get(x).solutionReturn = Evaluator.theReturn(pop.returnPop().get(x));
	   pop.returnPop().get(x).solutionRisk = Evaluator.theVolatility(pop.returnPop().get(x));
	   pop.returnPop().get(x).setSOfitness(Evaluator.evaluateFitness( pop.returnPop().get(x)));
}
popArray = pop.returnPop();
pop.nonDominatedSorting(popArray.size());
pop.crowdingDistance(popArray);

//XYSeries EqualPortfolioIn2014 = new XYSeries("Equally Weighted Portfolio");
//XYSeries originalParetoIn2014 = new XYSeries("Performance of 2010 Pareto Front Portfolios in 2014");

XYSeries BARC14 = new XYSeries("BARC", false);
XYSeries RR14 = new XYSeries("RR", false);
XYSeries CINE14 = new XYSeries("CINE", false);
XYSeries AAL14 = new XYSeries("AAL", false);
XYSeries BLT14 = new XYSeries("BLT", false);
XYSeries RDSA14 = new XYSeries("RDSA", false);
XYSeries RRS14 = new XYSeries("RRS", false);
XYSeries IGG14 = new XYSeries("IGG", false);
XYSeries SGC14 = new XYSeries("SGC", false);
XYSeries TATE14 = new XYSeries("TATE", false);
XYSeries GRG14 = new XYSeries("GRG", false);
XYSeries GNK14 = new XYSeries("GNK", false);
XYSeries BBY14 = new XYSeries("BBY", false);
XYSeries ADN14 = new XYSeries("ADN", false);
XYSeries RBS14 = new XYSeries("RBS", false);

System.out.println("!!!!!!!INITIAL POPULATION!!!!!!");

//Collections.sort(popArray, new ReturnComparator());
//Collections.sort(popArray, new RiskComparator());
//Collections.sort(popArray, new CrowdingDistanceComparator());

pop.printPopDetails(popArray);
GenZero = new XYSeries( "Gen Zero" );
XYSeries RunFive = new XYSeries("2014", false);
XYSeries ParetoFive = new XYSeries("Front Generated using 2014 Data");
popArray = pop.returnPop();
frontArray = pop.getFrontList();
for(int x = 0; x < frontArray.get(0).size(); x++){
	GenZero.add(frontArray.get(0).get(x).solutionReturn * 100, frontArray.get(0).get(x).solutionRisk);
}

RunFive.add(pop.getAverageSOfitness(), 0);

graphArray = new ArrayList<>();

for(int x = 0; x < 21; x++){
		graphArray.add(new XYSeries("Gen " + (x + 1)));
}

for(int x = 0; x < graphArray.size(); x++){
        pop.evolvePopulation(); 
        popArray = pop.returnPop();
        frontArray = pop.getFrontList();
	        for(int y = 0; y < frontArray.get(0).size(); y++){
	        	graphArray.get(x).add(frontArray.get(0).get(y).solutionReturn * 100, frontArray.get(0).get(y).solutionRisk);
        }
    RunFive.add(pop.getAverageSOfitness(), x + 1);
}

for(int x = 0; x < frontArray.get(0).size(); x++){
	ParetoFive.add(frontArray.get(0).get(x).solutionReturn * 100, frontArray.get(0).get(x).solutionRisk);
}

for(int x = 0; x < originalParetoReused.size(); x++){
	//originalParetoIn2014.add(Evaluator.theReturn(originalParetoReused.get(x)) * 100, Evaluator.theVolatility(originalParetoReused.get(x)));
}


//EqualPortfolioIn2014.add(Evaluator.theReturn(equalPortfolio) * 100, Evaluator.theVolatility(equalPortfolio));

dataset = new XYSeriesCollection( );
dataset.addSeries(GenZero);
for(int x = 0; x < graphArray.size(); x++){
	if((x + 1) % 3 == 0){
		dataset.addSeries(graphArray.get(x));
	}
}


runLengthDistribution.addSeries(RunFive);
paretoComparison.addSeries(ParetoFive);

xylineChart = ChartFactory.createXYLineChart(
    "Generations of Algorithm", 
    "Return",
    "Volatility", 
     dataset,
     PlotOrientation.VERTICAL, 
     true, true, false);

plot = xylineChart.getXYPlot();
renderer = new XYLineAndShapeRenderer();
plot.setRenderer(renderer);
plot.setBackgroundPaint(Color.DARK_GRAY);
     
 width = 1280;
 height = 960; 
 XYChart = new File( "2014.jpeg" ); 
 ChartUtilities.saveChartAsJPEG( XYChart, xylineChart, width, height);

xylineChart = ChartFactory.createXYLineChart(
         "Run Length Distribution",  
         "Avg. Fitness",
         "Generation",
         runLengthDistribution,
         PlotOrientation.HORIZONTAL, 
         true, true, false);
     
plot = xylineChart.getXYPlot();
renderer = new XYLineAndShapeRenderer();
plot.setRenderer(renderer);
plot.setBackgroundPaint(Color.DARK_GRAY);
  
width = 1280;
height = 960;
XYChart = new File( "RunLengthDistribution.jpeg" ); 
ChartUtilities.saveChartAsJPEG( XYChart, xylineChart, width, height); 

xylineChart = ChartFactory.createXYLineChart(
        "Pareto Fronts of all Runs", 
        "Return",
        "Volatility", 
        paretoComparison,
        PlotOrientation.VERTICAL, 
        true, true, false);
    
plot = xylineChart.getXYPlot();
renderer = new XYLineAndShapeRenderer();
plot.setRenderer(renderer);
plot.setBackgroundPaint(Color.DARK_GRAY);
 
width = 1280;
height = 960;
XYChart = new File( "ParetoFronts.jpeg" ); 
ChartUtilities.saveChartAsJPEG( XYChart, xylineChart, width, height); 

BARC14.add(barc.getReturn(Evaluator.startDate, Evaluator.endDate) * 100 , barc.portfolioVolatility(Evaluator.startDate, Evaluator.endDate, 21));
RR14.add(rr.getReturn(Evaluator.startDate, Evaluator.endDate) * 100 , rr.portfolioVolatility(Evaluator.startDate, Evaluator.endDate, 21));
CINE14.add(cine.getReturn(Evaluator.startDate, Evaluator.endDate) * 100 , cine.portfolioVolatility(Evaluator.startDate, Evaluator.endDate, 21));
AAL14.add(aal.getReturn(Evaluator.startDate, Evaluator.endDate) * 100 , aal.portfolioVolatility(Evaluator.startDate, Evaluator.endDate, 21));
BLT14.add(blt.getReturn(Evaluator.startDate, Evaluator.endDate) * 100 , blt.portfolioVolatility(Evaluator.startDate, Evaluator.endDate, 21));
RDSA14.add(rdsa.getReturn(Evaluator.startDate, Evaluator.endDate) * 100 , rdsa.portfolioVolatility(Evaluator.startDate, Evaluator.endDate, 21));
RRS14.add(rrs.getReturn(Evaluator.startDate, Evaluator.endDate) * 100 , rrs.portfolioVolatility(Evaluator.startDate, Evaluator.endDate, 21));
IGG14.add(igg.getReturn(Evaluator.startDate, Evaluator.endDate) * 100 , igg.portfolioVolatility(Evaluator.startDate, Evaluator.endDate, 21));
SGC14.add(sgc.getReturn(Evaluator.startDate, Evaluator.endDate) * 100 , sgc.portfolioVolatility(Evaluator.startDate, Evaluator.endDate, 21));
TATE14.add(tate.getReturn(Evaluator.startDate, Evaluator.endDate) * 100 , tate.portfolioVolatility(Evaluator.startDate, Evaluator.endDate, 21));
GRG14.add(grg.getReturn(Evaluator.startDate, Evaluator.endDate) * 100 , grg.portfolioVolatility(Evaluator.startDate, Evaluator.endDate, 21));
GNK14.add(gnk.getReturn(Evaluator.startDate, Evaluator.endDate) * 100 , gnk.portfolioVolatility(Evaluator.startDate, Evaluator.endDate, 21));
BBY14.add(bby.getReturn(Evaluator.startDate, Evaluator.endDate) * 100 , bby.portfolioVolatility(Evaluator.startDate, Evaluator.endDate, 21));
ADN14.add(adn.getReturn(Evaluator.startDate, Evaluator.endDate) * 100 , adn.portfolioVolatility(Evaluator.startDate, Evaluator.endDate, 21));
RBS14.add(rbs.getReturn(Evaluator.startDate, Evaluator.endDate) * 100 , rbs.portfolioVolatility(Evaluator.startDate, Evaluator.endDate, 21));




dataset = new XYSeriesCollection( );
//dataset.addSeries(originalParetoIn2014);
//dataset.addSeries(EqualPortfolioIn2014);
dataset.addSeries(ParetoFive);

dataset.addSeries(BARC14);
dataset.addSeries(RR14);
dataset.addSeries(CINE14);
dataset.addSeries(AAL14);
dataset.addSeries(BLT14);
dataset.addSeries(RDSA14);
dataset.addSeries(RRS14);
dataset.addSeries(IGG14);
dataset.addSeries(SGC14);
dataset.addSeries(TATE14);
dataset.addSeries(GRG14);
dataset.addSeries(GNK14);
dataset.addSeries(BBY14);
dataset.addSeries(ADN14);
dataset.addSeries(RBS14);

xylineChart = ChartFactory.createXYLineChart(
        "Performance of Optimised Portfolios In 2014 and Individual Shares", 
        "Return",
        "Volatility", 
        dataset,
        PlotOrientation.VERTICAL, 
        true, true, false);

plot = xylineChart.getXYPlot();
renderer = new XYLineAndShapeRenderer();
for(int x = 1; x < 16; x++){
	 renderer.setSeriesShape(x, cross);
}
renderer.setSeriesPaint(14, Color.WHITE);
renderer.setSeriesPaint(15, Color.BLACK);
//renderer.setSeriesPaint(1, Color.YELLOW);
plot.setRenderer(renderer);
plot.setBackgroundPaint(Color.DARK_GRAY);

width = 1280; //Width of the image     
height = 960; //Height of the image  
XYChart = new File( "IndSeed2014.jpeg" ); 
      ChartUtilities.saveChartAsJPEG( XYChart, xylineChart, width, height);

        /**
        String dominations = "";
        //Holds the list of what solutions a specific solution dominates
        ArrayList<StockWeightingSolution> dominatedList = new ArrayList<>();
        for(int x = 0; x < popArray.size(); x++){
            dominations = "Dominates Portfolios: ";
            dominatedList.clear();
            Collections.sort(popArray, new FitnessComparator());
            System.out.println("ID: " + popArray.get(x).id);
            popArray.get(x).printDetails();
            System.out.println("Return: " + Evaluator.theReturn(popArray.get(x)) * 100);
            System.out.println("Risk: " + Evaluator.theVolatility(popArray.get(x)));
            System.out.println("True Domination Count : " + popArray.get(x).getTrueDomCount());
            dominatedList.addAll(popArray.get(x).returnDominates());
            for(int y = 0; y < popArray.get(x).returnDominates().size(); y++){
                dominations += dominatedList.get(y).id + " ";
            }
            System.out.println(dominations);
            System.out.println("Fitness: " + popArray.get(x).fitness);
            System.out.println("\n");
            
        }
        **/
        
        /**
        System.out.println("Front List");
        ArrayList<ArrayList<StockWeightingSolution>> frontList = pop.getFrontList();
        for(int x = 0; x < frontList.size(); x++){
            for(int y = 0; y < frontList.get(x).size(); y++){
                frontList.get(x).get(y).printDetails();
                System.out.println("\n");
            }
                
        }


        **/
       
         
        
        
        
            
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        /**      
  //Make a new population
    Evaluator.publicMutationRate = 0.9;
    Evaluator.floor = 0.00;
    Evaluator.ceiling = 1.0;
    Evaluator.startDate = "2-Jan-98";
    Evaluator.endDate = "31-Dec-98";
    ArrayList<StockWeightingSolution> oldPareto = new ArrayList<>();
    Population pop = new Population(100);  
    pop.initialisation();
    ArrayList<StockWeightingSolution> popArray = pop.returnPop();
    pop.nonDominatedSorting(popArray.size());
    pop.crowdingDistance(popArray);

    System.out.println("!!!!!!!INITIAL POPULATION!!!!!!");

    //Collections.sort(popArray, new ReturnComparator());
    //Collections.sort(popArray, new RiskComparator());
    //Collections.sort(popArray, new CrowdingDistanceComparator());

    pop.printPopDetails(popArray);
    XYSeries GenZero = new XYSeries( "Gen Zero" );
    XYSeries RunOne = new XYSeries("1998", false);
    XYSeries ParetoOne = new XYSeries("Actual 1998 Front");
    popArray = pop.returnPop();
    ArrayList<ArrayList<StockWeightingSolution>> frontArray = pop.getFrontList();
    for(int x = 0; x < frontArray.get(0).size(); x++){
    	GenZero.add(frontArray.get(0).get(x).solutionReturn * 100, frontArray.get(0).get(x).solutionRisk);
    }
  
    for(int y = 0; y < popArray.size(); y++){
    	GenZero.add(popArray.get(y).solutionReturn * 100, popArray.get(y).solutionRisk);
    }
   

    RunOne.add(pop.getAverageSOfitness(), 0);

    ArrayList<XYSeries> graphArray = new ArrayList<>();
    for(int x = 0; x < 21; x++){
    		graphArray.add(new XYSeries("Gen " + (x + 1)));
    }

    for(int x = 0; x < graphArray.size(); x++){
            pop.evolvePopulation(); 
            popArray = pop.returnPop();
            frontArray = pop.getFrontList();
    	        for(int y = 0; y < frontArray.get(0).size(); y++){
    	        	graphArray.get(x).add(frontArray.get(0).get(y).solutionReturn * 100, frontArray.get(0).get(y).solutionRisk);
            }
        RunOne.add(pop.getAverageSOfitness(), x + 1);
    }

    for(int x = 0; x < frontArray.get(0).size(); x++){
    	oldPareto.add(new StockWeightingSolution(true));
    }
    for(int x = 0; x < frontArray.get(0).size(); x++){
    	ParetoOne.add(frontArray.get(0).get(x).solutionReturn * 100, frontArray.get(0).get(x).solutionRisk);
    	for(int y = 0; y < frontArray.get(0).get(x).alleles.size(); y++){
    		oldPareto.get(x).alleles.set(y, frontArray.get(0).get(x).alleles.get(y));
    	}
    }

    XYSeriesCollection dataset = new XYSeriesCollection( );
    dataset.addSeries(GenZero);
    for(int x = 0; x < graphArray.size(); x++){
    	if((x + 1) % 3 == 0){
    		dataset.addSeries(graphArray.get(x));
    	}
    }

    XYSeriesCollection runLengthDistribution = new XYSeriesCollection( );
    runLengthDistribution.addSeries(RunOne);

    XYSeriesCollection paretoComparison = new XYSeriesCollection( );
    paretoComparison.addSeries(ParetoOne);

    JFreeChart xylineChart = ChartFactory.createXYLineChart(
            "Generations of Algorithm", 
            "Return",
            "Volatility", 
            dataset,
            PlotOrientation.VERTICAL, 
            true, true, false);

    XYPlot plot = xylineChart.getXYPlot();
    XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
    plot.setRenderer(renderer);
    plot.setBackgroundPaint(Color.DARK_GRAY);
         
         int width = 1280;
         int height = 960;
         File XYChart = new File( "1998.jpeg" ); 
         ChartUtilities.saveChartAsJPEG( XYChart, xylineChart, width, height);

    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!NEW RUN OF ALGORITHM!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
     pop = new Population(100);  
     //Evaluator.ceiling = 0.25;
     //Evaluator.publicMutationRate = 0.9;
     Evaluator.startDate = "4-Jan-99";
     Evaluator.endDate = "31-Dec-99";
     pop.initialisation();
     popArray = pop.returnPop();
     pop.nonDominatedSorting(popArray.size());
     pop.crowdingDistance(popArray);
     ArrayList<StockWeightingSolution> oldPareto2 = new ArrayList<>();
     XYSeries ParetoTwo = new XYSeries("Actual 99 Front");
     
     System.out.println("!!!!!!!INITIAL POPULATION!!!!!!");
     
     //Collections.sort(popArray, new ReturnComparator());
     //Collections.sort(popArray, new RiskComparator());
     //Collections.sort(popArray, new CrowdingDistanceComparator());
     
     pop.printPopDetails(popArray);
     XYSeries RunTwo = new XYSeries("1999", false);
     XYSeries InitialPareto11_1 = new XYSeries("1998 Portfolios Reused");
     GenZero = new XYSeries( "Generation Zero" );
     popArray = pop.returnPop();
    frontArray = pop.getFrontList();
    for(int x = 0; x < frontArray.get(0).size(); x++){
    	GenZero.add(frontArray.get(0).get(x).solutionReturn * 100, frontArray.get(0).get(x).solutionRisk);
    }
     
     RunTwo.add(pop.getAverageSOfitness(), 0);
     
     
    graphArray = new ArrayList<>();

    for(int x = 0; x < 21; x++){
    	graphArray.add(new XYSeries("Gen " + (x + 1)));
    }

    for(int x = 0; x < graphArray.size(); x++){
            pop.evolvePopulation(); 
            popArray = pop.returnPop();
            frontArray = pop.getFrontList();
            for(int y = 0; y < frontArray.get(0).size(); y++){
            	graphArray.get(x).add(frontArray.get(0).get(y).solutionReturn * 100, frontArray.get(0).get(y).solutionRisk);
            }
        RunTwo.add(pop.getAverageSOfitness(), x + 1);
    }

    for(int x = 0; x < frontArray.get(0).size(); x++){
    	oldPareto2.add(new StockWeightingSolution(true));
    }
    
    Evaluator.startDate = "4-Jan-99";
    Evaluator.endDate = "31-Dec-99";
    
    for(int x = 0; x < frontArray.get(0).size(); x++){
      	ParetoTwo.add(frontArray.get(0).get(x).solutionReturn * 100, frontArray.get(0).get(x).solutionRisk);
      }
    
    for(int x = 0; x < frontArray.get(0).size(); x++){
    	for(int y = 0; y < frontArray.get(0).get(x).alleles.size(); y++){
    		oldPareto2.get(x).alleles.set(y, frontArray.get(0).get(x).alleles.get(y));
    	}
    }
    
    for(int x = 0; x < oldPareto.size(); x++){
    	InitialPareto11_1.add(Evaluator.theReturn(oldPareto.get(x)) * 100, Evaluator.theVolatility(oldPareto.get(x)));
    }
    
    XYSeriesCollection paretoDrift = new XYSeriesCollection( );
    //paretoDrift.addSeries(ParetoOne);
    paretoDrift.addSeries(InitialPareto11_1);
    paretoComparison.addSeries(ParetoTwo);
    paretoDrift.addSeries(ParetoTwo);

    dataset = new XYSeriesCollection( );
    dataset.addSeries(GenZero);
    for(int x = 0; x < graphArray.size(); x++){
    	if((x + 1) % 3 == 0){
    		dataset.addSeries(graphArray.get(x));
    	}
    }
     
     runLengthDistribution.addSeries(RunTwo);
     
     xylineChart = ChartFactory.createXYLineChart(
             "Generations of Algorithm", 
             "Return",
             "Volatility", 
             dataset,
             PlotOrientation.VERTICAL, 
             true, true, false);
     
     plot = xylineChart.getXYPlot();
     renderer = new XYLineAndShapeRenderer();
     plot.setRenderer(renderer);
     plot.setBackgroundPaint(Color.DARK_GRAY);
          
    width = 1280; //Width of the image     
    height = 960; //Height of the image  
    XYChart = new File( "1999.jpeg" ); 
          ChartUtilities.saveChartAsJPEG( XYChart, xylineChart, width, height);
          
          

          



          
          
                   
     //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!NEW RUN OF ALGORITHM!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
       //Evaluator.ceiling = 0.5;
       //Evaluator.publicMutationRate = 0.75;
          ArrayList<StockWeightingSolution> oldPareto3 = new ArrayList<>();
       pop = new Population(100);  
       XYSeries InitialPareto11_2 = new XYSeries("1998 Portfolios Reused");
       Evaluator.startDate = "4-Jan-99";
       Evaluator.endDate = "29-Dec-00";
       pop.initialisation();
       popArray = pop.returnPop();
       pop.nonDominatedSorting(popArray.size());
       pop.crowdingDistance(popArray);
       
       XYSeries ParetoThree= new XYSeries("Actual 99 - 00 Front");
       
       System.out.println("!!!!!!!INITIAL POPULATION!!!!!!");
       
       //Collections.sort(popArray, new ReturnComparator());
       //Collections.sort(popArray, new RiskComparator());
       //Collections.sort(popArray, new CrowdingDistanceComparator());
       
       pop.printPopDetails(popArray);
       GenZero = new XYSeries( "Generation Zero" );
       XYSeries RunThree = new XYSeries("2000", false);
       XYSeries InitialPareto12 = new XYSeries("1999 Portfolios Reused");
       popArray = pop.returnPop();
    	frontArray = pop.getFrontList();
    	for(int x = 0; x < frontArray.get(0).size(); x++){
    		GenZero.add(frontArray.get(0).get(x).solutionReturn * 100, frontArray.get(0).get(x).solutionRisk);
    	}
       
       RunThree.add(pop.getAverageSOfitness(), 0);
       
       
    graphArray = new ArrayList<>();

    for(int x = 0; x < 21; x++){
    	graphArray.add(new XYSeries("Gen " + (x + 1)));
    }

    for(int x = 0; x < graphArray.size(); x++){
            pop.evolvePopulation(); 
            popArray = pop.returnPop();
            frontArray = pop.getFrontList();
            for(int y = 0; y < frontArray.get(0).size(); y++){
            	graphArray.get(x).add(frontArray.get(0).get(y).solutionReturn * 100, frontArray.get(0).get(y).solutionRisk);
            }
        RunThree.add(pop.getAverageSOfitness(), x + 1);
    }
    
    for(int x = 0; x < frontArray.get(0).size(); x++){
    	ParetoThree.add(frontArray.get(0).get(x).solutionReturn * 100, frontArray.get(0).get(x).solutionRisk);
    }
    
    
    Evaluator.startDate = "4-Jan-99";
    Evaluator.endDate = "29-Dec-00";
    
    for(int x = 0; x < frontArray.get(0).size(); x++){
    	oldPareto3.add(new StockWeightingSolution(true));
    }
    for(int x = 0; x < frontArray.get(0).size(); x++){
    	for(int y = 0; y < frontArray.get(0).get(x).alleles.size(); y++){
    		oldPareto3.get(x).alleles.set(y, frontArray.get(0).get(x).alleles.get(y));
    	}
    }
    
    Evaluator.startDate = "3-Jan-00";
    Evaluator.endDate = "29-Dec-00";
    
    for(int x = 0; x < oldPareto2.size(); x++){
    	InitialPareto12.add(Evaluator.theReturn(oldPareto2.get(x)) * 100, Evaluator.theVolatility(oldPareto2.get(x)));
    }
    
    Evaluator.startDate = "4-Jan-99";
    Evaluator.endDate = "29-Dec-00";
    
    for(int x = 0; x < oldPareto.size(); x++){
    	InitialPareto11_2.add(Evaluator.theReturn(oldPareto.get(x)) * 100, Evaluator.theVolatility(oldPareto.get(x)));
    }
    
    XYSeriesCollection paretoDrift2 = new XYSeriesCollection( );
    //paretoDrift2.addSeries(ParetoTwo);
    paretoDrift2.addSeries(InitialPareto11_2);
    //paretoDrift2.addSeries(InitialPareto12);
    paretoComparison.addSeries(ParetoThree);
    paretoDrift2.addSeries(ParetoThree);

    dataset = new XYSeriesCollection( );
    dataset.addSeries(GenZero);
    for(int x = 0; x < graphArray.size(); x++){
    	if((x + 1) % 3 == 0){
    		dataset.addSeries(graphArray.get(x));
    	}
    }
       
       runLengthDistribution.addSeries(RunThree);
       
       xylineChart = ChartFactory.createXYLineChart(
           "Generations of Algorithm", 
           "Return",
           "Volatility", 
            dataset,
            PlotOrientation.VERTICAL, 
            true, true, false);
       
       plot = xylineChart.getXYPlot();
       renderer = new XYLineAndShapeRenderer();
       plot.setRenderer(renderer);
       plot.setBackgroundPaint(Color.DARK_GRAY);
            
        width = 1280; //Width of the image 
        height = 960; //Height of the image 
        XYChart = new File( "2000.jpeg" ); 
        ChartUtilities.saveChartAsJPEG( XYChart, xylineChart, width, height);


        
     
        
   
        
        
        
        
        
        
        
        pop = new Population(100);  
        //Evaluator.ceiling = 0.25;
        //Evaluator.publicMutationRate = 0.9;
        Evaluator.startDate = "3-Jan-00";
        Evaluator.endDate = "29-Dec-00";
        pop.initialisation();
        popArray = pop.returnPop();
        pop.nonDominatedSorting(popArray.size());
        pop.crowdingDistance(popArray);
        
        XYSeries ParetoThree00 = new XYSeries("Actual 00 Front");
        
        graphArray = new ArrayList<>();

        for(int x = 0; x < 21; x++){
        	graphArray.add(new XYSeries("Gen " + (x + 1)));
        }

        for(int x = 0; x < graphArray.size(); x++){
                pop.evolvePopulation(); 
                popArray = pop.returnPop();
                frontArray = pop.getFrontList();
                for(int y = 0; y < frontArray.get(0).size(); y++){
                	graphArray.get(x).add(frontArray.get(0).get(y).solutionReturn * 100, frontArray.get(0).get(y).solutionRisk);
                }
        }
        
        for(int x = 0; x < frontArray.get(0).size(); x++){
        	ParetoThree00.add(frontArray.get(0).get(x).solutionReturn * 100, frontArray.get(0).get(x).solutionRisk);
        }
       
        XYSeriesCollection paretoDrift299 = new XYSeriesCollection( );
        paretoDrift299.addSeries(ParetoThree00);
        
        
        
     
     //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!NEW RUN OF ALGORITHM!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
     //Evaluator.ceiling = 0.75;
     //Evaluator.publicMutationRate = 0.5;
     pop = new Population(100);  
     Evaluator.startDate = "4-Jan-99";
     Evaluator.endDate = "31-Dec-01";
     pop.initialisation();
     popArray = pop.returnPop();
     pop.nonDominatedSorting(popArray.size());
     pop.crowdingDistance(popArray);
     XYSeries InitialPareto11_3 = new XYSeries("1998 Portfolios Reused");
     ArrayList<StockWeightingSolution> oldPareto4 = new ArrayList<>();
     
     System.out.println("!!!!!!!INITIAL POPULATION!!!!!!");
     
     //Collections.sort(popArray, new ReturnComparator());
     //Collections.sort(popArray, new RiskComparator());
     //Collections.sort(popArray, new CrowdingDistanceComparator());
     
     pop.printPopDetails(popArray);
     GenZero = new XYSeries( "Generation Zero" );
     XYSeries RunFour = new XYSeries("2001", false);
     XYSeries ParetoFour = new XYSeries("Actual 99 - 01 Front");
     
     XYSeries InitialPareto13 = new XYSeries("1999 Portfolios Reused");
     popArray = pop.returnPop();
    frontArray = pop.getFrontList();
    for(int x = 0; x < frontArray.get(0).size(); x++){
    	GenZero.add(frontArray.get(0).get(x).solutionReturn * 100, frontArray.get(0).get(x).solutionRisk);
    }
     
     RunFour.add(pop.getAverageSOfitness(), 0);
     
     
    graphArray = new ArrayList<>();

    for(int x = 0; x < 21; x++){
    	graphArray.add(new XYSeries("Gen " + (x + 1)));
    }

    for(int x = 0; x < graphArray.size(); x++){
            pop.evolvePopulation(); 
            popArray = pop.returnPop();
            frontArray = pop.getFrontList();
            for(int y = 0; y < frontArray.get(0).size(); y++){
            	graphArray.get(x).add(frontArray.get(0).get(y).solutionReturn * 100, frontArray.get(0).get(y).solutionRisk);
            }
        RunFour.add(pop.getAverageSOfitness(), x + 1);
    }

    dataset = new XYSeriesCollection( );
    dataset.addSeries(GenZero);
    for(int x = 0; x < graphArray.size(); x++){
    	if((x + 1) % 3 == 0){
    		dataset.addSeries(graphArray.get(x));
    	}
    }

    Evaluator.startDate = "4-Jan-99";
    Evaluator.endDate = "31-Dec-01";
    
    for(int x = 0; x < frontArray.get(0).size(); x++){
    	oldPareto4.add(new StockWeightingSolution(true));
    }
    for(int x = 0; x < frontArray.get(0).size(); x++){
    	ParetoFour.add(frontArray.get(0).get(x).solutionReturn * 100, frontArray.get(0).get(x).solutionRisk);
    	for(int y = 0; y < frontArray.get(0).get(x).alleles.size(); y++){
    		oldPareto4.get(x).alleles.set(y, frontArray.get(0).get(x).alleles.get(y));
    	}
    }
    
    Evaluator.startDate = "3-Jan-00";
    Evaluator.endDate = "31-Dec-01";
    
    for(int x = 0; x < oldPareto2.size(); x++){
    	InitialPareto13.add(Evaluator.theReturn(oldPareto2.get(x)) * 100, Evaluator.theVolatility(oldPareto2.get(x)));
    }
    
    Evaluator.startDate = "4-Jan-99";
    Evaluator.endDate = "31-Dec-01";
    
    for(int x = 0; x < oldPareto.size(); x++){
    	InitialPareto11_3.add(Evaluator.theReturn(oldPareto.get(x)) * 100, Evaluator.theVolatility(oldPareto.get(x)));
    }
    
    XYSeriesCollection paretoDrift3 = new XYSeriesCollection( );
    //paretoDrift3.addSeries(ParetoThree);
    paretoDrift3.addSeries(InitialPareto11_3);
    paretoDrift3.addSeries(ParetoFour);
    //paretoDrift3.addSeries(InitialPareto13);
    
     
     runLengthDistribution.addSeries(RunFour);
     paretoComparison.addSeries(ParetoFour);
     
     xylineChart = ChartFactory.createXYLineChart(
         "Generations of Algorithm", 
         "Return",
         "Volatility", 
          dataset,
          PlotOrientation.VERTICAL, 
          true, true, false);
     
     plot = xylineChart.getXYPlot();
     renderer = new XYLineAndShapeRenderer();
     plot.setRenderer(renderer);
     plot.setBackgroundPaint(Color.DARK_GRAY);
          
      width = 1280; //Width of the image 
      height = 960; //Height of the image
      XYChart = new File( "2001.jpeg" ); 
      ChartUtilities.saveChartAsJPEG( XYChart, xylineChart, width, height);
      
      
      
      
      pop = new Population(100);  
      //Evaluator.ceiling = 0.25;
      //Evaluator.publicMutationRate = 0.9;
      Evaluator.startDate = "3-Jan-00";
      Evaluator.endDate = "31-Dec-01";
      pop.initialisation();
      popArray = pop.returnPop();
      pop.nonDominatedSorting(popArray.size());
      pop.crowdingDistance(popArray);
      
      XYSeries ParetoFour00 = new XYSeries("Actual 00 - 01 Front");
      
      graphArray = new ArrayList<>();

      for(int x = 0; x < 21; x++){
      	graphArray.add(new XYSeries("Gen " + (x + 1)));
      }

      for(int x = 0; x < graphArray.size(); x++){
              pop.evolvePopulation(); 
              popArray = pop.returnPop();
              frontArray = pop.getFrontList();
              for(int y = 0; y < frontArray.get(0).size(); y++){
              	graphArray.get(x).add(frontArray.get(0).get(y).solutionReturn * 100, frontArray.get(0).get(y).solutionRisk);
              }
      }
      
      for(int x = 0; x < frontArray.get(0).size(); x++){
      	ParetoFour00.add(frontArray.get(0).get(x).solutionReturn * 100, frontArray.get(0).get(x).solutionRisk);
      }
     
      XYSeriesCollection paretoDrift399 = new XYSeriesCollection( );
      paretoDrift399.addSeries(ParetoFour00);

    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!NEW RUN OF ALGORITHM!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //Evaluator.ceiling = 1.0;
    //Evaluator.publicMutationRate = 0.0;
    pop = new Population(100);  
    Evaluator.startDate = "4-Jan-99";
    Evaluator.endDate = "31-Dec-02";
    XYSeries InitialPareto11_4 = new XYSeries("1998 Portfolios Reused");
    pop.initialisation();
    popArray = pop.returnPop();
    pop.nonDominatedSorting(popArray.size());
    pop.crowdingDistance(popArray);
    
    System.out.println("!!!!!!!INITIAL POPULATION!!!!!!");

    //Collections.sort(popArray, new ReturnComparator());
    //Collections.sort(popArray, new RiskComparator());
    //Collections.sort(popArray, new CrowdingDistanceComparator());

    ArrayList<StockWeightingSolution> oldPareto5 = new ArrayList<>();
    pop.printPopDetails(popArray);
    GenZero = new XYSeries( "Gen Zero" );
    XYSeries RunFive = new XYSeries("2002", false);
    XYSeries ParetoFive = new XYSeries("Actual 99 - 02 Front");
    XYSeries InitialPareto14 = new XYSeries("1999 Portfolios Reused");
    popArray = pop.returnPop();
    frontArray = pop.getFrontList();
    for(int x = 0; x < frontArray.get(0).size(); x++){
    	GenZero.add(frontArray.get(0).get(x).solutionReturn * 100, frontArray.get(0).get(x).solutionRisk);
    }

    RunFive.add(pop.getAverageSOfitness(), 0);

    graphArray = new ArrayList<>();

    for(int x = 0; x < 21; x++){
    		graphArray.add(new XYSeries("Gen " + (x + 1)));
    }

    for(int x = 0; x < graphArray.size(); x++){
            pop.evolvePopulation(); 
            popArray = pop.returnPop();
            frontArray = pop.getFrontList();
    	        for(int y = 0; y < frontArray.get(0).size(); y++){
    	        	graphArray.get(x).add(frontArray.get(0).get(y).solutionReturn * 100, frontArray.get(0).get(y).solutionRisk);
            }
        RunFive.add(pop.getAverageSOfitness(), x + 1);
    }
    
    Evaluator.startDate = "4-Jan-99";
    Evaluator.endDate = "31-Dec-02";

    for(int x = 0; x < frontArray.get(0).size(); x++){
    	oldPareto5.add(new StockWeightingSolution(true));
    }
    for(int x = 0; x < frontArray.get(0).size(); x++){
    	ParetoFive.add(frontArray.get(0).get(x).solutionReturn * 100, frontArray.get(0).get(x).solutionRisk);
    	for(int y = 0; y < frontArray.get(0).get(x).alleles.size(); y++){
    		oldPareto5.get(x).alleles.set(y, frontArray.get(0).get(x).alleles.get(y));
    	}
    }
    
    Evaluator.startDate = "3-Jan-00";
    Evaluator.endDate = "31-Dec-02";

    dataset = new XYSeriesCollection( );
    dataset.addSeries(GenZero);
    for(int x = 0; x < graphArray.size(); x++){
    	if((x + 1) % 3 == 0){
    		dataset.addSeries(graphArray.get(x));
    	}
    }
    
    for(int x = 0; x < oldPareto2.size(); x++){
    	InitialPareto14.add(Evaluator.theReturn(oldPareto2.get(x)) * 100, Evaluator.theVolatility(oldPareto2.get(x)));
    }
    
    Evaluator.startDate = "4-Jan-99";
    Evaluator.endDate = "31-Dec-02";
    
    for(int x = 0; x < oldPareto.size(); x++){
    	InitialPareto11_4.add(Evaluator.theReturn(oldPareto.get(x)) * 100, Evaluator.theVolatility(oldPareto.get(x)));
    }
    
    XYSeriesCollection paretoDrift4 = new XYSeriesCollection( );
    //paretoDrift4.addSeries(ParetoFour);
    paretoDrift4.addSeries(InitialPareto11_4);
    paretoDrift4.addSeries(ParetoFive);
    //paretoDrift4.addSeries(InitialPareto14);

    runLengthDistribution.addSeries(RunFive);
    paretoComparison.addSeries(ParetoFive);

    xylineChart = ChartFactory.createXYLineChart(
        "Generations of Algorithm", 
        "Return",
        "Volatility", 
         dataset,
         PlotOrientation.VERTICAL, 
         true, true, false);

    plot = xylineChart.getXYPlot();
    renderer = new XYLineAndShapeRenderer();
    plot.setRenderer(renderer);
    plot.setBackgroundPaint(Color.DARK_GRAY);
         
     width = 1280;
     height = 960; 
     XYChart = new File( "2002.jpeg" ); 
     ChartUtilities.saveChartAsJPEG( XYChart, xylineChart, width, height);
     
     
     pop = new Population(100);  
     //Evaluator.ceiling = 0.25;
     //Evaluator.publicMutationRate = 0.9;
     Evaluator.startDate = "3-Jan-00";
     Evaluator.endDate = "31-Dec-02";
     pop.initialisation();
     popArray = pop.returnPop();
     pop.nonDominatedSorting(popArray.size());
     pop.crowdingDistance(popArray);
     
     XYSeries ParetoFive00 = new XYSeries("Actual 00 - 02 Front");
     
     graphArray = new ArrayList<>();

     for(int x = 0; x < 21; x++){
     	graphArray.add(new XYSeries("Gen " + (x + 1)));
     }

     for(int x = 0; x < graphArray.size(); x++){
             pop.evolvePopulation(); 
             popArray = pop.returnPop();
             frontArray = pop.getFrontList();
             for(int y = 0; y < frontArray.get(0).size(); y++){
             	graphArray.get(x).add(frontArray.get(0).get(y).solutionReturn * 100, frontArray.get(0).get(y).solutionRisk);
             }
     }
     
     for(int x = 0; x < frontArray.get(0).size(); x++){
     	ParetoFive00.add(frontArray.get(0).get(x).solutionReturn * 100, frontArray.get(0).get(x).solutionRisk);
     }
    
     XYSeriesCollection paretoDrift499 = new XYSeriesCollection( );
     paretoDrift499.addSeries(ParetoFive00);

     //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!NEW RUN OF ALGORITHM!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
     //Evaluator.ceiling = 1.0;
     ///Evaluator.publicMutationRate = 0.0;
     pop = new Population(100);  
     Evaluator.startDate = "4-Jan-16";
     Evaluator.endDate = "8-Nov-16";
     XYSeries InitialPareto11_5 = new XYSeries("2014 Portfolios Reused");
     pop.initialisation();
     popArray = pop.returnPop();
     pop.nonDominatedSorting(popArray.size());
     pop.crowdingDistance(popArray);
     
     System.out.println("!!!!!!!INITIAL POPULATION!!!!!!");

     //Collections.sort(popArray, new ReturnComparator());
     //Collections.sort(popArray, new RiskComparator());
     //Collections.sort(popArray, new CrowdingDistanceComparator());

     pop.printPopDetails(popArray);
     GenZero = new XYSeries( "Gen Zero" );
     XYSeries RunSix = new XYSeries("2016", false);
     XYSeries ParetoSix = new XYSeries("Actual 2016 Front");
     XYSeries InitialPareto15 = new XYSeries("2015 Portfolios Reused");
     popArray = pop.returnPop();
     frontArray = pop.getFrontList();
     for(int x = 0; x < frontArray.get(0).size(); x++){
     	GenZero.add(frontArray.get(0).get(x).solutionReturn * 100, frontArray.get(0).get(x).solutionRisk);
     }

     RunSix.add(pop.getAverageSOfitness(), 0);

     graphArray = new ArrayList<>();

     for(int x = 0; x < 21; x++){
     		graphArray.add(new XYSeries("Gen " + (x + 1)));
     }

     for(int x = 0; x < graphArray.size(); x++){
             pop.evolvePopulation(); 
             popArray = pop.returnPop();
             frontArray = pop.getFrontList();
     	        for(int y = 0; y < frontArray.get(0).size(); y++){
     	        	graphArray.get(x).add(frontArray.get(0).get(y).solutionReturn * 100, frontArray.get(0).get(y).solutionRisk);
             }
         RunSix.add(pop.getAverageSOfitness(), x + 1);
     }
     
     ArrayList<StockWeightingSolution> oldPareto6 = new ArrayList<>();
     for(int x = 0; x < frontArray.get(0).size(); x++){
     	oldPareto6.add(new StockWeightingSolution(true));
     }
     for(int x = 0; x < frontArray.get(0).size(); x++){
     	ParetoSix.add(frontArray.get(0).get(x).solutionReturn * 100, frontArray.get(0).get(x).solutionRisk);
     	for(int y = 0; y < frontArray.get(0).get(x).alleles.size(); y++){
     		oldPareto6.get(x).alleles.set(y, frontArray.get(0).get(x).alleles.get(y));
     	}
     }

     dataset = new XYSeriesCollection( );
     dataset.addSeries(GenZero);
     for(int x = 0; x < graphArray.size(); x++){
     	if((x + 1) % 3 == 0){
     		dataset.addSeries(graphArray.get(x));
     	}
     }
     
     for(int x = 0; x < oldPareto5.size(); x++){
     	InitialPareto15.add(Evaluator.theReturn(oldPareto5.get(x)) * 100, Evaluator.theVolatility(oldPareto5.get(x)));
     }
     
     for(int x = 0; x < oldPareto4.size(); x++){
     	InitialPareto11_5.add(Evaluator.theReturn(oldPareto4.get(x)) * 100, Evaluator.theVolatility(oldPareto4.get(x)));
     }
     
     XYSeriesCollection paretoDrift5 = new XYSeriesCollection( );
     //paretoDrift5.addSeries(ParetoFive);
     paretoDrift5.addSeries(ParetoSix);
     paretoDrift5.addSeries(InitialPareto11_5);
     paretoDrift5.addSeries(InitialPareto15);


     runLengthDistribution.addSeries(RunSix);
     paretoComparison.addSeries(ParetoSix);

     xylineChart = ChartFactory.createXYLineChart(
         "Generations of Algorithm", 
         "Return",
         "Volatility", 
          dataset,
          PlotOrientation.VERTICAL, 
          true, true, false);

     plot = xylineChart.getXYPlot();
     renderer = new XYLineAndShapeRenderer();
     plot.setRenderer(renderer);
     plot.setBackgroundPaint(Color.DARK_GRAY);
          
      width = 1280;
      height = 960; 
      XYChart = new File( "2016.jpeg" ); 
      ChartUtilities.saveChartAsJPEG( XYChart, xylineChart, width, height);
     
  
      //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!NEW RUN OF ALGORITHM!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
      //Evaluator.ceiling = 1.0;
      ///Evaluator.publicMutationRate = 0.0;
      pop = new Population(100);  
      Evaluator.startDate = "4-Jan-16";
      Evaluator.endDate = "8-Nov-16";
      XYSeries InitialPareto11_6 = new XYSeries("2010 Portfolios Reused");
      pop.initialisation();
      popArray = pop.returnPop();
      pop.nonDominatedSorting(popArray.size());
      pop.crowdingDistance(popArray);
      
      System.out.println("!!!!!!!INITIAL POPULATION!!!!!!");

      //Collections.sort(popArray, new ReturnComparator());
      //Collections.sort(popArray, new RiskComparator());
      //Collections.sort(popArray, new CrowdingDistanceComparator());

      pop.printPopDetails(popArray);
      GenZero = new XYSeries( "Gen Zero" );
      XYSeries RunSeven = new XYSeries("2016", false);
      XYSeries ParetoSeven = new XYSeries("Actual 2016 Front");
      XYSeries InitialPareto16 = new XYSeries("2015 Portfolios Reused");
      popArray = pop.returnPop();
      frontArray = pop.getFrontList();
      for(int x = 0; x < frontArray.get(0).size(); x++){
      	GenZero.add(frontArray.get(0).get(x).solutionReturn * 100, frontArray.get(0).get(x).solutionRisk);
      }

      RunSeven.add(pop.getAverageSOfitness(), 0);

      graphArray = new ArrayList<>();

      for(int x = 0; x < 21; x++){
      		graphArray.add(new XYSeries("Gen " + (x + 1)));
      }

      for(int x = 0; x < graphArray.size(); x++){
              pop.evolvePopulation(); 
              popArray = pop.returnPop();
              frontArray = pop.getFrontList();
      	        for(int y = 0; y < frontArray.get(0).size(); y++){
      	        	graphArray.get(x).add(frontArray.get(0).get(y).solutionReturn * 100, frontArray.get(0).get(y).solutionRisk);
              }
          RunSeven.add(pop.getAverageSOfitness(), x + 1);
      }

      for(int x = 0; x < frontArray.get(0).size(); x++){
      	ParetoSeven.add(frontArray.get(0).get(x).solutionReturn * 100, frontArray.get(0).get(x).solutionRisk);
      }

      dataset = new XYSeriesCollection( );
      dataset.addSeries(GenZero);
      for(int x = 0; x < graphArray.size(); x++){
      	if((x + 1) % 3 == 0){
      		dataset.addSeries(graphArray.get(x));
      	}
      }
      
      for(int x = 0; x < oldPareto4.size(); x++){
      	InitialPareto16.add(Evaluator.theReturn(oldPareto6.get(x)) * 100, Evaluator.theVolatility(oldPareto6.get(x)));
      }
      
      for(int x = 0; x < oldPareto.size(); x++){
      	InitialPareto11_6.add(Evaluator.theReturn(oldPareto.get(x)) * 100, Evaluator.theVolatility(oldPareto.get(x)));
      }
      
      XYSeriesCollection paretoDrift6 = new XYSeriesCollection( );
      //paretoDrift6.addSeries(ParetoSix);
      paretoDrift6.addSeries(ParetoSeven);
      paretoDrift6.addSeries(InitialPareto11_6);
      paretoDrift6.addSeries(InitialPareto16);


      runLengthDistribution.addSeries(RunSeven);
      paretoComparison.addSeries(ParetoSeven);

      xylineChart = ChartFactory.createXYLineChart(
          "Generations of Algorithm", 
          "Return",
          "Volatility", 
           dataset,
           PlotOrientation.VERTICAL, 
           true, true, false);

      plot = xylineChart.getXYPlot();
      renderer = new XYLineAndShapeRenderer();
      plot.setRenderer(renderer);
      plot.setBackgroundPaint(Color.DARK_GRAY);
           
       width = 1280;
       height = 960; 
       XYChart = new File( "2016.jpeg" ); 
       ChartUtilities.saveChartAsJPEG( XYChart, xylineChart, width, height);
     
   **/
     
     
     /**
     //Make a new population
     Evaluator.publicMutationRate = 0.9;
     Evaluator.floor = 0.05;
     Evaluator.ceiling = 0.2;
     Evaluator.startDate = "2-Jan-98";
     Evaluator.endDate = "31-Dec-98";
     ArrayList<StockWeightingSolution> oldParetoA = new ArrayList<>();
     pop = new Population(100);  
     pop.initialisation();
     popArray = pop.returnPop();
     pop.nonDominatedSorting(popArray.size());
     pop.crowdingDistance(popArray);

     System.out.println("!!!!!!!INITIAL POPULATION!!!!!!");

     //Collections.sort(popArray, new ReturnComparator());
     //Collections.sort(popArray, new RiskComparator());
     //Collections.sort(popArray, new CrowdingDistanceComparator());

     pop.printPopDetails(popArray);
     GenZero = new XYSeries( "Gen Zero" );
     XYSeries RunOneA = new XYSeries("1998", false);
     XYSeries ParetoOneA = new XYSeries("Actual 1998 Front (Constrained)");
     popArray = pop.returnPop();
     frontArray = pop.getFrontList();
     for(int x = 0; x < frontArray.get(0).size(); x++){
     	GenZero.add(frontArray.get(0).get(x).solutionReturn * 100, frontArray.get(0).get(x).solutionRisk);
     }
    
     for(int y = 0; y < popArray.size(); y++){
     	GenZero.add(popArray.get(y).solutionReturn * 100, popArray.get(y).solutionRisk);
     }
     

     RunOneA.add(pop.getAverageSOfitness(), 0);

     ArrayList<XYSeries> graphArrayA = new ArrayList<>();
     for(int x = 0; x < 21; x++){
     		graphArrayA.add(new XYSeries("Gen " + (x + 1)));
     }

     for(int x = 0; x < graphArrayA.size(); x++){
             pop.evolvePopulation(); 
             popArray = pop.returnPop();
             frontArray = pop.getFrontList();
     	        for(int y = 0; y < frontArray.get(0).size(); y++){
     	        	graphArrayA.get(x).add(frontArray.get(0).get(y).solutionReturn * 100, frontArray.get(0).get(y).solutionRisk);
             }
         RunOneA.add(pop.getAverageSOfitness(), x + 1);
     }

     for(int x = 0; x < frontArray.get(0).size(); x++){
     	oldParetoA.add(new StockWeightingSolution(true));
     }
     for(int x = 0; x < frontArray.get(0).size(); x++){
     	ParetoOneA.add(frontArray.get(0).get(x).solutionReturn * 100, frontArray.get(0).get(x).solutionRisk);
     	for(int y = 0; y < frontArray.get(0).get(x).alleles.size(); y++){
     		oldParetoA.get(x).alleles.set(y, frontArray.get(0).get(x).alleles.get(y));
     	}
     }

     XYSeriesCollection datasetA = new XYSeriesCollection( );
     datasetA.addSeries(GenZero);
     for(int x = 0; x < graphArrayA.size(); x++){
     	if((x + 1) % 3 == 0){
     		datasetA.addSeries(graphArrayA.get(x));
     	}
     }

     XYSeriesCollection runLengthDistributionA = new XYSeriesCollection( );
     runLengthDistributionA.addSeries(RunOneA);

     XYSeriesCollection paretoComparisonA = new XYSeriesCollection( );
     paretoComparisonA.addSeries(ParetoOneA);

     xylineChart = ChartFactory.createXYLineChart(
             "Generations of Algorithm", 
             "Return",
             "Volatility", 
             dataset,
             PlotOrientation.VERTICAL, 
             true, true, false);

     plot = xylineChart.getXYPlot();
     renderer = new XYLineAndShapeRenderer();
     plot.setRenderer(renderer);
     plot.setBackgroundPaint(Color.DARK_GRAY);
          
          width = 1280;
          height = 960;
          XYChart = new File( "1998Con.jpeg" ); 
          ChartUtilities.saveChartAsJPEG( XYChart, xylineChart, width, height);

     //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!NEW RUN OF ALGORITHM!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
      pop = new Population(100);  
      //Evaluator.ceiling = 0.25;
      //Evaluator.publicMutationRate = 0.9;
      Evaluator.startDate = "4-Jan-99";
      Evaluator.endDate = "31-Dec-99";
      pop.initialisation();
      popArray = pop.returnPop();
      pop.nonDominatedSorting(popArray.size());
      pop.crowdingDistance(popArray);
      ArrayList<StockWeightingSolution> oldPareto2A = new ArrayList<>();
      
      System.out.println("!!!!!!!INITIAL POPULATION!!!!!!");
      
      //Collections.sort(popArray, new ReturnComparator());
      //Collections.sort(popArray, new RiskComparator());
      //Collections.sort(popArray, new CrowdingDistanceComparator());
      
      pop.printPopDetails(popArray);
      GenZero = new XYSeries( "Generation Zero" );
      XYSeries RunTwoA = new XYSeries("1999", false);
      XYSeries ParetoTwoA = new XYSeries("Actual 99 Front (Constrained)");
      XYSeries InitialPareto11_1A = new XYSeries("1998 Reused (Constrained)");
      popArray = pop.returnPop();
     frontArray = pop.getFrontList();
     for(int x = 0; x < frontArray.get(0).size(); x++){
     	GenZero.add(frontArray.get(0).get(x).solutionReturn * 100, frontArray.get(0).get(x).solutionRisk);
     }
      
      RunTwoA.add(pop.getAverageSOfitness(), 0);
      
      
     graphArrayA = new ArrayList<>();

     for(int x = 0; x < 21; x++){
     	graphArrayA.add(new XYSeries("Gen " + (x + 1)));
     }

     for(int x = 0; x < graphArrayA.size(); x++){
             pop.evolvePopulation(); 
             popArray = pop.returnPop();
             frontArray = pop.getFrontList();
             for(int y = 0; y < frontArray.get(0).size(); y++){
             	graphArrayA.get(x).add(frontArray.get(0).get(y).solutionReturn * 100, frontArray.get(0).get(y).solutionRisk);
             }
         RunTwoA.add(pop.getAverageSOfitness(), x + 1);
     }

     Evaluator.startDate = "4-Jan-99";
     Evaluator.endDate = "31-Dec-99";
     
     for(int x = 0; x < frontArray.get(0).size(); x++){
     	oldPareto2A.add(new StockWeightingSolution(true));
     }
     for(int x = 0; x < frontArray.get(0).size(); x++){
     	ParetoTwoA.add(frontArray.get(0).get(x).solutionReturn * 100, frontArray.get(0).get(x).solutionRisk);
     	for(int y = 0; y < frontArray.get(0).get(x).alleles.size(); y++){
     		oldPareto2A.get(x).alleles.set(y, frontArray.get(0).get(x).alleles.get(y));
     	}
     }
     
     
     for(int x = 0; x < oldParetoA.size(); x++){
     	InitialPareto11_1A.add(Evaluator.theReturn(oldParetoA.get(x)) * 100, Evaluator.theVolatility(oldParetoA.get(x)));
     }
     
     //paretoDrift.addSeries(ParetoOne);
     paretoDrift.addSeries(ParetoTwoA);
     paretoDrift.addSeries(InitialPareto11_1A);

     datasetA = new XYSeriesCollection( );
     datasetA.addSeries(GenZero);
     for(int x = 0; x < graphArrayA.size(); x++){
     	if((x + 1) % 3 == 0){
     		datasetA.addSeries(graphArrayA.get(x));
     	}
     }
      
      runLengthDistributionA.addSeries(RunTwoA);
      
      paretoComparisonA.addSeries(ParetoTwoA);
      
      xylineChart = ChartFactory.createXYLineChart(
              "Generations of Algorithm", 
              "Return",
              "Volatility", 
              datasetA,
              PlotOrientation.VERTICAL, 
              true, true, false);
      
      plot = xylineChart.getXYPlot();
      renderer = new XYLineAndShapeRenderer();
      plot.setRenderer(renderer);
      plot.setBackgroundPaint(Color.DARK_GRAY);
           
     width = 1280; //Width of the image     
     height = 960; //Height of the image  
     XYChart = new File( "1999Con.jpeg" ); 
           ChartUtilities.saveChartAsJPEG( XYChart, xylineChart, width, height);
              
                    
      //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!NEW RUN OF ALGORITHM!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        //Evaluator.ceiling = 0.5;
        //Evaluator.publicMutationRate = 0.75;
           ArrayList<StockWeightingSolution> oldPareto3A = new ArrayList<>();
        pop = new Population(100);  
        XYSeries InitialPareto11_2A = new XYSeries("1998 Reused (Constrained)");
        Evaluator.startDate = "4-Jan-99";
        Evaluator.endDate = "29-Dec-00";
        pop.initialisation();
        popArray = pop.returnPop();
        pop.nonDominatedSorting(popArray.size());
        pop.crowdingDistance(popArray);
        
        
        System.out.println("!!!!!!!INITIAL POPULATION!!!!!!");
        
        //Collections.sort(popArray, new ReturnComparator());
        //Collections.sort(popArray, new RiskComparator());
        //Collections.sort(popArray, new CrowdingDistanceComparator());
        
        pop.printPopDetails(popArray);
        GenZero = new XYSeries( "Generation Zero" );
        XYSeries RunThreeA = new XYSeries("2000", false);
        XYSeries ParetoThreeA = new XYSeries("Actual 99 - 00 Front (Constrained)");

        popArray = pop.returnPop();
     	frontArray = pop.getFrontList();
     	for(int x = 0; x < frontArray.get(0).size(); x++){
     		GenZero.add(frontArray.get(0).get(x).solutionReturn * 100, frontArray.get(0).get(x).solutionRisk);
     	}
        
        RunThreeA.add(pop.getAverageSOfitness(), 0);
        
        
     graphArrayA = new ArrayList<>();

     for(int x = 0; x < 21; x++){
     	graphArrayA.add(new XYSeries("Gen " + (x + 1)));
     }

     for(int x = 0; x < graphArray.size(); x++){
             pop.evolvePopulation(); 
             popArray = pop.returnPop();
             frontArray = pop.getFrontList();
             for(int y = 0; y < frontArray.get(0).size(); y++){
             	graphArrayA.get(x).add(frontArray.get(0).get(y).solutionReturn * 100, frontArray.get(0).get(y).solutionRisk);
             }
         RunThreeA.add(pop.getAverageSOfitness(), x + 1);
     }
     
     Evaluator.startDate = "4-Jan-99";
     Evaluator.endDate = "29-Dec-00";
     
     for(int x = 0; x < frontArray.get(0).size(); x++){
     	oldPareto3A.add(new StockWeightingSolution(true));
     }
     for(int x = 0; x < frontArray.get(0).size(); x++){
     	ParetoThreeA.add(frontArray.get(0).get(x).solutionReturn * 100, frontArray.get(0).get(x).solutionRisk);
     	for(int y = 0; y < frontArray.get(0).get(x).alleles.size(); y++){
     		oldPareto3A.get(x).alleles.set(y, frontArray.get(0).get(x).alleles.get(y));
     	}
     }
      
     Evaluator.startDate = "4-Jan-99";
     Evaluator.endDate = "29-Dec-00";
     
     for(int x = 0; x < oldPareto.size(); x++){
     	InitialPareto11_2A.add(Evaluator.theReturn(oldParetoA.get(x)) * 100, Evaluator.theVolatility(oldParetoA.get(x)));
     }
     
     //paretoDrift2.addSeries(ParetoTwo);
     paretoDrift2.addSeries(ParetoThreeA);
     paretoDrift2.addSeries(InitialPareto11_2A);

     datasetA = new XYSeriesCollection( );
     datasetA.addSeries(GenZero);
     for(int x = 0; x < graphArray.size(); x++){
     	if((x + 1) % 3 == 0){
     		datasetA.addSeries(graphArray.get(x));
     	}
     }
        
        runLengthDistributionA.addSeries(RunThreeA);
        paretoComparisonA.addSeries(ParetoThreeA);
        
        xylineChart = ChartFactory.createXYLineChart(
            "Generations of Algorithm", 
            "Return",
            "Volatility", 
             datasetA,
             PlotOrientation.VERTICAL, 
             true, true, false);
        
        plot = xylineChart.getXYPlot();
        renderer = new XYLineAndShapeRenderer();
        plot.setRenderer(renderer);
        plot.setBackgroundPaint(Color.DARK_GRAY);
             
         width = 1280; //Width of the image 
         height = 960; //Height of the image 
         XYChart = new File( "2000Con.jpeg" ); 
         ChartUtilities.saveChartAsJPEG( XYChart, xylineChart, width, height);
         
         
         
         


         
         pop = new Population(100);  
         //Evaluator.ceiling = 0.25;
         //Evaluator.publicMutationRate = 0.9;
         Evaluator.startDate = "3-Jan-00";
         Evaluator.endDate = "29-Dec-00";
         pop.initialisation();
         popArray = pop.returnPop();
         pop.nonDominatedSorting(popArray.size());
         pop.crowdingDistance(popArray);
         
         XYSeries ParetoThreeA00 = new XYSeries("Actual 00 Front (Constrained)");
         XYSeries InitialPareto12A = new XYSeries("1999 Reused (Constrained)");
         
         graphArray = new ArrayList<>();

         for(int x = 0; x < 21; x++){
         	graphArray.add(new XYSeries("Gen " + (x + 1)));
         }

         for(int x = 0; x < graphArray.size(); x++){
                 pop.evolvePopulation(); 
                 popArray = pop.returnPop();
                 frontArray = pop.getFrontList();
                 for(int y = 0; y < frontArray.get(0).size(); y++){
                 	graphArray.get(x).add(frontArray.get(0).get(y).solutionReturn * 100, frontArray.get(0).get(y).solutionRisk);
                 }
         }
         
         for(int x = 0; x < frontArray.get(0).size(); x++){
         	ParetoThreeA00.add(frontArray.get(0).get(x).solutionReturn * 100, frontArray.get(0).get(x).solutionRisk);
         }
         
         for(int x = 0; x < oldPareto2A.size(); x++){
          	InitialPareto12A.add(Evaluator.theReturn(oldPareto2A.get(x)) * 100, Evaluator.theVolatility(oldPareto2A.get(x)));
          }
        
         paretoDrift299.addSeries(InitialPareto12);
         paretoDrift299.addSeries(InitialPareto12A);
         paretoDrift299.addSeries(ParetoThreeA00);

      
      //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!NEW RUN OF ALGORITHM!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
      //Evaluator.ceiling = 0.75;
      //Evaluator.publicMutationRate = 0.5;
      pop = new Population(100);  
      Evaluator.startDate = "4-Jan-99";
      Evaluator.endDate = "31-Dec-01";
      pop.initialisation();
      popArray = pop.returnPop();
      pop.nonDominatedSorting(popArray.size());
      pop.crowdingDistance(popArray);
      XYSeries InitialPareto11_3A = new XYSeries("1998 Reused (Constrained)");
      ArrayList<StockWeightingSolution> oldPareto4A = new ArrayList<>();
      
      System.out.println("!!!!!!!INITIAL POPULATION!!!!!!");
      
      //Collections.sort(popArray, new ReturnComparator());
      //Collections.sort(popArray, new RiskComparator());
      //Collections.sort(popArray, new CrowdingDistanceComparator());
      
      pop.printPopDetails(popArray);
      GenZero = new XYSeries( "Generation Zero" );
      XYSeries RunFourA = new XYSeries("2001", false);
      XYSeries ParetoFourA = new XYSeries("Actual 99 - 01 Front (Constrained)");
      popArray = pop.returnPop();
     frontArray = pop.getFrontList();
     for(int x = 0; x < frontArray.get(0).size(); x++){
     	GenZero.add(frontArray.get(0).get(x).solutionReturn * 100, frontArray.get(0).get(x).solutionRisk);
     }
      
      RunFourA.add(pop.getAverageSOfitness(), 0);
      
      
     graphArrayA = new ArrayList<>();

     for(int x = 0; x < 21; x++){
     	graphArrayA.add(new XYSeries("Gen " + (x + 1)));
     }

     for(int x = 0; x < graphArray.size(); x++){
             pop.evolvePopulation(); 
             popArray = pop.returnPop();
             frontArray = pop.getFrontList();
             for(int y = 0; y < frontArray.get(0).size(); y++){
             	graphArrayA.get(x).add(frontArray.get(0).get(y).solutionReturn * 100, frontArray.get(0).get(y).solutionRisk);
             }
         RunFourA.add(pop.getAverageSOfitness(), x + 1);
     }

     datasetA = new XYSeriesCollection( );
     datasetA.addSeries(GenZero);
     for(int x = 0; x < graphArrayA.size(); x++){
     	if((x + 1) % 3 == 0){
     		datasetA.addSeries(graphArrayA.get(x));
     	}
     }
     
     Evaluator.startDate = "4-Jan-99";
     Evaluator.endDate = "31-Dec-01";

     for(int x = 0; x < frontArray.get(0).size(); x++){
     	oldPareto4A.add(new StockWeightingSolution(true));
     }
     for(int x = 0; x < frontArray.get(0).size(); x++){
     	ParetoFourA.add(frontArray.get(0).get(x).solutionReturn * 100, frontArray.get(0).get(x).solutionRisk);
     	for(int y = 0; y < frontArray.get(0).get(x).alleles.size(); y++){
     		oldPareto4A.get(x).alleles.set(y, frontArray.get(0).get(x).alleles.get(y));
     	}
     }
     
     Evaluator.startDate = "4-Jan-99";
     Evaluator.endDate = "31-Dec-01";
     
     for(int x = 0; x < oldPareto.size(); x++){
     	InitialPareto11_3A.add(Evaluator.theReturn(oldParetoA.get(x)) * 100, Evaluator.theVolatility(oldParetoA.get(x)));
     }
     
     //paretoDrift3.addSeries(ParetoThree);
     paretoDrift3.addSeries(ParetoFourA);
     paretoDrift3.addSeries(InitialPareto11_3A);
      
      runLengthDistributionA.addSeries(RunFourA);
      paretoComparisonA.addSeries(ParetoFourA);
      
      xylineChart = ChartFactory.createXYLineChart(
          "Generations of Algorithm", 
          "Return",
          "Volatility", 
           datasetA,
           PlotOrientation.VERTICAL, 
           true, true, false);
      
      plot = xylineChart.getXYPlot();
      renderer = new XYLineAndShapeRenderer();
      plot.setRenderer(renderer);
      plot.setBackgroundPaint(Color.DARK_GRAY);
           
       width = 1280; //Width of the image 
       height = 960; //Height of the image
       XYChart = new File( "2001Con.jpeg" ); 
       ChartUtilities.saveChartAsJPEG( XYChart, xylineChart, width, height);
       
       
       
       
       pop = new Population(100);  
       //Evaluator.ceiling = 0.25;
       //Evaluator.publicMutationRate = 0.9;
       Evaluator.startDate = "3-Jan-00";
       Evaluator.endDate = "31-Dec-01";
       pop.initialisation();
       popArray = pop.returnPop();
       pop.nonDominatedSorting(popArray.size());
       pop.crowdingDistance(popArray);
       
       XYSeries ParetoFourA00 = new XYSeries("Actual 00 - 01 Front (Constrained)");
       XYSeries InitialPareto13A = new XYSeries("1999 Reused (Constrained)");
       
       graphArray = new ArrayList<>();

       for(int x = 0; x < 21; x++){
       	graphArray.add(new XYSeries("Gen " + (x + 1)));
       }

       for(int x = 0; x < graphArray.size(); x++){
               pop.evolvePopulation(); 
               popArray = pop.returnPop();
               frontArray = pop.getFrontList();
               for(int y = 0; y < frontArray.get(0).size(); y++){
               	graphArray.get(x).add(frontArray.get(0).get(y).solutionReturn * 100, frontArray.get(0).get(y).solutionRisk);
               }
       }
       
       for(int x = 0; x < frontArray.get(0).size(); x++){
       	ParetoFourA00.add(frontArray.get(0).get(x).solutionReturn * 100, frontArray.get(0).get(x).solutionRisk);
       }
       
       for(int x = 0; x < oldPareto2A.size(); x++){
        	InitialPareto13A.add(Evaluator.theReturn(oldPareto2A.get(x)) * 100, Evaluator.theVolatility(oldPareto2A.get(x)));
        }
      
       paretoDrift399.addSeries(InitialPareto13);
       paretoDrift399.addSeries(InitialPareto13A);
       paretoDrift399.addSeries(ParetoFourA00);

     //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!NEW RUN OF ALGORITHM!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
     //Evaluator.ceiling = 1.0;
     //Evaluator.publicMutationRate = 0.0;
     pop = new Population(100);  
     Evaluator.startDate = "4-Jan-99";
     Evaluator.endDate = "31-Dec-02";
     XYSeries InitialPareto11_4A = new XYSeries("1998 Reused (Constrained)");
     pop.initialisation();
     popArray = pop.returnPop();
     pop.nonDominatedSorting(popArray.size());
     pop.crowdingDistance(popArray);
     
     System.out.println("!!!!!!!INITIAL POPULATION!!!!!!");

     //Collections.sort(popArray, new ReturnComparator());
     //Collections.sort(popArray, new RiskComparator());
     //Collections.sort(popArray, new CrowdingDistanceComparator());

     ArrayList<StockWeightingSolution> oldPareto5A = new ArrayList<>();
     pop.printPopDetails(popArray);
     GenZero = new XYSeries( "Gen Zero" );
     XYSeries RunFiveA = new XYSeries("2002", false);
     XYSeries ParetoFiveA = new XYSeries("Actual 99 - 02 Front (Constrained)");
     popArray = pop.returnPop();
     frontArray = pop.getFrontList();
     for(int x = 0; x < frontArray.get(0).size(); x++){
     	GenZero.add(frontArray.get(0).get(x).solutionReturn * 100, frontArray.get(0).get(x).solutionRisk);
     }

     RunFiveA.add(pop.getAverageSOfitness(), 0);

     graphArrayA = new ArrayList<>();

     for(int x = 0; x < 21; x++){
     		graphArrayA.add(new XYSeries("Gen " + (x + 1)));
     }

     for(int x = 0; x < graphArray.size(); x++){
             pop.evolvePopulation(); 
             popArray = pop.returnPop();
             frontArray = pop.getFrontList();
     	        for(int y = 0; y < frontArray.get(0).size(); y++){
     	        	graphArrayA.get(x).add(frontArray.get(0).get(y).solutionReturn * 100, frontArray.get(0).get(y).solutionRisk);
             }
         RunFiveA.add(pop.getAverageSOfitness(), x + 1);
     }

     Evaluator.startDate = "4-Jan-99";
     Evaluator.endDate = "31-Dec-02";
     
     for(int x = 0; x < frontArray.get(0).size(); x++){
     	oldPareto5A.add(new StockWeightingSolution(true));
     }
     for(int x = 0; x < frontArray.get(0).size(); x++){
     	ParetoFiveA.add(frontArray.get(0).get(x).solutionReturn * 100, frontArray.get(0).get(x).solutionRisk);
     	for(int y = 0; y < frontArray.get(0).get(x).alleles.size(); y++){
     		oldPareto5A.get(x).alleles.set(y, frontArray.get(0).get(x).alleles.get(y));
     	}
     }
     

     datasetA = new XYSeriesCollection( );
     datasetA.addSeries(GenZero);
     for(int x = 0; x < graphArrayA.size(); x++){
     	if((x + 1) % 3 == 0){
     		datasetA.addSeries(graphArray.get(x));
     	}
     }
     
     Evaluator.startDate = "4-Jan-99";
     Evaluator.endDate = "31-Dec-02";
     
     for(int x = 0; x < oldPareto.size(); x++){
     	InitialPareto11_4A.add(Evaluator.theReturn(oldParetoA.get(x)) * 100, Evaluator.theVolatility(oldParetoA.get(x)));
     }
     
     //paretoDrift4.addSeries(ParetoFour);
     paretoDrift4.addSeries(ParetoFiveA);
     paretoDrift4.addSeries(InitialPareto11_4A);


     runLengthDistributionA.addSeries(RunFiveA);
     paretoComparisonA.addSeries(ParetoFiveA);

     xylineChart = ChartFactory.createXYLineChart(
         "Generations of Algorithm", 
         "Return",
         "Volatility", 
          datasetA,
          PlotOrientation.VERTICAL, 
          true, true, false);

     plot = xylineChart.getXYPlot();
     renderer = new XYLineAndShapeRenderer();
     plot.setRenderer(renderer);
     plot.setBackgroundPaint(Color.DARK_GRAY);
          
      width = 1280;
      height = 960; 
      XYChart = new File( "2002Con.jpeg" ); 
      ChartUtilities.saveChartAsJPEG( XYChart, xylineChart, width, height);    
     
     
      pop = new Population(100);  
      //Evaluator.ceiling = 0.25;
      //Evaluator.publicMutationRate = 0.9;
      Evaluator.startDate = "3-Jan-00";
      Evaluator.endDate = "31-Dec-02";
      pop.initialisation();
      popArray = pop.returnPop();
      pop.nonDominatedSorting(popArray.size());
      pop.crowdingDistance(popArray);
      
      XYSeries ParetoFiveA00 = new XYSeries("Actual 00 - 02 Front (Constrained)");
      XYSeries InitialPareto14A = new XYSeries("1999 Reused (Constrained)");
      
      graphArray = new ArrayList<>();

      for(int x = 0; x < 21; x++){
      	graphArray.add(new XYSeries("Gen " + (x + 1)));
      }

      for(int x = 0; x < graphArray.size(); x++){
              pop.evolvePopulation(); 
              popArray = pop.returnPop();
              frontArray = pop.getFrontList();
              for(int y = 0; y < frontArray.get(0).size(); y++){
              	graphArray.get(x).add(frontArray.get(0).get(y).solutionReturn * 100, frontArray.get(0).get(y).solutionRisk);
              }
      }
      
      for(int x = 0; x < frontArray.get(0).size(); x++){
      	ParetoFiveA00.add(frontArray.get(0).get(x).solutionReturn * 100, frontArray.get(0).get(x).solutionRisk);
      }
      
      for(int x = 0; x < oldPareto2A.size(); x++){
       	InitialPareto14A.add(Evaluator.theReturn(oldPareto2A.get(x)) * 100, Evaluator.theVolatility(oldPareto2A.get(x)));
       }
     
      paretoDrift499.addSeries(InitialPareto14);
      paretoDrift499.addSeries(InitialPareto14A);
      paretoDrift499.addSeries(ParetoFiveA00);
     

     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
    xylineChart = ChartFactory.createXYLineChart(
             "Run Length Distribution",  
             "Avg. Fitness",
             "Generation",
             runLengthDistribution,
             PlotOrientation.HORIZONTAL, 
             true, true, false);
         
    plot = xylineChart.getXYPlot();
    renderer = new XYLineAndShapeRenderer();
    plot.setRenderer(renderer);
    plot.setBackgroundPaint(Color.DARK_GRAY);
      
    width = 1280;
    height = 960;
    XYChart = new File( "RunLengthDistribution.jpeg" ); 
    ChartUtilities.saveChartAsJPEG( XYChart, xylineChart, width, height); 
    
    xylineChart = ChartFactory.createXYLineChart(
            "Run Length Distribution",  
            "Avg. Fitness",
            "Generation",
            runLengthDistributionA,
            PlotOrientation.HORIZONTAL, 
            true, true, false);
        
   plot = xylineChart.getXYPlot();
   renderer = new XYLineAndShapeRenderer();
   plot.setRenderer(renderer);
   plot.setBackgroundPaint(Color.DARK_GRAY);
     
   width = 1280;
   height = 960;
   XYChart = new File( "RunLengthDistributionConstrained.jpeg" ); 
   ChartUtilities.saveChartAsJPEG( XYChart, xylineChart, width, height);

    xylineChart = ChartFactory.createXYLineChart(
            "Pareto Fronts of all Runs", 
            "Return",
            "Volatility", 
            paretoComparison,
            PlotOrientation.VERTICAL, 
            true, true, false);
        
    plot = xylineChart.getXYPlot();
    renderer = new XYLineAndShapeRenderer();
    plot.setRenderer(renderer);
    plot.setBackgroundPaint(Color.DARK_GRAY);
     
    width = 1280;
    height = 960;
    XYChart = new File( "ParetoFronts.jpeg" ); 
    ChartUtilities.saveChartAsJPEG( XYChart, xylineChart, width, height); 
    
    xylineChart = ChartFactory.createXYLineChart(
            "Pareto Fronts of all Runs", 
            "Return",
            "Volatility", 
            paretoComparisonA,
            PlotOrientation.VERTICAL, 
            true, true, false);
        
    plot = xylineChart.getXYPlot();
    renderer = new XYLineAndShapeRenderer();
    plot.setRenderer(renderer);
    plot.setBackgroundPaint(Color.DARK_GRAY);
     
    width = 1280;
    height = 960;
    XYChart = new File( "ParetoFrontsConstrained.jpeg" ); 
    ChartUtilities.saveChartAsJPEG( XYChart, xylineChart, width, height); 
    
    xylineChart = ChartFactory.createXYLineChart(
            "Performance of 1998 Portfolios in 1999", 
            "Return",
            "Volatility", 
            paretoDrift,
            PlotOrientation.VERTICAL, 
            true, true, false);
        
    plot = xylineChart.getXYPlot();
    renderer = new XYLineAndShapeRenderer();
    plot.setRenderer(renderer);
    plot.setBackgroundPaint(Color.DARK_GRAY);
     
    width = 1280;
    height = 960;
    XYChart = new File( "HistoricPerformance98-99.jpeg" ); 
    ChartUtilities.saveChartAsJPEG( XYChart, xylineChart, width, height); 
    
    xylineChart = ChartFactory.createXYLineChart(
            "Performance of 1998 Portfolios in 2000", 
            "Return",
            "Volatility", 
            paretoDrift2,
            PlotOrientation.VERTICAL, 
            true, true, false);
        
    plot = xylineChart.getXYPlot();
    renderer = new XYLineAndShapeRenderer();
    plot.setRenderer(renderer);
    plot.setBackgroundPaint(Color.DARK_GRAY);
     
    width = 1280;
    height = 960;
    XYChart = new File( "CumulativeHistoricPerformance98-00.jpeg" ); 
    ChartUtilities.saveChartAsJPEG( XYChart, xylineChart, width, height);
    
    xylineChart = ChartFactory.createXYLineChart(
            "Performance of 1999 Portfolios in 2000", 
            "Return",
            "Volatility", 
            paretoDrift299,
            PlotOrientation.VERTICAL, 
            true, true, false);
        
    plot = xylineChart.getXYPlot();
    renderer = new XYLineAndShapeRenderer();
    plot.setRenderer(renderer);
    plot.setBackgroundPaint(Color.DARK_GRAY);
     
    width = 1280;
    height = 960;
    XYChart = new File( "CumulativeHistoricPerformance99-00.jpeg" ); 
    ChartUtilities.saveChartAsJPEG( XYChart, xylineChart, width, height);
    
    xylineChart = ChartFactory.createXYLineChart(
            "Performance of 1998 Portfolios in 2001", 
            "Return",
            "Volatility", 
            paretoDrift3,
            PlotOrientation.VERTICAL, 
            true, true, false);
        
    plot = xylineChart.getXYPlot();
    renderer = new XYLineAndShapeRenderer();
    plot.setRenderer(renderer);
    plot.setBackgroundPaint(Color.DARK_GRAY);
     
    width = 1280;
    height = 960;
    XYChart = new File( "CumulativeHistoricPerformance98-01.jpeg" ); 
    ChartUtilities.saveChartAsJPEG( XYChart, xylineChart, width, height); 
    
    xylineChart = ChartFactory.createXYLineChart(
            "Performance of 1999 Portfolios in 2001", 
            "Return",
            "Volatility", 
            paretoDrift399,
            PlotOrientation.VERTICAL, 
            true, true, false);
        
    plot = xylineChart.getXYPlot();
    renderer = new XYLineAndShapeRenderer();
    plot.setRenderer(renderer);
    plot.setBackgroundPaint(Color.DARK_GRAY);
     
    width = 1280;
    height = 960;
    XYChart = new File( "CumulativeHistoricPerformance99-01.jpeg" ); 
    ChartUtilities.saveChartAsJPEG( XYChart, xylineChart, width, height); 
    
    xylineChart = ChartFactory.createXYLineChart(
            "Performance of 1998 Portfolios in 2002", 
            "Return",
            "Volatility", 
            paretoDrift4,
            PlotOrientation.VERTICAL, 
            true, true, false);
        
    plot = xylineChart.getXYPlot();
    renderer = new XYLineAndShapeRenderer();
    plot.setRenderer(renderer);
    plot.setBackgroundPaint(Color.DARK_GRAY);
     
    width = 1280;
    height = 960;
    XYChart = new File( "CumulativeHistoricPerformance98-02.jpeg" ); 
    ChartUtilities.saveChartAsJPEG( XYChart, xylineChart, width, height); 
    
    xylineChart = ChartFactory.createXYLineChart(
            "Performance of 1999 Portfolios in 2002", 
            "Return",
            "Volatility", 
            paretoDrift499,
            PlotOrientation.VERTICAL, 
            true, true, false);
        
    plot = xylineChart.getXYPlot();
    renderer = new XYLineAndShapeRenderer();
    plot.setRenderer(renderer);
    plot.setBackgroundPaint(Color.DARK_GRAY);
     
    width = 1280;
    height = 960;
    XYChart = new File( "CumulativeHistoricPerformance99-02.jpeg" ); 
    ChartUtilities.saveChartAsJPEG( XYChart, xylineChart, width, height);
    
    **/
    
    /**
    
    xylineChart = ChartFactory.createXYLineChart(
            "Performance of 2014 & 2015 Portfolios in 2016", 
            "Return",
            "Volatility", 
            paretoDrift5,
            PlotOrientation.VERTICAL, 
            true, true, false);
        
    plot = xylineChart.getXYPlot();
    renderer = new XYLineAndShapeRenderer();
    plot.setRenderer(renderer);
    plot.setBackgroundPaint(Color.DARK_GRAY);
     
    width = 1280;
    height = 960;
    XYChart = new File( "HistoricPerformance15-16.jpeg" ); 
    ChartUtilities.saveChartAsJPEG( XYChart, xylineChart, width, height); 
    
    **/
    
    /**
    xylineChart = ChartFactory.createXYLineChart(
            "Performance of 2010 & 2015 Portfolios in 2016", 
            "Return",
            "Volatility", 
            paretoDrift6,
            PlotOrientation.VERTICAL, 
            true, true, false);
            
    plot = xylineChart.getXYPlot();
    renderer = new XYLineAndShapeRenderer();
    plot.setRenderer(renderer);
    plot.setBackgroundPaint(Color.DARK_GRAY);
     
    width = 1280;
    height = 960;
    XYChart = new File( "HistoricPerormance15-16.jpeg" ); 
    ChartUtilities.saveChartAsJPEG( XYChart, xylineChart, width, height); 
    
      **/
    
    
    }
    

}
