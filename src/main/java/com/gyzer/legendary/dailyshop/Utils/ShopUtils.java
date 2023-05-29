package com.gyzer.legendary.dailyshop.Utils;

import com.gyzer.legendary.dailyshop.Database.Config;
import com.gyzer.legendary.dailyshop.Database.MenuRead;
import com.gyzer.legendary.dailyshop.LegendaryDailyShop;
import com.gyzer.legendary.dailyshop.Menus.DeleteMenu;
import com.gyzer.legendary.dailyshop.Menus.ShopMenu;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class ShopUtils {

    public static void addShop(ItemStack i,String rarity,String type,String currency,double min,double max,int limit)
    {
        File file=new File("./plugins/LegendaryDailyShop","items.yml");
        YamlConfiguration yml=YamlConfiguration.loadConfiguration(file);
        String item=sealizedUtils.ItemToString(i);
        List<String> itemlist=yml.getStringList(rarity);
        itemlist.add(item);
        List<String> typelist=yml.getStringList(rarity+"_type");
        typelist.add(type);
        List<String> pricelist=yml.getStringList(rarity+"_price");
        pricelist.add(min+";"+max);
        List<String> currecylist=yml.getStringList(rarity+"_currency");
        currecylist.add(currency);
        List<Integer> limitlist=yml.getIntegerList(rarity+"_limit");
        limitlist.add(limit);
        List<String> uuidlist=yml.getStringList(rarity+"_uuid");
        uuidlist.add(UUID.randomUUID().toString());
        yml.set(rarity,itemlist);
        yml.set(rarity+"_type",typelist);
        yml.set(rarity+"_price",pricelist);
        yml.set(rarity+"_currency",currecylist);
        yml.set(rarity+"_limit",limitlist);
        yml.set(rarity+"_uuid",uuidlist);
        try {
            yml.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void removeShopItem(String rarity,int line)
    {
        File file=new File("./plugins/LegendaryDailyShop","items.yml");
        YamlConfiguration yml=YamlConfiguration.loadConfiguration(file);
        List<String> itemlist=yml.getStringList(rarity);

        itemlist.remove(line);
        List<String> typelist=yml.getStringList(rarity+"_type");

        typelist.remove(line);
        List<String> pricelist=yml.getStringList(rarity+"_price");

        pricelist.remove(line);
        List<String> currecylist=yml.getStringList(rarity+"_currency");

        currecylist.remove(line);
        List<Integer> limitlist=yml.getIntegerList(rarity+"_limit");

        limitlist.remove(line);
        List<String> uuidlist=yml.getStringList(rarity+"_uuid");

        if (yml.getStringList("today").contains(uuidlist.get(line)))
        {
            List<String> today=yml.getStringList("today");
            List<Double> price=yml.getDoubleList("today_price");
            int in=today.indexOf(uuidlist.get(line));
            today.remove(in);
            price.remove(in);
            yml.set("today",today);
            yml.set("today_price",price);

        }
        uuidlist.remove(line);
        yml.set(rarity,itemlist);
        yml.set(rarity+"_type",typelist);
        yml.set(rarity+"_price",pricelist);
        yml.set(rarity+"_currency",currecylist);
        yml.set(rarity+"_limit",limitlist);
        yml.set(rarity+"_uuid",uuidlist);



        try {
            yml.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    public static List<ItemStack> getItems(String rarity)
    {
        File file=new File("./plugins/LegendaryDailyShop","items.yml");
        YamlConfiguration yml=YamlConfiguration.loadConfiguration(file);
        List<ItemStack> l=new ArrayList<>();
        for (String bytes:yml.getStringList(rarity))
        {
            l.add(sealizedUtils.StringToItem(bytes));
        }
        return l;
    }

    public static DeleteMenu getDeleteHolder(Inventory inventory) {
        InventoryHolder holder = inventory.getHolder();
        if(!(holder instanceof DeleteMenu)) {
            return null;
        }
        return (DeleteMenu) holder;
    }
    public static ShopMenu getShopMenuHolder(Inventory inventory) {
        InventoryHolder holder = inventory.getHolder();
        if(!(holder instanceof ShopMenu)) {
            return null;
        }
        return (ShopMenu) holder;
    }
    public static String getRarity(String uuid)
    {
        File file=new File("./plugins/LegendaryDailyShop","items.yml");
        YamlConfiguration yml=YamlConfiguration.loadConfiguration(file);
        if (yml.getStringList("common_uuid").contains(uuid))
        {
            return "common";
        }
        else if (yml.getStringList("rare_uuid").contains(uuid))
        {
            return "rare";
        }
        else if (yml.getStringList("epic_uuid").contains(uuid))
        {
            return "epic";
        }
        else if (yml.getStringList("legendary_uuid").contains(uuid))
        {
            return "legendary";
        }
        return "null";
    }

    public static YamlConfiguration getItemsYml()
    {
        File file=new File("./plugins/LegendaryDailyShop","items.yml");
        return YamlConfiguration.loadConfiguration(file);
    }

    public static void setItemsDate(int date)
    {
        File file=new File("./plugins/LegendaryDailyShop","items.yml");
        YamlConfiguration yml=YamlConfiguration.loadConfiguration(file);;
        yml.set("last",date);
        try {
            yml.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void randomTodayShop()
    {
        File file=new File("./plugins/LegendaryDailyShop","items.yml");
        YamlConfiguration yml=YamlConfiguration.loadConfiguration(file);;

        List<String> uuidList=new ArrayList<>();
        List<Double> priceList=new ArrayList<>();

        HashMap<String,Integer> rarityAmount=new HashMap<>();
        HashMap<String,Integer> maxAmount=new HashMap<>();
        maxAmount.put("common",Config.max_common);
        maxAmount.put("rare",Config.max_rare);
        maxAmount.put("epic",Config.max_epic);
        maxAmount.put("legendary",Config.max_legendary);
        String[] raritys={
                "legendary","epic","rare","common"
        };

        HashMap<String,List<String>> map=new HashMap<>();
        map.put("common",getItemsYml().getStringList("common_uuid"));
        map.put("rare",getItemsYml().getStringList("rare_uuid"));
        map.put("epic",getItemsYml().getStringList("epic_uuid"));
        map.put("legendary",getItemsYml().getStringList("legendary_uuid"));

        HashMap<String,List<String>> priceMap=new HashMap<>();
        priceMap.put("common",getItemsYml().getStringList("common_price"));
        priceMap.put("rare",getItemsYml().getStringList("rare_price"));
        priceMap.put("epic",getItemsYml().getStringList("epic_price"));
        priceMap.put("legendary",getItemsYml().getStringList("legendary_price"));



        for (int in=0;in< 53 ;in ++)
        {
            if (uuidList.size()==MenuRead.layout.size())
            {
                break;
            }
            for (String rarity:raritys)
            {

                List<String> list=map.get(rarity);
                List<String> pricelist=priceMap.get(rarity);

                 int amount=rarityAmount.get(rarity) != null ? rarityAmount.get(rarity) : 0;
                 if (list.isEmpty() )
                {
                    continue;
                }

                int roll= (int) (LegendaryDailyShop.cmg.getDouble("set.rarity."+rarity+".chance")*100);


                if ((new Random().nextInt(101)) <= roll)
                {
                    if ( maxAmount.get(rarity) <= amount)
                    {
                        continue;
                    }
                    rarityAmount.put(rarity,amount+1);
                    int get=(new Random()).nextInt(list.size());
                    String uuid=list.get(get);

                    int line=getItemsYml().getStringList(rarity+"_uuid").indexOf(uuid);
                    double min=Double.parseDouble(pricelist.get(line).split(";")[0]) * 100;
                    double max=Double.parseDouble(pricelist.get(line).split(";")[1]) * 100;

                    double finalprice= max;
                    if (max != min)
                    {
                        finalprice=((new Random()).nextInt((int) (max-min))+min);
                    }
                    finalprice=finalprice/100;
                    priceList.add(finalprice);
                    uuidList.add(uuid);
                    list.remove(list.indexOf(uuid));
                    map.put(rarity,list);
                }
            }


        }

        yml.set("today",uuidList);
        yml.set("today_price",priceList);
        try {
            yml.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Bukkit.getConsoleSender().sendMessage(Config.plugin+"成功刷新今日商店。数据统计如下");
        for (String rarity:rarityAmount.keySet())
        {
            Bukkit.getConsoleSender().sendMessage(Config.plugin+rarity+" ×"+rarityAmount.get(rarity));
        }
    }

    public static int hasItem(Player p, ItemStack tar)
    {
        int has=0;
        for (ItemStack i:p.getInventory().getContents())
        {
            if (i!=null)
            {
                ItemStack i2=i.clone();
                i2.setAmount(1);
                if (i2.equals(tar))
                {
                    has+=i.getAmount();
                }
            }
        }
        return has;
    }

    public static int getRarityAmountToday(String rarity)
    {
        if (ShopUtils.getItemsYml().getStringList("today").isEmpty())
        {
            return 0;
        }
        int has=0;
        for (String uuid:ShopUtils.getItemsYml().getStringList("today"))
        {
            if (ShopUtils.getRarity(uuid).equals(rarity))
            {
                has+=1;
            }
        }
        return has;

    }

    public static void takeItem(Player p,ItemStack tar,int amount)
    {
        int last=amount;
        for (ItemStack i:p.getInventory().getContents())
        {
            if (i!=null)
            {
                ItemStack i2=i.clone();
                i2.setAmount(1);
                if (i2.equals(tar))
                {
                    if (i.getAmount() >= amount)
                    {
                        i.setAmount(i.getAmount()-amount);
                        return;
                    }
                    else {
                        last+=i.getAmount();
                        i.setAmount(0);
                    }
                }
            }
        }
    }
}
