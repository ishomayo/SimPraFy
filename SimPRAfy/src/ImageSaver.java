
import javax.swing.JPanel;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import javax.imageio.ImageIO;
import java.awt.Graphics2D;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.awt.PdfGraphics2D;
import com.itextpdf.text.Rectangle;

public class ImageSaver{
	private JPanel panel = new JPanel();
	public boolean hasError = false;

	public ImageSaver(){}

	public ImageSaver(JPanel panel){
		if (panel != null) {
			this.panel = panel;
		} else {
			throw new IllegalArgumentException("Panel cannot be null");
		}	
	}	

	public void saveAsImage(){
		try{
			BufferedImage bi = new BufferedImage(panel.getWidth(), panel.getHeight(), BufferedImage.TYPE_INT_ARGB);
			panel.paint(bi.getGraphics());
			ImageIO.write(bi, "PNG", new File(getFileName()+".png"));
		}catch(Exception e){
			hasError = true;
		}

	}

	public void saveAsPDF(){
		Rectangle pageSize = new Rectangle(0,panel.getHeight(), panel.getWidth(), 0);
		Document document = new Document(pageSize);
		try {
		    PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(new File(getFileName()+".pdf")));
		    document.open();
		    
		    PdfContentByte contentByte = writer.getDirectContent();
		    
	    	PdfTemplate template = contentByte.createTemplate(panel.getWidth(),panel.getHeight());
		    Graphics2D g2 = new PdfGraphics2D(template, panel.getWidth(),panel.getHeight());
			panel.print(g2);
			g2.dispose();
			contentByte.addTemplate(template, 0,0);

		} catch (Exception e) {
		    hasError = true;
		}
		finally{
		    if(document.isOpen()){
		        document.close();
		    }
		}
	}

	public String getFileName(){
		LocalDateTime now = LocalDateTime.now();
		String format = now.format(DateTimeFormatter.ofPattern("MMddyy_HHmmss", Locale.ENGLISH));
		return format+"_PG";
	}

	public boolean getHasError(){
		return this.hasError;
	}
}