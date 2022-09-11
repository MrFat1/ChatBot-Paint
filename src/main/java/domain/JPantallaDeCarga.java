package domain;

import javax.swing.*;

/** Ventana que inicia el programa junto con la animacion del panel "CargandoLienzo" */
public class JPantallaDeCarga extends JFrame{

    private static int ancho = 800;
    private static int alto = 600;

    public JPantallaDeCarga() {

        CargandoLienzo carga = new CargandoLienzo();

        this.add(carga);

        this.setUndecorated(true);
        this.setAlwaysOnTop(true);

        Thread pantallaCarga = new Thread(() -> {
            this.carga();
        });
        pantallaCarga.start();

        this.setResizable(true);
        this.setSize(ancho,alto);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    /** Inicia el Thread que retendrá el programa hasta que fialize la animacion */
    private void carga() {

        try {
            Thread.sleep(2800);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        this.dispose();
        new JVentanaLienzo("AI-Paint");
    }

}
