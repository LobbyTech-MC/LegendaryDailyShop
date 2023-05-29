package com.gyzer.legendary.dailyshop.Menus;

import com.gyzer.legendary.dailyshop.Utils.MsgUtils;
import com.gyzer.legendary.dailyshop.Utils.ShopUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class DeleteMenu implements InventoryHolder {


    private static String rarity;
    private static int page;
    public DeleteMenu(String rarity,int page)
    {
        this.rarity=rarity;
        this.page=page;
        this.inv= Bukkit.createInventory(this,54,"删除商品 - "+rarity+ MsgUtils.msg("&c SHIFT+右键删除"));

    }

    public void loadMenu()
    {
        int slot=0;
        for (ItemStack i:getPage(page))
        {
            inv.setItem(slot,i);
            slot++;
        }

        ItemStack i=new ItemStack(Material.PAPER);
        ItemMeta id=i.getItemMeta();
        id.setDisplayName(MsgUtils.msg("&f&l< &b上一页 &f&l>"));
        i.setItemMeta(id);
        inv.setItem(45,i);

        i=new ItemStack(Material.PAPER);
        id=i.getItemMeta();
        id.setDisplayName(MsgUtils.msg("&f&l< &b下一页 &f&l>"));
        i.setItemMeta(id);
        inv.setItem(53,i);
    }

    public List<ItemStack> getPage(int page)
    {
        List<ItemStack> l=new ArrayList<>();
        List<ItemStack> list=ShopUtils.getItems(rarity);
        int begin=0 + (page-1)*45;
        int end= 44 + (page-1)*45;
        for (int x=begin;x<=end;x++)
        {
            if (list.size() > x)
            {
              l.add(list.get(x));
            }
        }
        return l;
    }

    private static Inventory inv;
    @Override
    public Inventory getInventory() {
        return inv;
    }

    public int getPage()
    {
        return page;
    }

    public String getRarity()
    {
        return rarity;
    }

}
