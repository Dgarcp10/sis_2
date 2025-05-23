/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sis_2;

import POJOS.Contribuyente;
import POJOS.Ordenanza;
import POJOS.Recibos;
import POJOS.Vehiculos;
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
        //sesion.getSession();
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
    
    public Recibos anyadirRecibo(Recibos rec) {
        //boolean exito = true;
        sesion = ConexionManager.getSession();
        tx = sesion.beginTransaction();

        try {
            // 1. Verificar y añadir/actualizar Contribuyente
            Contribuyente contribuyente = comprobarOInsertarContribuyente(rec.getContribuyente());

            // 2. Verificar y añadir/actualizar Ordenanza
            Ordenanza ordenanza = comprobarOInsertarOrdenanza(rec.getVehiculos().getOrdenanza());

            // 3. Verificar y añadir/actualizar Vehículo
            Vehiculos vehiculo = comprobarOInsertarVehiculo(rec.getVehiculos(), contribuyente, ordenanza);

            // 4. Verificar si ya existe el recibo
            Query qRecibo = sesion.createQuery(
                "FROM Recibos r WHERE r.nifContribuyente = :nif AND r.fechaPadron = :fecha AND r.vehiculos.matricula = :matricula");
            qRecibo.setParameter("nif", rec.getNifContribuyente());
            qRecibo.setParameter("fecha", rec.getFechaPadron());
            qRecibo.setParameter("matricula", vehiculo.getMatricula());

            Recibos rExistente = (Recibos) qRecibo.uniqueResult();
            rec.setNumRecibo(rExistente.getNumRecibo());
            rec.setContribuyente(contribuyente);
            rec.setVehiculos(vehiculo);

            if (rExistente == null) {
                sesion.save(rec);
            } else {
                rec.setNumRecibo(rExistente.getNumRecibo());
                rExistente.setTotalRecibo(rec.getTotalRecibo());
                rExistente.setValorUnidad(rec.getValorUnidad());
                rExistente.setUnidad(rec.getUnidad());
                rExistente.setMarcaModelo(rec.getMarcaModelo());
                rExistente.setEmail(rec.getEmail());
                rExistente.setIban(rec.getIban());
                sesion.update(rExistente);
            }

            tx.commit();
        } catch (Exception e) {
            //exito = false;
            //return null;
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            if (sesion != null) sesion.close();
        }

        return rec;
    }


        private Contribuyente comprobarOInsertarContribuyente(Contribuyente c) {
        Query q = sesion.createQuery(
            "FROM Contribuyente c WHERE c.nombre = :n AND c.apellido1 = :a1 AND c.apellido2 = :a2 AND c.nifnie = :nif");
        q.setParameter("n", c.getNombre());
        q.setParameter("a1", c.getApellido1());
        q.setParameter("a2", c.getApellido2());
        q.setParameter("nif", c.getNifnie());

        Contribuyente existente = (Contribuyente) q.uniqueResult();

        if (existente == null) {
            sesion.save(c);
                
            return c;
        } else {
            c.setIdContribuyente(existente.getIdContribuyente());
            existente.setDireccion(c.getDireccion());
            existente.setEmail(c.getEmail());
            existente.setIban(c.getIban());
            existente.setBonificacion(c.getBonificacion());
            sesion.update(existente);
            return existente;
        }
    }


        private Vehiculos comprobarOInsertarVehiculo(Vehiculos v, Contribuyente c, Ordenanza ord) {
        Query q = sesion.createQuery("FROM Vehiculos v WHERE v.matricula = :m");
        q.setParameter("m", v.getMatricula());

        Vehiculos existente = (Vehiculos) q.uniqueResult();

        if (existente == null) {
            v.setContribuyente(c);
            v.setOrdenanza(ord);
            sesion.save(v);
            return v;
        } else {
            v.setIdVehiculo(v.getIdVehiculo());
            existente.setModelo(v.getModelo());
            existente.setTipo(v.getTipo());
            existente.setFechaAlta(v.getFechaAlta());
            existente.setCentimetroscubicos(v.getCentimetroscubicos());
            existente.setContribuyente(c);
            existente.setOrdenanza(ord);
            sesion.update(existente);
            return existente;
        }
    }

        private Ordenanza comprobarOInsertarOrdenanza(Ordenanza o) {
        Query q = sesion.createQuery("FROM Ordenanza o WHERE o.ayuntamiento = :a AND o.tipoVehiculo = :t AND o.unidad = :u AND o.minimoRango = :min AND o.maximoRango = :max");
        q.setParameter("a", o.getAyuntamiento());
        q.setParameter("t", o.getTipoVehiculo());
        q.setParameter("u", o.getUnidad());
        q.setParameter("min", o.getMinimoRango());
        q.setParameter("max", o.getMaximoRango());

        Ordenanza existente = (Ordenanza) q.uniqueResult();

        if (existente == null) {
            sesion.save(o);
            return o;
        } else {
            o.setId(existente.getId());
            existente.setImporte(o.getImporte()); // En caso de que cambie
            sesion.update(existente);
            return existente;
        }
    }

}
