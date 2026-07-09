package it.unibo;

import java.io.Serializable;

public class Valutazione implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private String autore;
    private int voto; // Da 1 a 5
    private String recensione;

    public Valutazione() {
    }

    private Valutazione(Builder builder) {
        this.id = builder.id;
        this.autore = builder.autore;
        this.voto = builder.voto;
        this.recensione = builder.recensione;
    }

    // Getter
    public int getId() { 
        return id; 
    }
    public String getAutore() { 
        return autore;
    }
    public int getVoto() {
         return voto;
    }
    public String getRecensione() {
         return recensione;
    }

    // Builder
    public static class Builder {
        private int id;
        private String autore;
        private int voto;
        private String recensione;

        public Builder id(int id) {
            this.id = id;
            return this;
        }

        public Builder autore(String autore) {
            this.autore = autore;
            return this;
        }

        public Builder voto(int voto) {
            this.voto = voto;
            return this;
        }

        public Builder recensione(String recensione) {
            this.recensione = recensione;
            return this;
        }

        public Valutazione build() {
            return new Valutazione(this);
        }
    }
}