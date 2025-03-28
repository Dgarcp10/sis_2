/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sis_2;

import java.util.regex.Pattern;
import POJOS.Contribuyente;

/**
 *
 * @author gar27
 * @author ngarng00
 */
public class Utilities {
    String[] listaNIFNIE;
    //ExcelManager em;

    public Utilities(){
       inicializar(); 
    }
    
    private void inicializar() {
            listaNIFNIE = new String[10];
            //em = new ExcelManager();
    }
    
    /**
     * 
     * @param con
     * Si es erroneo (blanco, erroneo o repetido) lo marca como tal y no lo subsana en cado de ser posinle.
     * @return contribuyente actualizado si corresponde (marca el tipo de error si no tiene es correcto)
     * 
     */
    public Contribuyente validadorNif(Contribuyente con){
        //boolean salida;
        String nif = con.getNifnie().toUpperCase();
        String regexp = "^[XYZxyz0-9][\\d]{7}[A-Ha-hJ-Nj-nP-Tp-tV-Zv-z]{0,1}$";
        if(Pattern.matches(regexp, nif)){
            nif = correctorNIF(nif);
            if(nif.equalsIgnoreCase("1")){ 
                if(anyadirNIFNIE(nif)){
                    //Es un nif correcto no duplicado.
                    con.setErrNif("");
                }else{
                    //Es un nif correcto pero duplicado, se marca y se manda al xml de errores
                    con.setErrNif("NIF DUPLICADO");
                }
            }else{                      
                if(anyadirNIFNIE(nif)){
                    //Es subsanable no repetido, lo actualiza y lo debuelve actualizado
                    con.setNifnie(nif);
                    //em.modificarContribuyente(con);
                    con.setErrNif("SUBSANADO");
                }else{
                    //No actualiza excel, lo manda al xml de errores (es repe y subsanable)
                    con.setErrNif("NIF DUPLICADO");
                }
            }
        }else{
            //Es un nif que no cumple la forma: erroneo o blanco, se manda al xml de errores.
            if(con.getNifnie() == null || "".equals(con.getNifnie())){
                con.setErrNif("NIF BLANCO");
            }else{
                con.setErrNif("NIF ERRONEO");
            }
        }
        
        return con;
    }

    /**
     * @param nif entre 8 y 9 caracters compatible con un NIF NIE correcto o subsanable. 
     * @return String (1, si es correcto; el dni corrregido, en caso de ser subsanado; -1, en caso de error.
     */
    private String correctorNIF(String nif){
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
            if(nifAux.toUpperCase().charAt(8)==letras.charAt(aux)){
                salida = "1";
            } else {
                nifAux = nif.substring(0, nif.length()-1) + (letras.charAt(aux));
            }
            salida = nifAux;
        }
        return salida;
    }
    
    /**
     * @param NIFNIE
     * @return true si no existia el dni en la lista y false si ya existia.
     */
    private boolean anyadirNIFNIE(String nif){
        boolean salida = false;
        if (isFull()) expandir();
        for (int i = 0; i < listaNIFNIE.length; i++) {
            String str = listaNIFNIE[i];
            if (str != null && str.equals(nif)) {
                return false;       //ya existe en la lista (es repe).

            }else if(str == null){
                listaNIFNIE[i]=nif;
                return true;      // No existe en la lista, lo añadimos.
            }
        }
        return salida;
    }

    /**
     * @param array
     * @return true si la lista esta llena false si tiene hueco.
     */
    private boolean isFull() {
        for (String str : listaNIFNIE) {
            if (str == null) {
                return false; // El array no está lleno
            }
        }
        return true; // El array está lleno
    }

    /**
     * @param array 
     * Expande el array de DNI correctos.
     */
    private void expandir() {
        String[] nuevoArray = new String[listaNIFNIE.length + 10];   // Duplicar el tamaño del array
        System.arraycopy(listaNIFNIE, 0, nuevoArray, 0, listaNIFNIE.length);    // Copiar los elementos del array original al nuevo array
        listaNIFNIE = nuevoArray;   // Asignar el nuevo array al array original.
    }
}