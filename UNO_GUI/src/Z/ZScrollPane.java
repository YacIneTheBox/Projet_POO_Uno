package Z;

import javax.swing.*;
import java.awt.*;

public class ZScrollPane extends JScrollPane {
    public ZScrollPane(ZPanel panel) {
        super(panel);
        this.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        this.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);

        // Réglage de la sensibilité du scroll
        getVerticalScrollBar().setUnitIncrement(20);
        getHorizontalScrollBar().setUnitIncrement(20);

        this.setBorder(null);
        this.setPreferredSize(new Dimension(600, 150));
    }
}
