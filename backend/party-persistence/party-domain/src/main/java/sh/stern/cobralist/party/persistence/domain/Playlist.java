package sh.stern.cobralist.party.persistence.domain;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Playlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    // id on streaming platform
    @Column(name = "id_on_streaming_platform")
    private String playlistId;

    @OneToOne(mappedBy = "playlist")
    private Party party;

    @OneToMany(mappedBy = "playlist", cascade = CascadeType.ALL)
    private Set<MusicRequest> musicRequestSet;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPlaylistId() {
        return playlistId;
    }

    public Party getParty() {
        return party;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPlaylistId(String playlistId) {
        this.playlistId = playlistId;
    }

    public void setParty(Party party) {
        this.party = party;
    }
}
