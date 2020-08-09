<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
      <el-form-item label="单位名称" prop="orgName">
        <el-input
          v-model="queryParams.orgName"
          placeholder="请输入单位名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="营业执照" prop="orgCode">
        <el-input
          v-model="queryParams.orgCode"
          placeholder="请输入营业执照"
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
          v-hasPermi="['payee:org:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['payee:org:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['payee:org:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['payee:org:export']"
        >导出</el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="orgList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="单位名称" align="center" prop="orgName" />
      <el-table-column label="营业执照" align="center" prop="orgCode" />
      <el-table-column label="开户行" align="center" prop="bank" />
      <el-table-column label="开户行账号" align="center" prop="bankAccount" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['payee:org:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['payee:org:remove']"
          >删除</el-button>
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

    <!-- 添加或修改收款方（企业）信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="80%" append-to-body  @open="getHeight"  :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" :style="bodyStyle"  label-width="100px">
        <el-form-item label="单位名称" prop="orgName">
          <el-input v-model="form.orgName" placeholder="请输入单位名称" />
        </el-form-item>
        <el-form-item label="营业执照" prop="orgCode">
          <el-input v-model="form.orgCode" placeholder="请输入营业执照" />
        </el-form-item>
        <el-form-item label="开户行" prop="bank">
          <el-input v-model="form.bank" placeholder="请输入开户行" />
        </el-form-item>
        <el-form-item label="开户行账号" prop="bankAccount">
          <el-input v-model="form.bankAccount" placeholder="请输入开户行账号"
                    maxlength="23"  @change="validateNum()"/>
        </el-form-item>
        <el-form-item label="地址" prop="address">
          <el-input v-model="form.address" placeholder="请输入地址" />
        </el-form-item>
        <el-form-item label="手机" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入联系方式(手机)" />
        </el-form-item>
        <el-form-item label="座机" prop="telephone">
          <el-input v-model="form.telephone" placeholder="请输入联系方式(座机)" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer"  :style="{textAlign:'center'}">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listOrg, getOrg, delOrg, addOrg, updateOrg, exportOrg } from "@/api/payee/org";

export default {
  name: "Org",
  data() {
    return {
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
      // 收款方（企业）信息表格数据
      orgList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        orgName: undefined,
        orgCode: undefined,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        orgName: [
          { required: true, message: "单位名称不能为空", trigger: "blur" }
        ],
        orgCode: [
          { required: true, message: "营业执照不能为空", trigger: "blur" },
          { pattern: /[^_IOZSVa-z\W]{2}\d{6}[^_IOZSVa-z\W]{10}/g,
            message: '营业执照格式不对',
            trigger: 'blur'
          }
        ],
        bank: [
          { required: true, message: "开户行不能为空", trigger: "blur" }
        ],
        bankAccount: [
          { required: true, message: "开户行账号不能为空", trigger: "blur" }
        ],
        phone: [
          { pattern: /^0{0,1}(13[0-9]|15[7-9]|153|156|18[7-9])[0-9]{8}$/,
            message: '手机号格式不对',
            trigger: 'blur'
          }
        ],
        telephone: [
          { pattern: /^(\d{3,4}-)?\d{7,8}$/,
            message: '座机号格式不对',
            trigger: 'blur'
          }
        ],
      },
      bodyStyle:{
          overflowY:'auto',
          height: '',
          marginLeft:'20%' ,
          paddingRight:'20%',
      },
    };
  },
  mounted () {
      window.addEventListener('resize', this.getHeight);
  },
  created() {
    this.getList();
  },
  methods: {
    validateNum () {
      this.setNum(this.form.bankAccount)
    },
    setNum (data) {
      data = data.replace(/\s/g, '').replace(/[^\d]/g, '').replace(/(\d{4})(?=\d)/g, '$1 ')
      this.form.bankAccount =data;
    },
    /** 对话框自适应高度 */
    getHeight() {
      this.bodyStyle.height = window.innerHeight - 281 + 'px';
    },
    /** 查询收款方（企业）信息列表 */
    getList() {
      this.loading = true;
      listOrg(this.queryParams).then(response => {
        this.orgList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: undefined,
        orgName: undefined,
        orgCode: undefined,
        bank: undefined,
        bankAccount: undefined,
        address: undefined,
        phone: undefined,
        telephone: undefined,
        deptId: undefined,
        userId: undefined,
        createTime: undefined
      };
      this.resetForm("form");
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
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!=1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加收款方（企业）信息";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getOrg(id).then(response => {
        this.form = response.data;
        this.validateNum();
        this.open = true;
        this.title = "修改收款方（企业）信息";
      });
    },
    /** 提交按钮 */
    submitForm: function() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != undefined) {
            updateOrg(this.form).then(response => {
              if (response.code === 200) {
                this.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              } else {
                this.msgError(response.msg);
              }
            });
          } else {
            addOrg(this.form).then(response => {
              if (response.code === 200) {
                this.msgSuccess("新增成功");
                this.open = false;
                this.getList();
              } else {
                this.msgError(response.msg);
              }
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$confirm('是否确认删除收款方（企业）信息编号为"' + ids + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delOrg(ids);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(function() {});
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有收款方（企业）信息数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportOrg(queryParams);
        }).then(response => {
          this.download(response.msg);
        }).catch(function() {});
    }
  }
};
</script>
