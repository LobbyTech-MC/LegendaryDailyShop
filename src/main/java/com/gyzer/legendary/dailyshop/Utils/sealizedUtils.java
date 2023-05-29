package com.gyzer.legendary.dailyshop.Utils;

import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;

public class sealizedUtils {
    public static String ItemToString(ItemStack i)
    {
        try (ByteArrayOutputStream outputStream=new ByteArrayOutputStream();
             BukkitObjectOutputStream dataOutput=new BukkitObjectOutputStream(outputStream)){
            dataOutput.writeObject(i);
            return Base64.getEncoder().encodeToString(outputStream.toByteArray());
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static ItemStack StringToItem(String arg)
    {
        byte[] bytes=Base64.getDecoder().decode(arg);
        try (ByteArrayInputStream byteArrayInputStream=new ByteArrayInputStream(bytes);
             BukkitObjectInputStream bukkitObjectInputStream=new BukkitObjectInputStream(byteArrayInputStream)){
            return (ItemStack) bukkitObjectInputStream.readObject();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
