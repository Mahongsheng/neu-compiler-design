package lexer;

import lombok.Data;
import lombok.AllArgsConstructor;

/**
 * @author 马洪升
 */
@Data
@AllArgsConstructor
public class Token {

    private int type;
    private String value;

    public String toString() {
        return "<" + this.type + ", " + this.value + ">";
    }
}
