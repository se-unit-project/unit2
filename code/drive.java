import model.ControlDesk;
import util.Setup;
import viewcontrol.ControlDeskView;
import viewcontrol.NotifyUser;

import java.io.*;
/**
 * drive serves as the startup file for the application.
 * 
 *
 */
public class drive {

	public static void main(String[] args) throws IOException {
		Setup setup = new Setup();
		NotifyUser notify = new NotifyUser();

		setup.readConfig();

		int numLanes = setup.getNumLanes();;
		int maxPatronsPerParty = setup.getMaxPatronsPerParty();

		if(maxPatronsPerParty > 6){
			notify.tooManyUsers();
			System.exit(0);
		}
		else {
			// Create a control desk and run the GUI
			ControlDesk controlDesk = new ControlDesk(numLanes);
			ControlDeskView cdv = new ControlDeskView( controlDesk, maxPatronsPerParty);
			controlDesk.addObserver(cdv);
		}
	}
}


