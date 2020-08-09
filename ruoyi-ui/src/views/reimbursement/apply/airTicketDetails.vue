<template>
  <div>
    <!-- 添加或修改机票行程单对话框 -->
    <el-dialog :title="title" :visible.sync="open" :fullscreen="true"
               append-to-body  @open="init"  :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" :style="bodyStyle" label-width="150px">
        <el-card shadow="always" style="margin-bottom: 30px;">
          <div slot="header" style="height: 25px">
            <span style="font-weight: bold;font-size: 16px">基本信息</span>
          </div>
          <el-row>
            <el-col :span="12">
              <el-form-item label="电子客票号码" prop="ticketNumber">
                <el-input v-model="form.ticketNumber" :disabled="disabled" placeholder="请输入电子客票号码"/>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="航班号" prop="flight">
                <el-input v-model="form.flight" :disabled="disabled" placeholder="请输入航班号"/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="始发站" prop="startingStation">
                <el-input v-model="form.startingStation" :disabled="disabled" placeholder="请输入始发站"/>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="目的站" prop="destinationStation">
                <el-input v-model="form.destinationStation" :disabled="disabled" placeholder="请输入目的站"/>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row>
            <el-col :span="12">
              <el-form-item label="出发日期" prop="dateTime">
                <el-date-picker clearable size="small" :disabled="disabled" style="width: 100%"
                                v-model="form.dateTime"
                                type="datetime"
                                value-format="yyyy-MM-dd HH:mm:ss"
                                placeholder="选择出发日期">
                </el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="合计金额" prop="ticketRates">
                <el-input-number v-model="form.ticketRates" :disabled="disabled" style="width: 100%"
                                 placeholder="请输入合计金额"
                                 :min="0" :precision="2" controls-position="right"></el-input-number>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="姓名" prop="name">
                <el-input v-model="form.name" :disabled="disabled" placeholder="请输入姓名"/>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="身份证号" prop="idNum">
                <el-input v-model="form.idNum" :disabled="disabled" placeholder="请输入身份证号"/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="票价" prop="fare">
                <el-input-number v-model="form.fare" :disabled="disabled" style="width: 100%"
                                 placeholder="请输入票价"
                                 :min="0" :precision="2" controls-position="right"></el-input-number>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="民航发展基金/基建费" prop="devFund">
                <el-input-number v-model="form.devFund" :disabled="disabled" style="width: 100%"
                                 placeholder="请输入民航发展基金/基建费"
                                 :min="0" :precision="2" controls-position="right"></el-input-number>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row>
            <el-col :span="12">
              <el-form-item label="燃油附加费" prop="fuelSurcharge">
                <el-input-number v-model="form.fuelSurcharge" :disabled="disabled" style="width: 100%"
                                 placeholder="请输入燃油附加费"
                                 :min="0" :precision="2" controls-position="right"></el-input-number>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="其他税费" prop="otherTax">
                <el-input-number v-model="form.otherTax" :disabled="disabled" style="width: 100%"
                                 placeholder="请输入其他税费"
                                 :min="0" :precision="2" controls-position="right"></el-input-number>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="舱位" prop="seatCategory">
                <el-select
                  v-model="form.seatCategory"
                  :disabled="disabled"
                  style="width:100%"
                  filterable
                  allow-create
                  default-first-option
                  placeholder="请输入席别">
                  <el-option
                    v-for="dict in airClassOptions"
                    :key="dict.dictValue"
                    :label="dict.dictLabel"
                    :value="dict.dictLabel"
                  >
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="24">
              <el-form-item label="舱位说明" prop="seatCategoryDesc"
                            :rules="[{required: seatCategoryDescRequired,message: `头等舱需说明`,trigger: 'blur'}]">
                <el-input v-model="form.seatCategoryDesc" :disabled="disabled" type="textarea"
                          placeholder="请输入席别说明"
                          :autosize="{minRows: 4, maxRows: 4}" :style="{width: '100%'}"/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="填开日期" prop="issuedDate">
                <el-date-picker clearable size="small" :disabled="disabled" style="width: 100%"
                                v-model="form.issuedDate"
                                type="date"
                                value-format="yyyy-MM-dd"
                                placeholder="选择填开日期">
                </el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="承运人" prop="carrier">
                <el-input v-model="form.carrier" :disabled="disabled" placeholder="请输入承运人"/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="订票渠道" prop="issuedBy">
                <el-input v-model="form.issuedBy" :disabled="disabled" placeholder="请输入订票渠道"/>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="印刷序号" prop="serialNumber">
                <el-input v-model="form.serialNumber" :disabled="disabled" placeholder="请输入印刷序号"/>
              </el-form-item>
            </el-col>
          </el-row>
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
  import {getAirTicket, addAirTicket, updateAirTicket, checkAirTicketExist} from "@/api/invoice/airTicket";
  import ticketPic from "./ticketPic";

  export default {
    name: "AirTicket",
    components: {
      ticketPic,
    },
    data() {
      let checkAirTicket= (rule, value, callback) => {
        checkAirTicketExist(this.form).then(response => {
          if (response.code === 200) {
            if(response.msg != ""){
              this.form.abnormalFlag = '1'
              if(this.submitFlag && response.msg.indexOf("已报销")==-1){
                callback();
              }else{
                callback(new Error(response.msg+"请确认!"));
              }
            }else{
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
        msg:"",
        // 是否显示弹出层
        open: false,
        // 表单参数
        form: {},
        // 表单校验
        rules: {
          startingStation: [
            {required: true, message: "始发站不能为空", trigger: "blur"}
          ],
          destinationStation: [
            {required: true, message: "目的站不能为空", trigger: "blur"}
          ],
          flight: [
            {required: true, message: "航班号不能为空", trigger: "blur"}
          ],
          dateTime: [
            {required: true, message: "出发日期不能为空", trigger: "blur"}
          ],
          ticketNumber: [
            {required: true, message: "电子客票号码不能为空", trigger: "blur"},
            {validator: checkAirTicket, trigger: 'blur'}
          ],
          ticketRates: [
            {required: true, message: "合计金额不能为空", trigger: "blur"}
          ],
        },
        bodyStyle: {
          overflowY: 'auto',
          height: '',
          marginLeft: '2%',
          paddingRight: '2%',
        },
        openType: '',
        //头等舱说明
        seatCategoryDescRequired:false,
        // 席别字典
        airClassOptions: [],
      };
    },
    watch:{
      'form.seatCategory': function (val){
        if(val == "头等舱"){
          this.seatCategoryDescRequired = true;
        }else{
          this.seatCategoryDescRequired = false;
        }
      }
    },
    mounted() {
      //window.addEventListener('resize', this.getHeight);
    },
    created() {
      this.getDicts("air_class").then(response => {
        this.airClassOptions = response.data;
      });
    },
    methods: {
      //打开时初始化
      init() {
        this.$nextTick(function () {
          this.$refs.ticketPic.init(this.form, 'ticket_type', '5', this.openType);
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
          name: undefined,
          startingStation: undefined,
          destinationStation: undefined,
          flight: undefined,
          dateTime: undefined,
          date: undefined,
          time: undefined,
          ticketNumber: undefined,
          fare: undefined,
          devFund: undefined,
          fuelSurcharge: undefined,
          otherTax: undefined,
          ticketRates: undefined,
          issuedDate: undefined,
          idNum: undefined,
          seatCategory: undefined,
          seatCategoryDesc: undefined,
          carrier: undefined,
          issuedBy: undefined,
          serialNumber: undefined,
          createTime: undefined,
          sysAttId: undefined,
          repeatFlag: undefined,
          abnormalFlag:undefined,
        };
        this.resetForm("form");

        this.msg="";
      },
      //表单格式化
      formFormat() {
        if (this.form.invoiceMoney == null) {
          this.form.invoiceMoney = undefined;
        }
      },
      // 标红异常信息
      abnormalInfo(){
        this.$refs["form"].validateField('ticketNumber');
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
        this.title = "添加机票行程单";
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.disabled = false;
        this.visible = '';
        this.reset();
        const id = row.id || this.ids
        getAirTicket(id).then(response => {
          this.form = response.data;
          this.formFormat();
          this.openType = 'edit';
          this.open = true;
          this.submitFlag = false;
          this.title = "修改机票行程单";
        }).then(()=>{
          this.abnormalInfo()
        });
      },
      /** 查看按钮操作 */
      handleSee(row) {
        this.disabled = true;
        this.visible = 'none';
        this.reset();
        const id = row.id || this.ids
        getAirTicket(id).then(response => {
          this.form = response.data;
          this.formFormat();
          this.openType = 'see';
          this.open = true;
          this.submitFlag = false;
          this.title = "查看机票行程单";
        }).then(()=>{
          this.abnormalInfo()
        });
      },
      //提交前先上传票据照片 返回照片附件id
      submitFormBefore() {
        this.form.abnormalFlag = '0'
        this.submitFlag =true ;
        this.$refs["form"].validate(valid => {
          if (valid) {
            this.$refs.ticketPic.submit();
          }else{
            this.submitFlag =false ;
            setTimeout(()=>{
              let isError= document.getElementsByClassName("is-error");
              isError[0].querySelector('input').focus();
            },100);
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
              updateAirTicket(this.form).then(response => {
                if (response.code === 200) {
                  this.msgSuccess("修改成功");
                  this.open = false;
                  this.$emit("ok");
                } else {
                  this.msgError(response.msg);
                }
              });
            } else {
              addAirTicket(this.form).then(response => {
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
