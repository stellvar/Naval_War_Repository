package co.edu.uptc.view.componentes;

import co.edu.uptc.view.Tema;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

/*
 * tablero 10 x 10. Se reutiliza en "TU FLOTA" (muestra los barcos)
 * Dibuja un TableroVista
 */
public class TableroPanel extends JComponent {


    public interface OyenteCasilla {
        void alSeleccionar(int fila, int columna);
    }

    private static final int N = TableroVista.N;
    private static final Color BARCO = new Color(0xF3F7FB);
    private static final Color CONTORNO = new Color(0x07142E);

    private final EstiloFlota estilo;
    private TableroVista tablero;
    private boolean verBarcos;
    private boolean interactivo;
    private OyenteCasilla oyente;
    private int hoverFila = -1;
    private int hoverColumna = -1;
    private boolean colocable;
    private TableroVista.Nave arrastrada;
    private int desplazaFila;
    private int desplazaColumna;
    private int destinoFila;
    private int destinoColumna;
    private boolean destinoValido;

    public TableroPanel(EstiloFlota estilo) {
        this.estilo = estilo;
        setOpaque(false);
        setPreferredSize(new Dimension(440, 340));
        setMinimumSize(new Dimension(220, 180));
        MouseAdapter raton = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (colocable && tablero != null) {
                    empezarArrastre(e);
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (arrastrada != null) {
                    arrastrar(e.getPoint());
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                actualizarHover(e.getPoint());
                if (colocable && tablero != null) {
                    int[] celda = celdaEn(e.getPoint());
                    boolean sobreBarco = celda != null && tablero.getNaveEn(celda[0], celda[1]) != null;
                    setCursor(sobreBarco ? Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR) : Cursor.getDefaultCursor());
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hoverFila = -1;
                hoverColumna = -1;
                setCursor(Cursor.getDefaultCursor());
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (arrastrada != null) {
                    soltar();
                    return;
                }
                int[] celda = celdaEn(e.getPoint());
                if (interactivo && celda != null && oyente != null) {
                    oyente.alSeleccionar(celda[0], celda[1]);
                }
            }
        };
        addMouseListener(raton);
        addMouseMotionListener(raton);
    }

    public void setTablero(TableroVista tablero, boolean verBarcos) {
        this.tablero = tablero;
        this.verBarcos = verBarcos;
        repaint();
    }

    public void setInteractivo(boolean interactivo) {
        this.interactivo = interactivo;
        if (!interactivo) {
            hoverFila = -1;
            hoverColumna = -1;
            setCursor(Cursor.getDefaultCursor());
        }
        repaint();
    }

    public void setOyente(OyenteCasilla oyente) {
        this.oyente = oyente;
    }

    /* permite arrastrar los barcos para posicionarlos (clic derecho para girarlos) */
    public void setColocable(boolean colocable) {
        this.colocable = colocable;
        if (!colocable) {
            arrastrada = null;
            setCursor(Cursor.getDefaultCursor());
        }
        repaint();
    }

    private void empezarArrastre(MouseEvent e) {
        int[] celda = celdaEn(e.getPoint());
        if (celda == null) {
            return;
        }
        TableroVista.Nave nave = tablero.getNaveEn(celda[0], celda[1]);
        if (nave == null) {
            return;
        }
        if (SwingUtilities.isRightMouseButton(e)) {
            tablero.rotar(nave);
            repaint();
        } else if (SwingUtilities.isLeftMouseButton(e)) {
            arrastrada = nave;
            desplazaFila = celda[0] - nave.getFila();
            desplazaColumna = celda[1] - nave.getColumna();
            destinoFila = nave.getFila();
            destinoColumna = nave.getColumna();
            destinoValido = true;
            setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
            repaint();
        }
    }

    private void arrastrar(Point p) {
        Rectangle2D r = rejilla();
        int columna = (int) Math.floor((p.x - r.getX()) / (r.getWidth() / N)) - desplazaColumna;
        int fila = (int) Math.floor((p.y - r.getY()) / (r.getHeight() / N)) - desplazaFila;
        if (fila != destinoFila || columna != destinoColumna) {
            destinoFila = fila;
            destinoColumna = columna;
            destinoValido = tablero.puedeColocar(arrastrada, fila, columna, arrastrada.isHorizontal());
            repaint();
        }
    }

