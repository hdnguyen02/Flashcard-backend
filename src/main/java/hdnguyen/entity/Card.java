package hdnguyen.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity(name = "cards")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_desk")
    private Desk desk;
}
