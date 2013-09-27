package harachu.mhr.util;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Properties;

public class ConfigManager {
	public static final String INI_FILE = "manhourRecorder.ini";
	private static Properties properties = new Properties();
	private static File propFile = new File(INI_FILE);
	
	static{
		if(propFile.exists()){
			load();
		}
	}
	
	
	public static String getProperty(Config config) {
		return properties.getProperty(config.name());
	}
	public static String getProperty(Config config,String initial) {
		String value = properties.getProperty(config.name());
		if(value==null){
			properties.setProperty(config.name(), initial);
			return initial;
		}
		return value;
	}
	
	public static void setProperty(Config config,String value) {
		properties.setProperty(config.name(),value);
	}

	public static void load() {
		Reader reader;
		try {
			reader = new FileReader(propFile);
			properties.load(reader);
			reader.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static void store(){
		Writer writer;
		try {
			writer = new FileWriter(propFile);
			String comments = "configs for ManHourRecorder";
			properties.store(writer, comments);
			writer.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
