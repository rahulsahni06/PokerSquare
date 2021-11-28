public class Genes {
    private Card card;
    private int row;
    private int col;

    public Genes(Card card, int row, int col) {
        this.card = card;
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }
}
