package nguyenlab.docsum.l2rccf.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import nguyenlab.docsum.l2rccf.entities.Word;

public class WordUtil {

    PreProcessing p = new PreProcessing();
    String regex = "\\s+";

    /**
     * *
     *
     * @param word
     * @param doc
     * @return term frequency of a word in a document
     * @throws IOException
     */
    private int DFUniGram(String word, List<String> doc) throws IOException {
        int df = 0;
        for (String s : doc) {
            List<String> words = p.removingStopWord(s);
            for (String w : words) {
                if (word.equals(w)) {
                    df++;
                }
            }
        }
        return df;
    }

    private int TFBiGram(String word, List<String> doc) throws IOException {
        int tf = 0;
        for (String s : doc) {
            String[] words = s.toLowerCase().split(regex);
            for (int i = 0; i < words.length - 1; i++) {
                String w1 = words[i];
                String w2 = words[i + 1];
                String w = w1 + " " + w2;
                if (word.equals(w)) {
                    tf++;
                }
            }
        }
        return tf;
    }

    /**
     * *
     *
     * @param doc
     * @return List of word along with its frequency of a document
     * @throws IOException
     */
    public List<Word> getWordsOneDoc(List<String> doc) throws IOException {
        List<Word> result = new ArrayList<>();
        for (String s : doc) {
            List<String> words = p.removingStopWord(s);
            for (String tmp : words) {
                int tf = DFUniGram(tmp, doc);
                if (checkContain(tmp, result)) {
                    Word w = new Word(tmp, tf);
                    result.add(w);
                }
            }
        }
        return result;
    }

    /**
     * *
     * getting a set of words of a document
     *
     * @param doc
     * @return a HashMap containing a set of words of a document and TF
     * @throws IOException
     */
    public Map<String, Integer> getUniGramOneDocHashMap(List<String> doc) throws IOException {
        Map<String, Integer> words = new HashMap<>();
        //System.out.println("Getting TF of a doc....");
        for (String s : doc) {
            List<String> ws = p.removingStopWord(s);
            for (String tmp : ws) {
                int tf = DFUniGram(tmp, doc);
                if (checkContain(tmp, words) == false) {
                    words.put(tmp, tf);
                }
            }
        }
        return words;
    }

    public Map<String, Double> getTFIDFHashMap(Map<String, Integer> uniGram, List<String> auxInstance) throws IOException {
        Map<String, Double> result = new HashMap<>();
        Set<String> keys = uniGram.keySet();
        int N = auxInstance.size();
        for (String key : keys) {
            int tf = 0;
            if (uniGram.containsKey(key)) {
                tf = uniGram.get(key);
            }
            int df = DFUniGram(key, auxInstance);
            double idf = Log.log2((double) N / df);
            double tfidf = tf * idf;
            result.put(key, tfidf);

            //System.out.println("TF:=" + tf + "\tDF:=" + df + "\tIDF:=" + idf + "\tTFIDF:=" + tfidf);
        }
        return result;
    }

    public Map<String, Integer> getBiGramOneDocHashMap(List<String> doc) throws IOException {
        Map<String, Integer> words = new HashMap<>();
        for (String s : doc) {
            String[] ws = s.toLowerCase().split(regex);
            for (int i = 0; i < ws.length - 1; i++) {
                String w1 = ws[i];
                String w2 = ws[i + 1];
                String w = w1 + " " + w2;
                int tf = TFBiGram(w, doc);
                if (checkContain(w, words) == false) {
                    words.put(w, tf);
                }
            }
        }
        return words;
    }

    /**
     * *
     * @param docs
     * @return list of words along with its frequency of a set of documents
     * @throws IOException
     */
    public List<Word> getWordsSetofDoc(List<List<String>> docs) throws IOException {
        List<Word> result = new ArrayList<>();
        List<Word> word_all = new ArrayList<>();
        List<Word> word_one = new ArrayList<>();
        for (List<String> doc : docs) {
            List<Word> words = getWordsOneDoc(doc);
            for (Word w : words) {
                word_all.add(w);// List of all word
                if (checkContain(w.getWord(), word_one)) {
                    word_one.add(w);//List of words appearing 1 time
                }
            }
        }
        //Count the frequency of word
        for (Word w1 : word_one) {
            int count = 0;
            for (Word w2 : word_all) {
                if (w1.getWord().equals(w2.getWord())) {
                    count++;
                }
            }
            w1.setFrequency(count);
            result.add(w1);
        }
        return result;
    }

