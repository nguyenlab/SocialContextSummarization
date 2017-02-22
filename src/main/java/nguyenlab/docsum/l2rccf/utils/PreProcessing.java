package nguyenlab.docsum.l2rccf.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * *
 * Preprocessing sentences
 *
 * @author minhtien
 *
 */
public class PreProcessing {

    String regex_space = " ";
    String regex_tab = "\t";
    String regex = ",().\"";
    String dict_file = "dict/stopwords_en.txt";

    public String[] slippingSentSpace(String sent) {
        String result[] = sent.toLowerCase().split(regex_space);
        return result;
    }

    public String[] slippingSentTab(String sent) {
        String result[] = sent.toLowerCase().split(regex_tab);
        return result;
    }

    public List<String> readingDict(String file_path) throws IOException {
        List<String> stopWords = new ArrayList<String>();
        FileUtil util = new FileUtil();
        stopWords = util.readFileString(file_path);
        return stopWords;
    }

    private boolean isContain(String word, List<String> stopwords) {
        boolean kt = true;
        for (String w : stopwords) {
            if (w.equals(word)) {
                kt = false;
                break;
            }
        }
        return kt;
    }

    public List<String> removingStopWord(String sent) throws IOException {
        List<String> result = new ArrayList<String>();
        String[] words = slippingSentSpace(sent);
        List<String> stopWords = readingDict(dict_file);
        for (String w : words) {
            boolean kt = isContain(w, stopWords);
            if (kt) {
                result.add(w);
            }
        }
        return result;
    }
}
