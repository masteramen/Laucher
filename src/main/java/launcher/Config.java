package launcher;

import java.io.IOException;
import java.util.Properties;

public final class Config {

	public static String checkUpdateUrl;
	public static String appName;
	
	
	private Config(){}
	
	static {
		final Properties properties = new Properties();
		try {
			
			properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties"));
			checkUpdateUrl = properties.getProperty("checkUpdateUrl");
			appName = properties.getProperty("appName");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}


}
