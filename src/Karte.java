import java.io.Serializable;

public class Karte implements Serializable {

    private int val;
    private char trumpf;
    private char wünschen = '0';


    public Karte(int val, char schlag) {
        this.val = val;
        this.trumpf = schlag;
    }


    public int getVal() {
        return val;
    }

    public char getTrumpf() {
        return trumpf;
    }

    @Override
    public String toString() {
        return "Karte{" +
                "val=" + val +
                ", schlag=" + trumpf +
                '}';
    }

    public void setWünschen(char wünschen) {
        if(val == 9) {
            this.wünschen = wünschen;
        }
    }

    public char getWünschen() {
        return wünschen;
    }
}
