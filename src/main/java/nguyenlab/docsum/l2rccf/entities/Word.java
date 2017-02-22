package nguyenlab.docsum.l2rccf.entities;

public class Word implements Comparable<Word> {

    String word;
    int frequency;

    public Word(String word, int frequency) {
        this.word = word;
        this.frequency = frequency;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String value) {
        this.word = value;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int value) {
        this.frequency = value;
    }

    public int compareTo(Word other) {
        return frequency > other.getFrequency()
                ? -1 : frequency < other.getFrequency()
                        ? 1 : 0;
    }
}
