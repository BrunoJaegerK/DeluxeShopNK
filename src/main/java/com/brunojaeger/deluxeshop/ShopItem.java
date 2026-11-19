package com.brunojaeger.deluxeshop;

import cn.nukkit.item.Item;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
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
            String[] imageParts=raw.split("\\|",2);
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

    private static boolean isValidImageUrl(String value) {
        try {
            URI uri = new URI(value);
            String scheme = uri.getScheme();
            return uri.isAbsolute() && uri.getHost() != null && uri.getUserInfo() == null
                    && ("http".equalsIgnoreCase(scheme) || "https".equalsIgnoreCase(scheme));
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Checks the final response after HTTP redirects. Returns null when the
     * endpoint responds with an image content type, otherwise a diagnostic.
     * This method performs network I/O and must only be called asynchronously.
     */
    public String validateImageEndpoint() {
        if (imageUrl == null || imageUrl.isEmpty()) return null;
        HttpURLConnection connection = null;
        try {
            connection = openImageConnection("HEAD");
            int status = connection.getResponseCode();
            String contentType = connection.getContentType();
            if (status >= 200 && status < 300 && isImageContentType(contentType)) return null;
        } catch (Exception ignored) {
            // Some hosts do not implement HEAD. Retry with GET below.
        } finally {
            if (connection != null) connection.disconnect();
        }

        connection = null;
        try {
            connection = openImageConnection("GET");
            int status = connection.getResponseCode();
            String contentType = connection.getContentType();
            if (status < 200 || status >= 300) {
                return "HTTP " + status + " (" + connection.getURL() + ")";
            }
            if (!isImageContentType(contentType)) {
                return "el servidor respondió Content-Type '" + (contentType == null ? "desconocido" : contentType)
                        + "' en lugar de image/*";
            }
            InputStream stream = connection.getInputStream();
            if (stream != null) stream.close();
            return null;
        } catch (Exception ex) {
            return "no se pudo comprobar la URL: " + ex.getClass().getSimpleName() + ": " + ex.getMessage();
        } finally {
            if (connection != null) connection.disconnect();
        }
    }

    private HttpURLConnection openImageConnection(String method) throws Exception {
        URL original = new URL(imageUrl);
        HttpURLConnection connection = (HttpURLConnection) original.openConnection();
        connection.setInstanceFollowRedirects(true);
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);
        connection.setRequestMethod(method);
        connection.setRequestProperty("User-Agent", "DeluxeShopNK/2.0");
        return connection;
    }

    private static boolean isImageContentType(String contentType) {
        return contentType != null && contentType.toLowerCase(java.util.Locale.ROOT).startsWith("image/");
    }

    public String getCategory(){return category;} public String getName(){return name;}
    public int getId(){return id;} public int getMeta(){return meta;} public double getPrice(){return price;}
    public String getCurrency(){return currency;} public String getImageUrl(){return imageUrl;} public Map<Integer,Integer> getEnchantments(){return enchantments;}
}
