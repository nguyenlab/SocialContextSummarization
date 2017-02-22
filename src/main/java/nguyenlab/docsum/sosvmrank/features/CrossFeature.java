package nguyenlab.docsum.sosvmrank.features;

import nguyenlab.docsum.sosvmrank.ner.NER;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import nguyenlab.docsum.l2rccf.features.CommonFeature;

import nguyenlab.docsum.sortesum.features.Coefficient;
import nguyenlab.docsum.sortesum.features.DamerauLevenshtein;
import nguyenlab.docsum.sortesum.features.IDF;
import nguyenlab.docsum.sortesum.features.JaroWinklerDistance;
import nguyenlab.docsum.sortesum.features.LCS;
import nguyenlab.docsum.sortesum.features.Levenshtein;
import nguyenlab.docsum.sortesum.features.OtherFeatures;
import nguyenlab.docsum.sortesum.features.SmithWaterman;
import nguyenlab.docsum.l2rccf.entities.Word;
import nguyenlab.docsum.l2rccf.utils.FileUtil;
import nguyenlab.docsum.l2rccf.utils.PreProcessing;
import nguyenlab.docsum.tools.SimpleWord2VecDouble;
//sosvmrank

public class CrossFeature {

    String regex = "\\s+";
    CommonFeature comFeature = null;
    FileUtil fileUtil = null;
    NER ner = null;
    PreProcessing p = null;

    OtherFeatures otherFeature = null;//other feature
    Coefficient coeffi = null;//coefficient matching
    LCS lcs = null;//longest common subtring
    Levenshtein leven = null;// Levenshtein distance
    IDF idf = null;// IDF
    JaroWinklerDistance jaro = null;//Jaro distance
    SmithWaterman sw;//Smithwaterman distance
    DamerauLevenshtein dl;//DamerauLevenshtein distance

    int feature_number = 13;
    int feature_number_lexical = 5;
    int feature_number_distance = 8;
    SimpleWord2VecDouble w2v = null;
    String w2vpath = "gbvectors.bin";

    public CrossFeature() throws Exception {
        comFeature = new CommonFeature();
        fileUtil = new FileUtil();
        ner = new NER();
        p = new PreProcessing();
        otherFeature = new OtherFeatures();
        coeffi = new Coefficient();
        lcs = new LCS();
        leven = new Levenshtein();
        idf = new IDF();
        jaro = new JaroWinklerDistance();

        w2v = SimpleWord2VecDouble.Loader.loadGoogleModel(w2vpath, true);
        System.out.println("Successfully loading W2V model!!!");
    }

    public double maxCosine(String instance, List<String> auxInstances) {
        double maxCosine = 0;
        for (String ints : auxInstances) {
            double cosine = cosine(instance, ints);
            if (maxCosine < cosine) {
                maxCosine = cosine;
            }
        }
        return maxCosine;
    }

    public double maxRTELexicalScore(String instance, List<String> auxInstances) {
        String[] s = instance.split(regex);
        double max_rte_lexical = 0;
        for (String aux : auxInstances) {
            String[] t = aux.split(regex);
            sw = new SmithWaterman(s, t);
            dl = new DamerauLevenshtein(s, t);

            //Coefficient features
            double overlap_coefficient = coeffi.Overlap_coefficient(s, t);
            double overlap2_coefficient = coeffi.inclu_exclu_coefficient(s, t);

            //Longest common subtring
            double lsc_value = (double) lcs.lcs(s, t) / (double) Math.min(s.length, t.length);
            //Percentage of words in s appearing in t
            double idf_percentage_st = idf.occurT(s, t);
            //Percentage of words in t apprearing in s
            double idf_percentage_ts = idf.occurT(t, s);

            double total = overlap_coefficient + overlap2_coefficient + lsc_value + idf_percentage_st + idf_percentage_ts;
            double rte_score = (double) total / feature_number_lexical;
            if (max_rte_lexical < rte_score) {
                max_rte_lexical = rte_score;
            }
        }
        return (double) max_rte_lexical / auxInstances.size();
    }

