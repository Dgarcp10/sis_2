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

        /* PRIMERA PRACTICA
        System.out.println("INTRODUZCA SU DNI:");
        String DNI = sc.nextLine();
        try{
            if(DAO.mostrarContribuyente(DNI)){
                DAO.importeTotalReciboContribuyente(DNI);
            }
            DAO.eliminarRecibosMenorMedia();
        }finally{
            HibernateUtil.shutdown();
        */
        
        //PRIMERA VERSION SUJETA A CAMBIOS
        int count = 1;
        Contribuyente con;
        while(count!=-1){
            con = eM.obtenerContribuyente(count);
            if(con == null){
                count = -1;
            }else if(con.getNombre() == null){
                count++;
            }else{
                con = u.validadorNif(con);
                if("".equals(con.getErrNif())){
                    //NIF_NIE CORRECTO (correcto o subsanado no repe) codigo futuro para BBDD o lo que corresponda.
                    
                }else xmlM.agregarContribuyente(con);
                
                count++;
            }
        }
        xmlM.escribir();
    }
}