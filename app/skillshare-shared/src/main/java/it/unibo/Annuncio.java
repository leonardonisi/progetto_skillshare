package it.unibo;

import java.io.Serializable;

public class Annuncio implements Serializable {

    private int id;
    private String autore;
    private String titolo;
    private String categoria;
    private String skillOfferta;
    private String controprestazioneCercata;
    private String disponibilita;

    public Annuncio() {
    }

    private Annuncio(Builder builder) {
        this.id = builder.id;
        this.autore = builder.autore;
        this.titolo = builder.titolo;
        this.categoria = builder.categoria;
        this.skillOfferta = builder.skillOfferta;
        this.controprestazioneCercata = builder.controprestazioneCercata;
        this.disponibilita = builder.disponibilita;
    }

    // Getter e Setter

    public int getId() {
        return id;
    }

    private void setId(int id){
        this.id = id;
    }

    public String getAutore() {
        return autore;
    }

    public void setAutore(String autore) {
        this.autore = autore;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getSkillOfferta() {
        return skillOfferta;
    }

    public void setOffro(String skillOfferta) {
        this.skillOfferta = skillOfferta;
    }

    public String getControprestazione() {
        return controprestazioneCercata;
    }

    public void setControprestazione(String controprestazioneCercata) {
        this.controprestazioneCercata = controprestazioneCercata;
    }

    public String getDisponibilita() {
        return disponibilita;
    }

    public void setDisponibilita(String disponibilita) {
        this.disponibilita = disponibilita;
    }

    // Builder
    public static class Builder {
        private int id;
        private String autore;
        private String titolo;
        private String categoria;
        private String skillOfferta;
        private String controprestazioneCercata;
        private String disponibilita;

        public Builder() {
        }

        public Builder id(int id) {
            this.id = id;
            return this;
        }

        public Builder autore(String autore) {
            this.autore = autore;
            return this;
        }

        public Builder titolo(String titolo) {
            this.titolo = titolo;
            return this;
        }

        public Builder categoria(String categoria) {
            this.categoria = categoria;
            return this;
        }

        public Builder skillOfferta(String skillOfferta) {
            this.skillOfferta = skillOfferta;
            return this;
        }

        public Builder controprestazioneCercata(String controprestazioneCercata) {
            this.controprestazioneCercata = controprestazioneCercata;
            return this;
        }

        public Builder disponibilita(String disponibilita) {
            this.disponibilita = disponibilita;
            return this;
        }

        public Annuncio build() {
            return new Annuncio(this);
        }
    }
}