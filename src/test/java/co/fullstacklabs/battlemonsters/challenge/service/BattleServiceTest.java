package co.fullstacklabs.battlemonsters.challenge.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import co.fullstacklabs.battlemonsters.challenge.dto.MonsterDTO;
import co.fullstacklabs.battlemonsters.challenge.model.Monster;
import co.fullstacklabs.battlemonsters.challenge.testbuilders.MonsterTestBuilder;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import co.fullstacklabs.battlemonsters.challenge.dto.BattleDTO;
import co.fullstacklabs.battlemonsters.challenge.model.Battle;
import co.fullstacklabs.battlemonsters.challenge.repository.BattleRepository;
import co.fullstacklabs.battlemonsters.challenge.repository.MonsterRepository;
import co.fullstacklabs.battlemonsters.challenge.service.impl.BattleServiceImpl;
import co.fullstacklabs.battlemonsters.challenge.testbuilders.BattleTestBuilder;

/**
 * @author FullStack Labs
 * @version 1.0
 * @since 2022-10
 */
@ExtendWith(MockitoExtension.class)
public class BattleServiceTest {
    
    @InjectMocks
    public BattleServiceImpl battleService;

    @Mock
    public BattleRepository battleRepository;

    @Mock
    private ModelMapper mapper;

    /**** Delete */
    @Mock
    public MonsterRepository monsterRepository;

    @Test
    public void testGetAll() {
        Battle battle1 = BattleTestBuilder.builder().id(1).build();
        Battle battle2 = BattleTestBuilder.builder().id(2).build();

        List<Battle> battleList = Lists.newArrayList(battle1, battle2);
        Mockito.when(battleRepository.findAll()).thenReturn(battleList);
        
        battleService.getAll();

        Mockito.verify(battleRepository).findAll();
        Mockito.verify(mapper).map(battleList.get(0), BattleDTO.class);
        Mockito.verify(mapper).map(battleList.get(1), BattleDTO.class);        
    }
    

    @Test
    void shouldInsertBattleWithMonsterBWinning() {
        MonsterDTO godzilla = MonsterDTO.builder()
                .name("Godzilla").hp(100).speed(100).attack(40).defense(40).imageUrl("http://images.com/godzilla")
                .build();
        MonsterDTO mothra = MonsterDTO.builder()
                .name("Mothra").hp(100).speed(30).attack(50).defense(20).imageUrl("http://images.com/mothra")
                .build();
        BattleDTO battle = BattleDTO.builder()
                .monsterA(godzilla)
                .monsterB(mothra)
                .build();
        BattleDTO outcome = battleService.fight(battle);
        assertEquals(outcome.getWinner().getName(), godzilla.getName());
    }

    @Test
    void shouldDeleteBattleSucessfully() {
        battleService.delete(1233);
    }
}
