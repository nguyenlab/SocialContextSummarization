/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nguyenlab.docsum.hgrw.lexrank;

import nguyenlab.docsum.hgrw.lexrank.Similar;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author TRAN
 */
public class Word implements Similar<Word>, Comparable<Word>, Serializable {

    private static final long serialVersionUID = 1L;
    private String text;
    private int startPos;
    private int endPos;

    public String getText() {
        return text;
    }

    public Word(String text) {
        this.text = text;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof Word) {
            return this.text.equals(((Word) obj).text);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.text);
    }

    @Override
    public String toString() {
        return text;
    }

    @Override
    public double similarity(Word other) {
        return Objects.equals(this, other) ? 1 : 0;
    }

    @Override
    public int compareTo(Word o) {
        if (o == null) {
            return 1;
        }
        return this.text.compareTo(o.text);
    }

}
