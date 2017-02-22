/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nguyenlab.docsum.hgrw.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author TRAN
 */
public class Stopwords {

    public static final Set<String> self;

    static {
        Set<String> stopwords = new HashSet<>();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(Stopwords.class.getResourceAsStream("stopwords.txt"), "utf-8"));
            String line;
            while (null != (line = reader.readLine())) {
                stopwords.add(line);
            }
        } catch (IOException ex) {
            Logger.getLogger(Stopwords.class.getName()).log(Level.SEVERE, null, ex);
        }
        self = Collections.unmodifiableSet(stopwords);
    }
}
