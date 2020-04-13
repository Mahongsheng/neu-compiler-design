package parser.LL1;

import lombok.Getter;

import java.util.*;

@Getter
public class AnalysisTable {
    private String[] grammar = {"E->TY", "Y->+TY", "Y->-TY", "Y->e", "T->FX", "X->*FX", "X->/FX", "X->e", "F->i", "F->(E)"};
    private HashSet<Character> Vn = new HashSet<>();//非终结符集
    private HashSet<Character> Vt = new HashSet<>();//终结符集合
    private ArrayList<HashSet<Character>> first = new ArrayList<>();
    private ArrayList<HashSet<Character>> follow = new ArrayList<>();
    private ArrayList<HashSet<Character>> select = new ArrayList<>();
    private HashMap<Character, ArrayList<AnalysisTableItem>> analysisTable = new HashMap<>();
    public int[] ifNeedFollow = new int[grammar.length];
    private char firstVn = grammar[0].charAt(0);

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
        if (firstVn == currentVn) currFollow.add(';');
        for (String currGrammar : grammar) {
            String rightOfGrammar = currGrammar.split("->")[1];
            //遍历并找到右部含有当前非终结符的产生式
            for (int i = 0; i < rightOfGrammar.length(); i++) {
                if (rightOfGrammar.charAt(i) == currentVn) {
                    if (i == rightOfGrammar.length() - 1 || rightOfGrammar.charAt(i + 1) == 'e') {
                        if (currGrammar.charAt(0) != currentVn)
                            currFollow.addAll(findFollowByVn(currGrammar.charAt(0)));
                        break;
                    } else {
                        for (int j = 0; j < grammar.length; j++) {
                            if (grammar[j].charAt(0) == rightOfGrammar.charAt(i + 1)) {
                                if (ifNeedFollow[j] == 1) {
                                    currFollow.addAll(findFollowByVn(rightOfGrammar.charAt(i + 1)));
                                    break;
                                }
                            }
                        }
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

    public boolean judgeIfLL1() {
        int wholeNumInSelect;
        char currentVn = 0;
        HashSet<Character> wholeSelect;
        HashSet<Character> judgedVn = new HashSet<>();
        for (int i = 0; i < grammar.length; i++) {
            if (!judgedVn.contains(currentVn)) {
                currentVn = grammar[i].charAt(0);
                judgedVn.add(currentVn);
                wholeNumInSelect = 0;
                wholeSelect = new HashSet<>();
            } else continue;
            for (int j = i + 1; j < grammar.length; j++) {
                if (grammar[j].charAt(0) == currentVn) {
                    wholeNumInSelect += select.get(j).size();
                    wholeSelect.addAll(select.get(j));
                }
            }
            if (wholeNumInSelect != wholeSelect.size()) return false;
        }
        return true;
    }

    private void handleAnalysisTable() {
        for (int i = 0; i < grammar.length; i++) {
            char VnOfThisGrammar = grammar[i].charAt(0);
            if (!analysisTable.containsKey(VnOfThisGrammar)) {
                ArrayList<AnalysisTableItem> arrOfThisVn = new ArrayList<>();
                analysisTable.put(VnOfThisGrammar, arrOfThisVn);
            }
            HashSet<Character> thisSelect = select.get(i);
            for (char thisChar : thisSelect) {
                AnalysisTableItem analysisTableItem = new AnalysisTableItem(VnOfThisGrammar, thisChar, i);
                analysisTable.get(VnOfThisGrammar).add(analysisTableItem);
            }
        }
    }

    public void showAnalysisTable() {
        for (char Vn : analysisTable.keySet()) {
            System.out.print(Vn + "\t");
            for (AnalysisTableItem item : analysisTable.get(Vn)) {
                System.out.print(item.getVt() + "->" + item.getChangeToNextGrammar() + "\t");
            }
            System.out.println();
        }
    }
}
