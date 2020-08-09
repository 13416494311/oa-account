<template>
  <div>
    <!-- 添加或修改增值税发票对话框 -->
    <el-dialog :visible.sync="open" :fullscreen="true" append-to-body
               :close-on-click-modal="false" @open="init">
      <el-form ref="form" :model="form" :rules="rules" :style="bodyStyle" label-width="150px">
        <el-card shadow="always" style="margin-bottom: 30px;">
          <div slot="header" style="height: 25px">
            <span style="font-weight: bold;font-size: 16px">基本信息</span>
          </div>
          <el-row>
            <el-col :span="12">
              <el-form-item label="发票种类" prop="invoiceType">
                <el-select
                  v-model="form.invoiceType"
                  :disabled="disabled"
                  style="width:100%"
                  filterable
                  allow-create
                  default-first-option
                  placeholder="请输入发票种类">

                  <el-option
                    v-for="dict in invoiceTypeOptions"
                    :key="dict.dictValue"
                    :label="dict.dictLabel"
                    :value="dict.dictLabel"
                  ></el-option>

                </el-select>
              </el-form-item>
            </el-col>
            <!--<el-col :span="12">
              <el-form-item label="发票名称" prop="invoiceTypeOrg">
                <el-input v-model="form.invoiceTypeOrg" :disabled="disabled" placeholder="请输入发票名称"/>
              </el-form-item>
            </el-col>-->
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="发票代码" prop="invoiceCode">
                <el-input v-model="form.invoiceCode" :disabled="disabled" placeholder="请输入发票代码"/>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="发票号码" prop="invoiceNum">
                <el-input v-model="form.invoiceNum" :disabled="disabled" placeholder="请输入发票号码"/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="校验码" prop="checkCode">
                <el-input v-model="form.checkCode" :disabled="disabled" placeholder="请输入校验码"/>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="开票日期" prop="invoiceDate">
                <el-date-picker clearable size="small"
                                v-model="form.invoiceDate"
                                type="date"
                                :disabled="disabled"
                                style="width: 100%"
                                value-format="yyyy-MM-dd"
                                placeholder="选择开票日期">
                </el-date-picker>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="价税合计(小写)" prop="amountInFiguers">
                <el-input-number v-model="form.amountInFiguers" :disabled="disabled" style="width: 100%"
                                 @change="amountInFiguersChange"
                                 placeholder="请输入价税合计(小写)"
                                 :min="0" :precision="2" controls-position="right"></el-input-number>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="价税合计(大写)" prop="amountInWords">
                <el-input v-model="form.amountInWords" :disabled="disabled" placeholder="请输入价税合计(大写)"/>
              </el-form-item>
            </el-col>
          </el-row>
          <!--<el-row>
            <el-col :span="12">
              <el-form-item label="合计金额" prop="totalAmount">
                <el-input-number v-model="form.totalAmount" :disabled="disabled" style="width: 100%"
                                 placeholder="请输入合计金额"
                                 :min="0" :precision="2" controls-position="right"></el-input-number>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="合计税额" prop="totalTax">
                <el-input-number v-model="form.totalTax" :disabled="disabled" style="width: 100%"
                                 placeholder="请输入合计税额"
                                 :min="0" :precision="2" controls-position="right"></el-input-number>
              </el-form-item>
            </el-col>
          </el-row>-->
        </el-card>

        <el-card shadow="always" style="margin-bottom: 30px;">
          <div slot="header" style="height: 25px">
            <span style="font-weight: bold;font-size: 16px">购方信息</span>
          </div>
          <el-row>
            <el-col :span="12">
              <el-form-item label="购方名称" prop="purchaserName">
                <el-input v-model="form.purchaserName" :disabled="disabled" placeholder="请输入购方名称"/>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="购方纳税人识别号" prop="purchaserRegisterNum">
                <el-input v-model="form.purchaserRegisterNum" :disabled="disabled" placeholder="请输入购方纳税人识别号"/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="购方地址及电话" prop="purchaserAddress">
                <el-input v-model="form.purchaserAddress" :disabled="disabled" placeholder="请输入购方地址及电话"/>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="购方开户行及账号" prop="purchaserBank">
                <el-input v-model="form.purchaserBank" :disabled="disabled" placeholder="请输入购方开户行及账号"/>
              </el-form-item>
            </el-col>
          </el-row>
        </el-card>

        <el-card shadow="always" style="margin-bottom: 30px;">
          <div slot="header" style="height: 25px">
            <span style="font-weight: bold;font-size: 16px">售方信息</span>
          </div>
          <el-row>
            <el-col :span="12">
              <el-form-item label="销售方名称" prop="sellerName">
                <el-input v-model="form.sellerName" :disabled="disabled" placeholder="请输入销售方名称"/>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="销售方纳税人识别号" prop="sellerRegisterNum">
                <el-input v-model="form.sellerRegisterNum" :disabled="disabled" placeholder="请输入销售方纳税人识别号"/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="销售方地址及电话" prop="sellerAddress">
                <el-input v-model="form.sellerAddress" :disabled="disabled" placeholder="请输入销售方地址及电话"/>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="销售方开户行及账号" prop="sellerBank">
                <el-input v-model="form.sellerBank" :disabled="disabled" placeholder="请输入销售方开户行及账号"/>
              </el-form-item>
            </el-col>
          </el-row>
        </el-card>
        <commodity ref="commodity"/>

        <el-card shadow="always" style="margin-bottom: 30px;">
          <div slot="header" style="height: 25px">
            <span style="font-weight: bold;font-size: 16px">其他信息</span>
          </div>
          <el-row>
            <el-col :span="12">
              <el-form-item label="密码区" prop="password">
                <el-input v-model="form.password" :disabled="disabled" placeholder="请输入密码区"/>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="收款人" prop="payee">
                <el-input v-model="form.payee" :disabled="disabled" placeholder="请输入收款人"/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="复核" prop="checker">
                <el-input v-model="form.checker" :disabled="disabled" placeholder="请输入复核"/>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="开票人" prop="noteDrawer">
                <el-input v-model="form.noteDrawer" :disabled="disabled" placeholder="请输入开票人"/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="24">
              <el-form-item label="备注" prop="remarks">
                <el-input v-model="form.remarks" type="textarea" :disabled="disabled"
                          :autosize="{ minRows: 3, maxRows: 6}" placeholder="请输入内容"/>
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
      <div slot="title" class="header-title">
        <span  class="title-name">{{title}} </span> 
        <span  class="title-name" style="color: red;font-size:12px ">{{msg}} </span>       
      </div>


    </el-dialog>
  </div>
