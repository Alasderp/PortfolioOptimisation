

/**
 * @author Alasdair Stuart 1306348
 */
public class Evaluator {
    
    private static Stock barc;
    private static Stock rr;
    private static Stock cine;
    private static Stock aal;
    private static Stock blt;
    private static Stock rdsa;
    private static Stock rrs;
    
    private static Stock igg;
    private static Stock sgc;
    private static Stock tate;
    private static Stock grg;
    private static Stock gnk;
    private static Stock bby;
    private static Stock adn;
    private static Stock rbs;
    
	
	private static Stock amzn;
	private static Stock axp;
	private static Stock csco;
	private static Stock fdx;
	private static Stock jnj;
	private static Stock lmt;
	private static Stock wfc;
	private static Stock wmt;
	private static Stock xom;
	private static Stock yhoo;
    
    
    public static int totalSolutions = 0;
    public static int numberIndvls;
    public static int evolutionCycle;
    public static double publicMutationRate;
    public static double floor;
    public static double ceiling;
    public static String startDate;
    public static String endDate;
    
    /**
     * Initialise the evaluator by creating the stock objects and parsing the .csv data in
     */
    public static void initialise(){
        
    
    	
        barc = new Stock("barc", 0);       
        barc.loadData();
        
        rr = new Stock("rr", 0);
        rr.loadData();
        
        cine = new Stock("cine", 0);
        cine.loadData();
        
        aal = new Stock("aal", 0);
        aal.loadData();
        
        blt = new Stock("blt", 0);
        blt.loadData();
        
        rdsa = new Stock("rdsa", 0);
        rdsa.loadData();
        
        rrs = new Stock("rrs", 0);
        rrs.loadData();
        
        igg = new Stock("igg", 0);
        igg.loadData();
        
        sgc = new Stock("sgc", 0);
        sgc.loadData();
        
        tate = new Stock("tate", 0);
        tate.loadData();
        
        grg = new Stock("grg", 0);
        grg.loadData();
        
        gnk = new Stock("gnk", 0);
        gnk.loadData();
        
        bby = new Stock("bby", 0);
        bby.loadData();
        
        adn = new Stock("adn", 0);
        adn.loadData();
        
        rbs = new Stock("rbs", 0);
        rbs.loadData();
        
    /**
    	
	amzn = new Stock("amzn", 0);       
	amzn.loadData();
	
	axp = new Stock("axp", 0);       
	axp.loadData();
	   
	csco = new Stock("csco", 0);       
	csco.loadData();
	  
	 fdx = new Stock("fdx", 0);       
	 fdx.loadData();
	 
	jnj = new Stock("jnj", 0);       
	jnj.loadData();
	
	lmt = new Stock("lmt", 0);       
	lmt.loadData();
	   
	wfc = new Stock("wfc", 0);       
	wfc.loadData();
	  
	wmt = new Stock("wmt", 0);       
	wmt.loadData();
	 
	xom = new Stock("xom", 0);       
	xom.loadData();
	
	yhoo= new Stock("yhoo", 0);       
	yhoo.loadData();
 
     **/
        
    }
    
