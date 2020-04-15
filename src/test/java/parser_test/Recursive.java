package parser_test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import parser.Recursive.Quadruple;

/**
 * 测试递归子程序法是否成功
 *
 * @author 软英1702 马洪升
 */
public class Recursive {

    parser.Recursive.Recursive recursive = new parser.Recursive.Recursive();

    @Test
    public void testAdd() {
        Assertions.assertDoesNotThrow(() -> {
            recursive.beginToAnalysis("a+b;");
        });
        recursive.showQT();
        Quadruple add = new Quadruple('+', "a", "b", "t1");
        Assertions.assertEquals(add, recursive.getQT().get(0));
    }

    @Test
    public void testSubtraction() {
        Assertions.assertDoesNotThrow(() -> {
            recursive.beginToAnalysis("a-b;");
        });
        recursive.showQT();
        Quadruple subtraction = new Quadruple('-', "a", "b", "t1");
        Assertions.assertEquals(subtraction, recursive.getQT().get(0));
    }

    @Test
    public void testMultiply() {
        Assertions.assertDoesNotThrow(() -> {
            recursive.beginToAnalysis("a*b;");
        });
        recursive.showQT();
        Quadruple multiply = new Quadruple('*', "a", "b", "t1");
        Assertions.assertEquals(multiply, recursive.getQT().get(0));
    }

    @Test
    public void testDivide() {
        Assertions.assertDoesNotThrow(() -> {
            recursive.beginToAnalysis("a/b;");
        });
        recursive.showQT();
        Quadruple divide = new Quadruple('/', "a", "b", "t1");
        Assertions.assertEquals(divide, recursive.getQT().get(0));
    }

    @Test
    public void testCombination() {
        Assertions.assertDoesNotThrow(() -> {
            recursive.beginToAnalysis("(a+b-c)*d/e;");
        });
        recursive.showQT();
        Quadruple add = new Quadruple('+', "a", "b", "t1");
        Quadruple subtraction = new Quadruple('-', "t1", "c", "t2");
        Quadruple multiply = new Quadruple('*', "t2", "d", "t3");
        Quadruple divide = new Quadruple('/', "t3", "e", "t4");
        Assertions.assertEquals(add, recursive.getQT().get(0));
        Assertions.assertEquals(subtraction, recursive.getQT().get(1));
        Assertions.assertEquals(multiply, recursive.getQT().get(2));
        Assertions.assertEquals(divide, recursive.getQT().get(3));
    }

    @Test
    public void testERR0() {
        Assertions.assertThrows(Exception.class, () -> {
            recursive.beginToAnalysis("a+b");
        });
    }

    @Test
    public void testERR1() {
        Assertions.assertThrows(Exception.class, () -> {
            recursive.beginToAnalysis("&a+b;");
        });
    }

    @Test
    public void testERR2() {
        Assertions.assertThrows(Exception.class, () -> {
            recursive.beginToAnalysis("(a+b;");
        });
    }
}