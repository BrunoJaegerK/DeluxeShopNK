/*    */ package net.lldv.llamashop.components.event;
/*    */ 
/*    */ public class ShopSellItemEvent extends Event {
/*    */   private final Player player;
/*    */   private final String name;
/*    */   private final int item;
/*    */   
/*    */   public ShopSellItemEvent(Player player, String name, int item, int meta, int amount, double price) {
/*  9 */     this.player = player; this.name = name; this.item = item; this.meta = meta; this.amount = amount; this.price = price;
/*    */   }
/*    */   private final int meta; private final int amount; private final double price;
/*    */   public Player getPlayer() {
/* 13 */     return this.player;
/* 14 */   } public String getName() { return this.name; }
/* 15 */   public int getItem() { return this.item; }
/* 16 */   public int getMeta() { return this.meta; }
/* 17 */   public int getAmount() { return this.amount; } public double getPrice() {
/* 18 */     return this.price;
/* 19 */   } private static final HandlerList handlers = new HandlerList();
/*    */   
/*    */   public static HandlerList getHandlers() {
/* 22 */     return handlers;
/*    */   }
/*    */ }
