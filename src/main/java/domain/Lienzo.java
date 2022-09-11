package domain;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Elemento grafico principal donde iran todas las figuras pintadas.
 */
public class Lienzo extends Canvas
{
	private HashMap<Figura, String> figuras =  new HashMap<>();

	/**
	 * Clase para anadir figuras a un HashMap que gestionara todas las figuras.
	 * @param f Figura a añadir
	 * @param grupo Grupo de la figura
	 */
	public void pintar(Figura f, String grupo)
	{
		figuras.put(f, grupo);
	}

	/**
	 * Metodo de la clase canvas para pintar todas las figuras del HashMap.
	 * @param g Parámetro del Graphics
	 */
	public void paint(Graphics g)
	{
		figuras.forEach((fig,grupo) -> fig.pintar(g));
	}

	/**
	 * Metodo para vaciar el lienzo.
	 */
	public void vaciar() {
		figuras.clear();
		this.repaint();
	}
}