import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class Frame extends JFrame implements ActionListener {
    private DrawPanel drawPanel;
    private boolean drawingEnabled = false;
    private StatusBar statusBar;
    public Frame() {
        this.setTitle("Simple Draw");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(600, 500);

        drawPanel = new DrawPanel(this);
        this.add(drawPanel, BorderLayout.CENTER);

        Menu menu = new Menu(this);
        JMenuBar menuBar = menu.getJMenuBar();
        this.setJMenuBar(menuBar);

        ImageIcon image = new ImageIcon("images/paintbrush.png");
        this.setIconImage(image.getImage());

        statusBar = new StatusBar();
        this.add(statusBar, BorderLayout.SOUTH); // dodajemy status bar na dół ramki
        statusBar.setFileState(menu.getStatus());

        // obsługa przycisku F1 dla figur
        this.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0), "enableDrawing");
        this.getRootPane().getActionMap().put("enableDrawing", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(drawingEnabled) {
                    String currentTool = drawPanel.getCurrentTool();
                    if (currentTool.equals("Circle") || currentTool.equals("Square")) {
                        // pobieranie współrzędnych globalnych kursora
                        Point mouseLocation = MouseInfo.getPointerInfo().getLocation();
                        // przekształcanie współrzędnych kursora na lokalne panelu rysowania
                        SwingUtilities.convertPointFromScreen(mouseLocation, drawPanel);
                        // rysowanie kształtu w miejscu kursora
                        drawPanel.drawShape(mouseLocation, currentTool);
                    }
                }
            }
        });

        this.setVisible(true);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if(command.equals("Pen")) {
            drawPanel.setCurrentTool("Pen");
            statusBar.setDrawingMode("Pen");
            drawingEnabled = false; // rysowanie figur wyłączone
            statusBar.setDrawingMode("Pen");
        } else if (command.equals("Circle") || command.equals("Square")) {
            drawPanel.setCurrentTool(command);
            drawingEnabled = true; // rysowanie figur włączone
            statusBar.setDrawingMode(command);
        } else if (command.equals("Clear")) {
            drawPanel.clearFrame();
        }
    }

    public boolean isDrawingEnabled() {
        return drawingEnabled;
    }
    public DrawPanel getDrawPanel() {
        return drawPanel;
    }
    public void setFrameTitle(String fileName) {
        if(fileName == null || fileName.isEmpty()) {
            this.setTitle("Simple Draw");
        } else {
            this.setTitle(fileName);
        }
    }
    public void setFileState(Status state) {
        statusBar.setFileState(state);
    }
}