package com.gyzer.legendary.dailyshop.Database;

import com.gyzer.legendary.dailyshop.Utils.MsgUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.HashMap;
import java.util.List;

public class MenuRead {
    public static String title;
    public static int size;
    public static HashMap<ItemStack, List<Integer>> CustomItem =new HashMap<>();

    public static List<Integer> layout;
    public static String holder_type_buy;
    public static String holder_type_sell;
    public static String holder_rarity_common;
    public static String holder_rarity_rare;
    public static String holder_rarity_epic;
    public static String holder_rarity_legendary;
    public static List<String> holder_shopitem_lore;
    public static String holder_currency_vault;
    public static String holder_currency_points;

    public static String holder_limit_buy;
    public static String holder_limit_sell;
    public static void load()
    {
        File file=new File("./plugins/LegendaryDailyShop","menu.yml");
        YamlConfiguration yml=YamlConfiguration.loadConfiguration(file);

        title = MsgUtils.msg(yml.getString("title"));
        size = yml.getInt("size");
        layout=yml.getIntegerList("layout");
        holder_currency_vault = MsgUtils.msg(yml.getString("placeholder.currency.vault"));
        holder_currency_points = MsgUtils.msg(yml.getString("placeholder.currency.points"));
        holder_type_buy = MsgUtils.msg(yml.getString("placeholder.type.buy"));
        holder_type_sell = MsgUtils.msg(yml.getString("placeholder.type.sell"));
        holder_rarity_common = MsgUtils.msg(yml.getString("placeholder.rarity.common"));
        holder_rarity_rare = MsgUtils.msg(yml.getString("placeholder.rarity.rare"));
        holder_rarity_epic = MsgUtils.msg(yml.getString("placeholder.rarity.epic"));
        holder_rarity_legendary = MsgUtils.msg(yml.getString("placeholder.rarity.legendary"));
        holder_shopitem_lore = MsgUtils.msg(yml.getStringList("placeholder.shopItemLoreFormat"));
        holder_limit_sell = MsgUtils.msg(yml.getString("placeholder.limit.sell"));
        holder_limit_buy = MsgUtils.msg(yml.getString("placeholder.limit.buy"));

        for (String name:yml.getConfigurationSection("customItem").getKeys(false))
        {
            ItemStack i=new ItemStack(Material.getMaterial(yml.getString("customItem."+name+".material")),1,(short)yml.getInt("customItem."+name+".data"));
            ItemMeta id=i.getItemMeta();
            id.setDisplayName(MsgUtils.msg(yml.getString("customItem."+name+".display")));
            id.setLore(MsgUtils.msg(yml.getStringList("customItem."+name+".lore")));
            i.setItemMeta(id);
            CustomItem.put(i,yml.getIntegerList("customItem."+name+".slot"));
        }

        Bukkit.getConsoleSender().sendMessage(Config.plugin+"加载Menu.yml成功！");

    }



}
