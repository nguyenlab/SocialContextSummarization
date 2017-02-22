# SocialContextSummarization
This project includes the codes of summary methods for social context summarization [4,6,7,8,9,10]. The codes are implemented from several papers shown in references. Summary methods include:

# Baselines
1. Lead-m [1]: select top m sentences as the summarization.
2. Cosine-based-ILP [2]: extract summary sentences based on integer linear programming.
3. LexRank [3]: select m sentences based one LexRank algorithm.
4. SVM: trains a binary classifier using a set of features and extracts sentences labeled by 1 [4].
5. CRF: trains a sequence labeling classifier and extracts sentences labeled by 1 [5].

# State-of-the-art methods
6. SoRTESum [6]: uses a set of distance and lexical features to compute the score of each sentence by using the support from tweets.
7. cc-TAM [7]: uses a cross-collection topic-aspect modeling as a preliminary step for co-ranking.
8. HGRW [8]: is an extension of LexRank
9. L2R [9]: uses RankBoost with a set of local sentence features and cross features from tweets.
10. SoSVMRank [10]: is an improvement of [9] by adding a set of new features.

Because summary methods in [1-4] is quite simple and using traditional features, we release methods in [5-10]. Each method is organized in a package corresponding to the method name. Each package has an Example.java file, which shows the usage of each method. For example, docsum.crf contains the code of CRF. In package docsum.crf.features include the features used to train CRF and package docsum.crf.main shows the usage of features.

NOTE: the code of cc-TAM is derived from the authors. Therefore, please contact to the authors of [7] to obtain the original code.

Please contact to us if you re-use the codes.

# References
[1] Ani Nenkova. 2005. Automatic Text Summarization of Newswire: Lessons Learned from the Document Understanding Conference. In AAAI: 1436-1441.

[2] Kristian Woodsend and Mirella Lapata. 2010. Automatic Generation of Story Highlights. In ACL: 565-574.

[3] Gunes Erkan and Dragomir R. Radev. 2004. Lexrank: Graph-based Lexical Centrality as Salience in Text Summarization. Journal of Artificial Intelligence Research, 22: 457-479 (2004).

[4] Zi Yang, Keke Cai, Jie Tang, Li Zhang, Zhong Su, and Juanzi Li. 2011. Social Context Summarization. In SIGIR: 255-264.

[5] Dou Shen, Jian-Tao Sun, Hua Li, Qiang Yang, and Zheng Chen. 2007. Document Summarization Using Conditional Random Fields. In IJCAI: 2862-2867.

[6] Minh-Tien Nguyen and Minh-Le Nguyen. 2016. SoRTESum: A Social Context Framework for Single-Document Summarization. In ECIR: 3-14.

[7] Wei Gao, Peng Li, and Kareem Darwish. 2012. Joint Topic Modeling for Event Summarization across News and Social Media Streams. In CIKM: 1173-1182.

[8] Zhongyu Wei and Wei Gao. 2015. Gibberish, Assistant, or Master?: Using Tweets Linking to News for Extractive Single-Document Summarization. In SIGIR: 1003-1006.

[9] Zhongyu Wei and Wei Gao. 2014. Utilizing Microblogs for Automatic News Highlights Extraction. In COLING: 872-883.

[10] Minh-Tien Nguyen, Duc-Vu Tran, Chien-Xuan Tran, and Minh-Le Nguyen. 2016. Learning to Summarize Web Documents using Social Information. In ICTAI: 619-626.
