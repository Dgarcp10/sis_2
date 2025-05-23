/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sis_2;

import POJOS.Contribuyente;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

/**
 *
 * @author gar27
 * @author ngarng00
 */
public class ExcelManager {
    private int ayuntamientoOrdenanzaColumn, tipoVehiculoColumn, unidadColumn, minimoColumn, maximoColumn, importeColumn; //ORDENANZA EXCEL
    private int nifnieColumn, apellido1Column, apellido2Column, nombreColumn, direccionColumn, numeroColumn, emailColumn, ayuntamientoContribuyenteColumn, paisCCCColumn,CCCColumn, IBANColumn, bonificacionColumn; //CONTRIBUYENTE EXCEL
    private int tipoColumn, marcaColumn, modeloColumn, matriculaColumn, bastidorColumn, caballosColumn, plazasColumn, kgColumn, CCColumn, exencionColumn,fechaMatriculaciónColumn, fechaAltaColumn, fechaBajaColumn,fechaBajaTemporalColumn, nifPropietarioColumn; //VEHICULO EXCEL
    String direccionOrdenanzas = "resources/SistemasOrdenanzas.xlsx";
    String direccionVehiculos = "resources/SistemasVehiculos.xlsx";
    private Workbook libroOrdenanzas, libroVehiculos;
    private Sheet hojaOrdenanza, hojaContribuyente, hojaVehiculos;
    
    public ExcelManager(){
        inicializar();
        asignarValores();
    }

