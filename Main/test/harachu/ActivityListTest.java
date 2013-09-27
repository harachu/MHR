package harachu;

import static org.junit.Assert.*;

import harachu.mhr.model.ActivityList;
import harachu.mhr.util.ConfigManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class ActivityListTest {

	@Before
	public void setup() {
		File listFile = new File(ActivityList.LIST_FILE);
		if(listFile.exists()){
			boolean isDeleted = listFile.delete();
			System.out.println("delete listfile at setup:"+isDeleted);
		}
		File orgFile = new File("activityList.org");
		
		boolean isCopied = TestUtil.copy(orgFile,listFile);
		System.out.println("copy orgfile:"+isCopied);
	}

	@Test
	public void testPushAndStore() throws IOException {
		ActivityList list = new ActivityList();
		list.pushAndStore("test1");
		list.pushAndStore("test2");
		list.pushAndStore("test3");
		list.store();
		
		FileReader fr = new FileReader(ActivityList.LIST_FILE);
		BufferedReader br = new BufferedReader(fr);
		assertEquals("test3",br.readLine());
		assertEquals("test2",br.readLine());
		assertEquals("test1",br.readLine());
	}
	
	@Test
	public void test20行更新() throws IOException {
		ActivityList list = new ActivityList();
		for(int i=1;i<ActivityList.listSize+1;i++){
			list.pushAndStore("test"+i);
		}
		list.store();
		
		FileReader fr = new FileReader(ActivityList.LIST_FILE);
		BufferedReader br = new BufferedReader(fr);
		for(int i=ActivityList.listSize;i<0;i--){
			assertEquals("test"+i,br.readLine());
		}
		br.close();
	}
	@Test
	public void test21行更新() throws IOException {
		ActivityList list = new ActivityList();
		for(int i=1;i<ActivityList.listSize+2;i++){
			list.pushAndStore("test"+i);
		}
		list.store();
		
		FileReader fr = new FileReader(ActivityList.LIST_FILE);
		BufferedReader br = new BufferedReader(fr);
		String line;
		int i=0;
		while((line=br.readLine())!=null){
			i++;
		}
		assertEquals(ActivityList.listSize, i);
		br.close();
	}

}
