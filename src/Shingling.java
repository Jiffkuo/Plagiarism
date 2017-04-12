import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Tzu-Chi Kuo on 2017/4/10.
 * ID: W1279858
 * Purpose: k-shingles, to open file and read k-token (character)
 */

public class Shingling {
    private int kvalue;
    private Set<String> shinglingset;

    // constructor
    public Shingling(int val) {
        kvalue = val;
    }

    // set document to shingling set
    // @param: file name
    public void set(String file) {
        shinglingset = new HashSet<>();
        // open file and read kvalue character
        System.out.print("\topen file '" + file + "'");
        try {
            FileReader fReader = new FileReader(file);
            BufferedReader bReader = new BufferedReader(fReader);
            int value = 0;
            int cnt = 0;
            boolean bwhitespace = false;
            boolean bappend = false;
            StringBuilder sb = new StringBuilder();
            while((value = bReader.read()) != -1) {
                // convert value to character
                char c = (char) value;
                // merge white space to single blank
                if (Character.isWhitespace(c) && !bwhitespace) {
                    if (Character.compare(c, '\n') != 0) {
                        bwhitespace = true;
                        bappend = true;
                    }
                }
                // erase punctuation
                if (Character.isLetterOrDigit(c)) {
                    bwhitespace = false;
                    bappend = true;
                    // all characters convert to lower case
                    c = Character.isLowerCase(c) ? c : Character.toLowerCase(c);
                }
                if (bappend) {
                    sb.append(c);
                    cnt++;
                    bappend = false;
                }
                // do k-shingles
                if (cnt == kvalue) {
                    shinglingset.add(sb.toString());
                    cnt = 0;
                    sb.setLength(0);
                }
            }
            // add rest of character into set
            if (cnt > 0) {
                shinglingset.add(sb.toString());
            }
            fReader.close();
        } catch (Exception e) {
            System.out.println("\n[Error]: cannot open file '" + file + "'");
            System.exit(0);
        }
    }

    // return singling set
    public Set<String> get() {
        System.out.println("\tsize of shinglingset = "+shinglingset.size());
        return shinglingset;
    }
}
