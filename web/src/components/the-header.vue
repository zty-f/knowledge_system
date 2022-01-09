<template>
  <a-layout-header class="header">
    <div class="logo">ZTY知识库</div>
    <a-popconfirm
        title="确认退出登录？"
        ok-text="是"
        cancel-text="否"
        @confirm="logout()"
    >
      <a class="login-menu" v-show="user.id">
        <span>退出登录</span>
      </a>
    </a-popconfirm>
    <a class="login-menu" v-show="user.id">
      <span>您好:{{ user.name }}</span>
    </a>
    <a class="login-menu" @click="showLoginModel" v-show="!user.id">
      <span>登录</span>
    </a>
    <a-menu
        theme="dark"
        mode="horizontal"
        v-model:selectedKeys="selectedKeys1"
        :style="{ lineHeight: '64px' }"
    >
      <a-menu-item key="/" >
        <router-link to="/">首页</router-link>
      </a-menu-item>

      <a-menu-item key="/about">
        <router-link to="/about">关于我们</router-link>
      </a-menu-item>

      <a-menu-item key="/admin/user" :style="user.id?{}:{display:'none'}">
        <router-link to="/admin/user">用户管理</router-link>
      </a-menu-item>

      <a-menu-item key="/admin/ebook" :style="user.id?{ }:{display:'none'}" >
        <router-link to="/admin/ebook">电子书管理</router-link>
      </a-menu-item>

      <a-menu-item key="/admin/category" :style="user.id?{ }:{display:'none'}" >
        <router-link to="/admin/category">分类管理</router-link>
      </a-menu-item>
    </a-menu>
  <a-modal
      title="登录"
      v-model:visible="loginModalVisible"
      :confirm-loading="loginModalLoading"
      @ok="login"
  >
    <a-form :model="loginUser" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }" :rules="rules">
      <a-form-item label="用户名">
        <a-input v-model:value="loginUser.loginName"/>  <!--加两个 ！可以跳过数据类型效验-->
      </a-form-item>
      <a-form-item label="密码" name="password">
        <a-input v-model:value="loginUser.password" type="password"/>
      </a-form-item>
    </a-form>
  </a-modal>
  </a-layout-header>
</template>

<script lang="ts">
import {computed, defineComponent, ref} from 'vue';
import axios from "axios";
import {message} from "ant-design-vue";
import store from "@/store";
declare let hexMd5:any;
declare let KEY:any;
export default defineComponent({
  name: 'the-header',
  setup(){
    // -------- 登录表单 ---------
    // 登录
    const loginUser = ref({
      loginName:'test',
      password:'test123'
    });
    // 登录后保存
    const user = computed(()=>store.state.user);
    const loginModalVisible = ref(false);
    const loginModalLoading = ref(false);
    const showLoginModel = () => {
      loginModalVisible.value = true;
    };
    /**
     * 登录
     */
    const login = () => {
      console.log("开始登录")
      loginModalLoading.value = true;
      // 前端md5加密
      loginUser.value.password = hexMd5(loginUser.value.password + KEY);
      axios.post("/user/login", loginUser.value).then((response) => {
        loginModalLoading.value = false;
        const data = response.data;
        if (data.success) {
          loginModalVisible.value = false;
          message.success("登录成功！");
          store.commit("setUser", data.content);
        } else {
          message.error(data.message);
        }
      });
    }
      /**
     * 退出登录
     */
    const logout = () => {
      console.log("开始退出登录")
      axios.get("/user/logout/"+ user.value.token).then((response) => {
        const data = response.data;
        if(data.success){
          message.success("退出登录成功！");
          store.commit("setUser",{});
        }else{
          message.error(data.message);
        }
      });
    };

    /**
     * 前端登录表单输入效验
     */
    const rules = {
      password: [
        { required: true, pattern: /^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,32}$/, message: '请输入正确的密码格式！',  trigger: 'change' }
      ],
    };

    return{
      loginModalVisible,
      loginModalLoading,
      loginUser,
      login,
      showLoginModel,
      user,
      logout,

      rules,
    }
  }
});
</script>

<style>
  .logo{
    width: 140px;
    height: 40px;
    float: left;
    color: white;
    font-size: 18px;
  }
 .login-menu{
  float: right;
  color: white;
   padding-left: 10px;
 }
</style>