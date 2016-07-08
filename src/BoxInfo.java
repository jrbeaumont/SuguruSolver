import java.util.ArrayList;

import javax.swing.JTextField;

public class BoxInfo {

    private int x;
    private int y;
    private int[] possibleNumbers;
    private JTextField t;
    private ArrayList<JTextField> group;
    
    public BoxInfo(int x, int y, int n[], JTextField t, ArrayList<JTextField> group) {
        this.x = x;
        this.y = y;
        possibleNumbers = n;
        this.t = t;
        this.group = group;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int[] getPossibleNumbers() {
        return possibleNumbers;
    }

    public JTextField getTextField() {
        return t;
    }

    public ArrayList<JTextField> getGroup() {
        return group;
    }

    public String toString() {
        return "X: " + x + " Y: " + y;
    }
}
