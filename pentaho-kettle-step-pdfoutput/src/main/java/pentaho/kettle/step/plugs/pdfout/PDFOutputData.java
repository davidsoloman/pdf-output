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
