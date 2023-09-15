package co.fullstacklabs.battlemonsters.challenge.controller;

import co.fullstacklabs.battlemonsters.challenge.dto.BattleDTO;
import co.fullstacklabs.battlemonsters.challenge.service.BattleService;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

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
    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        battleService.delete(id);
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    @ResponseStatus(value= HttpStatus.NOT_FOUND, reason="Battle not found")
    public ModelAndView handleBattleNotFoundException(HttpServletRequest request, Exception ex){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("exception", ex);
        modelAndView.addObject("url", request.getRequestURL());

        modelAndView.setViewName("error");
        return modelAndView;
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(value= HttpStatus.UNPROCESSABLE_ENTITY, reason="Monster not found")
    public ModelAndView handleMonsterNotFoundException(HttpServletRequest request, Exception ex){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("exception", ex);
        modelAndView.addObject("url", request.getRequestURL());

        modelAndView.setViewName("error");
        return modelAndView;
    }

}
