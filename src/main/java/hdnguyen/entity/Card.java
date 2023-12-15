package hdnguyen.entity;


import jakarta.persistence.*;
import lombok.*;


import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cards")
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String term;
    private String definition;
    private String image;
    private String audio;
    @Column(name = "extract_info")
    private String extractInfo;
    @Column(name = "create_at")
    private Date createAt;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_deck")
    private Deck deck;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable( name = "card_tag", joinColumns = @JoinColumn(name = "id_card"),inverseJoinColumns = @JoinColumn(name = "id_tag"))
    private List<Tag> tags;
    private Integer repetition;
    private Integer interval;
    private Date due;
    private Float ef;
    private String type;
}
