package sh.stern.cobralist.party.persistence;

import org.springframework.data.annotation.CreatedDate;
import sh.stern.cobralist.security.oauth2.user.model.User;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Party {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String name;

    private String password;

    @Column(name = "vote_down")
    private boolean downVotable;

    @Column(name = "creation_date")
    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    private boolean archived;

    private String description;

    public Long getId() {
        return id;
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
}
