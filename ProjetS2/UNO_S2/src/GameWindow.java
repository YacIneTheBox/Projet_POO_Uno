import javax.swing.*;
import java.awt.*;

public class GameWindow {
    private final JFrame frame;

    public GameWindow(String title) {
        frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
    }

    // Add this method to fix the repaint issue
    public void repaint() {
        if (!EventQueue.isDispatchThread()) {
            EventQueue.invokeLater(this::repaint);
            return;
        }
        frame.revalidate();
        frame.repaint();
    }

    public void addPlayerPanel(JPanel panel, String position) {
        switch(position.toLowerCase()) {
            case "top" -> frame.add(panel, BorderLayout.NORTH);
            case "left" -> frame.add(panel, BorderLayout.WEST);
            case "right" -> frame.add(panel, BorderLayout.EAST);
            default -> frame.add(panel, BorderLayout.SOUTH);
        }
    }

    public void setContent(JPanel panel) {
        frame.getContentPane().add(panel, BorderLayout.CENTER);
    }

    public Container getContentPane() {
        return frame.getContentPane();
    }

    public void setVisible(boolean visible) {
        frame.setVisible(visible);
    }

    public void setSize(int width, int height) {
        frame.setSize(width, height);
    }
}