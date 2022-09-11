package domain;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import util.ColorToRGB;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/** Ventana nueva que muestra una lista con todos los colores. Para verla pideselo al bot ;)
 * No interfiere en el programa principal.
 * */
public class JDisplayColores extends JFrame {

    private JSONArray colores;

    public JDisplayColores(String titulo) {
        super(titulo);

        JPanel pnlColores = new JPanel(new GridLayout(14, 18));

        try {
            colores = ColorToRGB.getListaColores();
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            ImageIcon icon = new ImageIcon("resources/icon-file-error.png");
            JOptionPane.showMessageDialog(this, "Error en el fichero colores.json", "Error", JOptionPane.INFORMATION_MESSAGE, icon);
        }


        for (Object c : colores)
        {
            JSONObject col = (JSONObject) c;

            String nombreColor = (String) col.get("color");

            String rgb = (String) col.get("rgb");
            String[] rgbs = rgb.split(",");

            Color color = new Color(Integer.parseInt(rgbs[0]), Integer.parseInt(rgbs[1]), Integer.parseInt(rgbs[2]));

            JLabel lbColor = new JLabel(nombreColor, SwingConstants.CENTER);
            lbColor.setBackground(color);
            lbColor.setForeground(Color.BLACK);
            lbColor.setOpaque(true);

            pnlColores.add(lbColor);

        }

        this.add(pnlColores);
        this.setSize(1200, 700);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setVisible(true);

    }

}
