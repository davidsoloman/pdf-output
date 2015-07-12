package pentaho.kettle.step.plugs.pdfout;

import java.util.List;
import java.util.Map;

import org.eclipse.swt.widgets.Shell;
import org.pentaho.di.core.CheckResult;
import org.pentaho.di.core.CheckResultInterface;
import org.pentaho.di.core.Counter;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.exception.KettleValueException;
import org.pentaho.di.core.exception.KettleXMLException;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.core.row.ValueMeta;
import org.pentaho.di.core.row.ValueMetaInterface;
import org.pentaho.di.core.variables.VariableSpace;
import org.pentaho.di.core.xml.XMLHandler;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.repository.ObjectId;
import org.pentaho.di.repository.Repository;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.BaseStepMeta;
import org.pentaho.di.trans.step.StepDataInterface;
import org.pentaho.di.trans.step.StepDialogInterface;
import org.pentaho.di.trans.step.StepInterface;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.step.StepMetaInterface;
import org.w3c.dom.Node;



/**
 * This Meta class is a part of the PDFOutput Step.
 * @author Rishu Shrivastava
 *
 */

public class PDFOutputMeta extends BaseStepMeta implements StepMetaInterface {
	
	private static Class<?> PKG = PDFOutputMeta.class;
	
	private String outputField;
	
	public PDFOutputMeta(){
		super();
	}
	
	public StepDialogInterface getDialog(Shell shell, StepMetaInterface meta,
			TransMeta transMeta, String name) {
		return new PDFOutputDialog(shell, meta, transMeta, name);
	}

	public StepInterface getStep(StepMeta stepMeta,
			StepDataInterface stepDataInterface, int cnr, TransMeta transMeta,
			Trans disp) {
		return new PDFOutputStep(stepMeta, stepDataInterface, cnr, transMeta, disp);
	}

	public StepDataInterface getStepData() {
		// TODO Auto-generated method stub
		return new PDFOutputData();
	}

	public void setDefault() {
		// default output field name
		outputField = "Result";
	}

	@Override
	public Object clone() {
		
		PDFOutputMeta retval= (PDFOutputMeta) super.clone();
		return retval;
	}

	@Override
	public String getXML() throws KettleValueException {

		// only one field to serialize
		StringBuilder xml=new StringBuilder();
		
		xml.append("      ").append(XMLHandler.addTagValue("outputfield", outputField));
				
		return xml.toString();
	}

	@Override
	public void loadXML(Node stepnode, List<DatabaseMeta> databases,
			Map<String, Counter> counters) throws KettleXMLException {

		try {
			setOutputField(XMLHandler.getNodeValue(XMLHandler.getSubNode(
					stepnode, "outputfield")));
			
		} catch (Exception e) {
			throw new KettleXMLException(
					"Plugin unable to read step info from XML node", e);
		}

	}

	@Override
	public void saveRep(Repository rep, ObjectId id_transformation,
			ObjectId id_step) throws KettleException {
		try {
			rep.saveStepAttribute(id_transformation, id_step, "outputfield", outputField); //$NON-NLS-1$			
			
		} catch (Exception e) {
			throw new KettleException("Unable to save step into repository: "
					+ id_step, e);
		}
	}

	@Override
	public void readRep(Repository rep, ObjectId id_step,
			List<DatabaseMeta> databases, Map<String, Counter> counters)
			throws KettleException {
		try {
			outputField = rep.getStepAttributeString(id_step, "outputfield"); //$NON-NLS-1$
			
		} catch (Exception e) {
			throw new KettleException("Unable to load step from repository", e);
		}
	}

	
	@SuppressWarnings("deprecation")
	@Override
	public void getFields(RowMetaInterface r, String origin,
			RowMetaInterface[] info, StepMeta nextStep, VariableSpace space) {

		/*
		 * This implementation appends the outputField to the row-stream
		 */

		// a value meta object contains the meta data for a field
		ValueMetaInterface v = new ValueMeta();

		// set the name of the new field
		v.setName(outputField);		
		
		// type is going to be string
		v.setType(ValueMetaInterface.TYPE_STRING);
		

		// setting trim type to "both"
		v.setTrimType(ValueMetaInterface.TRIM_TYPE_BOTH);

		// the name of the step that adds this field
		v.setOrigin(origin);

		// modify the row structure and add the field this step generates
		r.addValueMeta(v);

	}

	@Override
	public void check(List<CheckResultInterface> remarks, TransMeta transmeta,
			StepMeta stepMeta, RowMetaInterface prev, String input[],
			String output[], RowMetaInterface info) {

		CheckResult cr;

		// See if there are input streams leading to this step!
		if (input.length > 0) {
			cr = new CheckResult(CheckResultInterface.TYPE_RESULT_OK,
					BaseMessages.getString(PKG,
							"PDFOutput.CheckResult.ReceivingRows.OK"), stepMeta);
			remarks.add(cr);
		} else {
			cr = new CheckResult(CheckResultInterface.TYPE_RESULT_ERROR,
					BaseMessages.getString(PKG,
							"PDFOutput.CheckResult.ReceivingRows.ERROR"), stepMeta);
			remarks.add(cr);
		}

	}
	
	
	
	/*
	 * Pile up getters and setters.
	 */
	
	public String getOutputField() {
		return outputField;
	}

	public void setOutputField(String outputField) {
		this.outputField = outputField;
	}

	
	

}
