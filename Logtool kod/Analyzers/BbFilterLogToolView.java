package logtool.analyzer.bbfilter.ui;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import logtool.analyzer.bbfilter.Activator;
import logtool.analyzer.bbfilter.BbFilterLogToolAnalyzer;
import logtool.analyzer.bbfilter.preferences.BbFilterPreferencesConstants;
import logtool.analyzer.bbfilter.ui.config.ConfigSidebar;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.window.DefaultToolTip;
import org.eclipse.nebula.widgets.nattable.config.CellConfigAttributes;
import org.eclipse.nebula.widgets.nattable.grid.GridRegion;
import org.eclipse.nebula.widgets.nattable.painter.cell.TextPainter;
import org.eclipse.nebula.widgets.nattable.painter.cell.decorator.BeveledBorderDecorator;
import org.eclipse.nebula.widgets.nattable.painter.cell.decorator.PaddingDecorator;
import org.eclipse.nebula.widgets.nattable.resize.command.InitializeAutoResizeColumnsCommand;
import org.eclipse.nebula.widgets.nattable.style.DisplayMode;
import org.eclipse.nebula.widgets.nattable.util.GCFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.PlatformUI;

import MasterThesisPackage.MemoryInformationWindow;

import com.ericsson.logtool.core.Log;
import com.ericsson.logtool.resources.LogToolColor;
import com.ericsson.logtool.resources.LogToolImageRegistry;
import com.ericsson.logtool.resources.core.resource.ILogToolResource;
import com.ericsson.logtool.resources.core.ui.GuiControl;
import com.ericsson.logtool.resources.timer.TimerProvider;
import com.ericsson.logtool.resources.timer.TimerProviderListener;
import com.ericsson.logtool.resources.view.AbstractDynamicTableView2;
import com.ericsson.logtool.resources.view.DynamicTrace2;
import com.ericsson.logtool.resources.view.DynamicTraceList;
import com.ericsson.logtool.resources.view.LogToolArrayList;
import com.ericsson.logtool.resources.view.LogToolToolTip;
import com.ericsson.reveal.app.bbfilter.BbFilterView;
import com.ericsson.reveal.app.bbfilter.BbFilterView.Column;
import com.ericsson.reveal.fw.core.BbTimeFrame;
import com.ericsson.reveal.fw.core.Frame;
import com.ericsson.reveal.fw.core.FrameElement;
import com.ericsson.reveal.fw.core.FrameElementId;
import com.ericsson.reveal.fw.core.Input;
import com.ericsson.reveal.fw.core.Output;
import com.ericsson.reveal.fw.core.OutputCollection;
import com.ericsson.reveal.fw.core.View;

public class BbFilterLogToolView extends AbstractDynamicTableView2<DynamicTrace2> implements View, IPartListener2, TimerProviderListener {

	public static final String FILE_HISTORY_SPLIT_CHARACTER = ";";
	private static final Color COLOR_ERROR = LogToolColor.getInstance().getColor(LogToolColor.DefinedColors.ERROR_RED);
	private static final Color COLOR_OK = PlatformUI.getWorkbench().getDisplay().getSystemColor(SWT.COLOR_WHITE);

	private static Map<String, Integer> COLUMN_EXTRA_WIDTH = new HashMap<String, Integer>();
	private static Map<String, Integer> COLUMN_WIDTH = new HashMap<String, Integer>();

	private DynamicTraceList<DynamicTrace2> traceList = new DynamicTraceList<DynamicTrace2>(DynamicTrace2.class);
	
	private Map<String, String> toolTipsMap = new HashMap<String, String>();
	private List<Output> outputs;
	private BbFilterView bbFilterView;
	private BbFilterLogToolAnalyzer bbFilterLogtoolAnalyzer;
	private Combo combo;

	private Label labelProfileDescription;
	private Map<String, String> collectionAndToolTip;

	private ConfigSidebar configSidebar;
	private Composite parent;

	private Listener textKeyListener;

	private Action actionLoadCustomProfile;
	private Action actionExtractConfigFiles;

