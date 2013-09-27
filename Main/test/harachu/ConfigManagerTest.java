package harachu;

import static org.junit.Assert.*;

import harachu.mhr.util.Config;
import harachu.mhr.util.ConfigManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.FileChannel;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ConfigManagerTest {
	
	@Before
	public void setUp(){
		File iniFile = new File(ConfigManager.INI_FILE);
		if(iniFile.exists()){
			boolean isDeleted = iniFile.delete();
			System.out.println("delete inifile at setup:"+isDeleted);
		}
		File orgFile = new File("manhourRecorder.org");
		
		boolean isCopied = TestUtil.copy(orgFile,iniFile);
		System.out.println("copy orgfile:"+isCopied);
		
	}
	
	@After
	public void tearDown(){
//		File iniFile = new File(ConfigManager.INI_FILE);
//		if(iniFile.exists()){
//			boolean isDeleted = iniFile.delete();
//			System.out.println("delete inifile at tearDown:"+isDeleted);
//		}
	}

	@Test
	public void testGetProperty() {
		String INTERVAL = Config.INTERVAL.$;
		assertEquals("30",INTERVAL);
		String value = ConfigManager.getProperty(Config.INTERVAL);
		assertEquals("30",value);
	}

	@Test
	public void testConfig() {
		assertEquals("30",Config.INTERVAL.$);
	}

	
	@Test
	public void testファイル値更新() {
		Config.INTERVAL.updateValue("40");
		
		assertEquals("40",Config.INTERVAL.$);
		
		ConfigManager.load();
		assertEquals("40",Config.INTERVAL.$);
		
	}
	
	


}
