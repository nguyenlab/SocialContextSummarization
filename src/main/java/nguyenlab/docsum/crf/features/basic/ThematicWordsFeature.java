package nguyenlab.docsum.crf.features.basic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.util.CoreMap;
import nguyenlab.docsum.crf.features.Feature;
import nguyenlab.docsum.crf.entities.Doc;
import nguyenlab.docsum.crf.utils.NLPUtils;

public class ThematicWordsFeature implements Feature<int[], Doc> {

    public final int nThematicWords;

    public ThematicWordsFeature(int nThematicWords) {
        // TODO Auto-generated constructor stub
        this.nThematicWords = nThematicWords;
    }

    public ThematicWordsFeature() {
        this(10);
    }

    @Override
    public int[] extract(Doc doc, Object... args) {
        List<CoreMap> annotatedTexts = doc.getSentences();
        int[] ft = new int[annotatedTexts.size()];

        Map<String, Double> word_freqs = new HashMap<>();

        for (int i = 0; i < annotatedTexts.size(); i++) {
            CoreMap annotation = annotatedTexts.get(i);
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

        word_freqs.keySet().removeAll(NLPUtils.stopwords);

        Set<String> thematicWords = word_freqs.entrySet().stream()
                .sorted((e1, e2) -> -Double.compare(e1.getValue(),
                        e2.getValue())).collect(Collectors.toList()).subList(0,
                Math.min(word_freqs.size(), nThematicWords))
                .stream().map(e -> e.getKey()).collect(Collectors.toSet());

//		Set<String> thematicWords = thematicWordsByStdDist(word_freqs);
        for (int i = 0; i < annotatedTexts.size(); i++) {
            CoreMap annotation = annotatedTexts.get(i);
            List<CoreLabel> tokens = annotation.get(TokensAnnotation.class);
            Integer thematicCount = 0;
            for (CoreLabel token : tokens) {
                String lemma = token.get(LemmaAnnotation.class);
                lemma = lemma.toLowerCase();
                thematicCount += thematicWords.contains(lemma) ? 1 : 0;
            }
            ft[i] = thematicCount;

        }

        return ft;
    }

    static Set<String> thematicWordsByStdDist(Map<String, Double> wordsFreq) {
        double mean = wordsFreq.entrySet().stream().mapToDouble(e -> e.getValue()).sum() / wordsFreq.size();
        double std = Math.sqrt(wordsFreq.entrySet().stream().mapToDouble(e -> Math.pow(e.getValue() - mean, 2)).sum());
        double supr = mean + std;
        Set<String> thematicWords = wordsFreq.entrySet().stream().filter(e -> e.getValue() > supr).map(e -> e.getKey())
                .collect(Collectors.toSet());
        return thematicWords;
    }

    @Override
    public String name() {
        return "Thematic_Words";
    }
}
