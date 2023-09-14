package co.fullstacklabs.battlemonsters.challenge.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.univocity.parsers.common.processor.BeanListProcessor;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;

import co.fullstacklabs.battlemonsters.challenge.dto.MonsterDTO;
import co.fullstacklabs.battlemonsters.challenge.exceptions.ResourceNotFoundException;
import co.fullstacklabs.battlemonsters.challenge.exceptions.UnprocessableFileException;
import co.fullstacklabs.battlemonsters.challenge.model.Monster;
import co.fullstacklabs.battlemonsters.challenge.repository.MonsterRepository;
import co.fullstacklabs.battlemonsters.challenge.service.MonsterService;

import javax.validation.*;

import static java.util.stream.Collectors.toList;

/**
 * @author FullStack Labs
 * @version 1.0
 * @since 2022-10
 */
@Service
public class MonsterServiceImpl implements MonsterService {

    private MonsterRepository monsterRepository;
    private ModelMapper modelMapper;
    private Validator validator;
    public MonsterServiceImpl(MonsterRepository monsterRepository, ModelMapper modelMapper, Validator validator) {
        this.monsterRepository = monsterRepository;
        this.modelMapper = modelMapper;
        this.validator = validator;
    }

    @Override
    public MonsterDTO create(MonsterDTO monsterDTO) {
        Monster monster = modelMapper.map(monsterDTO, Monster.class);
        Set<ConstraintViolation<Monster>> constraintViolations = validator.validate(monster);
        if (!constraintViolations.isEmpty())
            throw new ConstraintViolationException(constraintViolations);
        monster = monsterRepository.save(monster);
        return modelMapper.map(monster, MonsterDTO.class);
    }

    private Monster findMonsterById(int id) {
         return monsterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Monster not found"));
    }

    @Override
    public MonsterDTO findById(int id) {
        Monster monster = findMonsterById(id);
        return modelMapper.map(monster, MonsterDTO.class);
    }

    @Override
    public MonsterDTO update(MonsterDTO monsterDTO) {
        Monster monster = findMonsterById(monsterDTO.getId());
        monster = modelMapper.map(monsterDTO, Monster.class);        
        monsterRepository.save(monster);
        return modelMapper.map(monster, MonsterDTO.class);

    }

    @Override
    public void delete(Integer id) {
        Monster monster = findMonsterById(id);
        monsterRepository.delete(monster);
    }

    public void importFromInputStream(InputStream inputStream) {
        try (Reader inputReader = new InputStreamReader(inputStream, "UTF-8")) {
            BeanListProcessor<MonsterDTO> rowProcessor = new BeanListProcessor<MonsterDTO>(MonsterDTO.class);
            CsvParserSettings settings = new CsvParserSettings();
            settings.setHeaderExtractionEnabled(true);
            settings.setProcessor(rowProcessor);
            CsvParser parser = new CsvParser(settings);
            parser.parse(inputReader);
            List<MonsterDTO> monsters = rowProcessor.getBeans();            
            monsters.forEach(m -> create(m));
        } catch (IOException ex) {
            throw new UnprocessableFileException(ex.getMessage());
        }
    }

    @Override
    public List<MonsterDTO> getAll() {
         return monsterRepository.findAll()
                 .stream()
                 .map(monster -> modelMapper.map(monster, MonsterDTO.class))
                 .collect(toList());
    }

}
