<template>
  <div>
    <div style="font-size: 8px; color: red; margin-bottom: 10px;">If you are a Chinese user, please use a VPN to view</div>
    <el-row>
      <el-col :span="14">
        <el-card style="width: 100%;">
          <vue-weather
            api-key="e726bc58550e9da951ceede76403afa7"
            units="uk"
            latitude="53.382349"
            longitude="-1.481200"
          />
        </el-card>
      </el-col>
      <el-col :span="9" style="margin-left: 20px;">
        <el-card>
          {{$store.state.curScore}}
        </el-card>
      </el-col>
    </el-row>
      
    <div id="map" style="width: 100%; height: 500px; margin-top: 15px;"></div>
  </div>
</template>

<script>
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
      };
    },
 
    mounted(){
      setTimeout(() => {
        this.initMap();
      }, 0)
    },
  
    methods: {
      initMap() {
        this.icons = {
          farm: {
            icon: "http://rf6jnyisc.bkt.clouddn.com/2022/08/0578e7e2322fd44c289a4ba09823306018.png",
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
    },
  }
  
</script>

<style scoped>
</style>
