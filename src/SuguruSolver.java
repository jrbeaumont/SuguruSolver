import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
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
        JButton clearBtn = new JButton("Clear");
        buttonPanel.add(goBtn);
        buttonPanel.add(clearBtn);
        addActionListeners(goBtn, clearBtn);

        mainWindow.add(buttonPanel, BorderLayout.SOUTH);

        mainWindow.setSize(500, 500);
        mainWindow.setVisible(true);
        mainWindow.setResizable(false);
        generateBorders();

    }
    
    private static void addActionListeners(JButton go, JButton clear) {

        go.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                sanityCheck();
                solve();
            }
            
        });

        clear.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < gridList.size(); i++) {
                    for (JTextField t : gridList.get(i)) {
                        t.setText("");
                    }
                }
                for (int i = 0; i < horizontalLines.size(); i++) {
                    for (GridLine l : horizontalLines.get(i)) {
                        if (l.getCanChange()) l.setIsBorder(false);
                    }
                }
                for (int i = 0; i < verticalLines.size(); i++) {
                    for (GridLine l : verticalLines.get(i)) {
                        if (l.getCanChange()) l.setIsBorder(false);
                    }
                }
                mainWindow.repaint();
            }

        });
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
                index--;
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
            }
            if (i > 0){
                index--;
            }
            for (JTextField t : getVerticalList(index)) {
                GridLine line;
                int x = (int) t.getLocation().getX();
                int y = (int) t.getLocation().getY();
                int h = t.getHeight() + 4;
                if (i != 0) {
                    x += t.getWidth();
                }
                if (rightBorder) {
                    x -= 2;
                }
                if (i == 0 || rightBorder) {
                    line = new GridLine(x, y, h, true, false, false);
                } else {
                    line = new GridLine(x, y, h, false, true, false);
                }
                v.add(line);
                gridPanel.add(line);
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

    public static boolean sanityCheck() {
        for (int i = 0; i < gridList.size(); i++) {
            for (JTextField t : gridList.get(i)) {
                String s = t.getText();

                if (s.length() > 1) {
                    JOptionPane.showMessageDialog(mainWindow, "One box contains too many characters", "ERROR", JOptionPane.ERROR_MESSAGE);
                    return false;
                } else if (!s.isEmpty() && (s.charAt(0) > '9' || s.charAt(0) < '0')) {
                    JOptionPane.showMessageDialog(mainWindow, "One box contains a non numeric character", "ERROR", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
        }
        return true;
    }
    
    public static void solve() {
        ArrayList<ArrayList<JTextField>> groups = findGroups();
    }
    
    public static ArrayList<ArrayList<JTextField>> findGroups() {
        ArrayList<ArrayList<JTextField>> groups = new ArrayList<>();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (!contains(groups, i, j)) {
                    Border b = findBorders(i, j);
                    ArrayList<JTextField> g = findGroupMembers(b);
                }
            }
        }

        return groups;
    }

    private static ArrayList<JTextField> findGroupMembers(Border b) {
        ArrayList<JTextField> result = new ArrayList<JTextField>();
        result.add(gridList.get(b.getX()).get(b.getY()));
        
        if (!b.getLeft()) {
            result.addAll(findGroupMembers(findBorders(b.getX() - 1, b.getY())));
        }

        if (!b.getTop()) {
            result.addAll(findGroupMembers(findBorders(b.getX(), b.getY() + 1)));
        }

        if (!b.getRight()) {
            result.addAll(findGroupMembers(findBorders(b.getX() + 1, b.getY())));
        }

        if (!b.getBottom()) {
            result.addAll(findGroupMembers(findBorders(b.getX(), b.getY() - 1)));
        }

        return result; 
    }

    private static boolean contains(ArrayList<ArrayList<JTextField>> groups, int i, int j) {
        JTextField c = gridList.get(i).get(j);
        for (ArrayList<JTextField> l : groups) {
            if (l.contains(c)) {
                return true;
            }
        }
        return false;
    }

    public static Border findBorders(int x, int y) {
        Border b = new Border(x, y);

        b.setBottom(horizontalLines.get(x).get(x).getIsBorder());

        b.setTop(horizontalLines.get(x+1).get(x).getIsBorder());

        b.setLeft(verticalLines.get(y).get(y).getIsBorder());

        b.setRight(verticalLines.get(y+1).get(y).getIsBorder());
        
        return b;
    }

}
