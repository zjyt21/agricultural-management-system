<template>
  <div>
    <el-form :inline="true" class="demo-form-inline" style="text-align: center;">
      <el-form-item>
        <el-date-picker
          v-model="chartDateVal"
          type="daterange"
          unlink-panels
          range-separator="to"
          start-placeholder="Begin date"
          end-placeholder="End date"
          value-format="yyyy-MM-dd">
        </el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button class="ml-5" type="primary" @click="getChart" icon="el-icon-search">Search</el-button>
      </el-form-item>
    </el-form>
    
    <el-row :gutter="10" style="margin-bottom: 40px;">
      <el-col :span="2"></el-col>
      <el-col :span="12" >
        <el-card id="price" style="height: 500px;" class="chart"></el-card>
      </el-col>
      <el-col :span="12" >
          <el-card id="trend" style="height: 500px;" class="chart"></el-card>
      </el-col>
    </el-row>

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
        :page-size=pageSize
        layout="total, sizes, prev, pager, next, jumper"
        :total="total">
      </el-pagination>
    </div>

    <el-dialog title="Market Information" :visible.sync="dialogFormVisible" width="30%" center>
      <el-form label-width="auto" size="small" :model="form" :rules="rules" ref="marketForm">
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
  </div>
</template>

<script>
  import * as echarts from 'echarts'; 
  export default {
    name:"Market",
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

        rules: {
          date: [ {required: true, message: 'Please choose the date', trigger: 'blur'},],
          crop: [{required: true, message: 'Please choose one crop', trigger: 'blur'},],
          unitPrice: [{required: true, message: 'Please input the price', trigger: 'blur'},],
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
    mounted() {
      this.chartDateVal[0] = this.getFormatDate(1)
      this.chartDateVal[1] = this.getFormatDate(0)
      this.getChart()
    },
    methods: {
      getFormatDate(offset) {
          var date = new Date()
          var seperator1 = '-'
          var year = date.getFullYear() - offset
          var month = date.getMonth() + 1
          var strDate = date.getDate()
          if (month >= 1 && month <= 9) {
            month = '0' + month
          }
          if (strDate >= 0 && strDate <= 9) {
            strDate = '0' + strDate
          }
          var currentdate = year + seperator1 + month + seperator1 + strDate
          return currentdate
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
        this.$refs['marketForm'].validate((valid) => {
            if (valid) {  // Form validation is valid
              this.request.post("/market-trend", this.form).then(res => {
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
        this.request.delete("/market-trend/" + id).then(res => {
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
        this.request.post("/market-trend/batchDel", ids).then(res => {
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
        this.loadPage()
      },
      cancel(){
        this.$message.warning("Operation successfully canceled")
      },
      formCancel(){
        this.dialogFormVisible = false
        this.$message.warning("Form submission canceled")
      },
      getChart() {
        var priceChartDom = document.getElementById('price');
        var priceChart = echarts.init(priceChartDom);

        var trendChartDom = document.getElementById('trend');
        var trendChart = echarts.init(trendChartDom);

        var priceOption = {
          title: {
            text: 'Unit Price of Crops',
            left: 'center',
            textStyle:{
              fontStyle:'normal',
              fontWeight:'bold',
              fontFamily:'Arial',
              fontSize:25
            }
          },
          tooltip: {
            trigger: 'item',
            axisPointer: {
              type: 'cross'
            }
          },
          legend: {
            orient: 'horizontal',
            x: 'center',
            y: 'bottom'
          },
          grid: {
            left: '5%',
            right: '7%',
            top:'15%',
            bottom: '12%',
            containLabel: true
          },
          toolbox: {
              show: true,
              //toolbox的配置项
              feature: {
                  //辅助线的开关
                  mark: { show: true },
                  //动态类型切换
                  magicType: {
                      show: true,
                      //line为折线图，bar为柱状图
                      type: ['line', 'bar']
                  },
                  //将图标保存为图片
                  saveAsImage: { show: true }
              }
          },
          calculable: true,
          xAxis: {
            name: 'Date',
            type: 'time',
            
            //数值起始和结束两端空白策略
            // boundaryGap: false,
            axisLabel: {        
                show: true,
                textStyle: {
                    color: 'green',
                    fontSize:'13',
                    fontWeight:'bold'
                }
            },
            axisLine:{
              lineStyle:{
                color:'black',
                width:2,
              }
            },
          },
          yAxis: {
            name: 'Price (GBP per ton)',
            type: 'value',  
            // splitLine: {
            //     lineStyle: {
            //       color: ['#eee']
            //     }
            // },
            axisLabel: {        
                show: true,
                textStyle: {
                    color: 'black',
                    fontSize:'10',
                }
            },
            axisLine:{
              lineStyle:{
                color:'black',
                width:2,
              }
            }      
          },
          series: [
            { name: 'paddy', data: [], type: 'line'},
            { name: 'potato', data: [], type: 'line'},
            { name: 'soybean', data: [], type: 'line'},
            { name: 'peanut', data: [], type: 'line'},
            { name: 'wheat', data: [], type: 'line'},
            { name: 'barley', data: [], type: 'line'},
          ]
        };

        var trendOption = {
          title: {
            text: 'Demand and Production Comparison',
            subtext: 'demand minus supply (local market)',
            left: 'center',
            textStyle:{
              fontStyle:'normal',
              fontWeight:'bold',
              fontFamily:'Arial',
              fontSize:25
            }
          },
          tooltip: {
            trigger: 'item',
            axisPointer: {
              type: 'cross'
            }
          },
          legend: {
            orient: 'horizontal',
            x: 'center',
            y: 'bottom',
          },
          grid: {
            left: '6%',
            right: '7%',
            top:'20%',
            bottom: '12%',
            containLabel: true
          },
          toolbox: {
              show: true,
              //toolbox的配置项
              feature: {
                  //辅助线的开关
                  mark: { show: true },
                  //动态类型切换
                  magicType: {
                      show: true,
                      //line为折线图，bar为柱状图
                      type: ['line', 'bar']
                  },
                  //将图标保存为图片
                  saveAsImage: { show: true }
              }
          },
          calculable: true,
          xAxis: {
            name: 'Date',
            type: 'time',
            //数值起始和结束两端空白策略
            boundaryGap: false,
            axisLabel: {        
                show: true,
                textStyle: {
                    color: 'green',
                    fontSize:'13',
                    fontWeight:'bold'
                }
            },
            axisLine:{
              lineStyle:{
                color:'black',
                width:2,   //这里是坐标轴的宽度,可以去掉
              }
            }
          },
          yAxis: {
            name: 'Quantity (10000 tons)',
            type: 'value',
            axisLabel: {        
                show: true,
                textStyle: {
                    color: 'black',
                    fontSize:'10'
                }
            },
            axisLine:{
              lineStyle:{
                color:'black',
                width:2,
              }
            }
          },
          series: [
            { name: 'paddy', data: [], type: 'line'},
            { name: 'potato', data: [], type: 'line'},
            { name: 'soybean', data: [], type: 'line'},
            { name: 'peanut', data: [], type: 'line'},
            { name: 'wheat', data: [], type: 'line'},
            { name: 'barley', data: [], type: 'line'},
          ]
        };
        this.request.get("/market-trend/chart", {
            params:{
              beginDate: this.chartDateVal[0],
              endDate: this.chartDateVal[1],
            }
        }).then(res => {
          priceOption.series[0].data = res.data[0].map(v => [v.date, v.unitPrice])
          priceOption.series[1].data = res.data[1].map(v => [v.date, v.unitPrice])
          priceOption.series[2].data = res.data[2].map(v => [v.date, v.unitPrice])
          priceOption.series[3].data = res.data[3].map(v => [v.date, v.unitPrice])
          priceOption.series[4].data = res.data[4].map(v => [v.date, v.unitPrice])
          priceOption.series[5].data = res.data[5].map(v => [v.date, v.unitPrice])

          trendOption.series[0].data = res.data[0].map(v => [v.date, v.demand - v.supply])
          trendOption.series[1].data = res.data[1].map(v => [v.date, v.demand - v.supply])
          trendOption.series[2].data = res.data[2].map(v => [v.date, v.demand - v.supply])
          trendOption.series[3].data = res.data[3].map(v => [v.date, v.demand - v.supply])
          trendOption.series[4].data = res.data[4].map(v => [v.date, v.demand - v.supply])
          trendOption.series[5].data = res.data[5].map(v => [v.date, v.demand - v.supply])


          priceChart.setOption(priceOption);
          trendChart.setOption(trendOption);
        })
       
      },
    },
  }
</script>

<style scoped>
  .chart {
    background-image: linear-gradient(to top, #1c92d2 0%, #f2fcfe 100%);
    overflow: hidden;
  };
</style>