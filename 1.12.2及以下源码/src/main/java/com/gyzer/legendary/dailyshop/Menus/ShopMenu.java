package com.gyzer.legendary.dailyshop.Menus;

import com.gyzer.legendary.dailyshop.Database.Config;
import com.gyzer.legendary.dailyshop.Database.MenuRead;
import com.gyzer.legendary.dailyshop.Database.PlayerData;
import com.gyzer.legendary.dailyshop.Utils.ShopUtils;
import com.gyzer.legendary.dailyshop.Utils.sealizedUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;


public class ShopMenu implements InventoryHolder {
    private static Inventory inv;

    @Override
    public  Inventory getInventory() {
        return inv;
    }

    private Player p;
    public ShopMenu(Player p)
    {
        this.p=p;
        this.inv= Bukkit.createInventory(this, MenuRead.size,MenuRead.title);
    }

    public void load()
    {
        for (ItemStack i:MenuRead.CustomItem.keySet())
        {
            for (int slot:MenuRead.CustomItem.get(i)) {
                inv.setItem(slot,i);
            }
        }

         int in=0;
        for (String uuid: ShopUtils.getItemsYml().getStringList("today"))
        {
             String rarity=ShopUtils.getRarity(uuid);

            int line=ShopUtils.getItemsYml().getStringList(rarity+"_uuid").indexOf(uuid);
            int limit=ShopUtils.getItemsYml().getIntegerList(rarity+"_limit").get(line);
            String type=ShopUtils.getItemsYml().getStringList(rarity+"_type").get(line);
            String currency=ShopUtils.getItemsYml().getStringList(rarity+"_currency").get(line);
            PlayerData data=PlayerData.getData(p);
            int amount=data.getAmount(rarity,uuid);

            ItemStack i=ShopUtils.getItems(ShopUtils.getRarity(uuid)).get(line);
            ItemMeta id=i.getItemMeta();
            List<String> l=new ArrayList<>();
            List<String> lore=MenuRead.holder_shopitem_lore;
            for (String msg:lore)
            {
                if (msg.equals("%lore%"))
                {
                    List<String> oldlore=new ArrayList<>();
                    if (id.hasLore())
                    {
                        oldlore=id.getLore();
                    }
                    l.addAll(oldlore);
                }
                else {
                    String rarityName=MenuRead.holder_rarity_common;
                    if (rarity.equals("rare"))
                    {
                        rarityName=MenuRead.holder_rarity_rare;
                    }
                    else if (rarity.equals("epic"))
                    {
                        rarityName=MenuRead.holder_rarity_epic;
                    }
                    else if (rarity.equals("legendary"))
                    {
                        rarityName=MenuRead.holder_rarity_legendary;
                    }
                    String typeName=MenuRead.holder_type_buy;
                    if (type.equals("收购"))
                    {
                        typeName=MenuRead.holder_type_sell;
                    }
                    String currencyName=MenuRead.holder_currency_vault;
                    if (currency.equals("points"))
                    {
                        currencyName=MenuRead.holder_currency_points;
                    }
                    String limitString="";
                    if (limit > 0 && type.equals("出售"))
                    {
                        limitString=MenuRead.holder_limit_buy.replace("%player_buy%",""+amount).replace("%limit%",""+limit);
                    }
                    if (limit > 0 && type.equals("收购"))
                    {
                        limitString=MenuRead.holder_limit_sell.replace("%player_sell%",""+amount).replace("%limit%",""+limit);
                    }
                    double price=ShopUtils.getItemsYml().getDoubleList("today_price").get(in);
                    l.add(msg.replace("%placeholder_limit%",limitString).replace("%currency%",currencyName).replace("%price%",""+price).replace("%placeholder_type%",typeName).replace("%placeholder_rarity%",rarityName));
                }
            }
            id.setLore(l);
            i.setItemMeta(id);
            if (MenuRead.layout.size() > in) {
                inv.setItem(MenuRead.layout.get(in), i);
            }
            in++;
        }



    }


    public String getUUIDBySlot(int slot)
    {
        if (MenuRead.layout.contains(slot))
        {
            int in=MenuRead.layout.indexOf(slot);
            return ShopUtils.getItemsYml().getStringList("today").get(in);
        }
        return null;

    }

    public String getBuyOrSell(String uuid)
    {
        String rarity=ShopUtils.getRarity(uuid);
        int in=ShopUtils.getItemsYml().getStringList(rarity+"_uuid").indexOf(uuid);
        return ShopUtils.getItemsYml().getStringList(rarity+"_type").get(in);
    }

    public double getTodayPrice(String uuid)
    {
        int in=ShopUtils.getItemsYml().getStringList("today").indexOf(uuid);
        return ShopUtils.getItemsYml().getDoubleList("today_price").get(in);
    }
    public int getLimit(String uuid)
    {
        String rarity=ShopUtils.getRarity(uuid);

        int in=ShopUtils.getItemsYml().getStringList(rarity+"_uuid").indexOf(uuid);

        return ShopUtils.getItemsYml().getIntegerList(rarity+"_limit").get(in);
    }
    public String getVaultOrPoints(String uuid)
    {
        String rarity=ShopUtils.getRarity(uuid);
        int in=ShopUtils.getItemsYml().getStringList(rarity+"_uuid").indexOf(uuid);
        return ShopUtils.getItemsYml().getStringList(rarity+"_currency").get(in);
    }

    public ItemStack getItem(String uuid)
    {
        String rarity=ShopUtils.getRarity(uuid);
        int in=ShopUtils.getItemsYml().getStringList(rarity+"_uuid").indexOf(uuid);
        return sealizedUtils.StringToItem(ShopUtils.getItemsYml().getStringList(rarity).get(in));
    }
}
