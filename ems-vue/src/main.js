import { createApp } from 'vue'
import './style.css'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import * as Icons from '@element-plus/icons-vue'
import { createPinia } from 'pinia'
import router from './router'
import App from './App.vue'
import permissionDirective from './directive/permission'

const app = createApp(App)
app.use(ElementPlus)
app.use(createPinia())
app.use(router)
app.directive('permission', permissionDirective)
for (const [name, comp] of Object.entries(Icons)) app.component(name, comp)
app.mount('#app')
