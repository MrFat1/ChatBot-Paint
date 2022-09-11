package domain;

import java.awt.*;

/**
 * Figura Hexágono
 */
public class Hexagono extends Figura{

    private boolean relleno;
    private Color color;

    /**
     * Inicializa los atributos del Hexágono
     * @param x Coordenada X
     * @param y Coordenada Y
     * @param a Alto
     * @param l Largo
     * @param relleno Relleno (true / false)
     * @param color Color del hexagono
     */

    public Hexagono(int x, int y, int a, int l, boolean relleno, Color color) {
        super(x, y, a, l);
        this.relleno = relleno;
        this.color = color;
    }


    public Color getColor() {
        return this.color;
    }


    public boolean getRelleno() {
        return this.relleno;
    }


    public void pintar(Graphics g) {
        if(isVisible()) {
            g.setColor(this.getColor());
            Polygon p = new Polygon();
            for (int i = 0; i < 6; i++)
                p.addPoint((int) (this.getAlto() + this.getLargo() * Math.cos(i * 2 * Math.PI / 6)),
                        (int) (this.getAlto() + this.getLargo() * Math.sin(i * 2 * Math.PI / 6)));
            g.drawPolygon(p);

            if (relleno) {
                g.fillPolygon(p);
            }

        }
    }
}
