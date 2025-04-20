package Z;
import java.awt.BorderLayout;

import javax.swing.*;
public class ZFrame extends JFrame {
public ZFrame(String msg) {
	super(msg);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setLayout(new BorderLayout());
}
}
