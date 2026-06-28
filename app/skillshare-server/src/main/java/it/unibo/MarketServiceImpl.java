package it.unibo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

import org.mapdb.DB;
import org.mapdb.Serializer;
import com.google.gwt.user.server.rpc.jakarta.RemoteServiceServlet;
import jakarta.servlet.ServletException;

public class MarketServiceImpl extends RemoteServiceServlet implements MarketService  {

    @Override
    public void init() throws ServletException {
        super.init();
        DatabaseCore.seedDatabase(); 
    }

    @Override
    public List<Annuncio> getAnnunci() {
        DB db = DatabaseCore.getDB();
        ConcurrentMap<Integer, Annuncio> dbAnnunci = db.hashMap("annunci", Serializer.INTEGER, Serializer.JAVA).createOrOpen();
        return new ArrayList<>(dbAnnunci.values());
    }

    @Override
    public List<String> getCategorie() {
        List<String> categorieImmutabili = DatabaseCore.getCategorie();
        return new ArrayList<>(categorieImmutabili);
    }
}