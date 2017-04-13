import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.io.File;

/**
 * Created by Tzu-Chi Kuo by 2017/04/09
 * ID: W1279858
 * Purpose:
 *   1. read *.dat and parse file name
 *   2. validate: exit program if no file
 */

public class OpenFileAndValidate {
    private ArrayList<String> fileList;

    public OpenFileAndValidate(String[] inputs) {
        // check each argument validate or not
        int inLen = inputs.length;
        fileList = new ArrayList<>();
        BufferedReader bReader = null;
        for (int i = 0; i < inLen; i++) {
            try {
                FileReader fReader = new FileReader(inputs[i]);
                bReader = new BufferedReader(fReader);
                setFiles(bReader);
                fReader.close();
            } catch (Exception e) {
                System.out.println("[Error]: No such file '" + inputs[i] + "' from arguments");
                System.exit(0);
            }
        }

        // redirect file: Autotest method
        try {
            bReader = new BufferedReader(new InputStreamReader(System.in));
            // only support when there is no input argument
            if (inLen == 0) {
                setFiles(bReader);
            }
        } catch (Exception e) {
            System.out.println("[Error]: No redirect input file");
            System.exit(0);
        }
    }

    // set files to file List
    public void setFiles(BufferedReader bReader) {
        try {
            String line = "";
            while ((line = bReader.readLine()) != null) {
                // split white space and tab
                String[] files = line.trim().split("[\\s\\t]+");
                for (String file : files) {
                    // skip empty line
                    if (file.equals("\n") || file.equals("")) {
                        continue;
                    }
                    // check file exist or not
                    File f = new File(file);
                    if (f.exists()) {
                        fileList.add(file);
                    } else {
                        System.out.println("[Error]: No such file '" + file + "'");
                        System.exit(0);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("[Error]: file format error");
            System.exit(0);
        }
    }

    // return file list
    public ArrayList<String> getFiles() {
        if (fileList.size() == 0) {
            System.out.println("[Error]: No any files");
            System.exit(0);
        }

        return fileList;
    }
}
