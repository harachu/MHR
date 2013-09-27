package harachu.mhr.model;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.util.LinkedList;

/**
 * @author JP203998
 *�A�N�e�B�r�e�B���X�g�N���X<br>
 *�A�N�e�B�r�e�B���X�g��ێ����邱�Ƃ�Ӗ��Ƃ���
 *LinkedList�N���X������f�[�^�N���X�Ƃ��Ďg�p���Ă���B
 */
public class ActivityList {
	//�B���List
	private static LinkedList<String> list;
	public static final int listSize = 20;
	public static final String LIST_FILE = "activityList.txt";
	public File listFile = new File(LIST_FILE);

	public ActivityList() {
		if (list == null) {
			list = new LinkedList<String>();
			synchronized (list) {
				if (!listFile.exists()) {
					list.add("*");
					store();
				} else {
					load();
				}
			}
		}
	}

	public String[] getListArray() {
		synchronized (list) {
			String[] a = {};
			return list.toArray(a);
		}
	}

	public void load() {
		synchronized (list) {
			try {

				LineNumberReader br = new LineNumberReader(new FileReader(
						listFile));
				String line;
				while ((line = br.readLine()) != null) {
					list.addLast(line);
					if (br.getLineNumber() == 20) {
						break;
					}
				}
				if (list.size() == 0) {
					list.add("*");
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	public void pushAndStore(String value) {
		synchronized (list) {
			list.remove(value);
			list.push(value);
			while (list.size() > listSize) {
				list.removeLast();
				;
			}
			store();
		}
	}

	public void store() {
		synchronized (list) {
			try {
				PrintWriter pw = new PrintWriter(new FileWriter(listFile));
				for (String line : list) {
					pw.println(line);
				}
				pw.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

}
