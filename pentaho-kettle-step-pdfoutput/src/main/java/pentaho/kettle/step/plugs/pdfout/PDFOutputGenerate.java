package pentaho.kettle.step.plugs.pdfout;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.pentaho.di.core.Const;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;



/**
 * This class is used to generate the PDF document for the incoming rows.
 * @author Rishu
 *
 */

public class PDFOutputGenerate {
	
	public void generatePDF(String OutputFileName) throws IOException{
		
		
		if( Const.isWindows() )
	    {
	    	if( OutputFileName.startsWith("file:///") ) OutputFileName=OutputFileName.substring(8);
	    	OutputFileName=OutputFileName.replace("\\", "\\\\");
	    }
		
		Document document=new Document();
		
		PdfWriter writer;
		try {
			writer = PdfWriter.getInstance(document, new FileOutputStream(OutputFileName));
		
		document.open();
		
		document.add(new Paragraph("Hello Rishu Here !!"));
		
		/*
		 * Setting up File Attributes - Document Description
		 */
		document.addAuthor("Rishu Shrivastava");
	    document.addCreationDate();
	    document.addCreator("Rishu");
	    document.addTitle("Set Attribute Example");
	    document.addSubject("An example to show how attributes can be added to pdf files.");
		
		
		
		document.close();
		writer.close();			
		
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		
	}

}
