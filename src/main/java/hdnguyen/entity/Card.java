package hdnguyen.entity;


import jakarta.persistence.*;
import lombok.*;
import java.util.Date;
import java.util.List;

@Table(name="cards")
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Card {
    @Id
    @Column(length = 36)
    private String id;

    @Column(nullable = false)
    private String term;

    @Column(nullable = false)
    private String definition;

    @Column(length = 300)
    private String image;

    @Column(length = 300)
    private String audio;

    @Column(name = "extract_info")
    private String extractInfo;

    @Column(name = "created_at")
    private Date createAt;

    @Column(name = "is-favourite")
    private Boolean isFavourite;

    @Column(name = "is-remembered")
    private Boolean isRemembered;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_deck", nullable = false)
    private Deck deck;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable( name = "card_tag", joinColumns = @JoinColumn(name = "id_card"),inverseJoinColumns = @JoinColumn(name = "id_tag"))
    private List<Tag> tags;
}
