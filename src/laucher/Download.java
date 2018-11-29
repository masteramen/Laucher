package laucher;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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
		JOptionPane.showMessageDialog(null, JarTool.getUpdateDir().getAbsolutePath()+"已经存在，并且不是目录,更新终止！", "错误提示",JOptionPane.ERROR_MESSAGE);  
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
public static Update checkUpdate( ) throws IOException {


	if(!JarTool.makeSureUpdateSuccess()){
		JOptionPane.showMessageDialog(null, JarTool.getUpdateDir().getAbsolutePath()+"已经存在，并且不是目录,更新终止！", "错误提示",JOptionPane.ERROR_MESSAGE);  
		return null;
	}
	
	
	String url = "http://www.jfox.info/update/"+JarTool.getJarModuleName()+"/"+JarTool.getJarModuleName()+".jar"+"?_t="+new Date().getTime();
 
	URL obj = new URL(url);
	HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
	conn.setReadTimeout(150000);
	conn.addRequestProperty("Accept-Language", "en-US,en;q=0.8");
	conn.addRequestProperty("User-Agent", "Mozilla");
	conn.addRequestProperty("Referer", "google.com");
	conn.setInstanceFollowRedirects(false);

	if (conn.getLastModified()>JarTool.getLatestJarModifiyDate()) {
 
		Update update = new Update();		
		update.url = url.split("\\?")[0];
		
		if(new File(JarTool.getDownloadUpdateJarFileName(url)).exists()){
			System.out.println("已经是最新的文件了");
			File f=new File(JarTool.getDownloadUpdateJarFileName(url));
			f.renameTo(new File(f.getParentFile().getAbsoluteFile()+"/"+f.getName().substring(0,f.getName().length()-4)+"-"+f.lastModified()+".jar"));
		}
		
		String size = conn.getHeaderField("Content-Length");
		if(size!=null)update.size = Long.parseLong(size);

		update.in = conn.getInputStream();
		update.lastModifyTime=conn.getLastModified();
		return update;
	}else
		conn.disconnect();
 
	return null;
}
}