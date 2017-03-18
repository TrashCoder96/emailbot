package service;

import controller.ParamsRo;
import ro.BotRo;
import ro.BotTypeInfoRo;

import java.util.List;

/**
 * Created by ivan on 18.03.17.
 */
public interface IManagementService {

    BotTypeInfoRo botTypeInfo();

    void changeBotParams(ParamsRo request);

    void startBot(String botId);

    void stopBot(String botId);

    List<BotRo> getBots(String account_id);

    BotRo getBot(String botId);

    void deleteBot(String botId);

    BotRo createBot(String name, String account_id);

    public Boolean checkAccess(String botId, String account_id);

}
