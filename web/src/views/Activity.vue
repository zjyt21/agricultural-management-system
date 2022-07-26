<template>
  <div>
    <el-form :inline="true" class="demo-form-inline">
      <el-form-item prop="crop">
        <el-select filterable v-model="crop" placeholder="Please select a crop name">
            <el-option label="paddy" value="paddy"></el-option>
            <el-option label="potato" value="potato"></el-option>
            <el-option label="soybean" value="soybean"></el-option>
            <el-option label="peanut" value="peanut"></el-option>
            <el-option label="wheat" value="wheat"></el-option>
            <el-option label="barley" value="barley"></el-option>
          </el-select>
      </el-form-item>
      <el-form-item prop="type">
        <el-select filterable v-model="type" placeholder="Please select the activity">
          <el-option label="watering" value="watering"></el-option>
          <el-option label="fertilization (nitrogen)" value="nitrogen"></el-option>
          <el-option label="fertilization (phosphate)" value="phosphate"></el-option>
          <el-option label="sowing" value="sowing"></el-option>
          <el-option label="harvest" value="harvest"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item prop="date">
        <el-date-picker
          v-model="dateVal"
          type="daterange"
          unlink-panels
          range-separator="to"
          start-placeholder="Begin date"
          end-placeholder="End date"
          :picker-options="pickerOptions"
          value-format="yyyy-MM-dd">
        </el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button class="ml-5" type="primary" @click="loadPage" icon="el-icon-search">Search</el-button>
        <el-button class="ml-5" type="warning" @click="reset" icon="el-icon-refresh">Reset</el-button>
      </el-form-item>
    </el-form>

    <div style="margin: 10px 0;">
      <el-button type="primary" @click="handleAdd">Add <i class="el-icon-circle-plus-outline"></i></el-button>
      <el-popconfirm
        class="ml-5"
        confirm-button-text='DELETE'
        cancel-button-text='CANCEL'
        icon="el-icon-info"
        icon-color="red"
        title="Are you sure you want to delete these data?"
        @confirm="delBatch"
        @cancel="cancel"
      >
      <el-button type="danger" slot="reference">Delete in bulk <i class="el-icon-remove-outline"></i></el-button>
      </el-popconfirm>
    </div>

    <el-table 
      :data="tableData" 
      :default-sort = "{prop: 'date', order: 'descending'}" 
      border 
      stripe 
      :header-cell-style="{ textAlign: 'center', background:'beige' }" 
      :cell-style="rowClass"
      @selection-change="handleSelectionChange">
      <div slot="empty" style="text-align: left;">
        <el-empty description="Sorry, there is no data you are looking for" />
      </div>
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="date" label="Date" sortable width="100">
      </el-table-column>
      <el-table-column prop="crop" label="Crop" width="150">
      </el-table-column>
      <el-table-column prop="type" label="Specific Activity">
      </el-table-column>
      <el-table-column prop="quantity" label="Quantity (in KG)" sortable>
      </el-table-column>
      <el-table-column prop="operator" label="Operator" >
      </el-table-column>
      <el-table-column prop="option" label="Operation" width="200">
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
        :current-page="currentPage"
        :page-sizes="[5, 10, 20]"
        :page-size=pageSize
        layout="total, sizes, prev, pager, next, jumper"
        :total="total">
      </el-pagination>
    </div>

    <el-dialog title="Activities Records" :visible.sync="dialogFormVisible" width="30%" center>
      <el-form label-width="auto" size="small" :model="form" :rules="rules" ref="activityForm">
        <el-form-item label="Date" prop="date">
          <el-col :span="11">
            <el-date-picker type="date" placeholder="select date" v-model="form.date" style="width: 100%;" value-format="yyyy-MM-dd"></el-date-picker>
          </el-col>
        </el-form-item>
        <el-form-item label="Crop" prop="crop">
          <el-select filterable v-model="form.crop" placeholder="Please select a crop name">
            <el-option label="paddy" value="paddy"></el-option>
            <el-option label="potato" value="potato"></el-option>
            <el-option label="soybean" value="soybean"></el-option>
            <el-option label="peanut" value="peanut"></el-option>
            <el-option label="wheat" value="wheat"></el-option>
            <el-option label="barley" value="barley"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="Activity" prop="type">
          <el-select filterable v-model="form.type" placeholder="Please select the specific activity">
            <el-option label="watering" value="watering"></el-option>
            <el-option label="fertilization (nitrogen)" value="nitrogen"></el-option>
            <el-option label="fertilization (phosphate)" value="phosphate"></el-option>
            <el-option label="sowing" value="sowing"></el-option>
            <el-option label="harvest" value="harvest"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="Quantity" prop="quantity">
          <el-input 
            type="number" 
            v-model.number="form.quantity" 
            autocomplete="off" 
            placeholder="(KG)"
            onKeypress="return (/[\d]/.test(String.fromCharCode(event.keyCode)))">
          </el-input>
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
  name:'Activity',
  data() {
    return {
      tableData: [],
      total:0,
      currentPage:1,
      pageSize:5,
      crop:"",
      type:"",
      form:{},
      multipleSelection: [],
      dialogFormVisible: false,

      rules: {
        date: [ {required: true, message: 'Please choose the date', trigger: 'blur'},],
        crop: [{required: true, message: 'Please choose one crop', trigger: 'blur'},],
        type: [{required: true, message: 'Please choose the activity', trigger: 'blur'},],
        quantity: [{required: true, message: 'Please input the quantity', trigger: 'blur'},],
      },

      chartDateVal:[],

      dateVal:[],
      pickerOptions: {
        shortcuts: [{
          text: 'within 7 days',
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
            picker.$emit('pick', [start, end]);
          }
        }, {
          text: 'within 30 days',
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
            picker.$emit('pick', [start, end]);
          }
        }, {
          text: 'within 90 days',
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
            picker.$emit('pick', [start, end]);
          }
        }]
      },
    }
  },
  watch:{
    dateVal(val){
      if(val == null) this.dateVal = ''
    },
    chartDateVal(val){
      if(val == null) this.chartDateVal = ''
    }
  },
  created() {
    this.loadPage()
  },
  methods: {
    rowClass(){
      return "text-align:center"
    },
    loadPage(){
      this.request.get("/agri-activity/page", {
        params:{
          currentPage: this.currentPage,
          pageSize: this.pageSize,
          crop:this.crop,
          type:this.type,
          beginDate:this.dateVal[0],
          endDate:this.dateVal[1],
        }
      }).then(res => {
          this.tableData = res.data.records
          this.total = res.data.total
          let maxPage = parseInt((this.total - 1) / this.pageSize + 1);
          if(this.currentPage > maxPage){
            this.currentPage = maxPage
          }
        })
    },
    handleSizeChange(pageSize){
      this.pageSize = pageSize
      this.loadPage()
    },
    handleCurrentChange(currentPage){
      this.currentPage = currentPage
      this.loadPage()
    },
    handleAdd(){
      this.dialogFormVisible = true

      this.form = {}
    },
    save(){
      this.$refs['activityForm'].validate((valid) => {
          if (valid) {  // Form validation is valid
            this.request.post("/agri-activity", this.form).then(res => {
              if(res.code === 200){
                this.$message.success("Successfully saved")
                this.dialogFormVisible = false;
                this.loadPage()
              }else{
                this.$message.error("Failed to save, please try again")
              }
            })
          }else{
            this.$message.error("Failed to save, please check that your data meets the requirements")
          }
      });
    },
    handleEdit(row){
      this.form = Object.assign({}, row) 
      this.dialogFormVisible = true
    },
    del(id){
      this.request.delete("/agri-activity/" + id).then(res => {
        if(res.code === 200){
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
      this.request.post("/agri-activity/batchDel", ids).then(res => {
        if(res.code === 200){
          this.$message.success("Successfully deleted")
          this.loadPage()
        }else{
          this.$message.error("Failed to delete")
        }
      })
    },
    reset(){
      this.crop = "",
      this.dateVal= "",
      this.currentPage = 1;
      this.type = "",
      this.loadPage()
    },
    cancel(){
      this.$message.warning("Operation successfully canceled")
    },
    formCancel(){
      this.dialogFormVisible = false
      this.$message.warning("Form submission canceled")
    },
  },

}
</script>

<style>

</style>