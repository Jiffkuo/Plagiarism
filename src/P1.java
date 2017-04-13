import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Created by Tzu-Chi Kuo by 2017/04/09
 * ID: W1279858
 *
 * Purpose: Use shingling, minhashing and locality-sentitve hashing to
 *          implement a way of finding plagiarism
 *  @param: *.dat, a set of documents
 *
 */
public class P1 {
    public static void main(String[] args) {
        // Step0. print basic information
        System.out.println("====================================");
        System.out.println("= Student name: Tzu-Chi Kuo        =");
        System.out.println("= Student ID: W1279858             =");
        System.out.println("= Program Assignment #1            =");
        System.out.println("====================================");

        // Step1. open files and exam each file is valid or not
        OpenFileAndValidate open = new OpenFileAndValidate(args);
        ArrayList<String> fileList = open.getFiles();
        // Step1-1. print information
        System.out.print("[Info]: Open '");
        String prefix = "";
        for (String file : fileList) {
            System.out.print(prefix + file);
            prefix = "' '";
        }
        System.out.println("' Successfully");

        // Step2. k-Shingling (k = 9) each file(document) and save it to Shingling set
        int k = 9;
        Map<String, Set<String>> docShinglingMap = new HashMap<>(fileList.size());
        Map<Integer, String> docIDMap = new HashMap<>(fileList.size());
        int docID = 0;
        Shingling shingling = new Shingling(k);
        System.out.println("[Info]: k-shingling (k = 9)");
        for (String file : fileList) {
            shingling.set(file);
            if (!docShinglingMap.containsKey(file)) {
                docShinglingMap.put(file, shingling.get());
                docIDMap.put(docID, file);
                docID++;
            } else {
                System.out.println("\n[Warning]: file '" + file + "' already exists");
            }
        }
        // Step2-1. print information
        /*
        for (Map.Entry<String, Set<String>> entry : docShinglingMap.entrySet()) {
            String doc = entry.getKey();
            Set<String> shingles = entry.getValue();
            System.out.println("[Info] set of "+ k + "-shingles for '" + doc + "' = {");
            System.out.print("\'");
            prefix = "";
            Iterator iter = shingles.iterator();
            for (String s : shingles) {
                System.out.print(prefix + s);
                prefix = "\', \'";
            }
            System.out.println("\'}\n");
        }
        */

        // Step3. minhashing input matrix to signature matrix M
        Minhashing minhashing = new Minhashing(docShinglingMap);
        minhashing.setInputMatrix(docIDMap, docShinglingMap);
        int[][] inputMatrix = minhashing.getInputMatrix();
        System.out.println("\n[Info-A]: minhashing tables as following:");
        System.out.print("\t[1] Input Matrix (col:Documents, row:Shingles) = ");
        minhashing.displayMatrix(inputMatrix);
        minhashing.setSignatureMatrix(true);
        int[][] singatureMatrix = minhashing.getSignatureMatrix();
        int rowOfM = minhashing.getNumOfPermutation();

        // Step4. locality-sensitive hashing
        // Step4-1. determine parameter b, r, AND or OR construction combination
        //          to meet (0.2, 0.8, 0.997, 0.003)-sensitive
        double d1 = 0.2;
        double d2 = 0.8;
        double p1 = 0.997;
        double p2 = 0.003;
        System.out.println("\n[Info-B]: Processing Locality-Sensitive Hashing ...");
        System.out.print("\tTarget (d1, d2, p1, p2)-sensitive = (" + d1 + ", " + d2 + ", ");
        System.out.println(p1 + ", " + p2 + ")");
        SearchBandAndRow sbr = new SearchBandAndRow(d1, d2, p1, p2);
        int mvalue = 1;
        System.out.print("\tSearching possible parameter (band, row) and AND or OR construction\n\tProcessing ");
        while (!sbr.searchCombination(false)) {
            sbr.setRowOfM(++mvalue);
            System.out.print(".");
            //sbr.displayPossibleBandRow();
        }
        System.out.println();
        // check if need to regenerate permutation and signature matrix M
        if (mvalue > rowOfM) {
            System.out.print("[Info]: Original signature matrix M (row of M) = " + rowOfM + " needs to regenerate ");
            System.out.print("with at least '" + mvalue + "' to guarantee the probabilities of target ");
            System.out.println("(d1, d2, p1, p2)-sensitive");
            minhashing.setNumOfPermutation(mvalue);
            minhashing.setSignatureMatrix(false);
            singatureMatrix = minhashing.getSignatureMatrix();
            minhashing.displayMatrix(singatureMatrix);
        }
        Pair<Integer> brpair = sbr.getResult();
        // Step4-2. hash candidate pair to bucket table by referring to
        //          document set of n-th band. also need to count
        //          how many a document hash to a bucket (check hash ratio >= 0.8)
        LSH lsh = new LSH(brpair.getValue1(), brpair.getValue2());
        lsh.hashSigatureToBucket(singatureMatrix);
        System.out.println("[Info]: Similar columns hash to the same buckets as following:");
        lsh.displayBucketSet(docIDMap);
        Map<String, Integer> candidatePair = lsh.getCandidatePair();

        // Step5. Test the similarity for all possible pairs (>= 0.8) and print the answer
        System.out.println("\n[Info-C]: Find all possible candidate pairs are plagiarism as following:");
        Similarities sims = new Similarities(brpair.getValue1(), (1 - d1));
        sims.testCandidatePair(docIDMap, candidatePair, inputMatrix);
        sims.displayResult();
    }
}
