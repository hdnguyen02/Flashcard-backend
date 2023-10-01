package hdnguyen.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;



@Entity(name = "desks")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Desk {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    @Column(name = "is_public")
    private Boolean isPublic;
    private String description;


    @ManyToOne(fetch = FetchType.LAZY)
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
}

// gửi lên bao gồm: hình ảnh, audio.
    