package sh.stern.cobralist.party.persistence.domain;

import javax.persistence.*;

@Entity
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "music_request_id")
    private MusicRequest musicRequest;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "guest_id")
    private Guest guest;

    @Column(name = "down_vote")
    private Boolean isDownVote;

    public Long getId() {
        return id;
    }

    public MusicRequest getMusicRequest() {
        return musicRequest;
    }

    public void setMusicRequest(MusicRequest musicRequest) {
        this.musicRequest = musicRequest;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Guest getGuest() {
        return guest;
    }

    public void setGuest(Guest guest) {
        this.guest = guest;
    }

    public Boolean getDownVote() {
        return isDownVote;
    }

    public void setDownVote(Boolean downVote) {
        isDownVote = downVote;
    }
}
