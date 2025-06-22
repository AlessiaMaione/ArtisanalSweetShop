package com.example.ArtisanalSweetShopping.com.example.ArtisanalSweetShopping.Entity;

public class ScontoEntity {

    private String codiceSconto;
    private float percentuale;
    private int idImpiegato;
    private boolean utilizzato;

    public ScontoEntity(String codiceSconto, float percentuale, int idImpiegato, boolean utilizzato) {
        this.codiceSconto = codiceSconto;
        this.percentuale = percentuale;
        this.idImpiegato = idImpiegato;
        this.utilizzato = utilizzato;
    }

    public String getCodiceSconto() {
        return codiceSconto;
    }

    public void setCodiceSconto(String codiceSconto) {
        this.codiceSconto = codiceSconto;
    }

    public float getPercentuale() {
        return percentuale;
    }

    public void setPercentuale(float percentuale) {
        this.percentuale = percentuale;
    }

    public int getIdImpiegato() {
        return idImpiegato;
    }

    public void setIdImpiegato(int idImpiegato) {
        this.idImpiegato = idImpiegato;
    }

    public boolean isUtilizzato() {
        return utilizzato;
    }

    public void setUtilizzato(boolean utilizzato) {
        this.utilizzato = utilizzato;
    }
}
