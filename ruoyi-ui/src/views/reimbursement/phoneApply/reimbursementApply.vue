<template>
  <div class="app-container">
    <!-- 添加或修改报销申请对话框 -->
    <el-dialog :title="title"
               :visible.sync="open"
               :fullscreen="true"
               :append-to-body="false"
               @open="init"
               :close-on-click-modal="false">
      <div :style="bodyStyle">
        <el-form ref="form" :model="form" :rules="rules" label-position="top" label-width="150px">
          <el-card shadow="always" style="margin-bottom: 30px;">
            <div slot="header" style="height: 25px">
              <span style="font-weight: bold;font-size: 16px">报销类型</span>
            </div>
            <el-form-item label="报销类型" prop="type">
              <el-select
                v-model="form.type"
                :disabled="disabled"
                style="width:100%"
                placeholder="请选择报销类型"
                @change="typeChange"
                clearable
                size="small">
                <el-option
                  v-for="dict in typeOptions"
                  :key="dict.dictValue"
                  :label="dict.dictLabel"
                  :value="dict.dictValue"
                />
              </el-select>
            </el-form-item>
          </el-card>
          <travel v-show="travelVisible" ref="travel" @ok="travelFormValidate" @setTotalMoney="setTotalMoney"/>
          <!--<payee v-show="payeeVisible" ref="payee" @ok="payeeFormValidate"/>-->
          <payee-expert v-show="payeeExpertVisible" ref="payeeExpert" @setTotalMoney="setTotalMoney"/>
          <ticket v-show="ticketVisible" ref="ticket" @setTotalMoney="setTotalMoney"/>
          <attachment v-show="attachmentVisible" ref="attachment"/>
          <el-card shadow="always" style="margin-bottom: 30px;">
            <div slot="header" style="height: 25px">
              <span style="font-weight: bold;font-size: 16px">基本信息</span>
            </div>
            <el-form-item label="收款人" prop="reimburser">
              <el-autocomplete
                style="width:100%"
                :fetch-suggestions="reimburserSearchAsync"
                v-model="form.reimburser"
                :disabled="disabled"
                @change.native="reimburserChange"
                placeholder="请输入收款人"/>
            </el-form-item>
            <el-form-item label="部门" prop="deptName">
              <el-select
                v-model="form.deptName"
                :disabled="disabled"
                style="width:100%"
                @change="forceRefresh"
                filterable
                allow-create
                default-first-option
                placeholder="请输入收款人部门">
                <el-option
                  v-for="item in deptNameOptions"
                  :key="item.deptName"
                  :value="item.deptName">{{item.deptName}}
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="总金额" prop="totalMoney">
              <el-input-number v-model="form.totalMoney" :disabled="disabled" style="width: 100%"
                               placeholder="请输入报销总金额"
                               @change="totalMoneyChange"
                               :min="0" :precision="2" controls-position="right"></el-input-number>
            </el-form-item>
            <el-form-item label="收款方式" prop="payeeType" v-if="payeeTypeShow"
                          :rules="[{required: bankRequired,message: `收款方式不能为空`,trigger: 'blur'}]">
              <el-select v-model="form.payeeType"
                         :disabled="disabled"
                         clearable
                         style="width: 100%"
                         @change="payeeTypeChange"
                         placeholder="请选择收款方式">
                <el-option
                  v-for="dict in payeeTypeOptions"
                  :key="dict.dictValue"
                  :label="dict.dictLabel"
                  :value="dict.dictValue"
                ></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="开户行账号" prop="bankNo" v-if="bankShow"
                          :rules="[{required: bankRequired,message: `开户行账号不能为空`,trigger: 'blur'},
          {validator: checkBankNo, trigger: 'blur'}]"
            >
              <el-select
                v-model="form.bankNo"
                :disabled="disabled"
                style="width:100%"
                filterable
                allow-create
                default-first-option
                @change="bankNoSelect"
                placeholder="请输入开户行账号">
                <el-option
                  v-for="item in bankNoOptions"
                  :key="item.bankNo"
                  :label="item.secrecyBankNo"
                  :value="item.bankNo">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="开户行" prop="bank" v-if="bankShow"
                          :rules="[{required: bankRequired,message: `开户行不能为空`,trigger: 'blur'}]"
            >
              <el-input v-model="form.bank" :disabled="disabled" placeholder="请输入开户行"/>
            </el-form-item>
            <el-form-item label="报销事由" prop="reason">
              <el-input v-model="form.reason" :disabled="disabled" type="textarea"
                        placeholder="请输入报销事由"
                        :autosize="{minRows: 4, maxRows: 4}" :style="{width: '100%'}"/>
            </el-form-item>
            <el-form-item label="课题全名">
              <el-autocomplete
                style="width:100%"
                :fetch-suggestions="topicSearchAsync"
                v-model="form.topic"
                :disabled="disabled"
                placeholder="请输入课题全名"/>
            </el-form-item>
            <el-form-item label="经办人">
              <el-autocomplete
                style="width:100%"
                :fetch-suggestions="operatorSearchAsync"
                v-model="form.operator"
                :disabled="disabled"
                placeholder="请输入经办人"/>
            </el-form-item>
          </el-card>
          <audit v-show="auditVisible" ref="audit"/>
          <audit-add v-show="auditAddVisible" ref="auditAdd" @ok="setAuditState"/>
        </el-form>
      </div>
      <div slot="footer" class="dialog-footer" :style="{textAlign:'center'}">
        <el-button v-if="!auditFlag" :style="{ display: visible }" type="primary" @click="setAuditState('0')">保 存
        </el-button>
        <el-button v-if="!auditFlag" :style="{ display: visible }" type="primary" @click="setAuditState('1')">提 交
        </el-button>
        <el-button v-if="auditFlag" :style="{ display: visible }" type="primary" @click="audit">审 核
        </el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>


