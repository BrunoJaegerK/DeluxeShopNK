/*    */ package net.lldv.llamashop.components.language;
/*    */ 
/*    */ import cn.nukkit.utils.Config;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import net.lldv.llamashop.LlamaShop;
/*    */ 
/*    */ 
/*    */ public class Language
/*    */ {
/* 11 */   public static HashMap<String, String> messages = new HashMap<>();
/*    */   public static String prefix;
/*    */   
/*    */   public static void init(LlamaShop instance) {
/* 15 */     messages.clear();
/* 16 */     instance.saveResource("messages.yml");
/* 17 */     Config m = new Config(instance.getDataFolder() + "/messages.yml");
/* 18 */     for (Map.Entry<String, Object> map : (Iterable<Map.Entry<String, Object>>)m.getAll().entrySet()) {
/* 19 */       String key = map.getKey();
/* 20 */       if (map.getValue() instanceof String) {
/* 21 */         String val = (String)map.getValue();
/* 22 */         messages.put(key, val);
/*    */       } 
/*    */     } 
/* 25 */     prefix = m.getString("prefix");
/*    */   }
/*    */   
/*    */   public static String get(String key, Object... replacements) {
/* 29 */     String message = prefix.replace("&", "§") + ((String)messages.getOrDefault(key, "null")).replace("&", "§");
/*    */     
/* 31 */     int i = 0;
/* 32 */     for (Object replacement : replacements) {
/* 33 */       message = message.replace("[" + i + "]", String.valueOf(replacement));
/* 34 */       i++;
/*    */     } 
/*    */     
/* 37 */     return message;
/*    */   }
/*    */   
/*    */   public static String getNP(String key, Object... replacements) {
/* 41 */     String message = ((String)messages.getOrDefault(key, "null")).replace("&", "§");
/*    */     
/* 43 */     int i = 0;
/* 44 */     for (Object replacement : replacements) {
/* 45 */       message = message.replace("[" + i + "]", String.valueOf(replacement));
/* 46 */       i++;
/*    */     } 
/*    */     
/* 49 */     return message;
/*    */   }
/*    */ }
