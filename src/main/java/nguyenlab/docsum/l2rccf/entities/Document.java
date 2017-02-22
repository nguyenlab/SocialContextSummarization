package nguyenlab.docsum.l2rccf.entities;

import java.util.List;

public class Document {

    String title;
    List<String> summary;
    List<String> sentence;
    List<String> comment;

    public Document(String title, List<String> summary, List<String> sentence, List<String> comment) {
        this.title = title;
        this.summary = summary;
        this.sentence = sentence;
        this.comment = comment;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String value) {
        this.title = value;
    }

    public List<String> getSummary() {
        return summary;
    }

    public void setSummary(List<String> value) {
        this.summary = value;
    }

    public List<String> getSentence() {
        return sentence;
    }

    public void setSentence(List<String> value) {
        this.sentence = value;
    }

    public List<String> getComment() {
        return comment;
    }

    public void setComment(List<String> value) {
        this.comment = value;
    }
}
