package harachu;

import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.jface.preference.PreferenceNode;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class PreferenceDialogTest extends ApplicationWindow {

        private String value1 = "";
        private String value2 = "";

        public PreferenceDialogTest() {
                super(null);

        }
        public static void main(String[] args) {
                ApplicationWindow w = new PreferenceDialogTest();
                System.out.println("here1");
                w.setBlockOnOpen(true);
                System.out.println("here2");
                w.open();
                System.out.println("here3");
                Display.getCurrent().dispose();
        }

        protected Control createContents(Composite parent) {
                getShell().setText("PreferenceManagerTest");
                Composite container = new Composite(parent, SWT.NONE);

                container.setLayout(new FillLayout());
                Button b = new Button(container, SWT.PUSH);
                b.setText("設定画面を開く");
                b.addSelectionListener(new SelectionAdapter() {
                        public void widgetSelected(SelectionEvent e) {
                                openPreferenceDialog();
                        }
                });

                return container;
        }
        private void openPreferenceDialog() {
                PreferenceManager pm = new PreferenceManager();

                PreferenceNode pnode1 = new PreferenceNode("NODE1");
                pnode1.setPage(new PreferencePage1());
                pm.addToRoot(pnode1);

                PreferenceNode pnode2 = new PreferenceNode("NODE2");
                pnode2.setPage(new PreferencePage2());
                pm.addTo("NODE1", pnode2);

                PreferenceDialog dialog = new PreferenceDialog(getShell(), pm);
                dialog.open();
                System.out.println("value1: " + value1);
                System.out.println("value2: " + value2);

        }

        class PreferencePage1 extends PreferencePage {
                Text text;
                public PreferencePage1() {
                        setTitle("ページ１");
                        setMessage("ページ１のメッセージ");
                }
                protected Control createContents(Composite parent) {
                        Composite c = new Composite(parent, SWT.NONE);
                        c.setLayout(new GridLayout(2, true));
                        new Label(c, SWT.NONE).setText("パラメータ１：");
                        text = new Text(c, SWT.SINGLE | SWT.BORDER);
                        return c;
                }
                protected void performApply() {
                        if (getControl() == null){
                                return;
                        }
                        value1 = text.getText();
                }
                public boolean performOk() {
                        performApply();
                        return true;
                }
        }

        class PreferencePage2 extends PreferencePage {
                Text text;
                public PreferencePage2() {
                        setTitle("ページ２");
                        setMessage("ページ２のメッセージ");
                }
                protected Control createContents(Composite parent) {
                        Composite c = new Composite(parent, SWT.NONE);
                        c.setLayout(new GridLayout(2, true));
                        new Label(c, SWT.NONE).setText("パラメータ２：");
                        text = new Text(c, SWT.SINGLE | SWT.BORDER);
                        return c;
                }
                protected void performApply() {
                        if (getControl() == null){
                                return;
                        }
                        value2 = text.getText();
                }
                public boolean performOk() {
                        performApply();
                        return true;
                }
        }
}