package lexer;

/**
 * 无法识别类型：-1
 * 关键字类型：1
 * 标识符类型：2
 * 数字类型：3
 * 界符类型：4~14
 */
public class JudgeType {

    static String[] keywords = {"abstract", "assert", "boolean", "break", "byte", "case",
            "catch", "char", "class", "const", "continue", "default", "do", "double",
            "else", "enum", "extends", "final", "finally", "float", "for", "goto", "if",
            "implements", "import", "instanceof", "int", "interface", "long", "native",
            "new", "package", "private", "protected", "public", "return", "short", "static",
            "strictfp", "super", "switch", "synchronized", "this", "throw", "throws",
            "transient", "try", "void", "volatile", "while"};

    static Character[] delimiters = {';', ':', '(', ')', '+', '-', '*', '/', '=', '>', '<'};

    public static boolean isKeywords(String compareString) {
        for (String keyword : keywords) {
            if (keyword.equals(compareString)) return true;
        }
        return false;
    }

    public static boolean isDelimiter(Character compareString) {
        for (Character delimiter : delimiters) {
            if (delimiter == compareString) return true;
        }
        return false;
    }

    public static int getDelimiterType(Character compareString) {
        for (int i = 0; i < delimiters.length; i++) {
            if (delimiters[i] == compareString) return i + 4;
        }
        return -1;
    }
}
