package nguyenlab.docsum.sosvmrank.ner;

import java.util.ArrayList;
import java.util.List;

import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.util.Triple;

/**
 * This is a demo of calling CRFClassifier programmatically.
 * <p>
 * Usage:
 * {@code java -mx400m -cp "*" NERDemo [serializedClassifier [fileName]] }
 * <p>
 * If arguments aren't specified, they default to
 * classifiers/english.all.3class.distsim.crf.ser.gz and some hardcoded sample
 * text. If run with arguments, it shows some of the ways to get k-best
 * labelings and probabilities out with CRFClassifier. If run without arguments,
 * it shows some of the alternative output formats that you can get.
 * <p>
 * To use CRFClassifier from the command line:
 * </p>
 * <blockquote>
 * {@code java -mx400m edu.stanford.nlp.ie.crf.CRFClassifier -loadClassifier [classifier] -textFile [file] }
 * </blockquote>
 * <p>
 * Or if the file is already tokenized and one word per line, perhaps in a
 * tab-separated value format with extra columns for part-of-speech tag, etc.,
 * use the version below (note the 's' instead of the 'x'):
 * </p>
 * <blockquote>
 * {@code java -mx400m edu.stanford.nlp.ie.crf.CRFClassifier -loadClassifier [classifier] -testFile [file] }
 * </blockquote>
 *
 * @author Jenny Finkel
 * @author Christopher Manning
 */
public class NER {

    String serializedClassifier = "classifiers/english.all.3class.distsim.crf.ser.gz";
    AbstractSequenceClassifier<CoreLabel> classifier = null;

    public NER() throws Exception {
        classifier = CRFClassifier.getClassifier(serializedClassifier);
    }

    public List<String> nerParser(String str) {
        List<Triple<String, Integer, Integer>> triples = classifier.classifyToCharacterOffsets(str);
        List<String> lstner = new ArrayList<>();
        String numNE = String.valueOf(triples.size());
        lstner.add(numNE);
        for (Triple<String, Integer, Integer> trip : triples) {
            lstner.add(trip.first());
        }
        return lstner;
    }

    public List<String> nerParser2(String str) {
        List<Triple<String, Integer, Integer>> triples = classifier.classifyToCharacterOffsets(str);
        List<String> lstner = new ArrayList<>();
        for (Triple<String, Integer, Integer> trip : triples) {
            lstner.add(trip.first());
        }
        return lstner;
    }

    public static void main(String[] args) throws Exception {
        NER ner = new NER();
        String str = "New York (AFP) - A 26-year-old terror suspect under FBI surveillance was shot dead outside a pharmacy in"
                + " Boston on Tuesday after brandishing a knife at police and federal agents, officials said";
        ner.nerParser(str);
    }
}
