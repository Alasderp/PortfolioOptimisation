import java.awt.Color;
import java.io.File;
import java.util.ArrayList;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class Test {
	
	/**
	//Make a new population
	Evaluator.publicMutationRate = 0.9;
	Evaluator.floor = 0.00;
	Evaluator.ceiling = 1.0;
	Evaluator.startDate = "4-Jan-10";
	Evaluator.endDate = "31-Dec-10";
	Evaluator.publicMutationRate = 0.90;
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
	XYSeries RunOne = new XYSeries("2010", false);
	XYSeries ParetoOne = new XYSeries("2010");
	popArray = pop.returnPop();
	ArrayList<ArrayList<StockWeightingSolution>> frontArray = pop.getFrontList();
	for(int x = 0; x < frontArray.get(0).size(); x++){
		GenZero.add(frontArray.get(0).get(x).solutionReturn * 100, frontArray.get(0).get(x).solutionRisk);
	}

	//for(int y = 0; y < popArray.size(); y++){
//		GenZero.add(popArray.get(y).solutionReturn * 100, popArray.get(y).solutionRisk);
	//}


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
		ParetoOne.add(frontArray.get(0).get(x).solutionReturn * 100, frontArray.get(0).get(x).solutionRisk);
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
	     File XYChart = new File( "0% Mutation.jpeg" ); 
	     ChartUtilities.saveChartAsJPEG( XYChart, xylineChart, width, height);

	//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!NEW RUN OF ALGORITHM!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	 pop = new Population(100);  
	 //Evaluator.ceiling = 0.25;
	 //Evaluator.publicMutationRate = 0.9;
	 Evaluator.startDate = "4-Jan-10";
	 Evaluator.endDate = "30-Dec-11";
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
	 XYSeries RunTwo = new XYSeries("2010 - 2011", false);
	 XYSeries ParetoTwo = new XYSeries("2010 - 2011");
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
	}

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
	XYChart = new File( "10% Mutation.jpeg" ); 
	      ChartUtilities.saveChartAsJPEG( XYChart, xylineChart, width, height);
	         
	               
	 //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!NEW RUN OF ALGORITHM!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	   //Evaluator.ceiling = 0.5;
	   //Evaluator.publicMutationRate = 0.75;
	   pop = new Population(100);  
	   Evaluator.startDate = "4-Jan-10";
	   Evaluator.endDate = "31-Dec-12";
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
	   XYSeries RunThree = new XYSeries("2010 - 2012", false);
	   XYSeries ParetoThree = new XYSeries("2010 - 2012");
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
	    XYChart = new File( "25% Mutation.jpeg" ); 
	    ChartUtilities.saveChartAsJPEG( XYChart, xylineChart, width, height);

	 
	 //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!NEW RUN OF ALGORITHM!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	 //Evaluator.ceiling = 0.75;
	 //Evaluator.publicMutationRate = 0.5;
	 pop = new Population(100);  
	 Evaluator.startDate = "4-Jan-10";
	 Evaluator.endDate = "31-Dec-13";
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
	 XYSeries RunFour = new XYSeries("2010 - 2013", false);
	 XYSeries ParetoFour = new XYSeries("2010 - 2013");
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
	  XYChart = new File( "50% Mutation.jpeg" ); 
	  ChartUtilities.saveChartAsJPEG( XYChart, xylineChart, width, height);

	//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!NEW RUN OF ALGORITHM!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	//Evaluator.ceiling = 1.0;
	Evaluator.publicMutationRate = 0.0;
	pop = new Population(100);  
	Evaluator.startDate = "4-Jan-10";
	Evaluator.endDate = "31-Dec-14";
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
	XYSeries RunFive = new XYSeries("2010 - 2014", false);
	XYSeries ParetoFive = new XYSeries("2010 - 2014");
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
	 XYChart = new File( "100% Mutation.jpeg" ); 
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
	
	**/

}
