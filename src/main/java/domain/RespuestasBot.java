package domain;

import java.util.ArrayList;
import java.util.Random;

/** Clase para organizar las respuestas del bot */
public class RespuestasBot {

    private static Random rnd = new Random();

    /**
     * Contiene las diferentes respuestas para un saludo.
     * @return Devuelve una frase aleatoria entre varias
     */
    public static String saludo() {

        ArrayList<String> sal =  new ArrayList<>();

        sal.add("Hola, ¿Que quieres pintar hoy?\n");
        sal.add("Hola, ¿Que puedo pintar por ti?\n");
        sal.add("Buenas, si tienes alguna duda pregúntame.\n");
        sal.add("Buenas, puedes preguntarme sobre qué figuras puedo pintar.\n");
        sal.add("Hola, ¿En que puedo ayudarte?\n");

        return sal.get(rnd.nextInt(sal.size()));

    }

    /**
     * Contiene las diferentes respuestas para la ayuda principal.
     * @return Devuelve una frase
     */
    public static String ayuda() {

        StringBuilder sb = new StringBuilder();

        sb.append("♦ Puedes pintar figuras simplemente pidiéndolo en el chat.\n");
        sb.append("♦ Si tienes alguna duda sobre las figuras, pregunta por estas.\n");
        sb.append("♦ Para ver los colores disponibles pregunta por estos.\n");
        sb.append("♦ Para salir del programa, despídete.\n");

        return sb.toString();

    }

    /**
     * Contiene las diferentes respuestas para la despedida
     * @return Devuelve una frase aleatoria entre varias
     */
    public static String despedida() {
        ArrayList<String> desp =  new ArrayList<>();

        desp.add("Adios, espero verte pronto.");
        desp.add("Hasta luego");
        desp.add("Nos vemos");
        desp.add("Chao");

        return desp.get(rnd.nextInt(desp.size()));

    }

    /**
     * Contiene la ayuda sobre las figuras
     * @return Devuelve una frase
     */
    public static String ayudaFiguras() {

        return "♦ Actualmente puedes pintar cuadrados, circulos, triángulos y hexágonos de las dimensiones que quieras (alto y largo), además puedes especificar parámetros como el color, el relleno (con o sin) y las coordenadas donde quieres que sea pintada.\n";

    }
}
