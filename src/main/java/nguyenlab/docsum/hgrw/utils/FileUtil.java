package nguyenlab.docsum.hgrw.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import nguyenlab.docsum.hgrw.entities.Document;
import nguyenlab.docsum.hgrw.entities.Sentence;

public class FileUtil {

    private String stopword_File = "stopwords_en.txt";
    private String regexspace = " ";
    private String regextab = "\t";

    public List<String> readFileString(String file) throws IOException {
        List<String> array = new ArrayList<String>();
        FileReader read = new FileReader(file);
        File f = new File(file);
        if (!f.exists()) {
            read.close();
            return array;
        }
        BufferedReader in = new BufferedReader(read);
        String s = null;

        while ((s = in.readLine()) != null) {
            array.add(s);
        }
        in.close();
        return array;
    }

    public List<String> readFileFile(File f) throws IOException {
        List<String> array = new ArrayList<String>();
        FileReader read = new FileReader(f);
        BufferedReader in = new BufferedReader(read);
        String s = null;

        while ((s = in.readLine()) != null) {
            array.add(s);
        }
        in.close();
        return array;
    }

    public List<Document> readFiles(File[] files) throws IOException {
        List<Document> docs = new ArrayList<Document>();
        for (File f : files) {
            Document d = readFileString(f);
            docs.add(d);
        }
        return docs;
    }

    public List<Document> readFiles(List<File> files) throws IOException {
        List<Document> docs = new ArrayList<Document>();
        for (File f : files) {
            Document d = readFileString(f);
            docs.add(d);
        }
        return docs;
    }

    /**
     * *
     * Reading a document
     *
     * @param file
     * @return the content of a document
     * @throws IOException
     */
    public Document readFileString(File f) throws IOException {
        FileReader read = new FileReader(f);
        BufferedReader in = new BufferedReader(read);
        String s = null;
        // Reading title
        String title = in.readLine();
        List<String> summary = new ArrayList<String>();
        // Read highlight
        in.readLine();
        in.readLine();
        while ((s = in.readLine()) != null) {
            if (s.length() == 0) {
                break;
            }
            if (s.length() != 0) {
                summary.add(s);
            }
        }

        in.readLine();// Move the point to 2 line
        List<String> sentence = new ArrayList<String>();
        // Read sentences
        while ((s = in.readLine()) != null) {
            if (s.length() == 0) {
                break;
            }
            String[] arr = s.split(regextab);
            if (arr.length != 0) {
                sentence.add(arr[0]);
            }
        }

        in.readLine();// Move the point to 2 line
        List<String> comment = new ArrayList<String>();
        // Read comments
        while ((s = in.readLine()) != null) {
            if (s.length() == 0) {
                continue;
            }
            String[] arr = s.split(regextab);
            if (arr.length != 0) {
                comment.add(arr[0]);
            }
        }

        Document d = new Document(title, summary, sentence, comment);

        in.close();
        return d;
    }

    public File[] listFile(String folder) {
        File f = new File(folder);
        File[] files = f.listFiles();
        return files;
    }

    public void writeData2File(String filename, List<String> data)
            throws FileNotFoundException {
        PrintWriter out = new PrintWriter(filename);
        for (String s : data) {
            out.write(s + "\n");
            out.flush();
        }
        out.close();
    }

    public void writeData2File(List<String> data, String name, String folder) throws FileNotFoundException {
        PrintWriter out = new PrintWriter(folder + "/" + name);
        for (String s : data) {
            System.out.println(s);
            out.write(s + ".\n");
            out.flush();

        }
        out.close();
    }

