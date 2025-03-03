/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sis_2;
/**
 *
 * @author Nicol
 */

import POJOS.*;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class sis_2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        SessionFactory sf = null;
        Session sesion = null;
        Transaction tx = null;
  
        System.out.println("INTRODUZCA SU DNI:");
        Scanner sc = new Scanner (System.in);
        String DNI = sc.nextLine();
  
        try{
            sf = HibernateUtil.getSessionFactory();
            sesion = sf.openSession();
            
            Query queryC = sesion.createQuery("SELECT c FROM Contribuyente c WHERE c.nifnie = :n");
            queryC.setParameter("n", DNI);
            
            
            
            Contribuyente c = (Contribuyente) queryC.uniqueResult();
            System.out.println(c.getNombre());
            System.out.println(c.getApellido1());
            System.out.println(c.getApellido2());
            System.out.println(c.getNifnie());
            System.out.println(c.getDireccion());
            

            Query queryR = sesion.createQuery("SELECT r FROM Recibos r WHERE r.nifContribuyente = :n");
            queryR.setParameter("n", DNI);
            
            List<Recibos> listaRecibos = queryR.list();
            tx = sesion.beginTransaction();
            for (Recibos r : listaRecibos) {
                System.out.println(r.getTotalRecibo());
                r.setTotalRecibo(115D);
                System.out.println(r.getTotalRecibo());
                sesion.update(r);
            }
            tx.commit();
            
            sesion.close();
            sf.close();
        }finally{
            
        }
           
    }
    //09677930J
}
