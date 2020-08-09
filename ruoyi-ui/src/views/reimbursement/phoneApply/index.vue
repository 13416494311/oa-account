<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm"  label-width="68px">
      <el-row :gutter="50">
        <el-col :span="8">
          <el-form-item label="报销类型" prop="type">
            <el-select v-model="queryParams.type" style="width:100%"
                       placeholder="请选择报销类型" clearable size="small">
              <el-option
                v-for="dict in typeOptions"
                :key="dict.dictValue"
                :label="dict.dictLabel"
                :value="dict.dictValue"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="报销事由" prop="reason">
            <el-input
              v-model="queryParams.reason"
              placeholder="请输入报销事由"
              clearable
              size="small"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="审核状态" prop="auditState">
            <el-select v-model="queryParams.auditState" style="width:100%"
                       placeholder="请选择审核状态" clearable size="small">
              <el-option
                v-for="dict in auditStateOptions"
                :key="dict.dictValue"
                :label="dict.dictLabel"
                :value="dict.dictValue"
              />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="50">
        <el-col :span="16">
          <div>
            <el-form-item label="关键字" prop="keyWord">
              <el-input
                v-model="queryParams.keyWord"
                placeholder="请输入报销人、部门、课题、发票号、报销事由、经办人关键字"
                clearable
                size="small"
                @keyup.enter.native="handleQuery"
              />
            </el-form-item>
          </div>
        </el-col>
        <el-col :span="8">
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
            <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <!--<el-button
          type="primary"
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
        >新增
        </el-button>-->
        <el-dropdown>
          <el-button
            type="primary"
            size="mini">
            新增<i class="el-icon-arrow-down el-icon&#45;&#45;right"></i>
          </el-button>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item
              v-for="dict in typeOptions"
              :key="dict.dictValue"
              @click.native="handleAdd(dict.dictValue)"
            >{{dict.dictLabel}}</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          icon="el-icon-upload2"
          size="mini"
          :disabled="multiple"
          @click="handleSubmit"
        >提交</el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="applyList"
              @selection-change="handleSelectionChange"
              @row-dblclick="handleSee"
              :show-summary = "showSummary"
              :summary-method="getTotal"
              ref="applyTable"
    >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="报销事由" show-overflow-tooltip align="center" prop="reason"/>
      <el-table-column label="报销类型" align="center" prop="type" :formatter="typeFormat"/>
      <el-table-column label="报销人" align="center" prop="reimburser"/>
      <el-table-column label="部门" align="center" prop="deptName"/>
      <el-table-column label="课题" align="center" prop="topic"/>
      <el-table-column label="经办人" align="center" prop="operator"/>
      <el-table-column label="报销总金额" align="center" prop="totalMoney" :formatter="feeFormat"/>
      <el-table-column label="审核状态" align="center" prop="auditState" :formatter="auditStateFormat"/>
      <el-table-column label="操作"  align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <!--审核通过的不能修改 其他情形都可以修改-->
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            v-if="scope.row.auditState != '2'"
            @click="handleUpdate(scope.row,'edit')"
          >修改
          </el-button>
          <!--审核通过和审核中的不能修改 其他情形都可以修改-->
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            v-if="scope.row.auditState != '1'&&scope.row.auditState != '2'"
            @click="handleDelete(scope.row)"
          >删除
          </el-button>
          <!--审核中的可以审核-->
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit-outline"
            v-if="scope.row.auditState == '1'"
            v-hasPermi="['reimbursement:apply:audit']"
            @click="handleUpdate(scope.row,'audit')"
          >审核
          </el-button>
          <!--审核通过查看-->
          <el-button
            size="mini"
            type="text"
            icon="el-icon-search"
            v-if="scope.row.auditState == '2'"
            @click="handleSee(scope.row)"
          >查看
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />
    <reimbursement-apply ref="reimbursement" @ok="handleQuery"/>
  </div>
</template>


