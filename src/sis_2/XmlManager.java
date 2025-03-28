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
    Document documento;
    Element rootElem;
    File archivo;
     
    public XmlManager() {
        
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            
            documento = builder.newDocument();
            documento.setXmlVersion("1.0");
            
            rootElem = documento.createElement("Contribuyentes");
            documento.appendChild(rootElem);
            String rutaArchivo = "resources/";
            String nombreArchivo = "ErroresNifNie.xml";
            archivo = new File(rutaArchivo + nombreArchivo);

        } catch (ParserConfigurationException ex) {
            System.out.println("ERROR: no se pudo abrir/crear el ErroresNifNie.xml correctamente");
        }
    }
    
    public void agregarContribuyente(Contribuyente con){
        //if(con.getErrNif() == null || "".equals(con.getErrNif())){
        //}else{
            
            Element contribuyente = documento.createElement("Contribuyente");
            contribuyente.setAttribute("id", String.valueOf(con.getIdExcel()+1));
            rootElem.appendChild(contribuyente);

            Element NIF_NIE = documento.createElement("NIF_NIE");
            Text textNIF_NIE;
            if (con.getNifnie() == null) {
                textNIF_NIE = documento.createTextNode("");
                NIF_NIE.appendChild(textNIF_NIE);
            }else{
                String text = con.getNifnie();
                if (text.matches("\\d+\\.0+")) { 
                    text = text.substring(0, text.indexOf('.'));
                }
                textNIF_NIE = documento.createTextNode(text);
                NIF_NIE.appendChild(textNIF_NIE);
            }
            contribuyente.appendChild(NIF_NIE); 

            Element Nombre = documento.createElement("Nombre");
            Text textNombre = documento.createTextNode(con.getNombre());
            Nombre.appendChild(textNombre);
            contribuyente.appendChild(Nombre);

            Element PrimerApellido = documento.createElement("PrimerApellido");
            Text textPrimerApellido = documento.createTextNode(con.getApellido1());
            PrimerApellido.appendChild(textPrimerApellido);
            contribuyente.appendChild(PrimerApellido);

            if (con.getApellido2() != null && !con.getApellido2().isEmpty()) {
                Element SegundoApellido = documento.createElement("SegundoApellido");
                Text textSegundoApellido = documento.createTextNode(con.getApellido2());
                SegundoApellido.appendChild(textSegundoApellido);
                contribuyente.appendChild(SegundoApellido);
            }
        
        
        
            Element TipoDeError = documento.createElement("TipoDeError");
            Text textTipoDeError = documento.createTextNode(con.getErrNif());
            TipoDeError.appendChild(textTipoDeError);
            contribuyente.appendChild(TipoDeError);
        //}
          
    }
    
    public boolean escribir(){
        boolean salida = false;
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            
            DOMSource source = new DOMSource(documento);
            StreamResult result = new StreamResult(archivo);
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
