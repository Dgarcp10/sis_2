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
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import java.util.regex.Pattern; //validador de patrones (PL)

public class Sis_2 {
    
    static SessionFactory sf = null;
    static Session sesion = null;
    static Transaction tx = null;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("INTRODUZCA SU DNI:");
        Scanner sc = new Scanner (System.in);
        String DNI = sc.nextLine();
  
        try{
            /*sf = HibernateUtil.getSessionFactory();
            
            if(mostrarContribuyente(DNI)){
                importeTotalReciboContribuyente(DNI);
            }
            eliminarRecibosMenorMedia();*/
        }finally{
            /*sf.close();
            HibernateUtil.shutdown();*/
            
        }
           
    }
    //09677930J
    
    /*private static boolean mostrarContribuyente(String DNI){
        
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
    
    private boolean validadorNif(int id, String nif){
        boolean salida = false;
        String regexp = "^[XYZxyz0-9][\\d]{7}[A-Ha-hJ-Nj-nP-Tp-tV-Zv-z]{0,1}$";
        if(Pattern.matches(regexp, nif)){
            if(CorrectorNIF(nif).equalsIgnoreCase("1")){
                //es correcto, comprueba si es repe y continua.
            }else{
                //devuelve dni corregido, actualiza, comprueba si es repe y continua.
                //llama al excelManager y le pasa id y dni actualizado.
            }
        }else{
                //es un error y se manda al xml de errores.
        }
        return salida;
    }
    
    
    /**
     * 
     * @param nif (recibe un dato valido, no es nul ni tienen tipo extraño concretamente tiene 9 caracteres u 8 (le falta la letra)) 
     * @return String (1 si es correcto, o el dni correcto en caso de estar subsanado.
     */
    private String CorrectorNIF(String nif){
        String salida = "-1";
        String letras = "TRWAGMYFPDXBNJZSQVHLCKE";
        String nifAux;
        switch(nif.toUpperCase().charAt(0)){        //cambia la letra X, Y o Z del inicio por su numero correspondiente.
            case 'X':
                nifAux = "0" + nif.substring(1, nif.length());
                break;
            case 'Y':
                nifAux = "1" + nif.substring(1, nif.length());
                break;
            case 'Z':
                nifAux = "2" + nif.substring(1, nif.length());
                break;
            default:
                nifAux = nif.substring(0, nif.length());
                break;
        }
        if(nifAux.length()==8){
            int aux = ((Integer.parseInt(nifAux))%23);
            nif +=(letras.charAt(aux));
            salida = nif;
            
        }else if(nifAux.length()==9){
            int aux = ((Integer.parseInt(nifAux.substring(0, nifAux.length()-1)))%23);
            if(nifAux.charAt(8)==letras.charAt(aux)){
                salida = "1";
            } else {
                nifAux = nifAux.substring(0, nifAux.length()-1) + (letras.charAt(aux));
            }
            salida = nifAux;
        }
        return salida;
    }
    
    
    boolean isDNI(String cadena){
        String regexp = "^[XYZxyz0-9][\\d]{7}[A-Ha-hJ-Nj-nP-Tp-tV-Zv-z]$";
        return Pattern.matches(regexp, cadena);
    }
}