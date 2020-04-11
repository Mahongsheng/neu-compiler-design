package parser.LL1;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Quadruple {
    private char operation;
    private String leftData;
    private String rightData;
    private String result;
}
