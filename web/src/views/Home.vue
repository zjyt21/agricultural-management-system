<template>
  <div>
    <el-row :gutter="10" style="margin-bottom: 40px;">
      <el-col :span="12">
        <div id="price" style="widows: 500px; height: 400px;"></div>
      </el-col>
      <el-col :span="12">
          <div id="trend" style="widows: 500px; height: 400px;"></div>
      </el-col>
    </el-row>
    
    
  </div>
</template>

<script>
  import * as echarts from 'echarts'; 
  export default {
    name:"Home",
    data() {
      return {

      }
    },
    mounted() {
      var priceChartDom = document.getElementById('price');
      var priceChart = echarts.init(priceChartDom);

      var priceOption = {
        title: {
          text: 'Unit Price',
          left: 'center'
        },
        tooltip: {
          trigger: 'item'
        },
        legend: {
          orient: 'vertical',
          left: 'left'
        },
        toolbox: {
            show: true,
            //toolbox的配置项
            feature: {
                //辅助线的开关
                mark: { show: true },
                //数据视图
                dataView: {
                    show: true,
                    readOnly: false
                },
                //动态类型切换
                magicType: {
                    show: true,
                    //line为折线图，bar为柱状图
                    type: ['line', 'bar']
                },
                //配置项还原
                restore: { show: true },
                //将图标保存为图片
                saveAsImage: { show: true }
            }
        },
        calculable: true,
        xAxis: {
          name: 'Date',
          type: 'category',
          //数值起始和结束两端空白策略
          boundaryGap: false,
          data: []
        },
        yAxis: {
          name: 'Price',
          type: 'value',
          interval: 300,
          min: 3300,
          max: 4500,
        },
        series: [
          {
            data: [],
            type: 'line'
          }
        ]
      };
      this.request.get("/market-trend/page", {
        params:{
          currentPage: 1,
          pageSize: 20,
          crop: 'paddy',
        }
      }).then(res => {
        var arr = new Array(res.data.records.length);
        var arr2 = new Array(res.data.records.length);
        for(var i = 0; i < arr.length; i++){
          arr[i] = res.data.records[i].unitPrice;
          arr2[i] = res.data.records[i].date;
        }
        priceOption.series[0].data = arr;
        priceOption.xAxis.data = arr2;
        priceChart.setOption(priceOption);

      })
      
    },
  }
</script>

<style>

</style>