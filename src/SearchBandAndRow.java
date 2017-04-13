import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tzu-Chi Kuo on 2017/4/11.
 * ID: W1279858
 * Purpose:
 * 1. Search band and row parameter to meet (d1, d2, p1, p2)-sensitive
 * 2. Search AND or OR constructions to meet (d1, d2, p1, p2)-sensitive
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
    public SearchBandAndRow(double d1, double d2, double p1, double p2) {
        this.d1 = d1;
        this.d2 = d2;
        targetP1 = p1;
        targetP2 = p2;
        setRowOfM(1);
   }

    // set and calculate combination of rows of M
    public void setRowOfM(int m) {
        // generate band row combination from m
        rowOfM = m;
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
        DecimalFormat df = new DecimalFormat("##.0000");
        return Double.parseDouble(df.format(Math.pow(prob, (double)row)));
    }
    // OR operation of hashing function
    private double orOp(int band, double prob) {
        DecimalFormat df = new DecimalFormat("##.0000");
        return Double.parseDouble(df.format(1 - Math.pow((1 - prob), (double)band)));
    }

    // handle with result
    private void saveResult(double p1, double p2, Pair<Double> prob, Pair<Integer> candidate) {
        if (prob.getValue1() == 0.0 && prob.getValue2() == 0.0) {
            prob.setValue1(p1);
            prob.setValue2(p2);
            result = candidate;
        } else if (p1 > prob.getValue1() && p2 < prob.getValue2()) {
            prob.setValue1(p1);
            prob.setValue2(p2);
            result = candidate;
        }
    }
    private boolean isfindResult(Pair<Double> prob) {
        if (result == null) {
            prob.setValue1(0.0);
            prob.setValue2(0.0);
            return false;
        } else {
            System.out.print("\n\tFind candidate (band, row) pair = ");
            System.out.print("(" + result.getValue1() + ", " + result.getValue2() + ") and ");
            System.out.print("(p1, p2) = (" + prob.getValue1() + ", " + prob.getValue2() + ")");
            System.out.println(" to achieve the requirement");
            return true;
        }
    }
    public Pair<Integer> getResult() {
        return result;
    }

    // display processing
    private void displayProcess(Pair<Integer> p, double p1, double p2) {
        System.out.print("\n\ttest candidate (band, row) pair = ");
        System.out.print("(" + p.getValue1() + ", " + p.getValue2() + ") and ");
        System.out.println("(p1, p2) = (" + p1 + ", " + p2 + ")");
    }

    // search all possible combination of b, r and ADN, OR constructor
    // if find first (band, row) can meet LSH-sensitive, return
    public boolean searchCombination(boolean isdisplay) {
        DecimalFormat df = new DecimalFormat("##.0");
        double p1 = Double.parseDouble(df.format(1.0 - d1));
        double p2 = Double.parseDouble(df.format(1.0 - d2));
        Pair<Double> prob = new Pair<>(0.0, 0.0);

        // AND (row) construction
        if (isdisplay) {
            System.out.println("\tTest 'AND' construction");
        }
        for (Pair<Integer> p : brSet) {
            double tmp1 = andOp(p.getValue2(), p1);
            double tmp2 = andOp(p.getValue2(), p2);
            if (tmp1 >= targetP1 && tmp2 <= targetP2) {
                saveResult(tmp1, tmp2, prob, p);
            }
            if (isdisplay) {
                displayProcess(p, tmp1, tmp2);
            }
        }
        if(isfindResult(prob)) {
            System.out.println("\tUnder 'AND' construction");
            return true;
        }

        // OR (band) construction
        if (isdisplay) {
            System.out.println("\tTest 'OR' construction");
        }
        for (Pair<Integer> p : brSet) {
            double tmp1 = orOp(p.getValue1(), p1);
            double tmp2 = orOp(p.getValue1(), p2);
            if (tmp1 >= targetP1 && tmp2 <= targetP2) {
                saveResult(tmp1, tmp2, prob, p);
            }
            if (isdisplay) {
                displayProcess(p, tmp1, tmp2);
            }
        }
        if(isfindResult(prob)) {
            System.out.println("\tUnder 'OR' construction");
            return true;
        }

        // AND-OR construction
        if (isdisplay) {
            System.out.println("\tTest 'AND-OR' construction");
        }
        for (Pair<Integer> p : brSet) {
            double tmp1 = orOp(p.getValue1(), andOp(p.getValue2(), p1));
            double tmp2 = orOp(p.getValue1(), andOp(p.getValue2(), p2));
            if (tmp1 >= targetP1 && tmp2 <= targetP2) {
                saveResult(tmp1, tmp2, prob, p);
            }
            if (isdisplay) {
                displayProcess(p, tmp1, tmp2);
            }
        }
        if(isfindResult(prob)) {
            System.out.println("\tUnder 'AND-OR' construction");
            return true;
        }

        // OR-AND construction
        if (isdisplay) {
            System.out.println("\tTest 'OR-AND' construction");
        }
        for (Pair<Integer> p : brSet) {
            double tmp1 = andOp(p.getValue2(), orOp(p.getValue1(), p1));
            double tmp2 = andOp(p.getValue2(), orOp(p.getValue1(), p2));
            if (tmp1 >= targetP1 && tmp2 <= targetP2) {
                saveResult(tmp1, tmp2, prob, p);
            }
            if (isdisplay) {
                displayProcess(p, tmp1, tmp2);
            }
        }
        if(isfindResult(prob)) {
            System.out.println("\tUnder 'OR-AND' construction");
            return true;
        }
        return false;
    }
}
