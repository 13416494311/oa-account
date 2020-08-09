<template>

  <el-card shadow="always" style="margin-bottom: 30px;">
    <div slot="header" style="height: 25px">
      <span style="font-weight: bold;font-size: 16px">付款信息</span>
    </div>
    <el-form ref="form" :model="form" :rules="rules" label-width="100px">
      <el-row>
        <el-col :span="12">
          <el-form-item label="收款人" prop="payee">
            <el-input v-model="form.payee" :disabled="true" placeholder="请输入收款人"/>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="收款方式">
            <el-select v-model="form.payeeType"
                       :disabled="disabled"
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
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item label="身份证" prop="cardNo">
            <el-select
              v-model="form.cardNo"
              :disabled="disabled"
              style="width:100%"
              filterable
              allow-create
              default-first-option
              @change="cardNoSelect"
              placeholder="请输入身份证">
              <el-option
                v-for="item in cardNoOptions"
                :key="item.cardNo"
                :label="item.secrecyCardNo"
                :value="item.cardNo">
              </el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="联系方式" prop="phone">
            <el-input v-model="form.phone" :disabled="disabled" maxlength="11" placeholder="请输入联系方式"/>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row >
        <el-col :span="12">
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
        </el-col>
        <el-col :span="12">
          <el-form-item label="开户行" prop="bank" v-if="bankShow"
                        :rules="[{required: bankRequired,message: `开户行不能为空`,trigger: 'blur'}]"
          >
            <el-input v-model="form.bank" :disabled="disabled" placeholder="请输入开户行"/>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
  </el-card>

</template>


