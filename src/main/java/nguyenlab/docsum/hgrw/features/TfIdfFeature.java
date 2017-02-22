/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nguyenlab.docsum.hgrw.features;

import java.util.HashMap;
import java.util.Map;

import nguyenlab.docsum.hgrw.lexrank.Sentence;
import nguyenlab.docsum.hgrw.lexrank.Similar;
import nguyenlab.docsum.hgrw.utils.MathUtils;

/**
 *
 * @author TRAN
 * @param <W>
 */
public class TfIdfFeature<W extends Similar<W>> extends BaseFeature<W> implements Feature<W> {

    private Map<String, Map<W, Double>> typedIdfMap;

    private Map<Sentence, Map<W, Double>> tfIdfCache;

    public TfIdfFeature() {
    }

    public TfIdfFeature(Map<String, Map<W, Double>> typedIdfMap) {
        this.typedIdfMap = typedIdfMap;
        tfIdfCache = new HashMap<>();
    }

    public void setTypedIdfMap(Map<String, Map<W, Double>> typedIdfMap) {
        this.typedIdfMap = typedIdfMap;
        tfIdfCache = new HashMap<>();
    }

    @Override
    public double getScore(Sentence<W> s1, Sentence<W> s2) {
        return MathUtils.innerProduct(getTfIdf(s1), getTfIdf(s2));
    }

    public Map<W, Double> getTfIdf(Sentence<W> s) {
        Map<W, Double> tfidfMap = tfIdfCache.get(s);
        if (tfidfMap == null) {
            tfidfMap = new HashMap<>();
            tfIdfCache.put(s, tfidfMap);
            Map<W, Double> idfMap = typedIdfMap.get(s.getType());
            //calc tf
            for (W w : s.getWords()) {
                Double tf = tfidfMap.get(w);
                if (tf == null) {
                    tf = 1.0;
                } else {
                    tf++;
                }
                tfidfMap.put(w, tf);
            }
            // normalize tf

            //put idf
            double L2Norm = 0;
            for (W w : tfidfMap.keySet()) {
                Double tf = tfidfMap.get(w);
                Double idf = idfMap.get(w);
                Double tfidf = tf * idf;
                L2Norm += Math.pow(tfidf, 2.0);
                tfidfMap.put(w, tfidf);
            }
            L2Norm = Math.pow(L2Norm, 0.5);

            //norm tfidf vector
            if (L2Norm > 0) {
                for (W w : tfidfMap.keySet()) {
                    Double tfidf = tfidfMap.get(w);
                    tfidf /= L2Norm;
                    tfidfMap.put(w, tfidf);
                }
            }
        }
        return tfidfMap;
    }

}
