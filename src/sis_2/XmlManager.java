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
            
            //System.out.println("A");
        } catch (ParserConfigurationException ex) {
            System.out.println("ERROR: no se pudo abrir/crear el ErroresNifNie.xml correctamente");
        }
        
// Esto es una prueba y funciona el fallo esta en el agregar
        Element contribuyente = documento.createElement("Contribuyente");
        contribuyente.setAttribute("id", String.valueOf(0));
        rootElem.appendChild(contribuyente);
        
        
        Element NIF_NIE = documento.createElement("NIF_NIE");
        //Text textNIF_NIE = documento.createTextNode("71314712S");
        //NIF_NIE.appendChild(textNIF_NIE);
        
        NIF_NIE.appendChild(documento.createTextNode(""));
        contribuyente.appendChild(NIF_NIE); 

        Element Nombre = documento.createElement("Nombre");
        Text textNombre = documento.createTextNode("Diego");
        Nombre.appendChild(textNombre);
        contribuyente.appendChild(Nombre);

        Element PrimerApellido = documento.createElement("PrimerApellido");
        Text textPrimerApellido = documento.createTextNode("García");
        PrimerApellido.appendChild(textPrimerApellido);
        contribuyente.appendChild(PrimerApellido);
        
        
        
        
// hasta aqui llega la prueba
    }
    
    public void agregarContribuyente(Contribuyente con){
        Element contribuyente = documento.createElement("Contribuyente");
        contribuyente.setAttribute("id", String.valueOf(con.getIdExcel()));
        rootElem.appendChild(contribuyente);
        
        Element NIF_NIE = documento.createElement("NIF_NIE");
        Text textNIF_NIE;
        if (!"".equals(con.getNifnie()) && con.getNifnie() != null) {
            textNIF_NIE = documento.createTextNode(con.getNifnie());
            NIF_NIE.appendChild(textNIF_NIE);
        }else{
            textNIF_NIE = documento.createTextNode("");
            NIF_NIE.appendChild(textNIF_NIE);
        }
        
        System.out.println("\nMi dni es: " + textNIF_NIE);
        contribuyente.appendChild(NIF_NIE); 

        Element Nombre = documento.createElement("Nombre");
        Text textNombre = documento.createTextNode(con.getNombre());
        System.out.println("Mi nombre es: " +con.getNombre());
        Nombre.appendChild(textNombre);
        contribuyente.appendChild(Nombre);

        Element PrimerApellido = documento.createElement("PrimerApellido");
        Text textPrimerApellido = documento.createTextNode(con.getApellido1());
        System.out.println("Mi apellido es: " +con.getApellido1());
        PrimerApellido.appendChild(textPrimerApellido);
        contribuyente.appendChild(PrimerApellido);

        if (con.getApellido2() != null && !con.getApellido2().isEmpty()) {
            Element SegundoApellido = documento.createElement("SegundoApellido");
            Text textSegundoApellido = documento.createTextNode(con.getApellido2());
            SegundoApellido.appendChild(textSegundoApellido);
            contribuyente.appendChild(SegundoApellido);
        }
        
        
        if(con.getErrNif() == null || "".equals(con.getErrNif())){
        }else{
            Element TipoDeError = documento.createElement("TipoDeError");
            Text textTipoDeError = documento.createTextNode(con.getErrNif());
            TipoDeError.appendChild(textTipoDeError);
            contribuyente.appendChild(TipoDeError);
        }
          
    }
    
    public boolean escribir(){
        boolean salida = false;
        try {
            System.out.println(rootElem);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            
            //Prueba 2
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            //Fin prueba 2
            
            DOMSource source = new DOMSource(documento);
            //System.out.println(archivo.getAbsolutePath());
            StreamResult result = new StreamResult(archivo);
            //if(documento != null) System.out.println("bla");
            transformer.transform(source, result);
            
            
            salida = true;
        } catch (TransformerConfigurationException ex) {
            System.out.println("ERROR 1: no se pudo escribir el ErroresNifNie.xml correctamente");
        } catch (TransformerException ex) {
            System.out.println("ERROR 2: no se pudo escribir el ErroresNifNie.xml correctamente");
            System.out.println(ex);
        }
        return salida;
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

}
