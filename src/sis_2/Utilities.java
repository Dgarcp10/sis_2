/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sis_2;

import java.util.regex.Pattern;
import POJOS.Contribuyente;
import POJOS.Ordenanza;
import POJOS.Recibos;
import POJOS.Vehiculos;
import java.math.BigInteger;
import java.util.Date;

/**
 *
 * @author gar27
 * @author ngarng00
 */
public class Utilities {
    String[] listaNIFNIE;
    String[] listaEmail;
    Contribuyente[] listaContribuyentes;
    
    public Utilities(){
       inicializar(); 
    }
    
    private void inicializar() {
            listaNIFNIE = new String[10];
            listaEmail = new String[10];
            listaContribuyentes = new Contribuyente [10];
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
        if (isFullNIFNIE()) expandirNIFNIE();
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
    private boolean isFullNIFNIE() {
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
    private void expandirNIFNIE() {
        String[] nuevoArray = new String[listaNIFNIE.length + 10];   // Duplicar el tamaño del array
        System.arraycopy(listaNIFNIE, 0, nuevoArray, 0, listaNIFNIE.length);    // Copiar los elementos del array original al nuevo array
        listaNIFNIE = nuevoArray;   // Asignar el nuevo array al array original.
    }
    
    /**
     * 
     * @param con
     * @return contribuyente actualizado.
     */
    public Contribuyente validadorCCC(Contribuyente con){
        String ccc = con.getCcc();
        String regexp = "^[\\d]{20}$";
        if(Pattern.matches(regexp, ccc)){
            //Tiene 20 caracteres numericos 
            con = corregirCCC(con);
            con = generadorIBAN(con);

        }else{
            //Es un ccc que no cumple la forma, es erroneo, se manda al xml de errores.
            con.setCccErroneo("IMPOSIBLE GENERAR IBAN");
        }
        
        return con;
    }
    
    private Contribuyente corregirCCC(Contribuyente con) {
        
        String ccc = con.getCcc();
        
        int[] parte1 = new int[10];
        int[] parte2 = new int[10];
        int[] control =new int[2];
        parte1[0] = 0;
        parte1[1] = 0;
        for (int i = 0; i < ccc.length(); i++) {
            if(i < 8){
                parte1[i+2] = Character.getNumericValue(ccc.charAt(i));
            }else if(i < 10){
                control[i-8] = Character.getNumericValue(ccc.charAt(i));
            }else{
                parte2[i-10] = Character.getNumericValue(ccc.charAt(i));
            }
        }
        int aux1 = digitoControl(parte1);
        int aux2 = digitoControl(parte2);
        
        if(control[0] == aux1 && control[1] == aux2){
            con.setCccErroneo(""); // El CCC es correcto
        
        }else { 
            StringBuilder sb = new StringBuilder(); 
            for(int i=2; i<parte1.length; i++){
                sb.append(parte1[i]);
            }
            sb.append(aux1);
            sb.append(aux2);
            for(int i=0; i<parte2.length; i++){
                sb.append(parte2[i]);
            }
            
            con.setCccErroneo(ccc);
            con.setCcc(sb.toString());
            //Estaba mal, guarda ambos para poder mostrarlo en el erroresCcc.xml
        }
        
        return con;
    }
    
    /**
     * 
     * @param substring
     * @return 
     */
    public int digitoControl(int[] substring){
        int[] multi = {1,2,4,8,5,10,9,7,3,6};
        int control = 0;
        for (int i = 0; i < 10; i++) {
            control += multi[i] * substring[i];
        }
        control = 11 - (control % 11);
        if(control == 10) control = 1;
        if(control == 11) control = 0;
        return control;
    }
    
    public Contribuyente generadorIBAN(Contribuyente con) {
        
        String letras = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String IBANStr = con.getCcc() + (letras.indexOf(con.getPaisCcc().charAt(0))+10) + "" + (letras.indexOf(con.getPaisCcc().charAt(1))+10) + "00";
        //int IBANINT = Integer.parseInt(IBANStr);
        BigInteger ibanBigInt = new BigInteger(IBANStr);
        BigInteger digControl = BigInteger.valueOf(98).subtract(ibanBigInt.mod(BigInteger.valueOf(97)));
        IBANStr = con.getPaisCcc();
        if (digControl.compareTo(BigInteger.TEN) < 0) {
            IBANStr += "0";
        }
        IBANStr += digControl + con.getCcc();
        
        con.setIban(IBANStr);
        
        return con;
    }
    
    public void addEmail(Contribuyente con) {
        if(con.getEmail() != null && !"".equals(con.getEmail())){
            if (isFullEmail()) expandirEmail();
            for (int i = 0; i < listaEmail.length; i++) {

                if (listaEmail[i] == null || "".equals(listaEmail[i])) {
                    listaEmail[i] = con.getEmail();
                    i = listaEmail.length+1;
                }
            }
        }
        
    }
    
    public void addContribuyente(Contribuyente con) {
        if (isFullContribuyentes()) expandirContribuyentes();
        for (int i = 0; i < listaContribuyentes.length; i++) {
            if (listaContribuyentes[i] == null || "".equals(listaContribuyentes[i])) {
                listaContribuyentes[i] = con;
                i = listaContribuyentes.length+1;
            }
        }       
    }
    
    public Contribuyente generadorEmail(Contribuyente con) {
        //      Deberia de leer antes los emails generados ya para no modificarlos en caso de ser ya existentes???
        //      Este codigo no lo contempla
        //      Seria hacer una pasada precia leyendo los emails y metiendoles a la laista si son correctos, posteriormente los saltaria en la segunda pasada si estos ya estan en la lista.
        
        
        if(con.getEmail() == null || "".equals(con.getEmail())){
            String email = ("" + con.getNombre().charAt(0) + con.getApellido1().charAt(0));
            if(con.getApellido2()!= null && !"".equals(con.getApellido2())){
                email += con.getApellido2().charAt(0);
            }
            int num = 0;
            String emailAux = "";
            if (isFullEmail()) expandirEmail();
            for (int i = 0; i < listaEmail.length; i++) {
                if(num >= 10){
                    emailAux = email + Integer.toString(num);
                }else{
                    emailAux = email + "0" + Integer.toString(num);
                }

                if (listaEmail[i] == null || "".equals(listaEmail[i])) {
                    
                    listaEmail[i] = (emailAux+ "@vehiculos2025.com");
                    con.setEmail(emailAux + "@vehiculos2025.com");
                    i = listaEmail.length+1;
                }else if((emailAux + "@vehiculos2025.com").equals(listaEmail[i])){
                    num++;
                    i = -1;
                }
            }
        }
        return con;
    }
    
    /**
     * @param array
     * @return true si la lista esta llena false si tiene hueco.
     */
    private boolean isFullEmail() {
        for (String str : listaEmail) {
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
    private void expandirEmail() {
        String[] nuevoArray = new String[listaEmail.length + 10];   // Duplicar el tamaño del array
        System.arraycopy(listaEmail, 0, nuevoArray, 0, listaEmail.length);    // Copiar los elementos del array original al nuevo array
        listaEmail = nuevoArray;   // Asignar el nuevo array al array original.
    }
    
    /**
     * @param array
     * @return true si la lista esta llena false si tiene hueco.
     */
    private boolean isFullContribuyentes() {
        for (Contribuyente c : listaContribuyentes) {
            if (c == null) {
                return false; // El array no está lleno
            }
        }
        return true; // El array está lleno
    }

    
    /**
     * @param array 
     * Expande el array de DNI correctos.
     */
    private void expandirContribuyentes() {
        Contribuyente[] nuevoArray = new Contribuyente[listaContribuyentes.length + 10];   // Duplicar el tamaño del array
        System.arraycopy(listaContribuyentes, 0, nuevoArray, 0, listaContribuyentes.length);    // Copiar los elementos del array original al nuevo array
        listaContribuyentes = nuevoArray;   // Asignar el nuevo array al array original.
    }
    
    
    public Vehiculos comprobarFechas(Vehiculos v){
        
        if("".equals(v.getFechaMatriculacion()) || (v.getFechaMatriculacion()) == null || "".equals(v.getFechaAlta()) || v.getFechaAlta() == null){
            if(v.getFechaMatriculacion().before(v.getFechaAlta())){
                if(!("".equals(v.getFechaBaja()) || (v.getFechaBaja()) == null)){
                    if(v.getFechaBaja().before(v.getFechaAlta())){
                        v.addErrores("Fechas incoherentes");
                    }
                }
                if(!("".equals(v.getFechaBajaTemporal()) || (v.getFechaBajaTemporal()) == null)){
                    if(v.getFechaBajaTemporal().before(v.getFechaAlta())){
                        v.addErrores("Fechas incoherentes");
                    }
                }
            }
        }
        return v;
    }
      
    /**
     * 
     * @param v
     * @return 
     */
    public Vehiculos comprobarMatricula(Vehiculos v){
        //TODO
        
        return v;
    }
    
    public Recibos crearRecibo(Vehiculos v, Date fechaPadron){
        Recibos r = new Recibos();
        Contribuyente con = v.getContribuyente();
        if(con!=null) r.setContribuyente(con);
        r.setVehiculos(v);
        r.setFechaPadron(fechaPadron);
        r.setFechaRecibo(new Date());
        if(con!=null && !"".equals(con.getNifnie())) r.setNifContribuyente(con.getNifnie());
        String direccion = "";
        direccion += con.getDireccion();
        direccion += "  "+con.getNumero();
        direccion += "  "+con.getAyuntamiento();
        r.setDireccionCompleta(direccion);
        r.setIban(con.getIban());
        r.setTipoVehiculo(v.getTipo());
        r.setMarcaModelo(v.getMarca()+ " "+ v.getModelo());
        r.setUnidad(v.getOrdenanza().getUnidad());
        r.setValorUnidad(v.getCaballosFiscales());
        r.setTotalRecibo(v.getOrdenanza().getImporte());
        r.setExencion(v.getExencion());
        r.setBonificacion(con.getBonificacion());
        r.setEmail(con.getEmail());
        r.setAyuntamiento(con.getAyuntamiento());
        return r;
    }
}