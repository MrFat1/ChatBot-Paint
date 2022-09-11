package domain;

import javax.swing.*;
import java.awt.*;

/**
 * Ventana que contiene el lienzo donde se pintarán las figuras
 */
public class JVentanaLienzo extends JFrame {

    private Lienzo lienzo;
    private static int ancho = 900;
    private static int alto = 700;


    public JVentanaLienzo(String titulo) {
        super(titulo);

        lienzo = new Lienzo();

        this.add(lienzo, BorderLayout.CENTER);

        this.pack();
        this.setResizable(true);
        this.setSize(ancho, alto);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocation(200,200);
        this.setVisible(true);

        new JVentanaChat("Bot-Chat", this);

    }

    /**
     * Este metodo sirve para pintar las figuras en el lienzo contenido en esta ventana.
     * @param figura Tipo de figura
     * @param color Color de la figura
     * @param x Coordenada x
     * @param y Coordenada Y
     * @param alto Alto
     * @param largo Largo
     * @param relleno true/false
     * @param txt Area de texto para el mensaje del bot
     * @param strColor String del color
     * @param strRelleno String del relleno (con o sin)
     */
    public void pintarFigura(String figura, Color color, int x, int y, int alto, int largo, boolean relleno, JTextArea txt, String strColor, String strRelleno) {

        String botMsg = "♦ " + JVentanaChat.Bot + ": Pintando un " + figura + " de color " + strColor +
                " en " + x + " " + y + " de alto " + alto + ", largo " + largo + " y " + strRelleno + " relleno.\n";

        txt.append(botMsg);

        switch (figura) {
            case "cuadrado" -> {
                Cuadrado cuad = new Cuadrado(x, y, alto, largo, relleno, color);
                lienzo.pintar(cuad, "Figura");
            }
            case "circulo" -> {
                Circulo circ = new Circulo(x, y, alto, largo, relleno, color);
                lienzo.pintar(circ, "Figura");
            }
            case "triángulo", "triangulo" -> {
                Triangulo trig = new Triangulo(x, y, alto, largo, relleno, color);
                lienzo.pintar(trig, "Figura");
            }
            case "hexagono", "hexágono" -> {
                Hexagono hex = new Hexagono(x,y,alto,largo, relleno,color);
                lienzo.pintar(hex, "Figura");
            }
        }

        lienzo.repaint();

    }


    /**
     * Método para vaciar el lienzo y mandar un mensaje del bot
     * @param txt Para mandar el mensaje del bot
     */
    public void borrarTodo(JTextArea txt) {

        int opc = JOptionPane.showConfirmDialog(this, "¿Seguro que quieres borrar todo? Se perderá para siempre (mucho tiempo)",
                "Confirmación de borrado", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (opc == JOptionPane.YES_OPTION) {
            lienzo.vaciar();
            txt.append("♦ " + JVentanaChat.Bot + ": " + "Se han borrado todas las figuras.\n");
        }

    }
}
