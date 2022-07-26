<template>
  <div>
    <div style="margin: 10px 0">
      <!-- <el-input style="width: 200px" placeholder="Please enter the name" suffix-icon="el-icon-search" v-model="name"></el-input>
      <el-button class="ml-5" type="primary" @click="search">Search</el-button>
      <el-button type="warning" @click="reset">Reset</el-button> -->
    </div>

    <div style="margin: 10px 0">
      <el-button type="primary" @click="handleAdd()">Add root menu <i class="el-icon-circle-plus-outline"></i></el-button>
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
      border 
      stripe 
      :header-cell-style="{ textAlign: 'center', background:'beige' }" 
      :cell-style="rowClass"
      @selection-change="handleSelectionChange"
      row-key="id"
      default-expand-all>

      <div slot="empty" style="text-align: left;">
        <el-empty description="Sorry, there is no data you are looking for" />
      </div>
      <el-table-column type="selection" width="55"></el-table-column>
      <!-- <el-table-column prop="id" label="ID" width="80"></el-table-column> -->
      <el-table-column prop="name" label="Name"></el-table-column>
      <el-table-column prop="path" label="Path"></el-table-column>
      <el-table-column prop="pagePath" label="Page Path"></el-table-column>
      <el-table-column label="Icon" class-name="fontSize18" label-class-name="fontSize12">
        <template slot-scope="scope">
          <i :class="scope.row.icon"></i>
        </template>
      </el-table-column>
      <el-table-column prop="description" label="Description"></el-table-column>
      <el-table-column label="Operation"  width="350" align="center">
        <template slot-scope="scope">
          <el-button type="primary" @click="handleAdd(scope.row.id)" v-if="!scope.row.pid && !scope.row.path">Add submenu <i class="el-icon-plus"></i></el-button>
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

    <el-dialog title="Menu Info" :visible.sync="dialogFormVisible" width="30%" >
      <el-form label-width="100px" size="small">
        <el-form-item label="Name">
          <el-input v-model="form.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Path">
          <el-input v-model="form.path" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Page Path">
          <el-input v-model="form.pagePath" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Icon">
          <el-select clearable v-model="form.icon" placeholder="Please Choose" style="width: 100%">
            <el-option v-for="item in options" :key="item.name" :label="item.name" :value="item.value">
              <i :class="item.value" /> {{ item.name }}
            </el-option>
          </el-select>
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
  </div>

</template>

<script>
  export default {
    name: "Menu",
    data() {
      return {
        tableData: [],
        total: 0,
        currentPage: 1,
        pageSize: 5,
        name: "",
        form: {},
        dialogFormVisible: false,
        options:[],

        menuDialogVis: false,
        multipleSelection: [],
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
        this.request.get("/menu", {
          params: {
            name: this.name,
          }
        }).then(res => {
          this.tableData = res.data
        })

        this.request.get("/menu/icons").then(res => {
          this.options = res.data
        })
      },
      save() {
        this.request.post("/menu", this.form).then(res => {
          if (res.code === 200) {
            this.$message.success("Successfully saved")
            this.dialogFormVisible = false
            this.load()
          } else {
            this.$message.error("Failed to save")
          }
        })
      },
      handleAdd(pid) {
        this.dialogFormVisible = true
        this.form = {}
        if(pid){
          this.form.pid = pid
        }
      },
      handleEdit(row) {
        this.form = JSON.parse(JSON.stringify(row))
        this.dialogFormVisible = true

        this.request.get("/menu/icons").then(res => {
          this.options = res.data
        })
      },
      del(id) {
        this.request.delete("/menu/" + id).then(res => {
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
        this.request.post("/menu/del/batch", ids).then(res => {
          if (res.code === 200) {
            this.$message.success("Delete successfully")
            this.load()
          } else {
            this.$message.error("Failed to delete")
          }
        })
      },
      // reset() {
      //   this.name = ""
      //   this.load()
      // },
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
    }
  }
</script>

<style>
  .fontSize18{
    font-size: 18px;
  }
  .fontSize12{
    font-size: 12px;
  }
</style>