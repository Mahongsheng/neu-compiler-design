package parser.LR;


import java.util.ArrayList;
import java.util.Stack;

/**
 * LR(0)分析法
 *
 * @author 软英1702 马洪升
 */
public class LR {

    private String[] grammar = {"Z->E", "E->E+T{G+}", "E->T", "T->T*F{G*}", "T->F", "F->i{Pi}", "F->(E)"};
    private Stack<String> SYN;//语法栈
    private Stack<String> SEM;//语义栈
    private ArrayList<Quadruple> QT;//四元式列表

    public ArrayList<Quadruple> getQT() {
        return QT;
    }

    //SLR(1)分析表
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

    /**
     * 初始化列表和栈，便于多次分析
     */
    private void init() {
        SYN = new Stack<>();
        SEM = new Stack<>();
        QT = new ArrayList<>();
    }

    /**
     * LR控制程序
     *
     * @param LR0String
     * @return true：无问题；false：有问题
     */
    public boolean LR0Control(String LR0String) {
        init();
        int k = 0;
        char w = 0;
        int state = 1;
        int cursorInString = 0;
        String resultFromTable = "";
        int tCount = 1;
        char lastW = 0;
        SYN.push(";0");
        while (true) {
            switch (state) {
                case 1:
                    lastW = w;
                    w = LR0String.charAt(cursorInString++);
                    state = 2;
                    break;
                case 2:
                    k = Integer.parseInt(SYN.peek().substring(1));
                    resultFromTable = findFromAnalysisTable(k, w);
                    if (resultFromTable.equals("")) {
                        return false;
                    } else if (resultFromTable.equals("OK")) {
                        return true;
                    } else if (resultFromTable.charAt(0) == 'r' && resultFromTable.charAt(1) == '(') {
                        state = 3;
                    } else {
                        //移进
                        SYN.push(resultFromTable);
                        state = 1;
                    }
                    break;
                case 3:
                    int grammarIndex = Integer.parseInt(resultFromTable.split("\\(")[1].split("\\)")[0]);
                    char Vn = grammar[grammarIndex].charAt(0);
                    String rightOfGrammar = grammar[grammarIndex].split("->")[1];
                    for (int i = 0; i < rightOfGrammar.length(); i++) {
                        if (rightOfGrammar.charAt(i) != '{') {
                            SYN.pop();
                        } else {
                            //发现动作符号并执行
                            if (rightOfGrammar.charAt(i + 1) == 'G') {
                                char symbol = rightOfGrammar.charAt(i + 2);
                                String rightData = SEM.pop();
                                String leftData = SEM.pop();
                                Quadruple quadruple = new Quadruple(symbol, leftData, rightData, "t" + tCount++);
                                QT.add(quadruple);
                                SEM.push(quadruple.getResult());
                            } else if (rightOfGrammar.charAt(i + 1) == 'P') {
                                SEM.push(String.valueOf(lastW));
                            }
                            break;
                        }
                    }
                    k = Integer.parseInt(SYN.peek().substring(1));
                    SYN.push(findFromAnalysisTable(k, Vn));
                    state = 2;
                    break;
            }
        }
    }

    /**
     * 查询SLR(1)分析表
     *
     * @param k 当前状态
     * @param w 当前字符
     * @return 查询结果
     */
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

    /**
     * 展示四元式区中结果
     */
    public void showQT() {
        QT.forEach(quadruple -> System.out.println(quadruple.toString()));
    }
}
