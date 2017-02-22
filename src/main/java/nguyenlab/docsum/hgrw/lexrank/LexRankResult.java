package nguyenlab.docsum.hgrw.lexrank;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A dumb container class that holds results from the LexRank algorithm.
 */
public class LexRankResult<T> {

    /**
     * The results, sorted in order of LexRank score
     */
    public List<T> rankedResults;
    /**
     * A mapping from each element to its LexRank score
     */
    public Map<T, Double> scores;
    /**
     * A mapping from each element to its neighbors in the thresholded
     * connectivity graph.
     */
    public Map<T, List<T>> neighbors;

    public LexRankResult() {
        rankedResults = new ArrayList<T>();
        scores = new HashMap<T, Double>();
        neighbors = new HashMap<T, List<T>>();
    }

}
