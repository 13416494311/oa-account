<template>
  <div class="app-container">
    <!-- 添加或修改审核记录对话框 -->
    <el-dialog :title="title"
               :visible.sync="open"
               append-to-body
               :close-on-click-modal="false"
               width="30%"
    >
      <el-form ref="form" :model="form" :rules="rules" label-position="top" label-width="100px">
        <el-form-item label="审核结果">
          <el-radio-group v-model="form.state">
            <el-radio
              v-for="dict in stateOptions"
              :key="dict.dictValue"
              :label="dict.dictValue"
              @change="stateChange"
            >{{dict.dictLabel}}
            </el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="审核原因" prop="reason"
                      :rules="[{required: required,message: `审核原因不能为空`,trigger: 'blur'}]">
          <el-input v-model="form.reason" type="textarea" :autosize="{ minRows: 4, maxRows: 4}" placeholder="请输入内容"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer" :style="{textAlign:'center'}">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import {addAuditLog} from "@/api/system/auditLog";

  export default {
    name: "AuditLog",
    data() {
      return {
        //是否必填
        required: false,
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 审核状态(2：通过 3：不通过)字典
        stateOptions: [],
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
      //window.addEventListener('resize', this.getHeight);
    },
    created() {
      this.getDicts("audit_result_state").then(response => {
        this.stateOptions = response.data;
      });
    },
    methods: {
      stateChange(){
        if(this.form.state == '2'){
          this.required = false;
        }else{
          this.required = true;
        }
      },
      /** 对话框自适应高度 */
      getHeight() {
        this.bodyStyle.height = window.innerHeight - 281 + 'px';
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
          uuid: undefined,
          userId: undefined,
          state: "2",
          createTime: undefined,
          reason: undefined
        };
        this.resetForm("form");
      },

      /** 新增按钮操作 */
      handleAdd(value) {
        this.reset();
        this.form.uuid =value
        this.open = true;
        this.required = false,
        this.title = "审核记录";
      },
      /** 提交按钮 */
      submitForm: function () {
        this.$refs["form"].validate(valid => {
          if (valid) {
            addAuditLog(this.form).then(response => {
              if (response.code === 200) {
                this.msgSuccess("审核成功");
                this.open = false;
                this.$emit("ok",this.form.state);
              } else {
                this.msgError(response.msg);
              }
            });
          }
        });
      },
    }
  };
</script>
