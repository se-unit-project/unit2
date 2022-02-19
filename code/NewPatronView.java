

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

/**
 * Class for GUI components need to add a patron
 *
 */
public class NewPatronView implements ActionListener {
	
	private JFrame win;
	private JButton abort, finished;
	private JLabel nickLabel, fullLabel, emailLabel;
	private JTextField nickField, fullField, emailField;
	private String nick, full, email;

	private boolean done;

	private AddPartyView addParty;

	public NewPatronView(AddPartyView v) {

		addParty = v;	
		done = false;
		
		win = new JFrame("Add Patron");
		win.getContentPane().setLayout(new BorderLayout());
		((JPanel) win.getContentPane()).setOpaque(false);

		JPanel colPanel = new JPanel();
		colPanel.setLayout(new BorderLayout());

		// Patron Panel
		JPanel patronPanel = new JPanel();
		patronPanel.setLayout(new GridLayout(3, 1));
		patronPanel.setBorder(new TitledBorder("Your Info"));

		JPanel nickPanel = new JPanel();
		addPanel(nickPanel, nickLabel, "Nick Name", nickField);

		JPanel fullPanel = new JPanel();
		addPanel(fullPanel, fullLabel, "Full Name", fullField);

		JPanel emailPanel = new JPanel();
		addPanel(emailPanel, emailLabel, "E-Mail", emailField);

		patronPanel.add(nickPanel);
		patronPanel.add(fullPanel);
		patronPanel.add(emailPanel);

		// Button Panel
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(4, 1));

		Insets buttonMargin = new Insets(4, 4, 4, 4);

		finished = new JButton("Add Patron");
		JPanel finishedPanel = new JPanel();
		addLayout(finishedPanel, finished);

		abort = new JButton("Abort");
		JPanel abortPanel = new JPanel();
		addLayout(abortPanel, abort);

		buttonPanel.add(abortPanel);
		buttonPanel.add(finishedPanel);

		// Clean up main panel
		colPanel.add(patronPanel, "Center");
		colPanel.add(buttonPanel, "East");

		win.getContentPane().add("Center", colPanel);

		win.pack();

		// Center Window on Screen
		Dimension screenSize = (Toolkit.getDefaultToolkit()).getScreenSize();
		win.setLocation(
			((screenSize.width) / 2) - ((win.getSize().width) / 2),
			((screenSize.height) / 2) - ((win.getSize().height) / 2));
		win.show();

	}

	private void addLayout(JPanel finishedPanel, JButton finished) {
		finishedPanel.setLayout(new FlowLayout());
		finished.addActionListener(this);
		finishedPanel.add(finished);
	}

	private void addPanel(JPanel nickPanel, JLabel nickLabel, String Nick_Name, JTextField nickField) {
		nickPanel.setLayout(new FlowLayout());
		nickLabel = new JLabel(Nick_Name);
		nickField = new JTextField("", 15);
		nickPanel.add(nickLabel);
		nickPanel.add(nickField);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(abort)) {
			done = true;
			win.hide();
		}

		if (e.getSource().equals(finished)) {
			nick = nickField.getText();
			full = fullField.getText();
			email = emailField.getText();
			done = true;
			addParty.updateNewPatron( this );
			win.hide();
		}

	}

	public boolean done() {
		return done;
	}

	public String getNick() {
		return nick;
	}

	public String getFull() {
		return full;
	}

	public String getEmail() {
		return email;
	}

}
