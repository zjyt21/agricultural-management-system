import Vue from 'vue'
import App from './App.vue'
import router from './router'
import ElementUI from 'element-ui';
import locale from 'element-ui/lib/locale/lang/en'
import 'element-ui/lib/theme-chalk/index.css';
import './assets/global.css'
import request from './util/request';
import i18n from './util/locale'
import store from './store'

import * as VueGoogleMaps from "vue2-google-maps" // Import package
Vue.use(VueGoogleMaps, {
  load: {
    key: "AIzaSyBpAr7_pXelgE3kw1WHDW37LgGjG6hiPgU",
    libraries: "places"
  }
});

Vue.use(ElementUI, {size:"mini"}, { locale })
Vue.config.productionTip = false
Vue.prototype.request = request

new Vue({
  router,
  store,
  i18n,
  render: h => h(App)
}).$mount('#app')
