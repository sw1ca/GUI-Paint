import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class DrawMenu implements ActionListener {
    private final JMenu drawMenu;
    private final JMenu figureSubMenu;
    private final JMenuItem color;
    private final JMenuItem clear;

    private final JMenuItem circle;
    private final JMenuItem square;
    private final JMenuItem pen;

    private ButtonGroup figureGroup;
    private Frame frame;

    public DrawMenu(Frame frame) {
        this.frame = frame;

        drawMenu = new JMenu("Draw");
        figureSubMenu = new JMenu("Figure");

        circle = new JRadioButtonMenuItem("Circle");
        square = new JRadioButtonMenuItem("Square");
        pen = new JRadioButtonMenuItem("Pen");

        circle.addActionListener(frame);
        square.addActionListener(frame);
        pen.addActionListener(frame);

        figureGroup = new ButtonGroup();
        figureGroup.add(circle);
        figureGroup.add(square);
        figureGroup.add(pen);

        figureSubMenu.add(circle);
        figureSubMenu.add(square);
        figureSubMenu.add(pen);

        color = new JMenuItem("Color");
        clear = new JMenuItem("Clear");

        drawMenu.addActionListener(this);
        color.addActionListener(this);
        clear.addActionListener(this);

        drawMenu.setMnemonic(KeyEvent.VK_D); // alt + d for draw menu
        color.setMnemonic(KeyEvent.VK_C); // c for color
        clear.setMnemonic(KeyEvent.VK_L); //  l for clear
        circle.setMnemonic(KeyEvent.VK_L); //  r for circle
        square.setMnemonic(KeyEvent.VK_L); //  u for square
        pen.setMnemonic(KeyEvent.VK_L); //  p for pen
        figureSubMenu.setMnemonic(KeyEvent.VK_F); // f for figure

        color.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_DOWN_MASK)); // ctrl + c for color
        clear.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, KeyEvent.CTRL_DOWN_MASK)); // ctrl + l for clear
        circle.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, KeyEvent.CTRL_DOWN_MASK)); // ctrl + r for circle
        square.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_U, KeyEvent.CTRL_DOWN_MASK)); // ctrl + u for square
        pen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, KeyEvent.CTRL_DOWN_MASK)); // ctrl + p for pen

        drawMenu.add(figureSubMenu);
        drawMenu.add(color);
        drawMenu.addSeparator();
        drawMenu.add(clear);
    }
    public JMenu getDrawMenu() {
        return drawMenu;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == color) {
            Color selectedColor = JColorChooser.showDialog(null, "Choose a color", Color.BLACK);
            if(selectedColor != null) {
                frame.getDrawPanel().setCurrentColor(selectedColor);
            }
        } else if(e.getSource() == clear) {
            frame.getDrawPanel().clearFrame();
        }
    }
}

