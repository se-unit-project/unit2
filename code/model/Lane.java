package model;

import util.ScoreHistoryDb;
import viewcontrol.ControlDeskView;

import java.util.*;

public class Lane extends Observable implements Observer, Runnable {

	public static final int NUMBER_OF_PINS = 10;
	private Party party;
	private Pinsetter setter;
	private HashMap scores;
	private boolean gameIsHalted;
	private boolean partyAssigned;
	private boolean gameFinished;
	private Iterator bowlerIterator;
	private int ball;
	private int bowlIndex;
	private int frameNumber;
	private boolean tenthFrameStrike;
	private int[][] cumulScores;
	private boolean canThrowAgain;
	private int gameNumber;
	private Bowler currentThrower;			// = the thrower who just took a throw
	private ScoreCalculator sc;
	private boolean extraChance;
	private int framesAllowed = 9;

	private int highestPlayer;
	private int secondHighestPlayer;
	private boolean tieBreakerAllowed = true;


	/** model.Lane()
	 *
	 * Constructs a new lane and starts its thread
	 *
	 * @pre none
	 * @post a new lane has been created and its thread is executing
	 */
	public Lane() {
		this.setter = new Pinsetter();
		this.setter.addObserver(this);
		this.scores = new HashMap();
		this.gameIsHalted = false;
		this.partyAssigned = false;
		this.gameNumber = 0;
//		this.framesAllowed = 9;
		(new Thread(this, "Lane Thread")).start();
	}

	public Lane(int f){
		this();
		this.extraChance = false;
		framesAllowed = f-1;
		this.tieBreakerAllowed = false;
	}

	private void handleEndGame() {

		int totalBowlers = party.getMembers().size();
		int max = 0, smax = 0, sind = 0, find = 0, i;

		// findSecondHighestPlayer()
		for(i=0; i<totalBowlers; i++) {
			if(cumulScores[i][9]>max) {
				smax = max;
				sind = find;
				max = cumulScores[i][9];
				find = i;
			}
			else if(cumulScores[i][9]>smax) {
				smax = cumulScores[i][9];
				sind = i;
			}
		}

		highestPlayer = find;
		secondHighestPlayer = sind;

		extraChance = true;
		for(i=0; i<sind; i++) {
			currentThrower = (Bowler)bowlerIterator.next();
		}
		// setter.ballThrown();
		Random rand = new Random();
		int randomNum = rand.nextInt((10 - 5) + 1) + 5;
		handleExtraChance(randomNum);
	}

	private void handleExtraChance(int pinsDown) {
		int highestScore = cumulScores[highestPlayer][9];
		int secondHighestScore = cumulScores[secondHighestPlayer][9] + pinsDown;


		if(highestScore <= secondHighestScore+50) {
			Vector bowlers = new Vector(party.getMembers());
			Vector partyNicks = new Vector();
			partyNicks.add(((Bowler) bowlers.get(highestPlayer)).getNickName());
			partyNicks.add(((Bowler) bowlers.get(secondHighestPlayer)).getNickName());


			ControlDesk newControlDesk = new ControlDesk(1, 3);
			ControlDeskView newCDV = new ControlDeskView( newControlDesk, 2);
//			newControlDesk.subscribe( newCDV );

			newControlDesk.addPartyQueue(partyNicks);
		} else {
			System.out.println("The second highest player is not able to cross the highest player");
			System.out.println("So the game ends here");
		}
		return ;
	}



	/** run()
	 *
	 * entry point for execution of this lane
	 */
	public void run() {
		while (true) {
			if (partyAssigned && !gameFinished) {	// we have a party on this lane, so next bower can take a throw
				while (gameIsHalted) {
					try {
						Thread.sleep(NUMBER_OF_PINS);
					} catch (Exception e) {}
				}
				if (bowlerIterator.hasNext()) {
					currentThrower = (Bowler)bowlerIterator.next();
					canThrowAgain = true;
					tenthFrameStrike = false;
					ball = 0;
//					if(signal==1)
//					{
//
//						publish(lanePublish());
//						signal = 0;
//						resetBowlerIterator();
//						ball = 0;
//						bowlIndex = 0;
//						continue;
//					}
					while (canThrowAgain) {
						setter.ballThrown();		// simulate the thrower's ball hiting
						ball++;
					}

					if (frameNumber == framesAllowed){
						try{
							Date date = new Date();
							String dateString = "" + date.getHours() + ":" + date.getMinutes() + " " + date.getMonth() + "/" + date.getDay() + "/" + (date.getYear() + 1900);
							ScoreHistoryDb.addScore(currentThrower.getNickName(), dateString, new Integer(cumulScores[bowlIndex][9]).toString());
						} catch (Exception e) {System.err.println("Exception in addScore. "+ e );}
					}
					setter.reset();
					bowlIndex++;
				}
				else {
					frameNumber++;
					resetBowlerIterator();
					bowlIndex = 0;
					if (frameNumber > framesAllowed) {
						publish();
						gameFinished = true;
						gameNumber++;
					}
				}
			}
			else if (partyAssigned && gameFinished) {
				if(tieBreakerAllowed){
					handleEndGame();
					tieBreakerAllowed = false;
				}
				publish();
			}
			try {
				Thread.sleep(NUMBER_OF_PINS);
			} catch (Exception e) {}
		}
	}

