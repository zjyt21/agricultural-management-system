<template>
  <el-container style="min-height: 100vh">
    <el-aside :width="sideWidth + 'px'" style="background-color: rgb(48, 65, 86); box-shadow: 2px 0 6px rgb(0 21 21 / 35%);">
      <Aside :isCollapse="isCollapse" />
    </el-aside>
    
    <el-container>
      <el-header style="border-bottom: 1px solid #ccc; box-shadow: 0px 1px 3px #ccc; ">
        <Header :collapseBtnClass="collapseBtnClass" :collapse="collapse" :user="user" />
      </el-header>
      
      <el-main>
        <router-view @refreshUser="getUser" />
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
        sideWidth: 250,
        user: {},
      }
    },
    created() {
      // Get the latest User data from the background
      this.getUser()
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
      getUser() {
        let username = localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")).username : ""
        if (username) {
          // Get User data from background
          this.request.get("/user/userInfo").then(res => {
            // Reassign the latest User data in the background
            console.log(res)
            this.user = res.data
          })
        }
      },
    }
  }
</script>
