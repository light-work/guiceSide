package org.guiceside.commons.lang;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 
 * <p>
 *  声明 此辅助类来源于(JRest4Guice) 版权归作者所有 借此一用 谢谢!
 * </p>
 */
public class ClassUtils {

	private static final String PROTOCOL_FILE = "file";

	private static final String PROTOCOL_JAR = "jar";

	private static final String PREFIX_FILE = "file:";

	private static final String JAR_URL_SEPERATOR = "!/";

	private static final String CLASS_FILE = ".class";

	private static final  Logger log = Logger.getLogger(ClassUtils.class);

	public static List<Class<?>> getClasses(String packages) {
		List<Class<?>> list = new ArrayList<Class<?>>();
		Enumeration<URL> en = null;
		try {
			en = ClassUtils.class.getClassLoader().getResources(
					nameToPath(packages));
			while (en.hasMoreElements()) {
				URL url = en.nextElement();
				if (PROTOCOL_FILE.equals(url.getProtocol())) {
					File root = new File(url.getFile());
					findInDirectory(list, root, root, packages);
				} else if (PROTOCOL_JAR.equals(url.getProtocol()))
					findInJar(list, getJarFile(url), packages);
			}
		} catch (IOException e) {
			log.error("Invalid package:" + packages, e.getCause());
		}
		return list;
	}

	public static File getJarFile(URL url) {
		String file = url.getFile();
		if (file.startsWith(PREFIX_FILE))
			file = file.substring(PREFIX_FILE.length());
		int end = file.indexOf(JAR_URL_SEPERATOR);
		if (end != (-1))
			file = file.substring(0, end);
		return new File(file);
	}

	private static void findInDirectory(List<Class<?>> results, File rootDir,
			File dir, String packageName) {
		File[] files = dir.listFiles();
		String rootPath = rootDir.getPath();
		for (File file : files)
			if (file.isFile()) {
				String classFileName = file.getPath();
				if (classFileName.endsWith(CLASS_FILE)) {
					String className = classFileName.substring(rootPath
							.length()-packageName.length(), classFileName.length()
							- CLASS_FILE.length());

					try {
						Class<?> clz = Class.forName(pathToName(className));
						results.add(clz);
					} catch (ClassNotFoundException e) {
						log.error("Can not load Class:" + className, e.getCause());
					}
				}
			} else if (file.isDirectory())
				findInDirectory(results, rootDir, file, packageName);
	}

	private static void findInJar(List<Class<?>> results, File file,
			String packageName) {
		JarFile jarFile = null;
		String packagePath = nameToPath(packageName) + "/";
		try {
			jarFile = new JarFile(file);
			Enumeration<JarEntry> en = jarFile.entries();
			while (en.hasMoreElements()) {
				JarEntry je = en.nextElement();
				String name = je.getName();
				if (name.startsWith(packagePath) && name.endsWith(CLASS_FILE)) {
					String className = name.substring(0, name.length()
							- CLASS_FILE.length());
					try {
						Class<?> clz = Class.forName(pathToName(className));
						results.add(clz);
					} catch (ClassNotFoundException e) {
						log.error("Can not load Class:" + className, e.getCause()); 
					}
				}
			}
		} catch (IOException e) {
			log.error("Can not load Jar File:" + file.getName(), e);
		} finally {
			if (jarFile != null)
				try {
					jarFile.close();
				} catch (IOException e) {
				}
		}
	}

	/**
	 * 将类名的字符串形式转换为路径表现形式
	 * 
	 * @param className
	 *            类名
	 * @return 路径的字符串
	 */
	private static String nameToPath(String className) {
		return className.replace('.', '/');
	}

	/**
	 * 将路径转换为类的字符串表现形式
	 * 
	 * @param path
	 * @return 类名的字符串形式
	 */
	private static String pathToName(String path) {
		return path.replace('/', '.').replace('\\', '.');
	}

	public static List<Method> getSortedMethodList(Class<?> clazz) {
		List<Method> methods = Arrays.asList(clazz.getDeclaredMethods());
		Collections.sort(methods, new Comparator<Method>() {
			public int compare(Method m1, Method m2) {
				String lower1 = m1.toString().toLowerCase();
				String lower2 = m2.toString().toLowerCase();
				return lower1.compareTo(lower2);
			}
		});

		return methods;
	}
}