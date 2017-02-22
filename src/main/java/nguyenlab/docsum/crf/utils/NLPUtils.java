package nguyenlab.docsum.crf.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import edu.stanford.nlp.pipeline.StanfordCoreNLP;

public class NLPUtils {

    public static final Set<String> stopwords;
    public static final StanfordCoreNLP instance;
    public static final Set<String> indicatorWords;

    static {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, lemma, parse");
        instance = new StanfordCoreNLP(props);

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(NLPUtils.class.getResourceAsStream("stopwords.txt")));
        Set<String> tmp = new HashSet<String>();
        try {
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                tmp.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        stopwords = Collections.unmodifiableSet(tmp);

        reader = new BufferedReader(new InputStreamReader(NLPUtils.class.getResourceAsStream("indicators.txt")));
        tmp = new HashSet<String>();
        try {
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                tmp.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        indicatorWords = Collections.unmodifiableSet(tmp);

    }
}
