/*    */ package net.lldv.llamashop.components.forms.custom;
/*    */ 
/*    */ import cn.nukkit.Player;
/*    */ import cn.nukkit.Server;
/*    */ import cn.nukkit.form.element.Element;
/*    */ import cn.nukkit.form.response.FormResponseCustom;
/*    */ import cn.nukkit.form.window.FormWindow;
/*    */ import cn.nukkit.form.window.FormWindowCustom;
/*    */ import java.util.ArrayList;
/*    */ import java.util.function.BiConsumer;
/*    */ import java.util.function.Consumer;
/*    */ import net.lldv.llamashop.components.forms.FormHandler;
/*    */ 
/*    */ 
/*    */ public class CustomForm
/*    */ {
/*    */   private final String title;
/*    */   private final ArrayList<Element> elements;
/*    */   private final Consumer<Player> closeCallback;
/*    */   private final BiConsumer<Player, FormResponseCustom> submitCallback;
/*    */   
/*    */   public CustomForm(Builder b) {
/* 23 */     this.title = b.title;
/* 24 */     this.elements = b.elements;
/* 25 */     this.closeCallback = b.closeCallback;
/* 26 */     this.submitCallback = b.submitCallback;
/*    */   }
/*    */ 
/*    */   
/*    */   public void send(Player player) {
/* 31 */     FormWindowCustom form = new FormWindowCustom(this.title);
/* 32 */     this.elements.forEach(form::addElement);
/*    */     
/* 34 */     FormHandler.customPending.put(player.getName(), this);
/* 35 */     player.showFormWindow((FormWindow)form);
/*    */     
/* 37 */     Server.getInstance().getScheduler().scheduleDelayedTask(() -> player.sendExperience(player.getExperience()), 20);
/*    */   }
/*    */   
/*    */   public void setClosed(Player player) {
/* 41 */     if (this.closeCallback == null)
/* 42 */       return;  this.closeCallback.accept(player);
/*    */   }
/*    */   
/*    */   public void setSubmitted(Player player, FormResponseCustom form) {
/* 46 */     this.submitCallback.accept(player, form);
/*    */   }
/*    */   
/*    */   public static class Builder
/*    */   {
/*    */     private final String title;
/* 52 */     private ArrayList<Element> elements = new ArrayList<>();
/*    */     private Consumer<Player> closeCallback;
/*    */     private BiConsumer<Player, FormResponseCustom> submitCallback;
/*    */     
/*    */     public Builder(String title) {
/* 57 */       this.title = title;
/*    */     }
/*    */     
/*    */     public Builder addElement(Element element) {
/* 61 */       this.elements.add(element);
/* 62 */       return this;
/*    */     }
/*    */     
/*    */     public Builder onClose(Consumer<Player> cb) {
/* 66 */       this.closeCallback = cb;
/* 67 */       return this;
/*    */     }
/*    */     
/*    */     public Builder onSubmit(BiConsumer<Player, FormResponseCustom> cb) {
/* 71 */       this.submitCallback = cb;
/* 72 */       return this;
/*    */     }
/*    */ 
/*    */     
/*    */     public CustomForm build() {
/* 77 */       return new CustomForm(this);
/*    */     }
/*    */   }
/*    */ }
