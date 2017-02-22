package nguyenlab.docsum.crf.features.complex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ejml.simple.SimpleMatrix;
import org.ejml.simple.SimpleSVD;

import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.util.CoreMap;
import nguyenlab.docsum.crf.features.Feature;
import nguyenlab.docsum.crf.entities.Doc;

public class LSAScoresFeature implements Feature<double[], Doc> {

    @Override
    public double[] extract(Doc doc, Object... args) {
        List<CoreMap> sents = doc.getSentences();
        List<Map<String, Double>> word_freqs = new ArrayList<>(sents.size());
        Map<String, Integer> words = new HashMap<>();
        for (int i = 0; i < sents.size(); i++) {
            CoreMap annotation = sents.get(i);
            List<CoreLabel> tokens = annotation.get(TokensAnnotation.class);
            Map<String, Double> word_freq;
            word_freqs.add(word_freq = new HashMap<>());
            double freq_unit = 1.0 / tokens.size();
            for (CoreLabel token : tokens) {
                String lemma = token.get(LemmaAnnotation.class);
                lemma = lemma.toLowerCase();
                if (!words.containsKey(lemma)) {
                    words.put(lemma, words.size());
                }
                Double count = word_freq.get(lemma);
                if (count == null) {
                    count = 0.0;
                }
                word_freq.put(lemma, count + freq_unit);
            }
        }

        double[] ft = new double[sents.size()];

        int nsents = sents.size();
        int nwords = words.size();
        SimpleMatrix word_sent_matrix = new SimpleMatrix(nwords, nsents);
        for (int s = 0; s < nsents; s++) {

            for (Map.Entry<String, Double> entry : word_freqs.get(s).entrySet()) {
                word_sent_matrix.set(words.get(entry.getKey()), s, entry.getValue());
            }
        }
        final boolean compact = true;
        @SuppressWarnings({"rawtypes"})
        SimpleSVD svd = word_sent_matrix.svd(compact);
        SimpleMatrix V = svd.getV();
        SimpleMatrix W = svd.getW();

        for (int k = 0; k < W.numCols(); k++) {
            double singularValue = W.get(k, k);
            int max_i = 0;
            for (int i = 1; i < V.numRows(); i++) {
                if (V.get(i, k) > V.get(max_i, k)) {
                    max_i = i;
                }
            }
            ft[max_i] = Math.max(singularValue, ft[max_i]);
        }

        return ft;
    }

    @Override
    public String name() {
        return "LSA_Scores";
    }

}
