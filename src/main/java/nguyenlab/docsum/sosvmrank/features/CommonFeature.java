/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nguyenlab.docsum.sosvmrank.features;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import nguyenlab.docsum.l2rccf.utils.PreProcessing;

public class CommonFeature {

    PreProcessing p = new PreProcessing();

    //sosvmrank
    public double localLDAScore(String instance, Map<String, Double> lda) throws IOException {
        double lda_score = 0;
        List<String> lstWord = p.removingStopWord(instance.toLowerCase());
        try {
            Set<String> keys = lda.keySet();
            for (String w : lstWord) {
                if (keys.contains(w)) {
                    lda_score += lda.get(w);
                }
            }
        } catch (Exception ex) {
            return 0;
        }
        return lda_score / lstWord.size();
    }
}
