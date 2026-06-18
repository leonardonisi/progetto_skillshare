package it.unibo;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("market")
public interface MarketService extends RemoteService {
    List<String> getAnnunci() throws IllegalArgumentException;
}