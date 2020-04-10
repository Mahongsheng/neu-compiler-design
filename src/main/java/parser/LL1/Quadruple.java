package parser.LL1;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Quadruple {
    private char operation;
    private char leftData;
    private char rightData;
    private char result;
}
