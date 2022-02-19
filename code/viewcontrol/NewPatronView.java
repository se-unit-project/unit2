package viewcontrol;

import viewcontrol.AddPartyView;

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

		nickLabel = new JLabel("Nick Name");
		nickField = new JTextField("", 15);
		JPanel nickPanel = getjPanel(nickLabel,nickField);

		fullLabel = new JLabel("Full Name");
		fullField = new JTextField("", 15);
		JPanel fullPanel = getjPanel(fullLabel,fullField);

		emailLabel = new JLabel("E-Mail");
		emailField = new JTextField("", 15);
		JPanel emailPanel = getjPanel(emailLabel,emailField);

		patronPanel.add(nickPanel);
		patronPanel.add(fullPanel);
		patronPanel.add(emailPanel);

		// Button Panel
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(4, 1));

		finished = new JButton("Add Patron");
		JPanel finishedPanel = addButton(finished);

		abort = new JButton("Abort");
		JPanel abortPanel = addButton(abort);

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

	private JPanel addButton(JButton finished) {
		JPanel finishedPanel = new JPanel();
		finishedPanel.setLayout(new FlowLayout());
		finished.addActionListener(this);
		finishedPanel.add(finished);
		return finishedPanel;
	}

	private JPanel getjPanel(JLabel nickLabel,JTextField nickField) {
		JPanel nickPanel = new JPanel();
		nickPanel.setLayout(new FlowLayout());
		nickPanel.add(nickLabel);
		nickPanel.add(nickField);
		return nickPanel;
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