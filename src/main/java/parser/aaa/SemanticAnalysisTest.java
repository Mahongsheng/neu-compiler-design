package parser.aaa;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class SemanticAnalysisTest {

    public static String LLStr[] = {"YT", "YgT+", "YgT-", "n", "XF", "XgF*", "XgF/", "n", "p", ")E("};
    public static Stack operate = new Stack();
    public static Stack syn = new Stack();    //分析算符
    public static Stack sem = new Stack();    //语义栈
    public static AnalysisTable analysisTable = new AnalysisTable();

    public static int j = 0;

    public static Queue<FQT> qt = new LinkedList();//四元式队列

    public static void main(String[] args) throws Exception {
        //生成分析表
        analysisTable.makeAnalysisTable();
//		makeAnalysisTable();
        char x = ' ';
        char w = ' ';
        //char fileName[10];
        syn.push('#');
        syn.push('E');
        String fileName = new String("src/main/java/parser/aaa/text.txt");
        File file = new File(fileName);
        Reader reader = null;
        // 一次读一个字符
        reader = new InputStreamReader(new FileInputStream(file));
        int tempchar = reader.read();
        w = (char) tempchar;
        while (true) {
            x = getSynElement();
            if (w == '#' && x == '#') {
                break;
            } else if (w != x) {
                dealCurrentChar(x, w);
            } else {//相等就读下一个字符
                tempchar = reader.read();
                w = (char) tempchar;
            }
        }
        reader.close();
        System.out.println("四元式如下：");
        for (FQT fqt : qt) {
            System.out.println("(" + fqt.getOp() + "," + fqt.getLeftdata() + "," + fqt.getRightdata() + "," + fqt.getResult() + ")");
        }
    }

    //处理当前字符
    private static void dealCurrentChar(char x, char w) {
        //判断是是否为动作操作
        if (x == 'p' || x == 'g' || x == 'n') {
            doAction(x);
        } else {
            doSearch(x, w);
        }
    }

    //根据分析表处理字符
    private static void doSearch(char x, char w) {
        int index = searchAnalysisTable(x, w);
        String str = LLStr[index];
//	System.out.println(w);
        if (index == 8)
            str += w;
        int len = str.length();
        if (len == 1 && str.charAt(0) == 'n') {
            return;
        }
        //i+-
        if (str.indexOf("p") != -1) {
            operate.push(w);
        } else if (str.indexOf("g") != -1) {
            operate.push(w);
        } else {
        }


        for (int i = 0; i < len; i++) {
//		System.out.println(str.charAt(i));
            syn.push(str.charAt(i));
        }
    }

    //查分析表
    private static int searchAnalysisTable(char x, char w) {
        String str = "";
//	System.out.print(x+"  ");
        //查分析表
        char changeCh = w;//因为i的符号判断需要包括其他所有字符
        for (int i = 0; i < analysisTable.analysisTable.size(); i++) {
            if (analysisTable.analysisTable.get(i).getNChar() == x) {
                for (int j = 0; j < analysisTable.analysisTable.get(i).getNodes().size(); j++) {
                    if (w >= 'a' && w <= 'z')
                        changeCh = 'i';
                    if (analysisTable.analysisTable.get(i).getNodes().get(j).getType() == changeCh)
                        return analysisTable.analysisTable.get(i).getNodes().get(j).getChangeNum();
                }
            }
        }
        return -1;
    }

    //执行动作符号
    private static void doAction(char x) {
        char i;
        switch (x) {

            case 'p':
                if (operate.empty()) {
                    System.out.println("error，OperateStackisEmpty\n");
                    System.exit(0);
                }
                i = (char) operate.pop();
//		System.out.println("p  "+i);
                sem.push(i);
                break;

            case 'g':
                if (operate.empty()) {
                    System.out.println("error，OperateStackisEmpty\n");
                    System.exit(0);
                }
                i = (char) operate.pop();
                makeFQT(i);
                break;

            default:
                System.out.println("error\n");
        }
    }

    //表达式四元式生成函数
    private static void makeFQT(char i) {
        char newResult;
        FQT newFQT = new FQT();
        if (sem.size() < 2) {
            System.out.println("SemaniticStackisNull\n");
            System.exit(0);
        }
        char rightOper = (char) sem.pop();
        char leftOper = (char) sem.pop();
        newFQT.setLeftdata(leftOper);
        newFQT.setRightdata(rightOper);
        newFQT.setOp(i);
        newResult = (char) ('A' + (j));
        newFQT.setResult(newResult);
        qt.offer(newFQT);
        sem.push(newFQT.getResult());
        j++;
    }

    //获得语法栈栈顶符
    private static char getSynElement() {
        if (syn.empty()) {
            System.out.println("SemanticsGrammerStackisNull\n");
            System.exit(0);
        }
        char x = (char) syn.pop();
        return x;
    }

}
