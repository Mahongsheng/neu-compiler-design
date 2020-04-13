package lexer;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 马洪升
 */
@Getter
@AllArgsConstructor
public class Token {

    private int type;
    private String value;

    public String toString() {
        return "<" + this.type + ", " + this.value + ">";
    }
}
