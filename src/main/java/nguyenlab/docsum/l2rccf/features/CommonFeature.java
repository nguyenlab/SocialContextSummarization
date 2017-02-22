package nguyenlab.docsum.l2rccf.features;

import java.io.IOException;
import java.util.List;

import nguyenlab.docsum.l2rccf.utils.PreProcessing;
import nguyenlab.docsum.l2rccf.utils.Similarity;

public class CommonFeature {

    String space = "\\s+";
    Similarity sim = new Similarity();
    PreProcessing p = new PreProcessing();

    public float rougeP(String instance, String ref) {
        float rougeP = 0;
        String[] arrR = ref.split(space);
        int match = countWord(instance, ref);
        rougeP = (float) match / arrR.length;
        return rougeP;
    }

    public float maxRougeP(String instance, List<String> refs) {
        float maxRougeP = 0;
        for (String ref : refs) {
            float rougeP = rougeP(instance, ref);
            if (maxRougeP < rougeP) {
                maxRougeP = rougeP;
            }
        }
        return (float) maxRougeP / refs.size();
    }

    public float rougeR(String instance, String ref) {
        float rougeR = 0;
        String[] arrI = instance.split(space);
        int match = countWord(instance, ref);
        rougeR = (float) match / arrI.length;
        return rougeR;
    }

    public float maxRougeR(String instance, List<String> refs) {
        float maxRougeR = 0;
        for (String ref : refs) {
            float rougeR = rougeR(instance, ref);
            if (maxRougeR < rougeR) {
                maxRougeR = rougeR;
            }
        }
        return (float) maxRougeR / refs.size();
    }

    public float maxRougeF(String instance, List<String> refs) {
        float maxRougeF = 0;
        for (String ref : refs) {
            float rougeP = rougeP(instance, ref);
            float rougeR = rougeR(instance, ref);
            float rougeF = rougeF(rougeP, rougeR);
            if (maxRougeF < rougeF) {
                maxRougeF = rougeF;
            }
        }
        return maxRougeF / refs.size();
    }

    public float rougeF(float rougeP, float rougeR) {
        return (float) 2 * rougeR * rougeP / (rougeR + rougeP);
    }

    private int countWord(String s1, String s2) {
        int count = 0;
        String[] arr1 = s1.toLowerCase().split(space);
        String[] arr2 = s1.toLowerCase().split(space);
        for (String w1 : arr1) {
            for (String w2 : arr2) {
                if (w1.equals(w2)) {
                    count++;
                }
            }
        }
        return count;
    }

    public float sentLengthBefore(String s, List<String> sents) throws IOException {
        int index = sents.indexOf(s);
        if (index == 0) {
            return 0;
        }
        String sbefore = sents.get(index - 1);
        List<String> words = p.removingStopWord(sbefore);
        return (float) words.size() / sents.size();
    }

    public float sentLength(String intance, List<String> instances) throws IOException {
        List<String> words = p.removingStopWord(intance);
        return (float) words.size() / instances.size();
    }

    public float sentLengthAfter(String instance, List<String> instances) throws IOException {
        int index = instances.indexOf(instance);
        if (index == instances.size() - 1) {
            return 0;
        }
        String safter = instances.get(index + 1);
        List<String> words = p.removingStopWord(safter);
        return (float) words.size() / instances.size();
    }

    public double cosineBefore(String instance, List<String> instances) {
        int index = instances.indexOf(instance);
        if (index == 0) {
            return 0;
        }
        String sbefore = instances.get(index - 1);
        return sim.cosinSimilarity(instance, sbefore);
    }

    public double cosineAfter(String instance, List<String> instances) {
        int index = instances.indexOf(instance);
        if (index == instances.size() - 1) {
            return 0;
        }
        String safter = instances.get(index + 1);
        return sim.cosinSimilarity(instance, safter);
    }

    public double titleSim(String instance, String title) {
        String[] arrI = instance.toLowerCase().split(space);
        String[] arrT = title.toLowerCase().split(space);
        int sizeT = arrT.length;

        int count = 0;
        for (String w : arrI) {
            if (title.contains(w)) {
                count++;
            }
        }

        return count / sizeT;
    }

    public double titleSimCosine(String instance, String title) {
        double cosine = sim.cosinSimilarity(instance, title);
        return cosine;
    }

    public double numberStopWord(String instance) throws IOException {
        double num = 0;
        String[] lstW1 = instance.split(space);
        List<String> lstW2 = p.removingStopWord(instance);
        int n = lstW1.length - lstW2.size();
        num = (double) n / lstW1.length;
        return num;
    }
}
