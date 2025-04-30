import Z.* ;

import javax.swing.*;
import java.awt.*;
import java.awt.Dimension;


public class GameWindow {
    private final ZFrame frame;

    public GameWindow(String title) {
        frame = new ZFrame(title);
        frame.setLayout(new GridBagLayout());
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
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        switch(position.toLowerCase()) {
            case "top" -> {
                gbc.gridx = 1;
                gbc.gridy = 0;
            }
            case "left" -> {
                gbc.gridx = 0;
                gbc.gridy = 1;
            }
            case "right" -> {
                gbc.gridx = 2;
                gbc.gridy = 1;
            }
            default -> { // "bottom"
                gbc.gridx = 1;
                gbc.gridy = 2;
            }
        }

        frame.add(panel, gbc);
    }



    public void setContent(ZPanel panel) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        frame.add(panel, gbc);
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

    public void setMinimumSize(Dimension dimension) {
        frame.setMinimumSize(dimension);
    }
}