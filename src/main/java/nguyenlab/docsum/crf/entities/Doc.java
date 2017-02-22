package nguyenlab.docsum.crf.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import edu.stanford.nlp.util.CoreMap;

public class Doc implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private List<CoreMap> sentences;
    private String text;
    private String title;
    private String tag;
    private String source;
    private List<String> labels;

    public Doc() {
        sentences = new ArrayList<>();
        labels = new ArrayList<>();
    }

    /**
     * @return the sentences
     */
    public List<CoreMap> getSentences() {
        return sentences;
    }

    /**
     * @param sentences the sentences to set
     */
    public void setSentences(List<CoreMap> sentences) {
        this.sentences = sentences;
    }

    /**
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * @param text the text to set
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the tag
     */
    public String getTag() {
        return tag;
    }

    /**
     * @param tag the tag to set
     */
    public void setTag(String tag) {
        this.tag = tag;
    }

    /**
     * @return the source
     */
    public String getSource() {
        return source;
    }

    /**
     * @param source the source to set
     */
    public void setSource(String source) {
        this.source = source;
    }

    /**
     * @return the labels
     */
    public List<String> getLabels() {
        return labels;
    }

    /**
     * @param labels the labels to set
     */
    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

}
