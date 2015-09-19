/*******************************************************************************
 *
 * PDF Output Writer - Pentaho Kettle Step plugin
 *
 * Author: Rishu Shrivastava
 * 
 *
 *******************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 ******************************************************************************/

package pentaho.kettle.step.plugs.pdfout;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.pentaho.di.core.Const;
import org.pentaho.di.core.Props;
import org.pentaho.di.core.exception.KettleStepException;
import org.pentaho.di.core.row.RowMeta;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.core.row.ValueMeta;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.BaseStepMeta;
import org.pentaho.di.trans.step.StepDialogInterface;
import org.pentaho.di.ui.core.dialog.EnterSelectionDialog;
import org.pentaho.di.ui.core.widget.ColumnInfo;
import org.pentaho.di.ui.core.widget.TableView;
import org.pentaho.di.ui.core.widget.TextVar;
import org.pentaho.di.ui.trans.step.BaseStepDialog;


/**
 * This Dialog Class is a part of the PDFOutput Plugin.
 * 
 * @author Rishu Shrivastava
 *
 */


public class PDFOutputDialog extends BaseStepDialog implements StepDialogInterface{
	
	private static Class<?> PKG = PDFOutputDialog.class;   // for il8n purposes

	private PDFOutputMeta meta;
	
	private CTabFolder wTabFolder;
	private CTabItem wFileTab, wFieldsTab;

	private TextVar wFieldName;
	private Button wFieldNameBrowse;
	//private Text wExtNameTxt;
	private Button wFullFile;
	private TableView wKeys;
	ColumnInfo fieldColumn=null;
	private ColumnInfo[] cikeys;
	private Button wGetFields;
	
	//private Map<String, Integer> prevField;
	
	private RowMetaInterface prevFields=null;
	

	public PDFOutputDialog(Shell parent, Object in, TransMeta transMeta, String sname) {
		super(parent, (BaseStepMeta) in, transMeta, sname);
		meta = (PDFOutputMeta) in;
	}

