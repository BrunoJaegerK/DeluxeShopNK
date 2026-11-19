/*    */ package net.lldv.llamashop.commands;
/*    */ 
/*    */ import cn.nukkit.Player;
/*    */ import cn.nukkit.command.CommandSender;
/*    */ import cn.nukkit.command.PluginCommand;
/*    */ import cn.nukkit.plugin.Plugin;
/*    */ import net.lldv.llamashop.LlamaShop;
/*    */ 
/*    */ public class ShopCommand extends PluginCommand<LlamaShop> {
/*    */   public ShopCommand(LlamaShop owner) {
/* 11 */     super(owner.getConfig().getString("Commands.Shop.Name"), (Plugin)owner);
/* 12 */     setDescription(owner.getConfig().getString("Commands.Shop.Description"));
/* 13 */     setAliases((String[])owner.getConfig().getStringList("Commands.Shop.Aliases").toArray((Object[])new String[0]));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean execute(CommandSender sender, String commandLabel, String[] args) {
/* 18 */     if (sender instanceof Player) {
/* 19 */       Player player = (Player)sender;
/* 20 */       ((LlamaShop)getPlugin()).getFormWindows().openShop(player);
/*    */     } 
/* 22 */     return true;
/*    */   }
/*    */ }
