package com.brunojaeger.deluxeshop.economy;

import cn.nukkit.Player;
import com.brunojaeger.deluxeshop.DeluxeShopNK;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Locale;

public final class EconomyBridge {
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
    }

    public BigDecimal balance(Player player,String currency) throws Exception {
        if(secondary(currency)) return new BigDecimal(String.valueOf(invoke(secondaryAPI(),"getBalance",player.getUniqueId(),currency)));
        if(primary==null) throw new IllegalStateException("Primary economy is not available");
        Object value="LlamaEconomy".equals(provider)?invoke(primary,"getMoney",player.getName()):invoke(primary,"myMoney",player);
        return new BigDecimal(String.valueOf(value));
    }

    public BigDecimal normalizeAmount(String currency,BigDecimal amount) throws Exception {
        if(!secondary(currency)) return amount;
        Object econ=secondaryAPI(), cur=invoke(econ,"getCurrency",currency);
        int decimals=((Number)invoke(cur,"getDecimals")).intValue();
        return amount.setScale(Math.max(0,Math.min(8,decimals)),RoundingMode.HALF_UP);
    }

    public boolean withdraw(Player player,String currency,BigDecimal amount) throws Exception {
        Object result;
        if(secondary(currency)) result=invoke(secondaryAPI(),"withdraw",player.getUniqueId(),currency,amount);
        else {
            if(primary==null) return false;
            result="LlamaEconomy".equals(provider)?invoke(primary,"reduceMoney",player.getName(),amount.doubleValue()):invoke(primary,"reduceMoney",player,amount.doubleValue());
        }
        if(result==null) return true; // LlamaEconomy API versions may return void.
        if(result instanceof Boolean) return ((Boolean)result).booleanValue();
        if(result instanceof Number) return ((Number)result).intValue()>0; // EconomyAPI RET_SUCCESS.
        return false;
    }

    private boolean secondary(String currency) { return currency!=null&&!currency.equalsIgnoreCase("default")&&!currency.equalsIgnoreCase("primary"); }
    private Object secondaryAPI() throws Exception {
        Class<?> c=Class.forName("com.bruno.economyexpand.EconomyExpandNK");
        return invoke(c.getMethod("getInstance").invoke(null),"getEconomy");
    }
    private static Object invoke(Object target,String name,Object... args) throws Exception {
        for(Method m:target.getClass().getMethods()) {
            if(!m.getName().equals(name)||m.getParameterTypes().length!=args.length) continue;
            Class<?>[] types=m.getParameterTypes(); boolean ok=true;
            for(int i=0;i<args.length;i++) if(args[i]!=null&&!box(types[i]).isAssignableFrom(args[i].getClass())) {ok=false;break;}
            if(ok) return m.invoke(target,args);
        }
        throw new NoSuchMethodException(target.getClass().getName()+"."+name);
    }
    private static Class<?> box(Class<?> c) {
        if(!c.isPrimitive()) return c;
        if(c==int.class)return Integer.class;if(c==long.class)return Long.class;if(c==double.class)return Double.class;
        if(c==float.class)return Float.class;if(c==boolean.class)return Boolean.class;if(c==short.class)return Short.class;
        if(c==byte.class)return Byte.class;if(c==char.class)return Character.class;return c;
    }
}