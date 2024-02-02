package client.client;

public class Zwischenspeicher {

    private static Zwischenspeicher speicher;
    private int filmId;
    private String email;
    private int discId;

    public Zwischenspeicher(){

    }

    public static Zwischenspeicher getSpeicher(){
        if(speicher == null){
            speicher = new Zwischenspeicher();
        }

        return speicher;
    }

    public void setFilmId(int filmId){
        this.filmId = filmId;
    }

    public int getFilmId(){
        return filmId;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getEmail(){
        return email;
    }

    public void setDiscId(int discId){ this.discId = discId; }

    public int getDiscId() { return discId; }
}
