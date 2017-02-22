package nguyenlab.docsum.l2rccf.entities;

public class Sentence implements Comparable<Sentence> {

    String sentence;
    double rte_score;

    public Sentence(String sentence, double rte_score) {
        this.sentence = sentence;
        this.rte_score = rte_score;
    }

    public String getSentence() {
        return sentence;
    }

    public void setSentence(String s) {
        this.sentence = s;
    }

    public double getScore() {
        return rte_score;
    }

    public void setScore(double value) {
        this.rte_score = value;
    }

    public int compareTo(Sentence other) {
        return rte_score > other.getScore()
                ? -1 : rte_score < other.getScore()
                        ? 1 : 0;
    }
}