    public double maxRTEDistanceScore(String instance, List<String> auxInstances) {
        String[] s = instance.split(regex);
        double max_rte_distance = 0;
        for (String aux : auxInstances) {
            String[] t = aux.split(regex);
            sw = new SmithWaterman(s, t);
            dl = new DamerauLevenshtein(s, t);

            //Calcualting all feature of RTE
            //Other features
            double manhatan = otherFeature.manhatan(s, t);
            double euclidean = otherFeature.euclidean(s, t);
            //Coefficient features
            double matching_coefficient = coeffi.Matching_coefficient(s, t);
            double dice_coefficient = coeffi.Dice_coefficient(s, t);
            double jarccard_coefficient = coeffi.Jaccard_coefficient(s, t);

            //Jaro distance
            double jaro_distance = jaro.proximity(s, t);
            //SmithWaterman value		
            double smith_waterman = (double) sw.computeSmithWaterman();
            //DamerauLevenshtein value
            double damerrau_Levenshtein = (double) dl.getSimilarity();

            double total = manhatan + euclidean + dice_coefficient + jarccard_coefficient + matching_coefficient
                    + jaro_distance + smith_waterman + damerrau_Levenshtein;

            double rte_score = (double) total / feature_number_distance;
            if (max_rte_distance < rte_score) {
                max_rte_distance = rte_score;
            }
        }
        return (double) max_rte_distance / auxInstances.size();
    }

    public double maxRteScore(String instance, List<String> auxInstances) {
        String[] s = instance.split(regex);
        double max_rte = 0;
        for (String aux : auxInstances) {
            String[] t = aux.split(regex);
            sw = new SmithWaterman(s, t);
            dl = new DamerauLevenshtein(s, t);

            //Calcualting all feature of RTE
            //Other features
            double manhatan = otherFeature.manhatan(s, t);
            double euclidean = otherFeature.euclidean(s, t);
            //Coefficient features
            double matching_coefficient = coeffi.Matching_coefficient(s, t);
            double dice_coefficient = coeffi.Dice_coefficient(s, t);
            double overlap_coefficient = coeffi.Overlap_coefficient(s, t);
            double jarccard_coefficient = coeffi.Jaccard_coefficient(s, t);
            double overlap2_coefficient = coeffi.inclu_exclu_coefficient(s, t);

            //Longest common subtring
            double lsc_value = (double) lcs.lcs(s, t) / (double) Math.min(s.length, t.length);
            //Levenstein distance
            double lenvenstein_distance = leven.LevenshteinDistance(s, t);
            //Percentage of words in s appearing in t
            double idf_percentage_st = idf.occurT(s, t);
            //Percentage of words in t apprearing in s
            double idf_percentage_ts = idf.occurT(t, s);

            //Jaro distance
            double jaro_distance = jaro.proximity(s, t);
            //SmithWaterman value		
            double smith_waterman = (double) sw.computeSmithWaterman();
            //DamerauLevenshtein value
            double damerrau_Levenshtein = (double) dl.getSimilarity();
            double total = manhatan + euclidean + matching_coefficient + dice_coefficient + jarccard_coefficient + jaro_distance + lenvenstein_distance
                    + lsc_value + overlap_coefficient + overlap2_coefficient + idf_percentage_st + idf_percentage_ts + damerrau_Levenshtein + smith_waterman;
            //double total = euclidean + cosine + matching_coefficient + dice_coefficient + tfidf + overlap2_coefficient + idf_percentage_st;
            //double total = matching_coefficient+overlap_coefficient + overlap2_coefficient + lsc_value + idf_percentage_st
            //			+ idf_percentage_ts + smith_waterman;
            double rte_score = (double) total / feature_number;
            if (max_rte < rte_score) {
                max_rte = rte_score;
            }
        }
        return (double) max_rte / auxInstances.size();
    }

    public double crossLDAScore(String instance, Map<String, Double> auxLDA) throws IOException {
        List<String> lstWord = p.removingStopWord(instance);
        Set<String> keys = auxLDA.keySet();
        double cross_lda_score = 0;
        for (String w : lstWord) {
            if (keys.contains(w)) {
                cross_lda_score += auxLDA.get(w);
            }
        }
        return (double) cross_lda_score / keys.size();
    }

