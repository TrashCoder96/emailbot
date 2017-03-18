package controller;

import data.BotConfigRepository;
import data.vo.BotConfig;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ro.BotRo;
import ro.BotTypeInfoRo;
import service.EmailSkypeService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by asus-pc on 04.02.2017.
 */

@RestController
public class ManagementController {

    @Autowired
    private EmailSkypeService emailSkypeService;

    @Autowired
    private BotConfigRepository botConfigRepository;

    @RequestMapping(value = "/management/botTypeInfo", method = RequestMethod.POST)
    @Transactional
    public ResponseEntity<BotTypeInfoRo> botTypeInfo() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("name", String.class.getCanonicalName());
        map.put("cron", String.class.getCanonicalName());
        map.put("email", String.class.getCanonicalName());
        map.put("emailpassword", String.class.getCanonicalName());
        map.put("microsoftid", String.class.getCanonicalName());
        map.put("microsoftsecret", String.class.getCanonicalName());
        map.put("emailserver", String.class.getCanonicalName());
        map.put("skypeid", String.class.getCanonicalName());
        BotTypeInfoRo botTypeInfoRo = new BotTypeInfoRo();
        botTypeInfoRo.setParams(map);
        botTypeInfoRo.setName("SKYPE");
        botTypeInfoRo.setDescription("none");
        return new ResponseEntity<BotTypeInfoRo>(HttpStatus.OK);
    }

    @RequestMapping(value = "/management/changeBotParams", method = RequestMethod.POST)
    public ResponseEntity changeBotParams(@RequestBody ParamsRo request) {
        emailSkypeService.updateBotConfig(request);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/management/startBot", method = RequestMethod.POST)
    public ResponseEntity startBot(@RequestParam String botId) {
        try {
            emailSkypeService.start(botId);
            return new ResponseEntity(HttpStatus.OK);
        } catch (SchedulerException e) {
            throw new RuntimeException("error");
        }
    }

    @RequestMapping(value = "/management/stopBot", method = RequestMethod.POST)
    public ResponseEntity stopBot(@RequestParam String botId) {
        try {
            emailSkypeService.stop(botId);
            return new ResponseEntity(HttpStatus.OK);
        } catch (SchedulerException e) {
            throw new RuntimeException("error");
        }
    }

    @RequestMapping(value = "/management/getBots", method = RequestMethod.POST)
    @Transactional
    public ResponseEntity<List<BotRo>> getBots(@RequestParam String account_id) {
        List<BotConfig> botConfigs = botConfigRepository.findByAccountId(account_id);
        List<BotRo> botRos = new ArrayList<BotRo>();
        for (BotConfig botConfig: botConfigs) {
            BotRo botRo = new BotRo();
            botRo.setId(botConfig.getId());
            botRo.setName(botConfig.getName());
            botRo.setType("SKYPE");
            Map<String, String> map = new HashMap<String, String>();
            map.put("name", botConfig.getName());
            map.put("cron", botConfig.getCron());
            map.put("email", botConfig.getEmail());
            map.put("emailpassword", botConfig.getEmailpassword());
            map.put("microsoftid", botConfig.getMicrosoft_id());
            map.put("microsoftsecret", botConfig.getMicrosoft_secret());
            map.put("emailserver", botConfig.getEmailserver());
            map.put("skypeid", botConfig.getSkype_id());
            botRo.setParams(map);
            botRos.add(botRo);
        }
        return new ResponseEntity<List<BotRo>>(botRos, HttpStatus.OK);
    }

    @RequestMapping(value = "/management/getBot", method = RequestMethod.POST)
    public ResponseEntity<BotRo> getBot(@RequestParam String botId) {
        BotRo botRo = new BotRo();
        BotConfig botConfig = botConfigRepository.findOne(botId);

    }

    @RequestMapping(value = "/management/deleteBot", method = RequestMethod.POST)
    public ResponseEntity deleteBot(@RequestParam String botId) {

    }

    @RequestMapping(value = "/management/createBot", method = RequestMethod.POST)
    public ResponseEntity<BotRo> createBot(@RequestParam String name) {
        ParamsRo paramsRo = new ParamsRo();
        emailSkypeService.updateBotConfig(paramsRo);
        return new ResponseEntity<BotRo>(HttpStatus.OK)
    }

}
