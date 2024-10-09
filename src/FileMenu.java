import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;

public class FileMenu implements ActionListener {
    private final JMenu fileMenu;
    private final JMenuItem openItem;
    private final JMenuItem saveItem;
    private final JMenuItem saveAsItem;
    private final JMenuItem quitItem;
    private final Frame frame;
    private File currentFile;

    private Status status;

    public FileMenu(Frame frame) {
        this.frame = frame;
        status = Status.NEW;

        fileMenu = new JMenu("File");

        openItem = new JMenuItem("Open");
        saveItem = new JMenuItem("Save");
        saveAsItem = new JMenuItem("Save as");
        quitItem = new JMenuItem("Quit");

        openItem.addActionListener(this);
        saveItem.addActionListener(this);
        saveAsItem.addActionListener(this);
        quitItem.addActionListener(this);

        openItem.setMnemonic(KeyEvent.VK_O); // o for open
        saveItem.setMnemonic(KeyEvent.VK_S); // s for save
        saveAsItem.setMnemonic(KeyEvent.VK_A); // a for save as
        quitItem.setMnemonic(KeyEvent.VK_Q); // q for quit
        fileMenu.setMnemonic(KeyEvent.VK_F); // alt + f for file menu

        openItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK));
        saveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
        saveAsItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.SHIFT_DOWN_MASK | KeyEvent.CTRL_DOWN_MASK)); // ctrl+shift+s for save as
        quitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, KeyEvent.CTRL_DOWN_MASK));

        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.add(saveAsItem);
        fileMenu.addSeparator();
        fileMenu.add(quitItem);
    }

    public Status getStatus(){
        return status;
    }

    public JMenu getFileMenu() {
        return fileMenu;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JFileChooser fileChooser;
        if (e.getSource() == openItem) {
            fileChooser = new JFileChooser();
            int loadingFile = fileChooser.showOpenDialog(null); // select file to open
            if (loadingFile == JFileChooser.APPROVE_OPTION) {
                File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
                currentFile = file;
                frame.getDrawPanel().loadShapesFromFile(file);
                frame.setFrameTitle(file.getName());
            }
            status = Status.SAVED;
            frame.setFileState(status);
        } else if (e.getSource() == saveItem) {
            if(currentFile == null) {
                fileChooser = new JFileChooser();
                int savingFile = fileChooser.showSaveDialog(null);
                if(savingFile == JFileChooser.APPROVE_OPTION) {
                    currentFile = new File(fileChooser.getSelectedFile().getAbsolutePath());
                }
            }
            if(currentFile != null) {
                frame.getDrawPanel().saveShapesToFile(currentFile);
                frame.setFrameTitle(currentFile.getName());
            }
            status = Status.SAVED;
            frame.setFileState(status);
        } else if (e.getSource() == saveAsItem) {
            fileChooser = new JFileChooser();
            int savingFile = fileChooser.showSaveDialog(null); // wybór pliku do zapisu jako
            if(savingFile == JFileChooser.APPROVE_OPTION) {
                File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
                currentFile = file;
                frame.getDrawPanel().saveShapesToFile(file);
                frame.setFrameTitle(file.getName());
            }
            status = Status.SAVED;
            frame.setFileState(status);
        } else if (e.getSource() == quitItem) {
            System.exit(0);
        }
    }
}