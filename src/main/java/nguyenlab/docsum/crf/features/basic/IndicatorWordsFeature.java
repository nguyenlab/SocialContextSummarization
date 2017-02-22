package nguyenlab.docsum.crf.features.basic;

import java.util.List;
import java.util.Set;

import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.util.CoreMap;
import nguyenlab.docsum.crf.features.Feature;
import nguyenlab.docsum.crf.entities.Doc;
import nguyenlab.docsum.crf.utils.NLPUtils;

public class IndicatorWordsFeature implements Feature<int[], Doc> {

    private Set<String> indicatorWords;

    public IndicatorWordsFeature() {
        this.indicatorWords = NLPUtils.indicatorWords;
    }

    public IndicatorWordsFeature(Set<String> indicatorWords) {
        this.indicatorWords = indicatorWords;
    }

    public Set<String> getIndicatorWords() {
        return indicatorWords;
    }

    public void setIndicatorWords(Set<String> indicatorWords) {
        this.indicatorWords = indicatorWords;
    }

    public int[] extract(Doc doc, Object... args) {
        List<CoreMap> sents = doc.getSentences();
        int[] ft = new int[sents.size()];

        for (int i = 0; i < sents.size(); i++) {
            CoreMap sent = sents.get(i);
            String txt = sent.get(TextAnnotation.class).toLowerCase();
            for (String indicator : indicatorWords) {
                if (txt.contains(indicator)) {
                    ft[i] = 1;
                    break;
                }
            }
        }
        return ft;
    }

    @Override
    public String name() {
        return "Indicator_Words";
    }
}
