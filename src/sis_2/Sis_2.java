/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sis_2;


import java.util.Scanner;
import POJOS.*;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.SessionFactory;
/**
 *
 * @author gar27
 */
public class Sis_2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        Scanner sc = new Scanner(System.in);
        
        System.out.println("Introduzca un DNI:");
        String newNIF = sc.nextLine();
        
        
        SessionFactory sf = null;
        Session session = null;
        Transaction tx = null;
        
        try{
            sf = HibernateUtil.getSessionFactory();
            session = sf.openSession();
            
            String HQL = "from Recibos r WHERE Recibos.idContribuyente.nifnie = :nifNie";
            
            Query query = session.createQuery(HQL);
            query.setParameter("nifNie", newNIF);
            
            List<Contribuyente> lista = query.list();
            
            
            
        }
        catch(NullPointerException e){
            System.out.println("El usuario no se encuentra entre nuestros datos."); 
        }
        
        sc.close();
        
        
    }
    
}
