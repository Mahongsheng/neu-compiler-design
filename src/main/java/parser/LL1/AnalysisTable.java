package parser.LL1;

import lombok.Getter;

import java.util.*;

@Getter
public class AnalysisTable {
    private String[] grammar = {"E->TY", "Y->+TgY", "Y->-TgY", "Y->n", "T->FX", "X->*FgX", "X->/FgX", "X->n", "F->ip", "F->(E)"};
    private HashSet<Character> Vn = new HashSet<>();//非终结符集
    private HashSet<Character> Vt = new HashSet<>();//终结符集合
    private ArrayList<HashSet<Character>> first = new ArrayList<>();
    private ArrayList<HashSet<Character>> follow = new ArrayList<>();
    private ArrayList<HashSet<Character>> select = new ArrayList<>();
    private HashMap<Character, ArrayList<AnalysisTableItem>> analysisTable = new HashMap<>();
    public int[] ifNeedFollow = new int[grammar.length];

    public AnalysisTable() {
        this.handleVnVt();
        this.handleFirst();
        this.handleFollow();
        this.handleSelect();
        this.handleAnalysisTable();
    }

    private void handleVnVt() {
        for (String grammarItem : grammar) {
            String[] VnItem = grammarItem.split("->");
            // Vn在左边
            Vn.add(VnItem[0].charAt(0));
        }
        for (String grammarItem : grammar) {
            String[] VtItem = grammarItem.split("->");
            // Vt在右边
            String VtItemStr = VtItem[1];
            // 遍历每一个字
            for (int i = 0; i < VtItemStr.length(); i++) {
                if (!Vn.contains(VtItemStr.charAt(i))) Vt.add(VtItemStr.charAt(i));
            }
        }
    }

    private void handleFirst() {
        for (int i = 0; i < grammar.length; i++) {
            HashSet<Character> firstOfThisItem = new HashSet<>();
            Queue<Character> nextVn = new LinkedList<>();
            char currentChar = grammar[i].split("->")[1].charAt(0);
            if (currentChar == 'e') {
                ifNeedFollow[i] = 1;
                first.add(firstOfThisItem);
                continue;
            }
            if (Vt.contains(currentChar)) firstOfThisItem.add(currentChar);
            else nextVn.add(currentChar);
            while (!nextVn.isEmpty()) {
                char currentVn = nextVn.poll();
                for (String currGrammar : grammar) {
                    if (currGrammar.charAt(0) == currentVn) {
                        currentChar = currGrammar.split("->")[1].charAt(0);
                        if (Vt.contains(currentChar)) {
                            if (currentChar == 'e') ifNeedFollow[i] = 1;
                            else firstOfThisItem.add(currentChar);
                        } else nextVn.add(currentChar);
                    }
                }
            }
            first.add(firstOfThisItem);
        }
    }

    private void handleFollow() {
        for (int i = 0; i < ifNeedFollow.length; i++) {
            HashSet<Character> followOfThisItem = new HashSet<>();
            follow.add(followOfThisItem);
            if (ifNeedFollow[i] == 1) {
                char currentVn = grammar[i].charAt(0);
                follow.get(i).addAll(findFollowByVn(currentVn));
            }
        }
    }

    private HashSet<Character> findFollowByVn(char currentVn) {
        HashSet<Character> currFollow = new HashSet<>();
        for (String currGrammar : grammar) {
            String rightOfGrammar = currGrammar.split("->")[1];
            for (int i = 0; i < rightOfGrammar.length(); i++) {
                if (rightOfGrammar.charAt(i) == currentVn) {
                    if (i == rightOfGrammar.length() - 1 || rightOfGrammar.charAt(i + 1) == 'e') {
                        findFollowByVn(currGrammar.charAt(0));
                        break;
                    } else {
                        currFollow.addAll(findFirstByVn(rightOfGrammar.charAt(i + 1)));
                    }
                }
            }
        }
        return currFollow;
    }

    private HashSet<Character> findFirstByVn(char currentVn) {
        HashSet<Character> currFirst = new HashSet<>();
        if (Vt.contains(currentVn)) currFirst.add(currentVn);
        else {
            for (int i = 0; i < grammar.length; i++) {
                if (grammar[i].charAt(0) == currentVn) {
                    currFirst.addAll(first.get(i));
                }
            }
        }
        return currFirst;
    }

    private void handleSelect() {
        for (int i = 0; i < grammar.length; i++) {
            HashSet<Character> selectOfThisItem = new HashSet<>();
            select.add(selectOfThisItem);
            HashSet<Character> thisFirst = first.get(i);
            select.get(i).addAll(thisFirst);
            if (ifNeedFollow[i] == 1) select.get(i).addAll(follow.get(i));
        }
    }

    private boolean judgeIfLL1() {
        return false;
    }

    private void handleAnalysisTable() {
        for (int i = 0; i < grammar.length; i++) {
            char VnOfthisGrammar = grammar[i].charAt(0);
            if (!analysisTable.containsKey(VnOfthisGrammar)) {
                ArrayList<AnalysisTableItem> arrOfThisVn = new ArrayList<>();
                analysisTable.put(VnOfthisGrammar, arrOfThisVn);
            }
            HashSet<Character> thisSelect = select.get(i);
            for (char thisChar : thisSelect) {
                AnalysisTableItem analysisTableItem = new AnalysisTableItem(VnOfthisGrammar, thisChar, i);
                analysisTable.get(VnOfthisGrammar).add(analysisTableItem);
            }
        }
    }

    private void showAnalysisTable() {
        for (char Vn : analysisTable.keySet()) {
            System.out.print(Vn + "\t");
            for (AnalysisTableItem item : analysisTable.get(Vn)) {
                System.out.print(item.getVt() + "->" + item.getChangeToNextGrammar() + "\t");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        AnalysisTable analysisTable = new AnalysisTable();
        analysisTable.showAnalysisTable();
//        System.out.println(analysisTable.first);
//        System.out.println(analysisTable.follow);
//        System.out.println(analysisTable.select);
//        System.out.println(analysisTable.analysisTable);
    }
}
