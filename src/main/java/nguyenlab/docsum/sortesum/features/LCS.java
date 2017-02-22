package nguyenlab.docsum.sortesum.features;

public class LCS {

    public int lcs(String[] a, String[] b) {
        int[][] length = new int[a.length + 1][b.length + 1];
        for (int i = a.length - 1; i >= 0; i--) {
            for (int j = b.length - 1; j >= 0; j--) {
                if (a[i].equals(b[j])) {
                    length[i][j] = length[i + 1][j + 1] + 1;
                } else {
                    length[i][j] = Math.max(length[i + 1][j], length[i][j + 1]);
                }
            }
        }
        return length[0][0];
    }

}
