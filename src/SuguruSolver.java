import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;

import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class SuguruSolver {

    private static JFrame mainWindow;
    
    private static final int rows = 6;
    private static final int columns = 6;
    private static ArrayList<ArrayList<JTextField>> gridList;
    private static ArrayList<ArrayList<GridLine>> verticalLines;
    private static ArrayList<ArrayList<GridLine>> horizontalLines;
    private static LinePanel gridPanel;
    private static int componentNo = 0;

    public static void main(String[] args) {
        mainWindow = new JFrame();
        setupWindow();
    }

    private static void setupWindow() {
        mainWindow.setLayout(new BorderLayout());
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainWindow.setTitle("Suguru Solver");

        JLabel label = new JLabel("Enter numbers and press go!");
        mainWindow.add(label, BorderLayout.NORTH);

        generateGrid();

        JPanel buttonPanel = new JPanel();
        JButton goBtn = new JButton("Go");
        buttonPanel.add(goBtn);

        mainWindow.add(buttonPanel, BorderLayout.SOUTH);

        mainWindow.setSize(500, 500);
        mainWindow.setVisible(true);
        mainWindow.setResizable(false);
        generateBorders();
    }
    
    private static void generateGrid() {
        GridLayout grid = new GridLayout();
        grid.setColumns(columns);
        grid.setRows(rows);
        grid.setVgap(6);
        grid.setHgap(6);

        gridPanel = new LinePanel();
        gridPanel.setLayout(grid);

        gridList = new ArrayList<>();

        Font font = new Font("SansSerif", Font.PLAIN, 40);

        for (int i = 0; i < rows; i++) {
            ArrayList<JTextField> list = new ArrayList<>();
            for (int j = 0; j < columns; j++) {
                JTextField t = new JTextField(1);
                list.add(t);
                t.setFont(font);
                t.setSize(40, 40);
                gridPanel.add(t, i, j);
            }
            gridList.add(list);
        }
        mainWindow.add(gridPanel, BorderLayout.CENTER);
    }

    private static void generateBorders() {
        verticalLines = new ArrayList<>();
        horizontalLines = new ArrayList<>();

        for (int i = 0; i < (columns + 1); i++) {
            ArrayList<GridLine> h = new ArrayList<GridLine>();
            int index = i;
            boolean bottomBorder = false;
            if (i == 0) {
                bottomBorder = true;
            } else {
                index = i - 1;
            }
            for (JTextField t : gridList.get(index)) {
                GridLine line;
                
                int x = (int) t.getLocation().getX() - 5;
                int y = (int) t.getLocation().getY() - 5;
                int l = t.getWidth() + 4;
                if (bottomBorder) {
                    y += t.getHeight();
                }
                if (i == columns || bottomBorder) {
                    line = new GridLine(x, y, l, true, false, true);
                } else {
                    line = new GridLine(x, y, l, false, true, true);
                }
                h.add(line);
                gridPanel.add(line);
            }
            horizontalLines.add(h);
        }

        for (int i = 0; i < (rows + 1); i++) {
            ArrayList<GridLine> v = new ArrayList<GridLine>();
            int index = i;
            boolean rightBorder = false;
            if (i == rows) {
                rightBorder = true;
                index = rows - 1;
            }
            for (JTextField t : getVerticalList(index)) {
                GridLine line;
                int x = (int) t.getLocation().getX();
                if (index == 0) {
                    x -= 1;
                } else
                {
                    x -= 5;
                }
                int y = (int) t.getLocation().getY();
                int h = t.getHeight();
                System.out.println(h);
                if (rightBorder) {
                    x += t.getWidth();
                }
                if (i == columns || rightBorder) {
                    line = new GridLine(x, y, h, true, false, false);
                } else {
                    line = new GridLine(x, y, h, false, true, false);
                }
                gridPanel.add(line);
                v.add(line);
            }
            verticalLines.add(v);
        }

    }

    public static JTextField[] getVerticalList(int index) {
        JTextField fields[] = new JTextField[rows];
        for (int i = 0; i < rows; i++) {
            fields[i] = gridList.get(i).get(index);
        }
        return fields;
    }

}
