package hdnguyen.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;


@Table(name="decks")
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Deck {
    @Id
    @Column(length = 36)
    private String id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(name = "is_public", nullable = false)
    private Boolean isPublic;

    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="uid")
    private User user;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable( name = "deck_label", joinColumns = @JoinColumn(name = "id_deck"),inverseJoinColumns = @JoinColumn(name = "id_label"))
    private List<Label> labels;

    @OneToMany(mappedBy = "deck",fetch = FetchType.EAGER)
    @JsonIgnore
    private List<ECard> cards;

    @Column(name = "create_at", nullable = false)
    private Date createAt;

    @Column(name = "new_limit")
    private Integer newLimit;

    @Column(name = "review_limit")
    private Integer reviewLimit;
}

// gửi lên bao gồm: hình ảnh, audio.
