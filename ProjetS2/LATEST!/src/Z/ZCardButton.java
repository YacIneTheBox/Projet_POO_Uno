package Z;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
public class ZCardButton extends JButton {
	
	public ZCardButton (ImageIcon img) {
		super(img);
		this.setPreferredSize(new Dimension(80, 120));
		this.setBorderPainted(true);
	    this.setContentAreaFilled(false);
	    this.setFocusPainted(false);
	}

}
