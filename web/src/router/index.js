import Vue from 'vue'
import VueRouter from 'vue-router'
import Container from '../views/Container.vue'
import Market from '../views/Market.vue'
import Home from '../views/Home.vue'
import Login from '../views/Login.vue'
import Register from '../views/Register.vue'
import Person from '../views/Person.vue'
import Role from '../views/Role.vue'
import Menu from '../views/Menu.vue'
import User from '../views/User.vue'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    component: Container,
    redirect:"/home",
    children:[
      { path: 'home', name: 'Home', component: Home, meta:{ name:'Home'}},
      { path: 'market', name: 'Market', component: Market, meta:{ name:'Market'}},
      { path: 'person', name: 'Person', component: Person, meta:{ name:'User information'}},
      { path: 'role', name: 'Role', component: Role, meta:{ name:'Role Management'}},
      { path: 'menu', name: 'Menu', component: Menu, meta:{ name:'Menu Management'}},
      { path: 'user', name: 'User', component: User, meta:{ name:'User Management'}},
    ]
  },
  {
    path: '/login',
    name: 'Login',
    component: Login,
    
  },
  {
    path: '/register',
    name: 'Register',
    component: Register,
    
  },
  
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