    // writing data for RTE
    public void writeRTEData2File(List<String> highlight,
            List<String> ran_sentences, List<String> ran_tweets, int i,
            String name, String hilightfolder, String sumfolder,
            String commentfolder) throws FileNotFoundException {
        name = name.replace(",", "");
        PrintWriter out_highlight = new PrintWriter(hilightfolder + "task" + i
                + "_reference " + name);
        PrintWriter out_doc_sum = new PrintWriter(sumfolder + "task" + i
                + "_docSum " + name);
        PrintWriter out_tweet_sum = new PrintWriter(commentfolder + "task" + i
                + "_tweetSum " + name);

        for (String h : highlight) {
            out_highlight.write(h + "\n");
            out_highlight.flush();
        }
        out_highlight.close();
        //out_doc_sum.write(ran_sentences.size() + "\n");
        for (String s : ran_sentences) {
            System.out.println(s);
            String[] arr = s.split("\t");
            //out_doc_sum.write(s + ".\n");
            out_doc_sum.write(arr[0] + ".\n");
            out_doc_sum.flush();

        }
        out_doc_sum.close();
        //out_tweet_sum.write(ran_tweets.size() + "\n");
        for (String t : ran_tweets) {
            System.out.println(t);
            String[] arr = t.split("\t");
            //out_doc_sum.write(s + ".\n");
            out_tweet_sum.write(arr[0] + ".\n");
            //out_tweet_sum.write(t + ".\n");
            out_tweet_sum.flush();
        }
        out_tweet_sum.close();
    }

    // Read file
    public List<List<String>> readFileColing(String file) throws IOException {
        List<List<String>> array = new ArrayList<List<String>>();
        FileReader read = new FileReader(file);
        File f = new File(file);
        if (!f.exists()) {
            read.close();
            return array;
        }
        BufferedReader in = new BufferedReader(read);
        in.readLine();
        in.readLine();
        in.readLine();
        String s = null;
        List<String> tmp = new ArrayList<String>();
        // Read highlight
        while ((s = in.readLine()) != null) {
            if (s.length() != 0) {
                tmp.add(s);
            }
            if (s.length() == 0) {
                break;
            }
        }
        array.add(tmp);
        s = in.readLine();// Move the point to 2 line
        tmp = new ArrayList<String>();
        // Read sentences
        while ((s = in.readLine()) != null) {
            if (s.length() != 0) {
                tmp.add(s);
            }
            if (s.length() == 0) {
                break;
            }
        }
        array.add(tmp);
        in.readLine();// Move the point to 2 line
        tmp = new ArrayList<String>();
        // Read tweets
        while ((s = in.readLine()) != null) {
            if (s.length() != 0) {
                tmp.add(s);
            }
        }
        array.add(tmp);
        in.close();
        return array;
    }

    // Read file
    public List<List<String>> readFileColing(File file) throws IOException {
        List<List<String>> array = new ArrayList<>();
        FileReader read = new FileReader(file);
        if (!file.exists()) {
            return array;
        }
        BufferedReader in = new BufferedReader(read);
        in.readLine();
        in.readLine();
        in.readLine();
        String s = null;
        List<String> tmp = new ArrayList<>();
        // Read highlight
        while ((s = in.readLine()) != null) {
            if (s.length() != 0) {
                tmp.add(s);
            }
            if (s.length() == 0) {
                break;
            }
        }
        array.add(tmp);
        s = in.readLine();// Move the point to 2 line
        tmp = new ArrayList<>();
        // Read sentences
        while ((s = in.readLine()) != null) {
            if (s.length() != 0) {
                tmp.add(s);
            }
            if (s.length() == 0) {
                break;
            }
        }
        array.add(tmp);
        in.readLine();// Move the point to 2 line
        tmp = new ArrayList<>();
        // Read tweets
        while ((s = in.readLine()) != null) {
            if (s.length() != 0) {
                tmp.add(s);
            }
        }
        array.add(tmp);
        in.close();
        return array;
    }

    public List<File> get9FoldFile(File[] trainingFile, File[] oneFoldFile) {
        List<File> nineFold = new ArrayList<File>();
        int size = trainingFile.length;
        boolean[] flags = new boolean[size];
        for (int i = 0; i < size; i++) {
            flags[i] = true;
        }

        for (int i = 0; i < size; i++) {
            String nameAll = trainingFile[i].getName();
            for (int j = 0; j < oneFoldFile.length; j++) {
                String nameOneFold = oneFoldFile[j].getName();
                if (nameAll.equals(nameOneFold)) {
                    flags[i] = false;
                }
            }
        }

        for (int i = 0; i < size; i++) {
            if (flags[i] == true) {
                nineFold.add(trainingFile[i]);
            }
        }
        return nineFold;
    }

    public void saveWord(Map<String, Integer> word, String filename) throws FileNotFoundException {
        PrintWriter out = new PrintWriter(filename);
        Set<String> keys = word.keySet();
        for (String k : keys) {
            int value = word.get(k);
            out.write(k + "\t" + value + "\n");
            out.flush();
        }
        out.close();
    }

