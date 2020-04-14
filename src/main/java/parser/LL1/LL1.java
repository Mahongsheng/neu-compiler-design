package parser.LL1;

import lombok.Getter;

import java.util.*;

/**
 * LL1分析法
 *
 * @author 软英1702 马洪升
 */
@Getter
public class LL1 {
    private String[] grammar = {"E->TY", "Y->+T{G+}Y", "Y->-T{G-}Y", "Y->e", "T->FX", "X->*F{G*}X", "X->/F{G/}X", "X->e", "F->i{Pi}", "F->(E)"};
    private AnalysisTable analysisTableObj = new AnalysisTable();//分析表对象，用于获取终结符集，非终结符集，分析表
    private char firstVn = analysisTableObj.getGrammar()[0].charAt(0);
    private Stack<Character> SYN;//语法栈
    private Stack<String> SEM;//语义栈
    private ArrayList<Quadruple> QT;//四元式区

    /**
     * 初始化列表和栈，便于多次分析
     */
    private void init() {
        SYN = new Stack<>();
        SEM = new Stack<>();
        QT = new ArrayList<>();
    }

    /**
     * LL(1)控制程序
     *
     * @param LL1String
     * @return true：无问题；false：有问题
     */
    public boolean LL1Control(String LL1String) {
        init();
        int cursorInString = 0;
        SYN.push(LL1String.charAt(LL1String.length() - 1));
        SYN.push(firstVn);
        int state = 1;
        char lastW = 0;
        char w = 0;
        char x = 0;
        int tCount = 1;
        while (true) {
            switch (state) {
                case 1:
                    lastW = w;
                    w = LL1String.charAt(cursorInString++);// NEXT(w)
                    state = 2;
                    break;
                case 2:
                    x = SYN.pop();// POP(x)
                    //判断是否为动作符号
                    if (x == '{') {
                        //执行动作符号
                        char operation = SYN.pop();
                        char symbol = SYN.pop();
                        if (SYN.pop() == '}') {
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
                    if (analysisTableObj.getVt().contains(x)) {
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
                    if (analysisTableObj.getVn().contains(x)) {
                        int changeToNextGrammar = -1;
                        for (Map.Entry<Character, ArrayList<AnalysisTableItem>> items : analysisTableObj.getAnalysisTable().entrySet()) {
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
                                if (rightOfNextGrammar.charAt(i) != 'e') SYN.push(rightOfNextGrammar.charAt(i));
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

    /**
     * 展示四元式区中结果
     */
    public void showQT() {
        QT.forEach(quadruple -> System.out.println(quadruple.toString()));
    }
}
