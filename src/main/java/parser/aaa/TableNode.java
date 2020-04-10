package parser.aaa;

public class TableNode {
    private char type;
    private int changeNum;

    public TableNode(char type, int changeNum) {
        this.type = type;
        this.changeNum = changeNum;
    }

    public char getType() {
        return type;
    }

    public void setType(char type) {
        this.type = type;
    }

    public int getChangeNum() {
        return changeNum;
    }

    public void setChangeNum(int changeNum) {
        this.changeNum = changeNum;
    }

}
