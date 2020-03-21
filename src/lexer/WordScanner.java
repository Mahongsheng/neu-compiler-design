package lexer;

import sun.nio.cs.ext.MacHebrew;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * @author 马洪升
 */
public class WordScanner {

    LinkedList<Character> analysingString = new LinkedList<>();
    LinkedList<Token> tokens = new LinkedList<>();
    ArrayList<String> keywords = new ArrayList<>();
    ArrayList<String> identifiers = new ArrayList<>();
    ArrayList<Double> constants = new ArrayList<>();
    ArrayList<String> symbols = new ArrayList<>();
    BufferedWriter writeToFile;

    public void getTextFromFileAndAnalysis() {
        try {
            BufferedReader readFromFile = new BufferedReader(new FileReader("src/lexer/file/input.txt"));
            String textLineInFile;
            while ((textLineInFile = readFromFile.readLine()) != null) {
                if (textLineInFile.startsWith("//")) continue;// 跳过当行注释
                char[] textToChar = textLineInFile.toCharArray();
                for (char charInArray : textToChar) {
                    analysingString.add(charInArray);
                }
                analysingString.add(' ');
                analysisWord();
            }
            readFromFile.close();
            for (Token token : tokens) {
                System.out.print("<" + token.getType() + ", " + token.getValue() + ">");
            }
        } catch (FileNotFoundException e) {
            System.err.println("输入文件不存在");
        } catch (IOException e) {
            System.out.println("文件读取错误");
        }
    }

    private void analysisWord() {
        int state = 1;
        Token token;
        StringBuilder currentWord = new StringBuilder();
        StringBuilder currentENum = new StringBuilder();
        while (!analysingString.isEmpty()) {
            char currentChar = analysingString.poll();
            switch (state) {
                case 1:
                    if (Character.isLetter(currentChar)) {
                        state = 2;
                        currentWord.append(currentChar);
                    } else if (Character.isDigit(currentChar)) {
                        state = 3;
                        currentWord.append(currentChar);
                    } else if (JudgeType.isDelimiter(currentChar)) {
                        token = new Token(JudgeType.getDelimiterType(currentChar), String.valueOf(currentChar));
                        tokens.add(token);
                        symbols.add(currentWord.toString());
                        currentWord = new StringBuilder();
                    } else if (currentChar != ' ') {
                        // 不能识别该字符，将其类别设置为-1
                        token = new Token(-1, String.valueOf(currentChar));
                        tokens.add(token);
                    }
                    break;
                case 2:
                    if (Character.isLetter(currentChar) || Character.isDigit(currentChar)) {
                        state = 2;
                        currentWord.append(currentChar);
                    } else {
                        state = 1;
                        analysingString.addFirst(currentChar);
                        if (JudgeType.isKeywords(currentWord.toString())) {
                            token = new Token(1, currentWord.toString());
                            tokens.add(token);
                            keywords.add(currentWord.toString());
                        } else {
                            token = new Token(2, currentWord.toString());
                            tokens.add(token);
                            identifiers.add(currentWord.toString());
                        }
                        currentWord = new StringBuilder();
                    }
                    break;
                case 3:
                    if (Character.isDigit(currentChar)) {
                        currentWord.append(currentChar);
                    } else if (currentChar == '.') {
                        state = 4;
                    } else if (currentChar == 'e') {
                        state = 6;
                    } else {
                        state = 1;
                        analysingString.addFirst(currentChar);
                        double wordToDouble = Double.parseDouble(currentWord.toString());
                        token = new Token(3, currentWord.toString());
                        tokens.add(token);
                        constants.add(wordToDouble);
                        currentWord = new StringBuilder();
                    }
                    break;
                case 4:
                    /* 数字中的小数 */
                    if (Character.isDigit(currentChar)) {
                        state = 5;
                        currentWord.append(currentChar);
                    }
                    break;
                case 5:
                    if (Character.isDigit(currentChar)) {
                        currentWord.append(currentChar);
                    } else if (currentChar == 'e') {
                        state = 6;
                    } else {
                        state = 1;
                        analysingString.addFirst(currentChar);
                        double wordToDouble = Double.parseDouble(currentWord.toString());
                        token = new Token(3, currentWord.toString());
                        tokens.add(token);
                        constants.add(wordToDouble);
                        currentWord = new StringBuilder();
                    }
                    break;
                case 6:
                    if (Character.isDigit(currentChar)) {
                        state = 7;
                        currentENum.append(currentChar);
                    } else if (currentChar == '+' || currentChar == '-') {
                        state = 8;
                        currentENum.append(currentChar);
                    }
                    break;
                case 7:
                    if (Character.isDigit(currentChar)) {
                        currentENum.append(currentChar);
                    } else {
                        state = 1;
                        analysingString.addFirst(currentChar);
                        int ENum = Integer.parseInt(currentENum.toString());
                        double doublePart = Double.parseDouble(currentWord.toString());
                        double constant = doublePart * Math.pow(10, ENum);
                        token = new Token(3, String.valueOf(constant));
                        tokens.add(token);
                        constants.add(constant);
                        currentWord = new StringBuilder();
                    }
                    break;
                case 8:
                    if (Character.isDigit(currentChar)) {
                        state = 7;
                        currentENum.append(currentChar);
                    }
                    break;
            }
        }
    }


    public void writeToFile() {
        try {
            writeToFile = new BufferedWriter(new FileWriter("src/lexer/file/input.txt"));
        } catch (IOException e) {
            System.err.println("输出文件不存在");
        }
    }

    public static void main(String[] args) {
        WordScanner wordScanner = new WordScanner();
        wordScanner.getTextFromFileAndAnalysis();
    }

}
