package co.edu.uptc.view.componentes;

import co.edu.uptc.view.Tema;

import javax.swing.BorderFactory;
import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.JTextComponent;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;


public class CampoRedondeado extends JTextField {

    private final String pista;

    public CampoRedondeado(String pista, int maxCaracteres) {
        this.pista = pista;
        setOpaque(false);
        setFont(Tema.fuente(Font.PLAIN, 18));
        setForeground(Tema.CAMPO_TEXTO);
        setCaretColor(Tema.CAMPO_TEXTO);
        setBorder(BorderFactory.createEmptyBorder(6, 14, 6, 14));
        limitar(this, maxCaracteres);
    }

    /* limite de caracteres */
    static void limitar(JTextComponent componente, int max) {
        ((AbstractDocument) componente.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String texto, AttributeSet a) throws BadLocationException {
                replace(fb, offset, 0, texto, a);
            }

            @Override
            public void replace(FilterBypass fb, int offset, int largo, String texto, AttributeSet a) throws BadLocationException {
                if (texto == null) {
                    texto = "";
                }
                int actual = fb.getDocument().getLength();
                int permitido = max - (actual - largo);
                if (permitido <= 0) {
                    return;
                }
                if (texto.length() > permitido) {
                    texto = texto.substring(0, permitido);
                }
                super.replace(fb, offset, largo, texto, a);
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g0) {
        Graphics2D g = (Graphics2D) g0.create();
        Tema.calidad(g);
        g.setColor(Tema.CAMPO);
        g.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
        g.dispose();
        super.paintComponent(g0);
        if (pista != null && getText().isEmpty() && !isFocusOwner()) {
            Graphics2D h = (Graphics2D) g0.create();
            Tema.calidad(h);
            h.setFont(getFont());
            h.setColor(new Color(8, 32, 58, 130));
            FontMetrics fm = h.getFontMetrics();
            h.drawString(pista, getInsets().left, (getHeight() - fm.getHeight()) / 2 + fm.getAscent());
            h.dispose();
        }
    }
}
