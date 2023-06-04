package com.gyzer.legendary.dailyshop.Database;

import com.gyzer.legendary.dailyshop.LegendaryDailyShop;
import com.gyzer.legendary.dailyshop.Utils.MsgUtils;
import org.bukkit.configuration.file.FileConfiguration;

public class Config {


    public static String plugin;

    public static String lang_nopermission;
    public static String lang_buy_Vault;
    public static String lang_buy_PlayerPoints;
    public static String lang_cant_noItem;
    public static String lang_cant_noVault;
    public static String lang_cant_PlayerPoints;
    public static String lang_cant_limit;
    public static String lang_sell_Vault;
    public static String lang_sell_PlayerPoints;
    public static String tips_common;
    public static String tips_rare;
    public static String tips_epic;
    public static String tips_legendary;
    public static boolean tips_common_enable;
    public static boolean tips_rare_enable;
    public static boolean tips_epic_enable;
    public static boolean tips_legendary_enable;
    public static double chance_common;
    public static double chance_rare;
    public static double chance_epic;
    public static double chance_legendary;
    public static int max_common;
    public static int max_rare;
    public static int max_epic;
    public static int max_legendary;
    public static void load()
    {
        FileConfiguration yml= LegendaryDailyShop.cmg;

        plugin= MsgUtils.msg(yml.getString("lang.plugin"));
        lang_nopermission = MsgUtils.msg(yml.getString("lang.nopermission"));
        lang_buy_Vault = MsgUtils.msg(yml.getString("lang.buy_Vault"));
        lang_sell_Vault = MsgUtils.msg(yml.getString("lang.sell_Vault"));
        lang_sell_PlayerPoints = MsgUtils.msg(yml.getString("lang.sell_PlayerPoints"));
        lang_buy_PlayerPoints = MsgUtils.msg(yml.getString("lang.buy_PlayerPoints"));
        lang_cant_PlayerPoints = MsgUtils.msg(yml.getString("lang.cant_noPlayerPoints"));
        lang_cant_limit = MsgUtils.msg(yml.getString("lang.cant_limit"));
        lang_cant_noItem = MsgUtils.msg(yml.getString("lang.cant_noItem"));
        lang_cant_noVault = MsgUtils.msg(yml.getString("lang.cant_noVault"));
        tips_common = MsgUtils.msg(yml.getString("set.tips.common.msg"));
        tips_rare = MsgUtils.msg(yml.getString("set.tips.rare.msg"));
        tips_epic = MsgUtils.msg(yml.getString("set.tips.epic.msg"));
        tips_legendary = MsgUtils.msg(yml.getString("set.tips.legendary.msg"));
        tips_common_enable = yml.getBoolean("set.tips.common.enable");
        tips_rare_enable = yml.getBoolean("set.tips.rare.enable");
        tips_epic_enable = yml.getBoolean("set.tips.epic.enable");
        tips_legendary_enable = yml.getBoolean("set.tips.legendary.enable");
        chance_common = yml.getDouble("set.rarity.common.chance");
        chance_rare = yml.getDouble("set.rarity.rare.chance");
        chance_epic = yml.getDouble("set.rarity.epic.chance");
        chance_legendary = yml.getDouble("set.rarity.legendary.chance");
        max_common = yml.getInt("set.rarity.common.max");
        max_rare = yml.getInt("set.rarity.rare.max");
        max_epic = yml.getInt("set.rarity.epic.max");
        max_legendary = yml.getInt("set.rarity.legendary.max");

    }
}
