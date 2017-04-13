import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Tzu-Chi Kuo on 2017/4/12.
 */
public class Similarities {
    private int bandVal;
    private double threadsholdVal;
    private Set<String> resultSet;

    // Constructor
    public Similarities (int b, double s) {
        bandVal = b;
        threadsholdVal = s;
        resultSet = new HashSet<>();
    }

    /*
    public void displayCandidatePair(Map<Integer, String> docidmap, Map<String, Integer> candidatepair) {
        if (candidatepair.size() == 0) {
            System.out.println("[Info]: There is no candidate pair in Plagiarism");
            System.exit(0);
        } else {
            System.out.println("[Info]: Find all possible candidate pairs in Plagiarism as following:");
            for (Map.Entry<String, Integer> pair : candidatepair.entrySet()) {
                double prob = (double)pair.getValue() / bandVal;
                String key = pair.getKey();
                if (prob >= threadsholdVal) {
                    String[] files = key.split(",");
                    System.out.print("\t");
                    String prefix = "";
                    for (String f : files) {
                        String name = docidmap.get(Integer.valueOf(f));
                        System.out.print(prefix + name);
                        prefix = ", ";
                    }
                    //System.out.println(" = " + prob);
                } else {
                    System.out.println("\n\t" + key + " need to use Jaccard Similarity to test input matrix again");
                }
            }
        }
    }
    */

    // get result
    public void displayResult() {
        if (resultSet.size() == 0) {
            System.out.println("\tNo plagiarism found!");
        } else {
            for (String s : resultSet) {
                System.out.println("\t(" + s + ")");
            }
        }
    }

    public void testCandidatePair(Map<Integer, String> docidmap, Map<String, Integer> canpair, int[][] inputs) {
        if (canpair.size() == 0) {
            System.out.println("\tThere is no candidate pair in Plagiarism");
            System.exit(0);
        } else {
            for (Map.Entry<String, Integer> pair : canpair.entrySet()) {
                String[] files = pair.getKey().split(",");
                int filelen = files.length;
                for (int i = 0; i < filelen; i++) {
                    int doc1 = Integer.valueOf(files[i]) ;
                    for (int j = i + 1; j < filelen; j++) {
                        int doc2 = Integer.valueOf(files[j]);
                        if (isPlaiarism(inputs[doc1], inputs[doc2])) {
                            StringBuilder sb = new StringBuilder();
                            sb.append(docidmap.get(doc1) + ", " + docidmap.get(doc2));
                            resultSet.add(sb.toString());
                            //System.out.println("\t(" + docidmap.get(doc1) + ", " + docidmap.get(doc2) + ")");
                        }
                    }
                }
            }
        }
    }

    // Check plagiarism by Jaccard Similarity
    private boolean isPlaiarism (int[] doc1, int[] doc2) {
        int union = 0;
        int intersection = 0;
        double jaccard = 0.0;

        if (doc1.length != doc2.length) {
            System.out.println("[Error]: the length of two documents are not equal");
            System.exit(0);
        }

        // Jaccard Similarity
        for (int i = 0; i < doc1.length; i++) {
            union += doc1[i] | doc2[i];
            intersection += doc1[i] & doc2[i];
        }
        jaccard = (double) intersection / (double) union;
        if (jaccard >= threadsholdVal) {
            return true;
        }
        return false;
    }
}
