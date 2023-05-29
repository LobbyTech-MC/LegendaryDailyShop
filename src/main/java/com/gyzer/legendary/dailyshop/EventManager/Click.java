package com.gyzer.legendary.dailyshop.EventManager;

import com.gyzer.legendary.dailyshop.Database.Config;
import com.gyzer.legendary.dailyshop.Database.MenuRead;
import com.gyzer.legendary.dailyshop.Database.PlayerData;
import com.gyzer.legendary.dailyshop.LegendaryDailyShop;
import com.gyzer.legendary.dailyshop.Menus.DeleteMenu;
import com.gyzer.legendary.dailyshop.Menus.ShopMenu;
import com.gyzer.legendary.dailyshop.Utils.ShopUtils;
import com.gyzer.legendary.dailyshop.Utils.sealizedUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.units.qual.C;

public class Click implements Listener {
    @EventHandler
    public void onInvClick(InventoryClickEvent e)
    {
        Player p=(Player) e.getWhoClicked();
        DeleteMenu deleteMenu= ShopUtils.getDeleteHolder(e.getInventory());
        ShopMenu shopMenu=ShopUtils.getShopMenuHolder(e.getInventory());
        if (deleteMenu != null)
        {
            e.setCancelled(true);
            if (e.getRawSlot() >=0 && e.getRawSlot() <=44)
            {
                if (e.getCurrentItem() == null)
                {
                    return;
                }
                if (e.isShiftClick() && e.isRightClick())
                {
                    int id=e.getRawSlot();
                    ShopUtils.removeShopItem(deleteMenu.getRarity(),id);
                    p.sendMessage(Config.plugin+"成功将该商品移除");
                    String rarity= deleteMenu.getRarity();
                    int page= deleteMenu.getPage();
                    DeleteMenu menu=new DeleteMenu(rarity,page);
                    menu.loadMenu();
                    p.openInventory(menu.getInventory());
                }
            }
            return;
        }
        if (shopMenu != null)
        {
            e.setCancelled(true);
            if (e.getRawSlot() >=0 && e.getRawSlot() < MenuRead.size)
            {
                if (e.getCurrentItem() == null)
                {
                    return;
                }
                if (shopMenu.getUUIDBySlot(e.getRawSlot()) != null)
                {
                    String uuid=shopMenu.getUUIDBySlot(e.getRawSlot());
                    double price=shopMenu.getTodayPrice(uuid);
                    int limit=shopMenu.getLimit(uuid);
                    PlayerData data=PlayerData.getData(p);
                    if (limit > 0 &&data.getAmount(ShopUtils.getRarity(uuid),uuid) >= limit)
                    {
                        p.sendMessage(Config.plugin+Config.lang_cant_limit);
                        return;
                    }
                    ItemStack i=shopMenu.getItem(uuid);
                    if (deal(p,i,uuid,shopMenu.getBuyOrSell(uuid),shopMenu.getVaultOrPoints(uuid),price)) {
                        if (limit > 0) {
                            data.addAmount(uuid);
                        }
                        shopMenu = new ShopMenu(p);
                        shopMenu.load();
                        p.openInventory(shopMenu.getInventory());
                    }
                    return;
                 }
            }
        }


    }
    public boolean deal(Player p, ItemStack i, String uuid, String type, String currency, double price)
    {
        if (type.equals("出售"))
        {
            String noMsg=Config.lang_cant_noVault;
            String buyMsg=Config.lang_buy_Vault;
            if (currency.equals("points"))
            {
                buyMsg=Config.lang_buy_PlayerPoints;
                noMsg=Config.lang_cant_PlayerPoints;
            }
            if (currency.equals("vault") && LegendaryDailyShop.econ.getBalance(p.getName()) >= price)
            {
                p.getInventory().addItem(i);
                LegendaryDailyShop.econ.withdrawPlayer(p.getName(),price);
                String name=i.getType().name();
                if (i.getItemMeta().hasDisplayName())
                {
                    name=i.getItemMeta().getDisplayName();
                }
                p.sendMessage(Config.plugin+buyMsg.replace("%price%",""+price).replace("%display%",name));
                return true;
            }
            else if (currency.equals("points") && LegendaryDailyShop.ppAPI.look(p.getUniqueId()) >= price)
            {
                p.getInventory().addItem(i);
                LegendaryDailyShop.ppAPI.take(p.getUniqueId(), (int) price);
                String name=i.getType().name();
                if (i.getItemMeta().hasDisplayName())
                {
                    name=i.getItemMeta().getDisplayName();
                }
                p.sendMessage(Config.plugin+buyMsg.replace("%price%",""+price).replace("%display%",name));
                return true;
            }
            else {
                p.sendMessage(Config.plugin+ noMsg.replace("%price%",price+""));
               return false;
            }

        }
        else {
            String sellMsg=Config.lang_sell_Vault;
            if (currency.equals("points"))
            {
                sellMsg=Config.lang_sell_PlayerPoints;
            }
            ItemStack item=i.clone();
            item.setAmount(1);

            if (ShopUtils.hasItem(p,item) >= i.getAmount() )
            {
                String name=i.getType().name();
                if (i.getItemMeta().hasDisplayName())
                {
                    name=i.getItemMeta().getDisplayName();
                }
                ShopUtils.takeItem(p,item,i.getAmount());
                p.sendMessage(Config.plugin+sellMsg.replace("%amount%",""+i.getAmount()).replace("%display%",name).replace("%price%",""+(price*i.getAmount())));
                if (currency.equals("vault"))
                {
                    LegendaryDailyShop.econ.depositPlayer(p.getName(),(i.getAmount()*price));
                }
                else {
                    LegendaryDailyShop.ppAPI.give(p.getUniqueId(), (int) (i.getAmount()*price));
                }
                return  true;
            }else {
                String name=i.getType().name();
                if (i.getItemMeta().hasDisplayName())
                {
                    name=i.getItemMeta().getDisplayName();
                }
                p.sendMessage(Config.plugin+Config.lang_cant_noItem.replace("%display%",name).replace("%amount%",""+i.getAmount()));
                return false;
            }
        }
    }


}
