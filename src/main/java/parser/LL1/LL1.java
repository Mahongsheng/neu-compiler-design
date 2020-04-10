package parser.LL1;

import java.util.*;

public class LL1 {

    private AnalysisTable analysisTableObj = new AnalysisTable();
    private HashMap<Character, ArrayList<AnalysisTableItem>> analysisTable = analysisTableObj.getAnalysisTable();
    private String[] grammar = analysisTableObj.getGrammar();
    private HashSet<Character> Vn = analysisTableObj.getVn();//非终结符集
    private HashSet<Character> Vt = analysisTableObj.getVt();//终结符集合
    private char firstVn = analysisTableObj.getGrammar()[0].charAt(0);

    public boolean LL1Control(String ll1String) {
        int cursorInStack = ll1String.length() - 1;
        Stack<Character> stack = new Stack<>();
        stack.push(ll1String.charAt(cursorInStack--));
        stack.push(firstVn);
        int state = 1;
        char w = 0;
        char x = 0;
        while (true) {
            switch (state) {
                case 1:
                    w = ll1String.charAt(cursorInStack--);// NEXT(w)
                    state = 2;
                    break;
                case 2:
                    x = stack.pop();// POP(x)
                    state = 3;
                    break;
                case 3:
                    if (Vt.contains(x)) {
                        state = 4;
                    } else {
                        state = 5;
                    }
                    break;
                case 4:
                    if (x == w) {
                        state = 1;
                    } else {
                        return false;
                    }
                    break;
                case 5:
                    if (Vn.contains(x)) {
                        int changeToNextGrammar = -1;
                        for (Map.Entry<Character, ArrayList<AnalysisTableItem>> items : analysisTable.entrySet()) {
                            if (items.getKey() == x) {
                                for (AnalysisTableItem item : items.getValue()) {
                                    if (item.getVt() == w) changeToNextGrammar = item.getChangeToNextGrammar();
                                }
                            }
                        }
                        if (changeToNextGrammar == -1) return false;
                        else {
                            String rightOfNextGrammar = grammar[changeToNextGrammar].split("->")[1];
                            for (int i = rightOfNextGrammar.length() - 1; i >= 0; i--) {
                                stack.push(rightOfNextGrammar.charAt(i));
                            }
                            state = 2;
                        }
                    } else {
                        state = 6;
                    }
                    break;
                case 6:
                    return w == ';';
            }
        }
    }

    public static void main(String[] args) {
        LL1 ll1 = new LL1();
        System.out.println(ll1.LL1Control("a-b;"));
    }
}
