package parser_test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import parser.LL1.Quadruple;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LL1 {

    parser.LL1.LL1 ll1 = new parser.LL1.LL1();

    @Test
    public void testAdd() {
        ll1.LL1Control("a+b;");
        ll1.showQT();
        Quadruple add = new Quadruple('+', "a", "b", "t1");
        Assertions.assertEquals(add, ll1.getQT().get(0));
    }

    @Test
    public void testSubtraction() {
        ll1.LL1Control("a-b;");
        ll1.showQT();
        Quadruple subtraction = new Quadruple('-', "a", "b", "t1");
        Assertions.assertEquals(subtraction, ll1.getQT().get(0));
    }

    @Test
    public void testMultiply() {
        ll1.LL1Control("a*b;");
        ll1.showQT();
        Quadruple multiply = new Quadruple('*', "a", "b", "t1");
        Assertions.assertEquals(multiply, ll1.getQT().get(0));
    }

    @Test
    public void testDivide() {
        ll1.LL1Control("a/b;");
        ll1.showQT();
        Quadruple divide = new Quadruple('/', "a", "b", "t1");
        Assertions.assertEquals(divide, ll1.getQT().get(0));
    }

    @Test
    public void testCombination() {
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
}
