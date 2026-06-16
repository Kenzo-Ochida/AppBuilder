import { createRouter, createWebHistory } from "vue-router";
import axios from "axios";
import Login from "../views/Login.vue";
import Logout from "../views/Logout.vue";
import Home from "../views/Home.vue";

const routes = [
  { path: "/", component: Login },
  {
    path: "/home",
    component: Home,
    meta: { requiresAuth: true },
  },
  {
    path: "/logout",
    component: Logout,
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

router.beforeEach(async (to, from, next) => {
  if (to.meta.requiresAuth) {
    try {
      await axios.get("http://localhost:8090/auth/session", {
        withCredentials: true, // essencial para enviar o JSESSIONID
      });
      next();
    } catch (err) {
      window.location.href = "/";
    }
  } else {
    next();
  }
});

export default router;
