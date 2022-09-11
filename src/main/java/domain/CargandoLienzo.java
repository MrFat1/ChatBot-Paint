package domain;

import util.NLPUtils;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/** Panel que contiene la animacion de inicio de programa */
public class CargandoLienzo extends JPanel implements Runnable {

    private Image img;

    public CargandoLienzo() {

        img = Toolkit.getDefaultToolkit().createImage("resources/loading-1.gif");

        new Thread(this).start();

    }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (img != null) {
            g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
        }

        g.dispose();

    }

    @Override
    public void run() {

        ImageIcon icon = new ImageIcon("resources/icon-error.png");

        try {
            NLPUtils.NERTrain();
            NLPUtils.categorizerTrain();
        } catch (IOException e) {
            System.err.println("Error al entrenar, revisa los ficheros.");
            JOptionPane.showMessageDialog(this, "Error interno, revisa la consola.", "Error", JOptionPane.INFORMATION_MESSAGE, icon);
            System.exit(0);
        }

    }

}
