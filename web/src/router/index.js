import Vue from 'vue'
import VueRouter from 'vue-router'
import Container from '../views/Container.vue'
import Home from '../views/Home.vue'
import Login from '../views/Login.vue'
import Register from '../views/Register.vue'
import NotFound from '../views/NotFound.vue'
import Person from '../views/Person.vue'


Vue.use(VueRouter)

const routes = [
  {path: '/login', name: 'Login', component: Login,},
  {path: '/register', name: 'Register', component: Register,},
  {path: '/notFound', name: 'NotFound', component: NotFound,},
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

// 提供一个重置路由的方法
export const resetRouter = () => {
  router.matcher = new VueRouter({
    mode: 'history',
    base: process.env.BASE_URL,
    routes
  })
}

export const setRoutes = () => {
  const storeMenus = localStorage.getItem("menus");
  if(storeMenus){
    
    const currentRoutes = router.getRoutes().map(v => v.name)
    if(!currentRoutes.includes('Container')){
      //dynamic route
      const manageRoute = { path: '/', name:'Container', component: Container, redirect:"/home", 
        children:[
          { path:'person', name:'User Info', component: Person, meta:{ name: 'User Information'},},
        ]}
      const menus = JSON.parse(storeMenus)
      menus.forEach(item => {
        if(item.path){
          let itemMenu = { path: item.path.replace("/", ""), name: item.pagePath, component:() => import('../views/' + item.pagePath + '.vue'), meta:{ name: item.name}}
          manageRoute.children.push(itemMenu)
        }else if(item.children.length){
          item.children.forEach(subItem => {
            if(subItem.path){
              let subItemMenu = { path: subItem.path.replace("/", ""), name: subItem.pagePath, component:() => import('../views/' + subItem.pagePath + '.vue'), meta:{ name: subItem.name}}
              manageRoute.children.push(subItemMenu)
            }  
          })
        }
      });
      router.addRoute(manageRoute)
    }
    
  }
}

//reset routes
setRoutes()

router.beforeEach((to, from, next) => {
  const storeMenus = localStorage.getItem("menus")
  if (!to.matched.length) {
    if(storeMenus){
      next("/notFound")
    }else{
      next("/login")
    }
  }
  next()
})
export default router
