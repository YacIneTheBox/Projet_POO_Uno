package Z;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ZCardButton extends JButton {

	private static Dimension normalSize = new Dimension(80, 120);
	private static Dimension zoomedSize = new Dimension(88, 132); // +10%
	private static Timer zoomInTimer;
    private static Timer zoomOutTimer;

	public ZCardButton(ImageIcon img) {
		super(img);
		setPreferredSize(normalSize);
		setBorderPainted(false);
		setContentAreaFilled(false);
		setFocusPainted(false);
		setCursor(new Cursor(Cursor.HAND_CURSOR));
		setOpaque(false);

		//hnaya les animation pour les carte des joueur

		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				if (zoomOutTimer != null && zoomOutTimer.isRunning()) zoomOutTimer.stop();
				zoomIn();
			}

			@Override
			public void mouseExited(MouseEvent e) {
				if (zoomInTimer != null && zoomInTimer.isRunning()) zoomInTimer.stop();
				zoomOut();
			}
		});
	}
	//ici c la definition de la fonction du zoom lorsqu'on passe le cursseur sur les cartes
	public void zoomIn() {
		zoomInTimer = new Timer(15, new ActionListener() {
			int step = 0;
			public void actionPerformed(ActionEvent e) {
				step++;
				int width = normalSize.width + (zoomedSize.width - normalSize.width) * step / 5;
				int height = normalSize.height + (zoomedSize.height - normalSize.height) * step / 5;
				setPreferredSize(new Dimension(width, height));
				revalidate();
				getParent().repaint();
				if (step >= 5) ((Timer) e.getSource()).stop();
			}
		});
		zoomInTimer.start();
	}
	// ici c la fonction du zoom out quand on passe le cursseur sur les cartes
	public void zoomOut() {
		zoomOutTimer = new Timer(10, new ActionListener() {
			int step = 0;
			public void actionPerformed(ActionEvent e) {
				step++;
				int width = zoomedSize.width - (zoomedSize.width - normalSize.width) * step / 5;
				int height = zoomedSize.height - (zoomedSize.height - normalSize.height) * step / 5;
				setPreferredSize(new Dimension(width, height));
				revalidate();
				getParent().repaint();
				if (step >= 5) ((Timer) e.getSource()).stop();
			}
		});
		zoomOutTimer.start();
	}

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		int arc = 30;
		int w = getWidth();
		int h = getHeight();



		// Dégradé glossy
		GradientPaint gp = new GradientPaint(0, 0, getBackground().brighter(), 0, h, getBackground().darker());
		g2.setPaint(gp);
		g2.fillRoundRect(0, 0, w - 4, h - 2, arc, arc);

		// Reflet
		g2.setColor(new Color(92, 89, 89, 0));
		g2.fillRoundRect(10, 10, w - 20, h / 2 - 10, 20, 20);

		g2.dispose();
		super.paintComponent(g);
	}

	@Override
	public void updateUI() {
		super.updateUI();
		setUI(new javax.swing.plaf.basic.BasicButtonUI());
	}
}