<script>
  import {remoteSearch, updateApply, addApply, listApplyNoPage} from "@/api/reimbursement/apply";
  import {addTravel, updateTravel, getTravelByApplyUuid} from "@/api/reimbursement/travel";
  import {getChildrenDepts} from "@/api/system/dept";
  import {listAuditLogNoPage} from "@/api/system/auditLog";
  import attachment from "./attachment";
  import travel from "./travel";
  /*import payee from "./payee";*/
  import payeeExpert from "./payeeExpert";
  import ticket from "./ticket";
  import audit from "./audit";
  import auditAdd from "./auditAdd";

  export default {
    name: "ReimbursementApply",
    components: {
      attachment,
      travel,
      /*payee,*/
      payeeExpert,
      ticket,
      audit,
      auditAdd
    },
    data() {
      return {
        //是否显示付款方式
        payeeTypeShow: true,
        //是否显示银行
        bankShow: true,
        //是否银行必填
        bankRequired: true,
        // 专家/劳务 付款信息卡片显示/隐藏
        payeeExpertVisible: false,
        //附件卡片显示/隐藏
        attachmentVisible: false,
        /*//付款信息卡片显示/隐藏
        payeeVisible: false,*/
        //审核记录显示/隐藏
        auditVisible: false,
        //审核操作界面显示/隐藏
        auditAddVisible: false,
        //差旅报销卡片显示/隐藏
        travelVisible: false,
        //票据录入卡片显示/隐藏
        ticketVisible: false,
        //审核显示
        auditFlag: false,
        // 是否可编辑
        disabled: false,
        // 报销人是否可编辑
        reimburserDisabled: false,
        //按钮是否显示
        visible: '',
        //是否显示操作列
        show: true,
        //打开方式
        openType: '',
        // 遮罩层
        loading: false,
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 报销类型字典
        typeOptions: [],
        // 审核状态字典
        auditStateOptions: [],
        // 交通工具字典
        toolOptions: [],
        //区域字典
        regionOptions: [],
        // 部门
        deptNameOptions: [],
        //银行卡号
        bankNoOptions: [],
        // 收款方式字典
        payeeTypeOptions: [],
        // 表单参数
        form: {},
        // 表单校验
        rules: {
          type: [
            {required: true, message: "请选择报销类型", trigger: "blur"}
          ],
          reimburser: [
            {required: true, message: "报销人不能为空", trigger: "blur"}
          ],
          deptName: [
            {required: true, message: "报销人部门不能为空", trigger: "blur"}
          ],
          reason: [
            {required: true, message: "报销事由不能为空", trigger: "blur"}
          ],
          totalMoney: [
            {required: true, message: "报销总金额不能为空", trigger: "blur"}
          ]
        },
        bodyStyle: {
          overflowY: 'auto',
          height: '',
          marginLeft: '2%',
          paddingRight: '2%',
        },
        fileDictType: '',
        //出差表单验证
        travelValidate: false,
        /*//付款表单验证
        payeeValidate: false,*/
        //申请表单验证
        applyValidate: false,
      };
    },
    watch: {},
    mounted() {
      //window.addEventListener('resize', this.getHeight);
      let from
      let form = {};
      form.type = '3';
      form.uuid = this.uuid();
      this.handleShow(form, "add");
    },
    created() {
      this.getDicts("reimbursement_type").then(response => {
        this.typeOptions = response.data;
      });
      this.getDicts("audit_state").then(response => {
        this.auditStateOptions = response.data;
      });
      this.getDicts("travel_tool").then(response => {
        this.toolOptions = response.data;
      });
      this.getDicts("payee_type").then(response => {
        this.payeeTypeOptions = response.data;
      });
    },
    methods: {
      totalMoneyChange(currentValue, oldValue) {
        if (currentValue > oldValue) {
          this.$nextTick(() => {
            this.form.totalMoney = oldValue;
          })
          this.msgInfo("报销总金额修改后不能大于合计总金额!");
        }
      },
      //付款方式change
      payeeTypeChange() {
        if (this.form.payeeType == '2' || this.form.payeeType == '3') {
          this.bankShow = false;
          this.bankRequired = false;
        } else {
          this.bankShow = true;
          this.bankRequired = true;
        }
      },
      //银行卡号 关联出银行
      bankNoSelect() {
        this.setBank();
      },
      //设置银行
      setBank() {
        listApplyNoPage({"reimburser": this.form.reimburser, "bankNo": this.form.bankNo}).then(response => {
          if (response.data != undefined && response.data.length > 0) {
            this.form.bank = response.data[0].bank;
          }
        });
      },
      //校验银行卡位数
      checkBankNo(rule, value, callback) {
        if (!value) {
          return new Error("请输入银行卡号");
        } else {
          callback();
          /*if (value.length < 16 || value.length > 19) {
            callback(new Error("请输入正确16-19位银行卡号"));
          } else {
            callback();
          }*/
        }
      },
      //初始化报销申请页
      init() {
        //this.getHeight();
        this.$nextTick(function () {
          this.auditAddVisible = false;
          listAuditLogNoPage({"uuid": this.form.uuid}).then(response => {
            if (response.data != undefined && response.data.length != 0) {
              this.auditVisible = true;
              this.$refs.audit.init(this.form.uuid);
            } else {
              this.auditVisible = false;
            }
          });
          this.typeChange();
        });
      },
      //报销类型change
      typeChange() {
        this.travelVisible = false;
        /*this.payeeVisible = false;*/
        this.attachmentVisible = false;
        this.payeeExpertVisible = false;
        this.ticketVisible = false;
        switch (this.form.type) {
          case '1':
            this.fileDictType = 'travel_att_type';
            break;    //差旅费
          case '2':
            this.fileDictType = 'expert_att_type';
            break;    //专家/劳务费
          case '3':
            this.fileDictType = 'common_att_type';
            break;    //一般报销
          case '4':
            this.fileDictType = 'material_att_type';
            break;  //资料/材料费
          case '5':
            this.fileDictType = 'traffic_att_type';
            break;    //交通报销
          case '6':
            this.fileDictType = 'meeting_att_type';
            break;    //会议费
          case '7':
            this.fileDictType = 'welfare_att_type';
            break;    //福利发放
          case '8':
            this.fileDictType = 'car_repair_att_type';
            break;    //车辆维修
          default:
            this.fileDictType = '';
            break;
        }

        this.payeeTypeShow = true;
        this.bankShow = true;
        this.bankRequired = true;

        if (this.form.type == '1') {
          this.ticketVisible = true;
          this.$refs.ticket.init(this.form.uuid, this.openType);
          this.travelVisible = true;
          this.$refs.travel.init(this.form.uuid, this.openType);
          /*this.payeeVisible = true;
          this.$refs.payee.init(this.form.uuid, this.openType);*/
          this.attachmentVisible = true;
          this.$refs.attachment.init(this.form.uuid, this.fileDictType, this.openType);
        } else if (this.form.type == '2') {
          this.payeeExpertVisible = true;
          this.$refs.payeeExpert.init(this.form.uuid, this.openType);
          this.attachmentVisible = true;
          this.$refs.attachment.init(this.form.uuid, this.fileDictType, this.openType);

          this.form.payeeType = undefined;
          this.payeeTypeShow = false;
          this.bankShow = false;
          this.bankRequired = false;

        } else {
          this.ticketVisible = true;
          this.$refs.ticket.init(this.form.uuid, this.openType);
          /*this.payeeVisible = true;
          this.$refs.payee.init(this.form.uuid, this.openType);*/
          this.attachmentVisible = true;
          this.$refs.attachment.init(this.form.uuid, this.fileDictType, this.openType);
        }
        this.reimburserChange();
      },
      //强制渲染
      forceRefresh() {
        this.$forceUpdate();
      },
      //设置银行卡号选取项
      setBankNoOptions() {
        listApplyNoPage({"reimburser": this.form.reimburser}).then(response => {
          if (response.data != undefined) {
            this.bankNoOptions = [];
            let bankNoTemp = {};
            let dataList = response.data;
            for (let i = 0; i < dataList.length; i++) {
              //保密
              if (this.openType != 'audit') {
                this.secrecy(dataList[i])
              }
              //去重
              let bankNo = dataList[i].bankNo;
              if (bankNo == undefined || bankNoTemp[bankNo]) {
                continue
              } else {
                bankNoTemp[bankNo] = bankNo
                this.bankNoOptions.push(dataList[i]);
              }
            }
            if (this.bankNoOptions.length == 1) {
              this.form.bankNo = this.bankNoOptions[0].bankNo;
              this.form.bank = this.bankNoOptions[0].bank;
            }
          }
        });
      },
      // 银行卡号 保密控制
      secrecy(data) {
        if (data.bankNo) {
          let length1 = data.bankNo.length - 8;
          let c1 = data.bankNo.substr(4, length1);
          let s1 = '';
          for (let i = 0; i < length1; i++) {
            s1 += "*"
          }
          data.secrecyBankNo = data.bankNo.replace(c1, s1);
        }
      },
      /*报销人change*/
      reimburserChange() {
        /*if (this.form.type != 2) {
          setTimeout(() => {
            this.$refs.payee.setPayee(this.form.reimburser);
          }, 500);
        }*/
        if (this.form.reimburser != undefined) {
          this.setBankNoOptions();
          this.deptNameOptions = [];
          let temp = {};
          listApplyNoPage({"reimburser": this.form.reimburser}).then(response => {
            if (response.data != undefined && response.data.length > 0) {
              if (this.form.deptName == undefined) {
                this.form.deptName = response.data[0].deptName;
              }
              let dataList = response.data;
              for (let i = 0; i < dataList.length; i++) {
                //去重
                let deptName = dataList[i].deptName;
                if (temp[deptName]) {
                  continue
                } else {
                  temp[deptName] = deptName;
                  this.deptNameOptions.push({"deptName": deptName});
                }
              }
            }
          }).then(() => {
            getChildrenDepts("100").then(response => {
              let dataList = response.data;
              for (let i = 0; i < dataList.length; i++) {
                //去重
                let deptName = dataList[i].deptName;
                if (temp[deptName]) {
                  continue
                } else {
                  temp[deptName] = deptName
                  this.deptNameOptions.push({"deptName": deptName});
                }
              }
            })

          });
        }
      },
      /**远程搜索*/
      reimburserSearchAsync(queryString, callback) {
        let list = [{}];
        remoteSearch({"reimburser": queryString}).then(response => {
          if (response.data != undefined) {
            for (let i of response.data) {
              i.value = i.reimburser;  //将想要展示的数据作为value
            }
            list = response.data;
          }
          callback(list);
        });
      },
      /*deptNameSearchAsync(queryString, callback) {
        let list = [{}];
        remoteSearch({"deptName": queryString}).then(response => {
          if (response.data != undefined) {
            for (let i of response.data) {
              i.value = i.deptName;  //将想要展示的数据作为value
            }
            list = response.data;
          }
          callback(list);
        });
      },*/
      topicSearchAsync(queryString, callback) {
        let list = [{}];
        remoteSearch({"topic": queryString}).then(response => {
          if (response.data != undefined) {
            for (let i of response.data) {
              i.value = i.topic;  //将想要展示的数据作为value
            }
            list = response.data;
          }
          callback(list);
        });
      },
      operatorSearchAsync(queryString, callback) {
        let list = [{}];
        remoteSearch({"operator": queryString}).then(response => {
          if (response.data != undefined) {
            for (let i of response.data) {
              i.value = i.operator;  //将想要展示的数据作为value
            }
            list = response.data;
          }
          callback(list);
        });
      },
      /**打开报销页面*/
      handleShow(value, type) {
        this.reset();
        this.form = value;
        this.formFormat();

        this.openType = type;
        if (type == 'see') {
          this.disabled = true;
          this.visible = 'none';
          this.show = false;
        } else if (type == 'edit' || type == 'add' || type == 'audit') {
          this.disabled = false;
          this.visible = '';
          this.show = true;
          this.auditFlag = false
        }
        this.reimburserDisabled = true;
        if (type == 'add') {
          this.reimburserDisabled = false;
          this.form.payeeType = '1';
        }
        if (type == 'audit') {
          this.auditFlag = true
        }
        this.title = "报销申请";
        this.open = true;
      },
      /** 对话框自适应高度 */
      getHeight() {
        this.bodyStyle.height = window.innerHeight - 281 + 'px';
      },
      // 取消按钮
      cancel() {
        this.open = false;
      },
      // 表单重置
      reset() {
        this.form = {
          id: undefined,
          uuid: undefined,
          type: undefined,
          reimburser: undefined,
          deptName: undefined,
          topic: undefined,
          operator: undefined,
          reason: undefined,
          totalMoney: undefined,
          auditState: undefined,
          createTime: undefined,
          payeeType: undefined,
          bank: undefined,
          bankNo: undefined,
        };
        this.resetForm("form");
      },
      //表单格式化
      formFormat() {
        if (this.form.totalMoney == null) {
          this.form.totalMoney = undefined;
        }
      },
      audit() {
        this.auditAddVisible = true;
        this.$refs.auditAdd.handleAdd(this.form.uuid);
      },
      //设置审核状态
      setAuditState(auditState) {
        this.form.auditState = auditState;
        this.submitForm();
      },
      /** 提交按钮 */
      submitForm() {
        this.applyValidate = false;
        this.travelValidate = false;
        /*this.payeeValidate = false;*/
        this.$refs["form"].validate(valid => {
          if (valid) {
            this.applyValidate = true;
          } else {
            setTimeout(() => {
              var isError = document.getElementsByClassName("is-error");
              isError[0].querySelector('input').focus();
            }, 100);
            return false;
          }
        });
        if (this.form.type == '1') {
          this.validateTravelForm();
          /*this.validatePayeeForm();*/
          if (this.travelValidate && this.applyValidate /*&& this.payeeValidate*/) {
            this.$refs.travel.submitForm();
            /*this.$refs.payee.submitForm();*/
            this.submitApplyForm()
          }
        } else /*if (this.form.type == '2')*/ {
          if (this.applyValidate) {
            this.submitApplyForm()
          }
        } /*else {
          this.validatePayeeForm();
          if (this.applyValidate && this.payeeValidate) {
            this.$refs.payee.submitForm();
            this.submitApplyForm()
          }
        }*/
      },
      setTotalMoney(value) {
        if (this.form.type == '1') {
          let ticketTotalMoney = this.$refs.ticket.totalMoney;
          let travelTotalMoney =
            Number(this.$refs.travel.trafficFeeTotalMoney == undefined ? 0 : this.$refs.travel.trafficFeeTotalMoney) +
            Number(this.$refs.travel.subsidyFeeTotalMoney == undefined ? 0 : this.$refs.travel.subsidyFeeTotalMoney) +
            Number(this.$refs.travel.travelForm.hotelFee == undefined ? 0 : this.$refs.travel.travelForm.hotelFee) +
            Number(this.$refs.travel.travelForm.cityFareFee == undefined ? 0 : this.$refs.travel.travelForm.cityFareFee) +
            Number(this.$refs.travel.travelForm.postageFee == undefined ? 0 : this.$refs.travel.travelForm.postageFee) +
            Number(this.$refs.travel.travelForm.officeSuppliesFee == undefined ? 0 : this.$refs.travel.travelForm.officeSuppliesFee) +
            Number(this.$refs.travel.travelForm.bedSubsidyFee == undefined ? 0 : this.$refs.travel.travelForm.bedSubsidyFee) +
            Number(this.$refs.travel.travelForm.otherFee == undefined ? 0 : this.$refs.travel.travelForm.otherFee);
          this.form.totalMoney = Number(ticketTotalMoney) + Number(travelTotalMoney) + '';
          this.$forceUpdate();
        } else {
          this.form.totalMoney = value;
        }
      },
      /**验证差旅表单*/
      validateTravelForm() {
        this.$refs.travel.formValidate();
      },
      /*差旅表单验证结果*/
      travelFormValidate() {
        this.travelValidate = true;
      },
      /**验证付款表单*/
      /*validatePayeeForm() {
        this.$refs.payee.formValidate();
      },
      /!*付款表单验证结果*!/
      payeeFormValidate() {
        this.payeeValidate = true;
      },*/
      /**回调提交申请表单*/
      submitApplyForm() {
        this.$refs["form"].validate(valid => {
          if (valid) {
            if (this.form.id != undefined) {
              updateApply(this.form).then(response => {
                if (response.code === 200) {
                  this.open = false;
                  this.msgSuccess("提交成功");
                  this.$emit("ok");
                } else {
                  this.msgError(response.msg);
                }
              });
            } else {
              addApply(this.form).then(response => {
                if (response.code === 200) {
                  this.msgSuccess("提交成功");
                  this.open = false;
                  this.$emit("ok");
                } else {
                  this.msgError(response.msg);
                }
              });
            }
          } else {
            setTimeout(() => {
              var isError = document.getElementsByClassName("is-error");
              isError[0].querySelector('input').focus();
            }, 100);
            return false;
          }
        });
      },
    }
  };
</script>
