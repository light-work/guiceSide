package org.guiceside.support.file;

import org.guiceside.commons.Assert;

import java.io.*;





/**
 * <p>
 * IO文件操作类
 * </p>
 * @author zhenjia <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @since JDK1.5
 * @version 1.0 2008-9-17
 *
 **/
public class FileManager {
	

	

	/**
	 * 将指定路径的file对象重新命名
	 * @param directory
	 * @param dest
	 */
	public static void renameTo(String directory, File dest){
		Assert.isNotBlank(directory);
		Assert.notNull(dest);
		File file=getFile(directory);
		if(file.exists()){
			if(file.isFile()){
				file.renameTo(dest);
			}
		}
	}
	
	
	/**
	 * 
	 * 通过指定路径获取目录下的file文件列表
	 * 
	 * @param directory
	 * @return File[]
	 */
	public static File[] getFiles(String directory){
		Assert.isNotBlank(directory);
		File file=getDirectory(directory);
		if(file!=null){
			return file.listFiles();
		}
		return null;
	}
	
	
	
	
	/**
	 * 
	 * 判断文件/文件夹是否存在
	 * 
	 * @param directory
	 * @return boolean
	 */
	public static boolean isExists(String directory){
		Assert.isNotBlank(directory);
		File file = new File(directory);
		return file.exists();
	}
	
	
	
	
	
	/**
	 * 
	 * 通过指定路径获取File对象<br/>
	 * 如果不存在不会进行自动创建File对象
	 * 
	 * @param directory
	 * @return file
	 */
	public static  File getFile(String directory){
		Assert.isNotBlank(directory);
		return getFile(directory,false);
	}
	
	
	/**
	 * 
	 * 通过指定路局获取File
	 * 
	 * @param create
	 * @param directory
	 * @return 如果create 为true 则不存在file时候创建新的file
	 */
	public static File getFile(String directory,boolean create){
		Assert.isNotBlank(directory);
		File file = new File(directory);
		if (!file.exists()) {
			if(create){
				try{
					file.createNewFile();
				}catch (Exception e) {
					throw new FileManagerException("生成新文件"+directory+"时出错");
				}
			}
		}
		return file;
	}
	
	/**
	 * 
	 * 通过指定路径获取Directory对象<br/>
	 * 如果不存在不会进行自动创建Directory对象
	 * 
	 * @param directory
	 * @return file
	 */
	public static File getDirectory(String directory){
		Assert.isNotBlank(directory);
		return getDirectory(directory,false);
	}
	
	/**
	 * 
	 * 通过指定路局获取Directory
	 * 
	 * @param directory
	 * @param create
	 * @return 如果create 为true 则不存在directory时候创建新的directory
	 */
	public static File getDirectory(String directory,boolean create){
		Assert.isNotBlank(directory);
		File file = new File(directory);
		if (!file.exists()) {
			if(create){
				file.mkdirs();
			}
		}
		return file;
	}
	
	
	
	/**
	 * 
	 * 通过指定路局创建文件夹
	 * 
	 * @param directory
	 * @return file
	 */
	public static File newFloder(String directory){
		Assert.isNotBlank(directory);
		return getDirectory(directory,true);
	}
	
	/**
	 * 
	 * 通过指定路径删除文件
	 * 
	 * @param directory
	 */
	public static void deleteFile(String directory){
		Assert.isNotBlank(directory);
		File file=getFile(directory);
		file.delete();
	}
	
	/**
	 * 
	 * 通过指定路径删除文件夹
	 * 
	 * @param directory
	 */
	public static void deleteFloder(String directory){
		Assert.isNotBlank(directory);
		File file=getDirectory(directory);
		file.delete();
	}
	
	/**
	 * 
	 * 通过指定路径删除目录类所有文件<br/>
	 * 目录本身不被删除
	 * 
	 * @param directory
	 */
	public static void deleteFileInDirectory(String directory){
		Assert.isNotBlank(directory);
		File[] files;
		if(hasChilds(directory)){
			files = getFiles(directory);
			for (File f : files) {
				if (f.isFile()) {
					f.delete();
				}
			}
		}
	}
	
	/**
	 * 
	 * 通过指定路径删除目录类所有文件夹<br/>
	 * 目录本身不被删除
	 * 
	 * @param directory
	 */
	public static void deleteFolderInDirectory(String directory){
		Assert.isNotBlank(directory);
		File[] files;
		if(hasChilds(directory)){
			files = getFiles(directory);
			for (File f : files) {
				if (f.isDirectory()) {
					f.delete();
				}
			}
		}
	}
	
	/**
	 * 
	 * 通过指定路径删除目录类所有文件/文件夹<br/>
	 * 目录本身不被删除
	 * 
	 * @param directory
	 */
	public static void deleteAllInDirectory(String directory){
		Assert.isNotBlank(directory);
		File[] files;
		if(hasChilds(directory)){
			files = getFiles(directory);
			for (File f : files) {
				f.delete();
			}
		}
	}
	
