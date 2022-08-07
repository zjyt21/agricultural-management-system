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
      
      <el-dropdown-menu slot="dropdown" style="width: 150px; text-align: center;">
        <el-dropdown-item style="font-size: 14px; padding: 5px 0;" class="el-icon-user-solid">
          <router-link to="/person">Your Account</router-link>
        </el-dropdown-item>
        <br>
        <el-dropdown-item style="font-size: 14px; padding: 5px 0">
          <div class="el-icon-edit" @click="changePassword" >Change Password</div>
        </el-dropdown-item>
        <el-dropdown-item style="font-size: 14px; padding: 5px 0;" class="el-icon-right">
          <span style="text-decoration: none;" @click="logout">Sign Out</span>
        </el-dropdown-item>
      </el-dropdown-menu>
    </el-dropdown>

    <el-dialog title="Change Password" :visible.sync="dialogFormVisible" width="30%" center>
      <el-form label-width="150px" size="small" :model="form" :rules="rules" ref="pwdForm">
        <el-form-item label="Previous Password" prop="password">
          <el-input v-model="form.password" autocomplete="off" show-password></el-input>
        </el-form-item>
        <el-form-item label="New Password" prop="newPassword">
          <el-input v-model="form.newPassword" autocomplete="off" show-password></el-input>
        </el-form-item>
        <el-form-item label="Confirm Password" prop="confirmPassword">
          <el-input v-model="form.confirmPassword" autocomplete="off" show-password></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="formCancel">Cancel</el-button>
        <el-button type="primary" @click="save">Confirm</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  export default {
    name:"Header",

    data() {
      return {
        form:{},
        dialogFormVisible:false,
        // user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
        rules: {
          password: [
            { required: true, message: 'Please input current password', trigger: 'blur' },
            { min: 3, message: 'At least 3 digits long', trigger: 'blur' }
          ],
          newPassword: [
            { required: true, message: 'Please input new password', trigger: 'blur' },
            { min: 3, message: 'At least 3 digits long', trigger: 'blur' }
          ],
          confirmPassword: [
            { required: true, message: 'Please confirm your new password', trigger: 'blur' },
            { min: 3, message: 'At least 3 digits long', trigger: 'blur' }
          ],
        }

      }
    },
    created() {
      this.form.username = this.user.username
    },
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
      },
      changePassword(){
        this.dialogFormVisible = true
      },
      save() {
        this.$refs.pwdForm.validate((valid) => {
          if (valid) {
            if (this.form.newPassword !== this.form.confirmPassword) {
              this.$message.error("The new password entered twice is not the same")
              return false
            }
            this.request.post("/user/password", this.form).then(res => {
              if (res.code === 200) {
                this.$message.success("Successfully modified")
                this.$store.commit("logout")
              } else {
                this.$message.error("The operation is abnormal, please change the password again")
              }
            })
          }
        })
      },
      formCancel(){
        this.dialogFormVisible = false
        this.$message.warning("Operation canceled")
      },

    },
  }
</script>

<style>

</style>