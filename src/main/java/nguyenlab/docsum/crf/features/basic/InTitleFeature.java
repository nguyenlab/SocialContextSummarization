package nguyenlab.docsum.crf.features.basic;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.util.CoreMap;
import nguyenlab.docsum.crf.features.Feature;
import nguyenlab.docsum.crf.entities.Doc;
import nguyenlab.docsum.crf.utils.NLPUtils;

public class InTitleFeature implements Feature<int[], Doc> {

    @Override
    public int[] extract(Doc doc, Object... args) {
        List<CoreMap> annotatedTexts = doc.getSentences();
        int[] ft = new int[annotatedTexts.size()];

        Set<String> wordsInTitle = wordsInTitle(doc);

        for (int i = 0; i < annotatedTexts.size(); i++) {
            CoreMap annotation = annotatedTexts.get(i);
            List<CoreLabel> tokens = annotation.get(TokensAnnotation.class);
            Integer wordsInTitleCount = 0;
            for (CoreLabel token : tokens) {
                String lemma = token.get(LemmaAnnotation.class);
                lemma = lemma.toLowerCase();
                wordsInTitleCount += wordsInTitle.contains(lemma) ? 1 : 0;
            }
            ft[i] = wordsInTitleCount;
        }

        return ft;

    }

    private Set<String> wordsInTitle(Doc doc) {
        Annotation annotation = new Annotation(doc.getTitle());
        NLPUtils.instance.annotate(annotation);
        return annotation.get(TokensAnnotation.class).stream().map((tk) -> tk.get(LemmaAnnotation.class).toLowerCase())
                .collect(Collectors.toSet());
    }

    @Override
    public String name() {
        return "In_Title";
    }
}
