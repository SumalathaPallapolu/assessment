package co.fullstacklabs.battlemonsters.challenge.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import co.fullstacklabs.battlemonsters.challenge.dto.MonsterDTO;
import co.fullstacklabs.battlemonsters.challenge.model.Monster;
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

    private transient BattleRepository battleRepository;
    private transient ModelMapper modelMapper;

   
    @Autowired
    public BattleServiceImpl(BattleRepository battleRepository, ModelMapper modelMapper) {
        this.battleRepository = battleRepository;
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
    public MonsterDTO startBattle(Monster a, Monster b) {
        boolean aTurnToAttack;

        if (a.getSpeed().equals(b.getSpeed())) {
            aTurnToAttack = a.getAttack() > b.getAttack();
        } else {
            aTurnToAttack =  (a.getSpeed() > b.getSpeed());
        }

        while (a.getHp() != 0 || b.getHp() != 0) {
            if (aTurnToAttack) {
                calcDamage(a, b);
            } else {
                calcDamage(b, a);
            }
        }

        Monster winner;
        if (a.getHp() > b.getHp()) {
            winner = a;
        } else {
            winner = b;
        }

        Battle battle = new Battle();
        battle.setMonsterA(a);
        battle.setMonsterB(b);
        battle.setWinner(winner);


        battleRepository.save(battle);

        return modelMapper.map(winner, MonsterDTO.class);

    }

    @Override
    public void deleteBattleById(int id) {
        battleRepository.deleteById(id);
    }

    private void calcDamage(Monster attacker, Monster defender) {
        int damage = (attacker.getAttack() <= attacker.getDefense()) ? 1 : attacker.getAttack() - defender.getDefense();
        defender.setHp(defender.getHp() - damage);
    }
}