    //USED FOR THE SINGLE OBJECTIVE RANDOM SEARCH ALGORITHM
    public static double evaluateFitness(StockWeightingSolution solution){
        
        Portfolio portfolio = new Portfolio();
        
        
        barc.setWeighting((double)solution.alleles.get(0));
        rr.setWeighting((double)solution.alleles.get(1));
        cine.setWeighting((double)solution.alleles.get(2));
        aal.setWeighting((double)solution.alleles.get(3));
        blt.setWeighting((double)solution.alleles.get(4));
        rdsa.setWeighting((double)solution.alleles.get(5));
        rrs.setWeighting((double)solution.alleles.get(6));
        
        igg.setWeighting((double)solution.alleles.get(7));
        sgc.setWeighting((double)solution.alleles.get(8));
        tate.setWeighting((double)solution.alleles.get(9));
        grg.setWeighting((double)solution.alleles.get(10));
        gnk.setWeighting((double)solution.alleles.get(11));
        bby.setWeighting((double)solution.alleles.get(12));
        adn.setWeighting((double)solution.alleles.get(13));
        rbs.setWeighting((double)solution.alleles.get(14));
        
        portfolio.addStock(barc);
        portfolio.addStock(rr);
        portfolio.addStock(cine);
        portfolio.addStock(aal);
        portfolio.addStock(blt);
        portfolio.addStock(rdsa);
        portfolio.addStock(rrs);
        
        portfolio.addStock(igg);
        portfolio.addStock(sgc);
        portfolio.addStock(tate);
        portfolio.addStock(grg);
        portfolio.addStock(gnk);
        portfolio.addStock(bby);
        portfolio.addStock(adn);
        portfolio.addStock(rbs);
        
 		/**
        
        amzn.setWeighting((double)solution.alleles.get(0));
        axp.setWeighting((double)solution.alleles.get(1));
        csco.setWeighting((double)solution.alleles.get(2));
        fdx.setWeighting((double)solution.alleles.get(3));
        jnj.setWeighting((double)solution.alleles.get(4));
        lmt.setWeighting((double)solution.alleles.get(5));
        wfc.setWeighting((double)solution.alleles.get(6));
        wmt.setWeighting((double)solution.alleles.get(7));
        xom.setWeighting((double)solution.alleles.get(8));
        yhoo.setWeighting((double)solution.alleles.get(9));
        
        portfolio.addStock(amzn);
        portfolio.addStock(axp);
        portfolio.addStock(csco);
        portfolio.addStock(fdx);
        portfolio.addStock(jnj);
        portfolio.addStock(lmt);
        portfolio.addStock(wfc);
        portfolio.addStock(wmt);
        portfolio.addStock(xom);
        portfolio.addStock(yhoo);
        **/
        
        
        
        return solution.solutionReturn / solution.solutionRisk;
        
        //solution.fitness = portfolio.getAverageReturn("4-Jan-10", "8-Nov-16", 21) / portfolio.portfolioVolatility("4-Jan-10", "8-Nov-16", 21);
        
    }
    
    /**
     * @param solution The solution being evaluated
     * @return The return of the equivalent portfolio generated from the solution
     * Convert a StockWeightingSolution object into a Portfolio object.
     * Use the methods available in the Portfolio class and the data in the Evaluator class to find the return
     */
    public static double theReturn(StockWeightingSolution solution){
        
        Portfolio portfolio = new Portfolio();
        
       
        
        barc.setWeighting((double)solution.alleles.get(0));
        rr.setWeighting((double)solution.alleles.get(1));
        cine.setWeighting((double)solution.alleles.get(2));
        aal.setWeighting((double)solution.alleles.get(3));
        blt.setWeighting((double)solution.alleles.get(4));
        rdsa.setWeighting((double)solution.alleles.get(5));
        rrs.setWeighting((double)solution.alleles.get(6));
        
        igg.setWeighting((double)solution.alleles.get(7));
        sgc.setWeighting((double)solution.alleles.get(8));
        tate.setWeighting((double)solution.alleles.get(9));
        grg.setWeighting((double)solution.alleles.get(10));
        gnk.setWeighting((double)solution.alleles.get(11));
        bby.setWeighting((double)solution.alleles.get(12));
        adn.setWeighting((double)solution.alleles.get(13));
        rbs.setWeighting((double)solution.alleles.get(14));
        
        /**
        
        amzn.setWeighting((double)solution.alleles.get(0));
        axp.setWeighting((double)solution.alleles.get(1));
        csco.setWeighting((double)solution.alleles.get(2));
        fdx.setWeighting((double)solution.alleles.get(3));
        jnj.setWeighting((double)solution.alleles.get(4));
        lmt.setWeighting((double)solution.alleles.get(5));
        wfc.setWeighting((double)solution.alleles.get(6));
        wmt.setWeighting((double)solution.alleles.get(7));
        xom.setWeighting((double)solution.alleles.get(8));
        yhoo.setWeighting((double)solution.alleles.get(9));
        
        **/
        
        
        portfolio.addStock(barc);
        portfolio.addStock(rr);
        portfolio.addStock(cine);
        portfolio.addStock(aal);
        portfolio.addStock(blt);
        portfolio.addStock(rdsa);
        portfolio.addStock(rrs);
        
        portfolio.addStock(igg);
        portfolio.addStock(sgc);
        portfolio.addStock(tate);
        portfolio.addStock(grg);
        portfolio.addStock(gnk);
        portfolio.addStock(bby);
        portfolio.addStock(adn);
        portfolio.addStock(rbs);
        
        /**
        
        portfolio.addStock(amzn);
        portfolio.addStock(axp);
        portfolio.addStock(csco);
        portfolio.addStock(fdx);
        portfolio.addStock(jnj);
        portfolio.addStock(lmt);
        portfolio.addStock(wfc);
        portfolio.addStock(wmt);
        portfolio.addStock(xom);
        portfolio.addStock(yhoo);
        
        **/
        
        //return portfolio.getReturn("4-Jan-10", "8-Nov-16");
        return portfolio.getReturn(startDate, endDate);
        
    }
    
