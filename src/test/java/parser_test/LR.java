package parser_test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import parser.LR.Quadruple;

/**
 * 测试LR分析法是否成功
 *
 * @author 软英1702 马洪升
 */
public class LR {

    parser.LR.LR LR = new parser.LR.LR();

    @Test
    public void testAdd() {
        System.out.println("输入：a+b;");
        LR.LR0Control("a+b;");
        LR.showQT();
        Quadruple add = new Quadruple('+', "a", "b", "t1");
        Assertions.assertEquals(add, LR.getQT().get(0));
    }


    @Test
    public void testMultiply() {
        System.out.println("输入：a*b;");
        LR.LR0Control("a*b;");
        LR.showQT();
        Quadruple multiply = new Quadruple('*', "a", "b", "t1");
        Assertions.assertEquals(multiply, LR.getQT().get(0));
    }

    @Test
    public void testCombination() {
        System.out.println("输入：(a+b)*c;");
        LR.LR0Control("(a+b)*c;");
        LR.showQT();
        Quadruple add = new Quadruple('+', "a", "b", "t1");
        Quadruple multiply = new Quadruple('*', "t1", "c", "t2");
        Assertions.assertEquals(add, LR.getQT().get(0));
        Assertions.assertEquals(multiply, LR.getQT().get(1));
    }

    @Test
    public void testWrongGrammar() {
        System.out.println("输入：aaa;");
        Assertions.assertFalse(LR.LR0Control("aaa"));
    }
}
