package domain;

import javax.swing.*;
import java.awt.*;

/**
 * Ventana que contiene el Chat donde el usuario interactua con el bot
 */
public class JVentanaChat extends JFrame {

    private JTextField txtUserChat;
    private JTextArea txt;
    public static final String Bot = "Lucy";
    public static int largo = 760;
    public static int ancho = 600;

    /**
     * Metodo que inicia la ventana
     * @param titulo Titulo de la ventana
     * @param Jlienzo Ventana que contendra el lienzo. (Lo llamamos aqui para obtener sus dimensiones)
     */
    public JVentanaChat(String titulo, JVentanaLienzo Jlienzo) {
        super(titulo);

        Font font = new Font("Arial", Font.PLAIN, 12);
        JButton btnSend = new JButton("Enviar");
        //JLabel label = new JLabel("Hola! Soy " + JVentanaChat.Bot + ", un chatbot que permite dibujar figuras. ¿Que figura quieres pintar?");
        txt = new JTextArea(47,49);
        txtUserChat =  new JTextField(txt.getColumns() - 5);
        JScrollPane jsp = new JScrollPane(txt);

        JPanel pnlSouth = new JPanel();
        JPanel pnlCentro = new JPanel();
        JPanel pnlNorte = new JPanel();

        btnSend.setFont(font);
        txtUserChat.setFont(font);
        jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        txt.setFont(font);
        txt.setEditable(false);
        txt.setLineWrap(true);

        txtUserChat.addKeyListener(new TextKeyAdapter(txtUserChat, txt, Jlienzo));

        pnlSouth.add(txtUserChat);
        pnlSouth.add(btnSend);
        pnlCentro.add(jsp);
        //pnlNorte.add(label);

        this.setResizable(false);
        this.add(pnlSouth, BorderLayout.SOUTH);
        this.add(pnlCentro, BorderLayout.CENTER);
        this.add(pnlNorte, BorderLayout.NORTH);
        this.setLocation(Jlienzo.getX() + Jlienzo.getWidth(), Jlienzo.getY());
        this.setSize(JVentanaChat.ancho,JVentanaChat.largo);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

    }


}
