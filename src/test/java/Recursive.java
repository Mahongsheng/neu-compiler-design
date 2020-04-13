import org.junit.jupiter.api.Test;
import parser.Recursive.Quadruple;

import static org.junit.jupiter.api.Assertions.*;

public class Recursive {

    parser.Recursive.Recursive recursive = new parser.Recursive.Recursive();

    @Test
    public void testAdd() {
        recursive.beginToAnalysis("a+b;");
        recursive.showQT();
        Quadruple add = new Quadruple('+', "a", "b", "t1");
        assertEquals(add, recursive.getQT().get(0));
    }

    @Test
    public void testSubtraction() {
        recursive.beginToAnalysis("a-b;");
        recursive.showQT();
        Quadruple subtraction = new Quadruple('-', "a", "b", "t1");
        assertEquals(subtraction, recursive.getQT().get(0));
    }

    @Test
    public void testMultiply() {
        recursive.beginToAnalysis("a*b;");
        recursive.showQT();
        Quadruple multiply = new Quadruple('*', "a", "b", "t1");
        assertEquals(multiply, recursive.getQT().get(0));
    }

    @Test
    public void testDivide() {
        recursive.beginToAnalysis("a/b;");
        recursive.showQT();
        Quadruple divide = new Quadruple('/', "a", "b", "t1");
        assertEquals(divide, recursive.getQT().get(0));
    }

    @Test
    public void testCombination() {
        recursive.beginToAnalysis("(a+b-c)*d/e;");
        recursive.showQT();
        Quadruple add = new Quadruple('+', "a", "b", "t1");
        Quadruple subtraction = new Quadruple('-', "t1", "c", "t2");
        Quadruple multiply = new Quadruple('*', "t2", "d", "t3");
        Quadruple divide = new Quadruple('/', "t3", "e", "t4");
        assertEquals(add, recursive.getQT().get(0));
        assertEquals(subtraction, recursive.getQT().get(1));
        assertEquals(multiply, recursive.getQT().get(2));
        assertEquals(divide, recursive.getQT().get(3));
    }
}