package com.gyzer.legendary.dailyshop.cmd;

import com.gyzer.legendary.dailyshop.Database.Config;
import com.gyzer.legendary.dailyshop.Database.MenuRead;
import com.gyzer.legendary.dailyshop.Database.PlayerData;
import com.gyzer.legendary.dailyshop.Menus.DeleteMenu;
import com.gyzer.legendary.dailyshop.Menus.ShopMenu;
import com.gyzer.legendary.dailyshop.Utils.MsgUtils;
import com.gyzer.legendary.dailyshop.Utils.ShopUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import javax.crypto.MacSpi;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class command implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand( CommandSender sender,  Command command,  String label,  String[] args) {
        if (args.length==0)
        {


            if (sender.isOp())
            {
                sender.sendMessage(Config.plugin+MsgUtils.msg("- &e&l指令帮助"));
                sender.sendMessage(MsgUtils.msg("&f &f - &3/lshop open &#E0FFFF打开商店 &8&o* legendarydailyshop.open"));
                sender.sendMessage(MsgUtils.msg("&f &f - &3/lshop admin &#E0FFFF管理员指令"));
            }
            return true;
        }
        else if (args.length==1)
        {

            if (args[0].equalsIgnoreCase("admin") && sender.isOp())
            {
                sender.sendMessage(Config.plugin+MsgUtils.msg("- &e&l指令帮助"));
                sender.sendMessage(MsgUtils.msg("&f &f - &3/lshop admin reload &#E0FFFF重载插件"));
                sender.sendMessage(MsgUtils.msg("&f &f - &3/lshop admin random &#E0FFFF强制随机今日商店"));
                sender.sendMessage(MsgUtils.msg("&f &f - &3/lshop admin add 稀有等级 出售/收购 vault/points 最低价格;最高价格 限售/购数量(-1为无限) &#E0FFFF将手上的物品增加到随机池商品"));
                sender.sendMessage(MsgUtils.msg("&f &f - &3/lshop admin remove (common/rare/epic/legendary) &#E0FFFF打开商品删除GUI"));
            }
            else if (args[0].equals("open") && sender.hasPermission("legendarydailyshop.open"))
            {
                Player p=(Player)sender;
                ShopMenu menu=new ShopMenu(p);
                menu.load();
                p.openInventory(menu.getInventory());
                return true;
            }
            else {
                sender.sendMessage(Config.plugin+Config.lang_nopermission);
                return true;
            }
        }

        else if (args.length==2)
        {

            if (args[0].equals("admin") && sender.isOp() && args[1].equals("reload"))
            {
                long time=System.currentTimeMillis();
                Config.load();
                MenuRead.load();
                sender.sendMessage(Config.plugin+MsgUtils.msg("重载插件成功！耗时 &a"+(System.currentTimeMillis()-time)+"ms"));
                return true;
            }
             if (args[0].equals("admin") && sender.isOp() && args[1].equals("random"))
            {
                ShopUtils.randomTodayShop();
                sender.sendMessage(Config.plugin+"成功随机今日商店商品，可在后台查看具体信息。");
                return true;
            }


        }
        else if (args.length==3)
        {

            if (args[0].equalsIgnoreCase("admin") && args[1].equalsIgnoreCase("remove") && sender.isOp()) {
                if (sender instanceof Player) {
                    Player p = (Player) sender;
                    if (!args[2].equals("common") && !args[2].equals("rare") && !args[2].equals("epic") && !args[2].equals("legendary"))
                    {
                        sender.sendMessage(Config.plugin+MsgUtils.msg("&#FF3030物品稀有等级输入错误 &7&o(common/rare/epic/legendary)"));
                        return true;
                    }
                    DeleteMenu menu=new DeleteMenu(args[2],1);
                    menu.loadMenu();
                    p.openInventory(menu.getInventory());
                    return  true;
                }
                sender.sendMessage(Config.plugin+"该指令只能由玩家发起");
                return true;
            }
        }
        else if (args.length==7)
        {
            if (args[0].equalsIgnoreCase("admin") && args[1].equalsIgnoreCase("add") && sender.isOp())
            {
                if (sender instanceof Player)
                {
                    Player p=(Player) sender;
                    if(p.getInventory().getItemInMainHand() == null)
                    {
                        sender.sendMessage(Config.plugin+MsgUtils.msg("&#FF3030请手持需要上架的物品"));
                        return true;
                    }
                    if (!args[2].equals("common") && !args[2].equals("rare") && !args[2].equals("epic") && !args[2].equals("legendary"))
                    {
                        sender.sendMessage(Config.plugin+MsgUtils.msg("&#FF3030物品稀有等级输入错误 &7&o(common/rare/epic/legendary)"));
                        return true;
                    }
                    if (!args[3].equals("出售") && !args[3].equals("收购"))
                    {
                        sender.sendMessage(Config.plugin+MsgUtils.msg("&#FF3030商品类型出错 &7&o(出售/收购)"));
                        return true;
                    }
                    if (!args[4].equals("vault") && !args[4].equals("points"))
                    {
                        sender.sendMessage(Config.plugin+MsgUtils.msg("&#FF3030商品货币类型出错 &7&o(vault/points)"));
                        return true;
                    }
                    if (!args[5].contains(";"))
                    {
                        sender.sendMessage(Config.plugin+MsgUtils.msg("&#FF3030价格格式为 &a最低价格;最高价格 &7例如: 5.0;10.0 或者希望价格固定 则 5.0;5.0"));
                        return true;
                    }
                    String[] prices=args[5].split(";");
                    if (!isNumeric(prices[0]) || !isNumeric(prices[1]))
                    {
                        sender.sendMessage(Config.plugin+MsgUtils.msg("&#FF3030请输入正确的价格"));
                        return true;
                    }
                    if (!isMath(args[6].replace("-","")))
                    {
                        sender.sendMessage(Config.plugin+MsgUtils.msg("&#FF3030请输入正确的限售/限购数量"));
                        return true;
                    }
                    ShopUtils.addShop(p.getInventory().getItemInMainHand(),args[2],args[3],args[4],Double.parseDouble(prices[0]),Double.parseDouble(prices[1]),Integer.parseInt(args[6]));
                    sender.sendMessage(Config.plugin+"成功添加手上物品到随机奖池内.");
                    return true;
                }
                else {
                    sender.sendMessage(Config.plugin+"该指令只能由玩家发起");
                    return true;
                }
            }
        }
        return true;
    }
    public static boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]+[.]{0,1}[0-9]*[dD]{0,1}");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }

    public static boolean isMath(String s) {
        for (int i = 0; i < s.length(); i++) {
            if (!Character.isDigit(s.charAt(i)))
                return false;
        }
        return true;
    }
    private List<String> filter(List<String> list, String latest) {
        if (list.isEmpty() || latest == null)
            return list;
        String list2 = latest.toLowerCase();
        list.removeIf(k -> !k.toLowerCase().startsWith(list2));
        return list;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        String latest = null;
        List<String> list = new ArrayList<>();
        if (args.length != 0)
            latest = args[args.length - 1];
        if (args.length == 1) {
            list.add("open");
            list.add("admin");
        } else if (args.length == 2) {
            if (args[0].equals("admin") ) {
                String playerName = args[1].toLowerCase();
                list.add("reload");
                list.add("random");
                list.add("add");
                list.add("remove");
            }
        }
        else if (args.length == 3) {
            if (args[0].equalsIgnoreCase("admin") && args[1].equalsIgnoreCase("add")) {
                list.add("common");
                list.add("rare");
                list.add("epic");
                list.add("legendary");
            } else if (args[0].equalsIgnoreCase("admin") && args[1].equalsIgnoreCase("remove")) {
                list.add("common");
                list.add("rare");
                list.add("epic");
                list.add("legendary");
            }
        }
        else if (args.length == 4) {
            if (args[0].equalsIgnoreCase("admin") && args[1].equalsIgnoreCase("add")) {
                list.add("出售");
                list.add("收购");
            }
        } else if (args.length == 5) {
            if (args[0].equalsIgnoreCase("admin") && args[1].equalsIgnoreCase("add")) {
                list.add("vault");
                list.add("points");
            }
        }
        else if (args.length == 6) {
            if (args[0].equalsIgnoreCase("admin") && args[1].equalsIgnoreCase("add")) {
                 list.add("最低价格;最高价格");
            }
        } else if (args.length == 7 ){
            if (args[0].equalsIgnoreCase("admin") && args[1].equalsIgnoreCase("add")) {
                list.add("限售/购数量(-1为无限)");
            }
        }
        return filter(list, latest);
    }
}
