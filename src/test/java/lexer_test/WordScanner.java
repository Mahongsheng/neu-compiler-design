package lexer_test;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.*;

/**
 * WordScanner测试类。因为涉及到文件读写，所以该测试类不能直接执行所有，会导致测试不通过。
 * 请一个测试用例一个测试用例过。
 *
 * @author 软英1702 马洪升
 */
public class WordScanner {

    static File readFile;
    static File writeFile;
    static lexer.WordScanner wordScanner;

    @BeforeAll
    public static void init() {
        readFile = new File("src/main/java/lexer/file/input.txt");
        writeFile = new File("src/main/java/lexer/file/output.txt");
        wordScanner = new lexer.WordScanner();
    }

    @BeforeEach
    public void clearFile() {
        try {
            readFile.createNewFile();
            writeFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testInt() {
        assertDoesNotThrow(() -> {
            BufferedWriter writeToFile = new BufferedWriter(new FileWriter(readFile));
            writeToFile.write("2e-2");
            writeToFile.close();
            wordScanner.getTextFromFileAndAnalysis();
            wordScanner.writeToFile();
            BufferedReader readFromFile = new BufferedReader(new FileReader("src/main/java/lexer/file/output.txt"));
            assertEquals("<3, 0.02>", readFromFile.readLine());
            readFromFile.close();
        });
    }

    @Test
    public void testKeyword() {
        assertDoesNotThrow(() -> {
            BufferedWriter writeToFile = new BufferedWriter(new FileWriter(readFile));
            writeToFile.write("int");
            writeToFile.close();
            wordScanner.getTextFromFileAndAnalysis();
            wordScanner.writeToFile();
            BufferedReader readFromFile = new BufferedReader(new FileReader("src/main/java/lexer/file/output.txt"));
            assertEquals("<1, int>", readFromFile.readLine());
            readFromFile.close();
        });
    }

    @Test
    public void testIdentifier() {
        assertDoesNotThrow(() -> {
            BufferedWriter writeToFile = new BufferedWriter(new FileWriter(readFile));
            writeToFile.write("student");
            writeToFile.close();
            wordScanner.getTextFromFileAndAnalysis();
            wordScanner.writeToFile();
            BufferedReader readFromFile = new BufferedReader(new FileReader("src/main/java/lexer/file/output.txt"));
            assertEquals("<2, student>", readFromFile.readLine());
            readFromFile.close();
        });
    }

    @Test
    public void testSingleDelimiter() {
        assertDoesNotThrow(() -> {
            BufferedWriter writeToFile = new BufferedWriter(new FileWriter(readFile));
            writeToFile.write(";");
            writeToFile.close();
            wordScanner.getTextFromFileAndAnalysis();
            wordScanner.writeToFile();
            BufferedReader readFromFile = new BufferedReader(new FileReader("src/main/java/lexer/file/output.txt"));
            assertEquals("<4, ;>", readFromFile.readLine());
            readFromFile.close();
        });
    }

    @Test
    public void testDoubleDelimiter() {
        assertDoesNotThrow(() -> {
            BufferedWriter writeToFile = new BufferedWriter(new FileWriter(readFile));
            writeToFile.write("<=");
            writeToFile.close();
            wordScanner.getTextFromFileAndAnalysis();
            wordScanner.writeToFile();
            BufferedReader readFromFile = new BufferedReader(new FileReader("src/main/java/lexer/file/output.txt"));
            assertEquals("<16, <=>", readFromFile.readLine());
            readFromFile.close();
        });
    }

    @Test
    public void testNote() {
        assertDoesNotThrow(() -> {
            BufferedWriter writeToFile = new BufferedWriter(new FileWriter(readFile));
            writeToFile.write("/*这是一个注释*/\r\n//这是一个注释\r\n/**这是一个注释*/");
            writeToFile.close();
            wordScanner.getTextFromFileAndAnalysis();
            wordScanner.writeToFile();
            BufferedReader readFromFile = new BufferedReader(new FileReader("src/main/java/lexer/file/output.txt"));
            assertNull(readFromFile.readLine());
            readFromFile.close();
        });
    }

    @Test
    public void testOthers() {
        assertDoesNotThrow(() -> {
            BufferedWriter writeToFile = new BufferedWriter(new FileWriter(readFile));
            writeToFile.write("~");
            writeToFile.close();
            wordScanner.getTextFromFileAndAnalysis();
            wordScanner.writeToFile();
            BufferedReader readFromFile = new BufferedReader(new FileReader("src/main/java/lexer/file/output.txt"));
            assertEquals("<-1, 无法识别字符串>", readFromFile.readLine());
            readFromFile.close();
        });
    }

    @Test
    public void testAll() {
        assertDoesNotThrow(() -> {
            BufferedWriter writeToFile = new BufferedWriter(new FileWriter(readFile));
            writeToFile.write("public static void main(String[] args) {\n" +
                    "   int a = 2;\n" +
                    "   System.out.println();\n" +
                    "}");
            writeToFile.close();
            wordScanner.getTextFromFileAndAnalysis();
            wordScanner.writeToFile();
        });
    }
}
