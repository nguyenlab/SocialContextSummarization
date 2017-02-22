package nguyenlab.docsum.sortesum.main;

import nguyenlab.docsum.sortesum.features.Coefficient;
import nguyenlab.docsum.sortesum.features.DamerauLevenshtein;
import nguyenlab.docsum.sortesum.features.IDF;
import nguyenlab.docsum.sortesum.features.JaroWinklerDistance;
import nguyenlab.docsum.sortesum.features.LCS;
import nguyenlab.docsum.sortesum.features.Levenshtein;
import nguyenlab.docsum.sortesum.features.OtherFeatures;
import nguyenlab.docsum.sortesum.features.SmithWaterman;

public class Example {

    public static void main(String[] args) {
        String[] newsSentence = "The special agent in charge of the FBI in Boston, Vincent Lisi , said the suspect had been under 24-hour surveillance by Boston and Massachusetts state police , and the Joint Terrorism Task Force .".split("\\s+");
        String[] tweet = "He was assassinated because he was on a watch list .".split("\\s+");

        OtherFeatures otherFeature = new OtherFeatures();//other feature
        Coefficient coeffi = new Coefficient();//coefficient matching
        LCS lcs = new LCS();//longest common subtring
        Levenshtein leven = new Levenshtein();// Levenshtein distance
        IDF idf = new IDF();// IDF
        JaroWinklerDistance jaro = new JaroWinklerDistance();//Jaro distance
        SmithWaterman sw = new SmithWaterman(newsSentence, tweet);//Smithwaterman distance
        DamerauLevenshtein dl = new DamerauLevenshtein(newsSentence, tweet);//DamerauLevenshtein distance

        //Calcualting all feature of RTE
        //Other features
        double manhatan = otherFeature.manhatan(newsSentence, tweet);
        double euclidean = otherFeature.euclidean(newsSentence, tweet);
        //Coefficient features
        double matching_coefficient = coeffi.Matching_coefficient(newsSentence, tweet);
        double dice_coefficient = coeffi.Dice_coefficient(newsSentence, tweet);
        double jarccard_coefficient = coeffi.Jaccard_coefficient(newsSentence, tweet);

        //Jaro distance
        double jaro_distance = jaro.proximity(newsSentence, tweet);
        //SmithWaterman value		
        double smith_waterman = (double) sw.computeSmithWaterman();
        //DamerauLevenshtein value
        double damerrau_Levenshtein = (double) dl.getSimilarity();

    }
}
