package nguyenlab.docsum.crf.features;

public interface Feature<R, I> {

    R extract(I input, Object... args);

    String name();
}
