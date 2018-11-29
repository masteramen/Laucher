package laucher;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.eclipse.jdt.internal.jarinjarloader.RsrcURLStreamHandlerFactory;


public class Laucher {
	    private static Method addURL = initAddMethod();  
	     static URLClassLoader system = (URLClassLoader) ClassLoader.getSystemClassLoader();  
	     private static final Method initAddMethod() {  
	        try {  
	            Method add = URLClassLoader.class  
	                .getDeclaredMethod("addURL", new Class[] { URL.class });  
	            add.setAccessible(true);  
	            return add;  
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        }  
	        return null;  
	    }  
	    
	    /** 初始化方法 */  
	    private static final void addURL(ClassLoader classLoader,URL[] urls) {  
	        try {   
	            for(URL url:urls)
	            {
	            	addURL.invoke(classLoader, url);
	            }
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        }  
	    }  
		private static class ManifestInfo {
			String rsrcMainClass;
			String[] rsrcClassPath;
		}

	  
	    private static final void loopFiles(File file, List<File> files) {  
	        if (file.isDirectory()) {  
	            File[] tmps = file.listFiles();  
	            for (File tmp : tmps) {  
	                loopFiles(tmp, files);  
	            }  
	        } else {  
	            if (file.getAbsolutePath().endsWith(".jar") || file.getAbsolutePath().endsWith(".zip")) {  
	                files.add(file);  
	            }  
	        }  
	    }  
	  
