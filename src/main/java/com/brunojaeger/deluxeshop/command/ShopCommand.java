package com.brunojaeger.deluxeshop.command;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import com.brunojaeger.deluxeshop.DeluxeShopNK;

public final class ShopCommand extends Command {
    private final DeluxeShopNK plugin;
    public ShopCommand(DeluxeShopNK plugin,String name) {
        super(name,"Open the shop","/"+name);
        this.plugin=plugin;
        setAliases(plugin.getConfig().getStringList("Commands.Shop.Aliases").toArray(new String[0]));
    }
    @Override public boolean execute(CommandSender sender,String label,String[] args) {
        if(sender instanceof Player) plugin.getForms().openShop((Player)sender);
        else sender.sendMessage("This command can only be used in-game.");
        return true;
    }
}