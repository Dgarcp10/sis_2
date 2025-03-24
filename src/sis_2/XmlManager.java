/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sis_2;

import POJOS.Contribuyente;
import java.io.File;
import java.io.FileOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;


/**
 *
 * @author gar27
 * @author ngarng00
 */
public class XmlManager {
    Document documentoNif;
    Element rootElemNIF;
    File archivoNif;
    Document documentoCcc;
    Element rootElemCcc;
    File archivoCcc;
     
    public XmlManager() {
        
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            
            documentoNif = builder.newDocument();
            documentoNif.setXmlVersion("1.0");
            
            rootElemNIF = documentoNif.createElement("Contribuyentes");
            documentoNif.appendChild(rootElemNIF);
            String rutaArchivoNif = "resources/";
            String nombreArchivoNif = "ErroresNifNie.xml";
            archivoNif = new File(rutaArchivoNif + nombreArchivoNif);
            
            documentoNif = builder.newDocument();
            documentoNif.setXmlVersion("1.0");
            
            rootElemCcc = documentoCcc.createElement("Cuentas");
            documentoNif.appendChild(rootElemNIF);
            String rutaArchivoCcc = "resources/";
            String nombreArchivoCcc = "ErroresCcc.xml";
            archivoNif = new File(rutaArchivoCcc + nombreArchivoCcc);
            
        } catch (ParserConfigurationException ex) {
            System.out.println("ERROR: no se pudo abrir/crear el ErroresNifNie.xml o erroresCcc.xml correctamente");
        }
    }
    
    public void agregarContribuyente(Contribuyente con){
        
            Element contribuyente = documentoNif.createElement("Contribuyente");
            contribuyente.setAttribute("id", String.valueOf(con.getIdExcel()+1));
            rootElemNIF.appendChild(contribuyente);

            Element NIF_NIE = documentoNif.createElement("NIF_NIE");
            Text textNIF_NIE;
            if (con.getNifnie() == null) {
                textNIF_NIE = documentoNif.createTextNode("");
                NIF_NIE.appendChild(textNIF_NIE);
            }else{
                String text = con.getNifnie();
                if (text.matches("\\d+\\.0+")) { 
                    text = text.substring(0, text.indexOf('.'));
                }
                textNIF_NIE = documentoNif.createTextNode(text);
                NIF_NIE.appendChild(textNIF_NIE);
            }
            contribuyente.appendChild(NIF_NIE); 

            Element Nombre = documentoNif.createElement("Nombre");
            Text textNombre = documentoNif.createTextNode(con.getNombre());
            Nombre.appendChild(textNombre);
            contribuyente.appendChild(Nombre);

            Element PrimerApellido = documentoNif.createElement("PrimerApellido");
            Text textPrimerApellido = documentoNif.createTextNode(con.getApellido1());
            PrimerApellido.appendChild(textPrimerApellido);
            contribuyente.appendChild(PrimerApellido);

            if (con.getApellido2() != null && !con.getApellido2().isEmpty()) {
                Element SegundoApellido = documentoNif.createElement("SegundoApellido");
                Text textSegundoApellido = documentoNif.createTextNode(con.getApellido2());
                SegundoApellido.appendChild(textSegundoApellido);
                contribuyente.appendChild(SegundoApellido);
            }
        
        
        
            Element TipoDeError = documentoNif.createElement("TipoDeError");
            Text textTipoDeError = documentoNif.createTextNode(con.getErrNif());
            TipoDeError.appendChild(textTipoDeError);
            contribuyente.appendChild(TipoDeError);
        //}
          
    }
    /*
    <Cuenta id="36">
        <Nombre>Tomas</Nombre>
        <Apellidos>Cedron Perez</Apellidos>
        <NIFNIE>a9731953D</NIFNIE>
        <CCCErroneo>21508149005421346497</CCCErroneo>
        <IBANCorrecto>DE5021508149175421346497</IBANCorrecto>
        o
        <CCCErroneo>245619375215464975</CCCErroneo>
        <TipoError>IMPOSIBLE GENERAR IBAN</TipoError>  
    </Cuenta>
    */
    
    public void agregarCcc(Contribuyente con){
        
            Element cuenta = documentoCcc.createElement("Cuenta");
            cuenta.setAttribute("id", String.valueOf(con.getIdExcel()+1));
            rootElemCcc.appendChild(cuenta);

            Element Nombre = documentoCcc.createElement("Nombre");
            Text textNombre = documentoCcc.createTextNode(con.getNombre());
            Nombre.appendChild(textNombre);
            cuenta.appendChild(Nombre);
            
            
            Element Apellidos = documentoCcc.createElement("Apellidos");
            Text textApellidos;
            if (con.getApellido2() != null && !con.getApellido2().isEmpty()) {
                textApellidos = documentoCcc.createTextNode(con.getApellido1() + " " + con.getApellido2());
            }else {
                textApellidos = documentoCcc.createTextNode(con.getApellido1());
            }
            Apellidos.appendChild(textApellidos);
            cuenta.appendChild(Apellidos);

            
            Element NIF_NIE = documentoCcc.createElement("NIF_NIE");
            Text textNIF_NIE;
            if (con.getNifnie() == null) {
                textNIF_NIE = documentoCcc.createTextNode("");
                NIF_NIE.appendChild(textNIF_NIE);
            }else{
                textNIF_NIE = documentoCcc.createTextNode(con.getNifnie());
                NIF_NIE.appendChild(textNIF_NIE);
            }
            cuenta.appendChild(NIF_NIE); 
            
                //añadir ccc e iban si es posible
            if(con.getCccErroneo()!= ""){       //significa que ha sudi actualizado/subsanado el ccc, esto generaria iban
                Element CccErroneo = documentoCcc.createElement("CCCErroneo");
                Text textCccErroneo = documentoCcc.createTextNode(con.getCcc());
                CccErroneo.appendChild(textCccErroneo);
                cuenta.appendChild(CccErroneo);
                
                Element TipoError = documentoCcc.createElement("TipoError");
                Text textTipoError = documentoCcc.createTextNode("IMPOSIBLE GENERAR IBAN");
                TipoError.appendChild(textTipoError);
                cuenta.appendChild(TipoError);
            }else{
                Element CccErroneo = documentoCcc.createElement("CCCErroneo");
                Text textCccErroneo = documentoCcc.createTextNode(con.getCccErroneo());
                CccErroneo.appendChild(textCccErroneo);
                cuenta.appendChild(CccErroneo);
                //generar iban y anotarlo aqui.
            }
            
            
    }
    
    public boolean escribir(){
        boolean salida = false;
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            
            DOMSource source = new DOMSource(documentoNif);
            StreamResult result = new StreamResult(archivoNif);
            transformer.transform(source, result);
            
            salida = true;
        } catch (TransformerConfigurationException ex) {
            System.out.println("ERROR 1: no se pudo escribir el ErroresNifNie.xml correctamente");
        } catch (TransformerException ex) {
            System.out.println("ERROR 2: no se pudo escribir el ErroresNifNie.xml correctamente");
        }
        return salida;
    }
}
