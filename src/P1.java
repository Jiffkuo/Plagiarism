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
        Map<String, Set> docShinglingMap = new HashMap<>(fileList.size());
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
        for (Map.Entry<String, Set> entry : docShinglingMap.entrySet()) {
            String doc = entry.getKey();
            Set<String> shingles = entry.getValue();
            System.out.println("[Info] set of "+ k + "-shingles for '" + doc + "' = ");
            System.out.print("{\"");
            prefix = "";
            for (String s : shingles) {
                System.out.print(prefix + s);
                prefix = "\", \"";
            }
            System.out.println("\"}\n");
        }
        */

        // Step3. minhashing and save to a signature matrix M
        Minhashing minhashing = new Minhashing(docShinglingMap);
        minhashing.setInputMatrix(docIDMap, docShinglingMap);
        int[][] inputMatrix = minhashing.getInputMatrix();
        System.out.println("[Info]: minhashing tables as following:");
        System.out.println("\t[1] Input Matrix (Document-Shingling)");
        minhashing.displayMatrix(inputMatrix);
        minhashing.setSignatureMatrix();

        // Step4. locality-sensitive
        // Step4-1. determine parameter b and r
        // Step4-2. determine AND or OR construction combination
        // Step4-3. calculate Jaccard Similarity / probability
        // Step4-4. hash candidate pair to bucket table
        // Step5. Test the similarity for all possible pairs
    }
}
