<template>
  <div>
    <el-card shadow="always" style="margin-bottom: 30px;">
      <div slot="header" style="height: 25px">
        <span style="font-weight: bold;font-size: 16px">审核记录</span>
      </div>
      <el-table v-loading="loading" :data="auditLogList">
        <el-table-column label="审核人" align="center" prop="userId">
          <template slot-scope="scope">
            <span>{{scope.row.sysUser.userName}}</span>
          </template>
        </el-table-column>
        <el-table-column label="审核结果" align="center" prop="state" :formatter="stateFormat"/>
        <el-table-column label="审核时间" align="center" prop="createTime"/>
        <el-table-column label="审核原因" align="center" prop="reason"/>
      </el-table>
    </el-card>
  </div>
</template>

<script>
  import {listAuditLogNoPage} from "@/api/system/auditLog";

  export default {
    name: "AuditLog",
    data() {
      return {
        // 遮罩层
        loading: true,
        // 审核记录表格数据
        auditLogList: [],
        // 审核状态(2：通过 3：不通过)字典
        stateOptions: [],
        // 表单参数
        form: {},
        // 表单校验
        rules: {},
      };
    },
    mounted() {

    },
    created() {
      this.getList();
      this.getDicts("audit_state").then(response => {
        this.stateOptions = response.data;
      });
    },
    methods: {
      /**初始化关联业务uuid*/
      init(uuid) {
        this.form.uuid = uuid;
        this.getList();

      },
      /** 查询审核记录列表 */
      getList() {
        this.loading = true;
        listAuditLogNoPage({"uuid": this.form.uuid}).then(response => {
          this.auditLogList = response.data;
          this.loading = false;
        });
      },
      // 审核状态(2：通过 3：不通过)字典翻译
      stateFormat(row, column) {
        return this.selectDictLabel(this.stateOptions, row.state);
      },

    }
  };
</script>
