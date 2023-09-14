package co.fullstacklabs.battlemonsters.challenge.controller;

import co.fullstacklabs.battlemonsters.challenge.ApplicationConfig;
import co.fullstacklabs.battlemonsters.challenge.dto.BattleDTO;
import co.fullstacklabs.battlemonsters.challenge.dto.MonsterDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    @Test
    void shouldFetchAllBattles() throws Exception {
        this.mockMvc.perform(get(BATTLE_PATH)).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", Is.is(1)));
    }
    
    @Test
    void shouldFailBattleWithNonExistentMonster() {
        //TODO: Implement
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

    @Test
    void shouldDeleteBattleSucessfully() {
        //TODO: Implement
        assertEquals(1, 0);
    }

    @Test
    void shouldFailDeletingInexistentBattle() {
        //TODO: Implement
        assertEquals(1, 0);
    }
}