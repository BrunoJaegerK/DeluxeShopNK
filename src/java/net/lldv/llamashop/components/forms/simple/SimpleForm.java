/*    */ package net.lldv.llamashop.components.forms.simple;
/*    */ 
/*    */ import cn.nukkit.Player;
/*    */ import cn.nukkit.Server;
/*    */ import cn.nukkit.form.element.ElementButton;
/*    */ import cn.nukkit.form.response.FormResponseSimple;
/*    */ import cn.nukkit.form.window.FormWindow;
/*    */ import cn.nukkit.form.window.FormWindowSimple;
/*    */ import java.util.HashMap;
/*    */ import java.util.LinkedHashMap;
/*    */ import java.util.function.BiConsumer;
/*    */ import java.util.function.Consumer;
/*    */ import net.lldv.llamashop.components.forms.FormHandler;
/*    */ 
/*    */ public class SimpleForm
/*    */ {
/*    */   private final LinkedHashMap<ElementButton, Consumer<Player>> buttons;
/*    */   private final String title;
/*    */   private final String content;
/*    */   private final Consumer<Player> closeCallback;
/*    */   private final BiConsumer<Player, FormResponseSimple> submitCallback;
/*    */   
/*    */   public SimpleForm(Builder b) {
/* 24 */     this.buttons = b.buttons;
/* 25 */     this.title = b.title;
/* 26 */     this.content = b.content;
/* 27 */     this.closeCallback = b.closeCallback;
/* 28 */     this.submitCallback = b.submitCallback;
/*    */   }
/*    */   
/*    */   public void send(Player player) {
/* 32 */     FormWindowSimple form = new FormWindowSimple(this.title, this.content);
/* 33 */     this.buttons.keySet().forEach(form::addButton);
/*    */     
/* 35 */     FormHandler.simplePending.put(player.getName(), this);
/*    */     
/* 37 */     player.showFormWindow((FormWindow)form);
/* 38 */     Server.getInstance().getScheduler().scheduleDelayedTask(() -> player.sendExperience(player.getExperience()), 20);
/*    */   }
/*    */   
/*    */   public void setClosed(Player player) {
/* 42 */     if (this.closeCallback == null)
/* 43 */       return;  this.closeCallback.accept(player);
/*    */   }
/*    */   
/*    */   public void setSubmitted(Player p, FormResponseSimple r) {
/* 47 */     if (this.submitCallback == null)
/* 48 */       return;  this.submitCallback.accept(p, r);
/*    */   }
/*    */   
/*    */   public HashMap<ElementButton, Consumer<Player>> getButtons() {
/* 52 */     return this.buttons;
/*    */   }
/*    */   
/*    */   public String getTitle() {
/* 56 */     return this.title;
/*    */   }
/*    */   
/*    */   public String getContent() {
/* 60 */     return this.content;
/*    */   }
/*    */   
/*    */   public static class Builder
/*    */   {
/* 65 */     private final LinkedHashMap<ElementButton, Consumer<Player>> buttons = new LinkedHashMap<>();
/*    */     private final String title;
/*    */     private final String content;
/*    */     private Consumer<Player> closeCallback;
/*    */     private BiConsumer<Player, FormResponseSimple> submitCallback;
/*    */     
/*    */     public Builder(String title, String content) {
/* 72 */       this.title = title;
/* 73 */       this.content = content;
/*    */     }
/*    */     
/*    */     public Builder onClose(Consumer<Player> close) {
/* 77 */       this.closeCallback = close;
/* 78 */       return this;
/*    */     }
/*    */     
/*    */     public Builder addButton(ElementButton button) {
/* 82 */       this.buttons.put(button, null);
/* 83 */       return this;
/*    */     }
/*    */     
/*    */     public Builder addButton(ElementButton button, Consumer<Player> callback) {
/* 87 */       this.buttons.put(button, callback);
/* 88 */       return this;
/*    */     }
/*    */     
/*    */     public Builder onSubmit(BiConsumer<Player, FormResponseSimple> r) {
/* 92 */       this.submitCallback = r;
/* 93 */       return this;
/*    */     }
/*    */     
/*    */     public SimpleForm build() {
/* 97 */       return new SimpleForm(this);
/*    */     }
/*    */   }
/*    */ }
