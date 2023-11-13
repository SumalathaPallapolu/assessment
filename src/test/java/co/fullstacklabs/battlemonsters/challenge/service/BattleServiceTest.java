package co.fullstacklabs.battlemonsters.challenge.service;

import co.fullstacklabs.battlemonsters.challenge.dto.BattleDTO;
import co.fullstacklabs.battlemonsters.challenge.model.Battle;
import co.fullstacklabs.battlemonsters.challenge.model.Monster;
import co.fullstacklabs.battlemonsters.challenge.repository.BattleRepository;
import co.fullstacklabs.battlemonsters.challenge.service.impl.BattleServiceImpl;
import co.fullstacklabs.battlemonsters.challenge.testbuilders.BattleTestBuilder;
import co.fullstacklabs.battlemonsters.challenge.testbuilders.MonsterTestBuilder;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

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

    @Mock
    public MonsterService monsterService;

    @Captor
    private ArgumentCaptor<Battle> battleArgumentCaptor;

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
        Monster monsterA = MonsterTestBuilder
                .monster(1, "MonsterA", 82, 45, 66, 42, null);

        Monster monsterB = MonsterTestBuilder
                .monster(2, "MonsterB", 41, 94, 78, 59, null);

        Mockito.when(monsterService.findMonsterById(monsterA.getId())).thenReturn(monsterA);
        Mockito.when(monsterService.findMonsterById(monsterB.getId())).thenReturn(monsterB);

        battleService.startBattle(monsterA.getId(), monsterB.getId());

        Mockito.verify(battleRepository).save(battleArgumentCaptor.capture());

        Battle battleSaved = battleArgumentCaptor.getValue();

        assertThat(battleSaved.getWinner().getId()).isEqualTo(monsterB.getId());
    }

    @Test
    void shouldDeleteBattleSucessfully() {
        Mockito.when(battleRepository.findById(1)).thenReturn(Optional.of(new Battle()));

        battleService.delete(1);

        Mockito.verify(battleRepository, Mockito.times(1)).delete(Mockito.any());
    }

}
