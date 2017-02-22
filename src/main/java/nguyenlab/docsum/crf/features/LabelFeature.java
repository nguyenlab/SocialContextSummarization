package nguyenlab.docsum.crf.features;

import nguyenlab.docsum.crf.entities.Doc;

public class LabelFeature implements Feature<Object[], Doc> {

    @Override
    public Object[] extract(Doc doc, Object... args) {
        Object[] ft = doc.getLabels().toArray();
        return ft;
    }

    @Override
    public String name() {
        return "Label";
    }

}
