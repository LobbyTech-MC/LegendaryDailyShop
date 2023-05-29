package com.gyzer.legendary.dailyshop;

import com.gyzer.legendary.dailyshop.Database.Config;
import com.gyzer.legendary.dailyshop.Database.MenuRead;
import com.gyzer.legendary.dailyshop.Database.PlayerData;
import com.gyzer.legendary.dailyshop.EventManager.Click;
import com.gyzer.legendary.dailyshop.EventManager.Join;
import com.gyzer.legendary.dailyshop.Utils.MsgUtils;
import com.gyzer.legendary.dailyshop.Utils.ShopUtils;
import com.gyzer.legendary.dailyshop.cmd.command;
import net.milkbowl.vault.economy.Economy;
import org.black_ixx.playerpoints.PlayerPoints;
import org.black_ixx.playerpoints.PlayerPointsAPI;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.units.qual.C;
import org.jetbrains.annotations.NotNull;

import java.util.Calendar;

public class LegendaryDailyShop extends JavaPlugin {
    public static Economy econ = null;
    public  static  PlayerPointsAPI ppAPI;
    public static FileConfiguration cmg;
    @Override
    public void onEnable() {

        if (!setupEconomy() ) {
            Bukkit.getLogger().info("未找到 Vault 插件已自动关闭");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        if (Bukkit.getPluginManager().isPluginEnabled("PlayerPoints")) {
            this.ppAPI = PlayerPoints.getInstance().getAPI();
        }
        // When you want to access the API, check if the instance is null
        if (this.ppAPI == null) {
            Bukkit.getLogger().info("未找到 PlayerPoints 插件已自动关闭");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        Bukkit.getLogger().info("已关联 PlayerPoints ");
        Bukkit.getLogger().info("已关联 Vault ");

        saveResource("menu.yml",false);
        saveResource("items.yml",false);
        saveResource("config.yml",false);
        cmg=getConfig();
        Config.load();
        MenuRead.load();
        Bukkit.getPluginCommand("lshop").setExecutor(new command());
        Bukkit.getPluginManager().registerEvents(new Click(),this);
        Bukkit.getPluginManager().registerEvents(new Join(),this);

        Bukkit.getScheduler().runTaskLaterAsynchronously(this, new Runnable() {
            @Override
            public void run() {
                for (Player p:Bukkit.getOnlinePlayers())
                {
                    PlayerData data=new PlayerData(p);
                }
            }
        },20);

        Bukkit.getScheduler().runTaskTimerAsynchronously(this, new Runnable() {
            @Override
            public void run() {
                Calendar cal= Calendar.getInstance();
                int date=cal.get(Calendar.DATE);
                if (ShopUtils.getItemsYml().getInt("last") != date)
                {
                    Bukkit.getConsoleSender().sendMessage("");
                    Bukkit.getConsoleSender().sendMessage(Config.plugin+ "新的一天到来，每日商店商品随机刷新....");
                    Bukkit.getConsoleSender().sendMessage("");
                    ShopUtils.setItemsDate(date);
                    ShopUtils.randomTodayShop();

                }
            }
        },20*60,20*60);
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

}
