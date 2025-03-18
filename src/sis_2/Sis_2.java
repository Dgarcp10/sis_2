/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sis_2;
/**
 *
 * @author Nico
 * @author Diego
 */

import POJOS.*;
//import sis_2.*;
//import java.util.List;
import java.util.Scanner;
//import java.util.regex.Pattern;
//import org.hibernate.Query;
//import org.hibernate.Session;
//import org.hibernate.SessionFactory;
//import org.hibernate.Transaction;

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
        
        //PRIMERA VERSION SUJETA A MUCHISSISISIMOS CAMBIOS
        int count = 0;
        while(count!=-1){
            Contribuyente con = eM.obtenerContribuyente(count);
            if(con == null){
                count = -1;
            }else if(con.getNifnie() == null){
                count++;
            }else{
                con = u.validadorNif(con);
                if(con == null) continue;
                else xmlM.agregarContribuyente(con);
                count++;
            }
        }
        xmlM.escribir();
    }
}