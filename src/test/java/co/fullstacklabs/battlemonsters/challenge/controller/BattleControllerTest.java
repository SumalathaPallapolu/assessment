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
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
    @BeforeEach
    void prepareBattle() {
        Battle b = BattleTestBuilder.builder()
                .monsterA(MonsterTestBuilder.builder()
                        .name("Godzilla").hp(100).speed(100).attack(40).defense(40).imageURL("http://images.com/godzilla")
                        .build())
                .monsterB(MonsterTestBuilder.builder()
                        .name("Mothra").hp(100).speed(30).attack(50).defense(20).imageURL("http://images.com/mothra").build())
                .build();
        battleRepository.save(b);
    }

    @Test
    void shouldFetchAllBattles() throws Exception {
        this.mockMvc.perform(get(BATTLE_PATH)).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", instanceOf(Integer.class)));
    }
    
    @Test
    void shouldFailBattleWithNonExistentMonster() {


        assertEquals(1, 0);
    }
    @Test
    void shouldInsertBattleWithMonsterBWinning() throws Exception {
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
        Monster godzilla = MonsterTestBuilder.builder()
                .name("Godzilla").hp(100).speed(100).attack(40).defense(40).imageURL("http://images.com/godzilla")
                .build();
        Monster mothra = MonsterTestBuilder.builder()
                .name("Mothra").hp(100).speed(30).attack(50).defense(20).imageURL("http://images.com/mothra")
                .build();
        Battle battle = BattleTestBuilder.builder()
                .monsterA(godzilla)
                .monsterB(mothra)
                .build();
        monsterRepository.save(godzilla);
        monsterRepository.save(mothra);
        battleRepository.save(battle);
        battleRepository.flush();

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