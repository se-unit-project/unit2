


/*
 * Class for representation of the control desk
 *
 */


import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.*;

public class ControlDeskView implements ActionListener, Observer {

	private JButton addParty, finished, assign,showScore;
	private JFrame window;
	private JList<Object> partyList;
	private int maxMembers; // 	The maximum  number of members in a party 
	private ControlDesk controlDesk;

	/**
	 * Displays a GUI representation of the ControlDesk
	 *
	 */
	public ControlDeskView(ControlDesk controlDesk, int maxMembers) {

		this.controlDesk = controlDesk;
		this.maxMembers = maxMembers;
		int numLanes = controlDesk.getNumLanes();

		window = new JFrame("Control Desk");
		window.getContentPane().setLayout(new BorderLayout());
		((JPanel) window.getContentPane()).setOpaque(false);

		JPanel colPanel = new JPanel();
		colPanel.setLayout(new BorderLayout());

		// Controls Panel
		JPanel controlsPanel = new JPanel();
		controlsPanel.setLayout(new GridLayout(3, 1));
		controlsPanel.setBorder(new TitledBorder("Controls"));

		addParty = new JButton("Add Party");
		JPanel addPartyPanel = new JPanel();
		addPartyPanel.setLayout(new FlowLayout());
		addParty.addActionListener(this);
		addPartyPanel.add(addParty);
		controlsPanel.add(addPartyPanel);

//		showScore = new JButton("Show Scores");
//		JPanel showScorePanel = new JPanel();
//		showScorePanel.setLayout(new FlowLayout());
//		showScore.addActionListener(this);
//		showScorePanel.add(showScore);
//		controlsPanel.add(showScorePanel);

		assign = new JButton("Assign Lanes");
		JPanel assignPanel = new JPanel();
		assignPanel.setLayout(new FlowLayout());
		assign.addActionListener(this);
		assignPanel.add(assign);

		finished = new JButton("Finished");
		JPanel finishedPanel = new JPanel();
		finishedPanel.setLayout(new FlowLayout());
		finished.addActionListener(this);
		finishedPanel.add(finished);
		controlsPanel.add(finishedPanel);

		// Lane Status Panel
		JPanel laneStatusPanel = new JPanel();
		laneStatusPanel.setLayout(new GridLayout(numLanes, 1));
		laneStatusPanel.setBorder(new TitledBorder("Lane Status"));

		HashSet lanes=controlDesk.getLanes();
		Iterator laneInterator = lanes.iterator();
		int laneCount=0;
		while (laneInterator.hasNext()) {
			Lane curLane = (Lane) laneInterator.next();
			LaneStatusView laneStat = new LaneStatusView(curLane,(laneCount+1));
			curLane.addObserver(laneStat);
			JPanel lanePanel = laneStat.showLane();
			lanePanel.setBorder(new TitledBorder("Lane" + ++laneCount ));
			laneStatusPanel.add(lanePanel);
		}

		// Party Queue Panel
		JPanel partyPanel = new JPanel();
		partyPanel.setLayout(new FlowLayout());
		partyPanel.setBorder(new TitledBorder("Party Queue"));

		Vector<String> empty = new Vector<>();
		empty.add("(Empty)");

		partyList = new JList<>(empty);
		partyList.setFixedCellWidth(120);
		partyList.setVisibleRowCount(10);
		JScrollPane partyPane = new JScrollPane(partyList);
		partyPane.setVerticalScrollBarPolicy(
			JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		partyPanel.add(partyPane);

		// Clean up main panel
		colPanel.add(controlsPanel, "East");
		colPanel.add(laneStatusPanel, "Center");
		colPanel.add(partyPanel, "West");

		window.getContentPane().add("Center", colPanel);

		window.pack();

		/* Close program when this window closes */
		window.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		// Center Window on Screen
		Dimension screenSize = (Toolkit.getDefaultToolkit()).getScreenSize();
		window.setLocation(
			((screenSize.width) / 2) - ((window.getSize().width) / 2),
			((screenSize.height) / 2) - ((window.getSize().height) / 2));
		window.show();
	}

	/**
	 * Handler for actionEvents
	 *
	 * @param e	the ActionEvent that triggered the handler
	 *
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(addParty)) {
			AddPartyView addPartyWin = new AddPartyView(this, maxMembers);
		}
//		if (e.getSource().equals(showScore)) {
//            ShowScores showScoreWin = new ShowScores();
//        }
		if (e.getSource().equals(assign)) {
			controlDesk.assignLane();
		}
		if (e.getSource().equals(finished)) {
			window.hide();
			System.exit(0);
		}
	}

	/**
	 * Receive a new party from andPartyView.
	 *
	 * @param addPartyView	the AddPartyView that is providing a new party
	 *
	 */
	public void updateAddParty(AddPartyView addPartyView) {
		controlDesk.addPartyQueue(addPartyView.getParty());
	}

	@Override
	public void update(Observable o, Object obj) {
		ControlDesk controlDesk;
		try{
			controlDesk = (ControlDesk)o;
		}catch(Exception e){
			return;
		}
		partyList.setListData(( controlDesk.getPartyQueue()));
	}
}