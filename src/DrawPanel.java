import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DrawPanel extends JPanel {
    private List<Shape> shapes = new ArrayList<>();
    private List<Color> colors = new ArrayList<>();
    private Shape currentShape;
    private String currentTool = "";
    private Point startPoint;
    private Color currentColor = Color.BLACK; // default color of pen is black
    private boolean drawingEnabled = false;

    private boolean isDPressed = false;
    private boolean isLeftMouseButtonPressed = false;

    private final Frame frame;

    public DrawPanel(Frame frame) {
        this.frame = frame;
        setBackground(Color.WHITE);
        setFocusable(true);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_D){
                    isDPressed = true;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_D){
                    isDPressed = false;
                }
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if(SwingUtilities.isLeftMouseButton(e)){
                    isLeftMouseButtonPressed = true;
                    checkAndDeleteFigure(e);
                }

                startPoint = e.getPoint();
                if (currentTool.equals("Pen")) {
                    frame.setFileState(Status.MODIFIED);
                    currentShape = new Path2D.Double();
                    ((Path2D) currentShape).moveTo(startPoint.x, startPoint.y);
                } 
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                if(SwingUtilities.isLeftMouseButton(e)){
                    isLeftMouseButtonPressed = false;
                }

                if(currentTool.equals("Pen")) {
                    shapes.add(currentShape);
                    colors.add(currentColor);
                    currentShape = null;
                    repaint();
                }
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if(currentTool.equals("Pen")) {
                    ((Path2D) currentShape).lineTo(e.getX(), e.getY());
                    repaint();
                }
            }
        });
    }

    private void checkAndDeleteFigure(MouseEvent e){
        if(isDPressed && isLeftMouseButtonPressed){
            for (int i = shapes.size() - 1; i >= 0; i--) {
                if(shapes.get(i).contains(e.getPoint())) {
                    int result = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this shape?", "Confirmation of deletion", JOptionPane.YES_NO_OPTION);
                    if(result == JOptionPane.YES_OPTION) {
                        shapes.remove(i);
                        colors.remove(i);
                        repaint();
                    }
                    break;
                }
            }
            frame.setFileState(Status.MODIFIED);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        for (int i = 0; i < shapes.size(); i++) {
            g2d.setColor(colors.get(i));
            Shape shape = shapes.get(i);
            if(shape instanceof Ellipse2D || shape instanceof Rectangle2D) {
                g2d.fill(shape); // wypełnienie tylko w przypadku koła lub kwadratu
            } else {
                g2d.draw(shape); // brak wypełnienia dla długopisu
            }
        }
        if(currentShape != null && currentTool.equals("Pen")) {
            g2d.setColor(currentColor);
            g2d.draw(currentShape);
        }
    }

    public void setCurrentTool(String tool) {
        currentTool = tool;
    }

    public void setCurrentColor(Color color) {
        currentColor = color;
    }

    public void clearFrame() {
        shapes.clear();
        colors.clear();
        frame.setFileState(Status.NEW);
        repaint();
    }

    public void drawShape(Point startPoint, String shapeType) {
        int size = 25; // rozmiar kształtu
        int x = startPoint.x - size / 2; // obliczanie pozycji startowej
        int y = startPoint.y - size / 2; // obliczanie pozycji startowej
        if(shapeType.equals("Circle")) {
            shapes.add(new Ellipse2D.Double(x, y, size, size));
        } else if (shapeType.equals("Square")) {
            shapes.add(new Rectangle2D.Double(x, y, size, size));
        }
        colors.add(randomColor());
        frame.setFileState(Status.MODIFIED);
        repaint();
    }

    public String getCurrentTool() {
        return currentTool;
    }

    private Color randomColor() {
        Random random = new Random();
        return new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
    }

    public void setDrawingEnabled(boolean enabled) {
        drawingEnabled = enabled;
    }

    public void saveShapesToFile(File file) {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(file))) {
            objectOutputStream.writeObject(shapes);
            objectOutputStream.writeObject(colors);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadShapesFromFile(File file) {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file))) {
            shapes = (List<Shape>) objectInputStream.readObject();
            colors = (List<Color>) objectInputStream.readObject();
            repaint();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

