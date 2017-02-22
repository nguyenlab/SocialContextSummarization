/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nguyenlab.docsum.hgrw.features.extractors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nguyenlab.docsum.hgrw.lexrank.Sentence;
import nguyenlab.docsum.hgrw.lexrank.Similar;
import nguyenlab.docsum.hgrw.lexrank.Summarizer;
import nguyenlab.docsum.hgrw.features.Feature;
import nguyenlab.docsum.hgrw.features.TfIdfFeature;

/**
 *
 * @author TRAN
 */
public class TfIfdExtractor<W extends Similar<W>> extends BaseFeatureExtractor<W> {

    @Override
    public Feature< W> extract(Map<String, List<List<W>>> typedDocs) {
        Map<String, Map<W, Double>> typedTfIdfMap = new HashMap<>();
        List<List<W>> allSentences = new ArrayList<>();
        for (String type : typedDocs.keySet()) {

            allSentences.addAll(typedDocs.get(type));
        }
        Map<W, Double> idf = Summarizer.idf(allSentences);

        for (String type : typedDocs.keySet()) {
            typedTfIdfMap.put(type, idf);
        }

        Feature<W> f = new TfIdfFeature<>(typedTfIdfMap);
        f.setWeight(this.getWeight());
        return f;
    }

    @Override
    public double getScore(Sentence<W> s1, Sentence<W> s2) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
