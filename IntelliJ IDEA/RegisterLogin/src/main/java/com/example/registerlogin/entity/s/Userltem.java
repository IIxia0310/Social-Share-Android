package com.example.registerlogin.entity.s;

import com.example.registerlogin.entity.User;

import java.io.Serializable;

public class Userltem implements Serializable {


   private User user;

   private Long gzSum;
   private Long fsSum;
   private Long likeSum;
   private Long collectSum;

   public User getUser() {
      return user;
   }

   public void setUser(User user) {
      this.user = user;
   }

   public Long getGzSum() {
      return gzSum;
   }

   public void setGzSum(Long gzSum) {
      this.gzSum = gzSum;
   }

   public Long getFsSum() {
      return fsSum;
   }

   public void setFsSum(Long fsSum) {
      this.fsSum = fsSum;
   }

   public Long getLikeSum() {
      return likeSum;
   }

   public void setLikeSum(Long likeSum) {
      this.likeSum = likeSum;
   }

   public Long getCollectSum() {
      return collectSum;
   }

   public void setCollectSum(Long collectSum) {
      this.collectSum = collectSum;
   }

   public Userltem(User user, Long gzSum, Long fsSum, Long likeSum, Long collectSum) {
      this.user = user;
      this.gzSum = gzSum;
      this.fsSum = fsSum;
      this.likeSum = likeSum;
      this.collectSum = collectSum;
   }
}