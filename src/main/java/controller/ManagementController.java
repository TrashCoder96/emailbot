package controller;

import data.BotConfigRepository;
import data.vo.BotConfig;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import service.EmailSkypeService;

import java.util.UUID;

/**
 * Created by asus-pc on 04.02.2017.
 */

@RestController
@RequestMapping(value = "/management")
public class ManagementController {

    @Autowired
    private EmailSkypeService emailSkypeService;

    @RequestMapping(value = "/start", method = RequestMethod.POST)
    public void start(@RequestParam String id) throws SchedulerException {
        emailSkypeService.start(id);
    }

    @RequestMapping(value = "/stop", method = RequestMethod.POST)
    public void stop(@RequestParam String id) throws SchedulerException {
       emailSkypeService.start(id);
    }

    @RequestMapping(value = "/changeParams", method = RequestMethod.POST, produces = "application/json")
    public void changeParams(@RequestBody ParamsRo paramsRo) {
        emailSkypeService.updateBotConfig(paramsRo);
    }

}
