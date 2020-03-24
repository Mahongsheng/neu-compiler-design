package lexer;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;

/**
 * @author 马洪升
 */
@Data
public class Token {

    private int type;
    private String value;

    public Token(int type, String value) {
        this.type = type;
        this.value = value;
    }

    public void display() {
        System.out.println("<" + this.type + ", " + this.value + ">");
    }
}
