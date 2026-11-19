package com.brunojaeger.deluxeshop;

import cn.nukkit.Player;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.scheduler.AsyncTask;
import com.brunojaeger.deluxeshop.command.ShopCommand;
import com.brunojaeger.deluxeshop.economy.EconomyBridge;
import com.brunojaeger.deluxeshop.forms.ShopForms;
import com.brunojaeger.deluxeshop.events.ShopPurchaseEvent;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class DeluxeShopNK extends PluginBase {
    private final Map<String, List<ShopItem>> categories = new LinkedHashMap<String, List<ShopItem>>();
    private EconomyBridge economy;
    private ShopForms forms;

    @Override public void onEnable() {
        saveDefaultConfig();
        reloadShop();
        economy = new EconomyBridge(this);
        forms = new ShopForms(this);
        getServer().getPluginManager().registerEvents(forms, this);
        getServer().getCommandMap().register("deluxeshop", new ShopCommand(this, getConfig().getString("Commands.Shop.Name", "shop")));
        getLogger().info("DeluxeShopNK enabled: " + categories.size() + " categories.");
    }

    public void reloadShop() {
        categories.clear();
        for (String category : getConfig().getSection("ShopForm").getKeys(false)) {
            java.util.ArrayList<ShopItem> entries = new java.util.ArrayList<ShopItem>();
            for (String raw : getConfig().getStringList("ShopForm." + category)) {
                ShopItem item = ShopItem.parse(category, raw);
                if (item != null) {
                    entries.add(item);
                    validateImageAsync(item);
                } else {
                    getLogger().warning("Skipping invalid entry in " + category + ": " + raw);
                }
            }
            categories.put(category, entries);
        }
    }

    private void validateImageAsync(final ShopItem item) {
        if (item.getImageUrl() == null || item.getImageUrl().isEmpty()) return;
        getServer().getScheduler().scheduleAsyncTask(this, new AsyncTask() {
            @Override
            public void onRun() {
                String problem = item.validateImageEndpoint();
                if (problem != null) {
                    getLogger().warning("Image URL for shop item '" + item.getName() + "' in category '"
                            + item.getCategory() + "' may not work in Bedrock Forms: " + problem
                            + ". URL: " + item.getImageUrl());
                }
            }
        });
    }

    public Map<String, List<ShopItem>> getCategories() { return categories; }
    public EconomyBridge getEconomy() { return economy; }
    public ShopForms getForms() { return forms; }
    public int getMaxPurchaseAmount() { return Math.max(1, getConfig().getInt("max-purchase-amount", 2304)); }
    public void callPurchaseEvent(Player player, ShopItem item, int amount, double total) {
        getServer().getPluginManager().callEvent(new ShopPurchaseEvent(player, item, amount, total));
    }
}
