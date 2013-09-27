package harachu.mhr.controller;

import harachu.jazz.InvalidResultException;
import harachu.jazz.WorkItemOverview;
import harachu.jazz.WorkItemRetriever;
import harachu.mhr.jazz.ActivityTranslator;
import harachu.mhr.model.ActivityList;
import harachu.mhr.model.TimeLogger;
import harachu.mhr.util.Config;
import harachu.mhr.view.MainWindow;
import harachu.mhr.view.MyTray;
import harachu.mhr.view.UnknownTimeInputWindow;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import static harachu.mhr.util.Config.*;

public class GuiController {
	private Logger logger = Logger.getLogger("mhr");
	private Display display;
	private Shell sShell;
	private MyTray tray;
	private MainWindow mainWindow;
	private UnknownTimeInputWindow unkownTimeInputindow;
	private ActivityList activityList;
	private TimeLogger timeLog;
	private int intervalTime = Integer.parseInt(Config.INTERVAL.$);
	private static final int MINUTE = 60 * 1000;



	private Runnable task = new Runnable() {
		public void run() {
			if (!sShell.isVisible()) {
				sShell.setVisible(true);
			}
		}
	};

	public GuiController() {
		activityList = new ActivityList();
		timeLog = new TimeLogger();
		display = new Display();

		display.addListener(SWT.Close, new Listener() {
			@Override
			public void handleEvent(Event arg0) {
				logger.info("Handling SWT.CLOSE event");
				timeLog.printUnkownLog();
			}
		});
		
		display.disposeExec(new Thread() {
			public void run() {
				logger.info("print unknownActivity in disposeExec");
				timeLog.printUnkownLog();
			}
		});

		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				logger.info("print unknownActivity in ShutdownHook");
				timeLog.printUnkownLog();
			}
		});
	}

	public void execute() throws IOException {


		if (!timeLog.hasResultFile()) {
			String fileName = timeLog.initResultFile();
			MessageBox msg = new MessageBox(new Shell());
			msg.setMessage("ログファイル" + fileName + "を作成しました。");
			msg.open();
		}

		if (timeLog.hasUnknwonFile()) {
			unkownTimeInputindow = new UnknownTimeInputWindow();
			unkownTimeInputindow.initialize(display, this,
					timeLog.getUnknownActivityInfo(), activityList);
			;
			timeLog.deleteUnknownFile();
		} else {
			display.timerExec(intervalTime * MINUTE, task);
		}
		mainWindow = new MainWindow();
		mainWindow.initialize(display, this, activityList);
		sShell = mainWindow.getShell();

		tray = new MyTray(display, sShell, this);

		while (!tray.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}

	public void logActivity(String value) {
		activityList.pushAndStore(value);
		timeLog.printTimeLog(value);
		turnTimerOn();
	}
	
	public void turnTimerOn(){
		display.timerExec(intervalTime * MINUTE, task);	
	}
	
	public void retrieveWorkitem(){
		MessageBox confirmBox = new MessageBox(new Shell(),SWT.OK|SWT.CANCEL);
		confirmBox.setMessage("RTC Serverからワークアイテムを取得します");
		int result = confirmBox.open();
		if(result == SWT.CANCEL){
			return;
		}
		WorkItemRetriever retriever = new WorkItemRetriever(SERVER_URL.$,PROJECT.$,USER_ID.$,PASSWORD.$);
		try {
			List<WorkItemOverview> workitemList = retriever.execute();
			activityList = ActivityTranslator.refill(activityList, workitemList);
			mainWindow.resetActivityList(activityList);
			MessageBox infoBox = new MessageBox(new Shell(),SWT.OK|SWT.ICON_INFORMATION);
			infoBox.setMessage("RTC Serverからワークアイテムを取得しました");
			infoBox.open();
		} catch (InvalidResultException e) {
			MessageBox alertBox = new MessageBox(new Shell(),SWT.NULL|SWT.ICON_ERROR);
			alertBox.setMessage(e.getMessage());
			alertBox.open();
		}
	}
}
