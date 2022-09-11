package domain;

import java.awt.*;
import java.awt.Color;

/**
 * Clase padre de cada figura
 */
public abstract class Figura {

	private int coordeandaX;
	private int coordeandaY;
	private int alto;
	private int largo;
	private boolean visible;

	/**
	 * Inicializa los atributos de la clase padre Figura
	 * @param x Posicion X en el lienzo
	 * @param y Posicion Y en el lienzo
	 * @param a Alto de la figura
	 * @param l Largo de la figura
	 */

	Figura(int x, int y, int a, int l) {

		this.coordeandaX = x;
		this.coordeandaY = y;
		this.alto = a;
		this.largo = l;
		this.visible = true;

	}

	public int getX() {
		return coordeandaX;
	}

	public int getY() {
		return coordeandaY;
	}

	public int getAlto() {
		return alto;
	}

	public int getLargo() {
		return largo;
	}

	public void setX(int x) {
		this.coordeandaX = x;
	}

	public void setY(int y) {
		this.coordeandaY = y;
	}

	public void setAlto(int alto) {
		this.alto = alto;
	}

	public void setLargo(int largo) {
		this.largo = largo;
	}

	public abstract	Color getColor();

	public abstract boolean getRelleno();  //Polimorfismo

	public abstract	void pintar(Graphics g);

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public String toString() {
		return " Coordenada X: " + coordeandaX + " Coordenada Y: " + coordeandaY + " Alto: " + alto + " Largo " + largo;
	}


}