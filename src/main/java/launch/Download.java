package launch;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

import javax.swing.JOptionPane;

class Update{
	public String url;
	public String version;
	public InputStream in;
	public long size;
	protected long lastModifyTime;
}
public class Download {
 
public static Update checkUpdate2( ) throws IOException {


	if(!JarTool.makeSureUpdateSuccess()){
		JOptionPane.showMessageDialog(null, JarTool.getWorkDir().getAbsolutePath()+"已经存在，并且不是目录,更新终止！", "错误提示",JOptionPane.ERROR_MESSAGE);  
		return null;
	}
	
	
	String url = "http://www.jfox.info/update.php?_m="+JarTool.getJarModuleName()+"&_v="+JarTool.getLatestJarVersion()+"&_postfix=.jar"+"&_t="+new Date().getTime();
 
	URL obj = new URL(url);
	HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
	conn.setReadTimeout(15000);
	conn.addRequestProperty("Accept-Language", "en-US,en;q=0.8");
	conn.addRequestProperty("User-Agent", "Mozilla");
	conn.addRequestProperty("Referer", "google.com");
	conn.setInstanceFollowRedirects(false);
	System.out.println("Request URL ... " + url);
 
	boolean redirect = false;
 
	// normally, 3xx is redirect
	int status = conn.getResponseCode();
	if (status != HttpURLConnection.HTTP_OK) {
		if (status == HttpURLConnection.HTTP_MOVED_TEMP
			|| status == HttpURLConnection.HTTP_MOVED_PERM
				|| status == HttpURLConnection.HTTP_SEE_OTHER)
		redirect = true;
	}
 
	System.out.println("Response Code ... " + status);
 
	if (redirect) {
 
		Update update = new Update();
		
		String newUrl = conn.getHeaderField("Location");
		
		update.url = newUrl;
		
		if(new File(JarTool.getDownloadUpdateJarFileName(newUrl)).exists()){
			System.out.println("已经是最新的文件了");
			return null;
		}
		
		String cookies = conn.getHeaderField("Set-Cookie");
 
		conn = (HttpURLConnection) new URL(newUrl).openConnection();
		conn.setRequestProperty("Cookie", cookies);
		conn.addRequestProperty("Accept-Language", "en-US,en;q=0.8");
		conn.addRequestProperty("User-Agent", "Mozilla");
		conn.addRequestProperty("Referer", "google.com");
		//update.size=conn.getHeaderFieldLong("Content-Length", 0);
		
		String size = conn.getHeaderField("Content-Length");
		if(size!=null)update.size = Long.parseLong(size);

		System.out.println("Redirect to URL : " + newUrl);
		update.in = conn.getInputStream();
		return update;
	}
 
	return null;
}
private static String readInputStreamToString(HttpURLConnection connection) {
    String result = null;
    StringBuffer sb = new StringBuffer();
    InputStream is = null;

    try {
        is = new BufferedInputStream(connection.getInputStream());
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String inputLine = "";
        while ((inputLine = br.readLine()) != null) {
            sb.append(inputLine).append("\n");
        }
        result = sb.toString();
    }
    catch (Exception e) {
        result = null;
    }
    finally {
        if (is != null) {
            try { 
                is.close(); 
            } 
            catch (IOException e) {
               e.printStackTrace();
            }
        }   
    }

    return result;
}
public static Update checkUpdate( ) throws IOException {


	if(!JarTool.makeSureUpdateSuccess()){
		JOptionPane.showMessageDialog(null, JarTool.getWorkDir().getAbsolutePath()+"已经存在，并且不是目录,更新终止！", "错误提示",JOptionPane.ERROR_MESSAGE);  
		return null;
	}
	
	
	String url = Config.checkUpdateUrl+"?_t="+new Date().getTime();
 
	URL obj = new URL(url);
	HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
	conn.setReadTimeout(150000);
	conn.addRequestProperty("Accept-Language", "en-US,en;q=0.8");
	conn.addRequestProperty("User-Agent", "Mozilla");
	conn.addRequestProperty("Referer", "google.com");
	conn.setInstanceFollowRedirects(false);
	

	String response = readInputStreamToString(conn);
	String[] vesionInfo = response.trim().split("\n+");
	if (vesionInfo!=null && vesionInfo[0].trim().compareTo(JarTool.getCurrentVersionString())>0) {
 
		Update update = new Update();		
		update.url = url.split("\\?")[0];
		
		if(new File(JarTool.getDownloadUpdateJarFileName(vesionInfo[0])).exists()){
			System.out.println("已经是最新的文件了");
			return null;
		}
		HttpURLConnection conn2 = (HttpURLConnection) new URL(vesionInfo[1]).openConnection();
		conn2.setReadTimeout(150000);
		conn2.addRequestProperty("Accept-Language", "en-US,en;q=0.8");
		conn2.addRequestProperty("User-Agent", "Mozilla");
		conn2.addRequestProperty("Referer", "google.com");
		conn2.setInstanceFollowRedirects(true);
		String size = conn2.getHeaderField("Content-Length");
		if(size!=null)update.size = Long.parseLong(size);
		
		update.in = conn2.getInputStream();
		update.lastModifyTime=conn2.getLastModified();
		update.version = vesionInfo[0].trim();
		return update;
	}else
		conn.disconnect();
 
	return null;
}
}