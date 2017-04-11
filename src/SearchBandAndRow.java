import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tzu-Chi Kuo on 2017/4/11.
 */
public class SearchBandAndRow {
    private double d1;
    private double d2;
    private double targetP1;
    private double targetP2;
    private int rowOfM;
    private List<Pair<Integer>> brSet;
    private Pair<Integer> result;

    // constructor
    public SearchBandAndRow(double d1, double d2, double p1, double p2, int m) {
        this.d1 = d1;
        this.d2 = d2;
        targetP1 = p1;
        targetP2 = p2;
        rowOfM = m;
        // generate band row combination from m
        brSet = new ArrayList<>();
        for (int i = 1; i <= rowOfM; i++) {
            if ((rowOfM % i) == 0) {
                // Pair(bands, rows)
                brSet.add(new Pair<>(i, rowOfM/i));
            }
        }
    }
    // display for debug purpose
    public void displayPossibleBandRow() {
        System.out.println("\tPossible (band, row) combinations of M(" + rowOfM + ") = {");
        for(Pair<Integer> p : brSet) {
            System.out.println("\t\t(" +p.getValue1() + ", " + p.getValue2() + "),");
        }
        System.out.println("\t}");
    }

    // Search and Exam all combination of AND-OR construction to meet the targets
    // AND operation of hashing function
    private double andOp(int row, double prob) {
        DecimalFormat df = new DecimalFormat("##.000");
        return Double.parseDouble(df.format(Math.pow(prob, (double)row)));
    }
    // OR operation of hashing function
    private double orOp(int band, double prob) {
        DecimalFormat df = new DecimalFormat("##.000");
        return Double.parseDouble(df.format(1 - Math.pow((1 - prob), (double)band)));
    }
    private void saveResult(double p1, double p2, Pair<Double> pair, Pair<Integer> candidate) {
        if (pair.getValue1() == 0.0 && pair.getValue2() == 0.0) {
            result = candidate;
        } else if (pair.getValue1() < p1 && pair.getValue2() > p2) {
            pair.setValue1(p1);
            pair.setValue2(p2);
            result = candidate;
        }
    }
    private void displayResult(Pair<Double> prob) {
        if (result == null) {
            System.out.println("\tCannot meet target");
            prob.setValue1(0.0);
            prob.setValue2(0.0);
        } else {
            result = null;
            System.out.print("\t Find a possible (band, row) pair = ");
            System.out.print("(" + result.getValue1() + ", " + result.getValue2() + ") and ");
            System.out.println("(p1, p2) = (" + prob.getValue1() + ", " + prob.getValue2() + ")");
        }
    }
    public void searchCombination() {
        DecimalFormat df = new DecimalFormat("##.0");
        double p1 = Double.parseDouble(df.format(1.0 - d1));
        double p2 = Double.parseDouble(df.format(1.0 - d2));
        Pair<Double> prob = new Pair<>(0.0, 0.0);
        // AND (row)
        System.out.println("\tSearching AND construction with ....");
        System.out.println("\tOriginal (p1, p2) = " + p1 + ", " + p2);
        for (Pair<Integer> p : brSet) {
            double tmp1 = andOp(p.getValue2(), p1);
            double tmp2 = andOp(p.getValue2(), p2);
            System.out.println("\t(p1, p2) = " + tmp1 + ", " + tmp2);
            if (tmp1 >= targetP1 && tmp2 <= targetP2) {
                saveResult(tmp1, tmp2, prob, p);
            }
        }
        displayResult(prob);
        // OR (band)
        System.out.println("\tSearching OR construction ....");
        System.out.println("\tOriginal (p1, p2) = " + p1 + ", " + p2);
        for (Pair<Integer> p : brSet) {
            double tmp1 = orOp(p.getValue1(), p1);
            double tmp2 = orOp(p.getValue1(), p2);
            System.out.println("\t(p1, p2) = " + tmp1 + ", " + tmp2);
            if (tmp1 >= targetP1 && tmp2 <= targetP2) {
                saveResult(tmp1, tmp2, prob, p);
            }
        }
        displayResult(prob);
        // AND-OR
        System.out.println("\tSearching AND-OR construction ....");
        System.out.println("\tOriginal (p1, p2) = " + p1 + ", " + p2);
        for (Pair<Integer> p : brSet) {
            double tmp1 = orOp(p.getValue1(), andOp(p.getValue2(), p1));
            double tmp2 = orOp(p.getValue1(), andOp(p.getValue2(), p2));
            System.out.println("\t(p1, p2) = " + tmp1 + ", " + tmp2);
            if (tmp1 >= targetP1 && tmp2 <= targetP2) {
                saveResult(tmp1, tmp2, prob, p);
            }
        }
        displayResult(prob);
        // OR-AND
        System.out.println("\tSearching OR-AND construction ....");
        System.out.println("\tOriginal (p1, p2) = " + p1 + ", " + p2);
        for (Pair<Integer> p : brSet) {
            double tmp1 = andOp(p.getValue2(), orOp(p.getValue1(), p1));
            double tmp2 = andOp(p.getValue2(), orOp(p.getValue1(), p2));
            System.out.println("\t(p1, p2) = " + tmp1 + ", " + tmp2);
            if (tmp1 >= targetP1 && tmp2 <= targetP2) {
                saveResult(tmp1, tmp2, prob, p);
            }
        }
        displayResult(prob);
        // (OR-AND)-(AND-OR)
        System.out.println("\tSearching OR-AND AND-OR cascade construction ....");
        System.out.println("\tOriginal (p1, p2) = " + p1 + ", " + p2);
        for (Pair<Integer> p : brSet) {
            // OR-AND
            double tmp1 = andOp(p.getValue2(), orOp(p.getValue1(), p1));
            double tmp2 = andOp(p.getValue2(), orOp(p.getValue1(), p2));
            // AND-OR
            tmp1 = orOp(p.getValue1(), andOp(p.getValue2(), tmp1));
            tmp2 = orOp(p.getValue1(), andOp(p.getValue2(), tmp2));
            System.out.println("\t(p1, p2) = " + tmp1 + ", " + tmp2);
            if (tmp1 >= targetP1 && tmp2 <= targetP2) {
                saveResult(tmp1, tmp2, prob, p);
            }
        }
        displayResult(prob);
        // (AND-OR)-(OR-AND)
        System.out.println("\tSearching AND-OR OR-AND cascade construction ....");
        System.out.println("\tOriginal (p1, p2) = " + p1 + ", " + p2);
        for (Pair<Integer> p : brSet) {
            // AND-OR
            double tmp1 = orOp(p.getValue1(), andOp(p.getValue2(), p1));
            double tmp2 = orOp(p.getValue1(), andOp(p.getValue2(), p2));
            // OR-AND
            tmp1 = andOp(p.getValue2(), orOp(p.getValue1(), tmp1));
            tmp2 = andOp(p.getValue2(), orOp(p.getValue1(), tmp2));
            System.out.println("\t(p1, p2) = " + tmp1 + ", " + tmp2);
            if (tmp1 >= targetP1 && tmp2 <= targetP2) {
                saveResult(tmp1, tmp2, prob, p);
            }
        }
        displayResult(prob);
    }
}
