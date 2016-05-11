package io.xinjian.hsdbtool;

import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.StatusLineManager;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.jface.action.CoolBarManager;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;

import com.amazonaws.services.simpledb.model.Item;

import io.xinjian.hsdbtool.dialog.*;
import io.xinjian.hsdbtool.sdb.*;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.widgets.List;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

public class MianWindow extends ApplicationWindow {
	private Text txtSelect;
	private Text txtItem;
	private Text txtName;
	private Text txtValue;
	private Text txtLoginfo;
	private Text txtCreateDomain;
	private Combo listRegions;
	private ListViewer listViewerDomains;
	private List listDomains;
	private Combo comboDomain;
	private Button btnReplaceExsitingValue;
	private TreeViewer treeViewer;
	private Tree tree;
	private Log log;
	
	private SDBDao hsdbdao;

	/**
	 * Create the application window,
	 */
	public MianWindow() {
		super(null);
		createActions();
//		addCoolBar(SWT.FLAT);
		addMenuBar();
		addStatusLine();
		hsdbdao = new SDBDao();
	}

	/**
	 * Create contents of the application window.
	 * @param parent
	 */
	@Override
	protected Control createContents(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setFont(SWTResourceManager.getFont("Lucida Grande", 11, SWT.NORMAL));
		container.setLayout(new FormLayout());
		
		Composite compositeHeader = new Composite(container, SWT.NONE);
		FormData fd_compositeHeader = new FormData();
		fd_compositeHeader.right = new FormAttachment(100);
		fd_compositeHeader.top = new FormAttachment(0, 2);
		fd_compositeHeader.left = new FormAttachment(0);
		compositeHeader.setLayoutData(fd_compositeHeader);
		compositeHeader.setLayout(new GridLayout(1, false));
		
		Label lblHeader = new Label(compositeHeader, SWT.HORIZONTAL | SWT.CENTER);
		lblHeader.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1));
		lblHeader.setFont(SWTResourceManager.getFont("Lucida Grande", 18, SWT.NORMAL));
		lblHeader.setAlignment(SWT.CENTER);
		lblHeader.setText("AWS SimpleDB Tool");
		
		Label separator1 = new Label(compositeHeader, SWT.SEPARATOR | SWT.HORIZONTAL);
		separator1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Composite compositeWest = new Composite(container, SWT.NONE);
		FormData fd_compositeWest = new FormData();
		fd_compositeWest.top = new FormAttachment(compositeHeader);
		fd_compositeWest.bottom = new FormAttachment(100);
		fd_compositeWest.right = new FormAttachment(35);
		fd_compositeWest.left = new FormAttachment(0, 10);
		compositeWest.setLayoutData(fd_compositeWest);
		compositeWest.setLayout(new FormLayout());
		
		Group grpRegion = new Group(compositeWest, SWT.NONE);
		FormData fd_grpRegion = new FormData();
		fd_grpRegion.top = new FormAttachment(0);
		fd_grpRegion.left = new FormAttachment(0, 10);
		fd_grpRegion.right = new FormAttachment(100);
		fd_grpRegion.bottom = new FormAttachment(100);
		grpRegion.setLayoutData(fd_grpRegion);
		grpRegion.setText("Region");
		grpRegion.setLayout(new FormLayout());
		
		//list regions
		listRegions = new Combo(grpRegion, SWT.READ_ONLY);
		listRegions.setFont(SWTResourceManager.getFont("Lucida Grande", 11, SWT.NORMAL));
		FormData fd_listRegions = new FormData();
		fd_listRegions.right = new FormAttachment(100, -3);
		fd_listRegions.top = new FormAttachment(0, 3);
		fd_listRegions.left = new FormAttachment(0, 3);
		listRegions.setLayoutData(fd_listRegions);
		listRegions.setItems(SDBToolRegions.SDBToolRegions);
		listRegions.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				loadDomains("Load");
			}
		});
		listRegions.select(2);  //default to us-west-2
		
		Group grpDomain = new Group(compositeWest, SWT.NONE);
		fd_grpRegion.bottom = new FormAttachment(grpDomain, -6);
		grpDomain.setLayout(new FormLayout());
		FormData fd_grpDomain = new FormData();
		fd_grpDomain.top = new FormAttachment(0, 63);
		fd_grpDomain.left = new FormAttachment(0, 10);
		fd_grpDomain.right = new FormAttachment(100);
		grpDomain.setLayoutData(fd_grpDomain);
		grpDomain.setText("Domain");
		
		Group grpAttributes = new Group(compositeWest, SWT.NONE);
		fd_grpDomain.bottom = new FormAttachment(grpAttributes, -6);
		//list domains
		listViewerDomains = new ListViewer(grpDomain, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		listViewerDomains.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				if(!listViewerDomains.getSelection().isEmpty()) {
					String selectedDomain = listViewerDomains.getSelection().toString();
					selectedDomain = selectedDomain.replace("[", "").replace("]", "");
					comboDomain.setText(selectedDomain);
				}
			}
		});
		listDomains = listViewerDomains.getList();
		listDomains.setFont(SWTResourceManager.getFont("Lucida Grande", 11, SWT.NORMAL));
		FormData fd_list = new FormData();
		fd_list.right = new FormAttachment(100, -7);
		fd_list.top = new FormAttachment(0, 6);
		fd_list.left = new FormAttachment(0, 8);
		listDomains.setLayoutData(fd_list);
		
		//reload domain button
		Button btnReloadDomains = new Button(grpDomain, SWT.NONE);
		btnReloadDomains.setFont(SWTResourceManager.getFont("Lucida Grande", 11, SWT.NORMAL));
		btnReloadDomains.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				loadDomains("Reload");
			}
		});

		fd_list.bottom = new FormAttachment(btnReloadDomains, -6);
		
		//pop menu
		Menu menuDmain = new Menu(listDomains);
		listDomains.setMenu(menuDmain);
		//pop menu 'select request'
		MenuItem mntmSelect = new MenuItem(menuDmain, SWT.NONE);
		mntmSelect.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(!listViewerDomains.getSelection().isEmpty()) {
					String selectedDomain = listViewerDomains.getSelection().toString();
					selectedDomain = selectedDomain.replace("[", "").replace("]", "");
					String sql = "select * from `" + selectedDomain + "` limit 100";
					txtSelect.setText(sql);
					runSelect();
				}
			}
		});
		mntmSelect.setText("Select Request");
		//pop menu 'delete domain'
		MenuItem mntmDelect = new MenuItem(menuDmain, SWT.NONE);
		mntmDelect.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				deleteDomain();
			}
		});
		mntmDelect.setText("Delect Domain");
		FormData fd_btnReloadDomains = new FormData();
		fd_btnReloadDomains.left = new FormAttachment(0, 8);
		btnReloadDomains.setLayoutData(fd_btnReloadDomains);
		btnReloadDomains.setText("Reload Domains");
		
		//delete domain button
		Button btnDeleteDomain = new Button(grpDomain, SWT.NONE);
		btnDeleteDomain.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				deleteDomain();
			}
		});
		btnDeleteDomain.setFont(SWTResourceManager.getFont("Lucida Grande", 11, SWT.NORMAL));
		FormData fd_btnDeleteDomain = new FormData();
		fd_btnDeleteDomain.right = new FormAttachment(100, -7);
		fd_btnDeleteDomain.top = new FormAttachment(btnReloadDomains, 0, SWT.TOP);
		btnDeleteDomain.setLayoutData(fd_btnDeleteDomain);
		btnDeleteDomain.setText("Delete Domain");
		
		txtCreateDomain = new Text(grpDomain, SWT.BORDER | SWT.SEARCH);
		txtCreateDomain.setToolTipText("[Domain Name]");
		fd_btnReloadDomains.bottom = new FormAttachment(txtCreateDomain, -6);
		FormData fd_txtCreateDomain = new FormData();
		fd_txtCreateDomain.bottom = new FormAttachment(100, -6);
		fd_txtCreateDomain.left = new FormAttachment(0, 8);
		txtCreateDomain.setLayoutData(fd_txtCreateDomain);
		
		//create domain button
		Button btnCreateDomain = new Button(grpDomain, SWT.NONE);
		btnCreateDomain.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String domain = txtCreateDomain.getText();
				if(domain.isEmpty()) return;
				setRegion();
				try {
					domain.trim();
					hsdbdao.createDomain(domain);
				} catch (Exception e1) {
					log.showLog(e1.getMessage());
				}
				log.showLog("Create Domain : [" + domain + "]");
				loadDomains("Reload");
			}
		});
		btnCreateDomain.setFont(SWTResourceManager.getFont("Lucida Grande", 11, SWT.NORMAL));
		fd_txtCreateDomain.top = new FormAttachment(btnCreateDomain, 4, SWT.TOP);
		fd_txtCreateDomain.right = new FormAttachment(btnCreateDomain, -6);
		FormData fd_btnCreateDomain = new FormData();
		fd_btnCreateDomain.right = new FormAttachment(100, -7);
		fd_btnCreateDomain.bottom = new FormAttachment(100, -1);
		btnCreateDomain.setLayoutData(fd_btnCreateDomain);
		btnCreateDomain.setText("Create Domain");
		
		grpAttributes.setLayout(new FormLayout());
		FormData fd_grpAttributes = new FormData();
		fd_grpAttributes.left = new FormAttachment(0, 10);
		fd_grpAttributes.right = new FormAttachment(100);
		fd_grpAttributes.top = new FormAttachment(100, -212);
		fd_grpAttributes.bottom = new FormAttachment(100);
		grpAttributes.setLayoutData(fd_grpAttributes);
		grpAttributes.setText("Add/Replace Attributes");
		
		Label lblDomain = new Label(grpAttributes, SWT.NONE);
		FormData fd_lblDomain = new FormData();
		fd_lblDomain.left = new FormAttachment(0);
		lblDomain.setLayoutData(fd_lblDomain);
		lblDomain.setText("Domain");
		
		//select which domain to put attribute
		comboDomain = new Combo(grpAttributes, SWT.READ_ONLY);
		comboDomain.setFont(SWTResourceManager.getFont("Lucida Grande", 11, SWT.NORMAL));
		comboDomain.setVisibleItemCount(15);
		comboDomain.setItems(new String[] {"[Select a domain]"});
		comboDomain.setText("[Select a domain]");
		comboDomain.setToolTipText("Select a domain");
		fd_lblDomain.top = new FormAttachment(comboDomain, 6, SWT.TOP);
		FormData fd_comboDomain = new FormData();
		fd_comboDomain.left = new FormAttachment(lblDomain);
		fd_comboDomain.right = new FormAttachment(100, -7);
		fd_comboDomain.top = new FormAttachment(0, 10);
		fd_comboDomain.bottom = new FormAttachment(0, 32);
		comboDomain.setLayoutData(fd_comboDomain);
		
		Label lblItem = new Label(grpAttributes, SWT.NONE);
		FormData fd_lblItem = new FormData();
		fd_lblItem.right = new FormAttachment(lblDomain, 0, SWT.RIGHT);
		fd_lblItem.top = new FormAttachment(lblDomain, 12);
		fd_lblItem.left = new FormAttachment(lblDomain, 0, SWT.LEFT);
		lblItem.setLayoutData(fd_lblItem);
		lblItem.setText("Item");
		
		Label lblName = new Label(grpAttributes, SWT.NONE);
		FormData fd_lblName = new FormData();
		fd_lblName.right = new FormAttachment(lblDomain, 0, SWT.RIGHT);
		fd_lblName.top = new FormAttachment(lblItem, 12);
		fd_lblName.left = new FormAttachment(lblDomain, 0, SWT.LEFT);
		lblName.setLayoutData(fd_lblName);
		lblName.setText("Name");
		
		Label lblValue = new Label(grpAttributes, SWT.NONE);
		FormData fd_lblValue = new FormData();
		fd_lblValue.right = new FormAttachment(lblDomain, 0, SWT.RIGHT);
		fd_lblValue.top = new FormAttachment(lblName, 12);
		fd_lblValue.left = new FormAttachment(lblDomain, 0, SWT.LEFT);
		lblValue.setLayoutData(fd_lblValue);
		lblValue.setText("Value");
		
		//item name
		txtItem = new Text(grpAttributes, SWT.BORDER);
		FormData fd_txtItem = new FormData();
		fd_txtItem.left = new FormAttachment(comboDomain, 3, SWT.LEFT);
		fd_txtItem.right = new FormAttachment(100, -10);
		fd_txtItem.top = new FormAttachment(lblItem, -5, SWT.TOP);
		txtItem.setLayoutData(fd_txtItem);
		
		//attribute name
		txtName = new Text(grpAttributes, SWT.BORDER);
		FormData fd_txtName = new FormData();
		fd_txtName.left = new FormAttachment(comboDomain, 3, SWT.LEFT);
		fd_txtName.right = new FormAttachment(100, -10);
		fd_txtName.top = new FormAttachment(lblName, -5, SWT.TOP);
		txtName.setLayoutData(fd_txtName);
		
		//attribute value
		txtValue = new Text(grpAttributes, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
		txtValue.setFont(SWTResourceManager.getFont("Lucida Grande", 11, SWT.NORMAL));
		txtValue.setToolTipText("[one value per line]");
		FormData fd_txtValue = new FormData();
		fd_txtValue.left = new FormAttachment(comboDomain, 3, SWT.LEFT);
		fd_txtValue.bottom = new FormAttachment(lblValue, 37);
		fd_txtValue.right = new FormAttachment(100, -10);
		fd_txtValue.top = new FormAttachment(lblValue, -5, SWT.TOP);
		txtValue.setLayoutData(fd_txtValue);
		
		 //if replace existing value
		btnReplaceExsitingValue = new Button(grpAttributes, SWT.CHECK); 
		btnReplaceExsitingValue.setFont(SWTResourceManager.getFont("Lucida Grande", 11, SWT.NORMAL));
		FormData fd_btnReplaceExsitingValue = new FormData();
		fd_btnReplaceExsitingValue.top = new FormAttachment(txtValue, 4);
		fd_btnReplaceExsitingValue.bottom = new FormAttachment(100, -30);
		fd_btnReplaceExsitingValue.left = new FormAttachment(comboDomain, 0, SWT.LEFT);
		comboDomain.setText("[Select a domain]");
		comboDomain.setItems(new String[]{"[Select a domain]"});
		comboDomain.select(0);
		btnReplaceExsitingValue.setLayoutData(fd_btnReplaceExsitingValue);
		btnReplaceExsitingValue.setText("Replace Exsiting Value");
		
		Button btnPutAttributes = new Button(grpAttributes, SWT.NONE);  //put attribute
		btnPutAttributes.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setRegion();
				String domainName = comboDomain.getItem(comboDomain.getSelectionIndex());
				String itemName = txtItem.getText().trim();
				String attrName = txtName.getText().trim();
				String attrValues = txtValue.getText().trim();
				boolean replace = btnReplaceExsitingValue.getSelection();
				
				if(domainName.isEmpty() || itemName.isEmpty() || attrName.isEmpty() || attrValues.isEmpty()) return;  //every must be not empty
				try {
					StringTokenizer st = new StringTokenizer(attrValues);
					while( st.hasMoreTokens()){
						String value = st.nextToken();
						hsdbdao.putAttribute(domainName, itemName, attrName, value, replace);
						replace = false;
						log.showLog("Put Item [" + itemName + "]  in domain  [" + domainName 
								+ "] with attribute { \"" + attrName + "\" : " + "\"" + value + "\" }");  //success put
					}
				} catch (Exception e1) {
					log.showLog("An error occoured when put attribute");
					log.showLog(e1.getMessage());
				}
			}
		});
		btnPutAttributes.setFont(SWTResourceManager.getFont("Lucida Grande", 11, SWT.NORMAL));
		FormData fd_btnPutAttributes = new FormData();
		fd_btnPutAttributes.bottom = new FormAttachment(100);
		fd_btnPutAttributes.top = new FormAttachment(btnReplaceExsitingValue, 2);
		fd_btnPutAttributes.right = new FormAttachment(100, -10);
		btnPutAttributes.setLayoutData(fd_btnPutAttributes);
		btnPutAttributes.setText("Put Attributes");
		
		Composite compositeCenter = new Composite(container, SWT.NONE);
		FormData fd_compositeCenter = new FormData();
		fd_compositeCenter.top = new FormAttachment(compositeHeader);
		fd_compositeCenter.bottom = new FormAttachment(100);
		fd_compositeCenter.right = new FormAttachment(100, -10);
		fd_compositeCenter.left = new FormAttachment(35);
		compositeCenter.setLayoutData(fd_compositeCenter);
		compositeCenter.setLayout(new FormLayout());
		
		Group grpSelect = new Group(compositeCenter, SWT.NONE);
		FormData fd_grpSelect = new FormData();
		fd_grpSelect.bottom = new FormAttachment(0, 57);
		fd_grpSelect.top = new FormAttachment(0);
		fd_grpSelect.right = new FormAttachment(100, -10);
		fd_grpSelect.left = new FormAttachment(0, 10);
		grpSelect.setLayoutData(fd_grpSelect);
		grpSelect.setLayout(new FormLayout());
		grpSelect.setText("Select");
		
		//input select sql
		txtSelect = new Text(grpSelect, SWT.BORDER | SWT.SEARCH);
		FormData fd_txtSelect = new FormData();
		fd_txtSelect.top = new FormAttachment(0, 3);
		fd_txtSelect.left = new FormAttachment(0, 6);
		fd_txtSelect.bottom = new FormAttachment(100, -5);
		fd_txtSelect.right = new FormAttachment(100, -120);
		txtSelect.setLayoutData(fd_txtSelect);
		
		Button btnSelect = new Button(grpSelect, SWT.CENTER);
		btnSelect.setFont(SWTResourceManager.getFont("Lucida Grande", 11, SWT.NORMAL));
		btnSelect.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				runSelect();
			}
		});
		FormData fd_btnSelect = new FormData();
		fd_btnSelect.right = new FormAttachment(100, -8);
		fd_btnSelect.top = new FormAttachment(0, 1);
		fd_btnSelect.left = new FormAttachment(txtSelect, 6);
		btnSelect.setLayoutData(fd_btnSelect);
		btnSelect.setText("Run Select");
		
		//show select result
		Group grpResult = new Group(compositeCenter, SWT.NONE);
		grpResult.setText("Result");
		grpResult.setLayout(new FormLayout());
		FormData fd_grpResult = new FormData();
		fd_grpResult.top = new FormAttachment(grpSelect, 63, SWT.TOP);
		fd_grpResult.right = new FormAttachment(100, -10);
		fd_grpResult.left = new FormAttachment(0, 10);
		grpResult.setLayoutData(fd_grpResult);
		
		treeViewer = new TreeViewer(grpResult, SWT.BORDER);
		tree = treeViewer.getTree();
		tree.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {  //select in table tree and change the "Add/Replace Attributes" area
				TreeSelection treeSelection = (TreeSelection) treeViewer.getSelection();
				if(treeSelection == null) return;  //return if no select
				TreePath treePath = treeSelection.getPaths()[0];  //get the selected tree path
				txtName.setText("");  //attribute name set to null
				txtValue.setText("");  //attribute value set to null
				int c = treePath.getSegmentCount();  //get the deep
				if(c > 0) txtItem.setText(((SDBItem)treePath.getSegment(0)).getName());
				if(c > 1) txtName.setText(((SDBAttribute)treePath.getSegment(1)).getName());
				if(c > 2) txtValue.setText((String)treePath.getSegment(2));
			}
		});
		tree.setFont(SWTResourceManager.getFont("Lucida Grande", 11, SWT.NORMAL));
		tree.setLinesVisible(true);
		tree.setHeaderVisible(true);
		FormData fd_tree = new FormData();
		fd_tree.bottom = new FormAttachment(100, -5);
		fd_tree.right = new FormAttachment(100, -8);
		fd_tree.top = new FormAttachment(0, 5);
		fd_tree.left = new FormAttachment(0, 8);
		tree.setLayoutData(fd_tree);
		
		TreeColumn trclmnItems = new TreeColumn(treeViewer.getTree(), SWT.LEFT);
		trclmnItems.setWidth(200);
		trclmnItems.setText("Items");
		
		TreeColumn trclmnAttributeNames = new TreeColumn(treeViewer.getTree(), SWT.LEFT);
		trclmnAttributeNames.setWidth(200);
		trclmnAttributeNames.setText("Attribute Names");
		
		TreeColumn trclmnAttributeValues = new TreeColumn(treeViewer.getTree(), SWT.LEFT);
		trclmnAttributeValues.setWidth(200);
		trclmnAttributeValues.setText("Attribute Values");
		
		treeViewer.setLabelProvider(new attTableLabelProvider());
		treeViewer.setContentProvider(new attTreeContentProvider());
		
		// show log info
		Group grpLog = new Group(compositeCenter, SWT.NONE);
		fd_grpResult.bottom = new FormAttachment(grpLog, -6);
		grpLog.setText("Log");
		grpLog.setLayout(new FormLayout());
		FormData fd_grpLog = new FormData();
		fd_grpLog.left = new FormAttachment(grpSelect, 0, SWT.LEFT);
		fd_grpLog.top = new FormAttachment(75);
		fd_grpLog.bottom = new FormAttachment(100);
		fd_grpLog.right = new FormAttachment(100, -10);
		grpLog.setLayoutData(fd_grpLog);
		
		txtLoginfo = new Text(grpLog, SWT.BORDER | SWT.READ_ONLY | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
		txtLoginfo.setToolTipText("Log Information");
		FormData fd_txtLoginfo = new FormData();
		fd_txtLoginfo.bottom = new FormAttachment(100, -5);
		fd_txtLoginfo.right = new FormAttachment(100, -8);
		fd_txtLoginfo.top = new FormAttachment(0, 5);
		fd_txtLoginfo.left = new FormAttachment(0, 8);
		txtLoginfo.setLayoutData(fd_txtLoginfo);
		log = new Log(txtLoginfo);

		return container;
	}

	/**
	 * Create the actions.
	 */
	private void createActions() {
		// Create the actions
		
	}

	/**
	 * Create the menu manager.
	 * @return the menu manager
	 */
	@Override
	protected MenuManager createMenuManager() {  //add menu
		MenuManager menuManager = new MenuManager("menu");
		MenuManager mainManager = new MenuManager("File");
	
		menuManager.add(mainManager);
		AboutAction aboutAction = new AboutAction();
		aboutAction.setToolTipText("About hSDBTool");
		mainManager.add(aboutAction);
		mainManager.add(new Separator());
		PreferenceAction preferenceAction = new PreferenceAction();
		preferenceAction.setToolTipText("Set profile");
		mainManager.add(preferenceAction);
		
		menuManager.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
			}
		});
		return menuManager;
	}

	/**
	 * Create the coolbar manager.
	 * @return the coolbar manager
	 */
	@Override
	protected CoolBarManager createCoolBarManager(int style) {
		CoolBarManager coolBarManager = new CoolBarManager(style);
		return coolBarManager;
	}

	/**
	 * Create the status line manager.
	 * @return the status line manager
	 */
	@Override
	protected StatusLineManager createStatusLineManager() {
		StatusLineManager statusLineManager = new StatusLineManager();
		statusLineManager.setMessage("Welcome!");
		return statusLineManager;
	}

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			MianWindow window = new MianWindow();
			window.setBlockOnOpen(true);
			window.open();
			Display.getCurrent().dispose();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Configure the shell.
	 * @param newShell
	 */
	@Override
	protected void configureShell(Shell newShell) {
		newShell.setToolTipText("Visual Tool for AWS SimpleDB");
		newShell.setMinimumSize(new Point(850, 500));
		super.configureShell(newShell);
		newShell.setText("AWS SimpleDB Tool -- hSDBTool");
		newShell.setSize(1120, 700);  //16:10 set default size
		
		int screenW = Toolkit.getDefaultToolkit().getScreenSize().width;
		int screenH = Toolkit.getDefaultToolkit().getScreenSize().height;
		int shellW = newShell.getBounds().width;
		int shellH = newShell.getBounds().height;
		if(shellW > screenW) shellW = screenW;
		if(shellH > screenH) shellH = screenH;
		
		newShell.setLocation((screenW-shellW)/2, (screenH-shellH)/10);
	}

	/**
	 * Return the initial size of the window.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(1000, 688);
	}
	
	private void loadDomains(String load) {  //list the domains
		setRegion();  //set region
		String regionName = SDBToolRegions.SDBToolRegions[listRegions.getSelectionIndex()];  //get the selected region name
		log.showLog(load + " ["  + regionName + "] domains");
		listDomains.removeAll();
		java.util.List<String> domainslist = null;
		try {
			domainslist = hsdbdao.getDomainsList(); 
		} catch (Exception e) {  //when exception
			log.showLog("An error occoured when " + load.toLowerCase() + " domains");
			log.showLog(e.getMessage());
		}
		
//		Object[] domains = new Object[0];
//		if (domainslist.toArray() != null) {
			listViewerDomains.add(domainslist.toArray());  //add domains list into viewer
//		}
		
		String[] sDomainlist = new String[domainslist.size()];  //add domains list into comboDoamin
		int i = 0;
		for(String s : domainslist){
			sDomainlist[i++] = s;
		}
		if(sDomainlist.length > 0){
			comboDomain.setItems(sDomainlist);
			comboDomain.select(0);
		}
		log.showLog( sDomainlist.length + " doamin(s) in region [" + regionName + "]");
	}
	
	private void setRegion(){
		hsdbdao.setRegion(SDBToolRegions.getRegion(listRegions.getSelectionIndex()));
	}
	
	private void deleteDomain() {
		if(!listViewerDomains.getSelection().isEmpty()) {
			String selectedDomain = listViewerDomains.getSelection().toString();
			selectedDomain = selectedDomain.replace("[", "").replace("]", "");
			MessageBox msgbox = new MessageBox(getShell(), SWT.ICON_WARNING | SWT.OK | SWT.CANCEL);
			msgbox.setMessage("Do you want to delete the domain: [" + selectedDomain + "] ?\n\nIt can't be restored!");
			if(msgbox.open() == SWT.OK) {  //alert warning dialog
				try {
					hsdbdao.deleteDomain(selectedDomain);
				} catch (Exception e1) {
					log.showLog(e1.getMessage());
				}
				log.showLog("Domain [" + selectedDomain + "] has been deleted");
				loadDomains("Reload");
			}
		}
	}
	
	private void runSelect() {
		String sql = txtSelect.getText();
		if(!sql.trim().isEmpty()){  //not empty to run select
			java.util.List<Item> requestItems = null;
			try{
				setRegion();
				requestItems = hsdbdao.select(sql);
				log.showLog("Run Select Request [" + sql + "]");
			} catch (NullPointerException e) {
				log.showLog("There is an error in [" + sql + "]");
				log.showLog(e.getMessage());
				log.showLog(e.getCause().getMessage());
			}catch (Exception ex ){
				log.showLog(ex.getMessage());
			} 
			if(requestItems != null) {
				java.util.List<SDBItem>sdbItems = new java.util.ArrayList<SDBItem>();
				for(Item item : requestItems) {  //turn Item object to SDBItem object
					sdbItems.add(new SDBItem(item));
				}
				treeViewer.setInput(sdbItems);  //input select result and then show in the table
			}
		}
	}
	
}
