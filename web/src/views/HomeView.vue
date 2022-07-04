<template>
  <el-container style="min-height: 100vh">
    <el-aside :width="sideWidth + 'px'" style="background-color: rgb(48, 65, 86); box-shadow: 2px 0 6px rgb(0 21 21 / 35%);">
      <el-menu :default-openeds="['1']" style="min-height: 100%; overflow-x:hidden" 
                background-color="rgb(48, 65, 86)"
                text-color="#fff"
                active-text-color="#ffd04b"
                :collapse-transition="false"
                :collapse="isCollapse">
        <div style="height: 60px; text-align: center">
          <img src="../assets/logo.png"  height="100%" alt="">
        </div>
        <div style="height: 20px; text-align: center; color:chartreuse;" v-show="!isCollapse">
          <b>Farmer Centre</b>
        </div>
        <el-menu-item index="0">
          <i class="el-icon-school"></i>
          <template slot="title">
            <span>Home</span>
          </template>
        </el-menu-item>
        <el-submenu index="1">
          <template slot="title">
            <i class="el-icon-coin"></i>
            <span>Data Records</span>
          </template>
          <el-menu-item index="1-1">Agricultural Behavior</el-menu-item>
          <el-menu-item index="1-2">Soil and Weather</el-menu-item>
          <el-menu-item index="1-3">History Harvest</el-menu-item>
        </el-submenu>
        <el-menu-item index="2">
          <i class="el-icon-s-marketing"></i>
          <template slot="title">
            <span>Market Details</span>
          </template>
        </el-menu-item>
        <el-submenu index="3">
          <template slot="title">
            <i class="el-icon-s-data"></i>
            <span>Data Analysis</span>
          </template>
          <el-menu-item index="3-1">Soil Assessment Model</el-menu-item>
          <el-menu-item index="3-2">Yield Estimation Model</el-menu-item>
        </el-submenu>
        <el-submenu index="4">
          <template slot="title">
            <i class="el-icon-s-custom"></i>
            <span>Staff</span>
          </template>
          <el-menu-item index="4-1">Authority Management</el-menu-item>
          <el-menu-item index="4-2">Operation Record</el-menu-item>
        </el-submenu>
      </el-menu>
    </el-aside>
    
    <el-container>
      <el-header style="font-size: 12px; border-bottom: 1px solid #ccc; box-shadow: 0px 1px 3px #ccc; line-height: 60px; display: flex">
        <div style="flex: 1; font-size: 18px">
          <span :class="collapseBtnClass" style="cursor: pointer" @click="collapse"></span>
        </div>
        <el-dropdown style="width: 70px; cursor: pointer">
          <span>BOSS</span><i class="el-icon-arrow-down" style="margin-left: 5px"></i>
          <el-dropdown-menu slot="dropdown">

            <el-dropdown-item class="el-icon-user-solid">Your Farm</el-dropdown-item>
            <br>
            <el-dropdown-item class="el-icon-right">Sign Out</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      
      </el-header>
      
      <el-main>
        <div style="margin-bottom: 30px;">
            <el-breadcrumb separator="/">
              <el-breadcrumb-item :to="{ path: '/' }">Home</el-breadcrumb-item>
              <el-breadcrumb-item>Market Details</el-breadcrumb-item>
            </el-breadcrumb>
          </div>

        <el-form :inline="true"  class="demo-form-inline">
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
          <el-button type="primary" class="ml-5">Import <i class="el-icon-document-add"></i></el-button>
          <el-button type="primary" class="ml-5">Download <i class="el-icon-download"></i></el-button>
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
          <el-table-column prop="unitPrice" label="Unit Price (GBP per ton)" sortable>
          </el-table-column>
          <el-table-column prop="supply" label="Supply (10,000 tons)" >
          </el-table-column>
          <el-table-column prop="demand" label="Demand (10,000 tons)" >
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
            :page-size="5"
            layout="total, sizes, prev, pager, next, jumper"
            :total="total">
          </el-pagination>
        </div>

        <el-dialog title="Market Information" :visible.sync="dialogFormVisible" width="30%" center>
          <el-form label-width="auto" size="small">
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
            <el-form-item label="Unit Price" prop="unitPrice">
              <el-input 
                type="number" 
                v-model.number="form.unitPrice" 
                autocomplete="off" 
                placeholder="(GBP per ton)"
                onKeypress="return (/[\d]/.test(String.fromCharCode(event.keyCode)))">
              </el-input>
            </el-form-item>
            <el-form-item label="Supply">
              <el-input type="number" v-model.number="form.supply" autocomplete="off" placeholder="(in 10,000 tons)" onKeypress="return (/[\d]/.test(String.fromCharCode(event.keyCode)))"></el-input>
            </el-form-item>
            <el-form-item label="Demand">
              <el-input type="number" v-model.number="form.demand" autocomplete="off" placeholder="(in 10,000 tons)" onKeypress="return (/[\d]/.test(String.fromCharCode(event.keyCode)))"></el-input>
            </el-form-item>
          </el-form>
          <div slot="footer" class="dialog-footer">
            <el-button @click="formCancel">Cancel</el-button>
            <el-button type="primary" @click="save">Confirm</el-button>
          </div>
        </el-dialog>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>

  export default {
    name: 'HomeView',
    components: {
      
    },
    data() {
      return {
        tableData: [],
        total:0,
        currentPage:1,
        pageSize:5,
        crop:"",
        form:{},
        multipleSelection: [],
        dialogFormVisible: false,

        dateVal:'',
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

        collapseBtnClass:'el-icon-s-fold',
        isCollapse: false,
        sideWidth: 250
      }
    },
    created() {
      this.loadPage()
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
      rowClass(){
        return "text-align:center"
      },
      loadPage(){
        this.request.get("/market-trend/page", {
          params:{
            currentPage: this.currentPage,
            pageSize: this.pageSize,
            crop:this.crop,
            beginDate:this.dateVal[0],
            endDate:this.dateVal[1],
          }
        }).then(res => {
            console.log(res)
            console.log(this.dateVal)
            this.tableData = res.records
            this.total = res.total
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
        this.request.post("/market-trend", this.form).then(res => {
          if(res){
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
        this.request.delete("/market-trend/" + id).then(res => {
          if(res){
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
        this.request.post("/market-trend/batchDel", ids).then(res => {
          if(res){
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
        this.loadPage()
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
