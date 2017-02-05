package service;

import data.BotConfigRepository;
import data.vo.BotConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by asus-pc on 04.02.2017.
 */

@Service
public class EmailSkypeService {

    @Autowired
    private BotConfigRepository botConfigRepository;

    @Transactional
    public void updateBotConfig(BotConfig config) {
        botConfigRepository.save(config);
    }


}
