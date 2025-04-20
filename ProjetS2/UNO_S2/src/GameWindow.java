import Z.* ;

import javax.swing.*;
import java.awt.*;

public class GameWindow {
    private final ZFrame frame;

    public GameWindow(String title) {
        frame = new ZFrame(title);
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
    public void close() {
    	frame.dispose();
    }
    public void addPlayerPanel(ZPanel panel, String position) {
        switch(position.toLowerCase()) {
            case "top" -> frame.add(panel, BorderLayout.NORTH);
            case "left" -> frame.add(panel, BorderLayout.WEST);
            case "right" -> frame.add(panel, BorderLayout.EAST);
            default -> frame.add(panel, BorderLayout.SOUTH);
        }
    }

    public void setContent(ZPanel panel) {
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