	public void clearLane(){
		party = null;
		partyAssigned = false;
	}

	/** resetBowlerIterator()
	 *
	 * sets the current bower iterator back to the first bowler
	 *
	 * @pre the party as been assigned
	 * @post the iterator points to the first bowler in the party
	 */
	public void resetBowlerIterator() {
		bowlerIterator = (party.getMembers()).iterator();
	}

	/** resetScores()
	 *
	 * resets the scoring mechanism, must be called before scoring starts
	 *
	 * @pre the party has been assigned
	 * @post scoring system is initialized
	 */
	public void resetScores() {
		Iterator bowlIt = (party.getMembers()).iterator();
		while ( bowlIt.hasNext() ) {
			int[] toPut = new int[25];
			for ( int i = 0; i != 25; i++){
				toPut[i] = -1;
			}
			scores.put( bowlIt.next(), toPut );
		}
		gameFinished = false;
		frameNumber = 0;
	}

	/** assignParty()
	 *
	 * assigns a party to this lane
	 *
	 * @pre none
	 * @post the party has been assigned to the lane
	 *
	 * @param theParty		model.Party to be assigned
	 */
	public void assignParty( Party theParty ) {
		party = theParty;
		resetBowlerIterator();
		partyAssigned = true;
		cumulScores = new int[party.getMembers().size()][NUMBER_OF_PINS];
		this.sc = new ScoreCalculator(cumulScores);
		gameNumber = 0;
		resetScores();
	}

	/** markScore()
	 *
	 * Method that marks a bowlers score on the board.
	 *
	 * @param Cur		The current bowler
	 * @param frame	The frame that bowler is on
	 * @param ball		The ball the bowler is on
	 * @param score	The bowler's score
	 */
	private void markScore( Bowler Cur, int frame, int ball, int score ){
		int[] curScore;
		int index =  ( (frame - 1) * 2 + ball);
		curScore = (int[]) scores.get(Cur);
		curScore[ index - 1] = score;
		scores.put(Cur, curScore);
		sc.calculateGame((int[])this.scores.get(Cur), this.bowlIndex, this.frameNumber, ball, score);
		publish();
	}

	public int[][] getFinalScores(){
		return(cumulScores);
	}

	public int getGameNumber(){
		return(this.gameNumber);
	}

	/** isPartyAssigned()
	 *
	 * checks if a party is assigned to this lane
	 *
	 * @return true if party assigned, false otherwise
	 */
	public boolean isPartyAssigned() {
		return partyAssigned;
	}

	/** isGameFinished
	 *
	 * @return true if the game is done, false otherwise
	 */
	public boolean isGameFinished() {
		return gameFinished;
	}

	public void publish(){
		this.setChanged();
		this.notifyObservers();
	}

	/**
	 * Pause the execution of this game
	 */
	public void pauseGame() {
		gameIsHalted = true;
		publish();
	}

	/**
	 * Resume the execution of this game
	 */
	public void unPauseGame() {
		gameIsHalted = false;
		publish();
	}

	/**
	 * @return the party
	 */
	public Party getParty() {
		return party;
	}

	/**
	 * @return the scores
	 */
	public HashMap getScores() {
		return scores;
	}

	/**
	 * @return the gameIsHalted
	 */
	public boolean isGameIsHalted() {
		return gameIsHalted;
	}

	/**
	 * @return the ball
	 */
	public int getBall() {
		return ball;
	}

	/**
	 * @return the bowlIndex
	 */
	public int getBowlIndex() {
		return bowlIndex;
	}

	/**
	 * @return the frameNumber
	 */
	public int getFrameNumber() {
		return frameNumber;
	}

	/**
	 * @return the cumulScores
	 */
	public int[][] getCumulScores() {
		return cumulScores;
	}

	/**
	 * @return the currentThrower
	 */
	public Bowler getCurrentThrower() {
		return currentThrower;
	}

	/**
	 * Assessor to get this model.Lane's pinsetter
	 *
	 * @return		A reference to this lane's pinsetter
	 */
	public Pinsetter getPinsetter() {
		return setter;
	}

	@Override
	public void update(Observable o, Object arg) {
		PinsetterEvent pe = (PinsetterEvent)arg;
		if(pe.pinsDownOnThisThrow() < 0){ // this is not a real throw
			return;
		}
		markScore(currentThrower, frameNumber + 1, pe.getThrowNumber(), pe.pinsDownOnThisThrow());
		// next logic handles the ?: what conditions dont allow them another throw?
		// handle the case of 10th frame first
		if (frameNumber == 9) {
			if (pe.totalPinsDown() == NUMBER_OF_PINS) {
				setter.resetPins();
				if(pe.getThrowNumber() == 1) {
					tenthFrameStrike = true;
				}
			}
			if ((pe.totalPinsDown() != NUMBER_OF_PINS) && (pe.getThrowNumber() == 2 && tenthFrameStrike == false) || pe.getThrowNumber() == 3) {
				canThrowAgain = false;
			}
		} else if (pe.pinsDownOnThisThrow() == NUMBER_OF_PINS || pe.getThrowNumber() == 2) {		// threw a strike
			canThrowAgain = false;
		}
	}
}