	private Button buttonOutputToFile;
	private Combo comboFilename;
	private Button buttonBrowse;
	private BufferedWriter printWriter;
	private String filename;
	private boolean validFileName = true;
	private boolean writeToFile = false;

	private StringBuilder headers = new StringBuilder();
	private StringBuilder bufferedOutput = new StringBuilder();
	private boolean emptyProfile;
	private String profileToBeUpdated;
	private int lastVisibleColumnIndex;

	private IPreferenceStore store = Activator.getDefault().getPreferenceStore();

	private MemoryInformationWindow informationWindow; //MasterThesis
	
	static {
		COLUMN_EXTRA_WIDTH.put("bsr", 5);
		COLUMN_EXTRA_WIDTH.put("tbs", 3);
		COLUMN_EXTRA_WIDTH.put("sinr", 2);
		COLUMN_EXTRA_WIDTH.put("assBits", 2);
		COLUMN_EXTRA_WIDTH.put("tbs2", 4);
		COLUMN_EXTRA_WIDTH.put("tbs1", 4);
		COLUMN_EXTRA_WIDTH.put("prb", 2);
		COLUMN_EXTRA_WIDTH.put("mode", 2);
		COLUMN_EXTRA_WIDTH.put("rnti", 2);
		COLUMN_EXTRA_WIDTH.put("sfn", 2);
		COLUMN_EXTRA_WIDTH.put("bbUeRef", 2);
		COLUMN_EXTRA_WIDTH.put("racUeRef", 3);
		COLUMN_EXTRA_WIDTH.put("tbsAvg", 4);
		COLUMN_EXTRA_WIDTH.put("timeStamp", 22);
	}
	
