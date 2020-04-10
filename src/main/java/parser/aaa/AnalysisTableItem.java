package parser.aaa;

import java.util.ArrayList;

public class AnalysisTableItem {
    private ArrayList<TableNode> nodes = new ArrayList<TableNode>();
    private char NChar;

    public ArrayList<TableNode> getNodes() {
        return nodes;
    }

    public void setNodes(ArrayList<TableNode> nodes) {
        this.nodes = nodes;
    }

    public char getNChar() {
        return NChar;
    }

    public void setNChar(char nChar) {
        NChar = nChar;
    }

}
