package util;

/**
 * Class for interfacing with model.Bowler database
 *
 */

import model.Bowler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Vector;

public class BowlerFile {

	private static String BOWLER_DAT = "./BOWLERS.DAT";

	/**
	 * Retrieves bowler information from the database and returns a model.Bowler objects with populated fields.
	 *
	 * @param nickName	the nickName of the bolwer to retrieve
	 *
	 * @return a model.Bowler object
	 * 
	 */

	public static Bowler getBowlerInfo(String nickName)
			throws IOException {

		BufferedReader in = new BufferedReader(new FileReader(BOWLER_DAT));
		String data;
		while ((data = in.readLine()) != null) {
			// File format is nick\tfname\te-mail
			String[] bowler = data.split("\t");
			if (nickName.equals(bowler[0])) {
				System.out.println("Nick: " + bowler[0] + " Full: " + bowler[1] + " email: " + bowler[2]);
				return (new Bowler(bowler[0], bowler[1], bowler[2]));
			}
		}
		System.out.println("Nick not found...");
		return null;
	}

	/**
	 * Stores a model.Bowler in the database
	 *
	 * @param bowler
	 *
	 */
	public static void storeBowlerInfo(Bowler bowler) throws IOException {
		String data = bowler.getNickName() + "\t" + bowler.getFullName() + "\t" + bowler.getEmail() + "\n";
		RandomAccessFile out = new RandomAccessFile(BOWLER_DAT, "rw");
		out.skipBytes((int) out.length());
		out.writeBytes(data);
		out.close();
	}

	/**
	 * Retrieves a list of nicknames in the bowler database
	 *
	 * @return a Vector of Strings
	 * 
	 */
	public static Vector getBowlers() throws IOException {
		Vector bowlers = new Vector();
		BufferedReader in = new BufferedReader(new FileReader(BOWLER_DAT));
		String data;
		while ((data = in.readLine()) != null) {
			// File format is nick\tfname\te-mail
			String[] bowler = data.split("\t");
			//"Nick: bowler[0] Full: bowler[1] email: bowler[2]
			bowlers.add(bowler[0]);
		}
		return bowlers;
	}
}