	public void initialize() {
		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				setComboEnabled(false);
			}
		});

		super.initialize();
		getTraceList().setLimitTraces(true);
		getTraceList().setTraceLimit(store.getInt(BbFilterPreferencesConstants.TRACE_LIMIT));
		
	}

	@Override
	public void setResource(ILogToolResource resource) {
		setTraceList(traceList);
		bbFilterLogtoolAnalyzer = (BbFilterLogToolAnalyzer) resource;
		fillComboBox();
		labelProfileDescription.setText(collectionAndToolTip.get(combo.getText()));
		combo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent selectionEvent) {
				updateAfterComboChange();
				emptyProfile = "".equals(combo.getText());
			}
		});
		emptyProfile = "".equals(combo.getText());

		bbFilterLogtoolAnalyzer.setBbFilterLogToolView(this, combo.getText());

		createToolBarActions();
		configSidebar.loadConfig(bbFilterLogtoolAnalyzer.getOutputCollections());
		super.setResource(resource);
		createTable(traceList, resource);

		GC gc = new GCFactory(getTable()).createGC();
		gc.setFont(getFont());
		int charWidth = gc.textExtent("W").x;
		for (Entry<String, Integer> entry : COLUMN_EXTRA_WIDTH.entrySet()) {
			int width = gc.textExtent(entry.getKey()).x + charWidth * entry.getValue();
			COLUMN_WIDTH.put(entry.getKey(), width);
		}
		autoResizeColumns();

		getTable().addListener(SWT.Resize, new Listener() {
			@Override
			public void handleEvent(Event event) {
				updateColumnResize();
			}
		});
		
		getTable().getHorizontalBar().addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				updateColumnResize();
			}
		});

		TextPainter txtPainter = new TextPainter(false, false);
		PaddingDecorator paddingDecorator = new PaddingDecorator(txtPainter, 0, 0, 0, 0);
		BeveledBorderDecorator beveledBorderDecorator = new BeveledBorderDecorator(paddingDecorator);
		getTable().getConfigRegistry().registerConfigAttribute(CellConfigAttributes.CELL_PAINTER, beveledBorderDecorator, DisplayMode.NORMAL, GridRegion.COLUMN_HEADER);
		getTable().layout();
	}

	@Override
	public void createPartControl(Composite parent) {
		this.parent = parent;
		parent.setLayout(new GridLayout(2, false));

		Composite outerComposite = new Composite(parent, SWT.NONE);
		outerComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		outerComposite.setLayout(new GridLayout(1, false));

		Composite composite = new Composite(outerComposite, SWT.NONE);
		composite.setLayout(new GridLayout(4, false));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));

		Group groupProfile = new Group(composite, SWT.NONE);
		groupProfile.setText("Profile chooser");
		GridData griddataProfile = new GridData(SWT.LEFT, SWT.CENTER, false, false);
		griddataProfile.horizontalSpan = 1;
		groupProfile.setLayoutData(griddataProfile);
		GridLayout layoutGroupProfile = new GridLayout(1, false);
		groupProfile.setLayout(layoutGroupProfile);

		combo = new Combo(groupProfile, SWT.NONE);

		createFileOutputSelection(composite);

		Group groupProfileDescription = new Group(composite, SWT.NONE);
		groupProfileDescription.setText("Profile description");
		GridData griddataProfileDescription = new GridData(SWT.FILL, SWT.FILL, true, true);
		griddataProfileDescription.horizontalSpan = 4;
		groupProfileDescription.setLayoutData(griddataProfileDescription);
		GridLayout layoutGroupProfileDescription = new GridLayout(1, false);
		groupProfileDescription.setLayout(layoutGroupProfileDescription);

		GridData gridData2 = new GridData(GridData.FILL_HORIZONTAL);
		gridData2.horizontalSpan = 4;
		labelProfileDescription = new Label(groupProfileDescription, SWT.NONE);
		labelProfileDescription.setLayoutData(gridData2);
		addKeyListener();
		//adds the button which creates a new "HarqView"
		IAction theAction = new Action() {
			@Override
			public void run() {
//				try{
				IViewPart viewPart = GuiControl.getInstance().showView("logtool.analyzer.bbfilter.basicLTEGraphView");
				if (viewPart == null) {
					return;
				}
				BasicLTEGraphView basicView = (BasicLTEGraphView) viewPart;
				basicView.setTraceList(traceList);
//				}
//				catch(Exception e){ //TODO this doesnt work yet...
//					for(Window window : Window.getWindows()) // .getWindows() returns an empty array...
//						if(window.isVisible())
//							JOptionPane.showMessageDialog(window, "Eggs are not supposed to be green.");
//				}
			}
		};
		theAction.setImageDescriptor(LogToolImageRegistry.getInstance().getImageDescriptor(LogToolImageRegistry.BASIC_GRAPHS_VIEW));
		theAction.setToolTipText("Open a Basic LTE analyzer view with graphs over the last run trace data");
		addActionToToolBar(theAction);
		
		//creates a button that opens a new "CombinedGraphsView"
		IAction combinedGraphsViewAction = new Action() {
			@Override
			public void run() {
				IViewPart viewPart = GuiControl.getInstance().showView("logtool.analyzer.bbfilter.combinedGraphsView");
				if (viewPart == null) {
					return;
				}
				CombinedGraphsView combinedGraphsView = (CombinedGraphsView) viewPart;
				//combinedGraphsView.setTraceList(traceList);
			}
		};
		combinedGraphsViewAction.setImageDescriptor(LogToolImageRegistry.getInstance().getImageDescriptor(LogToolImageRegistry.MULTIPLE_GRAPHS_VIEW));
		combinedGraphsViewAction.setToolTipText("open a view where the user can view and compare different graphs");
		addActionToToolBar(combinedGraphsViewAction);
		
		//creates a button that opens a new "AdvancedLTEGraphView"
		IAction advancedLTEGraphViewAction = new Action() {
			@Override
			public void run() {
				IViewPart viewPart = GuiControl.getInstance().showView("logtool.analyzer.bbfilter.advancedLTEGraphsView");
				if (viewPart == null) {
					return;
				}
				AdvancedLTEGraphView advancedLTEGraphViewAction = (AdvancedLTEGraphView) viewPart;
				advancedLTEGraphViewAction.setTraceList(traceList);
			}
		};
		advancedLTEGraphViewAction.setImageDescriptor(LogToolImageRegistry.getInstance().getImageDescriptor(LogToolImageRegistry.ADVANCED_GRAPHS_VIEW));
		advancedLTEGraphViewAction.setToolTipText("open an advanced LTE analyzer where the user can create any graph he/she wants");
		addActionToToolBar(advancedLTEGraphViewAction);
		