    public float maxRougeF(String instance, List<String> auxInstances) {
        float maxRougeF = comFeature.maxRougeF(instance, auxInstances);
        return maxRougeF;
    }

    public float maxRougeP(String instance, List<String> auxInstances) {
        return comFeature.maxRougeP(instance, auxInstances);
    }

    public float maxRougeR(String instance, List<String> auxInstances) {
        return comFeature.maxRougeR(instance, auxInstances);
    }

    /**
     *
     * @param instance
     * @param title
     * @return: only for comment summarization
     */
    public float titleSim(String instance, String title) {
        float rougeP = comFeature.rougeP(instance, title);
        float rougeR = comFeature.rougeR(instance, title);
        return comFeature.rougeF(rougeP, rougeR);
    }

    /**
     *
     * @param comment
     * @param sents
     * @return: only for comment summarization
     */
    public double maxSentPos(String comment, List<String> sents) {
        int pos = 0;
        int count = 0;
        float maxRougeF = 0;
        for (String sent : sents) {
            float rougeP = comFeature.rougeP(comment, sent);
            float rougeR = comFeature.rougeR(comment, sent);
            float rougeF = comFeature.rougeF(rougeP, rougeR);
            if (maxRougeF < rougeF) {
                maxRougeF = rougeF;
                pos = count;
            }
            count++;
        }
        return (double) pos / sents.size();
    }

    /**
     *
     * @return: only for comment summarization
     */
    public float leadSentSim(String comment, List<String> sents, int leadNum) {
        float sim = 0;
        List<String> leadSent = sents.subList(0, leadNum);
        for (String s : leadSent) {
            float rougeP = comFeature.rougeP(comment, s);
            float rougeR = comFeature.rougeR(comment, s);
            sim += comFeature.rougeF(rougeP, rougeR);
        }
        return (float) sim / leadSent.size();
    }

    public float simUniGram(String instance, Map<String, Integer> hashWord)
            throws IOException {
        float sim = 0;
        List<String> words = fileUtil.removeStopWord(instance);
        int size = hashWord.size();
        for (String w : words) {
            if (hashWord.containsKey(w)) {
                int tf = hashWord.get(w);
                sim += (float) tf / size;
            }
        }
        return sim;
    }

    public float simUniTFIDF(String instance, Map<String, Double> hashTFIDF) throws IOException {
        float sim = 0;
        List<String> words = fileUtil.removeStopWord(instance);
        for (String w : words) {
            if (hashTFIDF.containsKey(w)) {
                double tfidf = hashTFIDF.get(w);
                sim += tfidf;
            }
        }
        return (float) sim / hashTFIDF.size();
    }

    private List<Word> getTopWord(Map<String, Integer> hashWord, int topNum) {
        List<Word> lstWord = new ArrayList<Word>();
        Set<String> keys = hashWord.keySet();
        for (String key : keys) {
            int value = hashWord.get(key);
            Word w = new Word(key, value);
            lstWord.add(w);
        }

        Collections.sort(lstWord);
        List<Word> lstTop = lstWord.subList(0, topNum);
        return lstTop;
    }

    public float simTopUniGram(String instance, Map<String, Integer> hashWord, int topNum)
            throws IOException {
        float sim = 0;
        String[] arr = instance.split(regex);
        List<Word> lstTop = getTopWord(hashWord, topNum);
        List<String> words = fileUtil.removeStopWord(instance);
        int size = lstTop.size();
        for (String w : words) {
            for (Word word : lstTop) {
                if (word.getWord().equals(w)) {
                    int tf = word.getFrequency();
                    sim += (float) tf / size;
                }
            }
        }
        return (float) sim / arr.length;
    }

    public float countTopUniGram(String instance, Map<String, Integer> hashWord, int topNum)
            throws IOException {
        int count = 0;
        List<Word> lstTop = getTopWord(hashWord, topNum);
        List<String> words = fileUtil.removeStopWord(instance);
        for (String w : words) {
            for (Word word : lstTop) {
                String tmp = word.getWord();
                if (w.equals(tmp)) {
                    count++;
                }
            }
        }
        return (float) count / lstTop.size();
    }

