/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sis_2;

import POJOS.Contribuyente;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.OutputKeys;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.w3c.dom.DOMImplementation;


/**
 *
 * @author gar27
 */
public class XmlManager {
    FileInputStream fi;
    DocumentBuilderFactory factory;
    DocumentBuilder builder;
    DOMImplementation implementation;
    FileOutputStream fo;
    Document ErroresNIFNIE;
    
    
    
    public static void main(String[] args) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();

            // Elemento raíz
            Element rootElement = doc.createElement("ErroresNIFNIE");
            doc.appendChild(rootElement);

            // Elemento Contribuyentes
            Element contribuyentes = doc.createElement("Contribuyentes");
            rootElement.appendChild(contribuyentes);

            
            // Escribir el contenido en un archivo XML
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("ErroresNifNie.xml"));
            transformer.transform(source, result);

            System.out.println("Archivo XML creado con éxito");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    private void inicializar() {
        try{
            sc = new Scanner(System.in);
            listaNIFNIE = new String[10];
            listaEmails = new String[10];
            fi = new FileInputStream("resources/SistemasAgua.xlsx");
            libro = new XSSFWorkbook(fi);
            hoja1 = libro.getSheetAt(0);
            hoja2 = libro.getSheetAt(1);
            factory = DocumentBuilderFactory.newInstance();
            builder = factory.newDocumentBuilder();
            implementation = builder.getDOMImplementation();
            documentoNIFNIE = implementation.createDocument(null, "contribuyentes", null);
            documentoNIFNIE.setXmlVersion("1.0");
            //Element contribuyentes = documentoNIFNIE.createElement("contribuyentes");
            documentoCCC = implementation.createDocument(null, "cuentas", null);
            documentoCCC.setXmlVersion("1.0");
            documentoRecibos = implementation.createDocument(null, "Recibos", null);
            documentoRecibos.setXmlVersion("1.0");
            
            dao = new DAO();
        }catch (IOException | ParserConfigurationException ex){
            System.out.println(ex.getMessage());
        }
    }
    
    /*
    <Contribuyente id="2">
    <NIF_NIE>125C</NIF_NIE>
    <Nombre>Juan</Nombre>
    <PrimerApellido>Martinez</PrimerApellido>
    <SegundoApellido>Dominguez</SegundoApellido>
    <TipoDeError>NIF ERRONEO</TipoDeError>
    </Contribuyente>
    */

    public static void agregarContribuyente(Contribuyente con){
        agregarContribuyente(doc,contribuyentes,nifNie,error,con);
    }
    
    
    private static void agregarContribuyente(Document doc, Element contribuyentes, String nifNie, String error, Contribuyente con) {
        Element contribuyente = doc.createElement("Contribuyente");
        contribuyentes.appendChild(contribuyente);

        Element nifNieElement = doc.createElement("NIF_NIE");
        nifNieElement.appendChild(doc.createTextNode(con.getNifnie()));
        contribuyente.appendChild(nifNieElement);
        
        Element nombreElement = doc.createElement("Nombre");
        nombreElement.appendChild(doc.createTextNode(con.getNombre()));
        contribuyente.appendChild(nombreElement);

        Element primerApellidoElement = doc.createElement("PrimerApellido");
        primerApellidoElement.appendChild(doc.createTextNode(con.getApellido1()));
        contribuyente.appendChild(primerApellidoElement);
        
        if("".equals(con.getApellido2())|| con.getApellido2()==null){
            Element segundoApellidoElement = doc.createElement("SegundoApellido");
            nombreElement.appendChild(doc.createTextNode(con.getApellido2()));
            contribuyente.appendChild(segundoApellidoElement);
        }
        
        Element tipoDeErrorElement = doc.createElement("TipoDeError");
        primerApellidoElement.appendChild(doc.createTextNode(con.getErrNif()));
        contribuyente.appendChild(tipoDeErrorElement);

        Element errorElement = doc.createElement("Error");
        errorElement.appendChild(doc.createTextNode(error));
        contribuyente.appendChild(errorElement);
    }
}

        
    }
    
    public boolean addToXml(){
        // Añadir nuevo elemento
        Element newCont = doc.createElement("nuevo_elemento");
        nuevoCampo.setTextContent("Contenido");
        rootElement.appendChild(nuevoCampo);

        // Añadir comentario
        Comment comentario = doc.createComment("Ejemplo de comentario");
        rootElement.appendChild(comentario);

        // Añadir CDATA para contenido especial
        Element contenidoEspecial = doc.createElement("contenido");
        CDATASection cdata = doc.createCDATASection("<html>Etiquetas</html>");
        contenidoEspecial.appendChild(cdata);

    }

}


