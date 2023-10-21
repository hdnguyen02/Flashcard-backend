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
@Table(name="desks")
public class Desk {
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
    @JoinTable( name = "desk_label", joinColumns = @JoinColumn(name = "id_desk"),inverseJoinColumns = @JoinColumn(name = "id_label"))
    private List<Label> labels;


    @OneToMany(mappedBy = "desk",fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Card> cards;

    @Column(name = "create_at")
    private Date createAt;

    @Column(name = "study_card_number")
    private Integer studyCardNumber;
    @Column(name = "review_card_number")
    private Integer reviewCardNumber;
    @Column(name = "learned_card_number")
    private Integer learnedCardNumber;
    @Column(name = "reviewed_card_number")
    private Integer reviewedCardNumber;
    @Column(name = "last_date")
    private Date lastDate;
}

// gửi lên bao gồm: hình ảnh, audio.
