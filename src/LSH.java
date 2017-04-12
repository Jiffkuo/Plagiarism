import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Tzu-Chi Kuo on 2017/4/12.
 * Purpose:
 * 1. implement Locality-Sensitive Hashing
 * 2. Display candidate pair of documents is Plagiarism
 */

public class LSH {
    private int bandsOfM;
    private int rowsOfM;
    private Map<String, List<Pair<Integer>>> bucketSet;

    // Constructor
    public LSH(int band, int row) {
        bandsOfM = band;
        rowsOfM = row;
        bucketSet = new HashMap<>();
    }

    // hash signature matrix to bucket
    public void hashSigatureToBucket(int[][] sigmatrix) {
        // check singature size
        int shingleslen = sigmatrix[0].length;
        int docslen = sigmatrix.length;
        if ((bandsOfM * rowsOfM) != shingleslen) {
            System.out.print("[Error]: band and row doesn't match to signature matrix M");
            System.out.println("\tband:" + bandsOfM + " row: " + rowsOfM + " M: " + shingleslen);
            System.exit(0);
        }

        // search each document and map to bucket
        for (int docid = 0; docid < docslen; docid++) {
            // check each band of M
            for (int band = 0; band < bandsOfM; band++) {
                // check each row of band
                StringBuilder sb = new StringBuilder();
                for (int row = 0; row < rowsOfM; row++) {
                    sb.append(sigmatrix[docid][row * band]);
                }
                // map to bucket
                String key = sb.toString();
                List<Pair<Integer>> value = null;
                if (!bucketSet.containsKey(key)) {
                    value = new ArrayList<>();
                    value.add(new Pair<>(docid, 1));
                    bucketSet.put(key, value);
                } else {
                    value = bucketSet.get(key);
                    boolean isfound = false;
                    // find specific document
                    for (Pair<Integer> p : value) {
                        if (p.getValue1() == docid) {
                            p.setValue2(p.getValue2() + 1);
                            isfound = true;
                        }
                    }
                    if (!isfound) {
                        value.add(new Pair<>(docid, 1));
                    }
                }
            }
        }
    }

    // display bucketSet content
    public void displayBucketSet(Map<Integer, String> docidmap) {
        int bucketid = 0;
        for (Map.Entry<String, List<Pair<Integer>>> bucket : bucketSet.entrySet()) {
            System.out.print("\t bucket '" + bucketid + "' =");
            List<Pair<Integer>> value = bucket.getValue();
            for (Pair<Integer> p : value) {
                String filename = docidmap.get(p.getValue1());
                System.out.print("\t(" + filename + ", " + p.getValue2() + ")");
            }
            System.out.println();
            bucketid++;
        }
    }
}
