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
public class ECard {
    @Id
    @Column(length = 36)
    private String id;

    @Column(nullable = false)
    private String term;

    @Column(nullable = false)
    private String definition;

    @Column(length = 100)
    private String image;

    @Column(length = 100)
    private String audio;

    @Column(name = "extract_info")
    private String extractInfo;

    @Column(name = "created_at")
    private Date createAt;

    private Integer repetition;


    private Integer backticks;

    @Column(name = "due_date")
    private Date dueDate;

    private Float ef;

    private String type;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_deck")
    private Deck deck;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable( name = "card_tag", joinColumns = @JoinColumn(name = "id_card"),inverseJoinColumns = @JoinColumn(name = "id_tag"))
    private List<Tag> tags;
}
