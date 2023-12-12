package hdnguyen.entity;


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
    private Integer repetitions;
    @Column(name = "last_study_date")
    private Date lastStudyDate;
    private Integer interval;
    @Column(name = "ease_factor")
    private Float easeFactor;
    @Column(name = "due_date")
    private Date dueDate;
}
