package nguyenlab.docsum.sosvmrank.main;

import nguyenlab.docsum.sosvmrank.features.CrossFeature;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import nguyenlab.docsum.l2rccf.features.CommentFeature;
import nguyenlab.docsum.l2rccf.features.CommonFeature;
import nguyenlab.docsum.l2rccf.features.SentenceFeature;
import nguyenlab.docsum.l2rccf.entities.Word;
import nguyenlab.docsum.l2rccf.utils.FileUtil;
import nguyenlab.docsum.l2rccf.utils.PreProcessing;
import nguyenlab.docsum.l2rccf.utils.WordUtil;

public class Example {

    String regex = "\t";

    FileUtil fileUtil = null;
    SentenceFeature sentFeature = null;
    CommentFeature commentFeature = null;
    CommonFeature comFeature = null;
    CrossFeature crossFeature = null;
    PreProcessing p = null;
    WordUtil wordUtil = null;
    boolean letor = false;
    boolean mustHaveRelDoc = false;
    int k = 10, topNumEntity = 4, topNumWord = 20;

    String[] ners = {"LOCATION", "PERSON", "ORGANIZATION", "MONEY", "PERCENT", "DATE", "TIME"};

    public Example() throws Exception {
        fileUtil = new FileUtil();
        wordUtil = new WordUtil();
        sentFeature = new SentenceFeature();
        commentFeature = new CommentFeature();
        comFeature = new CommonFeature();
        crossFeature = new CrossFeature();
        crossFeature = new CrossFeature();
        p = new PreProcessing();
    }

