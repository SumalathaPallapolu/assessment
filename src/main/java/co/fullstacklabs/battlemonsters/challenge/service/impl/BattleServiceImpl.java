package co.fullstacklabs.battlemonsters.challenge.service.impl;

import co.fullstacklabs.battlemonsters.challenge.dto.BattleDTO;
import co.fullstacklabs.battlemonsters.challenge.model.Battle;
import co.fullstacklabs.battlemonsters.challenge.model.Monster;
import co.fullstacklabs.battlemonsters.challenge.repository.BattleRepository;
import co.fullstacklabs.battlemonsters.challenge.repository.MonsterRepository;
import co.fullstacklabs.battlemonsters.challenge.service.BattleService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Comparator.comparingInt;

/**
 * @author FullStack Labs
 * @version 1.0
 * @since 2022-10
 */
@Service
public class BattleServiceImpl implements BattleService {

    private BattleRepository battleRepository;
    private ModelMapper modelMapper;
    private MonsterRepository monsterRepository;
   
    @Autowired
    public BattleServiceImpl(BattleRepository battleRepository, ModelMapper modelMapper, MonsterRepository monsterRepository) {
        this.battleRepository = battleRepository;
        this.modelMapper = modelMapper;
        this.monsterRepository = monsterRepository;
    }

    /**
     * List all existence battles
     */
    @Override
    public List<BattleDTO> getAll() {
        return battleRepository.findAll().stream().map(battle -> modelMapper.map(battle, BattleDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public BattleDTO fight(BattleDTO battleDTO) {
        Battle battle = modelMapper.map(battleDTO, Battle.class);
        this.fight(battle);
        return modelMapper.map(battle, BattleDTO.class);
    }

    public Battle fight(Battle battle) {
        monsterRepository.save(battle.getMonsterA());
        monsterRepository.save(battle.getMonsterB());

        List<Monster> monsters = List.of(battle.getMonsterA(), battle.getMonsterB());
        Optional<Monster> fasterMonster = monsters.stream()
                .min(comparingInt(Monster::getSpeed));
        Optional<Monster> strongest = monsters.stream()
                .max(comparingInt(Monster::getAttack));
        Monster first = fasterMonster.orElseGet(() -> strongest.orElseGet(battle::getMonsterA));
        Monster defendant = monsters.stream()
                .filter(m -> !m.equals(first)).findFirst().get();

        Monster attacker = first;
        Optional<Monster> winner;
        do  {
            winner = turn(attacker, defendant);
            Monster temp = attacker;
            attacker = defendant;
            defendant = temp;
        } while (!winner.isPresent());

        battle.setWinner(winner.get());
        battleRepository.save(battle);
        return battle;
    }

    @Override
    public void delete(int id) {
        monsterRepository.deleteById(id);
    }

    private Optional<Monster> turn(Monster attacker, Monster defendant) {
        int damage = attacker.getAttack() <= defendant.getDefense()?1:attacker.getAttack() - defendant.getDefense();
        defendant.setHp(defendant.getHp() - damage);
        monsterRepository.save(defendant);
        return Optional.ofNullable(defendant.getHp() <= 0?attacker:null);
    }


}
