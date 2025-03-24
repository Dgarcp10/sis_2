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
        Boolean change;
        while(count!=-1){
            con = eM.obtenerContribuyente(count);
            if(con == null){
                count = -1;
            }else if(con.getNombre() == null){
                count++;
            }else{
                change = false;
                con = u.validadorNif(con);
                //INICIO LOGICA NIFNIE
                if("SUBSANADO".equals(con.getErrNif())){
                    change = true;
                    con.setErrNif("");
                }
                if(!("".equals(con.getErrNif()))) {
                    xmlM.agregarContribuyente(con);     //NIF_NIE erroneo, blanco o duplicado
                }else{
                    //NIF_NIE CORRECTO (correcto o subsanado no repe) codigo futuro para BBDD o lo que corresponda.
                    
                }
                //FIN LOGICA NIFNIE
                
                //INICIO LOGICA CCC
                //FIN LOGICA CCC
                
                //INICIO LOGICA IBAN
                //FIN LOGICA IBAN
                
                //INICIO LOGICA EMAIL
                //FIN LOGICA EMAIL
                
                
                eM.modificarContribuyente(con);
                count++;
            }
        }
        xmlM.escribir();
        if(eM.guardarCambios()) System.out.println("Excel guardado exitosamente.");
        
    }
}