package nguyenlab.docsum.hgrw.entities;

public class Word {

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
}
