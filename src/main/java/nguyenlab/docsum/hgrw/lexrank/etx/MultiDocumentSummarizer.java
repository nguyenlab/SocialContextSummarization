/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nguyenlab.docsum.hgrw.lexrank.etx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import nguyenlab.docsum.hgrw.features.Features;
import nguyenlab.docsum.hgrw.lexrank.Sentence;
import nguyenlab.docsum.hgrw.lexrank.Similar;
import nguyenlab.docsum.hgrw.lexrank.Summarizer;

/**
 *
 * @author TRAN
 * @param <W>
 */
public class MultiDocumentSummarizer<W extends Similar<W>> {

    private final SentenceCrossWeights sentenceCrossWeights;
    private final Summarizer summarizer;
    private final Features features;

    public MultiDocumentSummarizer(Features features, SentenceCrossWeights sentenceCrossWeights) {
        this.features = features;
        this.sentenceCrossWeights = sentenceCrossWeights;
        this.summarizer = new Summarizer();
    }

    private List<Sentence<W>> packTypedSentences(Map<String, List<List<W>>> typedDoc) {
        List<Sentence<W>> sentences = new ArrayList<>();

//        List<List<W>> all = new ArrayList<List<W>>();
//        for (String type : typedDoc.keySet()) {
//
//            all.addAll(typedDoc.get(type));
//        }
//        List<W> wordList;
//        Set<W> wordSet = new HashSet<W>();
//
//        for (List<W> seq : all) {
//            wordSet.addAll(seq);
//        }
//
//        wordList = new ArrayList<W>(wordSet);
//        Map<W, Double> idf = Summarizer.idf(wordList, all);
        for (String type : typedDoc.keySet()) {
            List<List<W>> sentenceTexts = typedDoc.get(type);
            for (int i = 0; i < sentenceTexts.size(); i++) {

                Sentence<W> sentence = new Sentence(
                        i, //use Sentence.ref as index
                        sentenceTexts.get(i),
                        features,
                        sentenceCrossWeights,
                        type);

                sentences.add(sentence);
            }
        }
        return sentences;
    }

    /**
     *
     * @param typedSentences List of sentences grouped by their types (e.g.
     * doc-sentence, comment). Example: {doc-sentence:[sentence_1,
     * sentence_2,...], comment:[sentence_1,sentence_2,...]}
     * @param resultSelector
     * @param param
     * @return indices of selected sentences mapped by type
     */
    public Map<String, List<Integer>> summarize(Map<String, List<List<W>>> typedSentences, Summarizer.ResultSelector resultSelector, Object param) {
        List<Sentence<W>> ranked;
        switch (resultSelector) {
            case LOCAL_MAXIMA:
            case THRESHOLD:
                ranked = summarizer.summarize(packTypedSentences(typedSentences), resultSelector, param);
                break;
            case TOP:
            case RANK_ONLY:
                ranked = summarizer.summarize(packTypedSentences(typedSentences), Summarizer.ResultSelector.RANK_ONLY, param);
                break;
            default:
                throw new UnsupportedOperationException();
        }
        int top = Integer.MAX_VALUE;
        if (resultSelector == Summarizer.ResultSelector.TOP) {
            // top by typed
            top = (Integer) param;
        }

        Map<String, List<Integer>> output = new HashMap<String, List<Integer>>();
        for (String type : typedSentences.keySet()) {
            output.put(type, new ArrayList<Integer>());
        }

        for (Sentence<W> sentence : ranked) {
            List<Integer> listToAdd = output.get(sentence.getType());
            if (listToAdd.size() >= top) {
                continue;
            }
            listToAdd.add((Integer) sentence.getRef());
        }
        return output;
    }

}
