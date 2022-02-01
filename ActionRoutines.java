import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class ActionRoutines {

    static HashMap<String, Object> varMap = new HashMap<String, Object>();
    static HashMap<Integer, Object> PosTupleMap = new HashMap<Integer, Object>();
    static HashMap<Integer, Object> NegTupleMap = new HashMap<Integer, Object>();

    // for tuple concatenation
    static int counter;
    static ArrayList<String> tupleConcat = new ArrayList<>();
    static ArrayList<String> concatenatedTup = new ArrayList<>();

    // positive indexing
    static int PosTupleIndex = -1;

    // negative indexing
    static int NegTupleIndex = 0;

    public ArrayList<String> addToList() {
        for (Entry<String, Object> entry : varMap.entrySet()) {

            String s = entry.getValue().toString();
            if (s.contains("(")) {
                if (!tupleConcat.contains(s)) {
                    tupleConcat.add(entry.getValue().toString());
                }
            }
        }

        // System.out.println(varMap);
        return (tupleConcat);
    }

    public ArrayList<String> concatTuples() {

        String s5 = new String("");

        // (1,2,3) (3,4,5) (6,7,8)
        int i = 0;
        String s1 = tupleConcat.get(i);
        String s2 = tupleConcat.get(i + 1);

        String s3 = s1.replace(")", "");

        String s4 = s2.replace("(", ",");

        s5 = s5 + s3 + s4;

        for (int j = 2; j < tupleConcat.size(); j++) {

            String s6 = tupleConcat.get(j);

            s5 = s5.replace(")", "");
            s6 = s6.replace("(", ",");

            s5 = s5 + s6;

        }

        if (!concatenatedTup.contains(s5)) {
            concatenatedTup.add(s5);
        }

        return (concatenatedTup);

    }

    public Object assignIDs() {

        Object val = PosTupleMap.get(counter);

        counter++;
        return val;

    }

    // for figuring out errors
    public void printTupleMaps() {
        // System.out.println(tupleConcat);
        // System.out.println("Positive Tuples: " + PosTupleMap);
        // System.out.println("Negative Tuples: " + NegTupleMap + " " + NegTupleIndex);
        // System.out.println("Var Map: " + varMap);
        // System.out.println("Pos index: " + PosTupleIndex + " Neg index: " +
        // NegTupleIndex);
    }

    public void assignNegTupleIndex(String x) {
        NegTupleMap.put(--NegTupleIndex, x);
    }

    public Object getNegTupleVal(Object x) {
        if ((Integer) x < NegTupleIndex) {
            System.out.println("Out of bounds!");
        }

        else {

            return (NegTupleMap.get(x));

        }

        return (NegTupleMap.get(x));
    }

    // positive indexing
    public void assignTupleIndex(String x) {
        PosTupleMap.put(++PosTupleIndex, x);

    }

    public Object getTupleVal(Object x) {

        if ((Integer) x > PosTupleIndex) {
            System.out.println("Out of Bounds!");
        } else {
            return (PosTupleMap.get(x));

        }

        return (PosTupleMap.get(x));
    }

    // assigning variables
    public void assign(String x, Object y) {
        varMap.put(x, y);

    }

    // for printing T[i:j]
    public ArrayList<Object> Slice2Ops(String s1, String s2) {
        int i = makeInt(s1);
        int j = makeInt(s2);

        ArrayList<Object> Ops = new ArrayList<Object>();

        if (i >= 0 & j >= 0) {

            if (j >= PosTupleIndex + 2) {
                System.out.println("Out of bounds!");
            }

            else {

                // System.out.print("The values between indices " + i + " and " + j + "
                // exclusive are: ");

                for (int k = i; k < j; k++) {

                    Ops.add(PosTupleMap.get(k));
                    // System.out.print(PosTupleMap.get(k) + ", ");

                }

                return Ops;
                // System.out.println();
            }
        }
        // T=(1,2,3)
        // T[-3:-1] = (1,2)
        else if (i < 0 & j < 0) {

            int size = NegTupleMap.size();

            if (i > size) {
                System.out.println("Invalid index!");
            }

            else {

                // System.out.print("The values between indices " + i + " and " + j + "
                // exclusive are: ");

                for (int k = i; k < j; k++) {

                    Ops.add(NegTupleMap.get(k));
                    // System.out.print(NegTupleMap.get(k) + ", ");
                }

                return Ops;
            }
        }

        else if (i < 0 & j >= 0) {

            if (i <= NegTupleIndex - 1 || j > PosTupleIndex + 1) {
                System.out.println("Invalid Index!");
            }

            else {
                // System.out.print("The values between indices " + i + " and " + j + "
                // exclusive are: ");

                // Here I'm getting the equivalent key of Object at given negative index
                // in the positive tuple map
                Object posIEq = NegTupleMap.get(i);
                // System.out.println(posIEq.toString());

                int realIndex = 0;

                // System.out.println(NegTupleMap);
                // System.out.println(PosTupleMap);

                for (Entry<Integer, Object> entry : PosTupleMap.entrySet()) {

                    // System.out.println(entry.getKey() + " " + entry.getValue());
                    if (entry.getValue().equals(posIEq)) {
                        // System.out.println(entry.getKey());
                        realIndex = entry.getKey();
                        // System.out.println(realIndex);
                    }
                }
                // System.out.println(realIndex);

                for (int k = realIndex; k < j; k++) {
                    Ops.add(PosTupleMap.get(k));
                    // System.out.print(NegTupleMap.get(k) + ", ");
                }

                return Ops;
            }
        }

        else if (i >= 0 & j < 0) {

            if (i > PosTupleIndex || j < NegTupleIndex) {
                System.out.println("Invalid index!");
            }

            else {

                Object NegEqI = NegTupleMap.get(j);
                int realIndex = 0;

                for (Entry<Integer, Object> entry : PosTupleMap.entrySet()) {
                    // System.out.println(entry.getKey() + " " + entry.getValue());
                    if (entry.getValue().equals(NegEqI)) {
                        // System.out.println(entry.getKey());
                        realIndex = entry.getKey();
                        // System.out.println(realIndex);
                    }
                }

                for (int k = i; k < realIndex; k++) {
                    Ops.add(PosTupleMap.get(k));
                }

                return Ops;
            }

        }

        return Ops;
    }

    // for printing T[i:]
    public ArrayList<Object> SliceLeft(String s1) {
        int i = makeInt(s1);

        ArrayList<Object> sliceLeft = new ArrayList<Object>();

        if (i >= 0) {
            if (i >= PosTupleIndex + 1) {
                System.out.println("Out of bounds!");
            }

            else {

                // System.out.print("The values in the tuple are: ");

                for (int k = i; k < PosTupleMap.size(); k++) {

                    sliceLeft.add(PosTupleMap.get(k));
                    // System.out.print(PosTupleMap.get(k) + ", ");
                }

                // System.out.println();
                return sliceLeft;
            }
        }

        else {

            if (i <= NegTupleIndex - 1) {
                System.out.println("Invalid index");
            }

            else {

                // System.out.print("The values in the tuple are: ");

                for (int k = i; k <= -1; k++) {

                    sliceLeft.add(NegTupleMap.get(k));
                    // System.out.print(NegTupleMap.get(k) + ", ");
                }

                return sliceLeft;
                // System.out.println();

            }
        }
        return sliceLeft;
    }

    public ArrayList<Object> SliceRight(String s1) {
        int i = makeInt(s1);

        ArrayList<Object> sliceRight = new ArrayList<Object>();

        if (i >= 0) {
            if (i >= PosTupleIndex + 2) {
                System.out.println("Out of bounds!");
            }

            else {
                // System.out.print("The values in the tuple are: ");

                for (int k = 0; k < i; k++) {

                    sliceRight.add(PosTupleMap.get(k));
                    // System.out.print(PosTupleMap.get(k) + ", ");
                }

                return sliceRight;
                // System.out.println();
            }
        }

        else {

            int size = -NegTupleMap.size();

            if (i < size) {
                System.out.println("Invalid index!");
            }

            else {

                // System.out.print("The values in the tuple are: ");

                for (int k = size; k < i; k++) {

                    sliceRight.add(NegTupleMap.get(k));
                    // System.out.print(NegTupleMap.get(k) + ", ");

                }

                return sliceRight;
            }
        }

        return sliceRight;
    }

    // printing assigned variables' value
    public void VarPrinting(Object x) {
        System.out.println(varMap.get(x));
    }

    public void print(String x) {
        String s = x.replace("'", "");
        System.out.println(s);
    }

    // check whether we're printing a variable's value or just a normal value
    // and call proper function for it
    public void checkForPrinting(Object x) {

        if (varMap.containsKey(x)) {
            VarPrinting(x);
        }

        else {
            print((String) x);
        }

    }

    // adding 2 or more integers
    public int addInt(String x) {
        int i = Integer.parseInt(x);
        return i;
    }

    // creating an integer from a string for addition
    public int makeInt(String x) {
        int i = Integer.parseInt(x);
        return i;
    }

    // removing quotations from strings for concatenation
    public String removeQuoteString(String x) {
        String s = x.replace("'", "");
        return s;
    }

}