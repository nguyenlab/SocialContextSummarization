/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nguyenlab.docsum.tools;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SimpleWord2VecDouble implements Serializable {

    private Map<String, Integer> indices;
    private String[] words;
    /**
     * words.length*vecterSize + dump values
     */
    private double[] wordvectors;
    public static final String UNK = "</s>";
    private int vectorSize;
    private int wordNums;

    public static class Loader {

        public static SimpleWord2VecDouble loadGoogleModel(String modelPath, boolean binary) throws IOException {
            return loadGoogleModel(new File(modelPath), binary);
        }

        public static SimpleWord2VecDouble loadGoogleModel(File modelFile, boolean binary) throws IOException {
            InputStream inputStream = null;
            try {
                inputStream = new FileInputStream(modelFile);
                return loadGoogleModel(inputStream, binary);
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
            }
        }

        public static SimpleWord2VecDouble loadGoogleModel(InputStream inputStream, boolean binary) throws IOException {
            Map<String, Integer> indices;
            String[] words;
            double[] wordvectors;
            int numWords;
            int vectorSize;
            int wordCount = 0;
            if (binary) {
                DataInputStream dis = null;
                double len;
                double vector;

                try {
                    dis = new DataInputStream(new BufferedInputStream(inputStream));
                    numWords = Integer.parseInt(readString(dis));
                    vectorSize = Integer.parseInt(readString(dis));
                    String word;
                    indices = new HashMap<>(numWords);
                    words = new String[numWords];
                    wordvectors = new double[numWords * vectorSize];
                    for (int i = 0; i < numWords; i++) {
                        //read word
                        word = readString(dis).trim();
                        len = 0;
                        for (int j = 0; j < vectorSize; j++) {
                            //read each element of vector
                            vector = readFloat(dis);
                            //for normalization
                            len += vector * vector;
                            wordvectors[wordCount * vectorSize + j] = vector;
                        }
                        //normalization
                        len = Math.sqrt(len);
                        for (int j = 0; j < vectorSize; j++) {
                            wordvectors[wordCount * vectorSize + j] /= len;
                        }
                        //add word 
                        if (!indices.containsKey(word) && word.length() > 0) {
                            words[wordCount] = (word);
                            indices.put(word, wordCount);
                            wordCount++;
                        }
                        //ignore once
                        dis.read();
                    }
                } finally {
                    if (dis != null) {
                        dis.close();
                    }
                }

            } else {
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line = reader.readLine();
                String[] initial = line.split(" ");
                numWords = Integer.parseInt(initial[0]);
                vectorSize = Integer.parseInt(initial[1]);
                indices = new HashMap<>(numWords);
                words = new String[numWords];
                wordvectors = new double[numWords * vectorSize];
                while ((line = reader.readLine()) != null) {
                    String[] split = line.split(" ");
                    String word = split[0];
                    for (int i = 1; i < split.length; i++) {
                        wordvectors[wordCount * vectorSize + i - 1] = Float.parseFloat(split[i]);
                    }
                    //add word 
                    if (!indices.containsKey(word) && word.length() > 0) {
                        words[wordCount] = (word);
                        indices.put(word, wordCount);
                        wordCount++;
                    }
                }
            }

            SimpleWord2VecDouble model = new SimpleWord2VecDouble();
            model.indices = indices;
            model.vectorSize = vectorSize;
            model.words = new String[wordCount];
            System.arraycopy(words, 0, model.words, 0, wordCount);
            model.wordvectors = wordvectors;
//        System.arraycopy(wordvectors, 0, model.wordvectors, 0, wordCount * vectorSize);
            model.wordNums = wordCount;

            return model;
        }

        private static String readString(DataInputStream dis) throws IOException {
            final int MAX_SIZE = 256;
            byte[] bytes = new byte[MAX_SIZE];
            byte b = dis.readByte();
            int i = -1;
            StringBuilder sb = new StringBuilder();
            while (b != 32 && b != 10) {
                i++;
                bytes[i] = b;
                b = dis.readByte();
                if (i == 49) {
                    sb.append(new String(bytes));
                    i = -1;
                    bytes = new byte[MAX_SIZE];
                }
            }
            sb.append(new String(bytes, 0, i + 1));
            return sb.toString();
        }

        private static float readFloat(InputStream is) throws IOException {
            byte[] bytes = new byte[4];
            is.read(bytes);
            return getFloat(bytes);
        }

        private static float getFloat(byte[] b) {
            int accum = 0;
            accum = accum | (b[0] & 0xff) << 0;
            accum = accum | (b[1] & 0xff) << 8;
            accum = accum | (b[2] & 0xff) << 16;
            accum = accum | (b[3] & 0xff) << 24;
            return Float.intBitsToFloat(accum);
        }

    }

    protected SimpleWord2VecDouble() {
    }

    public double[] vectorOf(String word) {
        double[] vector = new double[vectorSize];
        int index = indexOf(word);
        if (index < 0) {
            index = indices.get(UNK);
        }
        System.arraycopy(wordvectors, index * vectorSize, vector, 0, vectorSize);
        return vector;
    }

    public int vectorSize() {
        return vectorSize;
    }

    public List<String> wordList() {
        return Arrays.asList(words);
    }

    public int wordNums() {
        return wordNums;
    }

    public boolean hasWord(String word) {
        return indexOf(word) >= 0;
    }

    public int indexOf(String word) {
        Integer index = indices.get(word);
        return index == null ? -1 : index;
    }

    public List<String> similarWordsOf(Collection<String> words, int n) {
        List<String> similarWords = new ArrayList<>();
        Map<String, List<String>> cache = new HashMap<>();
        for (String word : words) {
            List<String> similarWords2 = cache.get(word);
            if (similarWords2 == null) {
                cache.put(word, similarWords2 = similarWordsOf(word, n));
            }
            similarWords.addAll(similarWords2);
        }
        return similarWords;
    }

    public List<String> similarWordsOf(String word, int n) {
        /**
         * index of input word
         */
        Integer wordIndex0 = indices.get(word);
        /**
         * not found word return empty list
         */
        if (wordIndex0 == null) {
            return new ArrayList<>();
        }
        /**
         * offset of input word's vector
         */
        int offset0 = wordIndex0 * vectorSize;
        /**
         * hold n max similarity of words and indices
         */
        double[] maxSimilarities = new double[n];
        int[] maxIndices = new int[n];
        /**
         * hold the index of min value of maxSimilarities array
         */
        int minJ = 0;
        /**
         * count the number of elements in the maxSimilarities array
         */
        int count = 0;

        /**
         * iterate on word list
         */
        int wordIndex = 0;
        /**
         * fill the maxSimilarities array with first n words
         */
        if (wordIndex0 >= n) {
            for (; wordIndex < n; wordIndex++, count++) {
                double sim = innerProduct(wordvectors, offset0, wordvectors, vectorSize * wordIndex, vectorSize);
                maxIndices[count] = wordIndex;
                maxSimilarities[count] = sim;
                if (maxSimilarities[count] < maxSimilarities[minJ]) {
                    minJ = wordIndex;
                }
            }
            for (; wordIndex < wordIndex0; wordIndex++) {
                double sim = innerProduct(wordvectors, offset0, wordvectors, vectorSize * wordIndex, vectorSize);
                if (sim > maxSimilarities[minJ]) {
                    maxIndices[minJ] = wordIndex;
                    maxSimilarities[minJ] = sim;
                    for (int j = 0; j < n; j++) {
                        if (maxSimilarities[j] < maxSimilarities[minJ]) {
                            minJ = j;
                        }
                    }
                }
            }
            wordIndex++;

        } else {
            for (; wordIndex < wordIndex0; wordIndex++) {
                double sim = innerProduct(wordvectors, offset0, wordvectors, vectorSize * wordIndex, vectorSize);
                if (sim > maxSimilarities[minJ]) {
                    maxIndices[minJ] = wordIndex;
                    maxSimilarities[minJ] = sim;
                    for (int j = 0; j < n; j++) {
                        if (maxSimilarities[j] < maxSimilarities[minJ]) {
                            minJ = j;
                        }
                    }
                }
            }
            wordIndex++;
            for (; wordIndex <= n; wordIndex++, count++) {
                double sim = innerProduct(wordvectors, offset0, wordvectors, vectorSize * wordIndex, vectorSize);
                maxIndices[count] = wordIndex;
                maxSimilarities[count] = sim;
                if (maxSimilarities[count] < maxSimilarities[minJ]) {
                    minJ = count;
                }
            }
        }
        /*end filling*/
        /**
         * continue with the rest of word list
         */
        for (; wordIndex < wordNums(); wordIndex++) {
            double sim = innerProduct(wordvectors, offset0, wordvectors, vectorSize * wordIndex, vectorSize);
            if (sim > maxSimilarities[minJ]) {
                maxIndices[minJ] = wordIndex;
                maxSimilarities[minJ] = sim;
                for (int j = 0; j < n; j++) {
                    if (maxSimilarities[j] < maxSimilarities[minJ]) {
                        minJ = j;
                    }
                }
            }
        }

        /**
         * sort the output in descending order use bubble sort, expect n to be
         * small
         */
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (maxSimilarities[i] < maxSimilarities[j]) {
                    double tmp = maxSimilarities[i];
                    maxSimilarities[i] = maxSimilarities[j];
                    maxSimilarities[j] = tmp;
                    int tmp2 = maxIndices[i];
                    maxIndices[i] = maxIndices[j];
                    maxIndices[j] = tmp2;
                }
            }
        }

        /**
         * create result list
         */
        List<String> similarWords = new ArrayList<>();
        for (int maxIndex : maxIndices) {
            similarWords.add(words[maxIndex]);
        }

        return similarWords;
    }

    public double similarity(String word1, String word2) {
        int index1 = indexOf(word1);
        if (index1 < 0) {
            index1 = indexOf(UNK);
        }
        int index2 = indexOf(word2);
        if (index2 < 0) {
            index2 = indexOf(UNK);
        }

        return innerProduct(wordvectors, index1 * vectorSize, wordvectors, index2 * vectorSize, vectorSize);
    }

    /**
     * find w22 = w12 - w11 + w21
     *
     * @param w11
     * @param w12
     * @param w21
     * @return
     */
    public String answer(String w11, String w12, String w21) {

        int index11 = indexOf(w11);
        int index12 = indexOf(w12);
        int index21 = indexOf(w21);

        if (index11 < 0) {
            index11 = indexOf(UNK);
        }
        if (index12 < 0) {
            index12 = indexOf(UNK);
        }
        if (index21 < 0) {
            index21 = indexOf(UNK);
        }

        int offset11 = index11 * vectorSize;
        int offset12 = index12 * vectorSize;
        int offset21 = index21 * vectorSize;

        double[] right = new double[vectorSize];
        double len = 0;
        for (int i = 0; i < vectorSize; i++) {
            right[i] = wordvectors[offset12 + i] - wordvectors[offset11 + i] + wordvectors[offset21 + i];
            len += right[i] * right[i];
        }
        len = Math.sqrt(len);
        for (int i = 0; i < vectorSize; i++) {
            right[i] /= len;
        }
        int maxI = 0;
        double max = 0;
        for (int i = 0; i < wordNums(); i++) {
            double sim = innerProduct(right, 0, wordvectors, i * vectorSize, vectorSize);
            if (sim > max) {
                maxI = i;
                max = sim;
            }
        }
        return words[maxI];
    }

    private static double innerProduct(double[] v1, int offset1, double[] v2, int offset2, int length) {
        double inner = 0;
        for (int i = 0; i < length; i++) {
            inner += v1[offset1 + i] * v2[offset2 + i];
        }
        return inner;
    }
}
