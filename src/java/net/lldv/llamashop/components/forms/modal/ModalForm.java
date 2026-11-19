/*    */ package net.lldv.llamashop.components.forms.modal;
/*    */ 
/*    */ import cn.nukkit.Player;
/*    */ import cn.nukkit.form.response.FormResponseModal;
/*    */ import cn.nukkit.form.window.FormWindow;
/*    */ import cn.nukkit.form.window.FormWindowModal;
/*    */ import java.util.function.BiConsumer;
/*    */ import java.util.function.Consumer;
/*    */ import net.lldv.llamashop.components.forms.FormHandler;
/*    */ 
/*    */ public class ModalForm {
/*    */   private final String title;
/*    */   private final String content;
/*    */   private final String yes;
/*    */   private final String no;
/*    */   
/*    */   public ModalForm(Builder b) {
/* 18 */     this.title = b.title;
/* 19 */     this.content = b.content;
/* 20 */     this.yes = b.yes;
/* 21 */     this.no = b.no;
/* 22 */     this.onYes = b.onYes;
/* 23 */     this.onNo = b.onNo;
/* 24 */     this.closeCallback = b.closeCallback;
/* 25 */     this.submitCallback = b.submitCallback;
/*    */   }
/*    */   private final Consumer<Player> onYes; private final Consumer<Player> onNo; private final Consumer<Player> closeCallback; private final BiConsumer<Player, FormResponseModal> submitCallback;
/*    */   public void send(Player player) {
/* 29 */     FormWindowModal form = new FormWindowModal(this.title, this.content, this.yes, this.no);
/*    */     
/* 31 */     FormHandler.modalPending.put(player.getName(), this);
/*    */     
/* 33 */     player.showFormWindow((FormWindow)form);
/*    */   }
/*    */   
/*    */   public void setYes(Player player) {
/* 37 */     this.onYes.accept(player);
/*    */   }
/*    */   
/*    */   public void setNo(Player player) {
/* 41 */     this.onNo.accept(player);
/*    */   }
/*    */   
/*    */   public void setClosed(Player player) {
/* 45 */     if (this.closeCallback == null)
/* 46 */       return;  this.closeCallback.accept(player);
/*    */   }
/*    */   
/*    */   public void setSubmitted(Player p, FormResponseModal r) {
/* 50 */     if (this.submitCallback == null)
/* 51 */       return;  this.submitCallback.accept(p, r);
/*    */   }
/*    */   
/*    */   public String getYes() {
/* 55 */     return this.yes;
/*    */   }
/*    */   
/*    */   public String getNo() {
/* 59 */     return this.no;
/*    */   }
/*    */   public static class Builder { private final String title; private final String content; private final String yes;
/*    */     private final String no;
/*    */     private Consumer<Player> onYes;
/*    */     private Consumer<Player> onNo;
/*    */     private Consumer<Player> closeCallback;
/*    */     private BiConsumer<Player, FormResponseModal> submitCallback;
/*    */     
/*    */     public Builder(String title, String content, String yes, String no) {
/* 69 */       this.title = title;
/* 70 */       this.content = content;
/* 71 */       this.yes = yes;
/* 72 */       this.no = no;
/*    */     }
/*    */     
/*    */     public Builder onYes(Consumer<Player> cb) {
/* 76 */       this.onYes = cb;
/* 77 */       return this;
/*    */     }
/*    */     
/*    */     public Builder onNo(Consumer<Player> cb) {
/* 81 */       this.onNo = cb;
/* 82 */       return this;
/*    */     }
/*    */     
/*    */     public Builder onClose(Consumer<Player> cb) {
/* 86 */       this.closeCallback = cb;
/* 87 */       return this;
/*    */     }
/*    */     
/*    */     public Builder onSubmit(BiConsumer<Player, FormResponseModal> cb) {
/* 91 */       this.submitCallback = cb;
/* 92 */       return this;
/*    */     }
/*    */     
/*    */     public ModalForm build() {
/* 96 */       return new ModalForm(this);
/*    */     } }
/*    */ 
/*    */ }
