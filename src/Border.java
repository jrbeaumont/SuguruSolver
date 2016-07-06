
public class Border {
    
    private int x;
    private int y;
    private boolean left;
    private boolean top;
    private boolean right;
    private boolean bottom;
    
    public Border(int x, int y) {
        this.x = x;
        this.y = y;
        left = false;
        top = false;
        right = false;
        bottom = false;
    }

    public void setLeft(boolean l) {
        left = l;
    }

    public boolean getLeft() {
        return left;
    }

    public void setTop(boolean t) {
        top = t;
    }

    public boolean getTop() {
        return top;
    }

    public void setRight(boolean r) {
        right = r;
    }

    public boolean getRight() {
        return right;
    }

    public void setBottom(boolean b) {
        bottom = b;
    }

    public boolean getBottom() {
        return bottom;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

}
