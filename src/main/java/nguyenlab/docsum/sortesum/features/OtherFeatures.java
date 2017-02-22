package nguyenlab.docsum.sortesum.features;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class OtherFeatures {

    // Search different words in two sentences
    private Set<String> distinctWord(String[] t, String[] h) {
        Set<String> setWords = new HashSet<String>();
        for (int i = 0; i < t.length; i++) {
            setWords.add(t[i]);
        }
        for (int i = 0; i < h.length; i++) {
            setWords.add(h[i]);
        }

        return setWords;
    }

    // Create an vector with elements are 
    private ArrayList<Integer> vector(Set<String> word, String[] s) {

        ArrayList<Integer> v = new ArrayList<Integer>();
        for (String w : word) {
            int countw = 0;
            for (int i = 0; i < s.length; i++) {
                if (s[i].equals(w)) {
                    countw++;
                }
            }
            v.add(countw);
        }
        return v;
    }

    // Manhatan Distance
    public double manhatan(String[] t, String[] h) {
        Set<String> setWords = distinctWord(t, h);
        ArrayList<Integer> x = vector(setWords, t);
        ArrayList<Integer> y = vector(setWords, h);
        double ManhatanDistance = 0;
        for (int i = 0; i < x.size(); i++) {

            ManhatanDistance = ManhatanDistance + Math.abs(x.get(i) - y.get(i));
        }

        return ManhatanDistance;
    }

    // Euclidean Distance
    public double euclidean(String[] t, String[] h) {
        Set<String> setWords = distinctWord(t, h);
        ArrayList<Integer> x = vector(setWords, t);
        ArrayList<Integer> y = vector(setWords, h);
        double Euclidean = 0;
        for (int i = 0; i < x.size(); i++) {

            Euclidean = Euclidean + (x.get(i) - y.get(i))
                    * (x.get(i) - y.get(i));
        }

        return Math.sqrt(Euclidean);
    }

    // Cosine score
    public double cosin(String[] t, String[] h) {
        Set<String> setWords = distinctWord(t, h);
        ArrayList<Integer> x = vector(setWords, t);
        ArrayList<Integer> y = vector(setWords, h);
        double Cosin = 0;
        double valueX = 0;
        double valueY = 0;
        double value = 0;

        for (int i = 0; i < x.size(); i++) {

            value = value + x.get(i) * y.get(i);
            valueX = valueX + x.get(i) * x.get(i);
            valueY = valueY + y.get(i) * y.get(i);
        }
        Cosin = value / Math.sqrt(valueY * valueX);
        return Cosin;
    }
}
