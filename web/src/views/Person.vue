<template>
  <el-card style="width: 500px;">
    <el-form label-width="80px" size="small">
      <el-upload
        class="avatar-uploader"
        :action="'http://'+ serverIp + ':9999/upload'"
        accept=".jpg, .png"
        :show-file-list="false"
        :on-success="handleAvatarSuccess"
        name="img">
        <img v-if="form.avatar" :src="form.avatar" class="avatar">
        <i v-else class="el-icon-plus avatar-uploader-icon"></i>
        <div slot="tip" style="color: orange;" class="el-upload__tip">Only accept jpg/png files, and no more than 4MB</div>
      </el-upload>
      <el-form-item label="username">
        <el-input v-model="form.username" autocomplete="off" disabled></el-input>
      </el-form-item>
      <el-form-item label="nickname">
        <el-input v-model="form.nickname" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="email">
        <el-input v-model="form.email" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="sex">
        <el-radio v-model="form.sex" label="0">Male</el-radio>
        <el-radio v-model="form.sex" label="1">Female</el-radio>
        <el-radio v-model="form.sex" label="2">Unknown</el-radio>
      </el-form-item>
      <el-form-item label="phone">
        <el-input v-model="form.phonenumber" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="save">Save</el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>

<script>
  import {serverIp} from "../../public/config";

  export default {
    name:"Person",
    data() {
      return {
        serverIp: serverIp,
        form:{},
        user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
      }
    },
    created(){
      this.getUser().then(res => {
        this.form = res
      })
    },
    methods:{
      async getUser() {
        return (await this.request.get("/user/userInfo")).data
      },
      save(){
        this.request.put("/user/userInfo", this.form).then(res => {
          if(res.code == 200){
            this.$message.success("Personal information modified successfully")
            // The method that triggers the parent to update the User
            this.$emit("refreshUser")

            // Update the user information stored by the browser
            this.getUser().then(res => {
              res.token = JSON.parse(localStorage.getItem("token"))
              localStorage.setItem("user", JSON.stringify(res))
            })
          }else{
            this.$message.error("Information modification failed, please try again")
          }
        })
      },
      handleAvatarSuccess(res){
        this.form.avatar = res.data
      },
    }
  }
</script>

<style>
  .avatar-uploader{
    text-align: center;
    padding-bottom: 10px;
  }
  .avatar-uploader .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
  }
  .avatar-uploader .el-upload:hover {
    border-color: #409EFF;
  }
  .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 138px;
    height: 138px;
    line-height: 138px;
    text-align: center;
  }
  .avatar {
    width: 138px;
    height: 138px;
    display: block;
  }
</style>