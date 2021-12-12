<template>
  <a-layout>
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
      <p>
        <a-form layout="inline" :model="param">
          <a-form-item>
            <a-input-search
                v-model:value="param.loginName"
                placeholder="登录名"
                enter-button="搜索"
                size="large"
                @search="handleQuery({page:1,size:pagination.pageSize})"
            />
          </a-form-item>
          <a-button type="primary"  @click="add()" size="large">新增</a-button>
        </a-form>
      </p>
      <a-table
          :columns="columns"
          :data-source="users"
          :row-key="record => record.id"
          :pagination="pagination"
          :loading="loading"
          @change="handleTableChange"
      >
        <template v-slot:action="{ text,record }">
          <a-space size="small">

            <a-button type="primary"  @click="edit(record)">编辑</a-button>

            <a-popconfirm
                title="删除后不可恢复，确认删除？"
                ok-text="是"
                cancel-text="否"
                @confirm="handleDelete(record.id)"
            >
              <a-button danger >删除</a-button>
            </a-popconfirm>
          </a-space>
        </template>

      </a-table>

    </a-layout-content>
  </a-layout>
  <a-modal
      title="用户表单"
      v-model:visible="modalVisible"
      :confirm-loading="modalLoading"
      @ok="handleModalOk"
  >
    <a-form :model="user" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
      <a-form-item label="用户名">
        <a-input v-model:value="user.loginName" :disabled="!!user.id"/>  <!--加两个 ！可以跳过数据类型效验-->
      </a-form-item>
      <a-form-item label="昵称">
        <a-input v-model:value="user.name" />
      </a-form-item>
      <a-form-item label="密码">
        <a-input v-model:value="user.password" type="textarea" />
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref } from 'vue';
import axios from 'axios';
import {message} from "ant-design-vue";
import { Tool } from '@/util/tool';

declare let hexMd5:any;
declare let KEY:any;

export default defineComponent({
  name: 'AdminUser',
  setup() {
    const param = ref();
    param.value={};
    const users = ref();
    const pagination = ref({
      current: 1,
      pageSize: 4,
      total: 0
    });
    const loading = ref(false);

    const columns = [
      {
        title: '登录名',
        dataIndex: 'loginName'
      },
      {
        title: '昵称',
        dataIndex: 'name'
      },
      {
        title: '密码',
        dataIndex: 'password'
      },{
        title: 'Action',
        key: 'action',
        slots: {customRender: 'action'}
      }
    ];

    /**
     * 数据查询
     **/
    const handleQuery = (params: any) => {
      // // 如果不清空现有数据，则编辑保存重新加载数据后，再点编辑，则列表显示的还是编辑前的数据
      // users.value = [];  我的版本不用这句就可以动态刷新表单
      loading.value = true;
      axios.get("/user/list", {
        params:{
          page: params.page,
          size: params.size,
          loginName:param.value.loginName
        }
      }).then((response) => {
        loading.value = false;
        const data = response.data;
        if(data.success){
          users.value = data.content.list;

          // 重置分页按钮
          pagination.value.current = params.page;
          pagination.value.total = data.content.total;
        }else {
          message.error(data.message);
        }
      });
    };

    /**
     * 表格点击页码时触发
     */
    const handleTableChange = (pagination: any) => {
      console.log("看看自带的分页参数都有啥：" + pagination);
      handleQuery({
        page: pagination.current,
        size: pagination.pageSize
      });
    };

    // -------- 表单 ---------
    /**
     * 数组[100,101]对应前端开发/vue
     */
    const user = ref();
    const modalVisible = ref(false);
    const modalLoading = ref(false);
    const handleModalOk = () => {
      modalLoading.value = true;
      // 前端md5加密
      user.value.password = hexMd5(user.value.password+KEY);
      axios.post("/user/save", user.value).then((response) => {
        modalLoading.value = false;
        const data = response.data;
         if(data.success){
           modalVisible.value = false;
           // 重新查询
           handleQuery({
             page:pagination.value.current,
             size:pagination.value.pageSize
           });
         }else{
           message.error(data.message)
         }
      });
    };

    /**
     * 编辑
     */
    const edit = (record: any) => {
      modalVisible.value = true;
      user.value = Tool.copy(record);
    };

    /**
     * 新增
     */
    const add = () => {
      modalVisible.value = true;
      user.value = {};
    };

    /**
     * 删除
     */
    const handleDelete = (id:number) => {
      axios.delete("/user/delete/"+id).then((response) => {
        const data = response.data;
        if(data.success){
        handleQuery({
          page:pagination.value.current,
          size:pagination.value.pageSize
        });
        }
      })
    };


    onMounted(() => {
        handleQuery({
          page:1,
          size:pagination.value.pageSize,
        });
    });

    return {
      users,
      pagination,
      columns,
      loading,
      param,

      handleTableChange,
      edit,
      add,
      handleDelete,
      handleModalOk,
      handleQuery,

      user,
      modalVisible,
      modalLoading,
    }
  }
});
</script>

<style>
.coverImage {
  vertical-align: middle;
  border-style: none;
  width: 80px;
  height: 80px;
}
</style>