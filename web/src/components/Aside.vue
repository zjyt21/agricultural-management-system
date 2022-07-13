<template>
  <el-menu :default-openeds="['1']" style="min-height: 100%; overflow-x:hidden" 
    background-color="rgb(48, 65, 86)"
    text-color="#fff"
    active-text-color="#ffd04b"
    :collapse-transition="false"
    :collapse="isCollapse"
    router>
    <div style="height: 60px; text-align: center">
      <img src="../assets/logo.png"  height="100%" alt="">
    </div>
    <div style="height: 20px; text-align: center; color:chartreuse;" v-show="!isCollapse">
      <b>Farmer Centre</b>
    </div>
    <el-menu-item index="/">
      <i class="el-icon-house"></i>
      <template slot="title">
        <span>Home</span>
      </template>
    </el-menu-item>
    <el-submenu index="1">
      <template slot="title">
        <i class="el-icon-coin"></i>
        <span>Data Records</span>
      </template>
      <el-menu-item index="1-1">Agricultural Behavior</el-menu-item>
      <el-menu-item index="1-2">Soil and Weather</el-menu-item>
      <el-menu-item index="1-3">History Harvest</el-menu-item>
    </el-submenu>
    <el-menu-item index="/market">
      <i class="el-icon-s-marketing"></i>
      <template slot="title">
        <span>Market Details</span>
      </template>
    </el-menu-item>
    <el-submenu index="3">
      <template slot="title">
        <i class="el-icon-s-data"></i>
        <span>Data Analysis</span>
      </template>
      <el-menu-item index="3-1">Soil Assessment Model</el-menu-item>
      <el-menu-item index="3-2">Yield Estimation Model</el-menu-item>
    </el-submenu>
    <el-submenu index="4">
      <template slot="title">
        <i class="el-icon-s-custom"></i>
        <span>Staff</span>
      </template>
      <el-menu-item index="4-1">Authority Management</el-menu-item>
      <el-menu-item index="4-2">Operation Record</el-menu-item>
    </el-submenu>
  </el-menu>
</template>

<script>

  export default {
    name:"Aside",
    
    props:{
        isCollapse:Boolean
    }
  }
</script>

<style>

</style>