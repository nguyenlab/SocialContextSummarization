package nguyenlab.docsum.l2rccf.features;

import edu.berkeley.nlp.lm.ContextEncodedNgramLanguageModel;
import edu.berkeley.nlp.lm.io.LmReaders;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nguyenlab.docsum.sosvmrank.ner.NER;
import nguyenlab.docsum.l2rccf.utils.FileUtil;
import nguyenlab.docsum.l2rccf.utils.Log;
import nguyenlab.docsum.l2rccf.utils.PreProcessing;
import nguyenlab.docsum.l2rccf.utils.WordUtil;

public class CommentFeature {

    String space = "\\s+";
    PreProcessing p = null;
    WordUtil wordUtil = null;
    NER ner = null;
    Map<String, Integer> vob = null;
    FileUtil fileUtil = null;
    String vobFile = "dict/gbvocab.txt";
    String lmUniFile = "LM/ngram-word-1.lm";
    String lmBiFile = "LM/ngram-word-2.lm";

    ContextEncodedNgramLanguageModel<String> lmUniGram = null;
    ContextEncodedNgramLanguageModel<String> lmBiGram = null;

    public CommentFeature() throws Exception {
        p = new PreProcessing();
        wordUtil = new WordUtil();
        fileUtil = new FileUtil();
        vob = new HashMap<String, Integer>();
        vob = fileUtil.readVocab(vobFile);
        ner = new NER();
        lmUniGram = LmReaders.readContextEncodedLmFromArpa(lmUniFile);
        lmBiGram = LmReaders.readContextEncodedLmFromArpa(lmBiFile);
    }

    public int lenght(String comment) {
        String[] arr = comment.split(space);
        return arr.length;
    }

    public float hTFIDF(String comment, List<String> comments, Map<String, Integer> hashWord) throws IOException {
        float htfidf = 0;
        List<String> lstWord = p.removingStopWord(comment);
        int lstSize = lstWord.size();
        if (lstSize == 0) {
            lstSize = 1;
        }
        int N = comments.size();
        int hashSize = hashWord.size();
        for (String w : lstWord) {
            float tf = 0;
            if (hashWord.containsKey(w)) {
                tf = (float) hashWord.get(w) / hashSize;
            }
            int df = DF(w, comments);
            if (df == 0) {
                df = 1;
            }
            double idf = Log.log2(N / df);
            htfidf += (float) tf * idf;
        }
        if (Double.isInfinite(htfidf)) {
            return 0;
        }
        return (float) htfidf / lstSize;
    }

    private int DF(String word, List<String> comments) {
        int df = 1;
        for (String comment : comments) {
            if (comment.contains(word)) {
                df++;
            }
        }
        return df;
    }

    public float OOV(String s) {
        int count = 0;
        String[] arr = s.toLowerCase().split("\\s+");
        for (String w : arr) {
            if (!vob.containsKey(w)) {
                count++;
            }
        }
        return (float) count / vob.size();
    }

    public float OOVPercentage(String s) {
        float count = OOV(s);
        String[] arr = s.toLowerCase().split("\\s+");
        return (float) count / arr.length;
    }

    public List<String> ner(String s) {
        return ner.nerParser(s);
    }

    public float qualityUniGram(String comment) {
        float sim = 0;
        String[] words = comment.split(space);
        List<String> lstWord = new ArrayList<String>();
        for (String w : words) {
            lstWord.add(w);
        }
        try {
            sim = lmUniGram.scoreSentence(lstWord);
        } catch (Exception ex) {
            return 0;
        }
        return (float) sim / 100;
    }

    public float qualityBiGram(String comment) {
        float sim = 0;
        String[] words = comment.split(space);
        List<String> lstWord = new ArrayList<String>();
        for (String w : words) {
            lstWord.add(w);
        }
        try {
            sim = lmBiGram.scoreSentence(lstWord);
        } catch (Exception ex) {
            return 0;
        }
        return sim / 100;
    }

    public float qualityTriGram(String comment) {
        float sim = 0;
        return sim;
    }
}
