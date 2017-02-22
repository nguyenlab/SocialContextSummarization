/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nguyenlab.docsum.hgrw.features;

import nguyenlab.docsum.hgrw.lexrank.Similar;

/**
 *
 * @author TRAN
 * @param <W>
 */
public abstract class BaseFeature<W extends Similar<W>> implements Feature<W> {

    protected double weight = 1;

    @Override
    public double getWeight() {
        return weight;
    }

    @Override
    public void setWeight(double weight) {
        this.weight = weight;
    }

    @Override
    public String getName() {
        return getClass().getName();
    }

}
