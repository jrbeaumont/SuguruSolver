import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
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
        setupTest();
    }

    private static void setupTest() {
        gridList.get(0).get(0).setText("4");
        gridList.get(1).get(3).setText("3");
        gridList.get(0).get(4).setText("2");
        gridList.get(3).get(4).setText("5");
        gridList.get(4).get(5).setText("3");
        gridList.get(4).get(0).setText("3");
        gridList.get(5).get(3).setText("2");
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

    private static JTextField[] getVerticalList(int index) {
        JTextField fields[] = new JTextField[rows];
        for (int i = 0; i < rows; i++) {
            fields[i] = gridList.get(i).get(index);
        }
        return fields;
    }

    private static boolean sanityCheck() {
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
    
    private static void solve() {
        ArrayList<ArrayList<JTextField>> groups = findGroups();
        ArrayList<BoxInfo> boxes = getBoxList(groups);
        checkNeighbours(boxes);
        updateGrid(boxes);
    }

    private static void updateGrid(ArrayList<BoxInfo> boxes) {
        for (BoxInfo box : boxes) {
            if (box.getTextField().getText().isEmpty()) {
                int[] numsLeft = box.getPossibleNumbers();
                boolean numFound = false;
                int num = 0;
                for (int i = 0; i < numsLeft.length; i++) {
                    if (numsLeft[i] > 0) { 
                            if (!numFound) {
                                numFound = true;
                                num = numsLeft[i];
                            } else {
                                numFound = false;
                                break;
                            }
                    }
                }
                if (numFound) {
                    box.getTextField().setText("" + num);
                }
            }
        }
    }

    private static void checkNeighbours(ArrayList<BoxInfo> boxes) {
        for (BoxInfo box : boxes) {
            if (box.getTextField().getText().isEmpty()) { 
                int[] numsLeft = box.getPossibleNumbers();
                int x = box.getX();
                int y = box.getY();

                ArrayList<JTextField> neighbours = new ArrayList<JTextField>(); 

                if (x > 0) {
                    //left
                    neighbours.add(gridList.get(y).get(x - 1));
                    if (y > 0) {
                        //left below
                        neighbours.add(gridList.get(y - 1).get(x - 1));
                    }
                    if (y < rows - 1) {
                        //left above
                        neighbours.add(gridList.get(y + 1).get(x - 1));
                    }
                }
                if (x < columns - 1) {
                    //right
                    neighbours.add(gridList.get(y).get(x + 1));
                    if (y > 0) {
                        //right below
                        neighbours.add(gridList.get(y - 1).get(x + 1));
                    }
                    if (y < rows - 1) {
                        //right above
                        neighbours.add(gridList.get(y + 1).get(x + 1));
                    }
                }
                if (y > 0) {
                    //below
                    neighbours.add(gridList.get(y - 1).get(x));
                }
                if (y < rows - 1) {
                    //above
                    neighbours.add(gridList.get(y + 1).get(x));
                }

                for (JTextField t : neighbours) {
                    if (!t.getText().isEmpty()) {
                        int n = Integer.parseInt(t.getText());
                        if (numsLeft.length > n) {
                            numsLeft[n - 1] = 0;
                        }
                    }
                }
                System.out.println(x + " " + y + ": ");
                for (int i = 0; i < numsLeft.length; i++) {
                    System.out.print(numsLeft[i]);
                }
                System.out.println();
            }
        }
    }

    private static ArrayList<BoxInfo> getBoxList(ArrayList<ArrayList<JTextField>> groups) {
        ArrayList<BoxInfo> result = new ArrayList<>();
        for (ArrayList<JTextField> l : groups){
            int[] numsLeft = getNumbersLeftInGroup(l);
            for (JTextField t : l) {
                Point2D.Double coordinates = getCoordinates(t);
                int x = (int) coordinates.getX();
                int y = (int) coordinates.getY();
                BoxInfo b;

                if (!t.getText().isEmpty()) {
                    int[] temp = new int[l.size()];
                    for (int i = 0; i < temp.length; i++) {
                        temp[i] = 0;
                    }
                    int n = Integer.parseInt(t.getText());
                    temp[n - 1] = n;
                    b = new BoxInfo(x, y, temp, t, l);
                } else {
                    b = new BoxInfo(x, y, numsLeft, t, l);
                }
                result.add(b);
            }
        }
        return result;
    }

    private static int[] getNumbersLeftInGroup(ArrayList<JTextField> l) {
        int[] numsLeft = new int[l.size()];

        for (int i = 0; i < numsLeft.length; i++) {
            numsLeft[i] = i + 1;
        }

        for (JTextField t : l) {
            if (!t.getText().isEmpty()) {
                int n = Integer.parseInt(t.getText());
                if (numsLeft.length > n) {
                    numsLeft[n - 1] = 0;
                }
            }
        }
        return numsLeft;
    }

    private static Point2D.Double getCoordinates(JTextField t) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (gridList.get(i).get(j).equals(t)) {
                    return new Point2D.Double(j, i);
                }
            }
        }
        return null;
    }

    private static ArrayList<ArrayList<JTextField>> findGroups() {
        ArrayList<ArrayList<JTextField>> groups = new ArrayList<>();

        for (int i = 0; i < columns; i++) {
            for (int j = 0; j < rows; j++) {
                JTextField current = gridList.get(i).get(j);
                if (!contains(groups, current)) {
                    ArrayList<JTextField> group = new ArrayList<>();
                    findGroupMembers(group, j, i);
                    groups.add(group);
                }
            }
            
        }

        return groups;
    }

    private static void findGroupMembers(ArrayList<JTextField> group, int x, int y) {
        JTextField current = gridList.get(y).get(x);
        if (!group.contains(current)) {
            group.add(current);
            Border b = findOpenBorders(current, x, y);

            if (!b.getLeft()) {
                findGroupMembers(group, x - 1, y);
            }

            if (!b.getTop()) {
                findGroupMembers(group, x, y + 1);
            }

            if (!b.getRight()) {
                findGroupMembers(group, x + 1, y);
            }

            if (!b.getBottom()) {
                findGroupMembers(group, x, y - 1);
            }
        }
        
    }

    private static Border findOpenBorders(JTextField current, int x, int y) {
        Border b = new Border(x, y);

        GridLine left = verticalLines.get(x).get(y);
        GridLine right = verticalLines.get(x+1).get(y);
        GridLine top = horizontalLines.get(y + 1).get(x);
        GridLine bottom = horizontalLines.get(y).get(x);

        b.setLeft(left.getIsBorder());
        b.setRight(right.getIsBorder());
        b.setTop(top.getIsBorder());
        b.setBottom(bottom.getIsBorder());

        return b;
    }

    private static boolean contains(ArrayList<ArrayList<JTextField>> groups, JTextField current) {
        for (ArrayList<JTextField> l : groups) {
            if (l.contains(current)) return true;
        }
        return false;
    }


}
