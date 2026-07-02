package it.unibo;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface SkillServiceAsync {
    void getMieSkills(String username, AsyncCallback<List<Annuncio>> callback);
}