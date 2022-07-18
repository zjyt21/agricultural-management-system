<template>
  <div class="wrapper">
    <div style="margin: 100px auto; background-color: #fff; width: 350px; height: 450px; padding: 20px; border-radius: 10px">
      <div style="height: 50px; text-align: center">
        <img src="../assets/logo.png"  height="100%" alt="">
      </div>
      <div style="margin: 20px 0; text-align: center; font-size: 24px"><b>Register</b></div>
      <el-form :model="user" :rules="rules" ref="userForm">
        <el-form-item prop="username">
          <el-input 
            placeholder="Please enter username (unmodified)"
            size="medium" 
            style="margin: 5px 0" 
            prefix-icon="el-icon-user" 
            v-model="user.username">
          </el-input>
        </el-form-item>
        <el-form-item prop="nickname">
          <el-input
          placeholder="Please enter nickname" 
            size="medium" 
            style="margin: 5px 0" 
            prefix-icon="el-icon-user-solid" 
            v-model="user.nickname">
          </el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input 
            placeholder="Please enter password"
            size="medium" 
            style="margin: 5px 0" 
            prefix-icon="el-icon-lock" 
            show-password
            v-model="user.password">
          </el-input>
        </el-form-item>
        <el-form-item prop="confirmPassword">
          <el-input
          placeholder="Please confirm password" 
            size="medium" 
            style="margin: 5px 0" 
            prefix-icon="el-icon-lock" 
            show-password
            v-model="user.confirmPassword">
          </el-input>
        </el-form-item>
        <el-form-item style="margin: 10px 0; text-align: right">
          <el-button type="warning" size="small" autocomplete="off" @click="$router.push('/login')">Back to login</el-button>
          <el-button type="primary" size="small" autocomplete="off" @click="register">Register</el-button>
        </el-form-item>
      </el-form>
      
    </div>
  </div>
</template>

<script>
// import {setRoutes} from "@/router";

export default {
  name: "Register",
  data() {
    return {
      user: {},
      rules: {
        username: [
          {required: true, message: 'Username', trigger: 'blur'},
          {min: 2, max: 10, message: 'Username should be between 2 and 5 characters long', trigger: 'blur'}
        ],
        nickname: [
          {required: true, message: 'Nickname', trigger: 'blur'},
          {min: 2, max: 10, message: 'Nickname should be between 2 and 5 characters long', trigger: 'blur'}
        ],
        password: [
          {required: true, message: 'Password', trigger: 'blur'},
          {min: 1, max: 20, message: 'Length should be between 1 and 20 characters', trigger: 'blur'}
        ],
        confirmPassword: [
          {required: true, message: 'Confirm password', trigger: 'blur'},
          {min: 1, max: 20, message: 'Length should be between 1 and 20 characters', trigger: 'blur'}
        ],
      }
    }
  },
  methods: {
    register() {
      this.$refs['userForm'].validate((valid) => {
          if (valid) {  // 表单校验合法
            if(this.user.password !== this.user.confirmPassword){
              this.$message.error("Password does not match")
              return false
            }
            this.request.post("/user/register", this.user).then(res => {
              if(res.code == 200){
                this.$message.success("Registration success")
              }else{
                this.$message.error(res.msg)
              }
            })
          }
      });
      // this.$refs['userForm'].validate((valid) => {
      //   if (valid) {  // 表单校验合法
      //     this.request.post("/user/login", this.user).then(res => {
      //       if (res.code === '200') {
      //         localStorage.setItem("user", JSON.stringify(res.data))  // 存储用户信息到浏览器
      //         localStorage.setItem("menus", JSON.stringify(res.data.menus))  // 存储用户信息到浏览器
      //         // 动态设置当前用户的路由
      //         setRoutes()
      //         this.$message.success("登录成功")

      //         if (res.data.role === 'ROLE_STUDENT') {
      //           this.$router.push("/front/home")
      //         } else {
      //           this.$router.push("/")
      //         }
      //       } else {
      //         this.$message.error(res.msg)
      //       }
      //     })
      //   }
      // });
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
