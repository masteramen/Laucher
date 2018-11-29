package laucher;

import java.io.File;  
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
/** 
 * 获取打包后jar的路径信息 
 * @author Administrator 
 *  2011-01-16 13:53:12 
 */  
public class JarTool {  
    //获取jar绝对路径  
    public static String getJarPath(){  
        File file = getFile();  
        if(file==null)return null;  
         return file.getAbsolutePath();  
    }  
    //获取jar目录  
    public static String getJarDir() {  
        File file = getFile();  
        if(file==null)return null;  
         return getFile().getParent();  
    }  
    //获取jar包名  
    public static String getJarName() {  
        File file = getFile();  
        if(file==null)return null;  
        return getFile().getName();  
    }  
    public static String getJarModuleName() {  
        File file = getFile();  
        if(file==null)return null;  
        return file.getName().substring(0,file.getName().length()-4);  
    }  
    private static File getFile() {  
        //关键是这行...  
        String path = JarTool.class.getProtectionDomain().getCodeSource().getLocation().getFile(); 

        try{  
            path = java.net.URLDecoder.decode(path, "UTF-8");//转换处理中文及空格  
        }catch (java.io.UnsupportedEncodingException e){  
            return null;  
        }  
        return new File(path);  
    }

	public static void sort(File[] array) {
		File temp = null;
		boolean condition = false;
		for (int i = 0; i < array.length; i++) {
			for (int j = array.length - 1; j > i; j--) {
				condition = array[j].lastModified() > array[j - 1]
						.lastModified();
				if (condition) {
					temp = array[j];
					array[j] = array[j - 1];
					array[j - 1] = temp;
				}
			}
		}
	}
    
	public static File getLatestJarFile() {
		// TODO Auto-generated method stub
		File ssDir = new File(JarTool.getJarDir()+"/"+JarTool.getJarModuleName());
		if(ssDir.exists()&&ssDir.isDirectory())
		{
			File[] files = ssDir.listFiles();
			sort(files);
			String startName=JarTool.getJarModuleName();
			String fileName;
			for(File f:files)
			{
				
				fileName =f.getName();
				if(fileName.startsWith(startName)&&fileName.endsWith(".jar"))
				{
					return f;
				}
			}
		}
		return null;
	}
	public static File getUpdateDir()
	{
		return  new File(JarTool.getJarDir()+"/"+JarTool.getJarModuleName());

	}
	public static boolean makeSureUpdateSuccess() {
		// TODO Auto-generated method stub
		File updateDir = getUpdateDir();
		if(updateDir.exists()&&!updateDir.isDirectory())return false;
		else updateDir.mkdirs();
		return true;
	}
	public static String getLatestJarVersion() {
		// TODO Auto-generated method stub
		File latestFile = getLatestJarFile();
		if(latestFile==null)return "";
		return latestFile.getName().substring(getJarModuleName().length());
	}
	public static long getLatestJarModifiyDate() {
		// TODO Auto-generated method stub
		File latestFile = getLatestJarFile();
		if(latestFile==null)return 0;
		return latestFile.lastModified();
	}
	public static String getDownloadUpdateJarTmpFileName(String url) {
		// TODO Auto-generated method stub
		return getUpdateDir().getAbsolutePath()+"/"+url.substring(url.lastIndexOf('/'))+".tmp";
	}  
	public static String getDownloadUpdateJarFileName(String url) {
		// TODO Auto-generated method stub
		return getUpdateDir().getAbsolutePath()+"/"+url.substring(url.lastIndexOf('/'));
	}
	public static void moveFile(File afile, File bfile) throws IOException {
		// TODO Auto-generated method stub
		InputStream inStream = null;
		OutputStream outStream = null;

	    inStream = new FileInputStream(afile);
	    outStream = new FileOutputStream(bfile);
	    byte[] buffer = new byte[1024];
	    int length;
	    while ((length = inStream.read(buffer)) > 0){

	    	outStream.write(buffer, 0, length);
	    }
	    inStream.close();
	    outStream.close();
	    afile.delete();

	
	}    
}  
