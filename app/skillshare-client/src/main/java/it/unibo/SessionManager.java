package it.unibo;

public class SessionManager {
    private static String utenteLoggato = null; 

    public static String getUtenteLoggato() {
        return utenteLoggato;
    }

    public static void setUtenteLoggato(String username) {
        utenteLoggato = username;
    }

    public static boolean isLogged() {
        return utenteLoggato != null;
    }

    public static void logout() {
        utenteLoggato = null;
    }
}