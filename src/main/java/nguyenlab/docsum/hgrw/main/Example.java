package nguyenlab.docsum.hgrw.main;

import nguyenlab.docsum.hgrw.lexrank.etx.WordSegmenter;
import nguyenlab.docsum.hgrw.lexrank.etx.TypedSentenceCrossWeights;
import nguyenlab.docsum.hgrw.utils.Stopwords;
import nguyenlab.docsum.hgrw.lexrank.etx.SentenceCrossWeights;
import nguyenlab.docsum.hgrw.lexrank.Word;
import nguyenlab.docsum.hgrw.lexrank.etx.StringMultiDocumentSummarizer;
import nguyenlab.docsum.hgrw.lexrank.Summarizer;
import nguyenlab.docsum.hgrw.utils.FileUtil;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.Stemmer;
import nguyenlab.docsum.hgrw.features.extractors.FeatureExtractor;
import nguyenlab.docsum.hgrw.features.extractors.FeatureExtractors;
import nguyenlab.docsum.hgrw.features.extractors.TfIfdExtractor;

/**
 *
 * @author TRAN
 */
public class Example {

    StringMultiDocumentSummarizer summarizer = null;
    final String typeSentence = "sentence";
    final String typeComment = "comment";

    final double beta = 0.7;
    final SentenceCrossWeights sentenceCrossWeights = new TypedSentenceCrossWeights(
            Arrays.asList(typeSentence, typeComment), new double[][]{
        {1 - beta, beta}, {beta, 1 - beta}});

    public List<String> oneSideSummarization(List<String> instances) {
        // tokenizer
        final WordSegmenter wordSegmenter = new WordSegmenter() {
            @Override
            public List<String> segment(String input) {
                return parse(input);
            }
        };
        // summarizer
        summarizer = new StringMultiDocumentSummarizer(getFeatureExtractors(), sentenceCrossWeights,
                wordSegmenter);
        Map<String, List<String>> input = new HashMap<>();
        input.put(typeSentence, instances);

        Map<String, List<String>> summary = summarizer.summarize(input,
                Summarizer.ResultSelector.TOP, 6);

        List<String> summarizedSentences = summary.get(typeSentence);
        return summarizedSentences;
    }

    public List<List<String>> twoSideSummarization(List<String> refs,
            List<String> sentences, List<String> comments) {
        // tokenizer
        final WordSegmenter wordSegmenter = new WordSegmenter() {
            @Override
            public List<String> segment(String input) {
                return parse(input);
            }
        };

        // summarizer
        summarizer = new StringMultiDocumentSummarizer(getFeatureExtractors(), sentenceCrossWeights,
                wordSegmenter);
        Map<String, List<String>> input = new HashMap<>();
        input.put(typeSentence, sentences);
        input.put(typeComment, comments);

        Map<String, List<String>> summary = summarizer.summarize(input,
                Summarizer.ResultSelector.TOP, 6);

        List<String> summarizedSentences = summary.get(typeSentence);
        List<String> summarizedComments = summary.get(typeComment);

        List<List<String>> result = new ArrayList<>();
        result.add(refs);
        result.add(summarizedSentences);
        result.add(summarizedComments);

        return result;
    }

    public static FeatureExtractors<Word> getFeatureExtractors() {
        List<FeatureExtractor<Word>> list = Arrays.asList(
                new TfIfdExtractor<Word>().withWeight(1.0)
        );
        return new FeatureExtractors<>(list);
    }

    public static final List<String> parse(String text) {
        return stanfordParse(text, true, true, true);
    }

    public static final List<String> stanfordParse(String text, boolean lowercase, boolean stemming, boolean remove_stopwords) {
        List<String> stemmedTokens = new ArrayList<>();
        Reader reader = new StringReader(text);
        Stemmer stemmer = new Stemmer();

        for (edu.stanford.nlp.ling.Word token : PTBTokenizer.newPTBTokenizer(reader).tokenize()) {
            String word = token.word();
            if (stemming) {
                word = stemmer.stem(word.toLowerCase());
            }

            if (lowercase) {
                word = word.toLowerCase();
            }

            if (remove_stopwords) {
                if (Stopwords.self.contains(word)) {
                    word = null;
                }
            }

            if (word != null) {
                stemmedTokens.add(word);
            }
        }
        return stemmedTokens;

    }

    private void writeData(List<String> highlight, List<String> ran_sentences,
            List<String> ran_tweets, int i, String name, String hilightfolder,
            String sumfolder, String commentfolder)
            throws FileNotFoundException {
        FileUtil fu = new FileUtil();
        fu.writeRTEData2File(highlight, ran_sentences, ran_tweets, i, name,
                hilightfolder, sumfolder, commentfolder);
    }

    public static void main(String[] args) throws Exception {
        FileUtil fileUtil = new FileUtil();
        Example docComSum = new Example();

        String inputfolder = "yahoonews-10fold/";
        for (int i = 1; i <= 10; i++) {
            String infolder = inputfolder + "fold-" + i + "/";
            File[] list = fileUtil.listFile(infolder);
            String root = "baseline/lexrank-social-stem/";
            root = root + "fold-" + i + "/";
            File folder = new File(root);
            if (!folder.exists()) {
                folder.mkdirs();
            }

            String hilightfolder = root + "summary/";
            folder = new File(hilightfolder);
            if (!folder.exists()) {
                folder.mkdirs();
            }
            String sumfolder = root + "doc-sum/";
            folder = new File(sumfolder);
            if (!folder.exists()) {
                folder.mkdirs();
            }
            String commentfolder = root + "comment-sum/";
            folder = new File(commentfolder);
            if (!folder.exists()) {
                folder.mkdirs();
            }

            int j = 1;
            for (File f : list) {
                System.out.println(f.getName());
                String name = f.getName();
                if (name.contains(".DS_Store")) {
                    continue;
                }
                List<List<String>> data = fileUtil.readFileColing(infolder
                        + "/" + f.getName());
                List<String> summary = data.get(0);
                List<String> sents = data.get(1);
                List<String> comments = data.get(2);

                List<List<String>> result = docComSum.twoSideSummarization(
                        summary, sents, comments);
                //List<String> sentSum = docComSum.oneSideSummarization(sents);
                //List<String> commentSum = docComSum.oneSideSummarization(comments);
                //docComSum.writeData(summary, sentSum, commentSum, j, name, hilightfolder, sumfolder, commentfolder);

                docComSum.writeData(summary, result.get(1), result.get(2), j, name, hilightfolder, sumfolder, commentfolder);

                j++;
            }
        }
    }
}
