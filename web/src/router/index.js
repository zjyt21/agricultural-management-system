import Vue from 'vue'
import VueRouter from 'vue-router'
import Container from '../views/Container.vue'
import Market from '../views/Market.vue'
import Home from '../views/Home.vue'
import Login from '../views/Login.vue'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    component: Container,
    redirect:"/home",
    children:[
      {
        path: 'home',
        name: 'Home',
        component: Home,
      },
      {
        path: 'market',
        name: 'Market',
        component: Market,
      },
    ]
  },
  {
    path: '/login',
    name: 'Login',
    component: Login,
    
  },
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
