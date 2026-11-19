package com.brunojaeger.deluxeshop.events;

import cn.nukkit.Player;
import cn.nukkit.event.Event;
import cn.nukkit.event.HandlerList;
import com.brunojaeger.deluxeshop.ShopItem;

public final class ShopPurchaseEvent extends Event {
    private static final HandlerList HANDLERS=new HandlerList();
    private final Player player;
    private final ShopItem item;
    private final int amount;
    private final double total;
    public ShopPurchaseEvent(Player player,ShopItem item,int amount,double total){this.player=player;this.item=item;this.amount=amount;this.total=total;}
    public Player getPlayer(){return player;} public ShopItem getShopItem(){return item;}
    public String getItemName(){return item.getName();} public int getItemId(){return item.getId();}
    public int getItemMeta(){return item.getMeta();} public int getAmount(){return amount;}
    public double getTotalPrice(){return total;} public String getCurrency(){return item.getCurrency();}
    public static HandlerList getHandlers(){return HANDLERS;}
}