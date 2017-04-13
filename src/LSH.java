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
    private Map<String, Integer> candidateSet;

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
            System.out.print("\tbucket '" + bucketid + "' = ");
            List<Pair<Integer>> value = bucket.getValue();
            for (Pair<Integer> p : value) {
                String filename = docidmap.get(p.getValue1());
                System.out.print("\t(" + filename + ", " + p.getValue2() + ")");
            }
            System.out.println();
            bucketid++;
        }
    }

    // exam candidate pair is plagiarism or not
    public Map<String, Integer> getCandidatePair() {
        candidateSet = new HashMap<>();
        for (Map.Entry<String, List<Pair<Integer>>> bucket : bucketSet.entrySet()) {
            List<Pair<Integer>> value = bucket.getValue();
            if (value.size() > 1) {
                // find union count
                int unionCnt = Integer.MAX_VALUE;
                StringBuilder sb = new StringBuilder();
                String prefix = "";
                for (Pair<Integer> p : value) {
                    sb.append(prefix + p.getValue1());
                    if (p.getValue2() < unionCnt) {
                        unionCnt = p.getValue2();
                    }
                    prefix = ",";
                }
                // store candidate
                String key = sb.toString();
                if (!candidateSet.containsKey(key)) {
                    candidateSet.put(key, unionCnt);
                } else {
                    candidateSet.put(key, candidateSet.get(key) + unionCnt);
                }
            }
        }
        // display possible candidate pair set
        for (Map.Entry<String, Integer> pair : candidateSet.entrySet()) {
            System.out.println("\t- buckets (" + pair.getKey() + ") = " + pair.getValue());
        }

        return candidateSet;
    }
}
