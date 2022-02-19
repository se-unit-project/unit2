package model;

/*
 * Class that represents control desk
 *
 */

import model.Bowler;
import model.BowlerFile;

import java.util.*;
import java.io.*;

public class ControlDesk extends Observable implements Runnable {

	/** The collection of Lanes */
	private HashSet<Lane> lanes;
	/** The party wait queue */
	private Queue partyQueue;
	/** The number of lanes represented */
	private int numLanes;

	/**
	 * Constructor for the model.ControlDesk class
	 *
	 * @param numlanes	the numbler of lanes to be represented
	 *
	 */
	public ControlDesk(int numLanes) {
		this.numLanes = numLanes;
		lanes = new HashSet<>(numLanes);
		partyQueue = new Queue();
		for (int i = 0; i < numLanes; i++) {
			lanes.add(new Lane());
		}
		(new Thread(this, "Control Desk Thread")).start();
	}

	/**
	 * Main loop for model.ControlDesk's thread
	 * 
	 */
	public void run() {
		while (true) {
			assignLane();
			try {
				Thread.sleep(250);
			} catch (Exception e) {}
		}
	}

	/**
	 * Retrieves a matching model.Bowler from the bowler database.
	 *
	 * @param nickName	The NickName of the model.Bowler
	 *
	 * @return a model.Bowler object.
	 *
	 */
	private Bowler registerPatron(String nickName) {
		Bowler patron = null;
		try {
			patron = BowlerFile.getBowlerInfo(nickName);
		} catch (FileNotFoundException e) {
			System.err.println("Error..." + e);
		} catch (IOException e) {
			System.err.println("Error..." + e);
		}
		return patron;
	}

	/**
	 * Iterate through the available lanes and assign the paties in the wait queue if lanes are available.
	 *
	 */
	public void assignLane() {
		Iterator<Lane> laneIterator = lanes.iterator();
		while (laneIterator.hasNext() && partyQueue.hasMoreElements()) {
			Lane curLane = laneIterator.next();

			if (curLane.isPartyAssigned() == false) {
				System.out.println("ok... assigning this party");
				curLane.assignParty(((Party) partyQueue.next()));
			}
		}
		publish();
	}

	/**
	 * Creates a party from a Vector of nickNAmes and adds them to the wait queue.
	 *
	 * @param partyNicks	A Vector of NickNames
	 *
	 */
	public void addPartyQueue(Vector partyNicks) {
		Vector<Bowler> partyBowlers = new Vector<>();
		for (int i = 0; i < partyNicks.size(); i++) {
			Bowler newBowler = registerPatron(((String) partyNicks.get(i)));
			partyBowlers.add(newBowler);
		}
		Party newParty = new Party(partyBowlers);
		partyQueue.add(newParty);
		publish();
	}

	/**
	 * Returns a Vector of party names to be displayed in the GUI representation of the wait queue.
	 *
	 * @return a Vecotr of Strings
	 *
	 */
	public Vector<String> getPartyQueue() {
		Vector<String> displayPartyQueue = new Vector<>();
		for ( int i=0; i < ( (Vector)partyQueue.asVector()).size(); i++ ) {
			String nextParty =
					((Bowler) ((Vector) ((Party) partyQueue.asVector().get( i ) ).getMembers())
							.get(0))
					.getNickName() + "'s Party";
			displayPartyQueue.addElement(nextParty);
		}
		return displayPartyQueue;
	}

	/**
	 * Accessor for the number of lanes represented by the model.ControlDesk
	 * 
	 * @return an int containing the number of lanes represented
	 *
	 */
	public int getNumLanes() {
		return numLanes;
	}

	/**
	 * Broadcast an event to subscribing objects.
	 *
	 */
	public void publish() {
		this.setChanged();
		this.notifyObservers();
	}

	/**
	 * Accessor method for lanes
	 * 
	 * @return a HashSet of Lanes
	 *
	 */
	public HashSet<Lane> getLanes() {
		return lanes;
	}
}
