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
public class ZScrollPane extends JScrollPane {
public ZScrollPane(ZPanel panel) {
	super(panel) ;
    this.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    this.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    this.setBorder(null);
    this.setPreferredSize(new Dimension(600, 150));
    setBorder(null);
    setOpaque(false);
    setBackground(Color.darkGray);
}
}
