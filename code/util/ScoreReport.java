package util;
/*
 * 
 * SMTP implementation based on code by Real Gagnon mailto:real@rgagnon.com
 * 
 */

import model.Bowler;
import model.Score;
import util.ScoreHistoryFile;
import model.ShareReport;

import java.io.*;
import java.util.Vector;
import java.util.Iterator;

public class ScoreReport {

	private final ShareReport shareReport = new ShareReport();

	public ScoreReport(Bowler bowler, int[] scores) {
		String nick = bowler.getNickName();

		String full = bowler.getFullName();
		Vector<Score> vec_scores = null;
		try{
			vec_scores = ScoreHistoryFile.getScores(nick);
		} catch (Exception e){System.err.println("Error: " + e);}
		
		Iterator<Score> scoreIt = vec_scores.iterator();

		shareReport.setContent("");
		shareReport.setContent(shareReport.getContent() + "--Lucky Strike Bowling Alley Score Report--\n");
		shareReport.setContent(shareReport.getContent() + "\n");
		shareReport.setContent(shareReport.getContent() + "Report for " + full + ", aka \"" + nick + "\":\n");
		shareReport.setContent(shareReport.getContent() + "\n");
		shareReport.setContent(shareReport.getContent() + "Final scores for this session: ");
		shareReport.setContent(shareReport.getContent() + scores[0]);
		for (int i = 1; i < scores.length; i++){
			shareReport.setContent(shareReport.getContent() + ", " + scores[i]);
		}
		shareReport.setContent(shareReport.getContent() + ".\n");
		shareReport.setContent(shareReport.getContent() + "\n");
		shareReport.setContent(shareReport.getContent() + "\n");
		shareReport.setContent(shareReport.getContent() + "Previous scores by date: \n");
		while (scoreIt.hasNext()){
			Score score = scoreIt.next();
			shareReport.setContent(shareReport.getContent() + "  " + score.getDate() + " - " + score.getScore());
			shareReport.setContent(shareReport.getContent() + "\n");
		}
		shareReport.setContent(shareReport.getContent() + "\n\n");
		shareReport.setContent(shareReport.getContent() + "Thank you for your continuing patronage.");
	}

	public void sendEmail(String recipient) {
		shareReport.sendEmail(recipient);
	}

	public void sendPrintout() {
		shareReport.sendPrintout();
	}

	public void sendln(BufferedReader in, BufferedWriter out, String s) {
		shareReport.sendln(in, out, s);
	}

	public void sendln(BufferedWriter out, String s) {
		shareReport.sendln(out, s);
	}
}