package pentaho.kettle.step.plugs.pdfout;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;


/**
 * This class is used to generate the PDF document for the incoming rows.
 * @author Rishu
 *
 */

public class PDFOutputGenerate {
	
	public void generatePDF(String OutputFileName){
		
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
