package viewcontrol;

import model.Pinsetter;
import model.PinsetterEvent;

import java.awt.*;
import javax.swing.*;

import java.util.*;

/**
 *  constructs a prototype PinSetter GUI
 *
 */
public class PinsetterView implements Observer {

	public static final int NUMBER_OF_PINS = 10;
	private Vector pins;
	private JPanel firstRoll;
	private JPanel secondRoll;
	private JFrame frame;

	/**
	 * Constructs a Pin Setter GUI displaying which roll it is with
	 * yellow boxes along the top (1 box for first roll, 2 boxes for second)
	 * and displays the pins as numbers in this format:
	 *
	 *                7   8   9   10
	 *                  4   5   6
	 *                    2   3
	 *                      1
	 *
	 */
	public PinsetterView (Pinsetter ps, int laneNum ) {
		ps.addObserver(this);
		frame = new JFrame ( "Lane " + laneNum + ":" );

		Container cpanel = frame.getContentPane ( );

		JPanel pinsPanel = new JPanel ( );

		pinsPanel.setLayout ( new GridLayout ( 4, 7 ) );

		//********************Top of GUI indicates first or second roll

		JPanel top = new JPanel ( );

		firstRoll = new JPanel ( );
		firstRoll.setBackground( Color.yellow );

		secondRoll = new JPanel ( );
		secondRoll.setBackground ( Color.black );

		top.add ( firstRoll, BorderLayout.WEST );

		top.add ( secondRoll, BorderLayout.EAST );

		//******************************************************************

		//**********************Grid of the pins**************************

		Vector<JPanel> panels = new Vector<JPanel>();
		Vector<JLabel> labels = new Vector<JLabel>();
		pins = new Vector ( ); //will keep references to the pin labels to show,which ones have fallen.
		for(int i=0; i<NUMBER_OF_PINS; i++) {
			panels.add(new JPanel());
			labels.add(new JLabel(Integer.toString(i+1)));
			panels.get(i).add(labels.get(i));
			pins.add(labels.get(i));
		}


		//******************************Fourth Row**************

		pinsPanel.add ( panels.get(6) );
		pinsPanel.add ( new JPanel ( ) );
		pinsPanel.add ( panels.get(7) );
		pinsPanel.add ( new JPanel ( ) );
		pinsPanel.add ( panels.get(8) );
		pinsPanel.add ( new JPanel ( ) );
		pinsPanel.add ( panels.get(9) );

		//*****************************Third Row***********

		pinsPanel.add ( new JPanel ( ) );
		pinsPanel.add ( panels.get(3) );
		pinsPanel.add ( new JPanel ( ) );
		pinsPanel.add ( panels.get(4) );
		pinsPanel.add ( new JPanel ( ) );
		pinsPanel.add ( panels.get(5) );

		//*****************************Second Row**************

		pinsPanel.add ( new JPanel ( ) );
		pinsPanel.add ( new JPanel ( ) );
		pinsPanel.add ( new JPanel ( ) );
		pinsPanel.add ( panels.get(1) );
		pinsPanel.add ( new JPanel ( ) );
		pinsPanel.add ( panels.get(2) );
		pinsPanel.add ( new JPanel ( ) );
		pinsPanel.add ( new JPanel ( ) );

		//******************************First Row*****************

		pinsPanel.add ( new JPanel ( ) );
		pinsPanel.add ( new JPanel ( ) );
		pinsPanel.add ( new JPanel ( ) );
		pinsPanel.add ( panels.get(0) );
		pinsPanel.add ( new JPanel ( ) );
		pinsPanel.add ( new JPanel ( ) );
		pinsPanel.add ( new JPanel ( ) );
		//*********************************************************

		top.setBackground ( Color.black );

		cpanel.add ( top, BorderLayout.NORTH );

		pinsPanel.setBackground ( Color.black );
		pinsPanel.setForeground ( Color.yellow );

		cpanel.add ( pinsPanel, BorderLayout.CENTER );

		frame.pack();
	}

	public void show() {
		frame.show();
	}

	public void hide() {
		frame.hide();
	}

	@Override
	public void update(Observable o, Object arg) {
		PinsetterEvent pe = (PinsetterEvent)arg;
		if ( !(pe.isFoulCommited()) ) {
			JLabel tempPin = new JLabel ( );
			for (int c = 0; c < NUMBER_OF_PINS; c++ ) {
				boolean pin = pe.pinKnockedDown ( c );
				tempPin = (JLabel) pins.get ( c );
				if ( pin ) tempPin.setForeground(Color.lightGray);
			}
		}
		if ( pe.getThrowNumber() == 1 ) secondRoll.setBackground(Color.yellow);
		if ( pe.pinsDownOnThisThrow() == -1) {
			for (int i = 0; i != NUMBER_OF_PINS; i++) ((JLabel) pins.get(i)).setForeground(Color.black);
			secondRoll.setBackground( Color.black);
		}
	}
}