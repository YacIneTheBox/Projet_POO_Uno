package Z;


import javax.swing.JDialog;

public class ZDialog extends JDialog {
    public ZDialog() {
        this.setModalityType(JDialog.ModalityType.APPLICATION_MODAL);
    }
}