<script>
  import reimbursementApply from "./reimbursementApply";
  import {listApply, getApply, delApply, submitApply ,
    addApply, updateApply, exportApply} from "@/api/reimbursement/apply";

  export default {
    name: "ApplyIndex",
    components: {
      reimbursementApply
    },
    data() {
      return {
        //显示总计
        showSummary: false,
        // 遮罩层
        loading: true,
        // 选中数组
        ids: [],
        // 非单个禁用
        single: true,
        // 非多个禁用
        multiple: true,
        // 总条数
        total: 0,
        // 报销申请表格数据
        applyList: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 报销类型字典
        typeOptions: [],
        // 审核状态字典
        auditStateOptions: [],
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          type: undefined,
          reason: undefined,
          auditState: undefined,
          keyWord: undefined,
        },
        // 表单参数
        form: {},
        // 表单校验
        rules: {},
        bodyStyle: {
          overflowY: 'auto',
          height: '',
          marginLeft: '20%',
          paddingRight: '20%',
        },
      };
    },
    mounted() {
      window.addEventListener('resize', this.getHeight);
    },
    created() {
      this.getList();
      this.getDicts("reimbursement_type").then(response => {
        this.typeOptions = response.data;
      });
      this.getDicts("audit_state").then(response => {
        this.auditStateOptions = response.data;
      });
    },
    methods: {
      feeFormat(row, column, cellValue) {
        if (cellValue != null && cellValue != undefined) {
          return cellValue.toFixed(2);
        }
      },
      /** 对话框自适应高度 */
      getHeight() {
        this.bodyStyle.height = (window.innerHeight - 281) * 0.2 + 'px';
      },
      /** 查询报销申请列表 */
      getList() {
        this.loading = true;
        listApply(this.queryParams).then(response => {
          this.applyList = response.rows;
          this.total = response.total;
          this.loading = false;
        });
      },
      // 报销类型字典翻译
      typeFormat(row, column) {
        return this.selectDictLabel(this.typeOptions, row.type);
      },
      // 审核状态字典翻译
      auditStateFormat(row, column) {
        return this.selectDictLabel(this.auditStateOptions, row.auditState);
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
      /*计算合计*/
      getTotal(param){
        const { columns, data } = param;
        const sums = [];
        columns.forEach((column, index) => {
          if (index === 1) {
            sums[index] = '合计';
            return;
          }
          const values = data.map(
            item =>{
              if(this.ids.indexOf(item.id)!=-1){
                return Number(item[column.property])
              }else{
                return ;
              }
            }
            );
          if (column.property === 'totalMoney' ) {
            sums[index] = values.reduce((prev, curr) => {
              const value = Number(curr);
              if (!isNaN(value)) {
                return prev + curr;
              } else {
                return prev;
              }
            }, 0);
            sums[index] = sums[index].toFixed(2)+' 元';
          }
        });
        return sums;
      },
      // 多选框选中数据
      handleSelectionChange(selection) {

        this.ids = selection.map(item => item.id)
        this.single = selection.length != 1
        this.multiple = !selection.length
        if(selection.length){
          this.showSummary = true ;
        }else{
          this.showSummary = false ;
        }
      },
      /** 新增按钮操作 */
      handleAdd(type) {
        this.form = {};
        this.form.type = type;
        this.form.uuid = this.uuid();
        this.typeShow(this.form, "add");
      },
      /**查看按钮操作*/
      handleSee(row) {
        const id = row.id || this.ids
        getApply(id).then(response => {
          this.form = response.data;
          this.typeShow(this.form, "see");
        });
      },
      /** 修改按钮操作 */
      handleUpdate(row, type) {
        const id = row.id || this.ids
        getApply(id).then(response => {
          this.form = response.data;
          this.typeShow(this.form, type);
        });
      },
      //批量提交
      handleSubmit(){
        let selections = this.$refs.applyTable.selection;
        let submitFlag = true ;
        selections.forEach(selection=>{
          if(selection.auditState =='2' ){
            this.$message.error('您选择批量提交的申请中有已审核通过项!');
            submitFlag = false;
          }
        });
        if(submitFlag){
          const ids = this.ids
          this.$confirm('是否确认提交选择数据项?', "警告", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning"
          }).then(function () {

            return submitApply(ids);
          }).then(() => {
            this.getList();
            this.msgSuccess("提交成功");
          }).catch(function () {
          });
        }
      },
      typeShow(value, type) {
        this.$refs.reimbursement.handleShow(value, type);
      },
      /** 删除按钮操作 */
      handleDelete(row) {
        const ids = row.id || this.ids;
        this.$confirm('是否确认删除报销申请编号为"' + ids + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return delApply(ids);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(function () {
        });
      },
      /** 导出按钮操作 */
      handleExport() {
        const queryParams = this.queryParams;
        this.$confirm('是否确认导出所有报销申请数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return exportApply(queryParams);
        }).then(response => {
          this.download(response.msg);
        }).catch(function () {
        });
      }
    },

  };
</script>
