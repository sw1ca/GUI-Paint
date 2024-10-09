import javax.swing.*;

public class Menu {
    private final FileMenu fileMenu;
    private final DrawMenu drawMenu;

    public Menu(Frame frame) {
        fileMenu = new FileMenu(frame);
        drawMenu = new DrawMenu(frame);
    }

    public JMenuBar getJMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(fileMenu.getFileMenu());
        menuBar.add(drawMenu.getDrawMenu());
        return menuBar;
    }

    public Status getStatus(){
        return fileMenu.getStatus();
    }
}
