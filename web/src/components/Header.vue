<template>
  <div style="font-size: 12px; line-height: 60px; display: flex">
    <div style="flex: 1; font-size: 18px">
      <span :class="collapseBtnClass" style="cursor: pointer" @click="collapse"></span>
      <el-breadcrumb separator="/" style="display: inline-block; margin-left: 10px;">
        <el-breadcrumb-item :to="'/'">Home</el-breadcrumb-item>
        <el-breadcrumb-item>{{ this.$route.meta.name }}</el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <el-dropdown style="width: 150px; cursor: pointer; text-align: right;">
      <div>
        <img :src="user.avatar" alt=""
             style="width: 30px; border-radius: 50%; position: relative; top: 10px; right: 5px">
        <span>{{ user.nickname }}</span><i class="el-icon-setting" style="margin-left: 5px"></i>
      </div>
      
      <el-dropdown-menu slot="dropdown" style="width: 130px; text-align: center;">
        <el-dropdown-item style="font-size: 14px; padding: 5px 0;" class="el-icon-user-solid">
          <router-link to="/person">Your Account</router-link>
        </el-dropdown-item>
        <br>
        <el-dropdown-item style="font-size: 14px; padding: 5px 0;" class="el-icon-right">
          <span style="text-decoration: none;" @click="logout">Sign Out</span>
        </el-dropdown-item>
      </el-dropdown-menu>
    </el-dropdown>
  </div>
</template>

<script>
  export default {
    name:"Header",

    // data() {
    //   return {
    //     user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {}
    //   }
    // },
    props:{
      collapseBtnClass:String,
      collapse:Function,
      user: Object
    },
    methods: {
      logout(){
        this.$store.commit("logout")
        this.$router.push("/login")
        localStorage.removeItem("user")
        localStorage.removeItem("token")
        localStorage.removeItem("menus")
        
        this.$message.success("Logout successfully")
      }
    },
  }
</script>

<style>

</style>