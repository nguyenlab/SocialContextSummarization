package nguyenlab.docsum.crf.features.basic;

import java.util.List;

import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.util.CoreMap;
import nguyenlab.docsum.crf.features.Feature;
import nguyenlab.docsum.crf.entities.Doc;

public class UpperCaseWordsFeature implements Feature<int[], Doc> {

    @Override
    public int[] extract(Doc doc, Object... args) {
        List<CoreMap> annotatedTexts = doc.getSentences();
        int[] ft = new int[annotatedTexts.size()];

        for (int i = 0; i < annotatedTexts.size(); i++) {
            CoreMap annotation = annotatedTexts.get(i);
            boolean upperCaseFound = false;
            for (CoreMap subsent : annotation.get(SentencesAnnotation.class)) {
                int pos = 0;
                for (CoreMap token : subsent.get(TokensAnnotation.class)) {

                    String word = token.get(TextAnnotation.class);
                    if (pos == 0 && word.length() == 1 && word.charAt(0) == 'A') {

                    } else {
                        upperCaseFound = true; // assume the word is UPPERCASE
                        for (int ic = 0; ic < word.length(); ic++) {
                            if (!Character.isUpperCase(word.charAt(ic))) {
                                upperCaseFound = false;
                                break;
                            }
                        }
                    }
                    if (upperCaseFound) {
                        break;
                    }
                    pos++;
                }
                if (upperCaseFound) {
                    break;
                }
            }
            ft[i] = upperCaseFound ? 1 : 0;
        }
        return ft;
    }

    @Override
    public String name() {
        return "Upper_Case_Words";
    }
}
