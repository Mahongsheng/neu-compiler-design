package lexer;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * 扫描器
 * 从文件中获取代码，分析后形成token，再写入另一文件中
 *
 * @author 马洪升
 */
public class WordScanner {

    //使用链表存储所有待处理字符串
    LinkedList<Character> analysingString = new LinkedList<>();
    //相应的Token序列；关键字表和界符表；符号表和常数表；
    LinkedList<Token> tokens = new LinkedList<>();
    ArrayList<String> keywords = new ArrayList<>();
    ArrayList<String> identifiers = new ArrayList<>();
    ArrayList<String> symbols = new ArrayList<>();
    ArrayList<Double> constants = new ArrayList<>();
    //是否正在处理注释
    boolean isProcessingNote = false;
    BufferedWriter writeToFile;
    //有限自动机的状态转换表
    int[][] transitionMatrix = {
            {2, 0, 0, 0, 8, 11, 0, 9, 19},
            {2, 3, 5, 14, 0, 0, 0, 0, 14},
            {4, 0, 0, 0, 0, 0, 0, 0, 0},
            {4, 0, 5, 14, 0, 0, 0, 0, 14},
            {7, 0, 0, 6, 0, 0, 0, 0, 0},
            {7, 0, 0, 0, 0, 0, 0, 0, 0},
            {7, 0, 0, 14, 0, 0, 0, 0, 14},
            {8, 0, 0, 0, 8, 0, 0, 0, 15},
            {0, 0, 0, 0, 0, 0, 0, 10, 17},
            {0, 0, 0, 0, 0, 0, 0, 0, 16},
            {0, 0, 0, 0, 0, 18, 12, 0, 0},
            {0, 0, 0, 0, 0, 0, 13, 0, 12},
            {0, 0, 0, 0, 0, 18, 13, 0, 12}};

    /**
     * 从文件中读取代码并开始分析
     */
    public void getTextFromFileAndAnalysis() {
        try {
            BufferedReader readFromFile = new BufferedReader(new FileReader("src/main/java/lexer/file/input.txt"));
            String textLineInFile;
            while ((textLineInFile = readFromFile.readLine()) != null) {
                textLineInFile = textLineInFile.trim();
                if (textLineInFile.startsWith("//")) continue;//在此处跳过单行注释，加快处理效率
                char[] textToChar = textLineInFile.toCharArray();
                for (char charInArray : textToChar) {
                    analysingString.add(charInArray);
                }
                analysingString.add(' ');
                analysisWord();//开始处理
            }
            readFromFile.close();
            showAll();
        } catch (FileNotFoundException e) {
            System.err.println("输入文件不存在");
        } catch (IOException e) {
            System.out.println("文件读取错误");
        }
    }

    /**
     * 使用有限自动机进行词法分析
     */
    private void analysisWord() {
        int state = 1;//当前状态
        if (isProcessingNote) state = 12;
        char currentChar;//当前字符
        Token token;
        StringBuilder currentWord = new StringBuilder();//当前单词
        StringBuilder currentENum = new StringBuilder();//当前数字型单词的科学计数部分
        while (!analysingString.isEmpty()) {
            currentChar = analysingString.poll();
            switch (state) {
                case 1:
                    currentWord = new StringBuilder();
                    state = getNextState(state, currentChar);
                    currentWord.append(currentChar);
                    break;
                case 2://识别为数字
                    if (Character.isDigit(currentChar)) {
                        currentWord.append(currentChar);
                    } else if (currentChar != 'e' && currentChar != 'E' && currentChar != '.')
                        analysingString.addFirst(currentChar);
                    state = getNextState(state, currentChar);
                    break;
                case 3:
                    currentWord.append(currentChar);
                    state = getNextState(state, currentChar);
                    break;
                case 4:
                    if (Character.isDigit(currentChar)) {
                        currentWord.append(currentChar);
                    } else analysingString.addFirst(currentChar);
                    state = getNextState(state, currentChar);
                    break;
                case 5://识别为数字且数字为科学计数法形式
                    currentENum = new StringBuilder();
                    currentENum.append(currentChar);
                    state = getNextState(state, currentChar);
                    break;
                case 6:
                    currentENum.append(currentChar);
                    state = getNextState(state, currentChar);
                    break;
                case 7:
                    if (Character.isDigit(currentChar)) {
                        currentENum.append(currentChar);
                    } else analysingString.addFirst(currentChar);
                    state = getNextState(state, currentChar);
                    break;
                case 8://识别为关键字或标识符
                    if (Character.isLetter(currentChar) || Character.isDigit(currentChar)) {
                        currentWord.append(currentChar);
                    } else analysingString.addFirst(currentChar);
                    state = getNextState(state, currentChar);
                    break;
                case 9://识别为界符
                    if (JudgeType.isDelimiter(currentChar)) {
                        currentWord.append(currentChar);
                    } else analysingString.addFirst(currentChar);
                    state = getNextState(state, currentChar);
                    break;
                case 10://识别为双界符
                    analysingString.addFirst(currentChar);
                    state = getNextState(state, currentChar);
                    break;
                case 11://识别为注释
                    currentWord.append(currentChar);
                    state = getNextState(state, currentChar);
                    break;
                case 12:
                    state = getNextState(state, currentChar);
                    isProcessingNote = true;
                    break;
                case 13:
                    state = getNextState(state, currentChar);
                    break;
                case 14://将当前字符串转化为数字并生成token
                    state = 1;
                    analysingString.addFirst(currentChar);
                    //开始转换
                    int ENum = 0;
                    if (!currentENum.toString().equals("")) {
                        ENum = Integer.parseInt(currentENum.toString());
                    }
                    double doublePart = Double.parseDouble(currentWord.toString());
                    double constant = doublePart * Math.pow(10, ENum);
                    token = new Token(3, String.valueOf(constant));
                    tokens.add(token);
                    constants.add(constant);
                    break;
                case 15://处理当前字符串为关键字或标识符
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
                    break;
                case 16://处理当前字符串为双界符
                    state = 1;
                    analysingString.addFirst(currentChar);
                    if (JudgeType.getDoubleDelimiterType(currentWord.toString()) == -1) {
                        token = new Token(JudgeType.getDelimiterType(currentWord.charAt(0)), currentWord.substring(0, 1));
                        analysingString.addFirst(currentWord.charAt(1));
                    } else {
                        token = new Token(JudgeType.getDoubleDelimiterType(currentWord.toString()), currentWord.toString());
                    }
                    tokens.add(token);
                    symbols.add(currentWord.toString());
                    break;
                case 17://处理当前字符为单界符
                    state = 1;
                    analysingString.addFirst(currentChar);
                    token = new Token(JudgeType.getDelimiterType(currentWord.toString().charAt(0)), currentWord.toString());
                    tokens.add(token);
                    symbols.add(currentWord.toString());
                    break;
                case 18://丢弃所有注释，不生成任何token
                    state = 1;
                    if (currentWord.toString().equals("//")) {
                        analysingString.clear();
                    } else {
                        analysingString.addFirst(currentChar);
                        isProcessingNote = false;
                    }
                    break;
                case 19://无法识别的字符串
                    state = 1;
                    analysingString.addFirst(currentChar);
                    token = new Token(-1, "无法识别字符串");
                    tokens.add(token);
                    break;
            }
        }
    }

