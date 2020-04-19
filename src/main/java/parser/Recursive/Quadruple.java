package parser.Recursive;

import java.util.Objects;

/**
 * 四元式类
 *
 * @author 软英1702 马洪升
 */

public class Quadruple {
    private char operation;
    private String leftData;
    private String rightData;
    private String result;

    public Quadruple(char operation, String leftData, String rightData, String result) {
        this.operation = operation;
        this.leftData = leftData;
        this.rightData = rightData;
        this.result = result;
    }

    public char getOperation() {
        return operation;
    }

    public String getLeftData() {
        return leftData;
    }

    public String getRightData() {
        return rightData;
    }

    public String getResult() {
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Quadruple quadruple = (Quadruple) o;
        return operation == quadruple.operation &&
                Objects.equals(leftData, quadruple.leftData) &&
                Objects.equals(rightData, quadruple.rightData) &&
                Objects.equals(result, quadruple.result);
    }

    @Override
    public String toString() {
        return "(" + this.getOperation() + "," + this.getLeftData() + "," + this.getRightData() + "," + this.getResult() + ")";
    }
}
