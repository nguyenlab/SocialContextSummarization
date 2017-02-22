/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nguyenlab.docsum.hgrw.features.extractors;

import java.util.List;
import java.util.Map;

import nguyenlab.docsum.hgrw.lexrank.Sentence;
import nguyenlab.docsum.hgrw.lexrank.Similar;
import nguyenlab.docsum.hgrw.features.BaseFeature;
import nguyenlab.docsum.hgrw.features.Feature;

/**
 *
 * @author TRAN
 */
public abstract class SimpleFeatureExtractor<W extends Similar<W>> extends BaseFeatureExtractor<W> {

    @Override
    public Feature<W> extract(Map<String, List<List<W>>> typedDocs) {
        Feature<W> f = new BaseFeature<W>() {
            @Override
            public double getScore(Sentence<W> s1, Sentence<W> s2) {
                return SimpleFeatureExtractor.this.getScore(s1, s2);
            }

            @Override
            public String getName() {
                return SimpleFeatureExtractor.this.getName();
            }

        };
        f.setWeight(this.getWeight());
        return f;
    }

}