    private void soltar() {
        if (destinoValido) {
            tablero.colocar(arrastrada, destinoFila, destinoColumna, arrastrada.isHorizontal());
        }
        arrastrada = null;
        setCursor(Cursor.getDefaultCursor());
        repaint();
    }

    private Rectangle2D rejilla() {
        double izquierda = Math.max(22, Math.min(40, getWidth() * 0.09));
        double arriba = Math.max(22, Math.min(36, getHeight() * 0.11));
        double derecha = 14;
        double abajo = 14;
        return new Rectangle2D.Double(izquierda, arriba, getWidth() - izquierda - derecha, getHeight() - arriba - abajo);
    }

    private int[] celdaEn(Point p) {
        Rectangle2D r = rejilla();
        if (!r.contains(p)) {
            return null;
        }
        int columna = (int) ((p.x - r.getX()) / (r.getWidth() / N));
        int fila = (int) ((p.y - r.getY()) / (r.getHeight() / N));
        if (fila < 0 || fila >= N || columna < 0 || columna >= N) {
            return null;
        }
        return new int[]{fila, columna};
    }

    private void actualizarHover(Point p) {
        int[] celda = celdaEn(p);
        int fila = celda == null ? -1 : celda[0];
        int columna = celda == null ? -1 : celda[1];
        boolean valida = interactivo && celda != null && tablero != null
                && tablero.getDisparo(fila, columna) == TableroVista.SIN_DISPARAR;
        if (!valida) {
            fila = -1;
            columna = -1;
        }
        if (fila != hoverFila || columna != hoverColumna) {
            hoverFila = fila;
            hoverColumna = columna;
            setCursor(valida ? Cursor.getPredefinedCursor(Cursor.HAND_CURSOR) : Cursor.getDefaultCursor());
            repaint();
        }
    }

    @Override
    protected void paintComponent(Graphics g0) {
        Graphics2D g = (Graphics2D) g0.create();
        Tema.calidad(g);
        int w = getWidth();
        int h = getHeight();

        RoundRectangle2D marco = new RoundRectangle2D.Double(1.5, 1.5, w - 3, h - 3, 22, 22);
        g.setColor(estilo.fondo);
        g.fill(marco);
        g.setColor(estilo.borde);
        g.setStroke(new BasicStroke(3f));
        g.draw(marco);

        Rectangle2D r = rejilla();
        double cw = r.getWidth() / N;
        double ch = r.getHeight() / N;

        // encabezados
        g.setFont(Tema.fuente(Font.BOLD, (float) Math.max(11, Math.min(20, Math.min(cw, ch) * 0.55))));
        g.setColor(Tema.TEXTO);
        FontMetrics fm = g.getFontMetrics();
        for (int i = 0; i < N; i++) {
            String letra = String.valueOf((char) ('A' + i));
            g.drawString(letra, (float) (r.getX() + i * cw + (cw - fm.stringWidth(letra)) / 2), (float) (r.getY() - 8));
            String numero = String.valueOf(i + 1);
            g.drawString(numero, (float) (r.getX() - 8 - fm.stringWidth(numero)),
                    (float) (r.getY() + i * ch + (ch + fm.getAscent() - fm.getDescent()) / 2));
        }

        // rejilla
        g.setStroke(new BasicStroke(1f));
        g.setColor(new Color(255, 255, 255, 26));
        for (int i = 1; i < N; i++) {
            g.draw(new Line2D.Double(r.getX() + i * cw, r.getY(), r.getX() + i * cw, r.getMaxY()));
            g.draw(new Line2D.Double(r.getX(), r.getY() + i * ch, r.getMaxX(), r.getY() + i * ch));
        }
        g.setColor(new Color(255, 255, 255, 120));
        g.draw(r);

        if (tablero != null) {
            for (TableroVista.Nave barco : tablero.getNaves()) {
                if (barco != arrastrada && (verBarcos || barco.estaHundido())) {
                    dibujarBarco(g, barco, r, cw, ch);
                }
            }
            if (arrastrada != null) {
                dibujarFantasma(g, r, cw, ch);
            }
            for (int f = 0; f < N; f++) {
                for (int c = 0; c < N; c++) {
                    int disparo = tablero.getDisparo(f, c);
                    double x = r.getX() + c * cw;
                    double y = r.getY() + f * ch;
                    if (disparo == TableroVista.AGUA) {
                        dibujarAgua(g, x, y, cw, ch);
                    } else if (disparo == TableroVista.TOCADO) {
                        TableroVista.Nave barco = tablero.getNaveEn(f, c);
                        boolean cubierto = verBarcos || (barco != null && barco.estaHundido());
                        if (!cubierto) {
                            dibujarFicha(g, x, y, cw, ch);
                        }
                        dibujarEquis(g, x, y, cw, ch);
                    }
                }
            }
        }

        if (hoverFila >= 0) {
            double x = r.getX() + hoverColumna * cw;
            double y = r.getY() + hoverFila * ch;
            Shape casilla = new Rectangle2D.Double(x + 1, y + 1, cw - 2, ch - 2);
            g.setColor(new Color(255, 255, 255, 48));
            g.fill(casilla);
            g.setColor(Tema.CIAN);
            g.setStroke(new BasicStroke(2f));
            g.draw(casilla);
        }
        g.dispose();
    }

