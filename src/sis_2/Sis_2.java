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
import java.util.List;
import java.util.Scanner;
//import org.hibernate.Query;
//import org.hibernate.Session;
//import org.hibernate.SessionFactory;
//import org.hibernate.Transaction;

public class Sis_2 {
    //static ConexionManager conexion = null;
    //static SessionFactory sf = null;
    //static Session sesion = null;
    //static Transaction tx = null;
    //static DAO dao = null;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("INTRODUZCA SU DNI:");
        Scanner sc = new Scanner (System.in);
        String DNI = sc.nextLine();
  
        try{
            
            //sf = HibernateUtil.getSessionFactory();
            //conexion = ConexionManager.getIntance();
            if(DAO.mostrarContribuyente(DNI)){
                DAO.importeTotalReciboContribuyente(DNI);
            }
            DAO.eliminarRecibosMenorMedia();
        }finally{
            //conexion.close();
            HibernateUtil.shutdown();
            
        }
           
    }
    //09677930J
    /*
    private static boolean mostrarContribuyente(String DNI){
        
        boolean salida = true;
        sesion = sf.openSession();
            
        Query query = sesion.createQuery("SELECT c FROM Contribuyente c WHERE c.nifnie = :n");
        query.setParameter("n", DNI);

        Contribuyente c = (Contribuyente) query.uniqueResult();
        if(c != null){
            System.out.println(c.getNombre());
            System.out.println(c.getApellido1());
            if(c.getApellido2() != null){
                System.out.println(c.getApellido2());
            }
            System.out.println(c.getNifnie());
            System.out.println(c.getDireccion());
        
        }else salida = false;
        
        sesion.close();
        return salida;
    }
    
    private static void importeTotalReciboContribuyente(String DNI){
        
        sesion = sf.openSession();
        Query query = sesion.createQuery("SELECT r FROM Recibos r WHERE r.nifContribuyente = :n");
        query.setParameter("n", DNI);
            
        List<Recibos> listaRecibos = query.list();
        tx = sesion.beginTransaction();
        for (Recibos r : listaRecibos) {
            r.setTotalRecibo(115D);
            sesion.update(r);
        }
        tx.commit();
    }
    
    private static void eliminarRecibosMenorMedia(){
        
        sesion = sf.openSession();
        Query query = sesion.createQuery("SELECT r FROM Recibos r");
            
        List<Recibos> listaRecibos = query.list();

        double media = 0;
        for (Recibos r : listaRecibos) {
            media += r.getTotalRecibo();
        }
        media = media/listaRecibos.size();
        //System.out.println("Media: " + media);
        tx = sesion.beginTransaction();
        for (Recibos r : listaRecibos) {
            if(r.getTotalRecibo()<media){
                sesion.delete(r);
            }
        }
        tx.commit();



        sesion.close();
    }*/
}