<template>
  <div>
    <div style="margin-bottom: 30px;">
      
    </div>

    <div style="margin: 10px 0;">
      <el-input style="width: 200px;" placeholder="Please input username" suffix-icon="el-icon-search" v-model="username"></el-input>
      <el-input style="width: 200px;" placeholder="Please input nickname" suffix-icon="el-icon-search" class="ml-5" v-model="nickname"></el-input>
      <el-input style="width: 200px;" placeholder="Please input email" suffix-icon="el-icon-message" class="ml-5" v-model="email"></el-input>
      <el-button class="ml-5" type="primary" @click="loadPage">Search</el-button>
      <el-button class="ml-5" type="warning" @click="reset">Reset</el-button>
    </div>

    <div style="margin: 10px 0;">
      <el-button type="primary" @click="handleAdd">Add <i class="el-icon-circle-plus-outline"></i></el-button>
      <el-popconfirm
        class="ml-5"
        confirm-button-text='DELETE'
        cancel-button-text='CANCEL'
        icon="el-icon-info"
        icon-color="red"
        title="Are you sure you want to delete these data?"
        @cancel="cancel"
        @confirm="delBatch">
        <el-button type="danger" slot="reference">Delete in bulk <i class="el-icon-remove-outline"></i></el-button>
      </el-popconfirm>
    </div>

    <el-table 
      :default-sort = "{prop: 'id', order: 'ascending'}" 
      :data="tableData" 
      border 
      :header-cell-style="{ textAlign: 'center', background:'beige' }" 
      stripe
      :cell-style="rowClass"
      @selection-change="handleSelectionChange">
      <div slot="empty" style="text-align: left;">
        <el-empty description="Sorry, there is no data you are looking for" />
      </div>
      <el-table-column type="selection" width="55">
      </el-table-column>
      <el-table-column prop="id" label="ID" sortable width="80"></el-table-column>
      <el-table-column prop="username" label="Username" width="140"></el-table-column>
      <el-table-column prop="nickname" label="Nickname" width="120"></el-table-column>
      <el-table-column prop="role" label="Role" width="200"></el-table-column>
      <el-table-column prop="email" label="Email"></el-table-column>
      <el-table-column prop="phonenumber" label="Phone"></el-table-column>
      <el-table-column prop="option" label="Operation">
        <template slot-scope="scope">
          <el-button type="success" @click="handleEdit(scope.row)">Edit <i class="el-icon-edit"></i></el-button>
          <el-popconfirm
          class="ml-5"
          confirm-button-text='DELETE'
          cancel-button-text='CANCEL'
          icon="el-icon-info"
          icon-color="red"
          title="Are you sure to delete?"
          @confirm="del(scope.row.id)"
          @cancel="cancel"
          >
            <el-button type="danger" slot="reference">Delete <i class="el-icon-remove-outline"></i></el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
        
    </el-table>
    <div style="padding: 10px 0;">
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pageNum"
        :page-sizes="[5, 10, 20]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total">
      </el-pagination>
    </div>

    <el-dialog title="User Info" :visible.sync="dialogFormVisible" width="30%">
      <el-form label-width="100px" size="small">
        <el-form-item label="Username">
          <el-input v-model="form.username" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Nickname">
          <el-input v-model="form.nickname" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Email">
          <el-input v-model="form.email" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Phone">
          <el-input v-model="form.phonenumber" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Role">
          <el-select clearable v-model="form.role" style="width: 100%;">
            <el-option v-for="item in roles" :key="item.roleKey" :label="item.roleKey" :value="item.roleKey"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="formCancel">Cancel</el-button>
        <el-button type="primary" @click="save">Save</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  export default {
    name:"User",
    data() {
      return {
        tableData:[],
        total:0,
        pageNum:1,
        pageSize:5,
        username:"",
        nickname:"",
        email:"",
        form:{},
        multipleSelection: [],
        dialogFormVisible: false,

        roles:[],
      }
    },
    created() {
      //Request paging query data
      this.loadPage()
    },
    methods: {
      rowClass(){
        return "text-align:center"
      },
      cancel(){
        this.$message.warning("Operation successfully canceled")
      },
      formCancel(){
        this.dialogFormVisible = false
        this.$message.warning("Form submission canceled")
      },
      loadPage(){
        this.request.get("/user/page", {
          params:{
            currentPage: this.pageNum,
            pageSize: this.pageSize,
            username: this.username,
            email:this.email,
            nickname:this.nickname,
          }
        }).then(res => {
          console.log(res)
          this.tableData = res.data.records
          this.total = res.data.total
        })

        this.request.get("/role").then(res => {
          this.roles = res.data
        })
      },
      handleSizeChange(pageSize){
        this.pageSize = pageSize
        this.loadPage()
      },
      handleCurrentChange(pageNum){
        this.pageNum = pageNum
        this.loadPage()
      },
      reset(){
        this.username = "",
        this.email = "",
        this.nickname = "",
        this.loadPage()
      },
      handleAdd(){
        this.dialogFormVisible = true
        this.form = {}
      },
      save(){
        this.request.post("/user", this.form).then(res => {
          if(res.code == 200){
            this.$message.success("Successfully saved")
            this.dialogFormVisible = false;
            this.loadPage()
          }else{
            this.$message.error("Failed to save, please try again")
          }
        })
      },
      handleEdit(row){
        this.form = Object.assign({}, row) 
        this.dialogFormVisible = true
      },
      del(id){
        this.request.delete("/user/" + id).then(res => {
          if(res.code == 200){
            this.$message.success("Successfully deleted")
            this.loadPage()
          }else{
            this.$message.error("Failed to delete")
          }
        })
      },
      handleSelectionChange(val){
        this.multipleSelection = val
      },
      delBatch(){
        let ids = this.multipleSelection.map(v => v.id)
        this.request.post("/user/batchDel", ids).then(res => {
          if(res.code == 200){
            this.$message.success("Successfully deleted")
            this.loadPage()
          }else{
            this.$message.error("Failed to delete")
          }
        })
      },
    }
                  
  }
</script>

<style>

</style>