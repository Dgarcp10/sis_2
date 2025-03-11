/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sis_2;

//import org.apache.poi.ss.usermodel.Workbook;
/**
 *
 * @author gar27
 */
public class ExcelManager {
    private int ayuntamientoOrdenanzaColumn, tipoVehiculoColumn, unidadColumn, minimoColumn, maximoColumn, importeColumn; //ORDENANZA EXCEL
    private int nifnieColumn, apellido1Column, apellido2Column, nombreColumn, direccionColumn, numeroColumn, emailColumn, ayuntamientoContribuyenteColumn, paisCCCColumn,CCCColumn, IBANColumn, bonificacionColumn; //CONTRIBUYENTE EXCEL
    private int tipoColumn, marcaColumn, modeloColumn, matriculaColumn, bastidorColumn, caballosColumn, plazasColumn, kgColumn, CCColumn, exencionColumn,fechaMatriculaciónColumn, fechaAltaColumn, fechaBajaColumn,fechaBajaTemporalColumn, nifPropietarioColumn; //VEHICULO EXCEL
    
    //private Workbook 
    
    //GETTERS Y SETTERS
    public int getAyuntamientoOrdenanzaColumn() {
        return ayuntamientoOrdenanzaColumn;
    }

    public void setAyuntamientoOrdenanzaColumn(int ayuntamientoOrdenanzaColumn) {
        this.ayuntamientoOrdenanzaColumn = ayuntamientoOrdenanzaColumn;
    }

    public int getTipoVehiculoColumn() {
        return tipoVehiculoColumn;
    }

    public void setTipoVehiculoColumn(int tipoVehiculoColumn) {
        this.tipoVehiculoColumn = tipoVehiculoColumn;
    }

    public int getUnidadColumn() {
        return unidadColumn;
    }

    public void setUnidadColumn(int unidadColumn) {
        this.unidadColumn = unidadColumn;
    }

    public int getMinimoColumn() {
        return minimoColumn;
    }

    public void setMinimoColumn(int minimoColumn) {
        this.minimoColumn = minimoColumn;
    }

    public int getMaximoColumn() {
        return maximoColumn;
    }

    public void setMaximoColumn(int maximoColumn) {
        this.maximoColumn = maximoColumn;
    }

    public int getImporteColumn() {
        return importeColumn;
    }

    public void setImporteColumn(int importeColumn) {
        this.importeColumn = importeColumn;
    }

    public int getNifnieColumn() {
        return nifnieColumn;
    }

    public void setNifnieColumn(int nifnieColumn) {
        this.nifnieColumn = nifnieColumn;
    }

    public int getApellido1Column() {
        return apellido1Column;
    }

    public void setApellido1Column(int apellido1Column) {
        this.apellido1Column = apellido1Column;
    }

    public int getApellido2Column() {
        return apellido2Column;
    }

    public void setApellido2Column(int apellido2Column) {
        this.apellido2Column = apellido2Column;
    }

    public int getNombreColumn() {
        return nombreColumn;
    }

    public void setNombreColumn(int nombreColumn) {
        this.nombreColumn = nombreColumn;
    }

    public int getDireccionColumn() {
        return direccionColumn;
    }

    public void setDireccionColumn(int direccionColumn) {
        this.direccionColumn = direccionColumn;
    }

    public int getNumeroColumn() {
        return numeroColumn;
    }

    public void setNumeroColumn(int numeroColumn) {
        this.numeroColumn = numeroColumn;
    }

    public int getEmailColumn() {
        return emailColumn;
    }

    public void setEmailColumn(int emailColumn) {
        this.emailColumn = emailColumn;
    }

    public int getAyuntamientoContribuyenteColumn() {
        return ayuntamientoContribuyenteColumn;
    }

    public void setAyuntamientoContribuyenteColumn(int ayuntamientoContribuyenteColumn) {
        this.ayuntamientoContribuyenteColumn = ayuntamientoContribuyenteColumn;
    }

    public int getPaisCCCColumn() {
        return paisCCCColumn;
    }

    public void setPaisCCCColumn(int paisCCCColumn) {
        this.paisCCCColumn = paisCCCColumn;
    }

    public int getCCCColumn() {
        return CCCColumn;
    }

    public void setCCCColumn(int CCCColumn) {
        this.CCCColumn = CCCColumn;
    }

    public int getIBANColumn() {
        return IBANColumn;
    }

    public void setIBANColumn(int IBANColumn) {
        this.IBANColumn = IBANColumn;
    }

    public int getBonificacionColumn() {
        return bonificacionColumn;
    }

    public void setBonificacionColumn(int bonificacionColumn) {
        this.bonificacionColumn = bonificacionColumn;
    }

    public int getTipoColumn() {
        return tipoColumn;
    }

    public void setTipoColumn(int tipoColumn) {
        this.tipoColumn = tipoColumn;
    }

    public int getMarcaColumn() {
        return marcaColumn;
    }

    public void setMarcaColumn(int marcaColumn) {
        this.marcaColumn = marcaColumn;
    }

    public int getModeloColumn() {
        return modeloColumn;
    }

    public void setModeloColumn(int modeloColumn) {
        this.modeloColumn = modeloColumn;
    }

    public int getMatriculaColumn() {
        return matriculaColumn;
    }

    public void setMatriculaColumn(int matriculaColumn) {
        this.matriculaColumn = matriculaColumn;
    }

    public int getBastidorColumn() {
        return bastidorColumn;
    }

    public void setBastidorColumn(int bastidorColumn) {
        this.bastidorColumn = bastidorColumn;
    }

    public int getCaballosColumn() {
        return caballosColumn;
    }

    public void setCaballosColumn(int caballosColumn) {
        this.caballosColumn = caballosColumn;
    }

    public int getPlazasColumn() {
        return plazasColumn;
    }

    public void setPlazasColumn(int plazasColumn) {
        this.plazasColumn = plazasColumn;
    }

    public int getKgColumn() {
        return kgColumn;
    }

    public void setKgColumn(int kgColumn) {
        this.kgColumn = kgColumn;
    }

    public int getCCColumn() {
        return CCColumn;
    }

    public void setCCColumn(int CCColumn) {
        this.CCColumn = CCColumn;
    }

    public int getExencionColumn() {
        return exencionColumn;
    }

    public void setExencionColumn(int exencionColumn) {
        this.exencionColumn = exencionColumn;
    }

    public int getFechaMatriculaciónColumn() {
        return fechaMatriculaciónColumn;
    }

    public void setFechaMatriculaciónColumn(int fechaMatriculaciónColumn) {
        this.fechaMatriculaciónColumn = fechaMatriculaciónColumn;
    }

    public int getFechaAltaColumn() {
        return fechaAltaColumn;
    }

    public void setFechaAltaColumn(int fechaAltaColumn) {
        this.fechaAltaColumn = fechaAltaColumn;
    }

    public int getFechaBajaColumn() {
        return fechaBajaColumn;
    }

    public void setFechaBajaColumn(int fechaBajaColumn) {
        this.fechaBajaColumn = fechaBajaColumn;
    }

    public int getFechaBajaTemporalColumn() {
        return fechaBajaTemporalColumn;
    }

    public void setFechaBajaTemporalColumn(int fechaBajaTemporalColumn) {
        this.fechaBajaTemporalColumn = fechaBajaTemporalColumn;
    }

    public int getNifPropietarioColumn() {
        return nifPropietarioColumn;
    }

    public void setNifPropietarioColumn(int nifPropietarioColumn) {
        this.nifPropietarioColumn = nifPropietarioColumn;
    }

    public ExcelManager(){
        //inicializarValores();
    }
}
