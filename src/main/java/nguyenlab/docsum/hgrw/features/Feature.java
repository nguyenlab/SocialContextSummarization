/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nguyenlab.docsum.hgrw.features;

import nguyenlab.docsum.hgrw.lexrank.Sentence;
import nguyenlab.docsum.hgrw.lexrank.Similar;

/**
 *
 * @author TRAN
 * @param <W>
 */
public interface Feature<W extends Similar<W>> {

    double getScore(Sentence<W> s1, Sentence<W> s2);

    double getWeight();

    void setWeight(double weight);

    String getName();
}
