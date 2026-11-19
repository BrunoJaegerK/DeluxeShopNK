/*    */ package net.lldv.llamashop.components.forms;
/*    */ 
/*    */ import cn.nukkit.Player;
/*    */ import cn.nukkit.form.element.ElementButton;
/*    */ import cn.nukkit.form.window.FormWindowCustom;
/*    */ import cn.nukkit.form.window.FormWindowModal;
/*    */ import cn.nukkit.form.window.FormWindowSimple;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import java.util.function.Consumer;
/*    */ import net.lldv.llamashop.components.forms.custom.CustomForm;
/*    */ import net.lldv.llamashop.components.forms.modal.ModalForm;
/*    */ import net.lldv.llamashop.components.forms.simple.SimpleForm;
/*    */ 
/*    */ 
/*    */ public class FormHandler
/*    */ {
/* 18 */   public static HashMap<String, SimpleForm> simplePending = new HashMap<>();
/* 19 */   public static HashMap<String, ModalForm> modalPending = new HashMap<>();
/* 20 */   public static HashMap<String, CustomForm> customPending = new HashMap<>();
/*    */   
/*    */   public static void handleSimple(Player player, FormWindowSimple form) {
/* 23 */     if (simplePending.containsKey(player.getName())) {
/* 24 */       SimpleForm sform = simplePending.get(player.getName());
/* 25 */       simplePending.remove(player.getName());
/*    */       
/* 27 */       if (form.getResponse() == null) {
/* 28 */         sform.setClosed(player);
/*    */         
/*    */         return;
/*    */       } 
/* 32 */       ElementButton clickedButton = form.getResponse().getClickedButton();
/*    */       
/* 34 */       for (Map.Entry<ElementButton, Consumer<Player>> map : (Iterable<Map.Entry<ElementButton, Consumer<Player>>>)sform.getButtons().entrySet()) {
/* 35 */         if (((ElementButton)map.getKey()).getText().equalsIgnoreCase(clickedButton.getText())) {
/* 36 */           if (map.getValue() != null) ((Consumer<Player>)map.getValue()).accept(player);
/*    */           
/*    */           break;
/*    */         } 
/*    */       } 
/* 41 */       sform.setSubmitted(player, form.getResponse());
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public static void handleModal(Player player, FormWindowModal form) {
/* 47 */     if (modalPending.containsKey(player.getName())) {
/* 48 */       ModalForm mform = modalPending.get(player.getName());
/* 49 */       modalPending.remove(player.getName());
/*    */       
/* 51 */       if (form.getResponse() == null) {
/* 52 */         mform.setClosed(player);
/*    */         
/*    */         return;
/*    */       } 
/* 56 */       String clickedButton = form.getResponse().getClickedButtonText();
/* 57 */       if (clickedButton.equalsIgnoreCase(mform.getYes())) mform.setYes(player); 
/* 58 */       if (clickedButton.equalsIgnoreCase(mform.getNo())) mform.setNo(player);
/*    */       
/* 60 */       mform.setSubmitted(player, form.getResponse());
/*    */     } 
/*    */   }
/*    */   
/*    */   public static void handleCustom(Player player, FormWindowCustom form) {
/* 65 */     if (customPending.containsKey(player.getName())) {
/* 66 */       CustomForm cform = customPending.get(player.getName());
/* 67 */       customPending.remove(player.getName());
/*    */       
/* 69 */       if (form.getResponse() == null) {
/* 70 */         cform.setClosed(player);
/*    */         
/*    */         return;
/*    */       } 
/* 74 */       cform.setSubmitted(player, form.getResponse());
/*    */     } 
/*    */   }
/*    */ }
