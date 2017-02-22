/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nguyenlab.docsum.hgrw.features.extractors;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import nguyenlab.docsum.hgrw.lexrank.Similar;
import nguyenlab.docsum.hgrw.features.Features;

/**
 *
 * @author TRAN
 * @param <W>
 */
public class FeatureExtractors<W extends Similar<W>> extends ArrayList<FeatureExtractor<W>> {

    public FeatureExtractors(int initialCapacity) {
        super(initialCapacity);
    }

    public FeatureExtractors() {
    }

    public FeatureExtractors(Collection<? extends FeatureExtractor<W>> c) {
        super(c);
    }

    public Features extract(Map<String, List<List<W>>> typedDocs) {
        Features features = new Features();
        for (FeatureExtractor<W> fe : this) {
            features.add(fe.extract(typedDocs));
        }
        return features;
    }
}
