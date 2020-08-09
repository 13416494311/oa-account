<template>
  <div>
    <!-- 添加或修改火车票对话框 -->
    <el-dialog :visible.sync="open" :fullscreen="true"
               append-to-body @open="init" :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-position="top" label-width="150px">
        <el-card shadow="always" style="margin-bottom: 30px;">
          <div slot="header" style="height: 25px">
            <span style="font-weight: bold;font-size: 16px">基本信息</span>
          </div>
          <el-form-item label="车票号" prop="ticketNum">
            <el-input v-model="form.ticketNum" :disabled="disabled" placeholder="请输入车票号"/>
          </el-form-item>
          <el-form-item label="车次号" prop="trainNum">
            <el-input v-model="form.trainNum" :disabled="disabled" placeholder="请输入车次号"/>
          </el-form-item>
          <el-form-item label="始发站" prop="startingStation">
            <el-input v-model="form.startingStation" :disabled="disabled" placeholder="请输入始发站"/>
          </el-form-item>
          <el-form-item label="到达站" prop="destinationStation">
            <el-input v-model="form.destinationStation" :disabled="disabled" placeholder="请输入到达站"/>
          </el-form-item>
          <el-form-item label="出发日期" prop="dateTime">
            <el-date-picker clearable size="small" :disabled="disabled" style="width: 100%"
                            v-model="form.dateTime"
                            type="datetime"
                            value-format="yyyy-MM-dd HH:mm:ss"
                            placeholder="选择出发日期">
            </el-date-picker>
          </el-form-item>
          <el-form-item label="车票金额" prop="ticketRates">
            <el-input-number v-model="form.ticketRates" :disabled="disabled" style="width: 100%"
                             placeholder="请输入车票金额"
                             :min="0" :precision="2" controls-position="right"></el-input-number>
          </el-form-item>
          <el-form-item label="乘客姓名" prop="name">
            <el-input v-model="form.name" :disabled="disabled" placeholder="请输入乘客姓名"/>
          </el-form-item>
          <el-form-item label="身份证号" prop="idNum">
            <el-input v-model="form.idNum" :disabled="disabled" placeholder="请输入身份证号"/>
          </el-form-item>
          <el-form-item label="席别" prop="seatCategory">
            <el-select
              v-model="form.seatCategory"
              :disabled="disabled"
              style="width:100%"
              filterable
              allow-create
              default-first-option
              placeholder="请输入席别">
              <el-option
                v-for="dict in seatCategoryOptions"
                :key="dict.dictValue"
                :label="dict.dictLabel"
                :value="dict.dictLabel"
              >
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="座位号" prop="seatNum">
            <el-input v-model="form.seatNum" :disabled="disabled" placeholder="请输入座位号"/>
          </el-form-item>
          <el-form-item label="席别说明" prop="seatCategoryDesc"
                        :rules="[{required: seatCategoryDescRequired,message: `一等座需说明`,trigger: 'blur'}]">
            <el-input v-model="form.seatCategoryDesc" :disabled="disabled" type="textarea"
                      placeholder="请输入席别说明"
                      :autosize="{minRows: 4, maxRows: 4}" :style="{width: '100%'}"/>
          </el-form-item>
          <el-form-item label="售站" prop="salesStation">
            <el-input v-model="form.salesStation" :disabled="disabled" placeholder="请输入售站"/>
          </el-form-item>
          <el-form-item label="序列号" prop="serialNumber">
            <el-input v-model="form.serialNumber" :disabled="disabled" placeholder="请输入序列号"/>
          </el-form-item>
        </el-card>
        <ticket-pic ref="ticketPic" @ok="submitForm"/>
      </el-form>

      <div slot="footer" class="dialog-footer" :style="{textAlign:'center'}">
        <el-button type="primary" :style="{ display: visible }" @click="submitFormBefore">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
      <div slot="title" class="header-title">
        <span class="title-name">{{title}} </span> 
        <span class="title-name" style="color: red;font-size:12px ">{{msg}} </span>       
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import {getTrainTicket, addTrainTicket, updateTrainTicket, checkTrainTicketExist} from "@/api/invoice/trainTicket";
  import ticketPic from "./ticketPic";

  export default {
    name: "TrainTicket",
    components: {
      ticketPic,
    },
    data() {
      let checkTrainTicket = (rule, value, callback) => {
        checkTrainTicketExist(this.form).then(response => {
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
        //一等座需说明
        seatCategoryDescRequired: false,
        // 席别字典
        seatCategoryOptions: [],
        // 表单校验
        rules: {
          ticketNum: [
            {required: true, message: "车票号不能为空", trigger: "blur"},
            {validator: checkTrainTicket, trigger: 'blur'}
          ],
          trainNum: [
            {required: true, message: "车次号不能为空", trigger: "blur"}
          ],
          startingStation: [
            {required: true, message: "始发站不能为空", trigger: "blur"}
          ],
          destinationStation: [
            {required: true, message: "到达站不能为空", trigger: "blur"}
          ],
          dateTime: [
            {required: true, message: "出发日期不能为空", trigger: "blur"}
          ],
          ticketRates: [
            {required: true, message: "车票金额不能为空", trigger: "blur"}
          ],
        },
        bodyStyle: {
          overflowY: 'auto',
          height: '',
          marginLeft: '2%',
          paddingRight: '2%',
        },
        openType: '',
      };
    },
    watch: {
      'form.seatCategory': function (val) {
        if (val == "一等座") {
          this.seatCategoryDescRequired = true;
        } else {
          this.seatCategoryDescRequired = false;
        }
      }
    },
    mounted() {
      //window.addEventListener('resize', this.getHeight);
    },
    created() {
      this.getDicts("train_seat_category").then(response => {
        this.seatCategoryOptions = response.data;
      });
    },
    methods: {
      //打开时初始化
      init() {
        this.$nextTick(function () {
          this.$refs.ticketPic.init(this.form, 'ticket_type', '4', this.openType);
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
          ticketUuid: undefined,
          uuid: undefined,
          accessWay: undefined,
          ticketNum: undefined,
          trainNum: undefined,
          startingStation: undefined,
          destinationStation: undefined,
          dateTime: undefined,
          date: undefined,
          time: undefined,
          ticketRates: undefined,
          seatCategory: undefined,
          seatCategoryDesc: undefined,
          name: undefined,
          idNum: undefined,
          serialNumber: undefined,
          salesStation: undefined,
          seatNum: undefined,
          createTime: undefined,
          sysAttId: undefined,
          repeatFlag: undefined,
          abnormalFlag: undefined,
        };
        this.resetForm("form");

        this.msg = "";
      },
      //表单格式化
      formFormat() {
        if (this.form.invoiceMoney == null) {
          this.form.invoiceMoney = undefined;
        }
      },
      // 标红异常信息
      abnormalInfo() {
        this.$refs["form"].validateField('ticketNum');
      },
      /** 新增按钮操作 */
      handleAdd(value) {
        this.disabled = false;
        this.visible = '';
        this.reset();
        this.form.uuid = value;
        this.form.ticketUuid = this.uuid();
        this.form.accessWay = 'writeIn';
        this.openType = 'add';
        this.open = true;
        this.submitFlag = false;
        this.title = "添加火车票";
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.disabled = false;
        this.visible = '';
        this.reset();
        const id = row.id || this.ids
        getTrainTicket(id).then(response => {
          this.form = response.data;
          this.formFormat();
          this.openType = 'edit';
          this.open = true;
          this.submitFlag = false;
          this.title = "修改火车票";
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
        getTrainTicket(id).then(response => {
          this.form = response.data;
          this.formFormat();
          this.openType = 'see';
          this.open = true;
          this.submitFlag = false;
          this.title = "修改火车票";
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
      submitForm(sysAttId) {
        this.$refs["form"].validate(valid => {
          if (valid) {
            if (sysAttId != undefined) {
              this.form.sysAttId = sysAttId;
            }
            this.form.repeatFlag = '0';
            if (this.form.id != undefined) {
              updateTrainTicket(this.form).then(response => {
                if (response.code === 200) {
                  this.msgSuccess("修改成功");
                  this.open = false;
                  this.$emit("ok");
                } else {
                  this.msgError(response.msg);
                }
              });
            } else {
              addTrainTicket(this.form).then(response => {
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
