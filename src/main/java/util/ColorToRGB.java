package util;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import domain.JVentanaChat;

import javax.swing.*;
import java.awt.*;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

/**
 * Clase para obtener el RGB de un color a partir de su nombre
 */
public class ColorToRGB {

    private static JSONParser parser = new JSONParser();
    private static Random rnd = new Random();
    public static JSONArray colores;

    /**
     * Utilizamos la libreria JSON-Simple para facilitar el manejo del fichero.
     * @return Devuelve un array de colores de tipo JSONArray
     * @throws IOException
     * @throws ParseException
     */
    public static JSONArray getListaColores() throws IOException, ParseException {
        colores = (JSONArray) parser.parse(new FileReader("resources/colores.json"));

        return colores;

    }

    /**
     * Para obtener el RGB de cada color
     * @param color Nombre del color a buscar
     * @param txt Para los mensajes del bot
     * @return Devuelve el color encontrado o uno aleatorio si no ha podido identificarlo.
     * @throws IOException
     * @throws ParseException
     */
    public static Color convertir(String color, JTextArea txt) throws IOException, ParseException {

        colores = getListaColores();

        for (Object c : colores)
        {
            JSONObject col = (JSONObject) c;

            String nombreColor = (String) col.get("color");

            if (nombreColor.toLowerCase().equals(color)) {
                String rgb = (String) col.get("rgb");
                String[] rgbs = rgb.split(",");

                return new Color(Integer.parseInt(rgbs[0]), Integer.parseInt(rgbs[1]), Integer.parseInt(rgbs[2]));
            }
        }

        txt.append("♦ " + JVentanaChat.Bot + ": El color seleccionado no se encuentra en mi base de datos, procederé a pintar de un color aleatorio.\n");
        return new Color(rnd.nextInt(255), rnd.nextInt(255), rnd.nextInt(255));

    }

}
