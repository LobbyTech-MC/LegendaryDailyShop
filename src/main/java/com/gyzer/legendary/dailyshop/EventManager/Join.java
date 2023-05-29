package com.gyzer.legendary.dailyshop.EventManager;

import com.gyzer.legendary.dailyshop.Database.Config;
import com.gyzer.legendary.dailyshop.Database.PlayerData;
import com.gyzer.legendary.dailyshop.Utils.PlayerDataUtils;
import com.gyzer.legendary.dailyshop.Utils.ShopUtils;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.checkerframework.checker.units.qual.C;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

public class Join implements Listener {
    @EventHandler
    public void onJ(PlayerJoinEvent e) {
        Player p=e.getPlayer();
        File file = new File("./plugins/LegendaryDailyShop/Data", p.getName() + ".yml");
        if (!file.exists()) {
            Calendar cal = Calendar.getInstance();
            YamlConfiguration yml = YamlConfiguration.loadConfiguration(file);
            yml.set("login",cal.get(Calendar.DATE));
            try {
                yml.save(file);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            System.out.println("为玩家 "+p+" 创建数据文件");
            SendTodayMsg(p);
            PlayerData data=new PlayerData(p);
        }
        else {
            PlayerData data=new PlayerData(p);
            Calendar cal = Calendar.getInstance();
            YamlConfiguration yml = YamlConfiguration.loadConfiguration(file);
            if (yml.getInt("login") != cal.get(Calendar.DATE))
            {
                yml.set("login",cal.get(Calendar.DATE));
                yml.set("data",null);
                try {
                    yml.save(file);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                SendTodayMsg(p);
            }
        }
    }

    public static void SendTodayMsg(Player p)
    {
        if (Config.tips_common_enable && ShopUtils.getRarityAmountToday("common") > 0)
        {
            p.sendMessage(Config.plugin+Config.tips_common.replace("%amount%",""+ShopUtils.getRarityAmountToday("common")));
        }
        if (Config.tips_rare_enable && ShopUtils.getRarityAmountToday("rare") > 0)
        {
            p.sendMessage(Config.plugin+Config.tips_rare.replace("%amount%",""+ShopUtils.getRarityAmountToday("rare")));
        }
        if (Config.tips_epic_enable && ShopUtils.getRarityAmountToday("epic") > 0)
        {
            p.sendMessage(Config.plugin+Config.tips_epic.replace("%amount%",""+ShopUtils.getRarityAmountToday("epic")));
        }
        if (Config.tips_legendary_enable && ShopUtils.getRarityAmountToday("legendary") > 0)
        {
            p.sendMessage(Config.plugin+Config.tips_legendary.replace("%amount%",""+ShopUtils.getRarityAmountToday("legendary")));
        }
    }
}
