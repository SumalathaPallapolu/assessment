package co.fullstacklabs.battlemonsters.challenge.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;


/**
 * @author FullStack Labs
 * @version 1.0
 * @since 2022-10
 */
@Entity
@Table(name = "MONSTER")
@Getter
@Setter
@NoArgsConstructor
public class Monster {
    @Id
    @GenericGenerator(name = "UseExistingIdOtherwiseGenerateUsingIdentity", strategy = "co.fullstacklabs.battlemonsters.challenge.UseExistingIdOtherwiseGenerateUsingIdentity")
    @GeneratedValue(generator = "UseExistingIdOtherwiseGenerateUsingIdentity")
    private Integer id;
    @NotNull
    @Column(name = "NAME", nullable = false)
    private String name;
    @NotNull
    @Column(name = "ATTACK", nullable = false)
    private Integer attack;
    @NotNull
    @Column(name = "DEFENSE", nullable = false)
    private Integer defense;
    @NotNull
    @Column(name = "HP", nullable = false)
    private Integer hp;
    @NotNull
    @Column(name = "SPEED", nullable = false)
    private Integer speed;
    @NotNull
    @Column(name = "IMAGE_URL", nullable = false)
    private String imageUrl;

}
