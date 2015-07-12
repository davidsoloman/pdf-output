package pentaho.kettle.step.plugs.pdfout;

import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.trans.step.BaseStepData;
import org.pentaho.di.trans.step.StepDataInterface;

/**
 * This data class is a part of the PDFOutput Plugin.
 * @author Rishu Shrivastava
 *
 */


public class PDFOutputData extends BaseStepData implements StepDataInterface {
	
	public RowMetaInterface outputRowMeta;

	public PDFOutputData(){
		super();
	}

}
