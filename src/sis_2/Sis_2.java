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
        while(count!=-1){
            con = eM.obtenerContribuyente(count);
            if(con == null){
                count = -1;
            }else if(con.getNombre() == null){
                count++;
            }else{
                
                //System.out.println(con.getNifnie());
                con = u.validadorNif(con);
                if("SUBSANADO".equals(con.getErrNif())){
                    //Subsanamos el NIF_NIE.
                    //System.out.println(con.getNifnie());
                    eM.modificarContribuyente(con);
                    con.setErrNif("");
                }
                if(!"".equals(con.getErrNif())) {
                    xmlM.agregarContribuyente(con);     //NIF_NIE erroneo, blanco o duplicado
                }else{
                    //NIF_NIE CORRECTO (correcto o subsanado no repe) codigo futuro para BBDD o lo que corresponda.
                    
                }
                count++;
            }
        }
        xmlM.escribir();
        if(eM.guardarCambios()) System.out.println("Excel guardado exitosamente.");
        
    }
}