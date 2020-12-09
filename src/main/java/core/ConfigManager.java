package core;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;

public class ConfigManager {

	private static String env;
	/**
	 * This field - init is used for Storing Initialization results.
	 */
	private static boolean init = false;
	/**
	 *
	 */
	private static Properties properties = new PropertyConfiguration();

	public static String getEnv() {
		return env;
	}

	public static void setEnv(String env) {
		Log.info("setting env value ----" + env);
		ConfigManager.env = env;
	}

	/**
	 * @param props
	 */
	public static void addFileInProps(Properties props) {
		for (Object key : props.keySet()) {
			ConfigManager.properties.setProperty((String) key, (String) props.get(key));
		}

	}

	/**
	 * @param fileName
	 */
	private static void addFileInProps(String fileName) {
		InputStream file;
		try {
			if (fileName.startsWith("classpath:")) {
				fileName = fileName.replace("classpath:", "");
				file = ClassLoader.getSystemClassLoader().getResourceAsStream(fileName);
			} else {
				file = new FileInputStream(fileName);
			}
			ConfigManager.properties.load(file);
		} catch (Exception e) {
			Log.debug("Can not find Property file [" + fileName + "] And it will be ignored from Property Manager");
			e.printStackTrace();
		}

	}

	/**
	 * @return
	 */
	public static Map<String, String> getAllPropertiesAsMap() {
		Map<String, String> propMap = new HashMap<String, String>();

		for (String key : ConfigManager.properties.stringPropertyNames()) {
			propMap.put(key, ConfigManager.properties.getProperty(key));
		}

		// return from here.
		return propMap;
	}

	/**
	 * Gets all the property values fetched byConfigManager and returns as map
	 *
	 * @param frameworkPropertyFileName Framework.properties file location
	 * @param cameraPropertyFileName    Camera.properties file location
	 * @return Map&lt;String, String&gt; of properties
	 */
	public static Map<String, String> getAllPropertiesAsMap(String frameworkPropertyFileName,
			String cameraPropertyFileName) {
		if (ConfigManager.properties.isEmpty()) {
			ConfigManager.addFileInProps(frameworkPropertyFileName);
			ConfigManager.addFileInProps(cameraPropertyFileName);
		}

		Map<String, String> propMap = new HashMap<String, String>();

		for (String key : ConfigManager.properties.stringPropertyNames()) {
			propMap.put(key, ConfigManager.properties.getProperty(key));
		}

		// return from here.
		return propMap;
	}

	public static String get(String key) {
		ConfigManager.init();
		return Optional.ofNullable(Strings.emptyToNull(System.getProperty(key)))
				.orElse(ConfigManager.properties.getProperty(key));
	}

	/**
	 * This method should be overridden from child class; if needs to use different
	 * file set.
	 */
	private static void init() {
		if (init)
			return;
		ConfigManager.init = true;
		final Pattern rexpPropFilename = Pattern.compile(".*_pset.properties");
		final Path propsDir = Paths.get("src/main/resources/config");

		filterFiles(propsDir, rexpPropFilename).forEach(filePath -> addFileInProps(filePath.toString()));
	}

	private static List<Path> filterFiles(Path dir, Pattern filenamePattern) {
		try {
			return Files.list(dir).filter(e -> e.getFileName().toString().matches(filenamePattern.pattern()))
					.collect(Collectors.toList());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Collections.emptyList();
	}

	public static List<String> getAsList(String str, String sep) {
		List<String> retList = new ArrayList<String>();
		if (str != null) {
			for (String val : str.split(sep, -2)) {
				retList.add(val);
			}
		}
		// return from here
		return retList;
	}

	/**
	 * @param key
	 * @param value
	 */
	public static void set(String key, String value) {
		set(key, value, null);
	}

	/**
	 * @param key
	 * @param value
	 */
	public static void set(String key, String value, String comment) {
		ConfigManager.init();
		ConfigManager.properties.setProperty(key, value);
	}

	public static HashMap<String, String> getAllPropertiesAsMapFromFileForDesiredCaps(
			String desiredCapabilityFileName) {
		HashMap<String, String> propMap = null;
		try {
			propMap = new ObjectMapper().readValue(new File(desiredCapabilityFileName),
					new TypeReference<HashMap<String, String>>() {
					});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return propMap;

	}

}
