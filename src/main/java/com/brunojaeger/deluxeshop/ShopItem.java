package com.brunojaeger.deluxeshop;

import cn.nukkit.item.Item;
import java.util.LinkedHashMap;
import java.util.Map;

public final class ShopItem {
    private final String category, name, currency;
    private final int id, meta;
    private final double price;
    private final String imageUrl;
    private final Map<Integer,Integer> enchantments;

    private ShopItem(String category,String name,int id,int meta,double price,String currency,String imageUrl,Map<Integer,Integer> enchants) {
        this.category=category; this.name=name; this.id=id; this.meta=meta; this.price=price; this.currency=currency; this.imageUrl=imageUrl; this.enchantments=enchants;
    }

    public static ShopItem parse(String category,String raw) {
        try {
            String[] imageParts=raw.split("\\\\|",2);
            String core=imageParts[0].trim();
            String imageUrl=imageParts.length>1?imageParts[1].trim():"";
            if(!imageUrl.isEmpty()&&!isValidImageUrl(imageUrl)) return null;
            String[] p=core.split(":",6);
            if(p.length<4) return null;
            String name=p[0].trim(); int id=Integer.parseInt(p[1].trim()), meta=Integer.parseInt(p[2].trim());
            double price=Double.parseDouble(p[3].trim());
            if(name.isEmpty()||id<0||meta<0||price<0||!Double.isFinite(price)) return null;
            String currency=p.length>4&&!p[4].trim().isEmpty()?p[4].trim():"default";
            Map<Integer,Integer> enchants=new LinkedHashMap<Integer,Integer>();
            if(p.length>5) for(String pair:p[5].split(",")) {
                String[] e=pair.trim().split("[=|]",2);
                if(e.length==2) { int eid=Integer.parseInt(e[0].trim()), level=Integer.parseInt(e[1].trim()); if(eid>=0&&level>0) enchants.put(eid,level); }
            }
            return new ShopItem(category,name,id,meta,price,currency,imageUrl,enchants);
        } catch(Exception ex) { return null; }
    }

    public Item createItem(int amount) {
        Item result=Item.get(id,meta,amount);
        result.setCustomName(name);
        for(Map.Entry<Integer,Integer> e:enchantments.entrySet()) applyEnchant(result,e.getKey(),e.getValue());
        return result;
    }

    private static void applyEnchant(Item item,int id,int level) {
        try {
            Class<?> type=Class.forName("cn.nukkit.item.enchantment.Enchantment");
            Object enchant=null;
            // getEnchantment returns a clone; get() exposes a shared registry instance.
            try { enchant=type.getMethod("getEnchantment",int.class).invoke(null,id); }
            catch(NoSuchMethodException ignored) { enchant=type.getMethod("get",int.class).invoke(null,id); }
            if(enchant==null) return;
            type.getMethod("setLevel",int.class).invoke(enchant,level);
            Class<?> arrayType=java.lang.reflect.Array.newInstance(type,0).getClass();
            Object array=java.lang.reflect.Array.newInstance(type,1);
            java.lang.reflect.Array.set(array,0,enchant);
            item.getClass().getMethod("addEnchantment",arrayType).invoke(item,array);
        } catch(Exception ignored) { }
    }
    private static boolean isValidImageUrl(String value) {\n        try { java.net.URI uri=new java.net.URI(value); String scheme=uri.getScheme(); return uri.isAbsolute()&&uri.getHost()!=null&&(\"http\".equalsIgnoreCase(scheme)||\"https\".equalsIgnoreCase(scheme)); } catch(Exception e) { return false; }\n    }\n    public String getCategory(){return category;} public String getName(){return name;}
    public int getId(){return id;} public int getMeta(){return meta;} public double getPrice(){return price;}
    public String getCurrency(){return currency;} public String getImageUrl(){return imageUrl;} public Map<Integer,Integer> getEnchantments(){return enchantments;}
}