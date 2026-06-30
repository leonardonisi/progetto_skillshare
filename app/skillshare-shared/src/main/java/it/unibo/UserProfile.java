package it.unibo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class UserProfile implements Serializable {
    
    private String username;
    private String bio;
    private String location;
    private String photoUrl;
    private List<String> categories; // La lista per i famosi Tag!

    // Costruttore vuoto obbligatorio per far viaggiare i dati sulla rete
    public UserProfile() {
        this.categories = new ArrayList<>();
    }

    // Costruttore completo
    public UserProfile(String username, String bio, String location, String photoUrl, List<String> categories) {
        this.username = username;
        this.bio = bio;
        this.location = location;
        this.photoUrl = photoUrl;
        this.categories = categories != null ? categories : new ArrayList<>();
    }

    // --- Getter e Setter ---
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getPhotoUrl() { return photoUrl; }
    public void setPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; }

    public List<String> getCategories() { return categories; }
    public void setCategories(List<String> categories) { this.categories = categories; }
}