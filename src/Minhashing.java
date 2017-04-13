import java.util.*;

/**
 * Created by Tzu-Chi Kuo on 2017/4/10.
 * ID: W1279858
 * Purpose: implement minhashing function, hash input boolean Matrix to signature Matrix
 */

public class Minhashing {
    private Set<String> shinglingAllSet;
    private int[][] inputMatrix;
    private int[][] signatureMatrix;
    private int numOfPermutation;
    private int allSetSize;
    private int allDocSize;

    // constructor
    @SuppressWarnings("unchecked")
    public Minhashing (Map<String, Set<String>> allmap) {
        shinglingAllSet = new HashSet<>();
        for (Map.Entry<String, Set<String>> entry : allmap.entrySet()) {
            Set<String> shingles = entry.getValue();
            for (String s : shingles) {
                shinglingAllSet.add(s);
            }
        }
        // row
        allSetSize = shinglingAllSet.size();
        // column
        allDocSize = allmap.size();
        inputMatrix = new int[allDocSize][allSetSize];
        System.out.println("[Info]: The size of all documents = " + allDocSize);
        System.out.println("[Info]: The size of all shingles set = " + allSetSize);

        // calculate number of permutation = sqrt(allShinglesSet), non-prime value
        numOfPermutation = (int)Math.round(Math.sqrt((double)allSetSize));
        if (isPrime(numOfPermutation)) {
            numOfPermutation++;
        }
        System.out.println("[Info]: The number of permutation (rows of M) = " + numOfPermutation);
        signatureMatrix = new int[allDocSize][numOfPermutation];
    }

    // check if value is prime number
    private boolean isPrime(int n) {
        for (int i = 2; i < n; i++) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }

    // set # of permutation
    public void setNumOfPermutation(int val) {
        numOfPermutation = val;
        if (isPrime(numOfPermutation)) {
            numOfPermutation++;
        }
        System.out.println("[Info]: The number of permutation (rows of M) = " + numOfPermutation);
        signatureMatrix = new int[allDocSize][numOfPermutation];
    }
    // return # of permutation to combine parameter b and r
    public int getNumOfPermutation() {
        return numOfPermutation;
    }

    // set input matrix M
    @SuppressWarnings("unchecked")
    public void setInputMatrix(Map<Integer, String> docidmap, Map<String, Set<String>> allmap) {
        // check doc ID map is valid
        if (docidmap.size() != allDocSize) {
            System.out.println("[Error]: Document ID-File hashmap doesn't match");
            System.exit(0);
        }

        // boolean vector
        for (int id = 0; id < allDocSize; id++) {
            String file = docidmap.get(id);
            Set<String> set = allmap.get(file);
            int ele = 0;
            for (String s : shinglingAllSet) {
                if (set.contains(s)) {
                    inputMatrix[id][ele] = 1;
                } else {
                    inputMatrix[id][ele] = 0;
                }
                ele++;
            }
        }
    }

    // get input matrix M
    public int[][] getInputMatrix() {
        return inputMatrix;
    }

    // generate number of permutation by random
    public int[][] generateRandomPermutation() {
        int[][] matrix = new int[numOfPermutation][allSetSize];
        List<Integer> random = new ArrayList<>();

        // initialize random seed
        for (int i = 0; i < allSetSize; i++) {
            random.add(i + 1);
        }
        // generate random list and set to permutation matrix
        for (int i = 0; i < numOfPermutation; i++) {
            Collections.shuffle(random);
            for (int j = 0; j < allSetSize; j++) {
                matrix[i][j] = random.get(j);
            }
        }

        return matrix;
    }

    // hash function
    public void hashFunction(int[][] permuteMatrix) {
        for (int i = 0; i < numOfPermutation; i++) {
            int[] permute = permuteMatrix[i];
            int[] index = new int[allSetSize];
            // create index to map between permutation and document
            for (int pos = 0; pos < allSetSize; pos++) {
                index[permute[pos] - 1] = pos;
            }

            // hashing
            for (int j = 0; j < allDocSize; j++) {
                int[] document = inputMatrix[j];
                for (int k = 0; k < allSetSize; k++) {
                    if (document[index[k]] == 1) {
                        signatureMatrix[j][i] = permute[index[k]];
                        break;
                    }
                }
            }
        }
    }

    // display matrix content
    public void displayMatrix(int[][] matrix) {
        System.out.println("("+matrix.length+", "+matrix[0].length + ")");
        for (int j = 0; j < matrix[0].length; j++) {
            String prefix = "\t";
            for (int i = 0; i < matrix.length; i++) {
                System.out.print(prefix + matrix[i][j]);
            }
            System.out.println();
        }
    }

    // set signature matrix M
    public void setSignatureMatrix(boolean display) {
        int[][] permuteMatrix = generateRandomPermutation();
        if (display) {
            System.out.print("\t[2] The permutation matrix (col: Permutations, row:Shingles) = ");
            displayMatrix(permuteMatrix);
        }

        hashFunction(permuteMatrix);
        if (display) {
            System.out.print("\t[3] The signature matrix M (col: Documents, row: Permutations) = ");
            displayMatrix(signatureMatrix);
        }
    }

    // get signature matrix M
    public int[][] getSignatureMatrix() {
        return signatureMatrix;
    }
}
