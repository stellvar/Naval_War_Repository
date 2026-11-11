package co.edu.uptc.view.componentes;

import co.edu.uptc.view.Tema;

import javax.swing.*;
import java.awt.*;


public class AreaRedondeada extends JPanel {

    private final JTextArea area = new JTextArea();

    public AreaRedondeada(int maxCaracteres) {
        super(new BorderLayout());
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        area.setOpaque(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setFont(Tema.fuente(Font.PLAIN, 16));
        area.setForeground(Tema.CAMPO_TEXTO);
        area.setCaretColor(Tema.CAMPO_TEXTO);
        CampoRedondeado.limitar(area, maxCaracteres);
        add(area, BorderLayout.CENTER);
    }

    public String getTexto() {
        return area.getText();
    }

    public void setTexto(String texto) {
        area.setText(texto);
        area.setCaretPosition(0);
    }

    @Override
    protected void paintComponent(Graphics g0) {
        Graphics2D g = (Graphics2D) g0.create();
        Tema.calidad(g);
        g.setColor(Tema.CAMPO);
        g.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
        g.dispose();
        super.paintComponent(g0);
    }
}
