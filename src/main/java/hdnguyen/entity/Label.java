package hdnguyen.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "labels")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Label {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String alias;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_topic")
    private Topic topic;
}
