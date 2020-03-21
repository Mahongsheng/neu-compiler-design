package lexer;

/**
 * 关键字类型：1
 * 标识符类型：2
 * 数字类型：3
 * 界符类型：4~14
 */
public class JudgeType {

    String[] keywords = {"abstract", "assert", "boolean", "break", "byte", "case",
            "catch", "char", "class", "const", "continue", "default", "do", "double",
            "else", "enum", "extends", "final", "finally", "float", "for", "goto", "if",
            "implements", "import", "instanceof", "int", "interface", "long", "native",
            "new", "package", "private", "protected", "public", "return", "short", "static",
            "strictfp", "super", "switch", "synchronized", "this", "throw", "throws",
            "transient", "try", "void", "volatile", "while"};

    String[] delimiters = {";", ":", "(", ")", "+", "-", "*", "/", "=", ">", "<"};


    public boolean isKeywords(String compareString) {
        for (String keyword : keywords) {
            if (keyword.equals(compareString)) return true;
        }
        return false;
    }

    public boolean isDelimiter(String compareString) {
        for (String delimiter : delimiters) {
            if (delimiter.equals(compareString)) return true;
        }
        return false;
    }
}
