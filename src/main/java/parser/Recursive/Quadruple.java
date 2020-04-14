package parser.Recursive;

import lombok.*;

/**
 * 四元式类
 *
 * @author 软英1702 马洪升
 */
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
