package parser.aaa;

import java.util.ArrayList;

public class AnalysisTable {

    public String grammer[] = {"E->TY", "Y->+TgY", "Y->-TgY", "Y->n", "T->FX", "X->*FgX", "X->/FgX", "X->n", "F->ip", "F->(E)"};
    public char Vt[] = {'+', '-', '*', '/', 'i', '(', ')', '#'};
    public char Vn[] = {'E', 'T', 'Y', 'F', 'X'};
    public ArrayList<Character> first;
    public ArrayList<Character> follow;
    public ArrayList<Character> select;
    public ArrayList<AnalysisTableItem> analysisTable = new ArrayList<AnalysisTableItem>();//邻接表方式存储

    public void makeAnalysisTable() {
        //先放入第一个非终结字符不然J循环不执行
        AnalysisTableItem analysisTableItem = new AnalysisTableItem();
        analysisTableItem.setNChar(grammer[0].split("->")[0].charAt(0));
        analysisTable.add(analysisTableItem);
        //初始化分析表，将表项的非终结左边字符填入
        for (int i = 0; i < grammer.length; i++) {
            for (int j = 0; j < analysisTable.size(); j++) {
//				System.out.print(analysisTable.get(j).getNChar()+" ");
                //判断是否已经存在分析表中了
                if (analysisTable.get(j).getNChar() == grammer[i].split("->")[0].charAt(0))
                    break;
                //检测到末尾都没有出现过，则新加进分析表中
                if (j == analysisTable.size() - 1) {
                    analysisTableItem = new AnalysisTableItem();
                    analysisTableItem.setNChar(grammer[i].split("->")[0].charAt(0));
                    analysisTable.add(analysisTableItem);
                }
            }
//			System.out.println();
        }
        //每个产生式子应的select集
        String str;
        for (int i = 0; i < grammer.length; i++) {
            first = new ArrayList<Character>();
            follow = new ArrayList<Character>();
            select = new ArrayList<Character>();
            str = grammer[i].split("->")[1];
            char followCh;
            //能星推导出空集
            if (makeFirstSet(str)) {
                select.addAll(first);

                //寻找生成str的产生式子

                if (str.equals("n")) {
                    followCh = grammer[i].split("->")[0].charAt(0);
                    makeFollowSet(followCh);
                } else {
                    for (int k = 0; k < grammer.length; k++) {
                        if (grammer[k].split("->")[1].equals(str)) {
                            followCh = grammer[k].split("->")[0].charAt(0);
                            makeFollowSet(followCh);
                        }
                    }
                }
                select.addAll(follow);
            } else {
                select.addAll(first);
            }
            select = removeDuplicate(select);
//			System.out.println(str+":");
            //将Select集用来生成分析表
            for (int k = 0; k < analysisTable.size(); k++) {
                //在分析表中找到产生是对应的左边非终结符，并且通过求到的select集生成对应的节点信息
                if (analysisTable.get(k).getNChar() == grammer[i].split("->")[0].charAt(0)) {
                    for (int j = 0; j < select.size(); j++) {
                        analysisTable.get(k).getNodes().add(new TableNode(select.get(j), i));
                    }
                }
            }
        }
        //打印分析表
        System.out.println("分析表如下：");
        for (int i = 0; i < analysisTable.size(); i++) {
            System.out.print(analysisTable.get(i).getNChar());
            for (int j = 0; j < analysisTable.get(i).getNodes().size(); j++) {
                System.out.print("->" + analysisTable.get(i).getNodes().get(j).getType() + "  " + analysisTable.get(i).getNodes().get(j).getChangeNum());
            }
            System.out.println();
        }
    }

    //生成First集同时判断字符串能否得到ipslon
    private boolean makeFirstSet(String str) {
        for (int i = 0; i < str.length(); i++) {
            //判断该字符是否能生成空集
            if (!makeFirstChar(str.charAt(i)))
                return false;
        }
        return true;
    }

    //针对一个字符同时求解其是否能得到ipslon和其first集
    private boolean makeFirstChar(char ch) {
        boolean ifPIpslon = false;
        //如果i是 终结符
        if (isVt(ch)) {
            first.add(ch);
            return false;//得不到ipslon
        } else if (ch == 'n') {
            return true;
        } else {
            for (int i = 0; i < grammer.length; i++) {
                if (grammer[i].split("->")[0].charAt(0) == ch) {
                    //有一个可以即可
                    if (makeFirstSet(grammer[i].split("->")[1]))
                        ifPIpslon = true;
                }
            }
            return ifPIpslon;
        }
    }

    //生成follow
    private void makeFollowSet(char followCh) {
        if (followCh == 'E') {
            follow.add('#');
        }
        if (isVt(followCh))
            return;
        //查看所有右部含有followCh的产生式
        for (int i = 0; i < grammer.length; i++) {
//			System.out.println(grammer[i].split("->")[1].length());
            //左边非终结符不能等于所求follow集合

            for (int k = 0; k < grammer[i].split("->")[1].length(); k++) {
                //右部含followCh
                if (grammer[i].split("->")[1].charAt(k) == followCh) {

                    first = new ArrayList<Character>();
                    //followCh不是产生式最后一个
                    if (k < grammer[i].split("->")[1].length() - 1) {
                        //followCh后面的可星推导出空集
                        if (makeFirstChar(grammer[i].split("->")[1].charAt(k + 1))) {
                            follow.addAll(first);
                            makeFollowSet(grammer[i].split("->")[1].charAt(k + 1));
                        } else {
                            follow.addAll(first);
                        }
                    } else {
                        //重点：在进行匹配的时候，判断避免出现无限递归
                        if (grammer[i].split("->")[0].charAt(0) != followCh) {
                            makeFollowSet(grammer[i].split("->")[0].charAt(0));
                        }
                    }
                }
            }
        }

    }

    //是否属于Vt
    private boolean isVt(char valueOf) {
        for (int i = 0; i < Vt.length; i++) {
            if (Vt[i] == valueOf)
                return true;
        }
        return false;
    }

    public ArrayList<Character> removeDuplicate(ArrayList<Character> list) {
        for (int i = 0; i < list.size() - 1; i++) {   //从左向右循环
            for (int j = list.size() - 1; j > i; j--) {  //从右往左内循环
                if (list.get(j).equals(list.get(i))) {
                    list.remove(j);                              //相等则移除
                }
            }
        }
        return list;
    }
}
