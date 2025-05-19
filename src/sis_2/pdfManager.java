/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sis_2;

import POJOS.Contribuyente;
import POJOS.Recibos;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import java.io.IOException;
import java.net.MalformedURLException;
import java.time.ZoneId;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Nicol
 */
public class pdfManager {
    String ruta = "resources/recibos/";
    public final static String imagen = "resources/img.jpg";
    public pdfManager(){
        
    }
    
    public void addRecibo(Recibos rec){
        try {
        Contribuyente c = rec.getContribuyente();

        String apellido1 = c.getApellido1() != null ? c.getApellido1() : "";
        String apellido2 = c.getApellido2() != null ? c.getApellido2() : "";

        String apellidos = (apellido1 + " " + apellido2).trim();
        
        Date fecha = rec.getFechaPadron();

        int anio = fecha.toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()
                        .getYear();

        String anioStr = String.valueOf(anio);

        PdfWriter writer = new PdfWriter(ruta+rec.getContribuyente().getNifnie()+rec.getContribuyente().getNombre()+apellidos+rec.getVehiculos().getMatricula()+anioStr);
        
        PdfDocument pdfDoc = new PdfDocument(writer);
        
        Document doc = new Document(pdfDoc, PageSize.LETTER);
        
        Paragraph empty = new Paragraph("");
        Table tabla1 = new Table(2);
        tabla1.setWidth(500);

        Paragraph nom = new Paragraph(rec.getAyuntamiento());
        Paragraph cif = new Paragraph("P24001017F");

        Paragraph dir1 = new Paragraph("Calle de la Iglesia, 13");
        Paragraph dir2 = new Paragraph("24280 "+rec.getAyuntamiento()+" León");

        Cell cell1 = new Cell();
        cell1.setBorder(new SolidBorder(1));
        cell1.setWidth(250);
        cell1.setTextAlignment(TextAlignment.CENTER);

        cell1.add(nom);
        cell1.add(cif);
        cell1.add(dir1);
        cell1.add(dir2);
        tabla1.addCell(cell1);

        Cell cell2 = new Cell();
        cell2.setBorder(Border.NO_BORDER);
        cell2.setPadding(10);
        cell2.setTextAlignment(TextAlignment.RIGHT);
        cell2.add(new Paragraph("IBAN: "+rec.getIban()));      
        cell2.add(new Paragraph("Fecha de recibo: "+rec.getFechaRecibo().toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()
                        .getYear()));
        cell2.add(new Paragraph("Fecha de matriculacion: "+rec.getVehiculos().getFechaMatriculacion().toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()
                        .getYear()));
        cell2.add(new Paragraph("Fecha de alta: "+rec.getVehiculos().getFechaAlta().toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()
                        .getYear()));
        tabla1.addCell(cell2);
        
        Table tabla2 = new Table(2);
        tabla2.setWidth(500);
        Image img;
        Cell cell3 = new Cell();
        try {
            img = new Image(ImageDataFactory.create(imagen));
            img.setBorder(Border.NO_BORDER);
            img.setPadding(10);
            cell3.add(img);
        } catch (MalformedURLException ex) {
            Logger.getLogger(pdfManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        cell3.setBorder(Border.NO_BORDER);
        cell3.setPaddingLeft(23);
        cell3.setPaddingTop(20);
        cell3.setWidth(250);
        
        Cell cell4 = new Cell();
        cell4.setBorder(new SolidBorder(1));
        cell4.setPadding(10);
        cell4.setTextAlignment(TextAlignment.RIGHT);
        
        cell4.add(new Paragraph("Destinatario:").setTextAlignment(TextAlignment.LEFT));
        cell4.add(new Paragraph(c.getNombre()+" "+apellidos));
        cell4.add(new Paragraph("DNI: "+c.getNifnie()));
        cell4.add(new Paragraph(c.getDireccion()));
        
        tabla2.addCell(cell3);
        tabla2.addCell(cell4);
        
        Table tabla3 = new Table(3);
        tabla3.setWidth(500);
        tabla3.setBorder(new SolidBorder(1));
        
        Cell cell5 = new Cell();
        cell5.setBorder(Border.NO_BORDER);
        cell5.setPadding(10);
        cell5.setTextAlignment(TextAlignment.CENTER);
        cell5.add(new Paragraph("Tipo: " + rec.getTipoVehiculo()));
        
        Cell cell6 = new Cell();
        cell6.setBorder(Border.NO_BORDER);
        cell6.setPadding(10);
        cell6.setTextAlignment(TextAlignment.CENTER);
        cell6.add(new Paragraph(rec.getMarcaModelo()));
        
        Cell cell7 = new Cell();
        cell7.setBorder(Border.NO_BORDER);
        cell7.setPadding(10);
        cell7.setTextAlignment(TextAlignment.CENTER);
        cell7.add(new Paragraph("Matricula: " + rec.getVehiculos().getMatricula()));
        
        tabla3.addCell(cell5);
        tabla3.addCell(cell6);
        tabla3.addCell(cell7);
        
        doc.add(tabla1);
        doc.add(tabla2);

        doc.close();
        } catch (IOException ex) {
            Logger.getLogger(pdfManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}


