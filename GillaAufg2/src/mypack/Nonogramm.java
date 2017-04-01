package mypack;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

/**
 * Klasse zur Abbildung der grafischen Oberflaeche des Spiels.
 * 
 * @author gillaj
 *
 */
public class Nonogramm extends JFrame {

	private static final long serialVersionUID = 1L;
	private static int groessenFaktor = 100;

	private Modell modell;
	private JButton[][] buttonFeld;

	/**
	 * Konstruktor zum Erstellen eines beliebigen Spiels.
	 * 
	 * @param groesse
	 *            Groesse des Spielfelds
	 */
	public Nonogramm(int groesse) {
		super("Nonogramm - Jan Gilla - awis TZ - SoSe 2017 - HS Mainz");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.setSize((groessenFaktor * (groesse + 1)), ((groessenFaktor * (groesse + 1)) + 50));

		this.modell = new Modell(groesse);
		this.buttonFeld = new JButton[groesse][groesse];

		if (groesse == 4)
			this.modell.beispielLaden();

		Container fensterInhalt = this.getContentPane();

		/**
		 * Erezugung des Spielfelds
		 */
		JPanel spielFeld = new JPanel();
		spielFeld.setLayout(new GridLayout((groesse + 1), (groesse + 1), 0, 0));

		for (int x = 0; x < (groesse + 1); x++) {
			for (int y = 0; y < (groesse + 1); y++) {
				if (x == 0 || y == 0) {
					/**
					 * Ueberschriften
					 */
					JLabel label = new JLabel("Text", JLabel.CENTER);
					label.setBorder(LineBorder.createBlackLineBorder());
					label.setFont(new Font(null, Font.BOLD, 20));

					/**
					 * Unterscheidung in Eckfeld Links Oben,
					 * Spaltenueberschriften und Zeilenueberschriften
					 */
					if (x == 0 && y == 0) {
						label.setText("");
					} else if (x == 0) {
						/**
						 * Spaltenfeld generieren
						 */
						label.setText(this.modell.getTitelFeld(0, (y - 1)));
					} else if (y == 0) {
						/**
						 * Zeilenfeld generieren
						 */
						label.setText(this.modell.getTitelFeld(1, (x - 1)));
					}

					spielFeld.add(label);
				} else {
					/**
					 * Spielfeld
					 */
					buttonFeld[x - 1][y - 1] = new JButton();
					buttonFeld[x - 1][y - 1].setBackground(Color.WHITE);
					buttonFeld[x - 1][y - 1].setName((x - 1) + ":" + (y - 1));
					buttonFeld[x - 1][y - 1].addMouseListener(new spielfeldButtonListener());
					spielFeld.add(buttonFeld[x - 1][y - 1]);
				}
			}
		}
		fensterInhalt.add((Component) spielFeld, "Center");

		/**
		 * Erezugung des "Menus"
		 */
		JPanel menue = new JPanel();
		menue.setLayout(new BorderLayout());

		JButton checkButton = new JButton("Pruefe Loesung");
		menue.add((Component) checkButton, "East");

		/**
		 * Button zum Erstellen einer neuen Runde
		 */
		JButton neuButton = new JButton("Neues Spiel");
		neuButton.addActionListener(e -> {
			String rueckgabe = JOptionPane.showInputDialog(this, "Bitte Groesse des Quadrats eingeben: ");
			if (rueckgabe != null) {
				try {
					int r = Integer.parseInt(rueckgabe);
					this.dispose();
					new Nonogramm(r);
				} catch (Exception e2) {
					System.err.println("Fehler beim Umwandeln der Eingabe \"" + rueckgabe + "\" in eine Ganzzahl.");
				}
			}
		});
		menue.add((Component) neuButton, "West");

		fensterInhalt.add((Component) menue, "South");

		/**
		 * Groessenveraenderung des Felds durch den ComponentAdapter erkennen.
		 * Quellen:
		 * http://stackoverflow.com/questions/2303305/window-resize-event,
		 * http://docs.oracle.com/javase/6/docs/api/java/awt/event/ComponentAdapter.html
		 */
		this.addComponentListener(new spielfeldComponentAdapter());
		this.setVisible(true);
	}

	/**
	 * Konstruktor zum Erstellen des vorgegebenen Beispiels.
	 */
	public Nonogramm() {
		this(4);
	}

	public static void main(String[] args) {
		new Nonogramm();
	}

	/**
	 * Klasse zur Implementierung des ueberschriebenen MouseListeners.
	 * 
	 * @author gillaj
	 *
	 */
	private class spielfeldButtonListener extends MouseAdapter {

		/**
		 * Methode, die beim Klick auf einen Button aufgerufen wird.
		 * 
		 * @param e:
		 *            Uebergebene Eventinformationen
		 */
		public void mouseClicked(MouseEvent e) {
			JButton button = (JButton) e.getComponent();
			Modell m = Nonogramm.this.modell;
			try {
				String[] koord = button.getName().split(":");

				int x = Integer.parseInt(koord[0]);
				int y = Integer.parseInt(koord[1]);

				switch (e.getButton()) {
				/**
				 * Linke Maustaste
				 */
				case 1:
					/**
					 * Schwarz und Wei� umschalten. Kreuz wird ebenfalls zu
					 * Schwarz.
					 */
					if (m.getStatus(x, y) == Status.SCHWARZ) {
						m.setStatus(x, y, Status.LEER);
						button.setBackground(Color.WHITE);
						button.setText("");
					} else {
						m.setStatus(x, y, Status.SCHWARZ);
						button.setBackground(Color.BLACK);
						button.setText("");
					}
					break;
				/**
				 * Rechte Maustaste
				 */
				case 3:
					/**
					 * Kreuz und Leer umschalten.
					 */
					if (m.getStatus(x, y) == Status.KREUZ) {
						m.setStatus(x, y, Status.LEER);
						button.setBackground(Color.WHITE);
						button.setText("");
					} else {
						m.setStatus(x, y, Status.KREUZ);
						button.setBackground(Color.WHITE);
						button.setText("X");
					}
					break;
				}
			} catch (Exception e2) {
				System.err.println("Exeption beim Auslesen der x und y Koordinaten des gedrueckten Buttons.");
			}
		}

	}

	/**
	 * Klasse zur Implementierung des ueberschriebenen ComponentAdapter zur
	 * Anpassung der Schriftgroesse inerhalb des Spielfelds.
	 * 
	 * @author gillaj
	 *
	 */
	private class spielfeldComponentAdapter extends ComponentAdapter {

		/**
		 * Methode am Listener, die bei einer Groessenaenderung des Fensters
		 * aufgerufen wird.
		 * 
		 * @param e:
		 *            Uebergebene Eventinformationen
		 */
		public void componentResized(ComponentEvent e) {
			JButton[][] bf = Nonogramm.this.buttonFeld;
			/**
			 * Iteration und Anpassung der Schriftgroesse auf den einzelnen
			 * Feldern.
			 */
			for (int x = 0; x < bf.length; x++) {
				for (int y = 0; y < bf[x].length; y++) {
					/**
					 * Vorgabe der neuen Groesse als Hoehe des Buttons / 2
					 */
					bf[x][y].setFont(new Font(null, Font.PLAIN, bf[x][y].getHeight() / 2));
				}
			}
		}

	}

}