import { createWebHistory, createRouter } from "vue-router";
import { RouteRecordRaw } from "vue-router";

import LoginPage from '@/pages/LoginPage.vue'

const routes: Array<RouteRecordRaw> = [
  {
    path: "/",
    name: "home",
    component: () => import("./components/Home.vue"),
    props: true
  },
  {
    path: "/MyGallery",
    name: "MyGallery",
    component: () => import("./components/MyGallery.vue"),
    props: true
  },
  {
    path: "/PublicGallery",
    name: "PublicGallery",
    component: () => import("./components/PublicGallery.vue"),
    props: true
  },
  {
    path: "/image/:id",
    name: "image",
    component: () => import("./components/Image.vue"),
    props: ({ params }) => ({ id: Number(params.id) || 0 })
  },
  {
    path: "/imagePublic/:id",
    name: "imagePublic",
    component: () => import("./components/ImagePublic.vue"),
    props: ({ params }) => ({ id: Number(params.id) || 0 })
  },
  {
    path: "/upload",
    name: "upload",
    component: () => import("./components/Upload.vue"),
    props: true
  },
  {
    path: "/filterIncreaseLuminosity/:id",
    name: "filterIncreaseLuminosity",
    component: () => import("./components/filterIncreaseLuminosity.vue"),
    props: ({ params }) => ({ id: Number(params.id) || 0 })
  },
  {
    path: "/filterHistogramEqualize/:id",
    name: "filterHistogramEqualize",
    component: () => import("./components/filterHistogramEqualize.vue"),
    props: ({ params }) => ({ id: Number(params.id) || 0 })
  },
  {
    path: "/filterColorize/:id",
    name: "filterColorize",
    component: () => import("./components/filterColorize.vue"),
    props: ({ params }) => ({ id: Number(params.id) || 0 })
  },
  {
    path: "/filterFlou/:id",
    name: "filterFlou",
    component: () => import("./components/filterFlou.vue"),
    props: ({ params }) => ({ id: Number(params.id) || 0 })
  },
  {
    path: "/filterSobel/:id",
    name: "filterSobel",
    component: () => import("./components/filterSobel.vue"),
    props: ({ params }) => ({ id: Number(params.id) || 0 })
  },
  {
    path: "/chooseFilter",
    name: "chooseFilter",
    component: () => import("./components/chooseFilter.vue"),
    props: true
   
  },
  {
    path: "/register",
    name: "register",
    component: () => import("./components/Register.vue"),
    props: true
  },
  {
    path: "/showUser",
    name: "showUser",
    component: () => import("./components/showUser.vue"),
    props: true
  },
  {
    path: "/loginPage",
    name: "loginPage",
    component: () => import("./components/LoginPage.vue"),
    props: true
 
  },
  {
    path: "/myProfile",
    name: "myProfile",
    component: () => import("./components/myProfile.vue"),
    props: true
 
  },
  {
    path: "/deconexion",
    name: "deconexion",
    component: () => import("./components/Deconexion.vue"),
    props: true
 
  },
  {
    path: "/WinCoins",
    name: "WinCoins",
    component: () => import("./components/WinCoins.vue"),
    props: true
 
  },
  {
    path: "/showDeletedUser",
    name: "showDeletedUser",
    component: () => import("./components/showDeletedUser.vue"),
    props: true
 
  },
  {
    path: "/ranking",
    name: "ranking",
    component: () => import("./components/ranking.vue"),
    props: true
    
 
  },
  {
    path: "/ranking",
    name: "ranking",
    component: () => import("./components/ranking.vue"),
    props: true
    
 
  },
  {
    path: "/checkProfil/:idU",
    name: "checkProfil",
    component: () => import("./components/checkProfil.vue"),
    props: ({ params }) => ({ idU: Number(params.idU) || 0 })
    
 
  }   

  

];


const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;