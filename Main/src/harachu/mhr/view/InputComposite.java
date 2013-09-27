package harachu.mhr.view;

import harachu.mhr.controller.GuiController;
import harachu.mhr.model.ActivityList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;

public class InputComposite extends Composite {
	private Combo activityCombo = null;
	private Button inputButton = null;
	private GuiController controller = null;
	private Composite parent = null;
	private ActivityList activityList;  //  @jve:decl-index=0:

	public InputComposite(Composite parent, int style) {
		super(parent, style);
		this.parent = parent;
	}
	
	public void resetActivityList(ActivityList activityList){
		String[] activityListArray = activityList.getListArray();
		activityCombo.setItems(activityListArray);
		activityCombo.setText(activityListArray[0]);
	}

	public void initialize(GuiController controller,ActivityList activityList) {
		this.controller = controller;
		this.activityList = activityList;
		
		
		setSize(new Point(200, 25));
		GridData gridData = new GridData();
		gridData.grabExcessHorizontalSpace= true;
		gridData.horizontalAlignment = SWT.END;
		gridData.heightHint = -1;
		gridData.widthHint = -1;
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		gridLayout.verticalSpacing = 0;
		gridLayout.marginWidth = 0;
		gridLayout.marginHeight = 0;
		gridLayout.horizontalSpacing = 0;
		setLayoutData(gridData);
		setLayout(gridLayout);
		createActivityCombo();
		createButton();
	}
	/**
	 * This method initializes activityCombo	
	 *
	 */
	private void createActivityCombo() {
		GridData gridData1 = new GridData();
		gridData1.grabExcessHorizontalSpace= true;
		gridData1.horizontalAlignment =SWT.FILL;
		//gridData1.widthHint = 180;
		gridData1.minimumWidth = 180;
		activityCombo = new Combo(this, SWT.V_SCROLL);
		activityCombo.setToolTipText("アクティビティの入力");
		activityCombo.setLayoutData(gridData1);
		String[] activityListArray = activityList.getListArray();
		activityCombo.setItems(activityListArray);
		activityCombo.setText(activityListArray[0]);
		activityCombo
				.addTraverseListener(new org.eclipse.swt.events.TraverseListener() {
					public void keyTraversed(org.eclipse.swt.events.TraverseEvent e) {
						if (e.detail == SWT.TRAVERSE_RETURN) {
						    inputButton.notifyListeners(SWT.Selection, new Event());
						}
					}
				});
	}
	/**
	 * This method initializes composite	
	 *
	 */
	private void createButton() {
		GridData gridData2 = new GridData(GridData.HORIZONTAL_ALIGN_END);
		inputButton = new Button(this, SWT.NONE);
		inputButton.setText("入力");
		inputButton.setLayoutData(gridData2);
		inputButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				parent.setVisible(false);
				controller.logActivity(activityCombo.getText());
				String[] activityListArray = activityList.getListArray();
				activityCombo.setItems(activityListArray);
				activityCombo.setText(activityListArray[0]);
			}
		});
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
