package domain;

import opennlp.tools.util.Span;
import util.ColorToRGB;
import util.NLPUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/** Accion llevada a cabo al presionar la tecla enter en la ventana de Chat */
public class TextKeyAdapter extends KeyAdapter {

    private JTextField txtUser;
    private JTextArea txt;
    private JVentanaLienzo Jlienzo;
    private JDisplayColores JColores;
    private Random rnd = new Random();

    /**
     * Constructor
     * @param txtUser Contiene el texto introducido por el usuario
     * @param txt JTextArea donde se escribira el mensaje del usuario y la respuesta del bot
     * @param lienzo Lienzo donde se pintara la figura
     */
    public TextKeyAdapter(JTextField txtUser, JTextArea txt, JVentanaLienzo lienzo) {
        this.txtUser = txtUser;
        this.txt = txt;
        this.Jlienzo = lienzo;
    }

    /**
     * Evento de deteccion de la tecla enter. Dependiendo de la categoria obtenida en el metodo categorizar() haremos
     * una cosa u otra.
     * @param e Evento
     */
    @Override
    public void keyPressed(KeyEvent e) {
        super.keyPressed(e);
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {

            if (!txtUser.getText().isEmpty()) {
                txt.append("\n♠ Usuario: " + txtUser.getText() + "\n");

                switch (NLPUtils.categorizar(txtUser.getText())) {
                    case "pintar" -> analizar(txtUser.getText());
                    case "saludo" -> txt.append("♦ " + JVentanaChat.Bot + ": " + RespuestasBot.saludo());
                    case "ayuda" -> txt.append(RespuestasBot.ayuda());
                    case "ayuda-figuras" -> txt.append(RespuestasBot.ayudaFiguras());
                    case "borrar" -> Jlienzo.borrarTodo(txt);
                    case "colores" -> {
                        if (JColores != null) {
                            if (JColores.isDisplayable()) {
                                txt.append("♦ " + JVentanaChat.Bot + ": " + "La ventana de colores ya se encuentra abierta.\n");
                            } else {
                                txt.append("♦ " + JVentanaChat.Bot + ": " + "Mostrando la lista de colores.\n");
                                JColores = new JDisplayColores("Lista de Colores");
                            }
                        } else {
                            txt.append("♦ " + JVentanaChat.Bot + ": " + "Mostrando la lista de colores.\n");
                            JColores = new JDisplayColores("Lista de Colores");
                        }

                    }
                    case "despedida" -> {
                        txt.append("♦ " + JVentanaChat.Bot + ": " + RespuestasBot.despedida());
                        System.exit(0);
                    }
                    default -> txt.append("♦ " + JVentanaChat.Bot + ": " + "Lo siento, no te he entendido.\n");
                }
            }

            txtUser.setText("");
        }
    }

    /**
     * Analiza la frase introducida. En concreto, aplica todos los métodos de la clase NLPUtils para obtener un hashmap
     * con cada palabra y su categoria. (P.Ej: azul - color).
     * Muestra informacion por consola de todo el proceso interno.
     * @param frase Frase a analizar, obtenida del chat de usuario.
     */
    private void analizar(String frase) {

        HashMap<String, String> nsLemmas = new HashMap<>();

        //Tokenizamos la frase
        String[] tokens = NLPUtils.Tokenizar(frase);

        //Sacamos los tags
        String[] tags = NLPUtils.PartOfSpeechTag(tokens);

        //Lemmatizamos
        String[] lemmas = NLPUtils.Lemmatizar(tokens, tags);

        Span[] names = NLPUtils.NamedEntityRecognition(lemmas);

        for (Span name : names) {

            StringBuilder intents = new StringBuilder();
            for (int i = name.getStart(); i < name.getEnd(); i++) {
                intents.append(lemmas[i]);
            }

            // Debug
            System.out.println("\n" + name.getType() + " : " + intents + "\t [probabilidad = " + name.getProb() + "]");

            if (name.getProb() > 0.9) { //Si la confianza no es mayor al 0.9 no metas la figura, probablemente se haya equivocado
                nsLemmas.put(name.getType(), intents.toString());
            }

        }

        checkPintar(nsLemmas);

    }

