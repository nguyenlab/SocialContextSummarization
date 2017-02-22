/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nguyenlab.docsum.hgrw.lexrank.etx;

import nguyenlab.docsum.hgrw.lexrank.etx.SentenceCrossWeights;
import nguyenlab.docsum.hgrw.lexrank.Sentence;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author TRAN
 */
public class TypedSentenceCrossWeights implements SentenceCrossWeights {

    private HashMap<String, Integer> index;
    private double[][] weights;

    private TypedSentenceCrossWeights(HashMap<String, Integer> index, double[][] weights) {

        if (index.size() != weights.length) {
            throw new IllegalArgumentException("size mismatched!");
        }

        for (double[] row : weights) {
            if (index.size() != row.length) {
                throw new IllegalArgumentException("size mismatched!");
            }
        }

        this.index = index;
        this.weights = weights;
    }

    public TypedSentenceCrossWeights(List<String> types, double[][] weights) {
        this(listToMap(types), weights);
    }

    private static <T> HashMap<T, Integer> listToMap(List<T> list) {
        HashMap<T, Integer> map = new HashMap<>();
        for (int i = 0; i < list.size(); i++) {
            map.put(list.get(i), i);
        }
        return map;
    }

    @Override
    public double getWeight(Sentence unit1, Sentence unit2) {
        Integer i1 = index.get(unit1.getType());
        Integer i2 = index.get(unit2.getType());
        if (i1 == null || i2 == null) {
            return 0;
        }

        return weights[i1][i2];
    }

}
