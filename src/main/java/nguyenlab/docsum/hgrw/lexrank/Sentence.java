/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nguyenlab.docsum.hgrw.lexrank;

import nguyenlab.docsum.hgrw.lexrank.Similar;
import java.util.List;
import nguyenlab.docsum.hgrw.features.Features;
import nguyenlab.docsum.hgrw.lexrank.etx.SentenceCrossWeights;

/**
 *
 * @author TRAN
 * @param <W>
 */
public class Sentence<W extends Similar<W>> implements Similar<Sentence<W>> {

    protected final List<W> words;
    protected final SentenceCrossWeights sentenceCrossWeights;
    protected final String type;
    protected final Object ref;
    protected final Features features;

    /**
     *
     * @param ref reference to some object for additional information (can be
     * used as index)
     * @param words
     * @param idf
     * @param terms
     * @param sentenceCrossWeights
     * @param type
     */
    public Sentence(Object ref, List<W> words, Features features, SentenceCrossWeights sentenceCrossWeights, String type) {
        this.ref = ref;
        this.words = words;
        this.features = features;
        this.sentenceCrossWeights = sentenceCrossWeights;
        this.type = type;
//
//        tfIDF = new double[terms.size()];
//        for (int i = 0; i < terms.size(); ++i) {
//            tfIDF[i] = 0;
//        }
//
//        for (W term : words) {
//            for (int i = 0; i < terms.size(); ++i) {
//                tfIDF[i] += term.similarity(terms.get(i)) * idf.get(term);
//            }
//        }
    }

    @Override
    public double similarity(Sentence other) {
        return (sentenceCrossWeights == null ? 1 : sentenceCrossWeights.getWeight(this, other))
                * features.score(this, other);
    }

    /**
     * get reference to some object (passed in constructor) for additional
     * information
     *
     * @return
     */
    public Object getRef() {
        return ref;
    }

    public List<W> getWords() {
        return words;
    }

    public String getType() {
        return type;
    }

}
