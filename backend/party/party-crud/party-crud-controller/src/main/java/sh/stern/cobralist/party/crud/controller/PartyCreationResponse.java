package sh.stern.cobralist.party.crud.controller;

public class PartyCreationResponse {

    private Long id;

    private String name = "";

    private String password = "";

    private boolean downVoting;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isDownVoting() {
        return downVoting;
    }

    public void setDownVoting(boolean downVoting) {
        this.downVoting = downVoting;
    }
}
