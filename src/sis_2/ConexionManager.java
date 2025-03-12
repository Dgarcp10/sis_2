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
public class ConexionManager {
    private static ConexionManager instancia;
    private static SessionFactory sessionFactory;

    private ConexionManager(){
        sessionFactory = HibernateUtil.getSessionFactory();
    }
    
    public static ConexionManager getIntance(){
        if(instancia == null || sessionFactory.isClosed()){
            instancia = new ConexionManager();
        }
        return instancia;
    }
    
    public static Session getSession(){
        getIntance();
        return sessionFactory.openSession();
    }
    
    public void close(){
        sessionFactory.close();
    }
}
