package harachu.mhr.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.logging.Logger;

public class TimeLogger {
	private static boolean isPrintedUnknownActivity = false;

	private Logger logger = Logger.getLogger("mhr");
	private Calendar oldCal;
	private final String unknownFileName = "unknown.dat";
	private File unknownFile;
	private File resultFile;
	private PrintWriter out;
	private String userName = System.getProperty("user.name");

	private String ext = ".csv";

	public TimeLogger() {
		oldCal = Calendar.getInstance();
		unknownFile = new File(unknownFileName);

		SimpleDateFormat formater = new SimpleDateFormat("yyyyMM");
		String yyyyMM = formater.format(new Date());
		String resultFileName = userName + "_" + yyyyMM + ext;
		resultFile = new File(resultFileName);
	}

	private String createLog(String activity) {
		Calendar nowCal = Calendar.getInstance();
		Date nowDate = nowCal.getTime();
		Date oldDate = oldCal.getTime();

		String datePattern = "yyyy,MM,dd,HH";
		String timePattern = "HH:mm:ss";

		DateFormat dateFormater = new SimpleDateFormat(datePattern);
		DateFormat timeFormater = new SimpleDateFormat(timePattern);
		DateFormat timeFormaterGmt = new SimpleDateFormat(timePattern);
		timeFormaterGmt.setTimeZone(TimeZone.getTimeZone("GMT"));

		String nowDateStr = dateFormater.format(nowDate);
		String nowTimeStr = timeFormater.format(nowDate);
		String oldTimeStr = timeFormater.format(oldDate);
		long diffMil = nowDate.getTime() - oldDate.getTime();
		Calendar difCal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		difCal.setTimeInMillis(diffMil);
		String diffTimeStr = timeFormaterGmt.format(difCal.getTime());

		String lineStr = "" + nowDateStr + "," + oldTimeStr + "," + nowTimeStr
				+ "," + diffMil + "," + diffTimeStr + "," + activity;
		oldCal = nowCal;
		return lineStr;
	}

	public boolean deleteUnknownFile() {
		boolean isSuccess = unknownFile.delete();
		logger.info("unknownfile is deleted:" + isSuccess);
		return isSuccess;
	}

	public String[] getUnknownActivityInfo() {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(
					unknownFile));
			String line = reader.readLine();
			String[] tokens = line.split(",");
			reader.close();
			return tokens;
		} catch (IOException e) {
			throw new RuntimeException();
		}
	}

	public boolean hasUnknwonFile() {
		return unknownFile.exists();
	}

	public boolean hasResultFile() {
		return resultFile.exists();
	}

	public String initResultFile() {
		if (!resultFile.exists()) {
			String lineStr = "年,月,日,時間帯,開始,終了,秒,時間,作業項目,作業種類,作業分類";

			try {
				out = new PrintWriter(new FileWriter(resultFile, true));
				out.println(lineStr);
				out.flush();
				out.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		return resultFile.getName();
	}

	public void printTimeLog(String activity) {
		String lineStr = createLog(activity);

		try {
			out = new PrintWriter(new FileWriter(resultFile, true));
			out.println(lineStr);
			out.flush();
			out.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

	public void printUnkownLog() {
		if (!isPrintedUnknownActivity) {
			String lineStr = createLog("unknown");
			try {
				out = new PrintWriter(new FileWriter(unknownFile));
				out.println(lineStr);
				out.flush();
				out.close();
			} catch (IOException e) {
				throw new RuntimeException();
			}
			isPrintedUnknownActivity = true;
		}else{
			logger.info("already printed UnknownActivityLog");
		}
	}
}
