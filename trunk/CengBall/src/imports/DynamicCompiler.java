package imports;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticListener;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import utils.CBLogger;
import utils.FileUtils;
import utils.UserSettings;

/**
 * Dynamic java class compiler and executer
 * compile dynamic java source code,
 * instantiate instance of the class, and call method of the class
 * 
 */
public class DynamicCompiler
{
	private static final Logger LOGGER = CBLogger.getInstance().getLogger();
	private String classOutputFolder;
	private String sourcePath;
	
	public DynamicCompiler(String sourcePath) {
		this.sourcePath = sourcePath;
		this.classOutputFolder = FileUtils.getImportsDirectory();
	}

	public static class MyDiagnosticListener implements DiagnosticListener<JavaFileObject> {
		public void report(Diagnostic<? extends JavaFileObject> diagnostic) {
			LOGGER.log(Level.WARNING, "Cannot compile file : " + System.getProperty("line.separator") + 
					"Line Number->" + diagnostic.getLineNumber() + System.getProperty("line.separator") + 
					"code->" + diagnostic.getCode() + System.getProperty("line.separator") + 
					"Message->" + diagnostic.getMessage(Locale.ENGLISH) + System.getProperty("line.separator") + 
					"Source->" + diagnostic.getSource() + System.getProperty("line.separator"));
		}
	}

	/** java File Object represents an in-memory java source file <br>
	 * so there is no need to put the source file on hard diskï¿½ **/
	public static class InMemoryJavaFileObject extends SimpleJavaFileObject {
		private String contents = null;

		public InMemoryJavaFileObject(String className, String contents) throws Exception {
			super(URI.create("string:///" + className.replace('.', '/') + Kind.SOURCE.extension), Kind.SOURCE);
			this.contents = contents;
		}

		public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
			return contents;
		}
	}
	
	//for testing purposes
	private JavaFileObject getSource(String path, String classname) {
		JavaFileObject source = null;
		
		try {
			source = new InMemoryJavaFileObject(classname, FileUtils.readFile(path, StandardCharsets.UTF_8));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return source;
	}

	/** compile your files by JavaCompiler */
	public boolean compile() {
		try {
			JavaFileObject[] fileler = new JavaFileObject[1];
			File file = new File(sourcePath);
			fileler[0] = getSource(sourcePath, file.getName().substring(0, file.getName().length()-5));
			Iterable<? extends JavaFileObject> files = Arrays.asList(fileler);

			UserSettings.loadSettings();
			if (!UserSettings.jdkPath.equals(""))
				System.setProperty("java.home", UserSettings.jdkPath);
			else 
				LOGGER.log(Level.WARNING, "JDK Path missing.");
			
			JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

			//for compilation diagnostic message processing on compilation WARNING/ERROR
			MyDiagnosticListener c = new MyDiagnosticListener();
			
			StandardJavaFileManager fileManager = compiler.getStandardFileManager(c, Locale.ENGLISH, null);
			
			//specify classes output folder
			String classpath1 = getClass().getResource("../CengBallLib.jar").getPath();
			Iterable<String> options = Arrays.asList("-d", classOutputFolder,"-classpath", classpath1);
			
			JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, c, options, null, files);
			return task.call();
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Compilation error: " + e);
		} 
		return false;
	}

	
	public static Object loadObject(String className) {
		// Create a File object on the root of the directory
		// containing the class file
		File file = new File(FileUtils.getImportsDirectory());
		URLClassLoader loader = null;
		Object instance = null;
		
		try {
			// Convert File to a URL
			URL url = file.toURI().toURL();
			URL[] urls = new URL[] { url };
			
			// Create a new class loader with the directory
			loader = new URLClassLoader(urls);

			// Load in the class; Class.childclass should be located in
			Class<?> thisClass = loader.loadClass(className);

			//Class<?>[] params = {};
			//Object[] paramsObj = {};
			instance = thisClass.newInstance();
		} catch (MalformedURLException e) {
			LOGGER.log(Level.SEVERE, "MalformedURLException : " + e);
		} catch (ClassNotFoundException e) {
			LOGGER.log(Level.SEVERE, "ClassNotFoundException : " + e);
		} catch(Exception e) {
			LOGGER.log(Level.SEVERE, "Exception : " + e);
		} finally {
			try {
                assert loader != null;
                loader.close();
			} catch (IOException e) {
				LOGGER.log(Level.SEVERE, "IOException : " + e);
			} catch (NullPointerException e) {
				LOGGER.log(Level.SEVERE, "NullPointerException : " + e);
            }
		}
		
		return instance;
	}
}