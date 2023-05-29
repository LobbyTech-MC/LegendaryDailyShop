package com.gyzer.legendary.dailyshop.Utils;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.logging.Logger;

public class PlayerDataUtils {

    public static void checkData(String p)
    {
        File file=new File("./plugins/LegendaryDailyShop/Data",p+".yml");
        if(!file.exists())
        {
            Calendar cal=Calendar.getInstance();
            YamlConfiguration yml=YamlConfiguration.loadConfiguration(file);
            yml.set("login",cal.get(Calendar.DATE));
            try {
                yml.save(file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.out.println("为玩家 "+p+" 创建数据文件");
        }
    }




    public static YamlConfiguration getData(String p)
    {
        checkData(p);
        File file=new File("./plugins/LegendaryDailyShop/Data",p+".yml");
        return YamlConfiguration.loadConfiguration(file);
    }

    public static void setData(String p,String path,String arg)
    {
        File file=new File("./plugins/LegendaryDailyShop/Data",p+".yml");
        YamlConfiguration yml=YamlConfiguration.loadConfiguration(file);
        yml.set(path,arg);
        try {
            yml.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void setData(String p,String path,int arg)
    {
        File file=new File("./plugins/LegendaryDailyShop/Data",p+".yml");
        YamlConfiguration yml=YamlConfiguration.loadConfiguration(file);
        yml.set(path,arg);
        try {
            yml.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
