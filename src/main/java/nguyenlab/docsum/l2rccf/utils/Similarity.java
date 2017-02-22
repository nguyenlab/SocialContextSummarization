package nguyenlab.docsum.l2rccf.utils;

import java.util.ArrayList;
import java.util.List;

public class Similarity {

    String regex = ":";
  

    public double cosinSimilarity(String[] docvec, String[] tweetvec) {
        double distance = 0;
        int length = docvec.length;
        double ts = 0, ms = 0, tmp1 = 0, tmp2 = 0;
        for (int i = 0; i < length; i++) {
            ts += Double.parseDouble(docvec[i]) * Double.parseDouble(tweetvec[i]);
            tmp1 = tmp1 + Double.parseDouble(docvec[i]) * Double.parseDouble(docvec[i]);
            tmp2 = tmp2 + Double.parseDouble(tweetvec[i]) * Double.parseDouble(tweetvec[i]);
        }
        ms = Math.sqrt(tmp1) * Math.sqrt(tmp2);
        distance = ts / ms;
        return distance;
    }

    //Calculating the Cosin distance between two events
    public double cosinSimilarity(String event1, String event2) {
        double distance = 0;
        List<String> list = mergeEvent(event1, event2);
        int[] vector1 = frequency2Vector(list, event1);
        //for(int i=0; i<vector1.length; i++) //System.out.print(vector1[i]+" ");
        //System.out.println();
        int[] vector2 = frequency2Vector(list, event2);
        //for(int i=0; i<vector2.length; i++) //System.out.print(vector2[i]+" ");		
        //System.out.println();

        double ts = 0, ms = 0, tmp1 = 0, tmp2 = 0;

        for (int i = 0; i < vector1.length; i++) {
            ts = ts + vector1[i] * vector2[i];
            tmp1 = tmp1 + vector1[i] * vector1[i];
            tmp2 = tmp2 + vector2[i] * vector2[i];
        }
        //System.out.println("TS:="+ts);
        ms = Math.sqrt(tmp1) * Math.sqrt(tmp2);
        //System.out.println("MS:="+ms);
        distance = ts / ms;
        return distance;
    }

    //Calculating the Simpson distance between two events
    public double simpDistance(String[] s, String[] t) {
        //event1=event1.toLowerCase(); event2=event2.toLowerCase();
        double sim = 0;
        double count = 0;
        for (String s1 : s) {
            for (String s2 : t) {
                if (s1.equals(s2)) {
                    count++;
                }
            }
        }
        //System.out.println("count:="+count);
        double len = 0;
        if (s.length > t.length) {
            len = t.length;
        } else {
            len = s.length;
        }
        //System.out.println("length:="+len);
        sim = 1 - (count / len);
        return sim;
    }

    private List<String> mergeEvent(String event1, String event2) {
        //event1=event1.toLowerCase(); event2=event2.toLowerCase();
        String[] arr1 = event1.split(" ");
        String[] arr2 = event2.split(" ");
        List<String> list = new ArrayList<>();
        for (String s : arr1) {
            boolean kt = true;
            for (String w : list) {
                if (s.equals(w)) {
                    kt = false;
                }
            }
            if (kt) {
                list.add(s);
            }
        }

        for (String s1 : arr2) {
            boolean kt = true;
            for (String s2 : list) {
                if (s1.equals(s2)) {
                    kt = false;
                }
            }
            if (kt) {
                list.add(s1);
            }
        }
        return list;
    }

    private int[] initVector(int size) {
        int[] vector = new int[size];
        for (int i = 0; i < size; i++) {
            vector[i] = 0;
        }
        return vector;
    }

    private int[] frequency2Vector(List<String> list, String event) {
        int size = list.size();
        int[] vector = initVector(size);
        String[] arr = event.split(" ");
        for (int i = 0; i < list.size(); i++) {
            int count = 0;
            String s = list.get(i);
            for (String w : arr) {
                if (s.equals(w)) {
                    count++;
                }
            }
            vector[i] = count;
        }
        return vector;
    }
}
