package nguyenlab.docsum.crf.features.basic;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.util.CoreMap;
import nguyenlab.docsum.crf.features.Feature;
import nguyenlab.docsum.crf.entities.Doc;

public class Similarity2NeighboringSentencesFeature implements Feature<double[], Doc> {

    public final int N;

    /**
     *
     * @param N : {-3, -2, -1 , 1 , 2, 3}, positive==next, negative==previous
     */
    public Similarity2NeighboringSentencesFeature(int N) {
        this.N = N;
    }

    @Override
    public double[] extract(Doc doc, Object... args) {
        List<CoreMap> sents = doc.getSentences();

        double[] ft = new double[sents.size()];

        for (int i = 0; i < sents.size(); i++) {
            int j = i + this.N;
            if (j < 0 || j >= sents.size()) {
                ft[i] = 0.0;
            } else {
                Set<String> tokens = sents.get(i).get(TokensAnnotation.class).stream()
                        .map(e -> e.get(LemmaAnnotation.class).toLowerCase()).collect(Collectors.toSet());
                Set<String> neighborTokens = sents.get(j).get(TokensAnnotation.class).stream()
                        .map(e -> e.get(LemmaAnnotation.class).toLowerCase()).collect(Collectors.toSet());
                long inter = tokens.stream().filter(e -> neighborTokens.contains(e)).count();
                ft[i] = inter / Math.sqrt(tokens.size() * neighborTokens.size() + 1e-6);
            }
        }
        return ft;
    }

    @Override
    public String name() {
        if (this.N < 0) {
            return "Sim_to_Pre_" + (-this.N);
        } else {
            return "Sim_to_Next_" + this.N;
        }
    }
}
