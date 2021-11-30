<template>
  <a-layout>
    <a-layout-sider width="200" style="background: #fff">
      <a-menu
          mode="inline"
          :style="{ height: '100%', borderRight: 0 }"
          @click="handleClick"
      >
        <a-menu-item key="welcome">
          <router-link :to="'/'">
            <MailOutlined />
            <span>欢迎</span>
          </router-link>
        </a-menu-item>
        <a-sub-menu v-for="item in level1" :key="item.id" >
          <template v-slot:title>
            <span><user-outlined />{{item.name}}</span>
          </template>
          <a-menu-item v-for="child in item.children" :key="child.id">
            <MailOutlined /><span>{{child.name}}</span>
          </a-menu-item>
        </a-sub-menu>
        <!--        <a-menu-item key="tip" :disabled="true">-->
        <!--          <span>以上菜单在分类管理配置</span>-->
        <!--        </a-menu-item>-->
      </a-menu>
  </a-layout-sider>

    <a-layout-content
      :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
  >
      <a-list item-layout="vertical" size="large" :grid="{ gutter:20,column:3}"
               :pagination="pagination" :data-source="ebooks">
        <template #renderItem="{ item }">
          <a-list-item key="item.name">
            <template #actions>
                <span v-for="{ type, text } in actions" :key="type">
                  <component v-bind:is="type" style="margin-right: 8px" />
                  {{ text }}
                </span>
            </template>

            <a-list-item-meta :description="item.description">
              <template #title>
                <a :href="item.href">{{ item.name }}</a>
              </template>
              <template #avatar><a-avatar :src="item.cover" /></template>
            </a-list-item-meta>
          </a-list-item>
        </template>
      </a-list>
  </a-layout-content>
  </a-layout>
</template>

<script lang="ts">
import { defineComponent,onMounted,ref,reactive,toRef} from 'vue';
import axios from 'axios';
import {message} from "ant-design-vue";
import {Tool} from "@/util/tool";

export default defineComponent({
  name: 'Home',
  setup(){
    console.log("setup");
    const ebooks = ref();
    // const ebooks1 = reactive({books:[]});
    const level1 = ref(); //一级分类树，children属性就是二级分类
    let categorys:any;
    /**
     * 查询所有分类
     **/
    const handleQueryCategory = () => {
      axios.get("/category/all").then((response) => {
        const data = response.data;
        if(data.success){
          categorys = data.content;
          console.log("原始数组：",categorys);
          level1.value = [];
          level1.value = Tool.array2Tree(categorys,0);
          console.log("树形结构：",level1);
        }else {
          message.error(data.message);
        }
      });
    };

    const handleClick = ()=>{
      console.log("菜单点击");
    };

    //页面初始时调用
    onMounted(()=>{
      handleQueryCategory();
      axios.get("/ebook/all")
          .then((response)=> {
            const data = response.data;
            ebooks.value = data.content;
          })
    });

    return{
      ebooks,
      // ebooks2:toRef(ebooks1,"books"),
      //分页功能
      pagination : {
        onChange: (page: any) => {
          console.log(page);
        },
        pageSize: 6,
      },
      actions:  [
      { type: 'StarOutlined', text: '156' },
      { type: 'LikeOutlined', text: '156' },
      { type: 'MessageOutlined', text: '2' },
    ],
     handleClick,
      level1,
    }
  }
});
</script>


<style scoped> /* scoped表示在当前页面生效*/
  .ant-avatar {
    width: 50px;
    height: 50px;
    line-height: 50px;
    border-radius: 8%;
    margin: 5px 0;
  }
</style>
