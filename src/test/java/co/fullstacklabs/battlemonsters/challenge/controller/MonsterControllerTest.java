package co.fullstacklabs.battlemonsters.challenge.controller;

import co.fullstacklabs.battlemonsters.challenge.ApplicationConfig;
import co.fullstacklabs.battlemonsters.challenge.dto.MonsterDTO;
import co.fullstacklabs.battlemonsters.challenge.repository.MonsterRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;

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
public class MonsterControllerTest {
    private static final String MONSTER_PATH = "/monster";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MonsterRepository monsterRepository;

    @Test
    void shouldFetchAllMonsters() throws Exception {
        this.mockMvc.perform(get(MONSTER_PATH+"/")).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", Is.is(1)))
                .andExpect(jsonPath("$[0].name", Is.is("Monster 1")))
                .andExpect(jsonPath("$[0].attack", Is.is(50)))
                .andExpect(jsonPath("$[0].defense", Is.is(40)))
                .andExpect(jsonPath("$[0].hp", Is.is(30)))
                .andExpect(jsonPath("$[0].speed", Is.is(25)));

    }

    @Test
    void shouldGetMosterSuccessfully() throws Exception {
        long id = 1l;
        this.mockMvc.perform(get(MONSTER_PATH + "/{id}", id)).andExpect(status().isOk())
                .andExpect(jsonPath("$.name", Is.is("Monster 1")));
    }

    @Test
    void shouldGetMonsterNotExists() throws Exception {
        long id = 3l;
        this.mockMvc.perform(get(MONSTER_PATH + "/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldDeleteMonsterSuccessfully() throws Exception {
        int id = 4;

        MonsterDTO newMonster = MonsterDTO.builder().id(id).name("Monster 4")
                .attack(50).defense(30).hp(30).speed(22)
                .imageUrl("ImageURL1").build();

        this.mockMvc.perform(post(MONSTER_PATH).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newMonster)));


        this.mockMvc.perform(delete(MONSTER_PATH + "/{id}", id))
                .andExpect(status().isOk());
    }

    @Test
    void shouldDeleteMonsterNotFound() throws Exception {
        int id = 5;

        this.mockMvc.perform(delete(MONSTER_PATH + "/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    void testImportCsvSuccessfully() throws Exception {
        // Implement take as a sample data/monsters-correct.csv
        File initialFile = new File("data/monsters-correct.csv");
        InputStream targetStream = new FileInputStream(initialFile);
        HashMap<String, String> contentTypeParams = new HashMap<>();
        contentTypeParams.put("boundary", "265001916915712");
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", targetStream);
        mockMvc.perform(fileUpload(MONSTER_PATH + "/import")
                        .file(mockMultipartFile)
                .contentType(new MediaType("multipart", "form-data", contentTypeParams))
        ).andExpect(status().isOk());
    }

    @Test
    void testImportCsvNonexistentColumns() throws Exception {
        //Implement take as a sample data/monsters-wrong-column.csv
        File initialFile = new File("data/monsters-wrong-column.csv");
        InputStream targetStream = new FileInputStream(initialFile);
        HashMap<String, String> contentTypeParams = new HashMap<>();
        contentTypeParams.put("boundary", "265001916915724");
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", targetStream);
        mockMvc.perform(fileUpload(MONSTER_PATH + "/import")
                .file(mockMultipartFile)
                .contentType(new MediaType("multipart", "form-data", contentTypeParams))
        ).andExpect(status().is4xxClientError())
                .andExpect(jsonPath("violations[0].message").value("may not be null"))
                .andExpect(jsonPath("violations[0].details").value("defense"))
                .andReturn();
    }


}