    private void dibujarBarco(Graphics2D g, TableroVista.Nave barco, Rectangle2D r, double cw, double ch) {
        double margen = Math.min(cw, ch) * 0.14;
        double x = r.getX() + barco.getColumna() * cw + margen;
        double y = r.getY() + barco.getFila() * ch + margen;
        double ancho = (barco.isHorizontal() ? barco.getTamano() * cw : cw) - 2 * margen;
        double alto = (barco.isHorizontal() ? ch : barco.getTamano() * ch) - 2 * margen;
        double arco = Math.min(ancho, alto);
        RoundRectangle2D capsula = new RoundRectangle2D.Double(x, y, ancho, alto, arco, arco);
        g.setColor(BARCO);
        g.fill(capsula);
        g.setColor(CONTORNO);
        g.setStroke(new BasicStroke(2f));
        g.draw(capsula);
    }

    private void dibujarFantasma(Graphics2D g, Rectangle2D r, double cw, double ch) {
        double margen = Math.min(cw, ch) * 0.14;
        boolean horizontal = arrastrada.isHorizontal();
        double x = r.getX() + destinoColumna * cw + margen;
        double y = r.getY() + destinoFila * ch + margen;
        double ancho = (horizontal ? arrastrada.getTamano() * cw : cw) - 2 * margen;
        double alto = (horizontal ? ch : arrastrada.getTamano() * ch) - 2 * margen;
        double arco = Math.min(ancho, alto);
        RoundRectangle2D capsula = new RoundRectangle2D.Double(x, y, ancho, alto, arco, arco);
        g.setColor(destinoValido ? new Color(243, 247, 251, 200) : new Color(220, 60, 60, 170));
        g.fill(capsula);
        g.setColor(destinoValido ? CONTORNO : Tema.IMPACTO);
        g.setStroke(new BasicStroke(2f));
        g.draw(capsula);
    }

    private void dibujarFicha(Graphics2D g, double x, double y, double cw, double ch) {
        double margen = Math.min(cw, ch) * 0.14;
        RoundRectangle2D ficha = new RoundRectangle2D.Double(x + margen, y + margen, cw - 2 * margen, ch - 2 * margen,
                Math.min(cw, ch) * 0.5, Math.min(cw, ch) * 0.5);
        g.setColor(BARCO);
        g.fill(ficha);
        g.setColor(CONTORNO);
        g.setStroke(new BasicStroke(2f));
        g.draw(ficha);
    }

    private void dibujarEquis(Graphics2D g, double x, double y, double cw, double ch) {
        double media = Math.min(cw, ch) * 0.23;
        double cx = x + cw / 2;
        double cy = y + ch / 2;
        g.setColor(Tema.IMPACTO);
        g.setStroke(new BasicStroke((float) Math.max(2.4, Math.min(cw, ch) * 0.10), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.draw(new Line2D.Double(cx - media, cy - media, cx + media, cy + media));
        g.draw(new Line2D.Double(cx - media, cy + media, cx + media, cy - media));
    }

    private void dibujarAgua(Graphics2D g, double x, double y, double cw, double ch) {
        double d = Math.min(cw, ch) * 0.36;
        Ellipse2D punto = new Ellipse2D.Double(x + (cw - d) / 2, y + (ch - d) / 2, d, d);
        g.setColor(new Color(255, 255, 255, 70));
        g.fill(punto);
        g.setColor(new Color(255, 255, 255, 210));
        g.setStroke(new BasicStroke(2f));
        g.draw(punto);
    }
}
