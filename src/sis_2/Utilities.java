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
import java.util.Arrays;
import java.util.Date;
import java.util.List;

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
            if (listaContribuyentes[i] == null) {
                listaContribuyentes[i] = con;
                i = listaContribuyentes.length+1;
            }
        }       
    }
    
    public Contribuyente generadorEmail(Contribuyente con) {
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
    
    
    /**
     * 
     * @param v
     * @return 
     */
    public Vehiculos comprobarVehiculo(Vehiculos v){
        //TODO
        v = comprobarFechas(v);
        v = comprobarMatriculas(v);
        v = comprobarContribuyente(v);
        return v;
    }
    
    /**
     * 
     * @param v
     * @return 
     */
    private Vehiculos comprobarFechas(Vehiculos v){
        if(v.getFechaMatriculacion() != null && v.getFechaAlta() != null && !(v.getFechaAlta().before(v.getFechaMatriculacion()))){
            //fechas de alta y matriculacion correcttas
            if(v.getFechaBajaTemporal() != null){
                if(v.getFechaBajaTemporal().before(v.getFechaAlta())){
                    v.addErrores("Fechas incoherentes.");
                }else if(v.getFechaBaja() != null){
                    if(v.getFechaBaja().before(v.getFechaAlta())){
                        v.addErrores("Fechas incoherentes.");
                    }else if(v.getFechaBajaTemporal().after(v.getFechaBaja())){
                        v.addErrores("Fechas incoherentes.");
                    }
                }
            }else if(v.getFechaBaja() != null){
                if(v.getFechaBaja().before(v.getFechaAlta())){
                    v.addErrores("Fechas incoherentes.");
                }
            }
        }else{
            //fecha de alta o matriculacion erroneas
            v.addErrores("Fechas incoherentes.");
        }
        return v;
    }
     
    /**
     * 
     * @param v
     * @return 
     */
    private Vehiculos comprobarMatriculas(Vehiculos v){
        List<String> ciudades = Arrays.asList("VI", "AB", "A", "AL", "AV", "BA", "IB", "B", "BU", "CC", "CA", "CS", "CE", "CR", "CO", "C", "CU", "GI", "GR", "GU", "SS", "H", "HU", "J", "LE", "L", "LO", "LU", "M", "MA", "ML", "MU", "NA", "OR", "O", "P", "GC", "PO", "SA", "TF", "S", "SG", "SE", "SO", "T", "TE", "TO", "V", "VA", "BI", "ZA", "Z");
        
        if(v.getMatricula() != null){
            switch(v.getTipo()){
                case "TURISMO":
                case "AUTOBUS":
                case "CAMION":
                case "MOTOCICLETA":
                    String tipo1 = "^\\d{4}[A-Z]{3}$";
                    String tipo2 = "\\b(" + String.join("|", ciudades) + ")\\b\\d{4}[A-Z]{1,2}$";
                    String tipo3 = "\\b(" + String.join("|", ciudades) + ")\\b\\d{1,5}";
                    if(Pattern.matches(tipo1, v.getMatricula()) || Pattern.matches(tipo2, v.getMatricula()) || Pattern.matches(tipo3, v.getMatricula())){
                        //Si es correcta que hago??
                    }else{
                        v.addErrores("Matricula Errónea.");
                    }
                    break;
                case "TRACTOR":
                    String tipo4 = "^E\\d{4}[A-Z]{3}$";
                    String tipo5 = "\\b(" + String.join("|", ciudades) + ")\\b\\d{5}VE$";
                    String tipo6 = "\\b(" + String.join("|", ciudades) + ")\\b\\d{1,6}";
                    if(Pattern.matches(tipo4, v.getMatricula()) || Pattern.matches(tipo5, v.getMatricula()) || Pattern.matches(tipo6, v.getMatricula())){
                        //Si es correcta que hago??
                    }else{
                        v.addErrores("Matricula Errónea.");
                    }
                    break;
                case "REMOLQUE":
                    String tipo7 = "^R\\d{4}[A-Z]{3}$";
                    String tipo8 = "\\b(" + String.join("|", ciudades) + ")\\b\\d{5}VE$";
                    String tipo9 = "\\b(" + String.join("|", ciudades) + ")\\b\\d{1,6}";
                    if(Pattern.matches(tipo7, v.getMatricula()) || Pattern.matches(tipo8, v.getMatricula()) || Pattern.matches(tipo9, v.getMatricula())){
                        //Si es correcta que hago??
                    }else{
                        v.addErrores("Matricula Errónea.");
                    }
                    break;
                case "CICLOMOTOR":
                    String tipo10 = "^[C]{1}\\d{4}[A-Z]{3}$";
                    if(Pattern.matches(tipo10, v.getMatricula())){
                        //Si es correcta que hago??    
                    }else{
                        v.addErrores("Matricula Errónea.");
                    }
                    break;
            }
        }else{
            v.addErrores("Matricula Errónea.");
        }
        return v;
    }
    
    /**
     * 
     * @param v
     * @return 
     */
    private Vehiculos comprobarContribuyente(Vehiculos v){
        if(v.getContribuyente() == null) {
            v.addErrores("Vehículo sin propietario.");
            return v;
        }
        for (Contribuyente listaContribuyente : listaContribuyentes) {
                System.out.println(listaContribuyente.getApellido1());
            if(listaContribuyente == null){
                v.addErrores("Vehículo con propietario erróneo.");
                return v;
            }
            if(v.getContribuyente().equals(listaContribuyente)){
                System.out.println("LISTA");
                return v;
            }
        }   
        //v.addErrores("Vehículo con propietario erróneo.");
        return v;
    }
    
    public Recibos crearRecibo(Vehiculos v, Date fechaPadron){
        Recibos r = new Recibos();
        Contribuyente con = v.getContribuyente();
        if(con!=null) r.setContribuyente(con);
        r.setVehiculos(v);
        r.setFechaPadron(fechaPadron);
        r.setFechaRecibo(new Date());
        if(con==null || "".equals(con.getNifnie())) return null; 
        r.setNifContribuyente(con.getNifnie());
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