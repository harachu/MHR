package harachu.mhr.view;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.layout.GridData;

public class TabWindow {

	private Shell sShell = null;
	private TabFolder tabFolder = null;
	/**
	 * This method initializes sShell
	 */
	private void createSShell() {
		GridLayout gridLayout = new GridLayout();
		sShell = new Shell(SWT.TITLE | SWT.ON_TOP | SWT.CLOSE);
		sShell.setText("Shell");
		sShell.setLayout(gridLayout);
		createTabFolder();
		sShell.setSize(new Point(200,100));
	}
	/**
	 * This method initializes tabFolder	
	 *
	 */
	private void createTabFolder() {
		GridData gridData = new GridData();
		gridData.widthHint = 175;
		gridData.heightHint = 40;
		tabFolder = new TabFolder(sShell, SWT.NONE);
		tabFolder.setLayout(new GridLayout());
		tabFolder.setLayoutData(gridData);
		TabItem item1 = new TabItem(tabFolder,SWT.NULL);
		item1.setText("Plain");

		TabItem item2 = new TabItem(tabFolder,SWT.NULL);
		item2.setText("Jazz");
	}
	
	public TabWindow(){
		Display display = new Display();
		createSShell();
		sShell.setVisible(true);

		while (!sShell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}
	
	public static void main(String args[]){
		new TabWindow();
	}

}
