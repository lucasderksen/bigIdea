package nl.fhict.s3.sharedmodel;

public class GameInfo {
    private boolean isRed;
    private int column;
    private int row;
    private String messageType;
//    private String name;

    public GameInfo() {
    }

    public GameInfo(boolean isRed, int column, int row) {
        this.isRed = isRed;
        this.column = column;
        this.row = row;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public boolean isRed() {
        return isRed;
    }

    public void setRed(boolean red) {
        isRed = red;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }
}
