/*    */ package net.lldv.llamashop.components.forms;
/*    */ import cn.nukkit.Player;
/*    */ import cn.nukkit.Server;
/*    */ import cn.nukkit.event.Event;
/*    */ import cn.nukkit.form.element.ElementButton;
/*    */ import cn.nukkit.form.response.FormResponseCustom;
/*    */ import cn.nukkit.item.Item;
/*    */ import cn.nukkit.level.Sound;
/*    */ import cn.nukkit.network.protocol.PlaySoundPacket;
/*    */ import net.lldv.llamaeconomy.LlamaEconomy;
/*    */ import net.lldv.llamashop.LlamaShop;
/*    */ import net.lldv.llamashop.components.forms.custom.CustomForm;
/*    */ import net.lldv.llamashop.components.forms.simple.SimpleForm;
/*    */ import net.lldv.llamashop.components.language.Language;
/*    */ 
/*    */ public class FormWindows {
/*    */   private final LlamaShop instance;
/*    */   
/*    */   public FormWindows(LlamaShop instance) {
/* 20 */     this.instance = instance;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void openShop(Player player) {
/* 26 */     SimpleForm.Builder form = new SimpleForm.Builder(Language.getNP("shop-title", new Object[0]), Language.getNP("shop-content", new Object[0]));
/* 27 */     this.instance.cachedCategory.keySet().forEach(category -> form.addButton(new ElementButton(category), ()));
/* 28 */     SimpleForm finalForm = form.build();
/* 29 */     finalForm.send(player);
/*    */   }
/*    */   
/*    */   public void openCategory(Player player, String category) {
/* 33 */     SimpleForm.Builder form = new SimpleForm.Builder(category, "");
/* 34 */     ((Set)this.instance.cachedCategory.get(category)).forEach(item -> {
/*    */           String[] s = item.split(":");
/*    */           form.addButton(new ElementButton(Language.getNP("category-item", new Object[] { s[0], s[3] })), ());
/*    */         });
/* 38 */     SimpleForm finalForm = form.build();
/* 39 */     finalForm.send(player);
/*    */   }
/*    */   
/*    */   public void openItemShop(Player player, String e) {
/* 43 */     String[] s = (String[])this.instance.cachedShopItem.get(e);
/* 44 */     String name = s[0];
/* 45 */     int itemID = Integer.parseInt(s[1]);
/* 46 */     int itemMeta = Integer.parseInt(s[2]);
/* 47 */     double price = Double.parseDouble(s[3]);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 64 */     CustomForm form = (new CustomForm.Builder(e)).addElement((Element)new ElementInput(Language.getNP("itembuy-info", new Object[] { name, Double.valueOf(price) }), Language.getNP("itembuy-amount", new Object[0]), "1")).onSubmit((f, r) -> { try { int amount = Integer.parseInt(r.getInputResponse(0)); if (amount <= 0) { player.sendMessage(Language.get("invalid-amount", new Object[0])); playSound(player, Sound.NOTE_BASS); return; }  sellItem(player, name, itemID, itemMeta, amount, price); } catch (NumberFormatException g) { player.sendMessage(Language.get("invalid-amount", new Object[0])); playSound(player, Sound.NOTE_BASS); }  }).build();
/* 65 */     form.send(player);
/*    */   }
/*    */   
/*    */   public void sellItem(Player player, String name, int item, int meta, int amount, double price) {
/* 69 */     CompletableFuture.runAsync(() -> {
/*    */           double newPrice = price * amount;
/*    */           if (LlamaEconomy.getAPI().getMoney(player.getName()) >= newPrice) {
/*    */             Item finalItem = Item.get(item, Integer.valueOf(meta), amount);
/*    */             if (player.getInventory().canAddItem(finalItem)) {
/*    */               player.getInventory().addItem(new Item[] { finalItem });
/*    */               LlamaEconomy.getAPI().reduceMoney(player.getName(), newPrice);
/*    */               Server.getInstance().getPluginManager().callEvent((Event)new ShopSellItemEvent(player, name, item, meta, amount, price));
/*    */               player.sendMessage(Language.get("item-bought", new Object[] { name, Integer.valueOf(amount), LlamaEconomy.getAPI().getMoneyFormat().format(newPrice) }));
/*    */               playSound(player, Sound.NOTE_HARP);
/*    */             } else {
/*    */               player.sendMessage(Language.get("inventory-full", new Object[0]));
/*    */               playSound(player, Sound.NOTE_BASS);
/*    */             } 
/*    */           } else {
/*    */             player.sendMessage(Language.get("no-money", new Object[0]));
/*    */             playSound(player, Sound.NOTE_BASS);
/*    */           } 
/*    */         });
/*    */   }
/*    */   
/*    */   private void playSound(Player player, Sound sound) {
/* 91 */     PlaySoundPacket packet = new PlaySoundPacket();
/* 92 */     packet.name = sound.getSound();
/* 93 */     packet.x = (new Double(player.getLocation().getX())).intValue();
/* 94 */     packet.y = (new Double(player.getLocation().getY())).intValue();
/* 95 */     packet.z = (new Double(player.getLocation().getZ())).intValue();
/* 96 */     packet.volume = 1.0F;
/* 97 */     packet.pitch = 1.0F;
/* 98 */     player.dataPacket((DataPacket)packet);
/*    */   }
/*    */ }