    /**
     * 根据当前状态和当前字符得到下一状态
     *
     * @param state
     * @param currentChar
     * @return
     */
    private int getNextState(int state, Character currentChar) {
        int column;
        if (Character.isDigit(currentChar)) column = 0;
        else if (currentChar == '.' && state == 2) column = 1;
        else if ((currentChar == 'e' || currentChar == 'E') && (state == 2 || state == 4)) column = 2;
        else if ((currentChar == '+' || currentChar == '-') && (state == 2 || state == 4 || state == 5 || state == 7))
            column = 3;
        else if (currentChar == '/' && (state == 1 || state == 11 || state == 13)) column = 5;
        else if (currentChar == '*' && (state == 11 || state == 12 || state == 13)) column = 6;
        else if (Character.isLetter(currentChar)) column = 4;
        else if (JudgeType.isDelimiter(currentChar)) column = 7;
        else column = 8;
        int nextState = transitionMatrix[state - 1][column];
        if (nextState == 0) {
            if (state == 2 && (currentChar != 'E' && currentChar != 'e' && currentChar != '.')) nextState = 14;
            else if (state == 4 && !Character.isDigit(currentChar)) nextState = 14;
            else if (state == 7 && !Character.isDigit(currentChar)) nextState = 14;
            else if (state == 8 && !Character.isDigit(currentChar) && !Character.isLetter(currentChar)) nextState = 15;
            else if (state == 9 && !JudgeType.isDelimiter(currentChar)) nextState = 17;
            else if (state == 10) nextState = 16;
            else if (state == 12 && currentChar != '*') nextState = 13;
            else if (state == 13 && currentChar != '/') nextState = 12;
        }
        if (state == 1 && currentChar == ' ') nextState = 1;
        return nextState;
    }

    /**
     * 将分析结果中的token写入文件
     */
    public void writeToFile() {
        try {
            writeToFile = new BufferedWriter(new FileWriter("src/main/java/lexer/file/output.txt"));
            for (Token token : tokens) {
                writeToFile.write(token.toString());
                writeToFile.newLine();
            }
            writeToFile.close();
        } catch (IOException e) {
            System.err.println("输出文件不存在");
        }
    }

    /**
     * 展示所有分析结果
     */
    private void showAll() {
        for (Token token : tokens) {
            System.out.print(token.toString() + " ");
        }
        System.out.println();
        System.out.print("关键字表：");
        keywords.forEach(e -> System.out.print(e + " "));
        System.out.println();
        System.out.print("界符表：");
        identifiers.forEach(e -> System.out.print(e + " "));
        System.out.println();
        System.out.print("符号表：");
        symbols.forEach(e -> System.out.print(e + " "));
        System.out.println();
        System.out.print("常数表：");
        constants.forEach(e -> System.out.print(e + " "));
    }
}