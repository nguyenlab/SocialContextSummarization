package nguyenlab.docsum.crf.features.basic;

import java.util.List;

import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.util.CoreMap;
import nguyenlab.docsum.crf.features.Feature;
import nguyenlab.docsum.crf.entities.Doc;
import nguyenlab.docsum.crf.utils.NLPUtils;

public class LengthFeature implements Feature<int[], Doc> {

    public int[] extract(Doc doc, Object... args) {
        List<CoreMap> sents = doc.getSentences();

        int[] ft = new int[sents.size()];

        for (int i = 0; i < sents.size(); i++) {
            CoreMap sent = sents.get(i);
            int len = 0;
            for (CoreMap token : sent.get(TokensAnnotation.class)) {
                String lemma = token.get(LemmaAnnotation.class);
                if (!NLPUtils.stopwords.contains(lemma.toLowerCase())) {
                    len++;
                }
            }
            ft[i] = len;
        }
        return ft;
    }

    @Override
    public String name() {
        return "Length";
    }
}
