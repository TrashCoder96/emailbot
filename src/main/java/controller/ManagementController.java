package controller;

import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.EmailSkypeService;
import java.util.HashMap;
import java.util.Map;

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

    @RequestMapping(value = "/getMetadata", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<Map<String, String>> getMetadata() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("name", String.class.getCanonicalName());
        map.put("cron", String.class.getCanonicalName());
        map.put("email", String.class.getCanonicalName());
        map.put("emailpassword", String.class.getCanonicalName());
        map.put("microsoftid", String.class.getCanonicalName());
        map.put("microsoftsecret", String.class.getCanonicalName());
        map.put("emailserver", String.class.getCanonicalName());
        map.put("skypeid", String.class.getCanonicalName());
        return new ResponseEntity<Map<String, String>>(map, HttpStatus.OK);
    }

}
