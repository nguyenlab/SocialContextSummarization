/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nguyenlab.docsum.hgrw.lexrank.etx;

import nguyenlab.docsum.hgrw.lexrank.Sentence;

/**
 *
 * @author TRAN
 */
public interface SentenceCrossWeights {

    public double getWeight(Sentence unit1, Sentence unit2);
}
