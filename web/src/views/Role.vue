<template>
  <div>
    <div style="margin: 10px 0">
      <el-input style="width: 200px" placeholder="Please enter the name" suffix-icon="el-icon-search" v-model="name"></el-input>
      <el-button class="ml-5" type="primary" @click="load">Search</el-button>
      <el-button type="warning" @click="reset">Reset</el-button>
    </div>

    <div style="margin: 10px 0">
      <el-button type="primary" @click="handleAdd">Add a new role <i class="el-icon-circle-plus-outline"></i></el-button>
      <el-popconfirm
          class="ml-5"
          confirm-button-text='DELETE'
          cancel-button-text='CANCEL'
          icon="el-icon-info"
          icon-color="red"
          title="Are you sure you want to delete these data?"
          @cancel="cancel"
          @confirm="delBatch"
      >
        <el-button type="danger" slot="reference">Delete in bulk <i class="el-icon-remove-outline"></i></el-button>
      </el-popconfirm>
    </div>

    <el-table 
      :data="tableData" 
      :default-sort = "{prop: 'id', order: 'descending'}" 
      border 
      stripe 
      :header-cell-style="{ textAlign: 'center', background:'beige' }" 
      :cell-style="rowClass"
      @selection-change="handleSelectionChange">

      <div slot="empty" style="text-align: left;">
        <el-empty description="Sorry, there is no data you are looking for" />
      </div>
      <el-table-column type="selection" width="55"></el-table-column>
      <!-- <el-table-column prop="id" label="ID" width="80"></el-table-column> -->
      <el-table-column prop="name" label="Role Name"></el-table-column>
      <el-table-column prop="roleKey" label="Role Key"></el-table-column>
      <el-table-column prop="description" label="Description"></el-table-column>
      <el-table-column label="Operation"  width="350" align="center">
        <template slot-scope="scope">
          <el-button type="info" @click="selectMenu(scope.row.id)">Assign menu <i class="el-icon-menu"></i></el-button>
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
    <div style="padding: 10px 0">
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="currentPage"
          :page-sizes="[5,10,20]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
      </el-pagination>
    </div>

    <el-dialog title="Role Info" :visible.sync="dialogFormVisible" width="30%" >
      <el-form label-width="100px" size="small">
        <el-form-item label="Role Name">
          <el-input v-model="form.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Role Key">
          <el-input v-model="form.roleKey" placeholder="Format: ROLE_XXX" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Description">
          <el-input v-model="form.description" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="formCancel">Cancel</el-button>
        <el-button type="primary" @click="save">Save</el-button>
      </div>
    </el-dialog>

    <el-dialog title="Menu Assignment" :visible.sync="menuDialogVis" width="30%">
      <el-tree
          :props="props"
          :data="menuData"
          show-checkbox
          node-key="id"
          ref="tree"
          :default-expanded-keys="expends"
          :default-checked-keys="checks">
         <span class="custom-tree-node" slot-scope="{ node, data }">
            <span><i :class="data.icon"></i> {{ data.name }}</span>
         </span>
      </el-tree>

      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogCancel">Cancel</el-button>
        <el-button type="primary" @click="saveRoleMenu">Save</el-button>
      </div>
    </el-dialog>
  </div>

</template>

<script>
  export default {
    name: "Role",
    data() {
      return {
        tableData: [],
        total: 0,
        currentPage: 1,
        pageSize: 5,
        name: "",
        form: {},
        dialogFormVisible: false,

        menuDialogVis: false,
        multipleSelection: [],
        menuData: [],
        props: {
          label: 'name',
        },
        expends: [],
        checks: [],
        roleId: 0,
        roleFlag: '',
        ids: [],
      }
    },
    created() {
      this.load()
    },
    methods: {
      rowClass(){
        return "text-align:center"
      },
      load() {
        this.request.get("/role/page", {
          params: {
            currentPage: this.currentPage,
            pageSize: this.pageSize,
            name: this.name,
          }
        }).then(res => {
          this.tableData = res.data.records
          this.total = res.data.total
        })

        // this.request.get("/menu/ids").then(r => {
        //   this.ids = r.data
        // })

      },
      save() {
        this.request.post("/role", this.form).then(res => {
          if (res.code === 200) {
            this.$message.success("Successfully saved")
            this.dialogFormVisible = false
            this.load()
          } else {
            this.$message.error("Failed to save")
          }
        })
      },
      saveRoleMenu() {
        console.log(this.roleId)
        this.request.post("/role/roleMenu/" + this.roleId, this.$refs.tree.getCheckedKeys()).then(res => {
          if (res.code === 200) {
            this.$message.success("Binding succeeded")
            this.menuDialogVis = false

            // 操作管理员角色后需要重新登录
            // if (this.roleFlag === 'ROLE_ADMIN') {
            //   this.$store.commit("logout")
            // }

          } else {
            this.$message.error(res.msg)
          }
        })
      },
      handleAdd() {
        this.dialogFormVisible = true
        this.form = {}
      },
      handleEdit(row) {
        this.form = JSON.parse(JSON.stringify(row))
        this.dialogFormVisible = true
      },
      del(id) {
        this.request.delete("/role/" + id).then(res => {
          if (res.code === 200) {
            this.$message.success("Successfully deleted")
            this.load()
          } else {
            this.$message.error("Failed to delete")
          }
        })
      },
      handleSelectionChange(val) {
        console.log(val)
        this.multipleSelection = val
      },
      delBatch() {
        let ids = this.multipleSelection.map(v => v.id)
        this.request.post("/role/del/batch", ids).then(res => {
          if (res.code === 200) {
            this.$message.success("Successfully deleted")
            this.load()
          } else {
            this.$message.error("Failed to delete")
          }
        })
      },
      reset() {
        this.name = ""
        this.load()
      },
      handleSizeChange(pageSize) {
        console.log(pageSize)
        this.pageSize = pageSize
        this.load()
      },
      handleCurrentChange(currentPage) {
        this.currentPage = currentPage
        this.load()
      },
      cancel(){
        this.$message.warning("Operation successfully canceled")
      },
      formCancel(){
        this.dialogFormVisible = false
        this.$message.warning("Form submission canceled")
      },
      dialogCancel(){
        this.menuDialogVis = false
        this.$message.warning("You canceled this operation")
      },
      selectMenu(roleId){
        this.menuDialogVis = true
        this.roleId = roleId

        this.request.get("/menu").then(res => {
          this.menuData = res.data

          //Process the menu data returned from the background into an id array
          this.expends = this.menuData.map(v => v.id) 
        })

        this.request.get("/role/roleMenu/" + roleId).then(res => {
          this.checks = res.data 
        })
      },
      // async selectMenu(role) {
      //   this.roleId = role.id
      //   this.roleFlag = role.flag

      //   // request menu data
      //   this.request.get("/menu").then(res => {
      //     this.menuData = res.data

      //     // Process the menu data returned from the background into an id array
      //     this.expends = this.menuData.map(v => v.id)
      //   })

      //   this.request.get("/role/roleMenu/" + this.roleId).then(res => {
      //     this.checks = res.data
      //     this.ids.forEach(id => {
      //       if (!this.checks.includes(id)) {
      //         // 可能会报错：Uncaught (in promise) TypeError: Cannot read properties of undefined (reading 'setChecked')
      //         this.$nextTick(() => {
      //           this.$refs.tree.setChecked(id, false)
      //         })
      //       }
      //     })
      //     this.menuDialogVis = true
      //   })
      // },
    }
  }
</script>

<style>
</style>