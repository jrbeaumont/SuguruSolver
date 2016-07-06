
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class LinePanel extends JPanel{

    private ArrayList<GridLine> lines = new ArrayList<>();

    public LinePanel() {
        super();
        this.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                Point clickedPoint = e.getPoint();

                for (GridLine l : lines) {
                    if (l.contains(clickedPoint)) {
                        if (l.getCanChange()) {
                            l.setIsBorder(!l.getIsBorder());
                            repaint();
                        }
                    }
                }

            }

            @Override
            public void mousePressed(MouseEvent e) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // TODO Auto-generated method stub
                
            }
            
        });
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        for (GridLine l : lines) {
                g2d.draw(l);
                g2d.fillRect((int) l.getX(), (int) l.getY(), (int) l.getWidth(), (int) l.getHeight());
        }
    }

    public void add(GridLine l) {
        lines.add(l);
    }

}
