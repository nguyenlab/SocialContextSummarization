package nguyenlab.docsum.crf.features.complex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections15.Transformer;

import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.util.CoreMap;
import edu.uci.ics.jung.algorithms.scoring.HITS;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;
import nguyenlab.docsum.crf.features.Feature;
import nguyenlab.docsum.crf.entities.Doc;

public class HITSScoresFeature implements Feature<double[], Doc> {

    @Override
    public double[] extract(Doc doc, Object... args) {
        List<CoreMap> sents = doc.getSentences();

        double[] ft = new double[sents.size()];

        List<Set<String>> tokensList = sents.stream()
                .map(sent -> sent.get(TokensAnnotation.class).stream()
                        .map(e -> e.get(LemmaAnnotation.class).toLowerCase()).collect(Collectors.toSet()))
                .collect(Collectors.toList());
        Graph<Integer, Integer> g = new DirectedSparseGraph<Integer, Integer>();
        for (int i = 0; i < tokensList.size(); i++) {
            g.addVertex(i);
        }
        final List<Double> weights = new ArrayList<>();
        for (int i = 1; i < tokensList.size(); i++) {
            Set<String> tokens1 = tokensList.get(i);
            for (int j = 0; j < i; j++) {
                Set<String> tokens2 = tokensList.get(j);
                long inter = tokens1.stream().filter(e -> tokens2.contains(e)).count();
                double cos = inter / Math.sqrt(tokens1.size() * tokens2.size() + 1e-6);
                g.addEdge(weights.size(), Arrays.asList(i, j));
                weights.add(cos);
            }
        }
        Transformer<Integer, Double> edge_weights = new Transformer<Integer, Double>() {

            @Override
            public Double transform(Integer input) {
                // TODO Auto-generated method stub
                return weights.get(input);
            }
        };
        HITS<Integer, Integer> hits = new HITS<>(g, edge_weights, 0.0);
        hits.setTolerance(1e-6);
        hits.setMaxIterations(200);
        hits.evaluate();
        for (Integer v : g.getVertices()) {
            ft[v] = hits.getVertexScore(v).authority;
        }

        return ft;
    }

    @Override
    public String name() {
        return "HITS_Scores";
    }
}
