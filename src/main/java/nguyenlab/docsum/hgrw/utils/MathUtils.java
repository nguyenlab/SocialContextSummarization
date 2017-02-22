/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nguyenlab.docsum.hgrw.utils;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author TRAN
 */
public class MathUtils {

    public static double innerProduct(Map<?, Double> a, Map<?, Double> b) {
        if (a.size() <= b.size()) {
            double prod = 0;
            for (Object k1 : a.keySet()) {
                Double v1 = a.get(k1);
                Double v2 = b.get(k1);
                if (v2 != null) {
                    prod += v1 * v2;
                }
            }
            return prod;
        } else {
            return innerProduct(b, a);
        }
    }

    public static <K> void softmax(Map<K, Double> a) {
        if (a.isEmpty()) {
            return;
        }
        Map<K, Double> b = new HashMap<>();
        double esum = 0;
        for (K key : a.keySet()) {
            esum += Math.exp(a.get(key));
            b.put(key, esum);
        }
        if (esum == 0) {
            return;
        }

        for (K key : b.keySet()) {
            a.put(key, b.get(key) / esum);
        }
    }
}
