package parser.LL1;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class Quadruple {
    private char operation;
    private String leftData;
    private String rightData;
    private String result;

    @Override
    public String toString() {
        return "(" + this.getOperation() + "," + this.getLeftData() + "," + this.getRightData() + "," + this.getResult() + ")";
    }
}
