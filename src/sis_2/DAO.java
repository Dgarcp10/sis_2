/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sis_2;

import POJOS.Contribuyente;
import POJOS.Recibos;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author Nicol
 */
public class DAO {
    static ConexionManager con = null;
    static Session sesion = null;
    static Transaction tx = null;
    public DAO (){
        sesion.getSession();
    }
    
    public static boolean mostrarContribuyente(String DNI){
        
        boolean salida = true;
        //con = ConexionManager.getIntance();
        sesion = ConexionManager.getSession();
            
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
    
    public static void importeTotalReciboContribuyente(String DNI){
        
        sesion = ConexionManager.getSession();
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
    
    public static void eliminarRecibosMenorMedia(){
        
        sesion = ConexionManager.getSession();
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
    }
}
