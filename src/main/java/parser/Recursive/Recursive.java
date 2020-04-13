package parser.Recursive;

import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;
import java.util.Stack;

@Getter
public class Recursive {

    private LinkedList<Quadruple> QT;//四元式队列
    private LinkedList<Character> allCharOfString;

    private Stack<String> SEM = new Stack<>();    //语义栈
    private char w;//当前字符
    private int tCount;

    private void init() {
        QT = new LinkedList<>();
        allCharOfString = new LinkedList<>();
        SEM = new Stack<>();
        tCount = 1;
    }

    public void beginToAnalysis(String arithmeticCode) {
        init();
        for (char eachChar : arithmeticCode.toCharArray()) {
            allCharOfString.add(eachChar);
        }
        if (!allCharOfString.isEmpty())
            w = allCharOfString.poll();
        E();
    }

    public void showQT() {
        QT.forEach(quadruple -> System.out.println(quadruple.toString()));
    }

    private void E() {
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

    private void T() {
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

    private void F() {
        if ((w >= '0' && w <= '9') || (w >= 'a' && w <= 'z')) {
            SEM.push(String.valueOf(w));
            if (!allCharOfString.isEmpty()) w = allCharOfString.poll();
        } else if (w == '(') {
            if (!allCharOfString.isEmpty()) w = allCharOfString.poll();
            E();
            if (w == ')') {
                if (!allCharOfString.isEmpty()) w = allCharOfString.poll();
            } else {
                System.out.println("err2");
            }
        } else {
            System.out.println("err1");
        }
    }

    private void GEQ(char action) {
        String rightData = SEM.pop();
        String leftData = SEM.pop();
        Quadruple fqt = new Quadruple(action, leftData, rightData, "t" + tCount++);
        SEM.push(fqt.getResult());
        QT.add(fqt);
    }
}
