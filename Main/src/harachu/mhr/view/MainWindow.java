package harachu.mhr.view;

import harachu.mhr.controller.GuiController;
import harachu.mhr.model.ActivityList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class MainWindow {
	private Shell sShell = null;  //  @jve:decl-index=0:visual-constraint="7,10"
	private InputComposite inputComposite = null;

	private Display display = null;  //  @jve:decl-index=0:
	private GuiController controller = null;  //  @jve:decl-index=0:
	private ActivityList activityList = null;  //  @jve:decl-index=0:
	public Shell getShell(){
		return sShell;
	}
	public void initialize(Display display,GuiController controller,ActivityList activityList){
		this.display= display;
		this.controller = controller;
		this.activityList = activityList;
		createSShell();
	}
	
	public void resetActivityList(ActivityList activityList){
		inputComposite.resetActivityList(activityList);
	}
	
	/**
	 * This method initializes sShell
	 */
	private void createSShell() {
		sShell = new Shell(SWT.TITLE | SWT.ON_TOP | SWT.CLOSE | SWT.RESIZE);
		sShell.setText("Man-Hour Recorder");
		sShell.setLayout(new GridLayout());
		sShell.setSize(new Point(250, 55));
		sShell.addShellListener(new org.eclipse.swt.events.ShellAdapter() {
			public void shellClosed(org.eclipse.swt.events.ShellEvent e) {
				sShell.setVisible(false);
				controller.turnTimerOn();
				e.doit = false;
			}
		});
		
		createInputComposite();
		
        Rectangle clientArea = display.getClientArea();
        Rectangle sShellArea = sShell.getBounds();
        int x = clientArea.width - sShellArea.width;
        int y = clientArea.height - sShellArea.height;
		sShell.setLocation(x,y);
	}
	
	/**
	 * This method initializes inputComposite	
	 *
	 */
	private void createInputComposite() {
		inputComposite = new InputComposite(sShell, SWT.NONE | SWT.RESIZE);
		inputComposite.initialize(controller,activityList);
		inputComposite.setVisible(true);
	}

}  //  @jve:decl-index=0:visual-constraint="264,23"
