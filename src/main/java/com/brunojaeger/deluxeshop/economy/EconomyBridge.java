package com.brunojaeger.deluxeshop.economy;

import cn.nukkit.Player;
import com.brunojaeger.deluxeshop.DeluxeShopNK;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Locale;

public final class EconomyBridge {
    private static final String SECONDARY_API = "com.bruno.economyexpand.api.SecondaryEconomy";
    private final DeluxeShopNK plugin;
    private Object primary;
    private String provider="none";

    public EconomyBridge(DeluxeShopNK plugin) {
        this.plugin=plugin;
        String mode=plugin.getConfig().getString("economy.provider","auto").toLowerCase(Locale.ROOT);
        if("auto".equals(mode)||"llamaeconomy".equals(mode)) {
            try {
                Class<?> c=Class.forName("net.lldv.llamaeconomy.LlamaEconomy");
                primary=c.getMethod("getAPI").invoke(null); provider="LlamaEconomy";
            } catch(Exception ignored) {}
        }
        if(primary==null&&("auto".equals(mode)||"economyapi".equals(mode))) {
            try {
                Class<?> c=Class.forName("me.onebone.economyapi.EconomyAPI");
                primary=c.getMethod("getInstance").invoke(null); provider="EconomyAPI";
            } catch(Exception ignored) {}
        }
        if(primary==null) plugin.getLogger().warning("No primary economy detected. 'default' currency purchases will not work.");
        else plugin.getLogger().info("Primary economy: "+provider);
        if(isPluginEnabled("EconomyExpandNK")) plugin.getLogger().info("Secondary economy detected: EconomyExpandNK");
    }

    public BigDecimal balance(Player player,String currency) throws Exception {
        if(secondary(currency)) return new BigDecimal(String.valueOf(invokeSecondary("getBalance",player.getUniqueId(),currency)));
        if(primary==null) throw new IllegalStateException("Primary economy is not available");
        Object value="LlamaEconomy".equals(provider)?invoke(primary,"getMoney",player.getName()):invoke(primary,"myMoney",player);
        return new BigDecimal(String.valueOf(value));
    }

    public BigDecimal normalizeAmount(String currency,BigDecimal amount) throws Exception {
        if(!secondary(currency)) return amount;
        Object cur=invokeSecondary("getCurrency",currency);
        int decimals=((Number)invoke(cur,"getDecimals")).intValue();
        return amount.setScale(Math.max(0,Math.min(8,decimals)),RoundingMode.HALF_UP);
    }

    public boolean withdraw(Player player,String currency,BigDecimal amount) throws Exception {
        Object result;
        if(secondary(currency)) result=invokeSecondary("withdraw",player.getUniqueId(),currency,amount);
        else {
            if(primary==null) return false;
            result="LlamaEconomy".equals(provider)?invoke(primary,"reduceMoney",player.getName(),amount.doubleValue()):invoke(primary,"reduceMoney",player,amount.doubleValue());
        }
        if(result==null) return true;
        if(result instanceof Boolean) return ((Boolean)result).booleanValue();
        if(result instanceof Number) return ((Number)result).intValue()>0;
        return false;
    }

    private boolean secondary(String currency) {
        return currency!=null&&!currency.equalsIgnoreCase("default")&&!currency.equalsIgnoreCase("primary");
    }

    private boolean isPluginEnabled(String name) {
        try {
            return plugin.getServer().getPluginManager().getPlugin(name)!=null;
        } catch(Exception ignored) {
            return false;
        }
    }

    private Object secondaryAPI() throws Exception {
        Class<?> c=Class.forName("com.bruno.economyexpand.EconomyExpandNK");
        Object instance=c.getMethod("getInstance").invoke(null);
        return invoke(instance,"getEconomy");
    }

    /*
     * EconomyExpandNK implements SecondaryEconomy with a private inner class
     * (EconomyExpandNK$EconomyImpl). Its methods are public, but reflection on
     * that private implementation class still throws IllegalAccessException.
     * Invoke the methods through the public API interface instead.
     */
    private Object invokeSecondary(String name,Object... args) throws Exception {
        Object target=secondaryAPI();
        Class<?> api=Class.forName(SECONDARY_API);
        Method method=findCompatibleMethod(api,name,args);
        if(method==null) throw new NoSuchMethodException(api.getName()+"."+name);
        return method.invoke(target,args);
    }

    private static Object invoke(Object target,String name,Object... args) throws Exception {
        Method method=findAccessibleMethod(target.getClass(),name,args);
        if(method==null) throw new NoSuchMethodException(target.getClass().getName()+"."+name);
        return method.invoke(target,args);
    }

    private static Method findAccessibleMethod(Class<?> type,String name,Object[] args) {
        for(Class<?> api:type.getInterfaces()) {
            Method method=findCompatibleMethod(api,name,args);
            if(method!=null) return method;
        }
        for(Method method:type.getMethods()) {
            if(method.getName().equals(name)&&Modifier.isPublic(method.getDeclaringClass().getModifiers())
                    &&compatible(method.getParameterTypes(),args)) return method;
        }
        return null;
    }

    private static Method findCompatibleMethod(Class<?> type,String name,Object[] args) {
        for(Method method:type.getMethods()) {
            if(method.getName().equals(name)&&compatible(method.getParameterTypes(),args)) return method;
        }
        return null;
    }

    private static boolean compatible(Class<?>[] types,Object[] args) {
        if(types.length!=args.length) return false;
        for(int i=0;i<args.length;i++) {
            if(args[i]!=null&&!box(types[i]).isAssignableFrom(args[i].getClass())) return false;
        }
        return true;
    }

    private static Class<?> box(Class<?> c) {
        if(!c.isPrimitive()) return c;
        if(c==int.class)return Integer.class;if(c==long.class)return Long.class;if(c==double.class)return Double.class;
        if(c==float.class)return Float.class;if(c==boolean.class)return Boolean.class;if(c==short.class)return Short.class;
        if(c==byte.class)return Byte.class;if(c==char.class)return Character.class;return c;
    }
}
