package nguyenlab.docsum.l2rccf.features;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nguyenlab.docsum.sosvmrank.ner.NER;
import nguyenlab.docsum.l2rccf.utils.PreProcessing;
import nguyenlab.docsum.l2rccf.utils.Similarity;
import nguyenlab.docsum.l2rccf.utils.WordUtil;

public class SentenceFeature {

    String space = "\\s+";
    WordUtil wordUtil = null;
    PreProcessing p = null;
    NER ner = null;
    Similarity sim = null;

    public SentenceFeature() throws Exception {
        wordUtil = new WordUtil();
        p = new PreProcessing();
        ner = new NER();
        sim = new Similarity();
    }

    public int isFirst(String s, List<String> sents) {
        int index = sents.indexOf(s);
        if (index == 0) {
            return 1;
        } else {
            return 0;
        }
    }

    public float sentPos(String s, List<String> sents) {
        int pos = 0;
        for (int i = 0; i < sents.size(); i++) {
            if (sents.get(i).toLowerCase().equals(s.toLowerCase())) {
                pos = i;
                break;
            }
        }
        return (float) pos / sents.size();
    }

    public float titleSim(String s, String title) {
        Map<String, Integer> words = getWord(title);
        String tmp = s.toLowerCase();
        String[] arrT = title.toLowerCase().split(space);
        String[] arrS = tmp.toLowerCase().split(space);
        int sizeT = arrT.length;
        int sizeS = arrS.length;
        float pt = 0;
        for (String w : arrT) {
            if (tmp.contains(w)) {
                int tf = words.get(w);
                pt += (float) tf / sizeT;
            }
        }
        return (float) pt / sizeS;
    }

    private Map<String, Integer> getWord(String title) {
        Map<String, Integer> words = new HashMap<>();
        String[] arr = title.toLowerCase().split(space);
        for (String w1 : arr) {
            int count = 0;
            for (String w2 : arr) {
                if (w1.equals(w2)) {
                    count++;
                }
            }
            boolean kt = checkContain(w1, words);
            if (!kt) {
                words.put(w1, count);
            }
        }
        return words;

    }

    private boolean checkContain(String word, Map<String, Integer> words) {
        return words.containsKey(word);
    }

    public float importanceUniGram(String s, Map<String, Integer> hashWord) throws IOException {
        List<String> lstWord = p.removingStopWord(s.toLowerCase());
        int sizeS = lstWord.size();
        if (sizeS == 0) {
            sizeS = 1;
        }
        int d = hashWord.size();
        if (d == 0) {
            d = 1;
        }
        float pw = 0;
        for (String w : lstWord) {
            int tf = 0;
            if (hashWord.containsKey(w)) {
                tf = hashWord.get(w);
            }
            pw += (float) tf / d;
        }
        return (float) pw / sizeS;
    }

    public float importanceBiGram(String s, Map<String, Integer> hashWord) {
        String[] words = s.toLowerCase().split(space);
        int sizeS = words.length;
        if (sizeS == 0) {
            sizeS = 1;
        }
        int d = hashWord.size();
        if (d == 0) {
            d = 1;
        }
        float pw = 0;
        for (int i = 0; i < sizeS - 1; i++) {
            String w1 = words[i];
            String w2 = words[i + 1];
            String w = w1 + " " + w2;
            int tf = 0;
            if (hashWord.containsKey(w)) {
                tf = hashWord.get(w);
            }
            pw += (float) tf / d;
        }
        return (float) pw / sizeS;
    }

    public List<String> ner(String s) {
        return ner.nerParser(s);
    }
}
