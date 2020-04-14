package lexer;

/**
 * 工具类，用于判断当前字符串是否属于某类型
 * 无法识别类型：-1
 * 关键字类型：1
 * 标识符类型：2
 * 常数类型：3
 * 界符类型：4~24
 *
 * @author 软英1702 马洪升
 */
public class JudgeType {

    //该列表包含了所有Java的关键字
    static String[] keywords = {"abstract", "assert", "boolean", "break", "byte", "case",
            "catch", "char", "class", "const", "continue", "default", "do", "double",
            "else", "enum", "extends", "final", "finally", "float", "for", "goto", "if",
            "implements", "import", "instanceof", "int", "interface", "long", "native",
            "new", "package", "private", "protected", "public", "return", "short", "static",
            "strictfp", "super", "switch", "synchronized", "this", "throw", "throws",
            "transient", "try", "void", "volatile", "while"};
    //Java的界符
    static Character[] delimiters = {';', ':', '(', ')', '+', '-', '*', '/', '=', '>', '<', '[', ']', '{', '}', '.', '"', '\''};
    //Java的双界符
    static String[] doubleDelimiters = {"==", "<=", ">="};

    /**
     * 判断是否为关键字
     *
     * @param compareString
     * @return
     */
    public static boolean isKeywords(String compareString) {
        for (String keyword : keywords) {
            if (keyword.equals(compareString)) return true;
        }
        return false;
    }

    /**
     * 判断是否为界符
     *
     * @param compareString
     * @return
     */
    public static boolean isDelimiter(Character compareString) {
        for (Character delimiter : delimiters) {
            if (delimiter == compareString) return true;
        }
        return false;
    }

    /**
     * 得到界符类别
     *
     * @param compareString
     * @return
     */
    public static int getDelimiterType(Character compareString) {
        for (int i = 0; i < delimiters.length; i++) {
            if (delimiters[i] == compareString) return i + 4;
        }
        return -1;
    }

    /**
     * 得到双界符类别
     *
     * @param compareString
     * @return
     */
    public static int getDoubleDelimiterType(String compareString) {
        for (int i = 0; i < doubleDelimiters.length; i++) {
            if (doubleDelimiters[i].equals(compareString)) return i + 15;
        }
        return -1;
    }
}
