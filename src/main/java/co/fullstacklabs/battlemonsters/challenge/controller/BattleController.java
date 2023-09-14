package co.fullstacklabs.battlemonsters.challenge.controller;

import java.util.List;

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

    private BattleService battleService;

    public BattleController(BattleService battleService) {
        this.battleService = battleService;
    }

    @GetMapping
    public List<BattleDTO> getAll() {
        return battleService.getAll();
    }
    @PostMapping
    public BattleDTO fight(@RequestBody BattleDTO battleDTO) {
        return battleService.fight(battleDTO);
    }


    
}
