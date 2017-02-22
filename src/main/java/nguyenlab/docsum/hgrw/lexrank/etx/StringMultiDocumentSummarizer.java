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

import nguyenlab.docsum.hgrw.features.extractors.FeatureExtractors;
import nguyenlab.docsum.hgrw.lexrank.Summarizer;
import nguyenlab.docsum.hgrw.lexrank.Word;

/**
 *
 * @author TRAN
 */
public class StringMultiDocumentSummarizer {

    private final WordSegmenter wordSegmenter;
    private final SentenceCrossWeights sentenceCrossWeights;
    private final FeatureExtractors<Word> featureExtractors;

    public StringMultiDocumentSummarizer(FeatureExtractors<Word> featureExtractors, SentenceCrossWeights sentenceCrossWeights, WordSegmenter wordSegmenter) {
        this.featureExtractors = featureExtractors;
        this.sentenceCrossWeights = sentenceCrossWeights;
        this.wordSegmenter = wordSegmenter;
    }

    private List<Word> toWords(String text) {
        List<String> words = wordSegmenter.segment(text);
        List<Word> seq = new ArrayList<Word>();
        for (String word : words) {
            seq.add(new Word(word));
        }
        return seq;
    }

    /**
     *
     * @param typedDocs List of sentences grouped by their types (e.g.
     * doc-sentence, comment). Example: {doc-sentence:[sentence_1,
     * sentence_2,...], comment:[sentence_1,sentence_2,...]}
     * @param resultSelector
     * @param param
     * @return
     */
    public Map<String, List<String>> summarize(final Map<String, List<String>> typedDocs, Summarizer.ResultSelector resultSelector, Object param) {
        Map<String, List<List<Word>>> internalInput = new HashMap<>();

//        List<List<Word>> allSentences = new ArrayList<>();
        for (String type : typedDocs.keySet()) {

            List<List<Word>> segmentedSentences = new ArrayList<>();
            internalInput.put(type, segmentedSentences);

            for (String sentence : typedDocs.get(type)) {
                segmentedSentences.add(toWords(sentence));
            }
//            allSentences.addAll(segmentedSentences);
        }

//        Map<String, Map<Word, Double>> typedTfIdfMap = new HashMap<>();
//
//        Map<Word, Double> idf = Summarizer.idf(allSentences);
//
//        for (String type : typedDocs.keySet()) {
//            typedTfIdfMap.put(type, idf);
//        }
//
//        TfIdfFeature<Word> tfIdfFeature = new TfIdfFeature<>(typedTfIdfMap);
        MultiDocumentSummarizer<Word> summarizer = new MultiDocumentSummarizer<>(featureExtractors.extract(internalInput), sentenceCrossWeights);

        Map<String, List<Integer>> internalOutput = summarizer.summarize(internalInput, resultSelector, param);

        Map<String, List<String>> output = new HashMap<String, List<String>>();
        for (String type : typedDocs.keySet()) {

            List<String> outSentences = new ArrayList<String>();
            output.put(type, outSentences);

            List<String> inSentences = typedDocs.get(type);

            for (int pos : internalOutput.get(type)) {
                outSentences.add(inSentences.get(pos));
            }
        }
        return output;
    }

}
