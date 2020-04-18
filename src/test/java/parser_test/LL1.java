package parser_test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import parser.LL1.Quadruple;

/**
 * 测试LL(1)分析是否成功
 *
 * @author 软英1702 马洪升
 */
public class LL1 {

    parser.LL1.LL1 ll1 = new parser.LL1.LL1();

    private parser.LL1.AnalysisTable analysisTable = new parser.LL1.AnalysisTable();

    @Test
    public void testAnalysisTable() {
        analysisTable.initAll();
        System.out.println("First集： " + analysisTable.getFirst());
        System.out.println("Follow集： " + analysisTable.getFollow());
        System.out.println("Select集： " + analysisTable.getSelect());
        System.out.println(analysisTable.judgeIfLL1() ? "该语句为LL(1)文法" : "该语句不是LL(1)文法");
        analysisTable.showAnalysisTable();
    }

    @Test
    public void testAnalysisTableWithNotLL1() {
        String[] wrongGrammar = {"T->TF"};
        analysisTable.setGrammar(wrongGrammar);
        analysisTable.initAll();
    }

    @Test
    public void testAdd() {
        System.out.println("输入：a+b;");
        ll1.LL1Control("a+b;");
        ll1.showQT();
        Quadruple add = new Quadruple('+', "a", "b", "t1");
        Assertions.assertEquals(add, ll1.getQT().get(0));
    }

    @Test
    public void testSubtraction() {
        System.out.println("输入：a-b;");
        ll1.LL1Control("a-b;");
        ll1.showQT();
        Quadruple subtraction = new Quadruple('-', "a", "b", "t1");
        Assertions.assertEquals(subtraction, ll1.getQT().get(0));
    }

    @Test
    public void testMultiply() {
        System.out.println("输入：a*b;");
        ll1.LL1Control("a*b;");
        ll1.showQT();
        Quadruple multiply = new Quadruple('*', "a", "b", "t1");
        Assertions.assertEquals(multiply, ll1.getQT().get(0));
    }

    @Test
    public void testDivide() {
        System.out.println("输入：a/b;");
        ll1.LL1Control("a/b;");
        ll1.showQT();
        Quadruple divide = new Quadruple('/', "a", "b", "t1");
        Assertions.assertEquals(divide, ll1.getQT().get(0));
    }

    @Test
    public void testCombination() {
        System.out.println("输入：(a+b-c)*d/e;");
        ll1.LL1Control("(a+b-c)*d/e;");
        ll1.showQT();
        Quadruple add = new Quadruple('+', "a", "b", "t1");
        Quadruple subtraction = new Quadruple('-', "t1", "c", "t2");
        Quadruple multiply = new Quadruple('*', "t2", "d", "t3");
        Quadruple divide = new Quadruple('/', "t3", "e", "t4");
        Assertions.assertEquals(add, ll1.getQT().get(0));
        Assertions.assertEquals(subtraction, ll1.getQT().get(1));
        Assertions.assertEquals(multiply, ll1.getQT().get(2));
        Assertions.assertEquals(divide, ll1.getQT().get(3));
    }

    @Test
    public void testWrongGrammar() {
        System.out.println("输入：aaa;");
        Assertions.assertFalse(ll1.LL1Control("aaa"));
        ll1.showQT();
    }
}
