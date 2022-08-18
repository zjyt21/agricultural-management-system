<template>
  <div>
    <div style="font-size: 8px; color: red; margin-bottom: 10px;">The map uses google map api, so if you are a Chinese user, please use a VPN to view</div>
    <el-row>
      <el-col :span="13">
        <el-card style="width: 100%;">
          <vue-weather
            api-key="e726bc58550e9da951ceede76403afa7"
            units="uk"
            latitude="53.382349"
            longitude="-1.481200"
          />
        </el-card>
      </el-col>
      <el-col :span="1">
        <div style="height: 270px;"></div>
      </el-col>
      <el-col :span="10" >
        <el-card style="height: 268px;">
        <div id="score" style="height: 270px;">

        </div>
      </el-card>
      </el-col>
    </el-row>
      
    <div id="map" style="width: 100%; height: 500px; margin-top: 15px;"></div>
  </div>
</template>

<script>
  import * as echarts from 'echarts'; 
  import { mapState } from 'vuex';
  import VueWeather from "vue-weather-widget";
  export default {
    name: "Home",
    components: {
      VueWeather,
    },
    data() {
      return {
        icons:{},
        features:[],
        center: { lat: 53.382349, lng: -1.481200 },
        map:null,
        contentString:'',
        infowindow:{},
        marker:{},

        scoreChartDom:{},
        scoreChart:{},
        scoreOption:{},
      };
    },

    computed:{
      ...mapState(['curScore'])
    },
 
    mounted(){
      setTimeout(() => {
        this.initMap();
      }, 0)
      this.getScoreChart()
    },
  
    methods: {
      initMap() {
        this.icons = {
          farm: {
            icon: "http://rgto2ucce.bkt.clouddn.com/2022/08/18b51117b8e0134d988c667bf44eaa3cbb.png",
          },
        };
        this.features = [
          {
            position: new google.maps.LatLng(53.382349, -1.481200),
            type: "farm",
          }
        ];
        this.map = new google.maps.Map(document.getElementById("map"), {
          zoom: 18,
          center: this.center,
        });

        this.contentString =
          '<div id="content">' +
          '<div id="siteNotice">' +
          "</div>" +
          '<h1 id="firstHeading" class="firstHeading">JLGL Farm</h1>' +
          '<div id="bodyContent">' +
          "<p><b>Welcome to JLGL Farm, we are located in Sheffield, UK, glad you could come and visit </b><br>" +
          "<b>Our Position: </b> S14NP, right next to diamond</p>" + 
          "</div>" +
          "</div>";
          
        this.infowindow = new google.maps.InfoWindow({
          content: this.contentString,
        });
        this.marker = new google.maps.Marker({
          position: this.features[0].position,
          icon: this.icons[this.features[0].type].icon,
          map: this.map,
        });
        
        this.marker.addListener("click", () => {
          this.infowindow.open({
            anchor: this.marker,
            map:this.map,
            shouldFocus: false,
          });
        });
      },
      getScoreChart(){
        this.scoreChartDom = document.getElementById('score');
        this.scoreChart = echarts.init(this.scoreChartDom);

        this.scoreOption = {
          series: [
            {
              type: 'gauge',
              center: ["50%", "70%"],
              radius:'120%',
              startAngle: 180,
              endAngle: 0,
              min: 0,
              max: 1,
              splitNumber: 10,
              axisLine: {
                lineStyle: {
                  width: 6,
                  color: [
                    [0.25, '#FF6E76'],
                    [0.5, '#FDDD60'],
                    [0.75, '#58D9F9'],
                    [1, '#7CFFB2']
                  ]
                }
              },
              pointer: {
                icon: 'path://M12.8,0.7l12,40.1H0.7L12.8,0.7z',
                length: '15%',
                width: 20,
                offsetCenter: [0, '-60%'],
                itemStyle: {
                  color: 'auto'
                }
              },
              axisTick: {
                length: 12,
                lineStyle: {
                  color: 'auto',
                  width: 2
                }
              },
              splitLine: {
                length: 20,
                lineStyle: {
                  color: 'auto',
                  width: 5
                }
              },
              axisLabel: {
                color: '#464646',
                fontSize: 18,
                distance: -80,
                formatter: function (value) {
                  if (value === 0.9) {
                    return 'Perfect';
                  } else if (value === 0.7) {
                    return 'Good';
                  } else if (value === 0.3) {
                    return 'So so';
                  } else if (value === 0.1) {
                    return 'Bad';
                  }
                  return '';
                }
              },
              title: {
                offsetCenter: [0, '-20%'],
                fontSize: 20
              },
              detail: {
                fontSize: 30,
                offsetCenter: [0, '0%'],
                valueAnimation: true,
                formatter: function (value) {
                  return Math.round(value * 100) + '%';
                },
                color: 'auto'
              },
              data: [
                {
                  value: this.curScore / 100,
                  name: 'Soil Condition'
                }
              ]

            }
          ]
        };
        this.scoreChart.setOption(this.scoreOption);
      }
    },
  }
  
</script>

<style scoped>
</style>
