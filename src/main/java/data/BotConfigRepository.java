package data;

import data.vo.BotConfig;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by asus-pc on 04.02.2017.
 */
public interface BotConfigRepository extends JpaRepository<BotConfig, String> {
}
