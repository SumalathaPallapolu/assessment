package co.fullstacklabs.battlemonsters.challenge.controller;

import java.util.List;

import co.fullstacklabs.battlemonsters.challenge.dto.MonsterDTO;
import co.fullstacklabs.battlemonsters.challenge.model.Monster;
import org.springframework.web.bind.annotation.*;

import co.fullstacklabs.battlemonsters.challenge.dto.BattleDTO;
import co.fullstacklabs.battlemonsters.challenge.service.BattleService;

/**
 * @author FullStack Labs
 * @version 1.0
 * @since 2022-10
 */
@RestController
@RequestMapping("/battle")
public class BattleController {

    private transient BattleService battleService;

    public BattleController(BattleService battleService) {
        this.battleService = battleService;
    }

    @GetMapping
    public List<BattleDTO> getAll() {
        return battleService.getAll();
    }

    @PostMapping
    public MonsterDTO startBattle(Monster a, Monster b) {
        return battleService.startBattle(a,b);
    }

    @DeleteMapping
    public void deleteBattleById(int id) {
        battleService.deleteBattleById(id);
    }
}
