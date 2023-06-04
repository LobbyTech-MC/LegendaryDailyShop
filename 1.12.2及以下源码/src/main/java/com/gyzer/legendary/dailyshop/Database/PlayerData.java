package com.gyzer.legendary.dailyshop.Database;

import com.gyzer.legendary.dailyshop.Utils.PlayerDataUtils;
import com.gyzer.legendary.dailyshop.Utils.ShopUtils;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import java.util.HashMap;

public class PlayerData {

    private static HashMap<Player,PlayerData> cache=new HashMap<>();

    private static HashMap<String,Integer> amount=new HashMap<>();

    private Player p;

    public PlayerData(Player p)
    {
        this.p=p;
        load();
        cache.put(p,this);
    }


    public void load()
    {
        String[] raritys={
                "common","rare","epic","legendary"
        };
        for (String rarity:raritys)
        {
            if (yml().contains("data."+rarity))
            {
                for (String uuid:yml().getConfigurationSection("data."+rarity).getKeys(false))
                {
                    amount.put(uuid,yml().getInt("data."+rarity+"."+uuid));
                }
            }
        }
    }
    public YamlConfiguration yml()
    {
        return  PlayerDataUtils.getData(p.getName());
    }
    public static PlayerData getData(Player p)
    {
        return cache.get(p) !=null ? cache.get(p) : null ;
    }

    public int getAmount(String rarity,String uuid)
    {
        return amount.get(uuid)!=null ? amount.get(uuid) : 0;
    }

    public void addAmount(String uuid)
    {
       if (ShopUtils.getRarity(uuid).equals("null"))
       {
           return;
       }
       int amount=getAmount(ShopUtils.getRarity(uuid),uuid);
       this.amount.put(uuid,(amount+1));
       PlayerDataUtils.setData(p.getName(),"data."+ShopUtils.getRarity(uuid)+"."+uuid,amount+1);
    }




}
