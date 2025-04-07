/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sis_2;


import POJOS.*;
import java.util.Scanner;

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
        XmlManager xmlM = new XmlManager();

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
                }else correctCcc = true;
                //FIN LOGICA CCC E IBAN
                
                
                
                //INICIO LOGICA EMAIL Y SUBSANAR IBAN 
                if(correctNif && correctCcc){
                    //Se genera el email (el iban ya esta calculado)
                    change = true;
                    con = u.generadorEmail(con);
                }else {
                    con.setIban("");    //Se borra en caso de que no sea necesario 
                    con.setEmail("");   //Se borra en caso de que no sea necesario, para que la casilla no sea null;
                }
                //FIN LOGICA EMAIL Y SUBSANAR IBAN
                
                
                
                if(change) eM.modificarContribuyente(con);
                count++;
            }
        }
        
        if(xmlM.escribir()) System.out.println("XMLs guardados exitosamente.");
        if(eM.guardarCambios()) System.out.println("EXCELs guardados exitosamente.");
    }
}