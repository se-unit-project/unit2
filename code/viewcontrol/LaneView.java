package viewcontrol;
/*
 *  constructs a prototype model.Lane View
 *
 */

import model.Bowler;
import model.Lane;
import model.Party;

import java.awt.*;
import java.awt.event.*;
import javax.imageio.ImageIO;
import javax.swing.*;


import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;

public class LaneView implements ActionListener, Observer {

	public static final int NUM_OF_TRIES = 23;
	public static final int NUM_OF_ROUNDS = 10;
	public static final int LAST_ROUND = 9;
	private boolean initDone = false;
	String winner;
	int result =0;
	JFrame frame;
	Container cpanel;
	Vector bowlers;
	JPanel[][] balls;
	JLabel[][] ballLabel;
	JPanel[][] scores;
	JLabel[][] scoreLabel;
	JLabel[][] emojiLable;
	JLabel envyEmojiLabel;
	JLabel embarrassedEmojiLabel;
	JLabel appreicateEmojiLabel;
	JLabel winnerLabel;
	JPanel[][] ballGrid;
	JPanel[] pins;
	JPanel panel;
	JButton maintenance,throwButton;
	Lane lane;
	int finalScore;
	public LaneView(Lane lane, int laneNum) {

		this.lane = lane;
		frame = new JFrame("Lane " + laneNum + ":");
		cpanel = frame.getContentPane();
		cpanel.setLayout(new BorderLayout());
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				frame.hide();
			}
		});
		cpanel.add(new JPanel());
	}

	public void show() {
		frame.show();
	}

	public void hide() {
		frame.hide();
	}

	private JPanel makeFrame(Party party) {
		bowlers = party.getMembers();
		int numBowlers = bowlers.size();
		panel = new JPanel();
		panel.setLayout(new GridLayout(0, 1));
		balls = new JPanel[numBowlers][NUM_OF_TRIES];
		ballLabel = new JLabel[numBowlers][NUM_OF_TRIES];

		scores = new JPanel[numBowlers][NUM_OF_ROUNDS];
		scoreLabel = new JLabel[numBowlers][NUM_OF_ROUNDS];
		emojiLable = new JLabel[numBowlers][NUM_OF_ROUNDS];
		ballGrid = new JPanel[numBowlers][NUM_OF_ROUNDS];
		pins = new JPanel[numBowlers];

		for (int i = 0; i != numBowlers; i++) {
			for (int j = 0; j != NUM_OF_TRIES; j++) {
				ballLabel[i][j] = new JLabel(" ");
				balls[i][j] = new JPanel();
				balls[i][j].setBorder(
						BorderFactory.createLineBorder(Color.BLACK));
				balls[i][j].add(ballLabel[i][j]);
			}
		}
		try{
//			System.out.println("Working Directory = " + System.getProperty("user.dir"));
			BufferedImage envy = ImageIO.read(new File("static/envy.jpg"));
			envyEmojiLabel = new JLabel(new ImageIcon(envy));
			envyEmojiLabel.setSize(10, 10);
			
			BufferedImage embarrassed = ImageIO.read(new File("static/embarrassed.jpg"));
			embarrassedEmojiLabel = new JLabel(new ImageIcon(embarrassed));
			embarrassedEmojiLabel.setSize(10, 10);
			
			BufferedImage appreicate = ImageIO.read(new File("static/appreciate.jpeg"));
			appreicateEmojiLabel = new JLabel(new ImageIcon(appreicate));
			appreicateEmojiLabel.setSize(10, 10);
			
		}
		catch(Exception e){
			System.out.println("Error loading emoji");
		}
		for (int i = 0; i != numBowlers; i++) {
			for (int j = 0; j != LAST_ROUND; j++) {
				ballGrid[i][j] = new JPanel();
				ballGrid[i][j].setLayout(new GridLayout(0, 3));
				ballGrid[i][j].add(new JLabel("  "), BorderLayout.EAST);
				ballGrid[i][j].add(balls[i][2 * j], BorderLayout.EAST);
				ballGrid[i][j].add(balls[i][2 * j + 1], BorderLayout.EAST);
			}

			ballGrid[i][LAST_ROUND] = new JPanel();
			ballGrid[i][LAST_ROUND].setLayout(new GridLayout(0, 3));
			ballGrid[i][LAST_ROUND].add(balls[i][2 * LAST_ROUND]);
			ballGrid[i][LAST_ROUND].add(balls[i][2 * LAST_ROUND + 1]);
			ballGrid[i][LAST_ROUND].add(balls[i][2 * LAST_ROUND + 2]);
		}

		for (int i = 0; i != numBowlers; i++) {
			pins[i] = new JPanel();
			pins[i].setBorder(
					BorderFactory.createTitledBorder(
							((Bowler) bowlers.get(i)).getNickName()));
			pins[i].setLayout(new GridLayout(0, NUM_OF_ROUNDS));
			for (int k = 0; k != NUM_OF_ROUNDS; k++) {
				scores[i][k] = new JPanel();
				scoreLabel[i][k] = new JLabel("  ", SwingConstants.CENTER);
				emojiLable[i][k] = new JLabel("  ", SwingConstants.CENTER);
				scores[i][k].setBorder(
						BorderFactory.createLineBorder(Color.BLACK));
				scores[i][k].setLayout(new GridLayout(0, 1));
				scores[i][k].add(ballGrid[i][k], BorderLayout.EAST);
				scores[i][k].add(scoreLabel[i][k], BorderLayout.SOUTH);
				scores[i][k].add(emojiLable[i][k], BorderLayout.SOUTH);
				pins[i].add(scores[i][k], BorderLayout.EAST);
			}
			panel.add(pins[i]);
		}

		initDone = true;
		return panel;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(maintenance)) lane.pauseGame();
	}

	@Override
	public void update(Observable o, Object arg) {		
		if(!(o instanceof Lane)) return;
		Lane le = (Lane)o;

		if (lane.isPartyAssigned() && !lane.isGameFinished()) {
			
			if(!initDone){
				cpanel.removeAll();
				cpanel.add(makeFrame(le.getParty()), "Center");
			}
			
			int numBowlers = le.getParty().getMembers().size();
		
			if (le.getFrameNumber() == 0 && le.getBall() == 0 && le.getBowlIndex() == 0) {
				System.out.println("Making the frame.");
				cpanel.removeAll();
				cpanel.add(makeFrame(le.getParty()), "Center");

				// Button Panel
				JPanel buttonPanel = new JPanel();
				buttonPanel.setLayout(new FlowLayout());

				maintenance = new JButton("Maintenance Call");
				JPanel maintenancePanel = new JPanel();
				maintenancePanel.setLayout(new FlowLayout());
				maintenance.addActionListener(this);
				maintenancePanel.add(maintenance);
				buttonPanel.add(maintenancePanel);

				throwButton = new JButton("Throw");
				JPanel throwPanel = new JPanel();
				throwPanel.setLayout(new FlowLayout());
				throwButton.addActionListener(this);
				throwPanel.add(throwButton);
				buttonPanel.add(throwPanel);

				cpanel.add(buttonPanel, "South");

				frame.pack();
				cpanel.setVisible(true);
			}
			
			int[][] lescores = le.getCumulScores();
			HashMap scores = le.getScores();
			this.bowlers = le.getParty().getMembers();
			for (int k = 0; k < numBowlers; k++) {
				for (int i = 0; i <= le.getFrameNumber() - 1; i++) {
					if (lescores[k][i] != 0){
						finalScore = lescores[k][i];
						this.scoreLabel[k][i].setText((new Integer(lescores[k][i])).toString());
						int prev = i>0 ? lescores[k][i-1] : 0;
						int diff = Math.abs(lescores[k][i] - prev);
						if(diff < 7){
							this.emojiLable[k][i].setText(new String(Character.toChars(0x1F633)));
						}else if(diff < 9){
							this.emojiLable[k][i].setText(new String(Character.toChars(0x1F609)));
						}
						else {
							this.emojiLable[k][i].setText(new String(Character.toChars(0x1F60E)));
						}

					}
				}
				if(finalScore > result){
					result = finalScore;
					winner = ((Bowler)bowlers.get(k)).getNickName();
				}
				for (int i = 0; i < 21; i++) {
					if (((int[]) (scores.get(bowlers.get(k))))[i] != -1){
						if (((int[]) scores.get(bowlers.get(k)))[i] == NUM_OF_ROUNDS && (i % 2 == 0 || i == 19)){
							ballLabel[k][i].setText("X");
						}					
						else if (i > 0 && ((int[]) scores.get(bowlers.get(k)))[i] + ((int[]) scores.get(bowlers.get(k)))[i - 1] == NUM_OF_ROUNDS && i % 2 == 1){
							ballLabel[k][i].setText("/");
						}
						else if ( ((int[])scores.get(bowlers.get(k)))[i] == -2 ){
							ballLabel[k][i].setText("F");
						} 
						else{
							ballLabel[k][i].setText((new Integer(((int[]) scores.get(bowlers.get(k)))[i])).toString());
						}
					}
				}

			}
			
		}
		else{
			initDone = false;
			System.out.println(result);
			winnerLabel = new JLabel("Winner: " + winner);
			winnerLabel.setFont(new Font("Serif", Font.BOLD, 20));
			winnerLabel.setHorizontalAlignment(JLabel.CENTER);
			panel.add(winnerLabel, BorderLayout.NORTH);
			if(result < 100)
				panel.add(embarrassedEmojiLabel);
			else if(result < 120)
				panel.add(envyEmojiLabel);
			else
				panel.add(appreicateEmojiLabel);
			panel.repaint();
			cpanel.removeAll();
			cpanel.add(panel, "Center");
			frame.pack();
			cpanel.setVisible(true);
		}
	}
}
