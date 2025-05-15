/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sis_2;


import POJOS.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
//import java.util.HashSet;
import java.util.Scanner;
//import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Nico
 * @author Diego
 */
public class Sis_2 {
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner (System.in);
        Utilities u = new Utilities();
        ExcelManager eM = new ExcelManager();
        
        
        System.out.println("INTRODUZCA EL AÑO A GENERAR RECIBOS:");
        String input = sc.nextLine();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        
        Date fechaPadron=null;
        try {
            fechaPadron = sdf.parse(input + "-01-01");
        } catch (ParseException ex) {
            Logger.getLogger(Sis_2.class.getName()).log(Level.SEVERE, null, ex);
        }
        XmlManager xmlM = new XmlManager(fechaPadron);
        
        Contribuyente c;
        int aux = 1;
        while(aux!=-1){     //Pasada para sacar los correos existentes en el excel
            c = eM.obtenerContribuyente(aux);
            if(c == null){
                aux = -1;
            }else {
                aux++;
                if(c.getNombre() != null){
                    u.addEmail(c);
                }
            }
        }
        
        int count = 1;
        Contribuyente con;
        Boolean change, correctNif, correctCcc;    //hazIban solo lirve como indicador de di hay que escribir el iban en excel o no.
        while(count!=-1){
            con = eM.obtenerContribuyente(count);
            if(con == null){
                count = -1;
            }else if(con.getNombre() == null){
                count++;
            }else{
                change = false;
                correctNif = false;
                correctCcc = false;
                //INICIO LOGICA NIFNIE
                con = u.validadorNif(con);
                if("SUBSANADO".equals(con.getErrNif())){
                    change = true;
                    correctNif = true;
                    con.setErrNif("");
                }
                if(!("".equals(con.getErrNif()))) {
                    correctNif = false;
                    xmlM.agregarContribuyente(con);     //NIF_NIE erroneo, blanco o duplicado
                }else{
                    correctNif = true;
                    //NIF_NIE CORRECTO (correcto o subsanado no repe) codigo futuro para BBDD o lo que corresponda.
                    
                }
                //FIN LOGICA NIFNIE
                
                
                
                //INICIO LOGICA CCC E IBAN
                con = u.validadorCCC(con);
                if(con.getCccErroneo()!=null && !"".equals(con.getCccErroneo())){    //si no esta en blnaco entra
                    
                    if(!"IMPOSIBLE GENERAR IBAN".equals(con.getCccErroneo())){
                        change =true;
                        xmlM.agregarCcc(con);           //lo pasa a errores y lo actualiza en excell
                        correctCcc = true;
                    }else{
                        xmlM.agregarCcc(con);           //lo pasa a errores
                        correctCcc = false;
                        
                    }
                }else {
                    correctCcc = true;
                }
                //FIN LOGICA CCC E IBAN
                
                
                
                //INICIO LOGICA EMAIL Y SUBSANAR IBAN 
                if(correctNif && correctCcc){
                    //Se genera el email (el iban ya esta calculado)
                    change = true;
                    con = u.generadorEmail(con);
                    u.addContribuyente(con);
                    //TODO
                }else {
                    con.setIban("");    //Se borra en caso de que no sea necesario 
                    con.setEmail("");   //Se borra en caso de que no sea necesario, para que la casilla no sea null;
                }
                //FIN LOGICA EMAIL Y SUBSANAR IBAN
                
                
                
                if(change) eM.modificarContribuyente(con);
                count++;
            }
        }
        
        count = 1;
        /*for(int i =0; i< u.listaContribuyentes.length; i++){
            System.out.println(u.listaContribuyentes[i].getNifnie());
        }*/
        
        /*for(Contribuyente co : u.listaContribuyentes){
            System.out.println(co.getNifnie());
        }*/
        while(count!=-1){
            Vehiculos v = eM.obtenerVehiculo(count);
            if(v == null) break;
            if(v.getTipo() == null) {
                count++;
                continue;
            }
            //BONIFICACION Y EXENCION ESTAN EN LOS OBJETOS,FECHAS TMBN A COMPROBAR EN EL SIGUIENTE IF, AL IGUAL QUE MATRICULA UNIDADES Y PROPIETARIO
            v = u.comprobarVehiculo(v);
            if(v.getErrores() == null || "".equals(v.getErrores())){ //el vehiculo esta bn
                Recibos r = u.crearRecibo(v, fechaPadron);
                xmlM.agregarRecibo(r);
            }else{ //ERRORES.XML (error de vehiculo) 
                xmlM.agregarVehiculo(v);
            }
            count++;
        }
        if(xmlM.escribir()) System.out.println("XMLs guardados exitosamente.");
        if(eM.guardarCambios()) System.out.println("EXCELs guardados exitosamente.");
    }
}