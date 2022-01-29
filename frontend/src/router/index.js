import {createRouter, createWebHistory} from 'vue-router'
import Home from '../views/Home.vue'

const routes = [
    {
        path: '/',
        name: 'Home',
        component: Home
    },

    {
        path: '/sign_in',
        name: 'SignIn',
        component: function () {
            return import(/* webpackChunkName: "sign_in" */ '../views/User/SignIn')
        }
    },

    {
        path: '/sign_up',
        name: 'SignUp',
        component: function () {
            return import(/* webpackChunkName: "sign_up" */ '../views/User/SignUp')
        }

    },

    {
        path: '/travel/get',
        name: 'TravelsData',
        component: function () {
            return import(/* webpackChunkName: "about" */ '../views/Travels/TravelsData')
        }

    },

    {
        path: '/travel/add',
        name: 'AddTravel',
        component: function () {
            return import(/* webpackChunkName: "about" */ '../views/Travels/AddTravel')
        }

    },

    {
        path: '/travel/modify',
        name: 'Travels',
        component: function () {
            return import(/* webpackChunkName: "about" */ '../views/Travels/ModifyTravel')
        }
    },

]

const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes
})


router.beforeEach((to, from, next) => {
    const publicPages = ['/sign_up', '/sign_in', '/'];
    const authRequired = !publicPages.includes(to.path);
    const loggedIn = localStorage.getItem('user');

    // trying to access a restricted page + not logged in
    // redirect to login page
    if (authRequired && !loggedIn) {
        next('/sign_in');
    }
    next();
});

export default router
