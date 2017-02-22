package nguyenlab.docsum.crf.features.basic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.util.CoreMap;
import nguyenlab.docsum.crf.features.Feature;
import nguyenlab.docsum.crf.entities.Doc;

public class LogLikelihoodFeature implements Feature<double[], Doc> {

    public double[] extract(Doc doc, Object... args) {
        List<CoreMap> sents = doc.getSentences();

        double[] ft = new double[sents.size()];

        Map<String, Double> word_freqs = new HashMap<>();

        for (int i = 0; i < sents.size(); i++) {
            CoreMap annotation = sents.get(i);
            List<CoreLabel> tokens = annotation.get(TokensAnnotation.class);
            for (CoreLabel token : tokens) {
                String lemma = token.get(LemmaAnnotation.class);
                lemma = lemma.toLowerCase();
                Double count = word_freqs.get(lemma);
                if (count == null) {
                    count = 0.0;
                }
                word_freqs.put(lemma, count + 1);

            }
        }

        double sum = 0f;

        for (Map.Entry<String, Double> entry : word_freqs.entrySet()) {
            sum += entry.getValue();
        }

        for (Map.Entry<String, Double> entry : word_freqs.entrySet()) {
            entry.setValue(Math.log(entry.getValue() / sum));
        }

        for (int i = 0; i < sents.size(); i++) {
            CoreMap annotation = sents.get(i);
            List<CoreLabel> tokens = annotation.get(TokensAnnotation.class);
            Double loglikelyhood = 0.0;
            for (CoreLabel token : tokens) {
                String lemma = token.get(LemmaAnnotation.class);
                lemma = lemma.toLowerCase();
                loglikelyhood += word_freqs.get(lemma);
            }
            ft[i] = loglikelyhood;
        }

        return ft;
    }

    @Override
    public String name() {
        return "Log_Likelihood";
    }
}
