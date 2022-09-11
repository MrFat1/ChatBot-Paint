package domain;

import java.awt.*;
import java.awt.Color;

/**
 * Figura Cuadrado
 */
public class Cuadrado extends Figura{

	private boolean relleno;
	private Color color;

	/**
	 * Inicializa todos los atributos del Cuadrado
	 * @param coordeandaX Posicion x en el lienzo
	 * @param coordeandaY Posicion Y en el lienzo
	 * @param alto Alto del Circulo
	 * @param largo Largo del circulo
	 * @param relleno Con o sin relleno
	 */

	Cuadrado(int coordeandaX, int coordeandaY, int alto, int largo, boolean relleno) {  //Si no se introduce color, poner el color default (gris)
		super(coordeandaX, coordeandaY, alto, largo);
		this.relleno = relleno;
	}

	public Cuadrado(int coordeandaX, int coordeandaY, int alto, int largo, boolean relleno, Color color) {

		this(coordeandaX, coordeandaY, alto, largo, relleno);
		this.color = color;

	}

	public boolean getRelleno() {
		return this.relleno;
	}

	public void setRelleno(boolean relleno) {
		this.relleno = relleno;
	}


	public String toString() {
		return super.toString() + " Relleno: " + relleno + " Color: " + color;
	}

	public Color getColor() {
		return this.color;
	}

	public void pintar(Graphics g) {  //El cuadrado se pinta a si mismo (Mira a ver si tiene el get, y si no lo tiene va al padre)

		if (isVisible()) {

			g.setColor(this.getColor());
			g.drawRect(this.getX(), this.getY(), this.getLargo(), this.getAlto());

			if (relleno) {
				g.fillRect(this.getX(), this.getY(), this.getLargo(), this.getAlto());
			}

		}

	}



}