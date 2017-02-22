/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nguyenlab.docsum.hgrw.utils;

import java.util.List;
import java.util.stream.Collectors;

import nguyenlab.docsum.hgrw.lexrank.Word;

/**
 *
 * @author TRAN
 */
public class Utils {

    public static List<String> fromWord2String(List<Word> words) {
        return words.stream().map((Word w) -> w.getText()).collect(Collectors.toList());
    }
}
