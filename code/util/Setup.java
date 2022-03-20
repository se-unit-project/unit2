package util;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Setup {
	static int numLanes = 0, maxPatronsPerParty = 0;
	static Properties props =new Properties();
	public static void readConfig() throws IOException {
		FileReader reader=new FileReader("config.properties");
		props.load(reader);
		numLanes = Integer.parseInt(props.getProperty("numLanes"));
		maxPatronsPerParty=Integer.parseInt(props.getProperty("maxPatronsPerParty"));
	}

	public int getNumLanes() {
		return Integer.parseInt(props.getProperty("numLanes"));
	}

	public int getMaxPatronsPerParty() {
		return Integer.parseInt(props.getProperty("maxPatronsPerParty"));
	}
}
