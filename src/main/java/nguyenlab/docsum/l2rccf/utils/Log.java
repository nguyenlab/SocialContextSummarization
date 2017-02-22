package nguyenlab.docsum.l2rccf.utils;

public class Log {

    public static double logb(double a, double b) {
        return Math.log(a) / Math.log(b);
    }

    public static double log2(double a) {
        return logb(a, 2);
    }
}
