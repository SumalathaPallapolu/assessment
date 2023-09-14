package co.fullstacklabs.battlemonsters.challenge.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.FileInputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import co.fullstacklabs.battlemonsters.challenge.dto.MonsterDTO;
import co.fullstacklabs.battlemonsters.challenge.exceptions.ResourceNotFoundException;
import co.fullstacklabs.battlemonsters.challenge.model.Monster;
import co.fullstacklabs.battlemonsters.challenge.repository.MonsterRepository;
import co.fullstacklabs.battlemonsters.challenge.service.impl.MonsterServiceImpl;
import co.fullstacklabs.battlemonsters.challenge.testbuilders.MonsterTestBuilder;

import javax.validation.Validator;

/**
 * @author FullStack Labs
 * @version 1.0
 * @since 2022-10
 */
@ExtendWith(MockitoExtension.class)
public class MonsterServiceTest {
    @InjectMocks
    public MonsterServiceImpl monsterService;

    @Mock
    public MonsterRepository monsterRepository;

    @Mock
    private ModelMapper mapper;
    @Mock
    public Validator validator ;

   @Test
   public void testGetAll() {
       String monsterName1 = "Monster 1";
       String monsterName2 = "Monster 2";
       Monster monster1 = MonsterTestBuilder.builder().id(1)
               .name(monsterName1).attack(30).defense(20).hp(21).speed(15)
               .imageURL("imageUrl1").build();
   
       Monster monster2 = MonsterTestBuilder.builder().id(2)
               .name(monsterName2).attack(30).defense(20).hp(21).speed(15)
               .imageURL("imageUrl1").build();
   
       List<Monster> monsterList = Arrays.asList(new Monster[]{monster1, monster2});
       Mockito.when(monsterRepository.findAll()).thenReturn(monsterList);
       
       monsterService.getAll();
   
       Mockito.verify(monsterRepository).findAll();
       Mockito.verify(mapper).map(monsterList.get(0), MonsterDTO.class);
       Mockito.verify(mapper).map(monsterList.get(1), MonsterDTO.class);        
   }

    @Test
    public void testGetMonsterByIdSuccessfully() throws Exception {
        int id = 1;
        Monster monster1 = MonsterTestBuilder.builder().build();
        Mockito.when(monsterRepository.findById(id)).thenReturn(Optional.of(monster1));
        monsterService.findById(id);
        Mockito.verify(monsterRepository).findById(id);
        Mockito.verify(mapper).map(monster1, MonsterDTO.class);
    }

    @Test
    public void testGetMonsterByIdNotExists() throws Exception {
        int id = 1;        
        Mockito.when(monsterRepository.findById(id)).thenReturn(Optional.empty());                
        Assertions.assertThrows(ResourceNotFoundException.class, 
                                    () -> monsterService.findById(id));
    }

    @Test
    public void testDeleteMonsterSuccessfully() throws Exception {
        int id = 1;
        Monster monster1 = MonsterTestBuilder.builder().build();
        Mockito.when(monsterRepository.findById(id)).thenReturn(Optional.of(monster1));
        Mockito.doNothing().when(monsterRepository).delete(monster1);

        monsterService.delete(id);

        Mockito.verify(monsterRepository).findById(id);        
        Mockito.verify(monsterRepository).delete(monster1);
    }

     @Test
     void testImportCsvSucessfully() throws Exception {
         // Implement take as a sample data/monster-correct.
         monsterService.importFromInputStream(new FileInputStream("data/monsters-correct.csv"));
     }

}
