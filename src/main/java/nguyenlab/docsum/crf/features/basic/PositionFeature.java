package nguyenlab.docsum.crf.features.basic;

import java.util.List;

import edu.stanford.nlp.util.CoreMap;
import nguyenlab.docsum.crf.features.Feature;
import nguyenlab.docsum.crf.entities.Doc;

public class PositionFeature implements Feature<int[], Doc> {

    public int[] extract(Doc doc, Object... args) {
        List<CoreMap> annotatedTexts = doc.getSentences();
        int[] ft = new int[annotatedTexts.size()];

        ft[0] = 1;
        ft[ft.length - 1] = 2;
        for (int i = ft.length - 2; i > 0; i--) {
            ft[i] = 3;
        }

        return ft;
    }

    @Override
    public String name() {
        return "Position";
    }
}
