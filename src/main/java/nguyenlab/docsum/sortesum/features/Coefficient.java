package nguyenlab.docsum.sortesum.features;

import java.util.HashSet;
import java.util.Set;

public class Coefficient {

    private Set<String> getWords(String[] sentences) {
        Set<String> setWords = new HashSet<String>();
        for (String sen : sentences) {
            String[] words = sen.split(":");
            for (String w : words) {
                setWords.add(w);
            }
        }
        return setWords;
    }

    public double Matching_coefficient(String[] t, String[] h) {
        double count = 0;
        Set<String> X = new HashSet<String>();
        Set<String> Y = new HashSet<String>();
        X = getWords(t);
        Y = getWords(h);

        for (String x : X) {
            for (String y : Y) {
                if (x.equals(y)) {
                    count++;
                    break;
                }
            }
        }
        return count;

    }

    public double Dice_coefficient(String[] t, String[] h) {

        Set<String> X = new HashSet<String>();
        Set<String> Y = new HashSet<String>();
        X = getWords(t);
        Y = getWords(h);
        double tmp = 2 * Matching_coefficient(t, h);
        double tmp2 = X.size() + Y.size();
        return tmp / tmp2;

    }

    public double Overlap_coefficient(String[] t, String[] h) {
        double count = Matching_coefficient(t, h);
        Set<String> X = new HashSet<String>();
        Set<String> Y = new HashSet<String>();
        X = getWords(t);
        Y = getWords(h);
        return count / Math.min(X.size(), Y.size());
    }

    public double Jaccard_coefficient(String[] t, String[] h) {

        Set<String> setWords = new HashSet<String>();
        for (int i = 0; i < t.length; i++) {
            setWords.add(t[i]);
        }
        for (int i = 0; i < h.length; i++) {
            setWords.add(h[i]);
        }
        double tmp = Matching_coefficient(t, h);
        return tmp / setWords.size();

    }

    public double inclu_exclu_coefficient(String[] t, String[] h) {
        Set<String> X = new HashSet<String>();
        Set<String> Y = new HashSet<String>();
        X = getWords(t);
        Y = getWords(h);

        Set<String> setWords = new HashSet<String>();
        for (int i = 0; i < t.length; i++) {
            setWords.add(t[i]);
        }
        for (int i = 0; i < h.length; i++) {
            setWords.add(h[i]);
        }
        double tmp = X.size() + Y.size();
        return (double) (setWords.size()) / tmp;
    }
}
