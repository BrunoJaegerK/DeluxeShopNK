package com.brunojaeger.deluxeshop.forms;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerFormRespondedEvent;
import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.element.ElementInput;
import cn.nukkit.form.window.FormWindow;
import cn.nukkit.form.window.FormWindowCustom;
import cn.nukkit.form.window.FormWindowSimple;
import cn.nukkit.item.Item;
import com.brunojaeger.deluxeshop.DeluxeShopNK;
import com.brunojaeger.deluxeshop.ShopItem;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

public final class ShopForms implements Listener {
    private final DeluxeShopNK plugin;
    private final Map<FormWindow,Action> callbacks=new IdentityHashMap<FormWindow,Action>();
    private final Map<FormWindow,ShopItem> purchaseForms=new IdentityHashMap<FormWindow,ShopItem>();
    private interface Action { void select(Player player,int index); }
    public ShopForms(DeluxeShopNK plugin){this.plugin=plugin;}

    public void openShop(Player player) {
        FormWindowSimple form=new FormWindowSimple(plugin.getConfig().getString("ui.title","Shop"),plugin.getConfig().getString("ui.main-content","Choose a category."));
        final List<String> categories=new ArrayList<String>(plugin.getCategories().keySet());
        for(String category:categories) form.addButton(new ElementButton(category));
        callbacks.put(form,(p,index)->{if(index>=0&&index<categories.size())openCategory(p,categories.get(index));});
        send(player,form);
    }

    private void openCategory(Player player,String category) {
        final List<ShopItem> items=plugin.getCategories().get(category);
        if(items==null){openShop(player);return;}
        FormWindowSimple form=new FormWindowSimple(category,plugin.getConfig().getString("ui.category-content","Select an item."));
        for(ShopItem item:items) {
            String text=plugin.getConfig().getString("ui.item-button","§b{item} §7- §a{price} {currency}")
                .replace("{item}",item.getName()).replace("{price}",format(item.getPrice())).replace("{currency}",item.getCurrency());
            form.addButton(new ElementButton(text));
        }
        form.addButton(new ElementButton(plugin.getConfig().getString("ui.back-button","§cBack")));
        callbacks.put(form,(p,index)->{if(index==items.size())openShop(p);else if(index>=0&&index<items.size())openPurchase(p,items.get(index));});
        send(player,form);
    }

    private void openPurchase(Player player,ShopItem item) {
        FormWindowCustom form=new FormWindowCustom(plugin.getConfig().getString("ui.purchase-title","Purchase {item}").replace("{item}",item.getName()));
        String prompt=plugin.getConfig().getString("ui.purchase-prompt","Price: {price} {currency}")
            .replace("{price}",format(item.getPrice())).replace("{currency}",item.getCurrency());
        form.addElement(new ElementInput(prompt,plugin.getConfig().getString("ui.amount-placeholder","Amount"),"1"));
        purchaseForms.put(form,item);
        send(player,form);
    }

    @EventHandler public void onResponse(PlayerFormRespondedEvent event) {
        FormWindow window=event.getWindow();Player player=event.getPlayer();
        Action action=callbacks.remove(window);
        if(action!=null) {
            if(window instanceof FormWindowSimple&&((FormWindowSimple)window).getResponse()!=null)
                action.select(player,((FormWindowSimple)window).getResponse().getClickedButtonId());
            return;
        }
        ShopItem item=purchaseForms.remove(window);
        if(item!=null&&window instanceof FormWindowCustom&&((FormWindowCustom)window).getResponse()!=null)
            buy(player,item,((FormWindowCustom)window).getResponse().getInputResponse(0));
    }

    private void buy(Player player,ShopItem shopItem,String raw) {
        int amount;
        try{amount=Integer.parseInt(raw==null?"":raw.trim());}catch(Exception ex){player.sendMessage(msg("invalid-amount"));return;}
        if(amount<=0||amount>plugin.getMaxPurchaseAmount()){player.sendMessage(msg("invalid-amount"));return;}
        Item stack=shopItem.createItem(amount);
        if(!player.getInventory().canAddItem(stack)){player.sendMessage(msg("inventory-full"));return;}
        BigDecimal total=BigDecimal.valueOf(shopItem.getPrice()).multiply(BigDecimal.valueOf(amount));
        try {
            total=plugin.getEconomy().normalizeAmount(shopItem.getCurrency(),total);
            if(plugin.getEconomy().balance(player,shopItem.getCurrency()).compareTo(total)<0){player.sendMessage(msg("no-money"));return;}
            if(!plugin.getEconomy().withdraw(player,shopItem.getCurrency(),total)){player.sendMessage(msg("transaction-failed"));return;}
            player.getInventory().addItem(stack);
            plugin.callPurchaseEvent(player,shopItem,amount,total.doubleValue());
            player.sendMessage(msg("item-bought").replace("{item}",shopItem.getName()).replace("{amount}",String.valueOf(amount))
                .replace("{price}",total.setScale(2,RoundingMode.HALF_UP).toPlainString()).replace("{currency}",shopItem.getCurrency()));
        } catch(Exception ex) {
            plugin.getLogger().warning("Purchase failed for "+player.getName()+": "+ex.getMessage());
            player.sendMessage(msg("economy-unavailable"));
        }
    }
    private String msg(String key){return plugin.getConfig().getString("messages."+key,"§c"+key).replace("&","§");}
    private String format(double n){return BigDecimal.valueOf(n).stripTrailingZeros().toPlainString();}
    private void send(Player p,FormWindow form){
        p.showFormWindow(form);
        plugin.getServer().getScheduler().scheduleDelayedTask(plugin,()->p.sendExperience(p.getExperience()),1);
    }
}