    private void inicializar() {
        try {          
            try (FileInputStream fisOrdenanzas = new FileInputStream(direccionOrdenanzas);
                 FileInputStream fisVehiculos = new FileInputStream(direccionVehiculos)) {

                libroOrdenanzas = WorkbookFactory.create(fisOrdenanzas);
                libroVehiculos = WorkbookFactory.create(fisVehiculos);
            }
                     
            hojaOrdenanza = libroOrdenanzas.getSheet("Ordenanza");
            hojaContribuyente = libroVehiculos.getSheet("Contribuyente");
            hojaVehiculos = libroVehiculos.getSheet("Vehiculos");
        } catch (IOException | EncryptedDocumentException ex) {
            Logger.getLogger(ExcelManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void asignarValores() {
        asignarValoresOrdenanza();
        asignarValoresContribuyente();
        asignarValoresVehiculos();
    }

    private void asignarValoresOrdenanza() {
        Iterator<Cell> ordenanzaColumn = hojaOrdenanza.getRow(0).cellIterator();
        while(ordenanzaColumn.hasNext()){
            Cell celda = ordenanzaColumn.next();
            switch (celda.getStringCellValue()) {
                case "AYUNTAMIENTO":
                    ayuntamientoOrdenanzaColumn = celda.getColumnIndex();
                    break;
                case "TIPOVEHICULO":
                    tipoVehiculoColumn = celda.getColumnIndex();
                    break;
                case "UNIDAD":
                    unidadColumn = celda.getColumnIndex();
                    break;
                case "MINIMO":
                    minimoColumn = celda.getColumnIndex();
                    break;
                case "MAXIMO":
                    maximoColumn = celda.getColumnIndex();
                    break;
                case "IMPORTE":
                    importeColumn = celda.getColumnIndex();
                    break;
                default:
                    System.out.println("HAY ALGUN ERROR ASIGNANDO VALORES COLUMNAS ORDENANZA");
                    break;
            }
        }
    }

    private void asignarValoresContribuyente() {
        Iterator<Cell> contribuyenteColumn = hojaContribuyente.getRow(0).cellIterator();
        while(contribuyenteColumn.hasNext()){
            Cell celda = contribuyenteColumn.next();
            switch (celda.getStringCellValue()) {
                case "NIFNIE":
                    nifnieColumn = celda.getColumnIndex();
                    break;
                case "Apellido1":
                    apellido1Column = celda.getColumnIndex();
                    break;
                case "Apellido2":
                    apellido2Column = celda.getColumnIndex();
                    break;
                case "Nombre":
                    nombreColumn = celda.getColumnIndex();
                    break;
                case "Direccion":
                    direccionColumn = celda.getColumnIndex();
                    break;
                case "Numero":
                    numeroColumn = celda.getColumnIndex();
                    break;
                case "Email":
                    emailColumn = celda.getColumnIndex();
                    break;
                case "Ayuntamiento":
                    ayuntamientoContribuyenteColumn = celda.getColumnIndex();
                    break;
                case "PaisCCC":
                    paisCCCColumn = celda.getColumnIndex();
                    break;
                case "CCC":
                    CCCColumn = celda.getColumnIndex();
                    break;
                case "IBAN":
                    IBANColumn = celda.getColumnIndex();
                    break;
                case "Bonificacion":
                    bonificacionColumn = celda.getColumnIndex();
                    break;
                default:
                    System.out.println("HAY ALGUN ERROR ASIGNANDO VALORES COLUMNAS CONTRIBUYENTE");
                    break;
            }
        }
    }

    private void asignarValoresVehiculos() {
        Iterator<Cell> vehiculosColumn = hojaVehiculos.getRow(0).cellIterator();
        while(vehiculosColumn.hasNext()){
            Cell celda = vehiculosColumn.next();
            switch (celda.getStringCellValue()) {
                case "TIPO":
                    tipoColumn = celda.getColumnIndex();
                    break;
                case "MARCA":
                    marcaColumn = celda.getColumnIndex();
                    break;
                case "MODELO":
                    modeloColumn = celda.getColumnIndex();
                    break;
                case "MATRICULA":
                    matriculaColumn = celda.getColumnIndex();
                    break;
                case "BASTIDOR":
                    bastidorColumn = celda.getColumnIndex();
                    break;
                case "CABALLOS":
                    caballosColumn = celda.getColumnIndex();
                    break;
                case "PLAZAS":
                    plazasColumn = celda.getColumnIndex();
                    break;
                case "KG":
                    kgColumn = celda.getColumnIndex();
                    break;
                case "CC":
                    CCColumn = celda.getColumnIndex();
                    break;
                case "EXENCION":
                    exencionColumn = celda.getColumnIndex();
                    break;
                case "FECHAMATRICULACION":
                    fechaMatriculaciónColumn = celda.getColumnIndex();
                    break;
                case "FECHAALTA":
                    fechaAltaColumn = celda.getColumnIndex();
                    break;
                case "FECHABAJA":
                    fechaBajaColumn = celda.getColumnIndex();
                    break;
                case "FECHABAJATEMPORAL":
                    fechaBajaTemporalColumn = celda.getColumnIndex();
                    break;
                case "NIFPROPIETARIO":
                    nifPropietarioColumn = celda.getColumnIndex();
                    break;
                default:
                    System.out.println("HAY ALGUN ERROR ASIGNANDO VALORES COLUMNAS VEHICULOS");
                    break;
            }
        }
    }
    //int nifnieColumn, apellido1Column, apellido2Column, nombreColumn, direccionColumn, numeroColumn, emailColumn, ayuntamientoContribuyenteColumn, paisCCCColumn,CCCColumn, IBANColumn, bonificacionColumn;
    /**
     * 
     * @param i
     * 
     */         //Revisar comentarios
    public Contribuyente obtenerContribuyente(int i){ //@return null si hemos llegado al final del excel     * Contribuyente vacio si es una fila del excel sin contribuyente     * Contribuyente con parametros es que se ha encontrado contribuyente  y se ha devuelto
        if(i>=hojaContribuyente.getPhysicalNumberOfRows()) return null;
        Row contribuyenteExcel = hojaContribuyente.getRow(i);
        if(contribuyenteExcel.getCell(nombreColumn)==null || contribuyenteExcel.getCell(nombreColumn).toString().equals("")) return new Contribuyente();
        Contribuyente contribuyente = new Contribuyente(contribuyenteExcel.getCell(nombreColumn).toString(), contribuyenteExcel.getCell(apellido1Column).toString(), contribuyenteExcel.getCell(nifnieColumn).toString(), contribuyenteExcel.getCell(direccionColumn).toString(), contribuyenteExcel.getCell(ayuntamientoContribuyenteColumn).toString());
        contribuyente.setIdExcel(i);
        if(contribuyenteExcel.getCell(apellido2Column) != null) contribuyente.setApellido2(contribuyenteExcel.getCell(apellido2Column).toString());
        if(contribuyenteExcel.getCell(numeroColumn) != null) contribuyente.setNumero(contribuyenteExcel.getCell(numeroColumn).toString());
        if(contribuyenteExcel.getCell(paisCCCColumn) != null) contribuyente.setPaisCcc(contribuyenteExcel.getCell(paisCCCColumn).toString());
        if(contribuyenteExcel.getCell(CCCColumn) != null) contribuyente.setCcc(contribuyenteExcel.getCell(CCCColumn).toString());
        if(contribuyenteExcel.getCell(IBANColumn) != null) contribuyente.setIban(contribuyenteExcel.getCell(IBANColumn).toString());
        if(contribuyenteExcel.getCell(emailColumn) != null) contribuyente.setEmail(contribuyenteExcel.getCell(emailColumn).toString());
        //if(contribuyenteExcel.getCell(bonificacionColumn) != null && contribuyenteExcel.getCell(bonificacionColumn).getStringCellValue().trim().isEmpty()) contribuyente.setBonificacion(Double.parseDouble(contribuyenteExcel.getCell(bonificacionColumn).getStringCellValue().trim()));
        if(contribuyenteExcel.getCell(bonificacionColumn) != null) contribuyente.setBonificacion(contribuyenteExcel.getCell(bonificacionColumn).getNumericCellValue());
        return contribuyente;
    }
    
    public void modificarContribuyente(Contribuyente contribuyente){
        if(contribuyente.getIdExcel()>hojaContribuyente.getPhysicalNumberOfRows()) return;
        Row rowAux = hojaContribuyente.getRow(contribuyente.getIdExcel());
        
        rowAux.getCell(nifnieColumn).setCellValue(contribuyente.getNifnie());
        rowAux.getCell(CCCColumn).setCellValue(contribuyente.getCcc());
        if(contribuyente.getIban() != null && !"".equals(contribuyente.getIban())){
            rowAux.createCell(IBANColumn).setCellValue(contribuyente.getIban());
        }
        if(contribuyente.getEmail() != null && !"".equals(contribuyente.getEmail())){
            rowAux.createCell(emailColumn).setCellValue(contribuyente.getEmail());
        }
    }
    
    public boolean guardarCambios() {
        boolean salida = false;
        try {           
            try (FileOutputStream fosOrdenanzas = new FileOutputStream(direccionOrdenanzas);
                 FileOutputStream fosVehiculos = new FileOutputStream(direccionVehiculos)) {

                libroOrdenanzas.write(fosOrdenanzas);
                libroVehiculos.write(fosVehiculos);
            }

            // Cerrar los libros después de escribir
            libroOrdenanzas.close();
            libroVehiculos.close();
            salida = true;
        } catch (IOException ex) {
            Logger.getLogger(ExcelManager.class.getName()).log(Level.SEVERE, "Error al guardar los cambios", ex);
            salida = false;
        }
        return salida;
    }

}