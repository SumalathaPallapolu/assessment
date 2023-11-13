package co.fullstacklabs.battlemonsters.challenge.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import co.fullstacklabs.battlemonsters.challenge.exceptions.ResourceNotFoundException;
import co.fullstacklabs.battlemonsters.challenge.model.Monster;
import co.fullstacklabs.battlemonsters.challenge.service.MonsterService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.fullstacklabs.battlemonsters.challenge.dto.BattleDTO;
import co.fullstacklabs.battlemonsters.challenge.model.Battle;
import co.fullstacklabs.battlemonsters.challenge.repository.BattleRepository;
import co.fullstacklabs.battlemonsters.challenge.service.BattleService;

/**
 * @author FullStack Labs
 * @version 1.0
 * @since 2022-10
 */
@Service
public class BattleServiceImpl implements BattleService {

    private BattleRepository battleRepository;
    private MonsterService monsterService;
    private ModelMapper modelMapper;


    @Autowired
    public BattleServiceImpl(BattleRepository battleRepository, MonsterService monsterService, ModelMapper modelMapper) {
        this.battleRepository = battleRepository;
        this.monsterService = monsterService;
        this.modelMapper = modelMapper;
    }

    /**
     * List all existence battles
     */
    @Override
    public List<BattleDTO> getAll() {
        List<Battle> battles = battleRepository.findAll();
        return battles.stream().map(battle -> modelMapper.map(battle, BattleDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public void delete(int battleId) {
        Battle battle = findBattleById(battleId);
        battleRepository.delete(battle);
    }

    @Override
    public BattleDTO startBattle(int monsterAId, int monsterBId) {
        Monster monsterA = monsterService.findMonsterById(monsterAId);
        Monster monsterB = monsterService.findMonsterById(monsterBId);

        Battle battle = initalizeBattle(monsterA, monsterB);

        startFight(battle);

        return modelMapper.map(battle, BattleDTO.class);
    }

    private Battle findBattleById(int battleId) {
        return battleRepository
                .findById(battleId)
                .orElseThrow(() -> new ResourceNotFoundException("Battle not found"));
    }

    private Battle initalizeBattle(Monster monsterA, Monster monsterB) {
        Battle battle = new Battle();
        battle.setMonsterA(monsterA);
        battle.setMonsterB(monsterB);
        return battle;
    }

    private void startFight(Battle battle) {
        Monster winner = determineWinner(battle.getMonsterA(), battle.getMonsterB());
        battle.setWinner(winner);
        battleRepository.save(battle);
    }

    private Monster determineWinner(Monster monsterA, Monster monsterB) {
        while (monsterA.hasHp() && monsterB.hasHp()) {
            Monster firstAttacker = determineFirstAttacker(monsterA, monsterB);
            Monster secondAttacker = (firstAttacker == monsterA) ? monsterB : monsterA;
            calculateAndSetDamage(firstAttacker, secondAttacker);
        }

        return (monsterA.getHp() > 0) ? monsterA : monsterB;
    }

    private Monster determineFirstAttacker(Monster monsterA, Monster monsterB) {
        boolean isMonsterAFaster = monsterA.getSpeed() > monsterB.getSpeed();
        boolean isMonsterAStronger = (monsterA.getSpeed().equals(monsterB.getSpeed()) && monsterA.getAttack() > monsterB.getAttack());
        return (isMonsterAFaster || isMonsterAStronger) ? monsterA : monsterB;
    }

    private void calculateAndSetDamage(Monster attacker, Monster defender) {
        int damage = Math.max(attacker.getAttack() - defender.getDefense(), 1);
        defender.setHp(defender.getHp() - damage);
    }
}
