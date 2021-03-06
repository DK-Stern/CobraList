package sh.stern.cobralist.party.persistence.domain;

import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
public class Party {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "party_code",
            unique = true)
    private String partyCode;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private User user;

    @OneToMany(mappedBy = "party", cascade = CascadeType.ALL)
    private Set<Guest> guests;

    private String name;

    private String password;

    @Column(name = "vote_down")
    private boolean downVotable;

    @Column(name = "creation_date")
    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    @ManyToMany(mappedBy = "joinedParties")
    private Set<User> joinedUsers;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "playlist_id")
    private Playlist playlist;

    private Boolean active;

    private Boolean archived;

    private String description;

    public Long getId() {
        return id;
    }

    public String getPartyCode() {
        return partyCode;
    }

    public void setPartyCode(String partyCode) {
        this.partyCode = partyCode;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public boolean isDownVotable() {
        return downVotable;
    }

    public void setDownVotable(boolean downVotable) {
        this.downVotable = downVotable;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public boolean isArchived() {
        return archived;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Guest> getGuests() {
        return guests;
    }

    public Set<User> getJoinedUsers() {
        return joinedUsers;
    }

    public Playlist getPlaylist() {
        return playlist;
    }

    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public void setGuests(Set<Guest> guests) {
        this.guests = guests;
    }
}
