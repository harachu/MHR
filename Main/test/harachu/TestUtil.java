package harachu;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class TestUtil {
	/**
	 * ファイルコピーします。
	 *
	 * @param srcPath コピー元
	 * @param destPath コピー先
	 */
	public static boolean copy(File srcPath, File destPath) {

	    FileChannel srcChannel = null;
	    FileChannel destChannel = null;

	    try {
	        srcChannel = new FileInputStream(srcPath).getChannel();
	        destChannel = new FileOutputStream(destPath).getChannel();

	        srcChannel.transferTo(0, srcChannel.size(), destChannel);
	        return true;
	    } catch (IOException e) {
	        e.printStackTrace();
	        return false;
	    } finally {
	        if (srcChannel != null) {
	            try {
	                srcChannel.close();
	            } catch (IOException e) {
	            }
	        }
	        if (destChannel != null) {
	            try {
	                destChannel.close();
	            } catch (IOException e) {
	            }
	        }
	    }
	}

}
