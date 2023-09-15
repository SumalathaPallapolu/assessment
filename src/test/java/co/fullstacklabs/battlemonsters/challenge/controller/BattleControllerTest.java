package co.fullstacklabs.battlemonsters.challenge.controller;

import co.fullstacklabs.battlemonsters.challenge.ApplicationConfig;
import co.fullstacklabs.battlemonsters.challenge.dto.BattleDTO;
import co.fullstacklabs.battlemonsters.challenge.dto.MonsterDTO;
import co.fullstacklabs.battlemonsters.challenge.model.Battle;
import co.fullstacklabs.battlemonsters.challenge.model.Monster;
import co.fullstacklabs.battlemonsters.challenge.repository.BattleRepository;
import co.fullstacklabs.battlemonsters.challenge.repository.MonsterRepository;
import co.fullstacklabs.battlemonsters.challenge.testbuilders.BattleTestBuilder;
import co.fullstacklabs.battlemonsters.challenge.testbuilders.MonsterTestBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * @author FullStack Labs
 * @version 1.0
 * @since 2022-10
 */
@SpringBootTest
@AutoConfigureMockMvc
@Import(ApplicationConfig.class)
public class BattleControllerTest {
    private static final String BATTLE_PATH = "/battle";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ModelMapper mapper;

    private Battle b;

    private Monster godzilla, mothra;
    @BeforeEach
    void prepareBattle() {
        godzilla = MonsterTestBuilder.builder()
                .id(1)
                .name("Godzilla").hp(100).speed(100).attack(40).defense(40).imageURL("http://images.com/godzilla")
                .build();
        mothra = MonsterTestBuilder.builder()
                .name("Mothra").hp(100).speed(30).attack(50).defense(20).imageURL("http://images.com/mothra")
                .build();
        b = BattleTestBuilder.builder()
                .monsterA(godzilla)
                .monsterB(mothra)
                .build();
        battleRepository.save(b);
    }

    @Test
    void shouldFetchAllBattles() throws Exception {
        this.mockMvc.perform(get(BATTLE_PATH)).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", instanceOf(Integer.class)));
    }
    
    @Test
    void shouldFailBattleWithNonExistentMonster() throws Exception {
        BattleDTO battle = BattleDTO.builder()
                .monsterA(mapper.map(godzilla, MonsterDTO.class))
                .monsterB(MonsterDTO.builder()
                        .id(90000)
                        .name("Not exists").hp(100).speed(30).attack(50).defense(20).imageUrl("http://images.com/empty_placeholder")
                        .build())
                .build();

        monsterRepository.save(godzilla);
        this.mockMvc
                .perform(post(BATTLE_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(battle)))
                .andExpect(status().isUnprocessableEntity())
                .andReturn();
    }
    @Test
    void shouldInsertBattleWithMonsterBWinning() throws Exception {
        BattleDTO battle = BattleDTO.builder()
                .monsterA(mapper.map(godzilla, MonsterDTO.class))
                .monsterB(mapper.map(mothra, MonsterDTO.class))
                .build();

        this.mockMvc
                .perform(post(BATTLE_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(battle)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("winner.name", Is.is(godzilla.getName())));
    }
    @Autowired
    BattleRepository battleRepository;
    @Autowired
    MonsterRepository monsterRepository;
    @Test
    void shouldDeleteBattleSuccessfully() throws Exception {
        Battle battle = BattleTestBuilder.builder()
                .id(100)
                .monsterA(godzilla)
                .monsterB(mothra)
                .build();

        battleRepository.save(battle);

        mockMvc
                .perform(delete(BATTLE_PATH+"/"+battle.getId()))
                .andExpect(status().isOk());
    }

    @Test
    void shouldFailDeletingNonExistentBattle() throws Exception {
        this.mockMvc
                .perform(delete(BATTLE_PATH+"/"+100000))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();
    }
}