    public float countTopEntity(String instance, List<Word> lstTop) {
        int count = 0;
        String[] arr = instance.split(regex);
        List<String> entity = ner.nerParser2(instance);
        for (String e : entity) {
            for (Word word : lstTop) {
                String w = word.getWord();
                if (e.equals(w)) {
                    count++;
                }
            }
        }
        return (float) count / arr.length;
    }

    public float simTopEntity(String instance, List<Word> lstTop) {
        float sim = 0;
        String[] arr = instance.split(regex);
        List<String> entity = ner.nerParser2(instance);
        int size = lstTop.size();
        for (String e : entity) {
            for (Word word : lstTop) {
                String w = word.getWord();
                int tf = word.getFrequency();
                if (e.equals(w)) {
                    sim += tf / size;
                }
            }
        }
        int normalization = arr.length * lstTop.size();
        return (float) sim / normalization;
    }

    public List<Word> getTopEntity(List<String> instances, int topNum) {
        List<Word> lstAllEntity = getEntity(instances);
        int top = topNum;
        if (lstAllEntity.size() < topNum) {
            top = lstAllEntity.size();
        }
        Collections.sort(lstAllEntity);
        List<Word> lstTop = lstAllEntity.subList(0, top);
        return lstTop;
    }

    private List<Word> getEntity(List<String> instances) {
        List<Word> result = new ArrayList<Word>();
        List<String> lstAllEntity = new ArrayList<String>();
        for (String ints : instances) {
            List<String> lstEntity = ner.nerParser2(ints);
            for (String e : lstEntity) {
                lstAllEntity.add(e);
            }
        }

        int size = lstAllEntity.size();
        boolean[] flags = new boolean[size];
        for (int i = 0; i < size; i++) {
            flags[i] = false;
        }

        for (int i = 0; i < size; i++) {
            String e1 = lstAllEntity.get(i);
            int count = 0;
            for (int j = 0; j < size; j++) {
                String e2 = lstAllEntity.get(j);
                if (e1.equals(e2)) {
                    count++;
                }
            }
            if (checkContain(e1, result)) {
                Word w = new Word(e1, count);
                result.add(w);
            }
        }

        return result;
    }

    // Check the appearance of a word in a list
    private boolean checkContain(String word, List<Word> words) {
        boolean kt = true;
        for (Word w : words) {
            if (w.getWord().equals(word)) {
                kt = false;
                break;
            }
        }
        return kt;
    }

    // Cosin score
    public double cosine(String sent, String comment) {
        String[] t = sent.toLowerCase().split(regex);
        String[] h = comment.toLowerCase().split(regex);
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

    private double wordSentSim(String w, String sent) throws IOException {
        double sim = 0;
        List<String> lstW2 = p.removingStopWord(sent);
        for (String w2 : lstW2) {
            sim += w2v.similarity(w, w2);
        }
        return sim;
    }

    private double sentSim(String s1, String s2) throws IOException {
        double sim = 0;

        List<String> lstW1 = p.removingStopWord(s1);
        List<String> lstW2 = p.removingStopWord(s2);
        int len = lstW1.size() + lstW2.size();
        for (String w : lstW1) {
            sim += wordSentSim(w, s2);
        }
        return (double) sim / len;
    }

    public double maxW2VSim(String instance, List<String> auxInstances) throws IOException {
        double maxsim = 0;
        String[] arr = instance.split(regex);
        for (String inst : auxInstances) {
            double sim = sentSim(instance, inst);
            if (maxsim < sim) {
                maxsim = sim;
            }
        }
        return (double) maxsim / arr.length;
    }

    private int index(String instance, List<String> auxInstances) {
        int ind = 0;
        double maxCosine = 0;
        for (int i = 0; i < auxInstances.size(); i++) {
            double cosine = cosine(instance, auxInstances.get(i));
            if (maxCosine < cosine) {
                maxCosine = cosine;
                ind = i;
            }
        }
        return ind;
    }

    public double maxSentPosCosine(String instance, List<String> auxInstances) {
        double maxpos = 0;
        int ind = index(instance, auxInstances);
        maxpos = (double) ind / auxInstances.size();
        return maxpos;
    }

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
}