    public boolean deleteFile(String filename) {
        File folder = new File(filename);
        boolean flag = false;
        if (folder.exists()) {
            flag = folder.delete();
        }
        return flag;
    }

    public boolean checkExistingFile(String filename) {
        File folder = new File(filename);
        if (folder.exists()) {
            return true;
        } else {
            return false;
        }
    }

    public Map<String, Integer> readWordFileMap(String filename) throws IOException {
        Map<String, Integer> result = new HashMap<String, Integer>();
        FileReader read = new FileReader(filename);
        BufferedReader in = new BufferedReader(read);
        String s = "";
        while ((s = in.readLine()) != null) {
            String[] arr = s.split("\t");
            if (arr.length >= 2) {
                String key = arr[0];
                int value = Integer.parseInt(arr[1]);
                result.put(key, value);
            }
        }
        in.close();
        return result;
    }

    public Map<String, Integer> readVocab(String filename) throws IOException {
        Map<String, Integer> result = new HashMap<String, Integer>();
        FileReader read = new FileReader(filename);
        BufferedReader in = new BufferedReader(read);
        String s = "";
        while ((s = in.readLine()) != null) {
            String[] arr = s.toLowerCase().split("\\s+");
            if (arr.length >= 2) {
                String key = arr[0];
                int value = Integer.parseInt(arr[1]);
                result.put(key, value);
            }
        }
        in.close();
        return result;
    }

    public List<String> removeStopWord(String sent) throws IOException {
        sent = sent.replace(",", "");
        sent = sent.replace(".", "");
        List<String> words = new ArrayList<String>();
        List<String> stop_words = readFileString(stopword_File);
        String[] word_sent = sent.split(regexspace);
        for (String w : word_sent) {
            if (!stop_words.contains(w.toLowerCase())) {
                words.add(w);
            }
        }
        return words;
    }

    public Map<String, Double> readFileTopicWord(String file) throws IOException {
        System.out.println(file);
        List<String> array = new ArrayList<String>();
        Map<String, Double> topic_words = new HashMap<String, Double>();
        FileReader read = new FileReader(file);
        File f = new File(file);
        if (!f.exists()) {
            read.close();
            return topic_words;
        }
        BufferedReader in = new BufferedReader(read);
        in.readLine();//read the first line
        String s = null;
        while ((s = in.readLine()) != null) {
            //System.out.println(s);
            array.add(s);
        }
        in.close();

        //Remove stop words
        List<String> stop_words = readFileString(stopword_File);
        System.out.println(stop_words.size());
        for (String w : array) {
            w = w.replace(".", "");
            w = w.replace(",", "");
            w = w.trim();
            String[] parts = w.split(regexspace);
            if (parts.length >= 2) {
                String tmp = parts[0];
                if (!stop_words.contains(tmp.toLowerCase())) {
                    System.out.println(tmp);
                    topic_words.put(tmp.toLowerCase(), Double.parseDouble(parts[1]));
                }
            }
        }
        System.out.println(topic_words.size());
        return topic_words;
    }

    //Read file --> a list of sentences
    public List<Sentence> readFileSent(String file) throws IOException {
        List<Sentence> array = new ArrayList<>();
        FileReader read = new FileReader(file);
        File f = new File(file);
        if (!f.exists()) {
            read.close();
            return array;
        }
        BufferedReader in = new BufferedReader(read);
        String s = null;
        Sentence sent = null;
        while ((s = in.readLine()) != null) {
            String[] arr = s.split(regexspace);
            if (arr.length >= 2) {
                sent = new Sentence(arr[0], Double.parseDouble(arr[1]));
                array.add(sent);
            }
        }
        in.close();
        return array;
    }

    public void writeData2FileLDARTE(String folder, String filename, List<Sentence> list_sum) throws FileNotFoundException {
        PrintWriter out = new PrintWriter(folder + "/" + filename);
        int i = 0;
        for (Sentence s : list_sum) {
            if (i > 3) {
                break;
            }
            String sent = s.getSentence();
            double value = s.getScore();
            //out.write(sent + "\t" + value + "\n");
            out.write(sent + "\n");
            out.flush();
            i++;
        }
        out.close();
    }
}
