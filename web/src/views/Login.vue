<template>
  <div class="wrapper">
    <div style="margin: 200px auto; background-color: #fff; width: 350px; height: 330px; padding: 20px; border-radius: 10px">
      <div style="height: 50px; text-align: center">
        <img src="../assets/logo.png"  height="100%" alt="">
      </div>
      <div style="margin: 10px 0; text-align: center; font-size: 24px"><b>Smart Farm</b></div>
      <el-form :model="user" :rules="rules" ref="userForm">
        <el-form-item prop="username">
          <el-input 
            size="medium" 
            style="margin: 10px 0" 
            prefix-icon="el-icon-user" 
            v-model="user.username">
          </el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input 
            size="medium" 
            style="margin: 10px 0" 
            prefix-icon="el-icon-lock" 
            show-password
            v-model="user.password">
          </el-input>
        </el-form-item>
        <el-form-item style="margin: 10px 0; text-align: right">
          <el-button type="warning" size="small" autocomplete="off" @click="$router.push('/register')">Sign Up</el-button>
          <el-button type="primary" size="small" autocomplete="off" @click="login">Sign In</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import { setRoutes } from '@/router';

export default {
  name: "Login",
  data() {
    return {
      user: {},
      rules: {
        username: [
          {required: true, message: 'Please enter username', trigger: 'blur'},
          {min: 2, max: 10, message: 'Username should be between 2 and 5 characters long', trigger: 'blur'}
        ],
        password: [
          {required: true, message: 'Please enter password', trigger: 'blur'},
          {min: 1, max: 20, message: 'Length should be between 1 and 20 characters', trigger: 'blur'}
        ],
      }
    }
  },
  methods: {
    login(){
      localStorage.removeItem("token")
      localStorage.removeItem("menus")
      localStorage.removeItem("user")
      this.$refs['userForm'].validate((valid) => {
          if (valid) {  // Form validation is valid
            this.request.post("/user/login", this.user).then(res => {
              if(res.code === 200){
                console.log("res:" + res)
                localStorage.setItem("token", JSON.stringify(res.data.token)) 
                localStorage.setItem("user", JSON.stringify(res.data.userInfo)) 
                localStorage.setItem("menus", JSON.stringify(res.data.userInfo.menus)) 

                //dynamic route
                setRoutes()
                this.$router.push("/")
                this.$message.success("Login successful")
              }else{
                this.$message.error(res.msg)
              }
            })
          }
      });
    }
  }
}
</script>

<style>
  .wrapper {
    height: 100vh;
    background-image: linear-gradient(to bottom right, #FC466B, #3F5EFB);
    overflow: hidden;
  }
</style>
