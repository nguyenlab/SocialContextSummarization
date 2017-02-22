package nguyenlab.docsum.crf.main;

import nguyenlab.docsum.crf.utils.Helper;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import edu.stanford.nlp.io.IOUtils;
import edu.stanford.nlp.pipeline.Annotation;
import nguyenlab.docsum.crf.entities.Doc;
import nguyenlab.docsum.crf.features.Feature;
import nguyenlab.docsum.crf.features.LabelFeature;
import nguyenlab.docsum.crf.features.basic.InTitleFeature;
import nguyenlab.docsum.crf.features.basic.IndicatorWordsFeature;
import nguyenlab.docsum.crf.features.basic.LengthFeature;
import nguyenlab.docsum.crf.features.basic.LogLikelihoodFeature;
import nguyenlab.docsum.crf.features.basic.PositionFeature;
import nguyenlab.docsum.crf.features.basic.Similarity2NeighboringSentencesFeature;
import nguyenlab.docsum.crf.features.basic.ThematicWordsFeature;
import nguyenlab.docsum.crf.features.basic.UpperCaseWordsFeature;
import nguyenlab.docsum.crf.features.complex.HITSScoresFeature;
import nguyenlab.docsum.crf.features.complex.LSAScoresFeature;
import nguyenlab.docsum.crf.utils.NLPUtils;
import nguyenlab.docsum.crf.utils.Object2Iterator;

public class Example {
    //	static int nThreads = 40;

    static final Logger logger = Logger.getLogger(Example.class.getName());

    public static void main(String[] args) throws Exception {
        exec(args[0]);
        // exec("./data/yahoonews-10fold-tagged");
    }

    @SuppressWarnings("unchecked")
    public static void exec(String dataDir) throws Exception {

        /**
         * feature output format: $ROOT/features/$FEATURE_NAME/$DOC_NAME
         */
        @SuppressWarnings("rawtypes")
        final List<Feature> features = Arrays.asList(new Feature[]{ //
            new PositionFeature(), //
            new LengthFeature(), //
            new LogLikelihoodFeature(), //
            new ThematicWordsFeature(), //
            new IndicatorWordsFeature(), //
            new UpperCaseWordsFeature(), //
            new Similarity2NeighboringSentencesFeature(-3), //
            new Similarity2NeighboringSentencesFeature(-2), //
            new Similarity2NeighboringSentencesFeature(-1), //
            new Similarity2NeighboringSentencesFeature(1), //
            new Similarity2NeighboringSentencesFeature(2), //
            new Similarity2NeighboringSentencesFeature(3), //
            new InTitleFeature(), //
            new LSAScoresFeature(), //
            new HITSScoresFeature(), //
            new LabelFeature(), //
        });

        logger.log(Level.INFO, "Features: \n{0}\n",
                String.join("\n", features.stream().map(e -> e.name()).collect(Collectors.toList())));
        /**
         * load data
         */
        List<List<Doc>> docComList = null;
        logger.log(Level.INFO, "Pre-processing data");

        File serFile = new File(dataDir + "/prepocessed-docs.ser.bin.gz");

        if (serFile.exists()) {
            logger.log(Level.INFO, "Loading existing data...");
            try {
                docComList = IOUtils.readObjectFromFile(serFile);
            } catch (Exception e1) {
                logger.log(Level.SEVERE, e1.getMessage(), e1);
            }
        }

        if (docComList == null) {
            File[] fileList = Helper.listDocComFiles(new File(dataDir, "folds"));
            docComList = loadDocCom(fileList);
        }

        try {
            IOUtils.writeObjectToFile(docComList, serFile);
            logger.log(Level.INFO, "Saved pre-processed data");
        } catch (IOException e1) {
            logger.log(Level.SEVERE, e1.getMessage(), e1);
        }

        logger.log(Level.INFO, "Extracting features");
        final File allFeatureDir = new File(dataDir, "features");
        allFeatureDir.mkdirs();

        AtomicInteger counter = new AtomicInteger(0);
        for (List<Doc> docCom : docComList) {
            docCom.parallelStream().forEach((doc) -> {

                for (@SuppressWarnings("rawtypes") Feature feature : features) {
                    File ftDir = new File(allFeatureDir, feature.name());
                    File ftFile = new File(ftDir, doc.getSource() + "." + doc.getTag());
                    if (ftFile.exists()) {
                        continue;
                    }

                    ftDir.mkdirs();

                    Object ft = null;

                    try {
                        ft = feature.extract(doc);
                    } catch (Exception ex) {
                        logger.log(Level.SEVERE, ex.getMessage(), ex);
                    }

                    try {
                        /**
                         * assure feature file completion
                         */
                        File ftFileTmp = new File(ftFile.getAbsolutePath() + ".tmp");
                        IOUtils.writeStringToFile(String.join("\n", Object2Iterator.stringIterable(ft)) + "\n",
                                ftFileTmp.getAbsolutePath(), "utf-8");
                        Files.move(ftFileTmp.toPath(), ftFile.toPath());
                    } catch (Exception e1) {
                        logger.log(Level.SEVERE, e1.getMessage(), e1);
                    }
                }

                synchronized (counter) {
                    if (counter.incrementAndGet() % 10 == 0) {
                        logger.log(Level.INFO, "Doc+Com: {0}", counter);
                    }
                }
            });
        }
        logger.log(Level.INFO, "Feature Extraction Completed. Jobs done: {0}", counter);
        logger.log(Level.INFO, "All Done.");
    }

