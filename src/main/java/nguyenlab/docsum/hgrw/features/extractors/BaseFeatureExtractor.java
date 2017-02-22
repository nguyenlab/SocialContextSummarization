/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nguyenlab.docsum.hgrw.features.extractors;

import nguyenlab.docsum.hgrw.lexrank.Similar;
import nguyenlab.docsum.hgrw.features.Feature;

/**
 *
 * @author TRAN
 */
public abstract class BaseFeatureExtractor<W extends Similar<W>> implements FeatureExtractor<W>, Feature<W> {

    private double weight = 1.0;

    @Override
    public double getWeight() {
        return weight;
    }

    @Override
    public void setWeight(double weight) {
        this.weight = weight;
    }

    @Override
    public FeatureExtractor<W> withWeight(double weight) {
        setWeight(weight);
        return this;
    }

    @Override
    public String getName() {
        return getClass().getName();
    }
}
