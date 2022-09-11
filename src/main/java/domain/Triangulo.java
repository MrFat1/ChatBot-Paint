package domain;

import java.awt.*;
import java.awt.Color;

/**
 * Figura triangulo
 */
public class Triangulo extends Figura {

	private boolean relleno;
	private Color color;

	/**
	 * Inicializa todos los atributos del Triángulo
	 * @param x Posición x en el lienzo
	 * @param y Posición Y en el lienzo
	 * @param alto Alto del Triángulo
	 * @param largo Largo del Triángulo
	 * @param relleno (true/false)
	 * @param color Color del Triángulo
	 */

	public Triangulo(int x, int y, int alto, int largo, boolean relleno, Color color) {

		super(x, y, alto, largo);
		this.relleno = relleno;
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

		int[] xs = {this.getX(), this.getX() + this.getAlto(), this.getX()};
		int[] ys = {this.getY(), this.getY(), this.getY() + this.getLargo()};

		if (isVisible()) {

			g.setColor(color);
			g.drawPolygon(xs, ys, xs.length);

			if (relleno) {
				g.fillPolygon(xs, ys, xs.length);
			}

		}
	}

}