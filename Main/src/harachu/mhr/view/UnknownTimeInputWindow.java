package harachu.mhr.view;

import harachu.mhr.controller.GuiController;
import harachu.mhr.model.ActivityList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class UnknownTimeInputWindow {

	private Shell sShell = null;  //  @jve:decl-index=0:visual-constraint="0,0"
	private Composite MessageComposite = null;
	private Label messageLabel = null;
	private Label dateLabel = null;
	private Label timeLabel = null;
	private InputComposite inputComposite = null;
	private GuiController controller;  //  @jve:decl-index=0:
	private Display display;
	
	private String[] unknownActivityInfo;
	private ActivityList activityList;  //  @jve:decl-index=0:

	
	public void initialize(Display display,GuiController controller,String[] unknownActivityInfo,ActivityList activityList){
		this.display= display;
		this.controller = controller;
		this.unknownActivityInfo = unknownActivityInfo;
		this.activityList = activityList;
		createSShell();
	}
	public Shell getShell(){
		return sShell;
	}

	/**
	 * This method initializes sShell
	 */
	public void createSShell() {
		GridLayout gridLayout1 = new GridLayout();
		gridLayout1.numColumns = 1;
		sShell = new Shell(SWT.TITLE | SWT.ON_TOP | SWT.RESIZE);
		sShell.setText("Man-Hour Recorder");
		createMessageComposite();
		createInputComposite();
		sShell.setLayout(gridLayout1);
		sShell.setSize(new Point(250, 100));
		
        Rectangle clientArea = display.getClientArea();
        Rectangle sShellArea = sShell.getBounds();
        int x = clientArea.width - sShellArea.width;
        int y = clientArea.height - sShellArea.height;
		sShell.setLocation(x,y);
		sShell.setVisible(true);
	}

	/**
	 * This method initializes MessageComposite	
	 *
	 */
	private void createMessageComposite() {


		FillLayout fillLayout = new FillLayout();
		fillLayout.type = org.eclipse.swt.SWT.VERTICAL;
		GridData gridData = new GridData();
		gridData.widthHint = 190;

		MessageComposite = new Composite(sShell, SWT.NONE);
		MessageComposite.setLayoutData(gridData);
		MessageComposite.setLayout(fillLayout);
		
		messageLabel = new Label(MessageComposite, SWT.NONE);
		messageLabel.setText("前回終了時に不明な作業があります。");


		dateLabel = new Label(MessageComposite, SWT.NONE);
		String year = unknownActivityInfo[0];
		String month = unknownActivityInfo[1];
		String day = unknownActivityInfo[2];
		String fromTime = unknownActivityInfo[4];
		String toTime = unknownActivityInfo[5];
		dateLabel.setText(year+"/"+month+"/"+day);
		
		timeLabel = new Label(MessageComposite, SWT.NONE);
		timeLabel.setText(fromTime+"-"+toTime);
	}

	/**
	 * This method initializes inputComposite	
	 *
	 */
	private void createInputComposite() {
		inputComposite = new InputComposite(sShell, SWT.NONE);
		inputComposite.initialize(controller,activityList);
		inputComposite.setVisible(true);
	}
}
