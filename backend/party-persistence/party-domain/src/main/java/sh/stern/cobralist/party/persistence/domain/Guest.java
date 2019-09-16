package sh.stern.cobralist.party.persistence.domain;

import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Guest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private String name;

    @ManyToOne
    @JoinColumn(name = "party_id")
    private Party party;

    @OneToMany(mappedBy = "guest", cascade = CascadeType.ALL)
    private Set<Vote> votes;

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

    public void setId(Long id) {
        this.id = id;
    }
}
