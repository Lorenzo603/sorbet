package it.lf.sorbet.models;

import java.util.List;

public class Team {

    private String id;
    private List<String> aliases;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getAliases() {
        return aliases;
    }

    public void setAliases(List<String> aliases) {
        this.aliases = aliases;
    }
}
