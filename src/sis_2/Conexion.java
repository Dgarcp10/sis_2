/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sis_2;

import POJOS.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 *
 * @author Nicol
 */
public class Conexion {
    private static Conexion instancia;
    private static SessionFactory sessionFactory;

    private Conexion(){
        sessionFactory = HibernateUtil.getSessionFactory();
    }
    
    public static Conexion getIntance(){
        if(instancia == null || sessionFactory.isClosed()){
            instancia = new Conexion();
        }
        return instancia;
    }
    
    public Session getSession(){
        return sessionFactory.openSession();
    }
    
    public void close(){
        sessionFactory.close();
    }
}
