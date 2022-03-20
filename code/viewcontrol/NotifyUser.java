package viewcontrol;

import javax.swing.*;

public class NotifyUser {
	static JFrame frame;
	public static void tooManyUsers(){
		frame = new JFrame();
		JOptionPane.showMessageDialog(frame,"Number of users More than 6");
	}
}
