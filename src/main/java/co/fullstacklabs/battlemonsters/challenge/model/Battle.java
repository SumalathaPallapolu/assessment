package co.fullstacklabs.battlemonsters.challenge.model;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

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
@Table(name = "BATTLE")
@Getter
@Setter
@NoArgsConstructor
public class Battle {
    @Id
    @GenericGenerator(name = "UseExistingIdOtherwiseGenerateUsingIdentity", strategy = "co.fullstacklabs.battlemonsters.challenge.UseExistingIdOtherwiseGenerateUsingIdentity")
    @GeneratedValue(generator = "UseExistingIdOtherwiseGenerateUsingIdentity")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "MONSTER_A_ID", nullable = false)
    private Monster monsterA;

    @ManyToOne
    @JoinColumn(name = "MONSTER_B_ID", nullable = false)
    private Monster monsterB;

    @ManyToOne
    @JoinColumn(name = "MONSTER_WINNER", nullable = false)
    private Monster winner;
}
