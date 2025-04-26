package Z;
import javax.swing.JButton;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ZButton extends JButton {

	private Dimension normalSize = new Dimension(115, 35);
	private Dimension zoomedSize = new Dimension(125, 43); // +10%
	private Timer zoomInTimer, zoomOutTimer;

	public ZButton(String text , Color c) {
		super(text);
		setForeground(Color.WHITE);          // Texte en blanc
		setBackground(c); // Rouge doux (tu peux ajuster)
		setFocusPainted(false);              // Pas de contour de focus
		setBorderPainted(false);            // Pas de bordure
		setContentAreaFilled(false);        // On dessine manuellement le fond
		setOpaque(false);                   // Transparent, car on gère le rendu custom
		setFont(getFont().deriveFont(Font.BOLD, 14f)); // Optionnel : police personnalisée

		setPreferredSize(normalSize);
		setBorderPainted(false);
		setContentAreaFilled(false);
		setFocusPainted(false);
		setCursor(new Cursor(Cursor.HAND_CURSOR));
		setBackground(Color.RED); // exemple

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

	private void zoomIn() {
		zoomInTimer = new Timer(10, new ActionListener() {
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

	private void zoomOut() {
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

		// Fond arrondi rouge
		g2.setColor(getBackground());
		g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);

		super.paintComponent(g);
		g2.dispose();
	}

	@Override
	public void updateUI() {
		super.updateUI();
		setUI(new javax.swing.plaf.basic.BasicButtonUI()); // Pour éviter LookAndFeel étranger
	}
}
