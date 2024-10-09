import javax.swing.*;

public class StatusBar extends JPanel {
    private JLabel drawingModeLabel;
    private JLabel fileStateLabel;

    public StatusBar() {
        super();
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        drawingModeLabel = new JLabel("Drawing Mode: ");
        fileStateLabel = new JLabel("File State: ");

        this.add(drawingModeLabel);
        this.add(Box.createHorizontalGlue());
        this.add(fileStateLabel);
    }

    public void setDrawingMode(String mode) {
        drawingModeLabel.setText("Mode: " + mode);
    }

    public void setFileState(Status state) {
        fileStateLabel.setText("File state: " + state.name());
    }
}