</template>

<script>
  import ticketPic from "./ticketPic";
  import commodity from "./commodity";
  import {getInvoice, addInvoice, updateInvoice,checkInvoiceExist} from "@/api/invoice/invoice";
  import {delAttachment} from "@/api/system/attachment";

  export default {
    name: "Invoice",
    components: {
      ticketPic,
      commodity,
    },
    data() {
      let checkInvoice= (rule, value, callback) => {
        checkInvoiceExist(this.form).then(response => {
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
      let checkPurchaserName= (rule, value, callback) => {
          if(this.submitFlag){
            callback();
          }else{
            if(this.purchaserName != value){
              this.form.abnormalFlag = '1'
              callback(new Error("购方名称不是中国自动化学会，请确认!"));
            }else{
              callback();
            }
          }
      };
      let checkPurchaserRegisterNum= (rule, value, callback) => {
        if(this.submitFlag){
          callback();
        }else{
          if(value != undefined && this.purchaserRegisterNum != value){
            this.form.abnormalFlag = '1'
            callback(new Error("购方纳税人识别号不是51100000500002862F，请确认!"));
          }else{
            callback();
          }
        }

      };
      return {
        //部分信息异常任然提交标识
        submitFlag: false,
        //购方名称
        purchaserName:'中国自动化学会',
        //购购方纳税人识别号
        purchaserRegisterNum:'51100000500002862F',
        // 是否可编辑
        disabled: false,
        //按钮是否显示
        visible: '',
        // 弹出层标题
        title: "",
        msg:"",
        // 是否显示弹出层
        open: false,
        // 增值税发票类型字典
        invoiceTypeOptions: [],
        // 表单参数
        form: {},
        // 表单校验
        rules: {
          invoiceType: [
            {required: true, message: "发票种类不能为空", trigger: "blur"}
          ],
          invoiceCode: [
            {required: true, message: "发票代码不能为空", trigger: "blur"}
          ],
          invoiceNum: [
            {required: true, message: "发票号码不能为空", trigger: "blur"},
            {validator: checkInvoice, trigger: 'blur'}
          ],
          invoiceDate: [
            {required: true, message: "开票日期不能为空", trigger: "blur"}
          ],
          purchaserName: [
            {required: true, message: "购方名称不能为空", trigger: "blur"},
            {validator: checkPurchaserName, trigger: 'blur'}
          ],
          purchaserRegisterNum: [
            {validator: checkPurchaserRegisterNum, trigger: 'blur'}
          ],
          sellerName: [
            {required: true, message: "销售方名称不能为空", trigger: "blur"}
          ],
          sellerRegisterNum: [
            {required: true, message: "销售方纳税人识别号不能为空", trigger: "blur"}
          ],
          amountInFiguers: [
            {required: true, message: "价税合计(小写)不能为空", trigger: "blur"}
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
    watch: {},
    mounted() {
      //window.addEventListener('resize', this.getHeight);
    },
    created() {
      this.getDicts("invoice_type").then(response => {
        this.invoiceTypeOptions = response.data;
      });
    },
    methods: {
      //小写转大写
      amountInFiguersChange(){
        let amountInWords = this.convertToChinaNum(this.form.amountInFiguers);
        this.form.amountInWords = amountInWords;
      },
      //打开时初始化
      init() {
        this.$nextTick(function () {
          this.$refs.ticketPic.init(this.form, 'ticket_type', '1', this.openType);
          this.$refs.commodity.init(this.form.invoiceUuid, this.openType);
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
          uuid: undefined,
          invoiceUuid: undefined,
          accessWay: undefined,
          invoiceType: undefined,
          invoiceTypeOrg: undefined,
          invoiceCode: undefined,
          invoiceNum: undefined,
          checkCode: undefined,
          invoiceDate: undefined,
          purchaserName: undefined,
          purchaserRegisterNum: undefined,
          purchaserAddress: undefined,
          purchaserBank: undefined,
          password: undefined,
          sellerName: undefined,
          sellerRegisterNum: undefined,
          sellerAddress: undefined,
          sellerBank: undefined,
          totalAmount: undefined,
          totalTax: undefined,
          amountInWords: undefined,
          amountInFiguers: undefined,
          payee: undefined,
          checker: undefined,
          noteDrawer: undefined,
          remarks: undefined,
          createTime: undefined,
          sysAttId: undefined,
          repeatFlag: undefined,
          abnormalFlag: undefined,
        };
        this.resetForm("form");

        this.msg="";
      },

      // 标红异常信息
      abnormalInfo(){
        this.$refs["form"].validateField('invoiceNum');
        this.$refs["form"].validateField('purchaserName');
        this.$refs["form"].validateField('purchaserRegisterNum');
      },
      /** 新增按钮操作 */
      handleAdd(value) {
        this.disabled = false;
        this.visible = '';
        this.reset();
        this.form.uuid = value;
        this.form.invoiceUuid = this.uuid();
        this.form.accessWay = 'writeIn';
        this.openType = 'add';
        this.open = true;
        this.submitFlag = false;
        this.title = "添加增值税发票";

      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.disabled = false;
        this.visible = '';
        this.reset();
        const id = row.id || this.ids
        getInvoice(id).then(response => {
          this.form = response.data;
          this.formFormat();
          this.openType = 'edit';
          this.open = true;
          this.submitFlag = false;
          this.title = "修改增值税发票";
        }).then(()=>{
          /*if(this.form.repeatFlag=='1'){
            //this.msg = "(该发票号码已上传过，请确认!)";
            this.$refs["form"].validateField('invoiceNum');
          }*/
          this.abnormalInfo()
        });
      },
      /** 查看按钮操作 */
      handleSee(row) {
        this.disabled = true;
        this.visible = 'none';
        this.reset();
        const id = row.id || this.ids
        getInvoice(id).then(response => {
          this.form = response.data;
          this.formFormat();
          this.openType = 'see';
          this.open = true;
          this.submitFlag = false;
          this.title = "查看增值税发票";
        }).then(()=>{
          this.abnormalInfo()
        });
      },
      //表单格式化
      formFormat() {
        if (this.form.totalAmount == null) {
          this.form.totalAmount = undefined;
        }
        if (this.form.totalTax == null) {
          this.form.totalTax = undefined;
        }
        if (this.form.amountInFiguers == null) {
          this.form.amountInFiguers = undefined;
        }
      },
      //提交前先获取照片附件id
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
              updateInvoice(this.form).then(response => {
                if (response.code === 200) {
                  this.msgSuccess("修改成功");
                  this.open = false;
                  this.$emit("ok");
                } else {
                  this.msgError(response.msg);
                }
              });
            } else {
              addInvoice(this.form).then(response => {
                if (response.code === 200) {
                  this.msgSuccess("新增成功");
                  this.open = false;
                  this.$emit("ok");
                } else {
                  this.msgError(response.msg);
                }
              });
            }
          }else{
            setTimeout(()=>{
              var isError= document.getElementsByClassName("is-error");
              isError[0].querySelector('input').focus();
            },100);
            return false;
          }
        });
      },
      /** 删除按钮操作 */
      handleDelete(row) {
        const ids = row.id || this.ids;
        this.$confirm('是否确认删除增值税发票编号为"' + ids + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return delInvoice(ids);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(function () {
        });
      },

    }
  };
</script>
