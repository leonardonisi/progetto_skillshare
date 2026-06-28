package it.unibo;

import com.google.gwt.user.server.rpc.jakarta.RemoteServiceServlet;
import org.mapdb.DB;
import org.mapdb.Serializer;
import java.util.concurrent.ConcurrentMap;
import java.util.ArrayList;
import java.util.List;

public class ProfileServiceImpl extends RemoteServiceServlet implements ProfileService {
    private static final DB db = DatabaseCore.getDB();
    private static final ConcurrentMap<Integer, Annuncio> dbAnnunci = db
            .hashMap("annunci", Serializer.INTEGER, Serializer.JAVA)
            .createOrOpen();

    @Override
    public List<String> getCategorie() {
        List<String> categorieImmutabili = DatabaseCore.getCategorie();
        return new ArrayList<>(categorieImmutabili);
    }

}