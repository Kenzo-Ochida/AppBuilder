import { createRouter, createWebHistory } from "vue-router";
import HomeView from "../views/HomeView.vue";
import AssociationType from "../views/AssociationType.vue";
import Association from "../views/Association.vue";
import AssetType from "../views/AssetType.vue";
import DetailsRealm from "../views/DetailsRealm.vue";
import Users from "../views/Users.vue";
import Group from "../views/Group.vue";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/",
      name: "home",
      component: HomeView,
    },
    {
      path: "/about",
      name: "about",
      component: () => import("../views/AboutView.vue"),
    },
    {
      path: "/associationType",
      name: "associationType",
      component: AssociationType,
    },
    {
      path: "/association",
      name: "association",
      component: Association,
    },
    {
      path: "/assetType",
      name: "assetType",
      component: AssetType,
    },
    {
      path: "/detailsRealm",
      name: "detailsRealm",
      component: DetailsRealm,
    },
    {
      path: "/users",
      name: "users",
      component: Users,
    },
    {
      path: "/group",
      name: "group",
      component: Group,
    },
  ],
});

export default router;
