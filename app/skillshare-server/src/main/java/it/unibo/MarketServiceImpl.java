package it.unibo;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.server.rpc.jakarta.RemoteServiceServlet;

public class MarketServiceImpl extends RemoteServiceServlet implements MarketService  {

    @Override
    public List<String> getAnnunci() throws IllegalArgumentException {
        // Struttura mock provvisoria in attesa dell'integrazione con MapDB
        List<String> annunciMock = new ArrayList<>();
        annunciMock.add("Ripetizioni di Java - Offro");
        annunciMock.add("Appunti di Sistemi Operativi - Cerco");
        annunciMock.add("Riparazione PC - Offro");
        
        return annunciMock;
    }
}