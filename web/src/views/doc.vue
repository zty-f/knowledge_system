<template>
  <a-layout>
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
      <h3 v-if="level1.length===0">对不起，找不到文档内容或当前电子书还没有文档内容！</h3>
      <a-row>
        <a-col :span="6">
          <a-tree
              v-if="level1.length>0"
              :tree-data="level1"
              @select="onSelect"
              :replaceFields="{title:'name', key:'id', value:'id'}"
              :defaultExpandAll="true"
              :defaultSelectedKeys="defaultSelectedKeys"
          />
        </a-col>
        <a-col :span="18">
          <div>
            <h2>{{doc.name}}</h2>
            <div>
              <span>阅读数:{{doc.viewCount}}</span>&nbsp;&nbsp;
              <span>点赞数:{{doc.voteCount}}</span>
            </div>
            <a-divider style="height: 2px; background-color: #9999cc"/>
          </div>
           <div class="wangeditor" :innerHTML="html"></div>
        </a-col>
      </a-row>
    </a-layout-content>
  </a-layout>
</template>

<script lang="ts">



import { defineComponent, onMounted, ref} from 'vue';
import axios from 'axios';
import {message, Modal} from "ant-design-vue";
import { Tool } from '@/util/tool';
import {useRoute} from "vue-router";

export default defineComponent({
  name: 'Doc',
  setup() {
    const route = useRoute();
    const docs = ref();
    const html = ref();
    const defaultSelectedKeys = ref();
    defaultSelectedKeys.value = [];

    //当前选中的文档
    const doc = ref();
    doc.value={};

    /**
     *  一级文档树，children属性就是二级文档
     *  [{
     *      id:""
     *      name:""
     *      children:[{
     *          id:""
     *          name:""
     *          ]}
     *  }]
     **/
    const level1 = ref(); //一级文档树，children属性就是二级文档
    level1.value = [];

    /**
     * 文档内容查询
     * */
    const handleQueryContent = (id:number) => {
      axios.get("/doc/findContent/"+id).then((response) => {
        const data = response.data;
        if(data.success){
          html.value = data.content;
        }else {
          message.error(data.message);
        }
      });
    };

    /**
     * 文档doc数据查询
     **/
    const handleQuery = () => {
      axios.get("/doc/all/"+route.query.ebookId).then((response) => {
        const data = response.data;
        if(data.success){
          docs.value = data.content;
          level1.value = [];
          level1.value = Tool.array2Tree(docs.value,0);

          if(Tool.isNotEmpty(level1)){
            defaultSelectedKeys.value = [level1.value[0].id];
            handleQueryContent(defaultSelectedKeys.value);
            // 初始显示文档信息
            doc.value = level1.value[0];
          }
        }else {
          message.error(data.message);
        }
      });
    };

    const onSelect = (selectKeys:any,info:any)=>{
      console.log('selected',selectKeys,info);
      if(Tool.isNotEmpty(selectKeys)){
        // 选中某个节点，加载该节点的文档信息
        doc.value = info.selectedNodes[0].props;
        //加载内容
        handleQueryContent(selectKeys[0]);
      }
    };


    onMounted(() => {
      handleQuery();
    });

    return {
      level1,
      html,
      onSelect,
      defaultSelectedKeys,
      doc,
    }
  }
});
</script>

<style> /*全局样式*/
/* wangeditor默认样式*/
/* table样式*/
.wangeditor table {
  border-top: 1px solid #ccc;
  border-left: 1px solid #ccc;
}
.wangeditor table td,
.wangeditor table th {
  border-bottom: 1px solid #ccc;
  border-right: 1px solid #ccc;
  padding: 3px 5px;
}
.wangeditor table th {
  border-bottom: 2px solid #ccc;
  text-align: center;
}

/* blockquote 样式 */
.wangeditor blockquote {
  display: block;
  border-left: 8px solid #d0e5f2;
  padding: 5px 10px;
  margin: 10px 0;
  line-height: 1.4;
  font-size: 100%;
  background-color: #f1f1f1;
}

/* code 样式 */
.wangeditor code {
  display: inline-block;
  *display: inline;
  *zoom: 1;
  background-color: #f1f1f1;
  border-radius: 3px;
  padding: 3px 5px;
  margin: 0 3px;
}
.wangeditor pre code {
  display: block;
}

/* ul ol 样式 */
.wangeditor ul, ol {
  margin: 10px 0 10px 20px;
}

/* 和antdesignvue p冲突，覆盖掉*/
.wangeditor blockquote p{
  font-family: "YouYuan";
  margin: 20px 10px !important;
  font-size: 16px !important;
  font-weight: 600;
}
</style>