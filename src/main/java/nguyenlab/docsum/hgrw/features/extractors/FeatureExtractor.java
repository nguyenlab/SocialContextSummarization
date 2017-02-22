/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nguyenlab.docsum.hgrw.features.extractors;

import java.util.List;
import java.util.Map;

import nguyenlab.docsum.hgrw.lexrank.Similar;
import nguyenlab.docsum.hgrw.features.Feature;

/**
 *
 * @author TRAN
 * @param <W>
 */
public interface FeatureExtractor<W extends Similar<W>> extends Feature<W> {

    Feature<W> extract(Map<String, List<List<W>>> typedDocs);

    FeatureExtractor<W> withWeight(double weight); //for builder
}
