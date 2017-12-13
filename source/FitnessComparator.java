/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.Comparator;

/**
 * @author Alasdairp
 */
public class FitnessComparator implements Comparator<StockWeightingSolution>{
    
@Override
public int compare(StockWeightingSolution S1, StockWeightingSolution S2) {
        return ((int)(S1.fitness - S2.fitness));
}
    
}
