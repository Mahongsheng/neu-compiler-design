package parser.Recursive;

import lombok.Getter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Stack;

/**
 * 递归子程序法
 *
 * @author 软英1702 马洪升
 */
@Getter
public class Recursive {

    private LinkedList<Character> allCharOfString;//待分析字符串
    private Stack<String> SEM;//语义栈
    private ArrayList<Quadruple> QT;//四元式列表
    private char w;//当前字符
    private int tCount;//四元式中t后系数

    /**
     * 初始化链表和栈，便于多次分析
     */
    private void init() {
        QT = new ArrayList<>();
        allCharOfString = new LinkedList<>();
        SEM = new Stack<>();
        tCount = 1;
    }

    /**
     * 将待分析字符拆分放入列表后开始分析
     *
     * @param arithmeticCode
     */
    public void beginToAnalysis(String arithmeticCode) throws Exception {
        init();
        for (char eachChar : arithmeticCode.toCharArray()) {
            allCharOfString.add(eachChar);
        }
        if (!allCharOfString.isEmpty())
            w = allCharOfString.poll();
        E();
        if (w != ';') {
            System.out.println("ERR0: 待识别字符串非正确算术表达式");
            throw new Exception("ERR0: 待识别字符串非正确算术表达式");
        }
        System.out.println(w);
    }

    /**
     * 子程序E
     */
    private void E() throws Exception {
        T();
        while (true) {
            if (w == '+') {
                if (!allCharOfString.isEmpty())
                    w = allCharOfString.poll();
                T();
                GEQ('+');
            } else if (w == '-') {
                if (!allCharOfString.isEmpty()) w = allCharOfString.poll();
                T();
                GEQ('-');
            } else {
                break;
            }
        }
    }

    /**
     * 子程序T
     */
    private void T() throws Exception {
        F();
        while (true) {
            if (w == '*') {
                if (!allCharOfString.isEmpty()) w = allCharOfString.poll();
                F();
                GEQ('*');
            } else if (w == '/') {
                if (!allCharOfString.isEmpty()) w = allCharOfString.poll();
                F();
                GEQ('/');
            } else {
                break;
            }
        }
    }

    /**
     * 子程序F
     */
    private void F() throws Exception {
        if ((w >= '0' && w <= '9') || (w >= 'a' && w <= 'z')) {
            SEM.push(String.valueOf(w));
            if (!allCharOfString.isEmpty()) w = allCharOfString.poll();
        } else if (w == '(') {
            if (!allCharOfString.isEmpty()) w = allCharOfString.poll();
            E();
            if (w == ')') {
                if (!allCharOfString.isEmpty()) w = allCharOfString.poll();
            } else {
                System.out.println("ERR2: 待识别字符串非正确算术表达式");
                throw new Exception("ERR2: 待识别字符串非正确算术表达式");
            }
        } else {
            System.out.println("ERR1: 待识别字符串非正确算术表达式");
            throw new Exception("ERR1: 待识别字符串非正确算术表达式");
        }
    }

    /**
     * 生成四元式
     *
     * @param action
     */
    private void GEQ(char action) {
        String rightData = SEM.pop();
        String leftData = SEM.pop();
        Quadruple fqt = new Quadruple(action, leftData, rightData, "t" + tCount++);
        SEM.push(fqt.getResult());
        QT.add(fqt);
    }

    /**
     * 展示四元式区中结果
     */
    public void showQT() {
        QT.forEach(quadruple -> System.out.println(quadruple.toString()));
    }
}