    /**
     * Aqui analizara cada palabra por separado junto a su categoria para obtener los detalles de la figura que va a ser
     * pintada. Invoca a la clase Lienzo para pintar la figura.
     * @param nsLemmas HashMap con cada palabra y su categoria obtenido en el metodo analizar().
     */
    private void checkPintar(HashMap<String, String> nsLemmas) {

        String figura;
        Color color = new Color(rnd.nextInt(255), rnd.nextInt(255), rnd.nextInt(255));
        int x;
        int y;
        int alto = 100;
        int largo = 100;
        boolean relleno;
        ArrayList<String> figuras = new ArrayList<>();

        figuras.add("cuadrado");
        figuras.add("circulo");
        figuras.add("triangulo");
        figuras.add("hexagono");
        figuras.add("triángulo");
        figuras.add("hexágono");
        //figura, color, x, y, lado, relleno


        if (nsLemmas.containsKey("figura")) {
            if (figuras.contains(nsLemmas.get("figura").toLowerCase()))
                figura = nsLemmas.get("figura").toLowerCase();
            else
                figura = figuras.get(rnd.nextInt(figuras.size()));
        } else {
            txt.append("♦ " + JVentanaChat.Bot + ": No he podido descifrar la figura que quieres pintar, procederé a pìntar una aleatoria.\n");
            figura = figuras.get(rnd.nextInt(figuras.size()));
        }

        if (nsLemmas.containsKey("color")) {
            try {
                color = ColorToRGB.convertir(nsLemmas.get("color").toLowerCase(), txt);
            } catch (Exception e) {
                e.printStackTrace();
                ImageIcon icon = new ImageIcon("resources/icon-file-error.png");
                JOptionPane.showMessageDialog(Jlienzo, "Error en el fichero colores.json", "Error", JOptionPane.INFORMATION_MESSAGE, icon);
            }

        } else {
            color = new Color(rnd.nextInt(255), rnd.nextInt(255), rnd.nextInt(255));
            txt.append("♦ " + JVentanaChat.Bot + ": No se ha detectado un color, procederé a elegir uno aleatorio.\n");
            nsLemmas.put("color", "R: " + color.getRed() + " G: " + color.getGreen() + " B: " + color.getBlue());
        }

        if (nsLemmas.containsKey("lado")) {
            alto = Integer.parseInt(nsLemmas.get("lado"));
            largo = Integer.parseInt(nsLemmas.get("lado"));
        } else {
            txt.append("♦ " + JVentanaChat.Bot + ": No se ha detectado un tamaño, procederé a elegir uno aleatorio.\n");
            alto = rnd.nextInt(100) + 10;
            largo = rnd.nextInt(100) + 10; //Por si justo sale 0
        }

        if (nsLemmas.containsKey("alto")) {
            alto = Integer.parseInt(nsLemmas.get("alto"));
        }

        if (nsLemmas.containsKey("largo")) {
            largo = Integer.parseInt(nsLemmas.get("largo"));
        }

        if (nsLemmas.containsKey("x")) {
            if (Integer.parseInt(nsLemmas.get("x")) < JVentanaChat.ancho) {
                x = Integer.parseInt(nsLemmas.get("x"));
            } else {
                txt.append("♦ " + JVentanaChat.Bot + ": La coordenada X se te salía del lienzo, te la he ajustado.\n");
                x = Jlienzo.getWidth() - largo;
            }
        } else {
            txt.append("♦ " + JVentanaChat.Bot + ": Pintaré tu figura en una X aleatoria.\n");
            x = rnd.nextInt(Jlienzo.getWidth() - largo);
        }

        if (nsLemmas.containsKey("y")) {
            if (Integer.parseInt(nsLemmas.get("y")) < JVentanaChat.largo) {
                y = Integer.parseInt(nsLemmas.get("y"));
            } else {
                txt.append("♦ " + JVentanaChat.Bot + ": La coordenada Y se te salía del lienzo, te la he ajustado.\n");
                y = Jlienzo.getHeight() - alto;
            }
        } else {
            txt.append("♦ " + JVentanaChat.Bot + ": Pintaré tu figura en una Y aleatoria.\n");
            y = rnd.nextInt(Jlienzo.getHeight() - alto);
        }

        if (nsLemmas.containsKey("relleno")) {
            if (nsLemmas.get("relleno").equals("sin")) {
                relleno = false;
            } else {
                relleno = true;
            }
        } else {
            relleno = rnd.nextBoolean();
            if (relleno) {
                nsLemmas.put("relleno", "con");
            } else {
                nsLemmas.put("relleno", "sin");
            }
        }

        Jlienzo.pintarFigura(figura, color, x, y, alto, largo, relleno, txt, nsLemmas.get("color"), nsLemmas.get("relleno").toLowerCase());

    }

}