	/**
	 * 查看文件是否包含子文件
	 * @param file
	 * @return boolean
	 */
	public static boolean hasChilds(File file){
		Assert.notNull(file);
		return file.listFiles().length > 0;
	}
	
	/**
	 * 查看指定路径目录下是否包含文件
	 * @param directory
	 * @return boolean
	 */
	public static boolean hasChilds(String directory){
		Assert.isNotBlank(directory);
		File[] files=getFiles(directory);
		if(files==null){
			return false;
		}
		return files.length > 0;
	}

	/**
	 * 删除指定路径目录以及父级所有目录/文件
	 * @param directory
	 */
	public static void deleteFloderAndParent(String directory){
		Assert.isNotBlank(directory);
		File file = getDirectory(directory);
		while (file.exists()&&file.getParentFile()!=null) {
			if(file.isDirectory()){
				deleteAllInDirectory(file.getPath());
				file.delete();
			}
			file = file.getParentFile();
		}
	}
	
	

	/**
	 * 
	 * copy单个文件至指定目录 如果文件重名称将重命名文件 不做覆盖<br/>
	 * 如存在相同名称文件，将以当前时间追加之原始文件名。做为新的文件创建
	 * 
	 * @param fileName
	 * @param toFolder
	 */
	public static void copyFile(String fileName,String toFolder){
		Assert.isNotBlank(fileName);
		Assert.isNotBlank(toFolder);
		File source = getFile(fileName);
		newFloder(toFolder);
		
		File target=getFile(toFolder+"/"+fileName);
		if(target.exists()){
			if(target.isFile()){
				String f_name=fileName.substring(0,fileName.lastIndexOf("."));
				String postfix=fileName.substring(fileName.lastIndexOf("."));
				f_name+=System.nanoTime();
				f_name+=postfix;
				target=getFile(toFolder+"/"+f_name);
			}
		}
		moveFile(source,target);
	}
	
	/**
	 * 
	 * copy单个文件至指定目录 如果文件重名称将覆盖文件
	 * 
	 * @param fileName
	 * @param toFolder
	 */
	public static void copyFileOverride(String fileName,String toFolder){
		Assert.isNotBlank(fileName);
		Assert.isNotBlank(toFolder);
		File source = getFile(fileName);
		newFloder(toFolder);
		File target = getFile(toFolder + "/" + fileName);
		moveFile(source,target);
	}
	
	
	/**
	 * 
	 * cut单个文件至指定目录 如果文件重名称将覆盖文件
	 * 
	 * @param fileName
	 * @param toFolder
	 */
	public static void cutFileOverride(String fileName,String toFolder){
		Assert.isNotBlank(fileName);
		Assert.isNotBlank(toFolder);
		File source = getFile(fileName);
		newFloder(toFolder);
		File target = getFile(toFolder + "/" + fileName);
		moveFile(source,target);
		source.delete();
	}
	
	
	
	
	/**
	 * 
	 * cut单个文件至指定目录 如果文件重名称将重命名文件 不做覆盖<br/>
	 * 如存在相同名称文件，将以当前时间追加之原始文件名。做为新的文件创建
	 * 
	 * @param fileName
	 * @param toFolder
	 */
	public static void cutFile(String fileName,String toFolder){
		Assert.isNotBlank(fileName);
		Assert.isNotBlank(toFolder);
		File source = getFile(fileName);
		newFloder(toFolder);
		
		File target=getFile(toFolder+"/"+fileName);
		if(target.exists()){
			if(target.isFile()){
				String f_name=fileName.substring(0,fileName.lastIndexOf("."));
				String postfix=fileName.substring(fileName.lastIndexOf("."));
				f_name+=System.nanoTime();
				f_name+=postfix;
				target=getFile(toFolder+"/"+f_name);
			}
		}
		moveFile(source,target);
		source.delete();
	}
	
	/**
	 * 
	 * 移动文件
	 * 
	 * @param source
	 * @param target
	 */
	public static void moveFile(File source,File target){
		Assert.notNull(source);
		Assert.notNull(target);
		byte[] buffer = new byte[1024];
		BufferedInputStream input = null;
		BufferedOutputStream output = null;
		try {
			input = new BufferedInputStream(new FileInputStream(source));

			output = new BufferedOutputStream(new FileOutputStream(target));
			int i = 0;
			while ((i = input.read(buffer)) != -1) {
				output.write(buffer, 0, i);
			}
			output.flush();
		} catch (Exception e) {
			e.printStackTrace();
			throw new FileManagerException("copy file: from :"+source.getName()+" to :"+target.getName()+" error");
		} finally {
			try {
				if (input != null)
					input.close();
				if (output != null)
					output.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	
	
	
}