    /**
     * @param solution The solution being evaluated
     * @return The volatility of the portfolio generated from the solution
     * Convert a StockWeightingSolution object into a Portfolio object.
     * Use the methods available in the Portfolio class and the data in the Evaluator class to find the risk
     */
    public static double theVolatility(StockWeightingSolution solution){
        
        Portfolio portfolio = new Portfolio();
        
        
        barc.setWeighting((double)solution.alleles.get(0));
        rr.setWeighting((double)solution.alleles.get(1));
        cine.setWeighting((double)solution.alleles.get(2));
        aal.setWeighting((double)solution.alleles.get(3));
        blt.setWeighting((double)solution.alleles.get(4));
        rdsa.setWeighting((double)solution.alleles.get(5));
        rrs.setWeighting((double)solution.alleles.get(6));
        
        igg.setWeighting((double)solution.alleles.get(7));
        sgc.setWeighting((double)solution.alleles.get(8));
        tate.setWeighting((double)solution.alleles.get(9));
        grg.setWeighting((double)solution.alleles.get(10));
        gnk.setWeighting((double)solution.alleles.get(11));
        bby.setWeighting((double)solution.alleles.get(12));
        adn.setWeighting((double)solution.alleles.get(13));
        rbs.setWeighting((double)solution.alleles.get(14));
        
        /**
       
        amzn.setWeighting((double)solution.alleles.get(0));
        axp.setWeighting((double)solution.alleles.get(1));
        csco.setWeighting((double)solution.alleles.get(2));
        fdx.setWeighting((double)solution.alleles.get(3));
        jnj.setWeighting((double)solution.alleles.get(4));
        lmt.setWeighting((double)solution.alleles.get(5));
        wfc.setWeighting((double)solution.alleles.get(6));
        wmt.setWeighting((double)solution.alleles.get(7));
        xom.setWeighting((double)solution.alleles.get(8));
        yhoo.setWeighting((double)solution.alleles.get(9));
        
        **/
        
        portfolio.addStock(barc);
        portfolio.addStock(rr);
        portfolio.addStock(cine);
        portfolio.addStock(aal);
        portfolio.addStock(blt);
        portfolio.addStock(rdsa);
        portfolio.addStock(rrs);
        
        portfolio.addStock(igg);
        portfolio.addStock(sgc);
        portfolio.addStock(tate);
        portfolio.addStock(grg);
        portfolio.addStock(gnk);
        portfolio.addStock(bby);
        portfolio.addStock(adn);
        portfolio.addStock(rbs);
        
        /**
        
        portfolio.addStock(amzn);
        portfolio.addStock(axp);
        portfolio.addStock(csco);
        portfolio.addStock(fdx);
        portfolio.addStock(jnj);
        portfolio.addStock(lmt);
        portfolio.addStock(wfc);
        portfolio.addStock(wmt);
        portfolio.addStock(xom);
        portfolio.addStock(yhoo);
        
        **/
        
        //return portfolio.portfolioVolatility("4-Jan-10", "8-Nov-16", 21);
        return portfolio.portfolioVolatility(startDate, endDate, 21);
        
    }
    
}
