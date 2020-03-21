package lexer;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

/**
 * @author 马洪升
 */
public class WordScanner {

    Queue<Character> analysingString = new LinkedList<>();
    ArrayList<String> keywords = new ArrayList<>();
    ArrayList<String> identifiers = new ArrayList<>();
    ArrayList<Integer> constants = new ArrayList<>();
    ArrayList<String> symbols = new ArrayList<>();
    BufferedWriter writeToFile;

    public void getTextFromFile() {
        try {
            BufferedReader readFromFile = new BufferedReader(new FileReader("src/lexer/file/input.txt"));
            String textLineInFile;
            while ((textLineInFile = readFromFile.readLine()) != null) {
                if (textLineInFile.startsWith("//")) continue;// 跳过当行注释
                char[] textToChar = textLineInFile.toCharArray();
                System.out.println(textLineInFile);
                for (char charInArray : textToChar) {
                    analysingString.add(charInArray);
                }
                analysisWord();
            }
            readFromFile.close();
        } catch (FileNotFoundException e) {
            System.err.println("输入文件不存在");
        } catch (IOException e) {
            System.out.println("文件读取错误");
        }
    }

    private void analysisWord() {
        int state = 0;
        Token token = new Token();
        while (!analysingString.isEmpty()) {
            char currentChar = analysingString.poll();
            switch (state) {
                case 0:
                    break;
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    break;
            }
        }
    }


    public void writeTextToFile() {
        try {
            writeToFile = new BufferedWriter(new FileWriter("src/lexer/file/input.txt"));
        } catch (IOException e) {
            System.err.println("输出文件不存在");
        }
    }

    public static void main(String[] args) {
        WordScanner wordScanner = new WordScanner();
        wordScanner.getTextFromFile();
    }

}
