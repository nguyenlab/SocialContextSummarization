package nguyenlab.docsum.sortesum.features;

public class IDF {

    // Total common words of 2 sentence
    double common(String[] t, String[] h) {
        double comm = 0;
        for (int i = 0; i < h.length; i++) {
            for (int j = 0; j < t.length; j++) {
                if (h[i].equals(t[j])) {
                    comm++;
                    break;
                }
            }
        }
        return comm;
    }

    // Tinh tan suat xuat hien cua cac tu t c� trong h
    public double occurH(String[] t, String[] h) {
        double comm = common(t, h);
        return comm / (double) h.length;

    }

    // Tinh tan suat xuat hien cua cac tu h c� trong t
    public double occurT(String[] t, String[] h) {
        double comm = common(t, h);
        return comm / (double) t.length;

    }

    // Tan suat xuat hien cua 1 tu trong cau khc
    static double[] frequecy(String[] a, String[] b) {
        double fre[] = new double[b.length];
        for (int i = 0; i < b.length; i++) {
            double tmp = 0;
            for (int j = 0; j < a.length; j++) {
                if (b[i].equals(a[j])) {
                    tmp++;
                }
            }
            fre[i] = tmp;
        }
        return fre;
    }

    /*	public static void main(String[] args) {
		String[] a = { "a", "a", "b", "d", "e", "e", "a", "f", "l", "g" };
		String[] b = { "a", "a", "b", "d", "e", "f", "l" };
		double[] fre = new double[b.length];
		double comm = common(a, b);
		double comm1 = occurH(a, b);
		double comm2 = occurT(a, b);
		fre = frequecy(a, b);
		for (int i = 0; i < fre.length; i++)
			System.out.print(fre[i] + "  ");

		System.out.print(comm + "  " + comm1 + " " + comm2);

	} */
}
