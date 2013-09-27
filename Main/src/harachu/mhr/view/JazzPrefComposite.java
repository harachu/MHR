package harachu.mhr.view;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridData;

public class JazzPrefComposite extends Composite {

	private Label serverUrlLabel = null;
	private Text serverUrlText = null;
	private Label userIdLabel = null;
	private Text userIdText = null;
	private Label passwdLabel = null;
	private Text passwdText = null;
	private Label projectLabel = null;
	private Text projectText = null;

	public JazzPrefComposite(Composite parent, int style) {
		super(parent, style);
		initialize();
	}

	private void initialize() {
		GridData gridData1 = new GridData();
		gridData1.widthHint = 200;
		GridData gridData = new GridData();
		gridData.widthHint = 100;
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		serverUrlLabel = new Label(this, SWT.NONE);
		serverUrlLabel.setText("Jazz Server URL");
		serverUrlLabel.setLayoutData(gridData);
		serverUrlText = new Text(this, SWT.BORDER);
		serverUrlText.setLayoutData(gridData1);
		projectLabel = new Label(this, SWT.NONE);
		projectLabel.setText("Project");
		projectText = new Text(this, SWT.BORDER);
		userIdLabel = new Label(this, SWT.NONE);
		userIdLabel.setText("User ID");
		userIdText = new Text(this, SWT.BORDER);
		passwdLabel = new Label(this, SWT.NONE);
		passwdLabel.setText("Password");
		passwdText = new Text(this, SWT.BORDER | SWT.PASSWORD);
		this.setLayout(gridLayout);
		setSize(new Point(320, 200));
	}

	public Text getServerUrlText() {
		return serverUrlText;
	}

	public Text getUserIdText() {
		return userIdText;
	}

	public Text getPasswdText() {
		return passwdText;
	}

	public Text getProjectText() {
		return projectText;
	}

}
