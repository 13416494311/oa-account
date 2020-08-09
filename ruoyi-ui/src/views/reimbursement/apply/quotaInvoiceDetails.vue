<template>
  <div>
    <!-- 添加或修改定额发票对话框 -->
    <el-dialog :visible.sync="open" :fullscreen="true"
               append-to-body  @open="init"  :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" :style="bodyStyle"  label-width="100px">
        <el-card shadow="always" style="margin-bottom: 30px;">
          <div slot="header" style="height: 25px">
            <span style="font-weight: bold;font-size: 16px">基本信息</span>
          </div>
          <el-row>
            <el-col :span="12">
              <el-form-item label="发票代码" prop="invoiceCode">
                <el-input v-model="form.invoiceCode" :disabled="disabled"placeholder="请输入发票代码" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="发票号码" prop="invoiceNumber">
                <el-input v-model="form.invoiceNumber" :disabled="disabled"placeholder="请输入发票号码" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="金额" prop="invoiceMoney">
                <el-input-number v-model="form.invoiceMoney" :disabled="disabled" style="width: 100%"
                                 placeholder="请输入金额"
                                 :min="0" :precision="2" controls-position="right"></el-input-number>
              </el-form-item>
            </el-col>
          </el-row>
        </el-card>
        <ticket-pic  ref="ticketPic" @ok="submitForm"/>
      </el-form>
      <div slot="footer" class="dialog-footer"  :style="{textAlign:'center'}">
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
  import {  getQuota, addQuota, updateQuota,checkQuotaInvoiceExist } from "@/api/invoice/quota";
  import ticketPic from "./ticketPic";

  export default {
    name: "Quota",
    components: {
      ticketPic,
    },
    data() {
      let checkQuotaInvoice= (rule, value, callback) => {
        checkQuotaInvoiceExist(this.form).then(response => {
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

          invoiceCode: [
            { required: true, message: "发票代码不能为空", trigger: "blur" }
          ],
          invoiceNumber: [
            { required: true, message: "发票号码不能为空", trigger: "blur" },
            {validator: checkQuotaInvoice, trigger: 'blur'}
          ],
          invoiceMoney: [
            { required: true, message: "金额（小写）不能为空", trigger: "blur" }
          ],
        },
        bodyStyle: {
          overflowY: 'auto',
          height: '',
          marginLeft: '2%',
          paddingRight: '2%',
        },
        openType:'',
      };
    },
    mounted () {
      //window.addEventListener('resize', this.getHeight);
    },
    created() {

    },
    methods: {
      //打开时初始化
      init(){
        this.$nextTick(function () {
          this.$refs.ticketPic.init(this.form,'ticket_type','2',this.openType);
        });
        //this.getHeight();
      },
      /** 对话框自适应高度 */
      getHeight(){
        this.bodyStyle.height=window.innerHeight-281+'px';
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
          invoiceNumber: undefined,
          invoiceRate: undefined,
          invoiceMoney: undefined,
          createTime: undefined,
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
        this.$refs["form"].validateField('invoiceNumber');
      },
      /** 新增按钮操作 */
      handleAdd(value) {
        this.disabled = false;
        this.visible = '';
        this.reset();
        this.form.uuid = value;
        this.form.invoiceUuid = this.uuid();
        this.form.accessWay= 'writeIn';
        this.openType = 'add';
        this.open = true;
        this.submitFlag = false;
        this.title = "添加定额发票";
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.disabled = false;
        this.visible = '';
        this.reset();
        const id = row.id || this.ids
        getQuota(id).then(response => {
          this.form = response.data;
          this.formFormat();
          this.openType = 'edit';
          this.open = true;
          this.submitFlag = false;
          this.title = "修改定额发票";
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
        getQuota(id).then(response => {
          this.form = response.data;
          this.formFormat();
          this.openType = 'see';
          this.open = true;
          this.submitFlag = false;
          this.title = "查看定额发票";
        }).then(()=>{
          this.abnormalInfo()
        });
      },
      //提交前先上传票据照片 返回照片附件id
      submitFormBefore(){
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
            if(sysAttId!= undefined){
              this.form.sysAttId = sysAttId;
            }
            this.form.repeatFlag = '0';
            if (this.form.id != undefined) {
              updateQuota(this.form).then(response => {
                if (response.code === 200) {
                  this.msgSuccess("修改成功");
                  this.open = false;
                  this.$emit("ok");
                } else {
                  this.msgError(response.msg);
                }
              });
            } else {
              addQuota(this.form).then(response => {
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
