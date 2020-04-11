package parser.LL1;

import java.util.*;

public class LL1 {

    private AnalysisTable analysisTableObj = new AnalysisTable();
    private HashMap<Character, ArrayList<AnalysisTableItem>> analysisTable = analysisTableObj.getAnalysisTable();
    //    private String[] grammar = analysisTableObj.getGrammar();
    private HashSet<Character> Vn = analysisTableObj.getVn();//非终结符集
    private HashSet<Character> Vt = analysisTableObj.getVt();//终结符集合
    private char firstVn = analysisTableObj.getGrammar()[0].charAt(0);

    private Stack<String> SEM = new Stack<>();
    private ArrayList<Quadruple> QT = new ArrayList<>();
    private String[] grammar = {"E->TY", "Y->+T{G+}Y", "Y->-T{G-}Y", "Y->e", "T->FX", "X->*F{G*}X", "X->/F{G/}X", "X->e", "F->i{Pi}", "F->(E)"};

    public boolean LL1Control(String ll1String) {
        int cursorInString = 0;
        Stack<Character> stack = new Stack<>();
        stack.push(ll1String.charAt(ll1String.length() - 1));
        stack.push(firstVn);
        int state = 1;
        char lastW = 0;
        char w = 0;
        char x = 0;
        int tCount = 1;
        while (true) {
            switch (state) {
                case 1:
                    lastW = w;
                    w = ll1String.charAt(cursorInString++);// NEXT(w)
                    state = 2;
                    break;
                case 2:
                    x = stack.pop();// POP(x)
                    //判断是否为动作符号
                    if (x == '{') {
                        //执行动作符号
                        char operation = stack.pop();
                        char symbol = stack.pop();
                        if (stack.pop() == '}') {
                            if (operation == 'G') {
                                String rightData = SEM.pop();
                                String leftData = SEM.pop();
                                Quadruple quadruple = new Quadruple(symbol, leftData, rightData, "t" + tCount++);
                                QT.add(quadruple);
                                SEM.push(quadruple.getResult());
                            } else if (operation == 'P') {
                                SEM.push(String.valueOf(lastW));
                            }
                        }
                        state = 2;
                    } else
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
                    } else if (x == 'i' && Character.isLetter(w)) {
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
                                    if (item.getVt() == w || (item.getVt() == 'i' && Character.isLetter(w))) {
                                        changeToNextGrammar = item.getChangeToNextGrammar();
                                        break;
                                    }
                                }
                                break;
                            }
                        }
                        if (changeToNextGrammar == -1) return false;
                        else {
                            String rightOfNextGrammar = grammar[changeToNextGrammar].split("->")[1];
                            for (int i = rightOfNextGrammar.length() - 1; i >= 0; i--) {
                                if (rightOfNextGrammar.charAt(i) != 'e') stack.push(rightOfNextGrammar.charAt(i));
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
        System.out.println(ll1.LL1Control("(a+b)*c;"));
        System.out.println(ll1.QT);
    }
}
