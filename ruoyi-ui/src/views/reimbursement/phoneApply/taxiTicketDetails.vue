<template>
  <div>
    <!-- 添加或修改出租车发票对话框 -->
    <el-dialog :title="title" :visible.sync="open" :fullscreen="true"
               append-to-body @open="init" :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-position="top" label-width="100px">
        <el-card shadow="always" style="margin-bottom: 30px;">
          <div slot="header" style="height: 25px">
            <span style="font-weight: bold;font-size: 16px">基本信息</span>
          </div>
          <el-form-item label="发票代码" prop="invoiceCode">
            <el-input v-model="form.invoiceCode" :disabled="disabled" placeholder="请输入发票代码"/>
          </el-form-item>
          <el-form-item label="发票号码" prop="invoiceNum">
            <el-input v-model="form.invoiceNum" :disabled="disabled" placeholder="请输入发票号码"/>
          </el-form-item>
          <el-form-item label="票价" prop="fare">
            <el-input v-model="form.fare" :disabled="disabled" placeholder="请输入票价"/>
          </el-form-item>
          <el-form-item label="车牌号" prop="taxiNum">
            <el-input v-model="form.taxiNum" :disabled="disabled" placeholder="请输入车牌号"/>
          </el-form-item>
          <el-form-item label="开始时间" prop="dateTimeStart">
            <el-date-picker clearable size="small"
                            v-model="form.dateTimeStart"
                            type="datetime"
                            value-format="yyyy-MM-dd HH:mm:ss"
                            :disabled="disabled"
                            style="width: 100%"
                            placeholder="选择开始时间">
            </el-date-picker>
          </el-form-item>
          <el-form-item label="结束时间" prop="dateTimeEnd">
            <el-date-picker clearable size="small"
                            v-model="form.dateTimeEnd"
                            type="datetime"
                            value-format="yyyy-MM-dd HH:mm:ss"
                            :disabled="disabled"
                            style="width: 100%"
                            placeholder="选择结束时间">
            </el-date-picker>
          </el-form-item>
        </el-card>
        <ticket-pic ref="ticketPic" @ok="submitForm"/>
      </el-form>
      <div slot="footer" class="dialog-footer" :style="{textAlign:'center'}">
        <el-button type="primary" :style="{ display: visible }" @click="submitFormBefore">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import {getTaxiTicket, addTaxiTicket, updateTaxiTicket, checkTaxiTicketExist} from "@/api/invoice/taxiTicket";
  import ticketPic from "./ticketPic";

  export default {
    name: "TaxiTicket",
    components: {
      ticketPic,
    },
    data() {
      let checkTaxiTicket = (rule, value, callback) => {
        checkTaxiTicketExist(this.form).then(response => {
          if (response.code === 200) {
            if (response.msg != "") {
              this.form.abnormalFlag = '1'
              if (this.submitFlag && response.msg.indexOf("已报销") == -1) {
                callback();
              } else {
                callback(new Error(response.msg + "请确认!"));
              }
            } else {
              callback();
            }
          } else {
            callback(new Error(response.msg));
          }
        });
      };
      return {
        //部分信息异常任然提交标识
        submitFlag: false,
        // 是否可编辑
        disabled: false,
        //按钮是否显示
        visible: '',
        // 弹出层标题
        title: "",
        msg: "",
        // 是否显示弹出层
        open: false,
        // 表单参数
        form: {},
        // 表单校验
        rules: {

          invoiceCode: [
            {required: true, message: "发票代码不能为空", trigger: "blur"}
          ],
          invoiceNum: [
            {required: true, message: "发票号码不能为空", trigger: "blur"},
            {validator: checkTaxiTicket, trigger: 'blur'}
          ],
          fare: [
            {required: true, message: "票价不能为空", trigger: "blur"}
          ],
        },
        bodyStyle: {
          overflowY: 'auto',
          height: '',
          marginLeft: '2%',
          paddingRight: '2%',
        },
      };
    },
    mounted() {
      //window.addEventListener('resize', this.getHeight);
    },
    created() {

    },
    methods: {
      //打开时初始化
      init() {
        this.$nextTick(function () {
          this.$refs.ticketPic.init(this.form, 'ticket_type', '7', this.openType);
        });
        //this.getHeight();
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
          invoiceUuid: undefined,
          uuid: undefined,
          accessWay: undefined,
          invoiceCode: undefined,
          invoiceNum: undefined,
          invoiceScanNum: undefined,
          taxiNum: undefined,
          dateTimeStart: undefined,
          dateTimeEnd: undefined,
          date: undefined,
          time: undefined,
          fare: undefined,
          fuelOilSurcharge: undefined,
          callServiceSurcharge: undefined,
          sysAttId: undefined,
          createTime: undefined,
          repeatFlag: undefined,
          abnormalFlag: undefined
        };
        this.resetForm("form");

        this.msg = "";
      },
      //表单格式化
      formFormat() {
        if (this.form.fare == null) {
          this.form.fare = undefined;
        }
        if (this.form.fuelOilSurcharge == null) {
          this.form.fuelOilSurcharge = undefined;
        }
        if (this.form.callServiceSurcharge == null) {
          this.form.callServiceSurcharge = undefined;
        }
      },
      // 标红异常信息
      abnormalInfo() {
        this.$refs["form"].validateField('invoiceNum');
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.disabled = false;
        this.visible = '';
        this.reset();
        this.form.uuid = value;
        this.form.ticketUuid = this.uuid();
        this.form.accessWay = 'writeIn';
        this.openType = 'add';
        this.open = true;
        this.submitFlag = false;
        this.title = "添加出租车发票";
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.disabled = false;
        this.visible = '';
        this.reset();
        const id = row.id || this.ids
        getTaxiTicket(id).then(response => {
          this.form = response.data;
          this.formFormat();
          this.openType = 'edit';
          this.open = true;
          this.submitFlag = false;
          this.title = "修改出租车发票";
        }).then(() => {
          this.abnormalInfo()
        });
      },
      /** 查看按钮操作 */
      handleSee(row) {
        this.disabled = true;
        this.visible = 'none';
        this.reset();
        const id = row.id || this.ids
        getTaxiTicket(id).then(response => {
          this.form = response.data;
          this.formFormat();
          this.openType = 'see';
          this.open = true;
          this.submitFlag = false;
          this.title = "查看出租车发票";
        }).then(() => {
          this.abnormalInfo()
        });
      },
      //提交前先上传票据照片 返回照片附件id
      submitFormBefore() {
        this.form.abnormalFlag = '0'
        this.submitFlag = true;
        this.$refs["form"].validate(valid => {
          if (valid) {
            this.$refs.ticketPic.submit();
          } else {
            this.submitFlag = false;
            setTimeout(() => {
              let isError = document.getElementsByClassName("is-error");
              isError[0].querySelector('input').focus();
            }, 100);
            return false;
          }
        });
      },
      /** 提交按钮 */
      submitForm: function (sysAttId) {
        this.$refs["form"].validate(valid => {
          if (valid) {
            if (sysAttId != undefined) {
              this.form.sysAttId = sysAttId;
            }
            this.form.repeatFlag = '0';
            if (this.form.id != undefined) {
              updateTaxiTicket(this.form).then(response => {
                if (response.code === 200) {
                  this.msgSuccess("修改成功");
                  this.open = false;
                  this.$emit("ok");
                } else {
                  this.msgError(response.msg);
                }
              });
            } else {
              addTaxiTicket(this.form).then(response => {
                if (response.code === 200) {
                  this.msgSuccess("新增成功");
                  this.open = false;
                  this.$emit("ok");
                } else {
                  this.msgError(response.msg);
                }
              });
            }
          }
        });
      },

    }
  };
</script>
