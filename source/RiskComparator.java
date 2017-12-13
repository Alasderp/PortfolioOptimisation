/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.Comparator;

/**
 *
 * @author Alasdairp
 */
public class RiskComparator implements Comparator<StockWeightingSolution>{
    
    @Override
    public int compare(StockWeightingSolution S1, StockWeightingSolution S2) {
        if(S1.solutionRisk > S2.solutionRisk){
            return -1;
        }
        else if(S1.solutionRisk < S2.solutionRisk){
            return 1;
        }
	    else{
	    	return 0;
	    }
    }
    
}
