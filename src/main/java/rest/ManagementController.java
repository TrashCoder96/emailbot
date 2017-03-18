package rest;

import controller.ParamsRo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.BotRo;
import ro.BotTypeInfoRo;
import service.IManagementService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ivan on 18.03.17.
 */

@RestController
@RequestMapping(value = "/management")
public class ManagementController {

    @Autowired
    private IManagementService managementService;

    @RequestMapping(value = "/botTypeInfo", method = RequestMethod.POST)
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

    @RequestMapping(value = "/changeBotParams", method = RequestMethod.POST)
    public ResponseEntity changeBotParams(@RequestBody ParamsRo request) {
        managementService.changeBotParams(request);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/startBot", method = RequestMethod.POST)
    public ResponseEntity startBot(@RequestParam String botId) {
        managementService.startBot(botId);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/stopBot", method = RequestMethod.POST)
    public ResponseEntity stopBot(@RequestParam String botId) {
        managementService.stopBot(botId);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/getBots", method = RequestMethod.POST)
    public ResponseEntity<List<BotRo>> getBots(@RequestParam String account_id) {
        return new ResponseEntity<List<BotRo>>(managementService.getBots(account_id), HttpStatus.OK);
    }

    @RequestMapping(value = "/getBot", method = RequestMethod.POST)
    public ResponseEntity<BotRo> getBot(@RequestParam String botId) {
        return new ResponseEntity<BotRo>(managementService.getBot(botId), HttpStatus.OK);
    }

    @RequestMapping(value = "/deleteBot", method = RequestMethod.POST)
    public ResponseEntity deleteBot(@RequestParam String botId) {
        managementService.deleteBot(botId);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/createBot", method = RequestMethod.POST)
    public ResponseEntity<BotRo> createBot(@RequestParam String name, @RequestParam String account_id) {
        managementService.createBot(name, account_id);
        return new ResponseEntity<BotRo>(HttpStatus.OK);
    }

    @RequestMapping(value = "/checkAccess")
    public ResponseEntity<Boolean> checkAccess(@RequestParam String botId, @RequestParam String account_id) {
        return new ResponseEntity<Boolean>(managementService.checkAccess(botId, account_id), HttpStatus.OK);
    }

}
