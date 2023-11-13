package co.fullstacklabs.battlemonsters.challenge.controller;

import co.fullstacklabs.battlemonsters.challenge.ApplicationConfig;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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


    @Test
    void shouldFetchAllBattles() throws Exception {
        this.mockMvc.perform(get(BATTLE_PATH)).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", Is.is(1)));
    }

    @Test
    void shouldFailBattleWithInexistentMonster() throws Exception {
        this.mockMvc.perform(post(BATTLE_PATH)
                        .param("monsterAId", "5433")
                        .param("monsterBId", "2")
                ).andExpect(status().isNotFound());
    }


    @Test
    void shouldInsertBattleWithMonsterBWinning() throws Exception {
        this.mockMvc.perform(post(BATTLE_PATH)
                .param("monsterAId", "1")
                .param("monsterBId", "2")
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$.winner.id").value(2));
    }

    @Test
    void shouldDeleteBattleSucessfully() throws Exception {
        this.mockMvc.perform(delete(BATTLE_PATH + "/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldFailDeletingInexistentBattle() throws Exception {
        this.mockMvc.perform(delete(BATTLE_PATH + "/15"))
                .andExpect(status().isNotFound());
    }
}
