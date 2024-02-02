package client.client.Soziales.Discussion.entities;

public class ChosenDiscussion {

    private static ChosenDiscussion chosenDiscussion;
    private Integer ID;
    private String name;

    public ChosenDiscussion() {
    }
    public static ChosenDiscussion getChosenDiscussion(){
        if(chosenDiscussion==null){
            chosenDiscussion=new ChosenDiscussion();
        }
        return chosenDiscussion;
    }


    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
