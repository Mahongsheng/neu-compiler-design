package lexer;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 封装的Token类，包含类别编号和值
 *
 * @author 马洪升
 */
@AllArgsConstructor
public class Token {

    private int type;
    private String value;

    public String toString() {
        return "<" + this.type + ", " + this.value + ">";
    }
}
