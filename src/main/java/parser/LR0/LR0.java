package parser.LR0;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Stack;

@Getter
public class LR0 {

    private Stack<String> SYN = new Stack<>();
    private Stack<String> SEM = new Stack<>();
    private ArrayList<Quadruple> QT = new ArrayList<>();
    private String[] grammar = {"Z->E", "E->E+T{G+}", "E->T", "T->T*F{G*}", "T->F", "F->i{Pi}", "F->(E)"};

    //我们需要一个java的G(Z)
    private String[][] analysisTable = {
            //i   +   *    (   )   ;   E   T   F
            {"i8", "", "", "(9", "", "", "E1", "T4", "F7"},//0
            {"", "+2", "", "", "", "OK", "", "", ""},//1
            {"i8", "", "", "(9", "", "", "", "T3", "F7"},//2
            {"", "r(1)", "*5", "", "r(1)", "r(1)", "", "", ""},//3
            {"", "r(2)", "*5", "", "r(2)", "r(2)", "", "", ""},//4
            {"i8", "", "", "(9", "", "", "", "", "F6"},// 5
            {"r(3)", "r(3)", "r(3)", "r(3)", "r(3)", "r(3)", "", "", ""},// 6
            {"r(4)", "r(4)", "r(4)", "r(4)", "r(4)", "r(4)", "", "", ""},// 7
            {"r(5)", "r(5)", "r(5)", "r(5)", "r(5)", "r(5)", "", "", ""},// 8
            {"i8", "", "", "(9", "", "", "E10", "T4", "F7"},// 9
            {"", "+2", "", "", ")11", "", "", "", ""},// 10
            {"r(6)", "r(6)", "r(6)", "r(6)", "r(6)", "r(6)", "", "", ""},// 11
    };

    private void init() {
        SYN = new Stack<>();
        SEM = new Stack<>();
        QT = new ArrayList<>();
    }

    private String findFromAnalysisTable(int k, char w) {
        int row = k, column = -1;
        char[] wInTable = {'+', '*', '(', ')', ';'};
        if (w == 'E') column = 6;
        else if (w == 'T') column = 7;
        else if (w == 'F') column = 8;
        else if (Character.isLetter(w) || Character.isDigit(w)) column = 0;
        else {
            for (int l = 0; l < wInTable.length; l++) {
                if (wInTable[l] == w) column = l + 1;
            }
        }
        return analysisTable[row][column];
    }

    public boolean LR0Control(String LR0String) {
        init();
        int k = 0;
        char w = 0;
        int state = 0;
        int cursorInString = 0;
        String resultFromTable = "";
        int tCount = 1;
        char lastW = 0;
        SYN.push(";0");
        while (true) {
            switch (state) {
                case 0:
                    lastW = w;
                    w = LR0String.charAt(cursorInString++);
                    state = 1;
                    break;
                case 1:
                    k = Integer.parseInt(SYN.peek().substring(1));
                    resultFromTable = findFromAnalysisTable(k, w);
                    if (resultFromTable.equals("")) {
                        return false;
                    } else if (resultFromTable.equals("OK")) {
                        return true;
                    } else if (resultFromTable.charAt(0) == 'r' && resultFromTable.charAt(1) == '(') {
                        state = 2;
                    } else {
                        //移进
                        SYN.push(resultFromTable);
                        state = 0;
                    }
                    break;
                case 2:
                    int grammarIndex = Integer.parseInt(resultFromTable.split("\\(")[1].split("\\)")[0]);
                    char Vn = grammar[grammarIndex].charAt(0);
                    String rightOfGrammar = grammar[grammarIndex].split("->")[1];
                    for (int i = 0; i < rightOfGrammar.length(); i++) {
                        if (rightOfGrammar.charAt(i) != '{') {
                            SYN.pop();
                        } else {//发现运算符
                            if (rightOfGrammar.charAt(i + 1) == 'G') {
                                char symbol = rightOfGrammar.charAt(i + 2);
                                String rightData = SEM.pop();
                                String leftData = SEM.pop();
                                Quadruple quadruple = new Quadruple(symbol, leftData, rightData, "t" + tCount++);
                                QT.add(quadruple);
                                SEM.push(quadruple.getResult());
                            } else if (rightOfGrammar.charAt(i + 1) == 'P') {
                                SEM.push(String.valueOf(lastW));
                            } else {
                                return false;
                            }
                            break;
                        }
                    }
                    k = Integer.parseInt(SYN.peek().substring(1));
                    SYN.push(findFromAnalysisTable(k, Vn));
                    state = 1;
                    break;
            }
        }
    }

    public void showQT() {
        QT.forEach(quadruple -> System.out.println(quadruple.toString()));
    }

    public static void main(String[] args) {
        LR0 lr0 = new LR0();
        lr0.LR0Control("a+b*c;");
        lr0.showQT();
    }
}
