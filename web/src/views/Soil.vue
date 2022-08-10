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
      <el-col :span="24" >
        <el-card id="score" style="height: 500px;" class="chart"></el-card>
      </el-col>
    </el-row>

    <el-form :inline="true" class="demo-form-inline">
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
      <el-table-column prop="compaction" label="Force to Penetrate the Soil (N)" width="220px">
      </el-table-column>
      <el-table-column prop="soilTemperature" label="Soil Temperature (°C)">
      </el-table-column>
      <el-table-column prop="ph" label="PH" width="60px">
      </el-table-column>
      <el-table-column prop="water" label="Water Content (%)" >
      </el-table-column>
      <el-table-column prop="nitrogen" label="Nitrogen Content (%)" >
      </el-table-column>
      <el-table-column prop="phosphorus" label="Phosphorus Content (%)" >
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
        :page-sizes="[5, 10, 20, this.total]"
        :page-size=pageSize
        layout="total, sizes, prev, pager, next, jumper"
        :total="total">
      </el-pagination>
    </div>

    <el-dialog title="Soil Condition" :visible.sync="dialogFormVisible" width="40%" center>
      <el-form label-width="auto" size="small" :model="form" :rules="rules" ref="soilForm">
        <el-form-item label="Date" prop="date">
          <el-col :span="11">
            <el-date-picker type="date" placeholder="select date" v-model="form.date" style="width: 100%;" value-format="yyyy-MM-dd"></el-date-picker>
          </el-col>
        </el-form-item>
        <el-form-item label="Force to Penetrate the Soil" prop="compaction">
          <el-input 
            type="number" 
            v-model.number="form.compaction" 
            autocomplete="off" 
            placeholder="(N)"
            onKeypress="return (/[[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?]/.test(String.fromCharCode(event.keyCode)))">
          </el-input>
        </el-form-item>
        <el-form-item label="Soil Temperature" prop="soilTemperature">
          <el-input 
            type="number" 
            v-model.number="form.soilTemperature" 
            autocomplete="off" 
            placeholder="(°C)"
            onKeypress="return (/[[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?]/.test(String.fromCharCode(event.keyCode)))">
          </el-input>
        </el-form-item>
        <el-form-item label="Soil pH" prop="ph">
          <el-input 
            type="number" 
            v-model.number="form.ph" 
            autocomplete="off" 
            placeholder=""
            onKeypress="return (/[[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?]/.test(String.fromCharCode(event.keyCode)))">
          </el-input>
        </el-form-item>
        <el-form-item label="Water Content" prop="water">
          <el-input 
            type="number" 
            v-model.number="form.water" 
            autocomplete="off" 
            placeholder="(%)"
            onKeypress="return (/[[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?]/.test(String.fromCharCode(event.keyCode)))">
          </el-input>
        </el-form-item>
        <el-form-item label="Nitrogen Content" prop="nitrogen">
          <el-input 
            type="number" 
            v-model.number="form.nitrogen"
            autocomplete="off" 
            placeholder="(%)"
            onKeypress="return (/[[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?]/.test(String.fromCharCode(event.keyCode)))">
          </el-input>
        </el-form-item>
        <el-form-item label="Phosphorus Content" prop="phosphorus">
          <el-input 
            type="number" 
            v-model.number="form.phosphorus" 
            autocomplete="off" 
            placeholder="(%)"
            onKeypress="return (/[[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?]/.test(String.fromCharCode(event.keyCode)))">
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
  import * as echarts from 'echarts'; 
  export default {
    name:"Soil",
    data() {
      return {
        tableData: [],
        total:0,
        currentPage:1,
        pageSize:5,
        form:{},
        multipleSelection: [],
        dialogFormVisible: false,

        rules: {
          date: [ {required: true, message: 'Please choose the date', trigger: 'blur'},],
          compaction: [{required: true, message: 'Please input the force to penetrate the soil', trigger: 'blur'},],
          soilTemperature: [{required: true, message: 'Please input the soil temperature', trigger: 'blur'},],
          ph: [{required: true, message: 'Please input the soil pH', trigger: 'blur'},],
          water: [{required: true, message: 'Please input the water content', trigger: 'blur'},],
          nitrogen: [{required: true, message: 'Please input the nitrogen content', trigger: 'blur'},],
          phosphorus: [{required: true, message: 'Please input the phosphorus content', trigger: 'blur'},],
        },

        chartDateVal:[],
        scoreChartDom:{},
        scoreChart:{},
        scoreOption:{},


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
        this.request.get("/soil/page", {
          params:{
            currentPage: this.currentPage,
            pageSize: this.pageSize,
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
            this.getChart()
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
        this.$refs['soilForm'].validate((valid) => {
            if (valid) {  // Form validation is valid
              this.request.post("/soil", this.form).then(res => {
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
        this.request.delete("/soil/" + id).then(res => {
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
        this.request.post("/soil/batchDel", ids).then(res => {
          if(res.code === 200){
            this.$message.success("Successfully deleted")
            this.loadPage()
          }else{
            this.$message.error("Failed to delete")
          }
        })
      },
      reset(){
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
        this.scoreChartDom = document.getElementById('score');
        this.scoreChart = echarts.init(this.scoreChartDom);

        this.scoreOption = {
          title: {
            text: 'Soil Condition Score Record',
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
            name: 'Score',
            type: 'value',
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
            { name: 'score', data: [], type: 'line'},
          ]
        };

        this.request.get("/soil/chart", {
            params:{
              beginDate: this.chartDateVal[0],
              endDate: this.chartDateVal[1],
            }
        }).then(res => {
          console.log(res)
          this.scoreOption.series[0].data = res.data.map(v => [v.date, v.score])
          this.scoreChart.setOption(this.scoreOption);

          this.$store.commit('GETSCORE', res.data[0].score)
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