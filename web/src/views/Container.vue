<template>
  <el-container style="min-height: 100vh">
    <el-aside :width="sideWidth + 'px'" style="background-color: rgb(48, 65, 86); box-shadow: 2px 0 6px rgb(0 21 21 / 35%);">
      <Aside :isCollapse="isCollapse" />
    </el-aside>
    
    <el-container>
      <el-header style="border-bottom: 1px solid #ccc; box-shadow: 0px 1px 3px #ccc; ">
        <Header :collapseBtnClass="collapseBtnClass" :collapse="collapse" />
      </el-header>
      
      <el-main>
        <router-view></router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
  import Aside from "@/components/Aside";
  import Header from "@/components/Header";
  export default {
    name: 'Container',
    components: {
      Aside,
      Header
    },
    data() {
      return {
        collapseBtnClass:'el-icon-s-fold',
        isCollapse: false,
        sideWidth: 250
      }
    },
    methods:{
      collapse(){
        this.isCollapse = !this.isCollapse
        if(this.isCollapse){
          this.sideWidth = 64
          this.collapseBtnClass = 'el-icon-s-unfold'
        }else{
          this.sideWidth = 250
          this.collapseBtnClass = 'el-icon-s-fold'
        }
      },
    }
  }
</script>
