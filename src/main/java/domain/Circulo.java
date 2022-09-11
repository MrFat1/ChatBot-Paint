package domain;

import java.awt.*;
import java.awt.Color;

/**
 * Figura circulo
 */
public class Circulo extends Figura{

	private boolean relleno;
	private Color color;

	/**
	 * Inicializa todos los atributos del Circulo
	 * @param x Posicion x en el lienzo
	 * @param y Posicion Y en el lienzo
	 * @param alto Alto del Circulo
	 * @param largo Largo del circulo
	 * @param relleno Con o sin relleno
	 */

	Circulo(int x, int y, int alto, int largo, boolean relleno) {
		super(x, y, alto, largo);
		this.relleno = relleno;
	}

	public Circulo(int x, int y, int alto, int largo, boolean relleno, Color color) {

		this(x, y, alto, largo, relleno);
		this.color = color;

	}

	public String toString() {
		return super.toString() + " Relleno: " + relleno + " Color: " + color;
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
			g.drawOval(this.getX(), this.getY(), this.getLargo(), this.getAlto());

			if (relleno) {
				g.fillOval(this.getX(), this.getY(), this.getLargo(), this.getAlto());
			}

		}

	}
 

}