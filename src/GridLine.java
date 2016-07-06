import java.awt.geom.Rectangle2D;

@SuppressWarnings("serial")
public class GridLine extends Rectangle2D.Double{

    private boolean isBorder;
    private boolean canChange;
    private boolean isVert;

    public GridLine(int x, int y, int l, boolean isBorder, boolean canChange, boolean isVert) {
        super(x, y, 1, 1);
        setIsBorder(isBorder);
        this.canChange = canChange;
        this.isVert = isVert;
        setLength(l);
    }

    public boolean getIsBorder() {
        return isBorder;
    }

    public void setIsBorder(boolean b) {
        isBorder = b;
        updateWidth();
    }

    public boolean getCanChange(){
        return canChange;
    }

    private void updateWidth() {
        if (isBorder) {
            if (isVert) {
                height = 8;
            } else {
                width = 8;
            }
        } else {
            if (isVert) {
                height = 3;
            } else {
                width = 3;
            }
        }
    }
    
    private void setLength(int l) {
        if (isVert) {
            width = l;
        } else {
            height = l;
        }
        updateWidth();
    }

}