    static List<List<Doc>> loadDocComFast(File[] files) {
        final List<List<Doc>> docComs = new ArrayList<>();

        Arrays.asList(files).parallelStream().forEach((file) -> {
            List<Doc> docCom = loadDocCom(file, true);
            synchronized (docComs) {
                docComs.add(docCom);
                if (docComs.size() % 10 == 0) {
                    logger.log(Level.INFO, "DocCom: {0}", docComs.size());
                }
            }
        });

        logger.log(Level.INFO, "Pre-processing data done. Jobs done: {0}", docComs.size());
        return docComs;
    }

    static List<List<Doc>> loadDocCom(File[] files) {
        return loadDocCom(files, true);
    }

    static List<List<Doc>> loadDocCom(File[] files, boolean preprocessed) {
        if (preprocessed) {
            return loadDocComFast(files);
        }
        return Arrays.stream(files).map(file -> loadDocCom(file, false)).collect(Collectors.toList());
    }

    static List<Doc> loadDocCom(File file, boolean preprocessed) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"))) {
            Doc doc = new Doc();
            Doc com = new Doc();
            String title = null;
            doc.setTag("D");
            doc.setSource(file.getName());
            com.setTag("C");
            com.setSource(file.getName());

            BiFunction<Doc, String, ?> lineProcess = (d, line) -> {
                //cut off <T> and </T>
                line = line.substring(3, line.length() - 4);
                String[] cols = line.split("\t");
                int textspan;
                String label = null;
                if (cols.length > 1) {
                    String lastCol = cols[cols.length - 1];
                    if (lastCol.matches("[01]")) {
                        label = lastCol;
                    }
                }

                if (label == null) {
                    doc.getLabels().add("0");
                    textspan = cols.length;
                } else {
                    d.getLabels().add(label);
                    textspan = cols.length - 1;
                }
                Annotation sent = new Annotation(String.join(" ", Arrays.asList(cols).subList(0, textspan)));
                if (preprocessed) {
                    NLPUtils.instance.annotate(sent);
                }
                d.getSentences().add(sent);
                return null;
            };
            for (String line : IOUtils.getLineIterable(reader, false)) {
                line = line.trim();
                if (title == null) {
                    if (!line.isEmpty()) {
                        title = line;
                        doc.setTitle(title);
                        com.setTitle(title);
                    }
                } else if (line.startsWith("<D>") && line.endsWith("</D>")) {
                    lineProcess.apply(doc, line);
                } else if (line.startsWith("<C>") && line.endsWith("</C>")) {
                    lineProcess.apply(com, line);
                }
            }
            return new ArrayList<>(Arrays.asList(doc, com));

        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
        return null;
    }

}
