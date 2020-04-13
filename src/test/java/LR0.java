import org.junit.jupiter.api.Test;
import parser.LR0.Quadruple;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LR0 {

    parser.LR0.LR0 LR0 = new parser.LR0.LR0();

    @Test
    public void testAdd() {
        LR0.LR0Control("a+b;");
        LR0.showQT();
        Quadruple add = new Quadruple('+', "a", "b", "t1");
        assertEquals(add, LR0.getQT().get(0));
    }


    @Test
    public void testMultiply() {
        LR0.LR0Control("a*b;");
        LR0.showQT();
        Quadruple multiply = new Quadruple('*', "a", "b", "t1");
        assertEquals(multiply, LR0.getQT().get(0));
    }

    @Test
    public void testCombination() {
        LR0.LR0Control("(a+b)*c;");
        LR0.showQT();
        Quadruple add = new Quadruple('+', "a", "b", "t1");
        Quadruple multiply = new Quadruple('*', "t1", "c", "t2");
        assertEquals(add, LR0.getQT().get(0));
        assertEquals(multiply, LR0.getQT().get(1));
    }
}