	    public static final void loadJarFile(File file,String[] args) {  
	    	 try {
				addURL(system, new URL[]{file.toURI().toURL()});
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
	    }  
	  
	    public static void main(final String[] args) {	
	        try {
	            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	          }
	          catch (Exception e) {
	            e.printStackTrace();
	          }
	    	final SplashScreen screen = new SplashScreen(null);
	        screen.setLocationRelativeTo(null);
	        screen.setProgressMax(100);
	        screen.setScreenVisible(true);
	        screen.setProgress("正在检查更新",0);
	        new Thread()
	        {
	        		private int percent;
	        		private boolean stop=false;
	    			@Override
	    			public void run() {
	    				try {
	    					screen.close.addMouseListener(new MouseAdapter()  
	    					{  
	    					    public void mouseClicked(MouseEvent e)  
	    					    {  
	    					    	stop=true;
	    					    	screen.setVisible(false);
	    					    }  
	    					}); 
	    					 Update update=Download.checkUpdate();
	    					if(update!=null&&update.in!=null)
	    					{
	    	                    byte[] buffer = new byte[1024];

	    	                    int size = 0;
	    						 BufferedInputStream bis = new BufferedInputStream(update.in);
	    		                 FileOutputStream fos = new FileOutputStream(JarTool.getDownloadUpdateJarTmpFileName(update.url));
	    		                 long rSize =0;
	    		                 while ((size = bis.read(buffer)) != -1) {
	    		                	    fos.write(buffer, 0, size);
	    	                            fos.flush();
	    	                            rSize+=size;
	    	                            percent=(int)(rSize*100/update.size);
	    	                            if(stop)break;
	    	                            SwingUtilities.invokeLater(new Runnable() {
	    									public void run() {
	    										// TODO Auto-generated method stub
	    			                            screen.setProgress("已经下载"+percent+"%",percent);
	    									}
	    								});
	    		                 }
	    		                 fos.close();
	    		                 bis.close();
	    		                 try {
	 	                            SwingUtilities.invokeLater(new Runnable() {
	 	    									public void run() {
	 	    										// TODO Auto-generated method stub
	 	    			                            screen.setProgress("已经更新完成，正在启动程序.",percent);
	 	    									}
	 	    								});
	 	                           JarTool.moveFile(new File(JarTool.getDownloadUpdateJarTmpFileName(update.url)),new File(JarTool.getDownloadUpdateJarFileName(update.url)));
	 	                          new File(JarTool.getDownloadUpdateJarFileName(update.url)).setLastModified(update.lastModifyTime);
	 	                           Thread.sleep(1000);
								} catch (InterruptedException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
	    					}
	    				} catch (IOException e) {
	    					// TODO Auto-generated catch block
	    					e.printStackTrace();
	    					JOptionPane.showMessageDialog(null, "发生错误："+e.getMessage(), "发生错误",JOptionPane.ERROR_MESSAGE);
	    				}
	    				screen.dispose();
	    				lauch(args);
	    			}
	    		
	        }.start();
		
		}

		private static void lauch(String[] args) {
			File file = JarTool.getLatestJarFile();
			if(file==null)
			{
				JOptionPane.showMessageDialog(null, "程序文件缺少启动的文件，启动终止。", "错误提示",JOptionPane.ERROR_MESSAGE);  
				System.exit(1);
			}
	        try {  
	        	addURL(system, new URL[]{ file.toURI().toURL()});
	    		ClassLoader cl = Thread.currentThread().getContextClassLoader();
	    		try{
	    		RsrcURLStreamHandlerFactory handler = new RsrcURLStreamHandlerFactory(cl);
				URL.setURLStreamHandlerFactory(handler);
	    		}catch(Throwable t){}
	            JarFile jarFile = new JarFile(file);  
	            // Get the manifest  
	            Manifest manifest = jarFile.getManifest();  
	            // Close the JAR file  
	            jarFile.close();
				ManifestInfo result = new ManifestInfo();
				Attributes mainAttribs = manifest.getMainAttributes();
				result.rsrcMainClass = mainAttribs.getValue(JIJConstants.REDIRECTED_MAIN_CLASS_MANIFEST_NAME); 
				String rsrcCP = mainAttribs.getValue(JIJConstants.REDIRECTED_CLASS_PATH_MANIFEST_NAME); 
				if (rsrcCP == null)
					rsrcCP = JIJConstants.DEFAULT_REDIRECTED_CLASSPATH; 
				result.rsrcClassPath = splitSpaces(rsrcCP);
				if ((result.rsrcMainClass != null) && !result.rsrcMainClass.trim().equals(""))
				{
					ManifestInfo mi = result;
					URL[] rsrcUrls = new URL[mi.rsrcClassPath.length];
					for (int i = 0; i < mi.rsrcClassPath.length; i++) {
						String rsrcPath = mi.rsrcClassPath[i];
						if (rsrcPath.endsWith(JIJConstants.PATH_SEPARATOR)) 
							rsrcUrls[i] = new URL(JIJConstants.INTERNAL_URL_PROTOCOL_WITH_COLON + rsrcPath); 
						else
							rsrcUrls[i] = new URL(JIJConstants.JAR_INTERNAL_URL_PROTOCOL_WITH_COLON + rsrcPath + JIJConstants.JAR_INTERNAL_SEPARATOR);    
					}
					ClassLoader jceClassLoader = new URLClassLoader(rsrcUrls, null);
					Thread.currentThread().setContextClassLoader(jceClassLoader);
					Class c = Class.forName(mi.rsrcMainClass, true, jceClassLoader);

					Method main = c.getMethod(JIJConstants.MAIN_METHOD_NAME, new Class[]{args.getClass()}); 
					main.invoke((Object)null, new Object[]{args});
				}
	        } catch (Exception e) {  
	            e.printStackTrace();  
				JOptionPane.showMessageDialog(null, "程序发生错误:"+e.getMessage()+"，启动终止。", "错误提示",JOptionPane.ERROR_MESSAGE);  
				System.exit(1);
	        }
		}
	    


		private static String[] splitSpaces(String line) {
			if (line == null) 
				return null;
			List result = new ArrayList();
			int firstPos = 0;
			while (firstPos < line.length()) {
				int lastPos = line.indexOf(' ', firstPos);
				if (lastPos == -1)
					lastPos = line.length();
				if (lastPos > firstPos) {
					result.add(line.substring(firstPos, lastPos));
				}
				firstPos = lastPos+1; 
			}
			return (String[]) result.toArray(new String[result.size()]);
		}
}
class JIJConstants {
	
	static final String REDIRECTED_CLASS_PATH_MANIFEST_NAME  = "Rsrc-Class-Path";  //$NON-NLS-1$
	static final String REDIRECTED_MAIN_CLASS_MANIFEST_NAME  = "Rsrc-Main-Class";  //$NON-NLS-1$
	static final String DEFAULT_REDIRECTED_CLASSPATH         = "";  //$NON-NLS-1$
	static final String MAIN_METHOD_NAME                     = "main";  //$NON-NLS-1$
	static final String JAR_INTERNAL_URL_PROTOCOL_WITH_COLON = "jar:rsrc:";  //$NON-NLS-1$
	static final String JAR_INTERNAL_SEPARATOR               = "!/";  //$NON-NLS-1$
	static final String INTERNAL_URL_PROTOCOL_WITH_COLON     = "rsrc:";  //$NON-NLS-1$
	static final String INTERNAL_URL_PROTOCOL                = "rsrc";  //$NON-NLS-1$
	static final String PATH_SEPARATOR                       = "/";  //$NON-NLS-1$
	static final String CURRENT_DIR                          = "./";  //$NON-NLS-1$
	static final String UTF8_ENCODING                        = "UTF-8";  //$NON-NLS-1$
}
