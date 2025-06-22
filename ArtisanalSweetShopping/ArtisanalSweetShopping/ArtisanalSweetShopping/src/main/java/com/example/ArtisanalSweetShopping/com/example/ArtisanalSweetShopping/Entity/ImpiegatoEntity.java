package com.example.ArtisanalSweetShopping.com.example.ArtisanalSweetShopping.Entity;

public class ImpiegatoEntity {
    private int IDImpiegato;
    private String nome;
    private String cognome;
    private String password;

    public ImpiegatoEntity(int IDImpiegato, String nome, String cognome, String password) {
        this.IDImpiegato = IDImpiegato;
        this.nome = nome;
        this.cognome = cognome;
        this.password = password;
    }

    public int getIDImpiegato() {
    	return IDImpiegato;
    }

    public void setIDImpiegato(int IDImpiegato) {
    	this.IDImpiegato = IDImpiegato;
    }

    public String getNome() {
    	return nome;
    }

    public void setNome(String nome) {
    	this.nome = nome;
    }

    public String getCognome() {
    	return cognome;
    }

    public void setCognome(String cognome) {
    	this.cognome = cognome;
    	}

    public String getPassword() {
    	return password;
    	}

    public void setPassword(String password) {
    	this.password = password;
    	}
}