	public String open() {

		// store some convenient SWT variables
		Shell parent = getParent();
		Display display = parent.getDisplay();

		// SWT code for preparing the dialog
		shell = new Shell(parent, SWT.DIALOG_TRIM | SWT.RESIZE | SWT.MIN
				| SWT.MAX);
		props.setLook(shell);
		setShellImage(shell, meta);

		// The ModifyListener used on all controls. It will update the meta
		// object to
		// indicate that changes are being made.
		ModifyListener lsMod = new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				meta.setChanged();
			}
		};

		// Save the value of the changed flag on the meta object. If the user
				// cancels
				// the dialog, it will be restored to this saved value.
				// The "changed" variable is inherited from BaseStepDialog
		changed = meta.hasChanged();
		
		// ------------------------------------------------------- //
		// SWT code for building the actual settings dialog //
		// ------------------------------------------------------- //
		FormLayout formLayout = new FormLayout();
		formLayout.marginWidth = Const.FORM_MARGIN;
		formLayout.marginHeight = Const.FORM_MARGIN;
		
		shell.setLayout(formLayout);
		shell.setText(BaseMessages.getString(PKG, "PDFOutput.Shell.Title"));
		
		int middle = props.getMiddlePct();
		int margin = Const.MARGIN;

		// Stepname line
		wlStepname = new Label(shell, SWT.RIGHT);
		wlStepname.setText(BaseMessages.getString(PKG, "System.Label.StepName"));
		props.setLook(wlStepname);
		fdlStepname = new FormData();
		fdlStepname.left = new FormAttachment(0, 0);
		fdlStepname.right = new FormAttachment(middle, -margin);
		fdlStepname.top = new FormAttachment(0, margin);
		wlStepname.setLayoutData(fdlStepname);

		wStepname = new Text(shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
		wStepname.setText(stepname);
		props.setLook(wStepname);
		wStepname.addModifyListener(lsMod);
		fdStepname = new FormData();
		fdStepname.left = new FormAttachment(middle, 0);
		fdStepname.top = new FormAttachment(0, margin);
		fdStepname.right = new FormAttachment(100, 0);
		wStepname.setLayoutData(fdStepname);
		
		
		wTabFolder = new CTabFolder(shell, SWT.BORDER);
 		props.setLook(wTabFolder, Props.WIDGET_STYLE_TAB);
 		wTabFolder.setSimple(false);
		
		/*
		 * ----------- START OF THE FILE TAB -----------------------
		 */
 		wFileTab=new CTabItem(wTabFolder, SWT.NONE);
		wFileTab.setText(BaseMessages.getString(PKG, "PDFOutput.Shell.FileTab"));
		
		Composite wFileComp = new Composite(wTabFolder, SWT.NONE);
 		props.setLook(wFileComp);

		FormLayout fileLayout = new FormLayout();
		fileLayout.marginWidth  = 3;
		fileLayout.marginHeight = 3;
		wFileComp.setLayout(fileLayout);
		
		
		// Filename along with BROWSE button
		Label wlValName = new Label(wFileComp, SWT.RIGHT);
		wlValName.setText(BaseMessages.getString(PKG,"PDFOutput.Shell.FieldName.Label"));
		props.setLook(wlValName);
		FormData fdlValName = new FormData();
		fdlValName.left = new FormAttachment(0, 0);
		fdlValName.top  = new FormAttachment(0, margin);
		fdlValName.right= new FormAttachment(middle, -margin);
		wlValName.setLayoutData(fdlValName);

		wFieldNameBrowse=new Button(wFileComp, SWT.PUSH |  SWT.CENTER); //File Browse Button
		props.setLook(wFieldNameBrowse);
		wFieldNameBrowse.setText(BaseMessages.getString(PKG,"PDFOutput.Shell.FieldNameBrowse.Label"));
		FormData fdValNameBrowse = new FormData();
		fdValNameBrowse.right= new FormAttachment(100, 0);
		fdValNameBrowse.top  = new FormAttachment(0, 0);
		wFieldNameBrowse.setLayoutData(fdValNameBrowse);
		
		wFieldNameBrowse.addListener(SWT.Selection, new Listener(){ //Listener for Browse Button
		      
			public void handleEvent(Event arg0) {
				 FileDialog dialog = new FileDialog(shell);
				 dialog.setFilterExtensions(new String[] { "*.pdf","*" });
				 
				 dialog.setFilterNames(new String[] {BaseMessages.getString(PKG, "PDFOutput.Shell.FileTypeExt"),BaseMessages.getString(PKG, "System.FileType.AllFiles")});

		         String filePath = dialog.open();

		         if(filePath != null){
		         	 dialog.setFileName(transMeta.environmentSubstitute(filePath));
		         }
		         
		         if(dialog.open()!=null){
		        	 
		        	wFieldName.setText(dialog.getFilterPath()+System.getProperty("file.separator")+dialog.getFileName()+".pdf");
		        	 
		         }
				meta.setOutputFileName(wFieldName.getText()); //change once the test is over
			}
	    });
		
		wFieldName = new TextVar(transMeta,wFileComp, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
		props.setLook(wFieldName);
		wFieldName.addModifyListener(lsMod);
		FormData fdValName = new FormData();
		fdValName.left = new FormAttachment(middle, 0);
		fdValName.top  = new FormAttachment(0, margin);
		fdValName.right= new FormAttachment(wFieldNameBrowse, -margin);
		wFieldName.setLayoutData(fdValName);
		
		
		
		/*File Extension		
		Label wlExtName = new Label(wFileComp, SWT.RIGHT);
		wlExtName.setText(BaseMessages.getString(PKG,"PDFOutput.Shell.FileExtension.Label"));
		props.setLook(wlExtName);
		FormData fdlExtName = new FormData();
		fdlExtName.left = new FormAttachment(0, 0);
		fdlExtName.right = new FormAttachment(middle, -margin);
		fdlExtName.top = new FormAttachment(wFieldNameBrowse, margin);
		wlExtName.setLayoutData(fdlExtName);
		
		wExtNameTxt = new Text(wFileComp, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
		props.setLook(wExtNameTxt);
		wExtNameTxt.setText(BaseMessages.getString(PKG,"PDFOutput.Shell.FileExtensionFormat.Label"));
		wExtNameTxt.addModifyListener(lsMod);
		FormData fdExtName = new FormData();
		fdExtName.left = new FormAttachment(middle, 0);
		fdExtName.right = new FormAttachment(80, 0);
		fdExtName.top = new FormAttachment(wFieldNameBrowse, margin);
		wExtNameTxt.setLayoutData(fdExtName);*/
		
		wFullFile=new Button(wFileComp, SWT.PUSH); //Full FileName Button
		props.setLook(wFullFile);
		wFullFile.setText(BaseMessages.getString(PKG,"PDFOutput.Shell.FullFileName.Label"));
		FormData fdFullFile = new FormData();
		fdFullFile.left = new FormAttachment(middle, 0);
		//fdFullFile.right = new FormAttachment(100, 0);
		fdFullFile.top = new FormAttachment(wFieldNameBrowse, margin);
		wFullFile.setLayoutData(fdFullFile);

		wFullFile.addSelectionListener(new SelectionAdapter() {	// Listener for full filename button
			public void widgetSelected(SelectionEvent e) 
			{
				String files[] = {meta.getOutputFileName()};
				
				if (files!=null && files.length>0)
				{
					EnterSelectionDialog esd = new EnterSelectionDialog(shell, files,
							BaseMessages.getString(PKG, "PDFOutput.Shell.FullFileName.Dialog.Title"),
							BaseMessages.getString(PKG, "PDFOutput.Shell.FullFileName.Dialog.OutputMessage"));
					esd.setViewOnly();
					esd.open();
				}
				else
				{
					MessageBox mb = new MessageBox(shell, SWT.OK | SWT.ICON_ERROR );
					mb.setMessage(BaseMessages.getString(PKG, "PDFOutput.Shell.FullFileName.Dialog.NoFileFound"));
					mb.setText(BaseMessages.getString(PKG, "System.Dialog.Error.Title"));
					mb.open(); 
				}
			}
			
		});
		
		
		
		FormData fdFileComp=new FormData();
		fdFileComp.left  = new FormAttachment(0, 0);
		fdFileComp.top   = new FormAttachment(0, 0);
		fdFileComp.right = new FormAttachment(100, 0);
		fdFileComp.bottom= new FormAttachment(100, 0);
		wFileComp.setLayoutData(fdFileComp);
	
		wFileComp.layout();
		wFileTab.setControl(wFileComp);
		
		/* ----------- END OF FILE TAB ------------------------*/
		
		
		/*
		 * ------------ START OF FIELDS TAB ----------------------
		 */
		
		wFieldsTab = new CTabItem(wTabFolder, SWT.NONE);
		wFieldsTab.setText(BaseMessages.getString(PKG, "PDFOutput.Shell.FieldTab"));
		
		FormLayout fieldsLayout = new FormLayout ();
		fieldsLayout.marginWidth  = Const.FORM_MARGIN;
		fieldsLayout.marginHeight = Const.FORM_MARGIN;
		
		Composite wFieldsComp = new Composite(wTabFolder, SWT.NONE);
		wFieldsComp.setLayout(fieldsLayout);
 		props.setLook(wFieldsComp);
		
 		//Get Fields Button
        wGetFields=new Button(wFieldsComp, SWT.PUSH);
		props.setLook(wGetFields);
		wGetFields.setText(BaseMessages.getString(PKG,"PDFOutput.Shell.GetFields.Label"));
	
		setButtonPositions(new Button[] { wGetFields }, margin, null);
		
		int keyCols=4;
        int keyrows= (meta.getKeyField()!=null?meta.getKeyField().length:1);
        
        
		cikeys=new ColumnInfo[keyCols];
		cikeys[0]=new ColumnInfo(BaseMessages.getString(PKG, "PDFOutput.ColumnInfo.KeyField"),    ColumnInfo.COLUMN_TYPE_CCOMBO,  new String[]{}, false); 
		cikeys[1]=new ColumnInfo(BaseMessages.getString(PKG, "PDFOutput.ColumnInfo.ValueField"),  ColumnInfo.COLUMN_TYPE_TEXT, false); 
		//cikeys[2]=new ColumnInfo(BaseMessages.getString(PKG, "PDFOutput.ColumnInfo.DefaultField"),     ColumnInfo.COLUMN_TYPE_TEXT,   false); 
		cikeys[2]=new ColumnInfo(BaseMessages.getString(PKG, "PDFOutput.ColumnInfo.Type"),        ColumnInfo.COLUMN_TYPE_CCOMBO, ValueMeta.getTypes()); 
		cikeys[3]=new ColumnInfo(BaseMessages.getString(PKG, "PDFOutput.ColumnInfo.Format"),      ColumnInfo.COLUMN_TYPE_FORMAT, 4);
		//cikeys[5]=new ColumnInfo(BaseMessages.getString(PKG, "PDFOutput.ColumnInfo.Length"),      ColumnInfo.COLUMN_TYPE_TEXT,   false);
        //cikeys[6]=new ColumnInfo(BaseMessages.getString(PKG, "PDFOutput.ColumnInfo.Precision"),   ColumnInfo.COLUMN_TYPE_TEXT,   false);
        //cikeys[7]=new ColumnInfo(BaseMessages.getString(PKG, "PDFOutput.ColumnInfo.Currency"),    ColumnInfo.COLUMN_TYPE_TEXT,   false);
        //cikeys[8]=new ColumnInfo(BaseMessages.getString(PKG, "PDFOutput.ColumnInfo.Decimal"),     ColumnInfo.COLUMN_TYPE_TEXT,   false);
        //cikeys[9]=new ColumnInfo(BaseMessages.getString(PKG, "PDFOutput.ColumnInfo.Group"),       ColumnInfo.COLUMN_TYPE_TEXT,   false);
         
        fieldColumn=cikeys[0];
        
        wKeys=new TableView(transMeta, wFieldsComp, 
                              SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL, 
                              cikeys, 
                              keyrows,  
                              lsMod,
                              props
                              );
 
        FormData fdReturn=new FormData();
        fdReturn.left  = new FormAttachment(0, 0);
        fdReturn.top   = new FormAttachment(0, 0);
        fdReturn.right = new FormAttachment(100, 0);
        fdReturn.bottom= new FormAttachment(wGetFields, -margin);
        wKeys.setLayoutData(fdReturn); 
		
        
        wFieldsComp.layout();
		wFieldsTab.setControl(wFieldsComp);

		FormData fdTabFolder = new FormData();
		fdTabFolder.left  = new FormAttachment(0, 0);
		fdTabFolder.top   = new FormAttachment(wStepname, margin);
		fdTabFolder.right = new FormAttachment(100, 0);
		fdTabFolder.bottom= new FormAttachment(100, -50);
		wTabFolder.setLayoutData(fdTabFolder);
		
		
		//listener for GETFIELDS Tab
		wGetFields.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e){
				setComboBox();
			}
			
		});
		
		
		
		
		
		/*------------ END OF THE FIELD TAB -----------------*/
		
		
		
		
		// OK and cancel buttons and Get Fields Button
		wOK = new Button(shell, SWT.PUSH);
		wOK.setText(BaseMessages.getString(PKG, "System.Button.OK"));
		wCancel = new Button(shell, SWT.PUSH);
		wCancel.setText(BaseMessages.getString(PKG, "System.Button.Cancel"));

		BaseStepDialog.positionBottomButtons(shell,new Button[] { wOK, wCancel }, margin, wTabFolder);

		// Add listeners for cancel and OK
		lsCancel = new Listener() {public void handleEvent(Event e) {cancel();}};
		lsOK = new Listener() {public void handleEvent(Event e) {ok();}};
		

		wCancel.addListener(SWT.Selection, lsCancel);
		wOK.addListener(SWT.Selection, lsOK);

		// default listener (for hitting "enter")
		lsDef = new SelectionAdapter() {
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				ok();
			}
		};
		
		wStepname.addSelectionListener(lsDef);
		wFieldName.addSelectionListener(lsDef);
		
		
		// Detect X or ALT-F4 or something that kills this window and cancel the
		// dialog properly
		shell.addShellListener(new ShellAdapter() {
			@Override
			public void shellClosed(ShellEvent e) {
				cancel();
			}
		});
		
		
		//select always the first tab
		wTabFolder.setSelection(0); 
		
		// Set/Restore the dialog size based on last position on screen
		// The setSize() method is inherited from BaseStepDialog
		setSize();

		setComboBox();
		
		// populate the dialog with the values from the meta object
		populateDialog();
		
		// restore the changed flag to original value, as the modify listeners
		// fire during dialog population
		meta.setChanged(changed);

		// open dialog and enter event loop
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}

		// at this point the dialog has closed, so either ok() or cancel() have
		// been executed
		// The "stepname" variable is inherited from BaseStepDialog
		return stepname;
	}

	
	
	/*
	 * Setting up the FIELDS Tab section having the data fields from the prev. step
	 */
	private void setComboBox() {
		Runnable fieldLoader=new Runnable() {
			public void run() {
				
				try {
					prevFields=transMeta.getPrevStepFields(stepname);
					
				} catch (KettleStepException e) {		
					prevFields=new RowMeta();
					logError("Unable to Find Input Fields");
				}
				
				//prevField=new HashMap<String, Integer>();
				
				String[] prevStepFieldsNames=prevFields.getFieldNames();
				String[] prevStepFieldNamesandType=prevFields.getFieldNamesAndTypes(0);
				logBasic("Prev Step FieldName and Type : "+prevStepFieldNamesandType[0]);
				
				/*for(int i=0;i<prevStepFieldsNames.length; i++){
					prevField.put(prevStepFieldsNames[i],Integer.valueOf(i));
					
				}*/
						
				cikeys[0].setComboValues(prevStepFieldsNames);
				
				//if(meta.getInputDropDataIndex()!=null){ //checking for the previously selected entry
				//	int int_index=Integer.parseInt(meta.getInputDropDataIndex());
				//	wInputDrop.select(int_index);
				//}
				
				
			}
		};
		new Thread(fieldLoader).run();
		
	}

	
		
	/**
	 * populateDialog method is called when we open or re-open the dialog. 
	 * Also displays the last selections done by the user.
	 * 
	 */
	private void populateDialog() {
		wStepname.selectAll();

		if(meta.getOutputFileName()!=null){
			wFieldName.setText(meta.getOutputFileName());
		}
		
	}

	
	/**
	 * Called when the user cancels the dialog.
	 */
	private void cancel() {
		// The "stepname" variable will be the return value for the open()
		// method.
		// Setting to null to indicate that dialog was cancelled.
		stepname = null;
		// Restoring original "changed" flag on the met a object
		meta.setChanged(changed);
		// close the SWT dialog window
		dispose();
	}

	/**
	 * Called when the user confirms the dialog
	 */
	private void ok() {
		// The "stepname" variable will be the return value for the open()
		// method.
		// Setting to step name from the dialog control
		stepname = wStepname.getText();
	
		// Setting the settings to the meta object
		meta.setOutputFileName(wFieldName.getText());
	
		// close the SWT dialog window
		dispose();
	}

	public RowMetaInterface getPrevFields() {
		return prevFields;
	}

	public void setPrevFields(RowMetaInterface prevFields) {
		this.prevFields = prevFields;
	}

	
	

}
