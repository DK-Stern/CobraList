package sh.stern.cobralist.guest;

import sh.stern.cobralist.party.persistence.Party;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class Guest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @ManyToOne
    @JoinColumn(name = "party_id")
    private Party party;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Party getParty() {
        return party;
    }

    public void setParty(Party party) {
        this.party = party;
    }
}
