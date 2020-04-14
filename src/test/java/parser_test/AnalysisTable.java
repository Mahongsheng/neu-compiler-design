package parser_test;

import org.junit.jupiter.api.Test;

/**
 * 测试分析表是否正确生成
 *
 * @author 软英1702 马洪升
 */
public class AnalysisTable {

    private parser.LL1.AnalysisTable analysisTable = new parser.LL1.AnalysisTable();

    @Test
    public void testAnalysisTable() {
        System.out.println("First集： " + analysisTable.getFirst());
        System.out.println("Follow集： " + analysisTable.getFollow());
        System.out.println("Select集： " + analysisTable.getSelect());
        System.out.println(analysisTable.judgeIfLL1() ? "该语句为LL(1)文法" : "该语句不是LL(1)文法");
        analysisTable.showAnalysisTable();
    }
}