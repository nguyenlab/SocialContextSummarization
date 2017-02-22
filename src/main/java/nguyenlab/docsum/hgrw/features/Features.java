/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nguyenlab.docsum.hgrw.features;

import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import nguyenlab.docsum.hgrw.lexrank.Sentence;
import nguyenlab.docsum.hgrw.lexrank.Similar;

/**
 *
 * @author TRAN
 * @param <W>
 */
public class Features<W extends Similar<W>> extends ArrayList<Feature<W>> {

    /**
     * if weight absolute value smaller than margin then treated as 0
     */
    public static final double WEIGHT_MARGIN = 1e-9;

    public Features(int initialCapacity) {
        super(initialCapacity);
    }

    public Features() {
    }

    public Features(Collection<? extends Feature<W>> c) {
        super(c);
    }

    public double score(Sentence<W> s1, Sentence<W> s2) {
        double score = 0;
        StringBuilder sb = new StringBuilder();
        for (Feature feature : this) {
            if (Math.abs(feature.getWeight()) > WEIGHT_MARGIN) {
                double fs = feature.getScore(s1, s2) * feature.getWeight();
                score += fs;
                sb.append(feature.getName()).append(": ").append(fs).append("\n");
            }
        }
        Logger.getLogger(Features.class.getName()).log(Level.INFO, sb.toString());

        return score;
    }

}