//		informationWindow = new MemoryInformationWindow(); //MasterThesis
//		informationWindow.startWindowUpdate(); //MasterThesis
		
		super.createPartControl(outerComposite);
	}

	public void fillComboBox() {
		String activeProfile = getActiveSelection();
		Map<String, OutputCollection> outputCollections = bbFilterLogtoolAnalyzer.getOutputCollections();
		collectionAndToolTip = new HashMap<String, String>();
		for (OutputCollection outputCollection : outputCollections.values()) {
			collectionAndToolTip.put(outputCollection.getName(), outputCollection.getDescription());
		}
		String[] itemNames = collectionAndToolTip.keySet().toArray(new String[collectionAndToolTip.size()]);
		emptyProfile = itemNames == null || itemNames.length == 0;
		combo.removeAll();
		if (emptyProfile) {
			combo.getParent().layout();
			return;
		}
		Arrays.sort(itemNames);
		combo.setItems(itemNames);
		boolean hasSelected = false;
		for (int index = 0; index < itemNames.length; ++index) {
			if (activeProfile.equals(itemNames[index])) {
				combo.select(index);
				hasSelected = true;
				break;
			} 
		}

		if (!hasSelected) {
			combo.select(0);
		}
		combo.getParent().getParent().layout();

		if (activeProfile.equals(profileToBeUpdated)) {
			updateAfterComboChange();
		}
	}

	public void updateAfterComboChange() {
		labelProfileDescription.setText(collectionAndToolTip.get(combo.getText()));
		bbFilterLogtoolAnalyzer.setBbFilterLogToolView(BbFilterLogToolView.this, combo.getText());
		createTable();
		configSidebar.loadConfig(bbFilterLogtoolAnalyzer.getOutputCollections());
		autoResizeColumns();
	}

	public boolean isEmptyProfile() {
		return emptyProfile;
	}

	@Override
	public void newOutputFrame(BbTimeFrame outputFrame, Frame context) {
		for (Entry<FrameElementId, List<FrameElement>> elementMapEntry : outputFrame.getElementMap().entrySet()) { 
			final LogToolArrayList<Object> values = new LogToolArrayList<Object>(bbFilterView.getUsedColumns().size());
			int columnIndex = 0;
			for (Column column : bbFilterView.getUsedColumns()) {
				StringBuilder value = new StringBuilder();
				column.printElement(value, outputFrame, elementMapEntry.getKey().getCell(), elementMapEntry.getKey().getUe(), elementMapEntry.getValue());
				values.set(columnIndex, value.toString());
				if (writeToFile) {
					synchronized (bufferedOutput) {
						bufferedOutput.append(value.toString() + ", ");
					}
				}
				++columnIndex;
			}
			if (writeToFile) {
				synchronized (bufferedOutput) {
					bufferedOutput.append(System.getProperty("line.separator"));
				}	
			}

			Display.getDefault().syncExec(new Runnable() {
				@Override
				public void run() {
					traceList.addTrace(new DynamicTrace2(values));
				}
			});
		}
	}

	@Override
	public void newInput(Input input) {
		// For future use
	}

	@Override
	public void finished(BbTimeFrame context) {
		// For future use
	}

	public void setOutputs(List<Output> outputs) {
		clearColumns();
		headers = new StringBuilder();
		this.outputs = outputs;
		bbFilterView = new BbFilterView(outputs, null);
		bbFilterView.setSeparator("");
		bbFilterView.createColumns(new ArrayList<Output>());

		toolTipsMap.clear();
		for (Column column : bbFilterView.getUsedColumns()) {
			addColumn(column.getShortName());
			toolTipsMap.put(column.getShortName(), column.getDescription());
			headers.append(column.getShortName());
			headers.append(", ");
		}
	}

	@Override
	public List<Output> getOutputs() {
		return outputs;
	}

	public void setComboEnabled(boolean parameter) {
		combo.setEnabled(parameter);
	}

	public String getActiveSelection() {
		return combo.getText();
	}

	public int getSelectionCount() {
		return combo.getItemCount();
	}
	public void setProfileToBeUpdated(String profileToBeUpdated) {
		this.profileToBeUpdated = profileToBeUpdated;
	}

	@Override
	public void partClosed(IWorkbenchPartReference partRef) {
		if (bbFilterLogtoolAnalyzer.getID().equals(partRef.getPartName())) {
			PlatformUI.getWorkbench().getDisplay().removeFilter(SWT.KeyDown, textKeyListener);
		}
		super.partClosed(partRef);
	}

	@Override
	public void timeout() {
		try {
			synchronized (bufferedOutput) {
				printWriter.write(bufferedOutput.toString());
				bufferedOutput.setLength(0);
			}
		} catch (IOException ioe) {
			Log.getInstance().log(Log.WARNING, Activator.PLUGIN_ID, ioe.getMessage(), ioe);
		}
	}

	public void terminate() {
		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				if (!buttonOutputToFile.isDisposed()) {
					if (buttonOutputToFile.getSelection()) {
						stopWriteToCsvFile();
						buttonOutputToFile.setSelection(false);
						enableFileComponents();
					}
					setComboEnabled(true);
				}
			}
		});
	}

	@Override
	protected DefaultToolTip getTableToolTip() {
		return new LogToolToolTip(this, toolTipsMap);
	}

	//Här kanske vi kan sno lite kod exempel????
	private void startWriteToCsvFile() {
		try {
			synchronized (bufferedOutput) {
				bufferedOutput.setLength(0);
			}
			printWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(comboFilename.getText()), Charset.defaultCharset()));
			printWriter.write(headers.toString());
			printWriter.newLine();
		} catch (IOException ioe) {
			displayError("Could not write to file: " + ioe.getMessage());
			buttonOutputToFile.setSelection(false);
			enableFileComponents();
			return;
		}
		TimerProvider.getInstance().add(this);
	}

	private void stopWriteToCsvFile() {
		try {
			if (printWriter != null) {
				TimerProvider.getInstance().remove(this);
				timeout();
				printWriter.close();
			}	
		} catch (IOException e1) {
			Log.getInstance().log(Log.WARNING, Activator.PLUGIN_ID, e1.getMessage(), e1);
		}
	}

	private void createToolBarActions() {
		actionLoadCustomProfile = new Action("Load config") {
			public void run() {
				loadCustomProfile();
			}
		};
		actionLoadCustomProfile.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJ_FOLDER));
		actionLoadCustomProfile.setToolTipText("Load custom profile yaml files (CTRL-L)");
		ActionContributionItem contributionLoadCustomProfile = new ActionContributionItem(actionLoadCustomProfile);
		addContributionToToolBar(contributionLoadCustomProfile);

		actionExtractConfigFiles = new Action("Extract config") {
			public void run() {
				IPreferenceStore store = Activator.getDefault().getPreferenceStore();

				DirectoryDialog dialog = new DirectoryDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), SWT.SINGLE);
				dialog.setText("Select path where to extract configuration files");
				dialog.setFilterPath(store.getString(BbFilterPreferencesConstants.SAVE_CONFIG_PATH_HISTORY));

				final String path = dialog.open();
				if (path == null) {
					return;
				}
				bbFilterLogtoolAnalyzer.saveConfiguration(path);
				store.setValue(BbFilterPreferencesConstants.SAVE_CONFIG_PATH_HISTORY, path);
			}
		};
		actionExtractConfigFiles.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_ETOOL_SAVEALL_EDIT));
		actionExtractConfigFiles.setToolTipText("Save all default configuration yaml files (CTRL-S)");
		ActionContributionItem contributionExtractConfigFiles = new ActionContributionItem(actionExtractConfigFiles);
		addContributionToToolBar(contributionExtractConfigFiles);

		configSidebar = new ConfigSidebar(parent, bbFilterLogtoolAnalyzer, this);
		Action actionSelectColumns = new Action("Profile editor", IAction.AS_PUSH_BUTTON) {
			public void run() {
				if (configSidebar.isDisposed()) {
					configSidebar.buildConfigSidebar();
				} else {
					configSidebar.dispose();
				}
			}
		};
		actionSelectColumns.setToolTipText("Profile editor (CTRL-Y)");
		actionSelectColumns.setImageDescriptor(ImageDescriptor.createFromImage(LogToolImageRegistry.getInstance().getImage(LogToolImageRegistry.CONFIGURATION)));
		addActionToToolBar(actionSelectColumns);
	}

	private void loadCustomProfile() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();

		DirectoryDialog dialog = new DirectoryDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), SWT.SINGLE);
		dialog.setText("Select path to configuration files");
		dialog.setFilterPath(store.getString(BbFilterPreferencesConstants.LOAD_CONFIG_PATH_HISTORY));

		final String path = dialog.open();
		if (path == null) {
			return;
		}
		bbFilterLogtoolAnalyzer.loadConfigurationFromPath(path);

		store.setValue(BbFilterPreferencesConstants.LOAD_CONFIG_PATH_HISTORY, path);

		fillComboBox();	

		configSidebar.loadConfig(bbFilterLogtoolAnalyzer.getOutputCollections());

		if (!configSidebar.isDisposed()) {

			configSidebar.addItemsToProfileCombo();
		}
	}

	private void addKeyListener() {
		textKeyListener = new Listener() {
			@Override
			public void handleEvent(Event event) {
				if (!isInFocus()) {
					return;
				}
				if (((event.stateMask & SWT.CTRL) != 0) && (event.keyCode == 121)) {
					if (configSidebar.isDisposed()) {
						configSidebar.buildConfigSidebar();
					} else {
						configSidebar.dispose();
					}
				}
				if (((event.stateMask & SWT.CTRL) != 0) && (event.keyCode == 108)) {
					actionLoadCustomProfile.run();
				}
				if (((event.stateMask & SWT.CTRL) != 0) && (event.keyCode == 115)) {
					actionExtractConfigFiles.run();
				}
			}
		};
		PlatformUI.getWorkbench().getDisplay().addFilter(SWT.KeyDown, textKeyListener);
	}

	private void saveToFileHandling() {
		if (buttonOutputToFile.getSelection()) {
			startWriteToCsvFile();
			writeToFile = true;

		} else {
			stopWriteToCsvFile();
			enableFileComponents();
			writeToFile = false;
		}
	}

	private void createFileOutputSelection(Composite composite) {

		Group outputToFile = new Group(composite, SWT.NONE);
		outputToFile.setText("Save output to csv file");

		GridData griddataProfile = new GridData(SWT.FILL, SWT.FILL, true, true);
		griddataProfile.horizontalSpan = 3;
		outputToFile.setLayoutData(griddataProfile);
		GridLayout layoutGroupOutputToFile = new GridLayout(4, false);
		outputToFile.setLayout(layoutGroupOutputToFile);

		comboFilename = new Combo(outputToFile, SWT.NONE);
		GridData gridDataComboFileName = new GridData(SWT.LEFT, SWT.CENTER, false, false);
		gridDataComboFileName.widthHint = 300;
		comboFilename.setLayoutData(gridDataComboFileName);

		String fileHistory = store.getString(BbFilterPreferencesConstants.FILE_HISTORY);
		if (fileHistory != null) {
			String[] filenames = fileHistory.split(FILE_HISTORY_SPLIT_CHARACTER);
			if (filenames != null && filenames.length > 0) {
				comboFilename.setItems(filenames);
				comboFilename.select(0);
				filename = filenames[0];
			}
		}
		comboFilename.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				saveFileHistory();
			}
		});
		comboFilename.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent event) {
				validateFilename();
			}
		});

		buttonBrowse = new Button(outputToFile, SWT.PUSH);
		buttonBrowse.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
		buttonBrowse.setText("Browse...");
		buttonBrowse.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				browse();
			}
		});

		buttonOutputToFile = new Button(outputToFile, SWT.CHECK);
		buttonOutputToFile.setText("Output to file");
		buttonOutputToFile.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
		buttonOutputToFile.setEnabled(true);

		buttonOutputToFile.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				enableFileComponents();
				validateFilename();
				if (validFileName) {
					saveToFileHandling();	
				} else {
					if(buttonOutputToFile.getSelection()) {
						displayError("Invalid filename");
					}	
					writeToFile = false;
				}
			}
		});
	}

	private void saveFileHistory() {
		if (comboFilename.isDisposed()) {
			return;
		}
		String fileHistory = "";
		if (comboFilename.getItemCount() == 0) {
			filename = "";
		} else {
			fileHistory = getFileHistory();
		}
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		store.setValue(BbFilterPreferencesConstants.FILE_HISTORY, fileHistory);
	}

	private void displayError(String message) {
		MessageDialog.openError(Display.getDefault().getActiveShell(), getPartName() + " configuration error", message);
	}

	private String getFileHistory() {
		StringBuilder fileHistory = new StringBuilder();
		filename = comboFilename.getText().trim();
		int noFilenames = 0;
		if (filename.length() > 0) {
			fileHistory.append(filename);
			++noFilenames;
		}
		boolean filenameInList = false;
		for (int index = 0; index < comboFilename.getItemCount(); ++index) {
			String itemFilename = comboFilename.getItem(index).trim();
			if (filename.equals(itemFilename)) {
				filenameInList = true;
			} else if (noFilenames <= 10 && itemFilename.length() > 0) {
				if (noFilenames > 0) {
					fileHistory.append(FILE_HISTORY_SPLIT_CHARACTER);
				}
				fileHistory.append(itemFilename);
				++noFilenames;
			}
		}
		if (!filenameInList && filename.length() > 0) {
			comboFilename.add(filename);
		}
		return fileHistory.toString();
	}

	private void browse() {
		FileDialog dialog = new FileDialog(Display.getDefault().getActiveShell(), SWT.SAVE);
		dialog.setText("Select log file");
		String[] filter = {"*.csv", "*.*" };
		dialog.setFilterExtensions(filter);
		dialog.setFileName("output.csv");

		if (comboFilename.getItemCount() > 0) {
			File file = new File(comboFilename.getText());
			dialog.setFilterPath(file.getParent());
		}
		String newFilename = dialog.open();
		if (newFilename == null) {
			return;
		}

		comboFilename.add(newFilename);
		comboFilename.setText(newFilename);
		saveFileHistory();
	}

	private void enableFileComponents() {
		if (buttonOutputToFile.getSelection()) {
			comboFilename.setEnabled(false);
			buttonBrowse.setEnabled(false);
		} else {
			comboFilename.setEnabled(true);
			buttonBrowse.setEnabled(true);
		}
	}

	private void validateFilename() {
		if (comboFilename.getText().length() == 0) {
			comboFilename.setBackground(COLOR_ERROR);
			validFileName = false;
		} else {
			comboFilename.setBackground(COLOR_OK);
			validFileName = true;
		}
	}

	private void autoResizeColumns() {
		getTable().addListener(SWT.Paint, new Listener() { 
			@Override public void handleEvent(Event arg0) {
				lastVisibleColumnIndex = getLastVisibleColumnIndex();
				handleColumnResize();
				getTable().removeListener(SWT.Paint, this);
			}
		});
	}

	private void handleColumnResize() {
		for (int index = 1; index <= bbFilterView.getUsedColumns().size(); ++index) {
			if (COLUMN_WIDTH.containsKey(getTable().getDataValueByPosition(index, 0))) {
				setColumnWidthByPosition(getTable().getColumnIndexByPosition(index), COLUMN_WIDTH.get(getTable().getDataValueByPosition(index, 0)).intValue());
			} else {
				InitializeAutoResizeColumnsCommand columnCommand = new InitializeAutoResizeColumnsCommand(getTable(), index, getTable().getConfigRegistry(), new GCFactory(getTable())); 
				getTable().doCommand(columnCommand);
			}
		}
	} 

	private int getLastVisibleColumnIndex() {
		return getTable().getColumnIndexByPosition(getTable().getColumnCount() - 1);
	}

	private void updateColumnResize() {
		synchronized (combo) {
			if (getLastVisibleColumnIndex() > lastVisibleColumnIndex) {
				lastVisibleColumnIndex = getLastVisibleColumnIndex();
				handleColumnResize();
			}
		}
	}
}
