<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
      <el-form-item label="模型名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入模型名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['activiti:model:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleEdit"
          v-hasPermi="['activiti:model:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDel"
          v-hasPermi="['activiti:model:remove']"
        >删除</el-button>
      </el-col>
    </el-row>


    <div :style="pageStyle">
      <el-table  v-loading="loading" :data="tableList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column label="序号" type="index" width="50" align="center">
          <template slot-scope="scope">
            <span>{{(queryParams.pageNum - 1) * queryParams.pageSize + scope.$index + 1}}</span>
          </template>
        </el-table-column>
        <el-table-column
          label="模型id"
          align="center"
          prop="id"
          :show-overflow-tooltip="true"
          min-width="100"
        />
        <el-table-column
          label="模型标识"
          align="center"
          prop="key"
          :show-overflow-tooltip="true"
          min-width="180"
        />
        <el-table-column
          label="模型名称"
          align="center"
          prop="name"
          :show-overflow-tooltip="true"
          min-width="250"
        />
        <el-table-column
          label="版本号"
          align="center"
          prop="version"
          :show-overflow-tooltip="true"
          min-width="130"
        />
        <el-table-column
          label="创建时间"
          align="center"
          prop="createTime"
          :formatter="formatDate"
          min-width="180"
        />
        <el-table-column
          label="最后更新时间"
          align="center"
          prop="lastUpdateTime"
          min-width="180"
          :formatter="formatDate"
        />

        <el-table-column label="操作"
                         align="center"
                         class-name="small-padding fixed-width"
                         min-width="250"
        >
          <template slot-scope="scope">
            <el-button
              type="text"
              size="small"
              icon="el-icon-download"
              @click="handleExport(scope.row)"
              v-hasPermi="['activiti:model:export']"
            >导出</el-button>
            <el-button
              type="text"
              size="small"
              icon="el-icon-upload2"
              @click="handleDeploy(scope.row)"
              v-hasPermi="['activiti:model:deploy']"
            >部署</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 弹窗, 新增 -->
    <detail-model  ref="detail" @close="handleQuery"></detail-model>
  </div>
</template>

<style scoped>
</style>

<script>
  import DetailModel from './model'
  import {addModel, listTable,delModel,depModel} from "@/api/activiti/model";
  import {formatDate} from "@/utils/index";
  import { downLoadZip } from "@/utils/zipdownload";

  export default {
    data () {
      return {
        // 遮罩层
        loading: true,
        // 唯一标识符
        uniqueId: "",
        // 选中数组
        ids: [],
        // 选中表数组
        names: [],
        // 非单个禁用
        single: true,
        // 非多个禁用
        multiple: true,
        // 总条数
        total: 0,
        // 表数据
        tableList: [],
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          name: undefined,
        },
        pageStyle:{
          overflow: 'auto',
          height: '',
        },
      }
    },
    mounted(){
      window.addEventListener('resize', this.setSize);
      this.setSize();
    },
    components: {
      DetailModel
    },
    created() {
      this.getList();
    },
    activated() {
      const time = this.$route.query.t;
      if (time != null && time != this.uniqueId) {
        this.uniqueId = time;
        this.resetQuery();
      }
    },
    methods: {
      /*日期格式化*/
      formatDate: function(row, column, cellValue){
        return formatDate(cellValue);
      },
      /*table窗口高度*/
      setSize(){
        this.pageStyle.height=window.innerHeight-280+'px';
      },
      /** 查询表集合 */
      getList() {
        this.loading = true;
        listTable(this.queryParams)
          .then(response => {
            this.tableList = response.rows;
            this.total = response.total;
            this.loading = false;
          })
          .catch(function (err) {
            this.loading = false;
          });
      },
      /** 搜索按钮操作 */
      handleQuery() {
        this.queryParams.pageNum = 1;
        this.getList();
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.resetForm("queryForm");
        this.handleQuery();
      },
      /** 多选 */
      handleSelectionChange (selection) {
        this.ids = selection.map(item => item.id);
        this.names = selection.map(item => item.name);
        this.single = selection.length != 1;
        this.multiple = !selection.length;
      },
      /*创建新流程模型*/
      handleAdd(){
        addModel().then(res => {
          this.$refs.detail.showModel(res.data);
        });
      },
      /*编辑流程模型*/
      handleEdit () {
        let data={};
        data.id=this.ids[0];
        data.name=this.names[0];
        this.$refs.detail.showModel(data);
      },
      /*删除流程模型*/
      handleDel () {
        const ids = this.ids;
        this.$confirm('请确认是否删除选中的模型?', '警告', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }
        ).then(function() {
          return delModel(ids);
        }).then((res) => {
          this.getList();
          this.msgSuccess(res.msg);
        }).catch(function() {});
      },
      /*导出流程模型文件*/
      handleExport (row){
        downLoadZip("/activiti/model/export/" + row.id);
      },
      /*当前系统部署流程*/
      handleDeploy (row) {
        this.$confirm('确认在当前系统部署ID为"' + row.id + '"的模型?', '警告', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(function () {
          return depModel(row.id);
        }).then((res) => {
          this.getList();
          this.msgSuccess(res.msg);
        }).catch(function (err) {
        })
      },

    }
  }
</script>
