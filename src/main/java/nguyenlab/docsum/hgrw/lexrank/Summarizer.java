package nguyenlab.docsum.hgrw.lexrank;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This class can be used to summarize a bunch of commentTexts. It computes the
 * IDF for every term in the group of commentTexts, re-represents them as a
 * tf-idf weighted bag of words, and runs LexRank on the whole thing.
 */
public class Summarizer<W extends Similar<W>> {

    public enum ResultSelector {
        LOCAL_MAXIMA, TOP, THRESHOLD, RANK_ONLY
    }

    /**
     * Computes the inverse document frequency for each word in the corpus. IDF
     * is defined as log(N/t), where N is the number of documents (in this case,
     * commentTexts), and t is the number of documents that term appears in.
     *
     * @param <W>
     * @param words
     * @param sentences
     * @return
     */
    public static <W extends Similar<W>> Map<W, Double> idf(List<List<W>> sentences) {
        Set<W> words = new HashSet<>();
        for (List<W> tk : sentences) {
            words.addAll(tk);
        }
        Map<W, Double> idf = new HashMap<W, Double>();
        Map<W, Integer> df = new HashMap<W, Integer>();
        for (W word : words) {
            df.put(word, 0);
        }
        for (List<W> sentence : sentences) {
            for (W word : sentence) {
                df.put(word, df.get(word) + 1);
            }
        }
        for (W word : words) {
            idf.put(word, Math.log(sentences.size() * 1.0 / df.get(word)));
        }
        return idf;
    }

    /**
     * Generates a summary of the sentences passed into this Summarizer. The
     * output is a list of salient sentences, ordered from most salient to
     * least. Currently, we just take any sentence that is locally maximal in
     * relevance, so it's possible that the summary could have multiple entries
     * that mean the same thing. Anecdotally, though, it work pretty well.
     *
     * @param sentences
     * @param resultSelector
     * @param resultSelectorParam
     * @return
     */
    public List<Sentence<W>> summarize(List<Sentence<W>> sentences, ResultSelector resultSelector, Object resultSelectorParam) {
        switch (resultSelector) {
            case LOCAL_MAXIMA:
                return summarizeByLocalMaxima(sentences);
            case THRESHOLD:
                return summarizeByThreshold(sentences, (Double) resultSelectorParam);
            case TOP:
                return summarizeByTop(sentences, (Integer) resultSelectorParam);
            case RANK_ONLY:
                return summarizeByRankOnly(sentences);
            default:
                throw new UnsupportedOperationException();
        }
    }

    public List<Sentence<W>> summarizeByLocalMaxima(List<Sentence<W>> sentences) {
        LexRankResult<Sentence<W>> results = LexRank.rank(sentences, 0.002, false);

        List<Sentence<W>> finalResults = new ArrayList<Sentence<W>>();
        //try{
        for (Sentence<W> c : results.rankedResults) {
            // Only return results that are local maxima
            boolean max = true;
            for (Sentence<W> neighbor : results.neighbors.get(c)) {
                if (results.scores.get(neighbor) > results.scores.get(c)) {
                    max = false;
                }
            }
            if (max) {
                finalResults.add(c);
            }
        }
        //} catch(Exception ex){
        //	System.out.println("Error:=" + ex.getMessage());
        //}
        return finalResults;
    }

    public List<Sentence<W>> summarizeByTop(List<Sentence<W>> sentences, int top) {
        LexRankResult<Sentence<W>> results = LexRank.rank(sentences, 0.002, false);
        if (results.rankedResults.size() <= top) {
            return results.rankedResults;
        }
        return results.rankedResults.subList(0, top);
    }

    public List<Sentence<W>> summarizeByThreshold(List<Sentence<W>> sentences, double threshold) {
        LexRankResult<Sentence<W>> results = LexRank.rank(sentences, 0.002, false);

        List<Sentence<W>> output = new ArrayList<Sentence<W>>();
        for (Map.Entry<Sentence<W>, Double> entry : results.scores.entrySet()) {
            if (entry.getValue() >= threshold) {
                output.add(entry.getKey());
            }
        }
        return output;
    }

    public List<Sentence<W>> summarizeByRankOnly(List<Sentence<W>> sentences) {
        return LexRank.rank(sentences, 0.002, false).rankedResults;
    }
}
