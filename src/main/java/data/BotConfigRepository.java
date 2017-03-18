package data;

import data.vo.BotConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by asus-pc on 04.02.2017.
 */

@Repository
public interface BotConfigRepository extends JpaRepository<BotConfig, String> {

    List<BotConfig> findByAccountId(String accountId);

}
