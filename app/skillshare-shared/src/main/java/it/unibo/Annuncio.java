package it.unibo;

import java.io.Serializable;

public class Annuncio implements Serializable {

    private String titolo;
    private String categoria;
    private String skillOfferta;
    private String controprestazioneCercata;
    private String disponibilita;
    private String utenteId;

    public Annuncio() {
    }

    private Annuncio(Builder builder) {
        this.titolo = builder.titolo;
        this.categoria = builder.categoria;
        this.skillOfferta = builder.skillOfferta;
        this.controprestazioneCercata = builder.controprestazioneCercata;
        this.disponibilita = builder.disponibilita;
        this.utenteId = builder.utenteId;
    }

    // Getter e Setter
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

    public String getUtente() {
        return utenteId;
    }

    public void setUtente(String utenteId) {
        this.utenteId = utenteId;
    }

    // Builder
    public static class Builder {
        private String titolo;
        private String categoria;
        private String skillOfferta;
        private String controprestazioneCercata;
        private String disponibilita;
        private String utenteId;

        public Builder() {
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