package pentaho.kettle.step.plugs.pdfout;


import org.eclipse.swt.SWT;
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
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.pentaho.di.core.Const;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.core.row.ValueMeta;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.BaseStepMeta;
import org.pentaho.di.trans.step.StepDialogInterface;
import org.pentaho.di.ui.core.widget.ColumnInfo;
import org.pentaho.di.ui.core.widget.TableView;
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
	
	private Text wFieldName;
	private Button wFieldNameBrowse;
	private Text wExtNameTxt;
	private Button wFullFile;
	private TableView wKeys;
	//private ColumnInfo fieldColumn = null; 
	private Button wGetFields;
	
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
		
		// Filename along with BROWSE button
		Label wlValName = new Label(shell, SWT.RIGHT);
		wlValName.setText(BaseMessages.getString(PKG,"PDFOutput.Shell.FieldName.Label"));
		props.setLook(wlValName);
		FormData fdlValName = new FormData();
		fdlValName.left = new FormAttachment(0, 0);
		fdlValName.right = new FormAttachment(middle, -margin);
		fdlValName.top = new FormAttachment(wStepname, margin);
		wlValName.setLayoutData(fdlValName);

		wFieldName = new Text(shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
		props.setLook(wFieldName);
		wFieldName.addModifyListener(lsMod);
		FormData fdValName = new FormData();
		fdValName.left = new FormAttachment(middle, 0);
		fdValName.right = new FormAttachment(90, 0);
		fdValName.top = new FormAttachment(wStepname, margin);
		wFieldName.setLayoutData(fdValName);
		
		wFieldNameBrowse=new Button(shell, SWT.PUSH); //File Browse Button
		props.setLook(wFieldNameBrowse);
		wFieldNameBrowse.setText(BaseMessages.getString(PKG,"PDFOutput.Shell.FieldNameBrowse.Label"));
		FormData fdValNameBrowse = new FormData();
		fdValNameBrowse.left = new FormAttachment(wFieldName, 0);
		fdValNameBrowse.right = new FormAttachment(100, 0);
		fdValNameBrowse.top = new FormAttachment(wStepname, margin);
		wFieldNameBrowse.setLayoutData(fdValNameBrowse);
		
		//File Extension		
		Label wlExtName = new Label(shell, SWT.RIGHT);
		wlExtName.setText(BaseMessages.getString(PKG,"PDFOutput.Shell.FileExtension.Label"));
		props.setLook(wlExtName);
		FormData fdlExtName = new FormData();
		fdlExtName.left = new FormAttachment(0, 0);
		fdlExtName.right = new FormAttachment(middle, -margin);
		fdlExtName.top = new FormAttachment(wFieldNameBrowse, margin);
		wlExtName.setLayoutData(fdlExtName);
		
		wExtNameTxt = new Text(shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
		props.setLook(wExtNameTxt);
		wExtNameTxt.setText(BaseMessages.getString(PKG,"PDFOutput.Shell.FileExtensionFormat.Label"));
		wExtNameTxt.addModifyListener(lsMod);
		FormData fdExtName = new FormData();
		fdExtName.left = new FormAttachment(middle, 0);
		fdExtName.right = new FormAttachment(80, 0);
		fdExtName.top = new FormAttachment(wFieldNameBrowse, margin);
		wExtNameTxt.setLayoutData(fdExtName);
		
		wFullFile=new Button(shell, SWT.PUSH); //Full FileName Button
		props.setLook(wFullFile);
		wFullFile.setText(BaseMessages.getString(PKG,"PDFOutput.Shell.FullFileName.Label"));
		FormData fdFullFile = new FormData();
		fdFullFile.left = new FormAttachment(wExtNameTxt, 0);
		fdFullFile.right = new FormAttachment(100, 0);
		fdFullFile.top = new FormAttachment(wFieldNameBrowse, margin);
		wFullFile.setLayoutData(fdFullFile);
		
		
		// Building the Field Section
		Label wlKeys = new Label(shell, SWT.NONE);
		wlKeys.setText(BaseMessages.getString(PKG,"PDFOutput.Shell.Keys.Label"));
		props.setLook(wlKeys);
		FormData fdlKeys = new FormData();
		fdlKeys.left = new FormAttachment(0, 0);
		//fdlKeys.right = new FormAttachment(middle, -margin);
		fdlKeys.top = new FormAttachment(wFullFile, margin);
		wlKeys.setLayoutData(fdlKeys);
		
		int keyCols=10;
        int keyrows= (meta.getKeyField()!=null?meta.getKeyField().length:1);
        
        
		ColumnInfo[] cikeys=new ColumnInfo[keyCols];
		cikeys[0]=new ColumnInfo(BaseMessages.getString(PKG, "PDFOutput.ColumnInfo.KeyField"),    ColumnInfo.COLUMN_TYPE_CCOMBO,  new String[]{}, false); 
		cikeys[1]=new ColumnInfo(BaseMessages.getString(PKG, "PDFOutput.ColumnInfo.ValueField"),  ColumnInfo.COLUMN_TYPE_TEXT, false); 
		cikeys[2]=new ColumnInfo(BaseMessages.getString(PKG, "PDFOutput.ColumnInfo.DefaultField"),     ColumnInfo.COLUMN_TYPE_TEXT,   false); 
		cikeys[3]=new ColumnInfo(BaseMessages.getString(PKG, "PDFOutput.ColumnInfo.Type"),        ColumnInfo.COLUMN_TYPE_CCOMBO, ValueMeta.getTypes()); 
		cikeys[4]=new ColumnInfo(BaseMessages.getString(PKG, "PDFOutput.ColumnInfo.Format"),      ColumnInfo.COLUMN_TYPE_FORMAT, 4);
		cikeys[5]=new ColumnInfo(BaseMessages.getString(PKG, "PDFOutput.ColumnInfo.Length"),      ColumnInfo.COLUMN_TYPE_TEXT,   false);
        cikeys[6]=new ColumnInfo(BaseMessages.getString(PKG, "PDFOutput.ColumnInfo.Precision"),   ColumnInfo.COLUMN_TYPE_TEXT,   false);
        cikeys[7]=new ColumnInfo(BaseMessages.getString(PKG, "PDFOutput.ColumnInfo.Currency"),    ColumnInfo.COLUMN_TYPE_TEXT,   false);
        cikeys[8]=new ColumnInfo(BaseMessages.getString(PKG, "PDFOutput.ColumnInfo.Decimal"),     ColumnInfo.COLUMN_TYPE_TEXT,   false);
        cikeys[9]=new ColumnInfo(BaseMessages.getString(PKG, "PDFOutput.ColumnInfo.Group"),       ColumnInfo.COLUMN_TYPE_TEXT,   false);
         
        //fieldColumn=cikeys[0];
        
        wKeys=new TableView(transMeta, shell, 
                              SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL, 
                              cikeys, 
                              keyrows,  
                              lsMod,
                              props
                              );
 
        FormData fdReturn=new FormData();
        fdReturn.left  = new FormAttachment(0, 0);
        fdReturn.top   = new FormAttachment(wlKeys, margin);
        fdReturn.right = new FormAttachment(100, 0);
        fdReturn.bottom= new FormAttachment(100, -50);
        wKeys.setLayoutData(fdReturn); 
		
        //Get Fields Button
        wGetFields=new Button(shell, SWT.PUSH); 
		props.setLook(wGetFields);
		wGetFields.setText(BaseMessages.getString(PKG,"PDFOutput.Shell.GetFields.Label"));
		FormData fdGetFields = new FormData();
		//fdGetFields.left = new FormAttachment(middle, 0);
		fdGetFields.right = new FormAttachment(middle, 0);
		fdGetFields.top = new FormAttachment(wKeys, margin);
		wGetFields.setLayoutData(fdGetFields);
		
		
		
		// OK and cancel buttons
		wOK = new Button(shell, SWT.PUSH);
		wOK.setText(BaseMessages.getString(PKG, "System.Button.OK"));
		wCancel = new Button(shell, SWT.PUSH);
		wCancel.setText(BaseMessages.getString(PKG, "System.Button.Cancel"));

		BaseStepDialog.positionBottomButtons(shell,
				new Button[] { wOK, wCancel }, margin, wGetFields);

		// Add listeners for cancel and OK
		lsCancel = new Listener() {
			public void handleEvent(Event e) {
				cancel();
			}
		};
		lsOK = new Listener() {
			public void handleEvent(Event e) {
				ok();
			}
		};

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
		
		
		
		/*
		 * Listener for BROWSE Button
		 */
		wFieldNameBrowse.addListener(SWT.Selection, new Listener(){
	      
			public void handleEvent(Event arg0) {
				// TODO Auto-generated method stub
				 FileDialog dialog = new FileDialog(shell);

		         String filePath = dialog.open();

		         if(filePath != null)
		        	 wFieldName.setText(filePath);
				
			}
	    });
		
		
		// Detect X or ALT-F4 or something that kills this window and cancel the
		// dialog properly
		shell.addShellListener(new ShellAdapter() {
			@Override
			public void shellClosed(ShellEvent e) {
				cancel();
			}
		});
		
				
		// Set/Restore the dialog size based on last position on screen
		// The setSize() method is inherited from BaseStepDialog
		setSize();

		// populate the dialog with the values from the meta object
		populateDialog();
		
		//set asynchronous listing of the dropdown combo box
		

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
