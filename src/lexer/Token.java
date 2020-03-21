package lexer;

/**
 * @author 马洪升
 */

public class Token {

    private int type;
    private String value;

    public Token(int type, String value) {
        this.type = type;
        this.value = value;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void display() {
        System.out.println("<" + this.type + ", " + this.value + ">");
    }

    @Override
    public String toString() {
        return "<" + this.type + ", " + this.value + ">";
    }
}
