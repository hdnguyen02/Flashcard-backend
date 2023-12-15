package hdnguyen.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;



@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="decks")
public class Deck {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    @Column(name = "is_public")
    private Boolean isPublic;
    private String description;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="email_user")
    private User user;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable( name = "deck_label", joinColumns = @JoinColumn(name = "id_deck"),inverseJoinColumns = @JoinColumn(name = "id_label"))
    private List<Label> labels;


    @OneToMany(mappedBy = "deck",fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Card> cards;

    @Column(name = "create_at")
    private Date createAt;
    @Column(name = "new_limit")
    private Integer newLimit;
    @Column(name = "review_limit")
    private Integer reviewLimit;
    @Column(name = "total_studied_new")
    private Integer totalStudiedNew;
    @Column(name = "total_studied_review")
    private Integer totalStudiedReview;
    @Column(name = "recent_alter")
    private Date recentAlter;
}

// gửi lên bao gồm: hình ảnh, audio.
