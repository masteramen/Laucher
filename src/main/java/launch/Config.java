package launch;

import java.io.IOException;
import java.util.Properties;

public final class Config {

	public static String checkUpdateUrl;
	public static String appName;
	public static String loadingImageSrc;
	
	
	private Config(){}
	
	static {
		final Properties properties = new Properties();
		try {
			
			properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties"));
			checkUpdateUrl = properties.getProperty("checkUpdateUrl");
			appName = properties.getProperty("appName");
			loadingImageSrc = properties.getProperty("loadingImageSrc");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}


}