<script>
  import {remoteSearch,getPayeeByApplyUuid,getPayeeListByParam, addPayee, updatePayee, getPayee} from "@/api/reimbursement/payee";

  export default {
    name: "Payee",
    data() {
      let checkPhone = (rule, value, callback) => {
        if (!value) {
          return new Error("请输入电话号码");
        } else {
          const telReg = /^1(3[0-9]|4[5,7]|5[0,1,2,3,5,6,7,8,9]|6[2,5,6,7]|7[0,1,7,8]|8[0-9]|9[1,8,9])\d{8}$/;
          const isTelPhone = telReg.test(value);
          const reg = /^((0\d{2,3}-\d{7,8})|(1[3584]\d{9}))$/;
          const isPhone = reg.test(value);
          if (!isTelPhone && !isPhone) {
            callback(new Error("请输入正确手机号或座机"));
          } else {
            callback();
          }
        }
      };
      let checkCardNo = (rule, value, callback) => {
        if (!value) {
          return new Error("请输入身份证号");
        } else {
          if (value.length!=18) {
            callback(new Error("请输入18位有效身份证号"));
          } else {
            callback();
          }
        }
      };
      return {
        //是否显示银行
        bankShow: true,
        //是否银行必填
        bankRequired: true,
        // 是否可编辑
        disabled: false,
        //按钮是否显示
        visible: '',
        //是否显示操作列
        show: true,
        // 遮罩层
        loading: true,
        // 付款信息表格数据
        payeeList: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 收款方式字典
        payeeTypeOptions: [],
        // 表单参数
        form: {},
        // 表单校验
        rules: {
          payee: [
            {required: true, message: "收款人不能为空", trigger: "blur"}
          ],
          payeeType: [
            {required: true, message: "收款方式（1：汇款；2：现金；3：支票）不能为空", trigger: "blur"}
          ],
          cardNo: [
            {required: true, message: "身份证不能为空", trigger: "blur"},
            {validator: checkCardNo, trigger: 'blur'}
          ],
          phone: [
            {required: true, message: "电话不能为空", trigger: "blur"},
            {validator: checkPhone, trigger: 'blur'}
          ],
        },
        //申请uuid
        applyUuid: '',
        //打开方式
        openType: '',
        //身份证号
        cardNoOptions:[],
        //银行卡号
        bankNoOptions:[],
      };
    },
    watch: {
    },
    mounted() {
    },
    created() {
      this.getDicts("payee_type").then(response => {
        this.payeeTypeOptions = response.data;
      });
    },
    methods: {
      //校验银行卡位数
      checkBankNo (rule, value, callback)  {
        if (!value) {
          return new Error("请输入银行卡号");
        } else {
          if (value.length<16||value.length>19) {
            callback(new Error("请输入正确16-19位银行卡号"));
          } else {
            callback();
          }
        }
      },
      //付款方式change
      payeeTypeChange(){
        if(this.form.payeeType == '2' || this.form.payeeType == '3' ){
          this.bankShow = false;
          this.bankRequired = false;
        }else {
          this.bankShow = true;
          this.bankRequired = true;
        }
      },
      //银行卡号 关联出银行
      bankNoSelect(){
        this.setBank();
      },
      //设置银行
      setBank(){
        this.form.bank = undefined;
        getPayeeListByParam({"payee":this.form.payee,"bankNo":this.form.bankNo}).then(response => {
          if (response.data != undefined  && response.data.length>0) {
            this.form.bank = response.data[0].bank;
          }
        });
      },

      /*获取收款人*/
      setPayee(value) {
        if(this.openType == 'add'){
          let applyUuid = this.form.applyUuid;
          this.reset();
          this.form.applyUuid = applyUuid;
        }
        this.form.payee = value;
        this.setPayeeOptions();

      },
      /**显示付款信息*/
      init(uuid, type) {
        this.reset();
        this.applyUuid = uuid;
        this.openType = type;
        this.cardNoOptions=[];
        if (this.openType == 'see') {
          this.disabled = true;
          this.visible = 'none';
          this.show = false;
        } else if (this.openType == 'edit' || this.openType == 'add'|| this.openType == 'audit') {
          this.disabled = false;
          this.visible = '';
          this.show = true;
        }
        this.payeeTypeChange();
        this.setPayeeOptions();
        this.showPayee();
      },
      // 选择身份证号
      cardNoSelect(){
        this.setPhone();
      },
      //设置手机号
      setPhone(){
        this.form.phone = undefined ;
        getPayeeListByParam({"payee":this.form.payee,"cardNo":this.form.cardNo}).then(response => {
          if (response.data != undefined && response.data.length>0) {
            this.form.phone = response.data[0].phone;
          }
        });
      },
      //设置身份证号选取项
      setPayeeOptions(){
        if(this.form.payee == undefined){
          return
        }
        getPayeeListByParam({"payee":this.form.payee}).then(response => {
          if (response.data != undefined) {
            this.cardNoOptions=[];
            this.bankNoOptions=[];
            let cardNoTemp = {} ;
            let bankNoTemp = {} ;
            let dataList = response.data;
            for(let i = 0; i < dataList.length; i++){
              //保密
              if(this.openType != 'audit'){
                this.secrecy(dataList[i])
              }
              //去重
              let cardNo = dataList[i].cardNo;
              if (cardNoTemp[cardNo]){
                continue
              }else{
                cardNoTemp[cardNo] = cardNo
                this.cardNoOptions.push(dataList[i]);
              }
              let bankNo = dataList[i].bankNo;
              if (bankNoTemp[bankNo]){
                continue
              }else{
                bankNoTemp[bankNo] = bankNo
                this.bankNoOptions.push(dataList[i]);
              }
            }
            if(this.cardNoOptions.length == 1){
              this.form.cardNo = this.cardNoOptions[0].cardNo;
              this.form.phone = this.cardNoOptions[0].phone;
            }
            if(this.bankNoOptions.length == 1){
              this.form.bankNo = this.bankNoOptions[0].bankNo;
              this.form.bank = this.bankNoOptions[0].bank;
            }
          }
        });

      },
      //显示付款人信息
      showPayee() {
        this.form.applyUuid = this.applyUuid;
        getPayeeByApplyUuid(this.form.applyUuid).then(response => {
          if (response.data != undefined) {
            this.form = response.data;
          }
        });
      },
      //身份证 银行卡号 保密控制
      secrecy(data){
        let length = data.cardNo.length-10;
        let c = data.cardNo.substr(6,length);
        let s='';
        for(let i=0;i<length;i++){
          s+="*"
        }
        data.secrecyCardNo =data.cardNo.replace(c,s);

        let length1 = data.bankNo.length-8;
        let c1 = data.bankNo.substr(4,length1);
        let s1='';
        for(let i=0;i<length1;i++){
          s1+="*"
        }
        data.secrecyBankNo =data.bankNo.replace(c1,s1);

      },
      // 收款方式字典翻译
      payeeTypeFormat(row, column) {
        return this.selectDictLabel(this.payeeTypeOptions, row.payeeType);
      },
      // 表单重置
      reset() {
        this.form = {
          id: undefined,
          applyUuid: undefined,
          payee: undefined,
          payeeType: '1',
          cardNo: undefined,
          phone: undefined,
          bank: undefined,
          bankNo: undefined,
          createTime: undefined
        };
        this.resetForm("form");
      },
      /*验证差旅表单*/
      formValidate() {
        this.$refs["form"].validate(valid => {
          if (valid) {
            this.$emit("ok");
          }else{
            setTimeout(()=>{
              var isError= document.getElementsByClassName("is-error");
              isError[0].querySelector('input').focus();
            },100);
            return false;
          }
        })
      },
      /** 提交按钮 */
      submitForm: function () {
        this.$refs["form"].validate(valid => {
          if (valid) {
            if (this.form.id != undefined) {
              updatePayee(this.form).then(response => {
                if (response.code === 200) {
                } else {
                  this.msgError(response.msg);
                }
              });
            } else {
              addPayee(this.form).then(response => {
                if (response.code === 200) {
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

    }
  };
</script>
