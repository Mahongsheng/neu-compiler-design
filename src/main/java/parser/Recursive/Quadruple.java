package parser.Recursive;

import lombok.*;

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