    private void oneFileFeatureGeneratingSentTrain(String title, List<String> refs, List<String> sents,
            List<String> comments, String filename, int fileCount, Map<String, Double> localLDA, Map<String, Double> auxLDA) throws IOException {
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(filename, true)));

        Map<String, Integer> uniGrams_Sent = wordUtil.getUniGramOneDocHashMap(sents);
        Map<String, Integer> biGrams_Sent = wordUtil.getBiGramOneDocHashMap(sents);

        Map<String, Integer> uniGrams_Comment = wordUtil.getUniGramOneDocHashMap(comments);
        Map<String, Double> hashTFIDF_Comment = wordUtil.getTFIDFHashMap(uniGrams_Comment, comments);

        // Feature
        for (String s : sents) {
            float maxRougeFOrder = comFeature.maxRougeF(s, refs);

            //local sentence feature
            int isFirst = sentFeature.isFirst(s, sents);
            float pos = sentFeature.sentPos(s, sents);


            double titleSim = comFeature.titleSimCosine(s, title);
            float impUnigram = sentFeature.importanceUniGram(s, uniGrams_Sent);
            float impBigram = sentFeature.importanceBiGram(s, biGrams_Sent);
            //double numStopWord = comFeature.numberStopWord(s);//new

            //cross features
            //System.out.println(s + "\t" + comments.size());
            double maxCosine = crossFeature.maxCosine(s, comments);
            float maxRougeF = crossFeature.maxRougeF(s, comments);
            float maxRougeP = crossFeature.maxRougeP(s, comments);
            float maxRougeR = crossFeature.maxRougeR(s, comments);
            float simUniGram = crossFeature.simUniGram(s, uniGrams_Comment);

            List<Word> topCommentEntity = crossFeature.getTopEntity(comments, topNumEntity);
            float topEntityCount = crossFeature.countTopEntity(s, topCommentEntity);
            float topEntitySim = crossFeature.simTopEntity(s, topCommentEntity);
            float topUniGramCount = crossFeature.countTopUniGram(s, uniGrams_Comment, topNumWord);
            float simTopUniGram = crossFeature.simTopUniGram(s, uniGrams_Comment, topNumWord);
            float simUniTFIDF = crossFeature.simUniTFIDF(s, hashTFIDF_Comment);

        
            //System.out.println(str);
            String str = maxRougeFOrder + " qid:" + fileCount + " 1:" + isFirst
                    + " 2:" + pos + " 3:" + titleSim
                    + " 4:" + impUnigram + " 5:" + impBigram + " 6:" + maxCosine + " 7:" + maxRougeF
                    + " 8:" + maxRougeP + " 9:" + maxRougeR + " 10:" + simUniGram + " 11:" + topEntityCount
                    + " 12:" + topEntitySim + " 13:" + topUniGramCount + " 14:" + simTopUniGram + " 15:" + simUniTFIDF;

            out.write(str + "\n");
            out.flush();
        }
        out.close();
    }

    private void oneFileFeatureGeneratingSentTest(String title, List<String> refs, List<String> sents,
            List<String> comments, String filename, Map<String, Double> localLDA, Map<String, Double> auxLDA) throws IOException {
        PrintWriter out = new PrintWriter(filename);

        Map<String, Integer> uniGrams_Sent = wordUtil.getUniGramOneDocHashMap(sents);
        Map<String, Integer> biGrams_Sent = wordUtil.getBiGramOneDocHashMap(sents);

        Map<String, Integer> uniGrams_Comment = wordUtil.getUniGramOneDocHashMap(comments);
        Map<String, Double> hashTFIDF_Comment = wordUtil.getTFIDFHashMap(uniGrams_Comment, comments);

        // Feature
        int count = 0;
        for (String s : sents) {
            float maxRougeFOrder = comFeature.maxRougeF(s, refs);

            //local sentence feature
            int isFirst = sentFeature.isFirst(s, sents);
            float pos = sentFeature.sentPos(s, sents);

            /*float sentLengthBefore = comFeature.sentLengthBefore(s, sents);//new
			float sentLength = comFeature.sentLength(s, sents);//new
			float sentLengthAfter = comFeature.sentLengthAfter(s, sents);//new
			
			double cosineBefore = comFeature.cosineBefore(s, sents);//new
			double cosineAfter = comFeature.cosineAfter(s, sents);//new
			double local_lda_score = comFeature.localLDAScore(s, localLDA);//new*/
            double titleSim = comFeature.titleSimCosine(s, title);
            float impUnigram = sentFeature.importanceUniGram(s, uniGrams_Sent);
            float impBigram = sentFeature.importanceBiGram(s, biGrams_Sent);
            //double numStopWord = comFeature.numberStopWord(s);//new

            //cross features
            double maxCosine = crossFeature.maxCosine(s, comments);
            float maxRougeF = crossFeature.maxRougeF(s, comments);
            float maxRougeP = crossFeature.maxRougeP(s, comments);
            float maxRougeR = crossFeature.maxRougeR(s, comments);
            float simUniGram = crossFeature.simUniGram(s, uniGrams_Comment);

            List<Word> topCommentEntity = crossFeature.getTopEntity(comments, topNumEntity);
            float topEntityCount = crossFeature.countTopEntity(s, topCommentEntity);
            float topEntitySim = crossFeature.simTopEntity(s, topCommentEntity);
            float topUniGramCount = crossFeature.countTopUniGram(s, uniGrams_Comment, topNumWord);
            float simTopUniGram = crossFeature.simTopUniGram(s, uniGrams_Comment, topNumWord);
            float simUniTFIDF = crossFeature.simUniTFIDF(s, hashTFIDF_Comment);

            //System.out.println(str);
            String str = maxRougeFOrder + " qid:" + count + " 1:" + isFirst
                    + " 2:" + pos + " 3:" + titleSim
                    + " 4:" + impUnigram + " 5:" + impBigram + " 6:" + maxCosine + " 7:" + maxRougeF
                    + " 8:" + maxRougeP + " 9:" + maxRougeR + " 10:" + simUniGram + " 11:" + topEntityCount
                    + " 12:" + topEntitySim + " 13:" + topUniGramCount + " 14:" + simTopUniGram + " 15:" + simUniTFIDF;

            out.write(str + "\n");
            out.flush();
            count++;
        }

        out.close();
    }

    private void oneFileFeatureGeneratingCommentTrain(String title, List<String> refs,
            List<String> sents, List<String> comments, String filename, int fileCount,
            Map<String, Double> docLDA, Map<String, Double> commentLDA) throws IOException {
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(filename, true)));

        Map<String, Integer> uniGrams_Comment = wordUtil.getUniGramOneDocHashMap(comments);
        Map<String, Integer> uniGrams_Sent = wordUtil.getUniGramOneDocHashMap(sents);

        Map<String, Double> hashTFIDF_Sent = wordUtil.getTFIDFHashMap(uniGrams_Sent, sents);

        for (String comment : comments) {
            float maxRougeFOrder = comFeature.maxRougeF(comment, refs);

            //comment local features
            double len = comFeature.sentLength(comment, comments);
            /*double lenbefore = comFeature.sentLengthBefore(comment, comments);//new
			double lenafter = comFeature.sentLengthAfter(comment, comments);//new
			double numStopWord = comFeature.numberStopWord(comment);//new */
            double titleSim = comFeature.titleSimCosine(comment, title);

            /*double cosineBefore = comFeature.cosineBefore(comment, comments);//new
			double cosineAfter = comFeature.cosineAfter(comment, comments);//new
			double local_lda_score = comFeature.localLDAScore(comment, commentLDA);//new */
            float htfidf = commentFeature.hTFIDF(comment, comments, uniGrams_Comment);
            float oov = commentFeature.OOV(comment);
            float oovPercentage = commentFeature.OOVPercentage(comment);

            List<String> lstNER = commentFeature.ner(comment);
            float nerCount = Float.parseFloat(lstNER.get(0));
            double loc = 0.0, person = 0.0, org = 0.0, money = 0.0, percent = 0.0, date = 0.0, time = 0.0;
            for (int i = 1; i < lstNER.size(); i++) {
                String entity = lstNER.get(i);
                if (entity.equals("LOCATION")) {
                    loc = 1.0;
                }
                if (entity.equals("PERSON")) {
                    person = 1.0;
                }
                if (entity.equals("ORGANIZATION")) {
                    org = 1.0;
                }
                if (entity.equals("MONEY")) {
                    money = 1.0;
                }
                if (entity.equals("PERCENT")) {
                    percent = 1.0;
                }
                if (entity.equals("DATE")) {
                    date = 1.0;
                }
                if (entity.equals("TIME")) {
                    time = 1.0;
                }
            }

            // cross features
            double maxCosine = crossFeature.maxCosine(comment, sents);
            float maxRougeF = crossFeature.maxRougeF(comment, sents);
            float maxRougeP = crossFeature.maxRougeP(comment, sents);
            float maxRougeR = crossFeature.maxRougeR(comment, sents);
            float simUniGram = crossFeature.simUniGram(comment, uniGrams_Sent);
            double maxSentR1Pos = crossFeature.maxSentPos(comment, sents);

            List<Word> topSentEntity = crossFeature.getTopEntity(sents, topNumEntity);
            float topEntityCount = crossFeature.countTopEntity(comment, topSentEntity);
            float topEntitySim = crossFeature.simTopEntity(comment, topSentEntity);
            float topUniGramCount = crossFeature.countTopUniGram(comment, uniGrams_Sent, topNumWord);
            float simTopUniGram = crossFeature.simTopUniGram(comment, uniGrams_Sent, topNumWord);
            float simUniTFIDF = crossFeature.simUniTFIDF(comment, hashTFIDF_Sent);

            float qualityUni = commentFeature.qualityUniGram(comment);
            float qualityBi = commentFeature.qualityBiGram(comment);

           
            //System.out.println(str);
            String str = maxRougeFOrder + " qid:" + fileCount + " 1:" + len + " 2:" + titleSim
                    + " 3:" + htfidf + " 4:" + oov + " 5:" + oovPercentage + " 6:" + nerCount + " 7:" + loc + " 8:" + person
                    + " 9:" + org + " 10:" + money + " 11:" + percent + " 12:" + date + " 13:" + time + " 14:" + maxCosine
                    + " 15:" + maxRougeF + " 16:" + maxRougeP + " 17:" + maxRougeR + " 18:" + simUniGram
                    + " 19:" + topEntityCount + " 20:" + topEntitySim + " 21:" + topUniGramCount + " 22:" + simTopUniGram
                    + " 23:" + simUniTFIDF + " 24:" + qualityUni + " 25:" + qualityBi + " 26:" + maxSentR1Pos;

            out.write(str + "\n");
            out.flush();
        }

        out.close();
    }

    private void oneFileFeatureGeneratingCommentTest(String title, List<String> refs, List<String> sents,
            List<String> comments, String filename, Map<String, Double> docLDA, Map<String, Double> commentLDA) throws IOException {
        PrintWriter out = new PrintWriter(filename);

        Map<String, Integer> uniGrams_Comment = wordUtil.getUniGramOneDocHashMap(comments);
        Map<String, Integer> uniGrams_Sent = wordUtil.getUniGramOneDocHashMap(sents);

        Map<String, Double> hashTFIDF_Sent = wordUtil.getTFIDFHashMap(uniGrams_Sent, sents);

        int count = 0;
        for (String comment : comments) {
            float maxRougeFOrder = comFeature.maxRougeF(comment, refs);

            //comment local features
            double len = comFeature.sentLength(comment, comments);
           
            double titleSim = comFeature.titleSimCosine(comment, title);

            float htfidf = commentFeature.hTFIDF(comment, comments, uniGrams_Comment);
            float oov = commentFeature.OOV(comment);
            float oovPercentage = commentFeature.OOVPercentage(comment);

            List<String> lstNER = commentFeature.ner(comment);
            float nerCount = Float.parseFloat(lstNER.get(0));
            double loc = 0.0, person = 0.0, org = 0.0, money = 0.0, percent = 0.0, date = 0.0, time = 0.0;
            for (int i = 1; i < lstNER.size(); i++) {
                String entity = lstNER.get(i);
                if (entity.equals("LOCATION")) {
                    loc = 1.0;
                }
                if (entity.equals("PERSON")) {
                    person = 1.0;
                }
                if (entity.equals("ORGANIZATION")) {
                    org = 1.0;
                }
                if (entity.equals("MONEY")) {
                    money = 1.0;
                }
                if (entity.equals("PERCENT")) {
                    percent = 1.0;
                }
                if (entity.equals("DATE")) {
                    date = 1.0;
                }
                if (entity.equals("TIME")) {
                    time = 1.0;
                }
            }

            // cross features
            double maxCosine = crossFeature.maxCosine(comment, sents);
            float maxRougeF = crossFeature.maxRougeF(comment, sents);
            float maxRougeP = crossFeature.maxRougeP(comment, sents);
            float maxRougeR = crossFeature.maxRougeR(comment, sents);
            float simUniGram = crossFeature.simUniGram(comment, uniGrams_Sent);
            double maxSentR1Pos = crossFeature.maxSentPos(comment, sents);

            List<Word> topSentEntity = crossFeature.getTopEntity(sents, topNumEntity);
            float topEntityCount = crossFeature.countTopEntity(comment, topSentEntity);
            float topEntitySim = crossFeature.simTopEntity(comment, topSentEntity);
            float topUniGramCount = crossFeature.countTopUniGram(comment, uniGrams_Sent, topNumWord);
            float simTopUniGram = crossFeature.simTopUniGram(comment, uniGrams_Sent, topNumWord);
            float simUniTFIDF = crossFeature.simUniTFIDF(comment, hashTFIDF_Sent);

            float qualityUni = commentFeature.qualityUniGram(comment);
            float qualityBi = commentFeature.qualityBiGram(comment);

        
            //System.out.println(str);
            String str = maxRougeFOrder + " qid:" + count + " 1:" + len + " 2:" + titleSim
                    + " 3:" + htfidf + " 4:" + oov + " 5:" + oovPercentage + " 6:" + nerCount + " 7:" + loc + " 8:" + person
                    + " 9:" + org + " 10:" + money + " 11:" + percent + " 12:" + date + " 13:" + time + " 14:" + maxCosine
                    + " 15:" + maxRougeF + " 16:" + maxRougeP + " 17:" + maxRougeR + " 18:" + simUniGram
                    + " 19:" + topEntityCount + " 20:" + topEntitySim + " 21:" + topUniGramCount + " 22:" + simTopUniGram
                    + " 23:" + simUniTFIDF + " 24:" + qualityUni + " 25:" + qualityBi + " 26:" + maxSentR1Pos;

            out.write(str + "\n");
            out.flush();
            count++;
        }
        out.close();
    }

}
