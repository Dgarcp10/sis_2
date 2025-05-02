/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sis_2;

import POJOS.Contribuyente;
import POJOS.Vehiculos;
import POJOS.Recibos;
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
    
    Document documentoVeh;
    Element rootElemVeh;
    File archivoVeh;
    
    Document documentoRec;
    Element rootElemRec;
    File archivoRec;
    int numRecibos;
    float totalRecibos;
    
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
            
            documentoCcc = builder.newDocument();
            documentoCcc.setXmlVersion("1.0");
            
            rootElemCcc = documentoCcc.createElement("Cuentas");
            documentoCcc.appendChild(rootElemCcc);
            String rutaArchivoCcc = "resources/";
            String nombreArchivoCcc = "ErroresCcc.xml";
            archivoCcc = new File(rutaArchivoCcc + nombreArchivoCcc);
            
            documentoVeh = builder.newDocument();
            documentoVeh.setXmlVersion("1.0");
            
            rootElemVeh = documentoVeh.createElement("Vehiculos");
            documentoVeh.appendChild(rootElemVeh);
            String rutaArchivoVeh = "resources/";
            String nombreArchivoVeh = "ErroresVehiculos.xml";
            archivoVeh = new File(rutaArchivoVeh + nombreArchivoVeh);
            
            documentoRec = builder.newDocument();
            documentoRec.setXmlVersion("1.0");
            
            rootElemRec = documentoRec.createElement("Recibos");
            rootElemRec.setAttribute("fechaPadron", "IVTM de " + "2025") ;
            
            //documentoRec.appendChild(rootElemRec);            //descomentar si da error
            String rutaArchivoRec = "resources/";
            String nombreArchivoRec = "Recibos.xml";
            archivoRec = new File(rutaArchivoRec + nombreArchivoRec);
            this.numRecibos = 0;
            this.totalRecibos = 0;
            
        } catch (ParserConfigurationException ex) {
            System.out.println("ERROR: no se pudo abrir/crear el ErroresNifNie.xml, erroresCcc.xml, ErroresVehiculos.xml o Recibos.xml correctamente");
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
    }
    
    public void agregarCcc(Contribuyente con){
        if(!("".equals(con.getCccErroneo()) || con.getCccErroneo() == null || "".equals(con.getCcc()) || con.getCcc() == null)){
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
            if("IMPOSIBLE GENERAR IBAN".equals(con.getCccErroneo())){       //significa que ha sudi actualizado/subsanado el ccc, esto generaria iban
                Element CccErroneo = documentoCcc.createElement("CCCErroneo");
                Text textCccErroneo = documentoCcc.createTextNode(con.getCcc());
                CccErroneo.appendChild(textCccErroneo);
                cuenta.appendChild(CccErroneo);
                
                Element TipoError = documentoCcc.createElement("TipoError");
                Text textTipoError = documentoCcc.createTextNode(con.getCccErroneo());
                TipoError.appendChild(textTipoError);
                cuenta.appendChild(TipoError);
            }else{
                Element CccErroneo = documentoCcc.createElement("CCCErroneo");
                Text textCccErroneo = documentoCcc.createTextNode(con.getCccErroneo());
                CccErroneo.appendChild(textCccErroneo);
                cuenta.appendChild(CccErroneo);
                //generar iban y anotarlo aqui.
                Element IBANCorrecto = documentoCcc.createElement("IBANCorrecto");
                Text textIBANCorrecto = documentoCcc.createTextNode(con.getIban());       //Dar una vuelta antes de terminarlo llamar desde aqui a generar IBAN??
                IBANCorrecto.appendChild(textIBANCorrecto);
                cuenta.appendChild(IBANCorrecto);
            }
        }  
    }
    
    public void agregarVehiculo(Vehiculos v){
        
        Element vehiculo = documentoVeh.createElement("Vehiculo");
        vehiculo.setAttribute("id", String.valueOf(v.getIdExcel()+1));  //de donde saca el id??
        rootElemVeh.appendChild(vehiculo);

        Element marca = documentoVeh.createElement("Marca");
        Text textMarca = documentoVeh.createTextNode(v.getMarca());
        marca.appendChild(textMarca);
        vehiculo.appendChild(marca); 
        
        Element modelo = documentoVeh.createElement("Modelo");
        Text textModelo = documentoVeh.createTextNode(v.getModelo());
        modelo.appendChild(textModelo);
        vehiculo.appendChild(modelo);

        Element error = documentoVeh.createElement("Error");
        Text textError = documentoVeh.createTextNode(v.getErrores());  //de donde sacan los errores y como??  (Pueden ser varios, ¿Stringcon appednd o Array?)  Hay 4 tipos de error posibles
        error.appendChild(textError);
        vehiculo.appendChild(error);  
    }
    
    
    
    public void agregarRecibo(Recibos r){
        
        Element recibo = documentoRec.createElement("Recibo");
        recibo.setAttribute("id", String.valueOf(r.getNumRecibo()));  //de donde saca el id??, es correcto??
        rootElemRec.appendChild(recibo);

        Element exencion = documentoRec.createElement("Exencion");
        Text textExencion = documentoRec.createTextNode(String.valueOf(r.getExencion()));
        exencion.appendChild(textExencion);
        recibo.appendChild(exencion); 
        
        Element idFilaExcelVehiculo = documentoRec.createElement("idFilaExcelVehiculo");
        Text textIdFilaExcelVehiculo = documentoRec.createTextNode(String.valueOf(r.getVehiculos().getIdExcel()));       //como lo saco??   o parece en el recibo y lo saco de ahi??
        idFilaExcelVehiculo.appendChild(textIdFilaExcelVehiculo);
        recibo.appendChild(idFilaExcelVehiculo);

        Element nombre = documentoRec.createElement("Nombre");
        Text textNombre = documentoRec.createTextNode(r.getContribuyente().getNombre());
        nombre.appendChild(textNombre);
        recibo.appendChild(nombre);  
        
        Element apellido1 = documentoRec.createElement("primerApellido");
        Text textApellido1 = documentoRec.createTextNode(r.getContribuyente().getApellido1());
        apellido1.appendChild(textApellido1);
        recibo.appendChild(apellido1);  
        
        if(!"".equals(r.getContribuyente().getApellido2()) || r.getContribuyente().getApellido2() != null){
            Element apellido2 = documentoRec.createElement("segundoApellido");
            Text textApellido2 = documentoRec.createTextNode(r.getContribuyente().getApellido2());
            apellido2.appendChild(textApellido2);
            recibo.appendChild(apellido2);  
        }
        
        Element nif = documentoRec.createElement("NIF");
        Text textNif = documentoRec.createTextNode(r.getNifContribuyente());
        nif.appendChild(textNif);
        recibo.appendChild(nif);  
        
        Element iban = documentoRec.createElement("IBAN");
        Text textIban = documentoRec.createTextNode(r.getIban());
        iban.appendChild(textIban);
        recibo.appendChild(iban);  
        
        Element tipoVehiculo = documentoRec.createElement("tipoVehiculo");
        Text textTipoVehiculo = documentoRec.createTextNode(r.getVehiculos().getTipo());
        tipoVehiculo.appendChild(textTipoVehiculo);
        recibo.appendChild(tipoVehiculo);  
        
        Element marcaModelo = documentoRec.createElement("marcaModelo");
        Text textMarcaModelo = documentoRec.createTextNode(r.getVehiculos().getMarca() + " " + r.getVehiculos().getModelo());
        marcaModelo.appendChild(textMarcaModelo);
        recibo.appendChild(marcaModelo);  
        
        Element matricula = documentoRec.createElement("matricula");
        Text textMatricula = documentoRec.createTextNode(r.getVehiculos().getMatricula());
        matricula.appendChild(textMatricula);
        recibo.appendChild(matricula);  
        
        Element totalRecivo = documentoRec.createElement("totalRecivo");
        Text textTotalRecivo = documentoRec.createTextNode(String.valueOf(r.getTotalRecibo()));        // calcular el recivo??
        this.totalRecibos += r.getTotalRecibo(); 
        totalRecivo.appendChild(textTotalRecivo);
        recibo.appendChild(totalRecivo);  
        
        this.numRecibos++;
    }
    /*
        <Recibos fechaPadron="IVTM de 2025" totalPadron="9999,99" numeroTotalRecibos="3">
        <Recibo idRecibo="1">
        <Exencion>N</Exencion>
        <idFilaExcelVehiculo>3</idFilaExcelVehiculo>
        <nombre>Miguel angel</nombre>
        <primerApellido>Quintanilla</primerApellido>
        <segundoApellido>Fernandez</segundoApellido>
        <NIF>09715890T</NIF>
        <IBAN>ES5023455254943263234457</IBAN>
        <tipoVehiculo>REMOLQUE</tipoVehiculo>
        <marcaModelo>JOHN DEERE F90</marcaModelo>
        <matricula>M125VE</matricula>
        <totalRecibo>46.9</totalRecibo>
        </Recibo>
    */
    
    public boolean escribir(){
        boolean salida = false;
        //fechaPadron="IVTM de 2025" totalPadron="9999,99" numeroTotalRecibos="3"
        rootElemRec.setAttribute("totalPadron", String.valueOf(this.totalRecibos));
        rootElemRec.setAttribute("numeroTotalRecivos", String.valueOf(this.numRecibos));
        documentoRec.appendChild(rootElemRec);
        
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            
            DOMSource sourceNif = new DOMSource(documentoNif);
            StreamResult resultNif = new StreamResult(archivoNif);
            transformer.transform(sourceNif, resultNif);
            
            salida = true;
        } catch (TransformerConfigurationException ex) {
            System.out.println("ERROR 1.1: no se pudo escribir el ErroresNifNie.xml correctamente");
        } catch (TransformerException ex) {
            System.out.println("ERROR 1.2: no se pudo escribir el ErroresNifNie.xml correctamente");
        }
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            
            DOMSource sourceCcc = new DOMSource(documentoCcc);
            StreamResult resultCcc = new StreamResult(archivoCcc);
            transformer.transform(sourceCcc, resultCcc);
            
            salida = true;
        } catch (TransformerConfigurationException ex) {
            System.out.println("ERROR 2.1: no se pudo escribir el ErroresCcc.xml correctamente");
        } catch (TransformerException ex) {
            System.out.println("ERROR 2.2: no se pudo escribir el ErroresCcc.xml correctamente");
        }
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            
            DOMSource sourceVeh = new DOMSource(documentoVeh);
            StreamResult resultVeh = new StreamResult(archivoVeh);
            transformer.transform(sourceVeh, resultVeh);
            
            salida = true;
        } catch (TransformerConfigurationException ex) {
            System.out.println("ERROR 3.1: no se pudo escribir el ErroresVehiculos.xml correctamente");
        } catch (TransformerException ex) {
            System.out.println("ERROR 3.2: no se pudo escribir el ErroresVehiculos.xml correctamente");
        }
        
        return salida;
    }
}