    /**
     * *
     * getting a set of words in all documents
     *
     * @param docs
     * @return a HashMap containing words and its frequency over corpus
     * @throws IOException
     */
    /*public Map<String, Integer> getWordsSetofDocHashMap(List<List<String>> docs) throws IOException{
		Map<String, Integer> words = new HashMap<String, Integer>();
		List<Word> word_all = new ArrayList<Word>();
		List<Word> word_one = new ArrayList<Word>();
		for(List<String> doc : docs){
			//System.out.println("Collecting words from doc...");
			List<Word> ws = getWordsOneDoc(doc);
			for(Word w : ws){
				word_all.add(w);// List of all word
				if(checkContain(w.getWord(), word_one)) word_one.add(w);//List of words appearing 1 time
			}
		}
		//Count the frequency of word
		for(Word w1 : word_one){
			int count = 0;
			for(Word w2 : word_all)
				if(w1.getWord().equals(w2.getWord())) count ++;
				words.put(w1.getWord(), count);
		}
		return words;
	}*/
    public Map<String, Integer> getWordsSetofDocHashMap(List<List<String>> docs) throws IOException {
        Map<String, Integer> words = new HashMap<>();
        //Map<String, Integer> word_all = new HashMap<String, Integer>();
        Map<String, Integer> word_one = new HashMap<>();
        int i = 1;
        for (List<String> doc : docs) {
            if (doc.isEmpty()) {
                continue;
            }
            System.out.println("Calculating for doc...." + i);
            //System.out.println("Collecting words from doc...");
            Map<String, Integer> ws = getUniGramOneDocHashMap(doc);
            Set<String> keys = ws.keySet();
            for (String k : keys) {
                //word_all.put(k, ws.get(k));// List of all word
                if (checkContain(k, word_one) == false) {
                    word_one.put(k, ws.get(k));//List of words appearing 1 time
                }
            }
            i++;
        }

        //Count the frequency of word
        Set<String> word_one_keys = word_one.keySet();
        //Set<String> word_all_keys = word_one.keySet();
        for (String k : word_one_keys) {
            System.out.println("Colecting the word...." + k);
            int count = 1;
            for (List<String> doc : docs) {
                for (String s : doc) {
                    if (s.contains(k)) {
                        count++;
                        break;
                    }
                }
            }
            words.put(k, count);
        }
        return words;
    }

    public Map<String, Integer> gettingDFHashMap(List<List<String>> docs) throws IOException {
        Map<String, Integer> words_DF = new HashMap<>();
        List<Word> tmp = new ArrayList<>();
        //Getting all words
        int countdoc = 1;
        for (List<String> doc : docs) {
            System.out.println("Getting words from doc " + countdoc);
            List<Word> wordOneDoc = getWordsOneDoc(doc);
            for (Word w : wordOneDoc) {
                if (checkContain(w.getWord(), tmp)) {
                    tmp.add(w);
                }
            }
            countdoc++;
        }
        //Getting DF
        System.out.println("Getting DF from docs....");
        for (Word w : tmp) {
            int count = 0;
            countdoc = 1;
            for (List<String> doc : docs) {
                Map<String, Integer> wordOneDoc = getUniGramOneDocHashMap(doc);
                boolean value = wordOneDoc.containsKey(w.getWord());
                if (value) {
                    count++;
                }
                countdoc++;
            }
            words_DF.put(w.getWord(), count);
        }
        return words_DF;
    }

    //Check the appearance of a word in a list
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

    //Check the appearance of a word in a HashMap
    private boolean checkContain(String word, Map<String, Integer> words) {
        return words.containsKey(word);
    }
}
