package harachu.mhr.view;

import harachu.mhr.controller.GuiController;

import java.util.logging.Logger;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tray;
import org.eclipse.swt.widgets.TrayItem;

public class MyTray {
	private Logger logger = Logger.getLogger("mhr");  //  @jve:decl-index=0:
	private Shell shell;
	private Tray tray;
	private Menu menu;
	private Display display;
	private Image image;
	private GuiController controller;
	
	public MyTray(Display display,Shell shell,GuiController controller){
		this.display=display;
		this.shell = shell;
		this.controller = controller;
		this.createTray();
		this.createMenu();
	}
	
	private void createTray(){
		tray = display.getSystemTray();

		if(tray == null){
			throw new RuntimeException("This platfor is not run.");
		}
		image = display.getSystemImage(SWT.ICON_INFORMATION);
		TrayItem item = new TrayItem(tray, SWT.NONE);
		item.setImage(image);
		item.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
	    		if(!shell.isVisible()){
	    			shell.setVisible(true);
	    		}
			}
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				menu.setVisible(true);
			}
		});	
		
		tray.addDisposeListener(new org.eclipse.swt.events.DisposeListener() {
			public void widgetDisposed(org.eclipse.swt.events.DisposeEvent e) {
				image.dispose();
			}
		});
		
	}
	private void createMenu(){
	    menu = new Menu(shell, SWT.POP_UP);
	    MenuItem retrieve = new MenuItem(menu, SWT.PUSH);
	    MenuItem max = new MenuItem(menu, SWT.PUSH);
	    MenuItem min = new MenuItem(menu, SWT.PUSH);
	    MenuItem quit = new MenuItem(menu, SWT.PUSH);
	    
	    quit.setText("èIóπ");
	    quit.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
	    	public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
	    		tray.dispose();
	    		shell.dispose();
	    	}
	    	public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
	    		logger.finest("widgetDefaultSelected()");
	    	}
	    });
;
	    min.setText("ç≈è¨âª");
	    min.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
	    	public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
	    		if(shell.isVisible()){
	    			shell.setVisible(false);
	    		}
	    	}
	    	public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
	    		logger.finest("widgetDefaultSelected()");
	    	}
	    });
	    
	    max.setText("ç≈ëÂâª");
	    max.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
	    	public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
	    		if(!shell.isVisible()){
	    			shell.setVisible(true);
	    		}

	    	}
	    	public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
	    		logger.finest("widgetDefaultSelected()");
	    	}
	    });
	    
	    retrieve.setText("JazzIteméÊìæ");
	    retrieve.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
	    	public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
	    		controller.retrieveWorkitem();

	    	}
	    	public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
	    		logger.finest("widgetDefaultSelected()");
	    	}
	    });
	}
	
	public boolean isDisposed(){
		return tray.isDisposed();
	}
	
	
}
