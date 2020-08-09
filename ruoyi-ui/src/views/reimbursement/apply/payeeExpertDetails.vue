<template>
  <div class="app-container">
    <!-- 添加或修改专家/劳务对话框 -->
    <el-dialog :title="title" :visible.sync="open" :fullscreen="true" append-to-body
               :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" :style="bodyStyle"  label-width="100px">
        <el-form-item label="姓名" prop="name">
          <el-input v-model="form.name" :disabled="disabled" placeholder="请输入专家/劳务 姓名" />
        </el-form-item>
        <el-form-item label="单位" prop="company">
          <el-input v-model="form.company" :disabled="disabled" placeholder="请输入单位" />
        </el-form-item>
        <el-form-item label="所在区域" prop="area">
          <el-radio-group v-model="form.area">
            <el-radio
              v-for="dict in areaOptions"
              :key="dict.dictValue"
              :label="dict.dictValue"
              @change="areaChange"
            >{{dict.dictLabel}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item :label="cardNoLabel" prop="cardNo" >
          <el-input v-model="form.cardNo" :disabled="disabled" placeholder="请输入证件号" />
        </el-form-item>
        <el-form-item label="证件照片" prop="sysAttId"  v-if="areaOut">
          <el-upload
            :disabled="disabled"
            ref="upload"
            action="#"
            :http-request="uploadAtt"
            list-type="picture-card"
            :on-preview="handlePictureCardPreview"
            :on-change="handleChange"
            :before-remove="handleBeforeRemove"
            :on-remove="handleRemove"
            :file-list="fileList"
            :class="{hide:hideUpload}"
            accept="image/jpeg,image/jpg,image/gif,image/png,.pdf"
          >
            <i class="el-icon-plus"></i>
          </el-upload>
          <el-dialog
            :visible.sync="dialogVisible"
            append-to-body
            :close-on-click-modal="false"
          >
            <img width="100%" :src="dialogImageUrl" alt="">
          </el-dialog>
        </el-form-item>


        <el-form-item label="手机号" prop="phone">
          <el-input v-model="form.phone" :disabled="disabled" placeholder="请输入手机号" />
        </el-form-item>
        <el-form-item label="开户行" prop="bank">
          <el-input v-model="form.bank" :disabled="disabled" placeholder="请输入开户行" />
        </el-form-item>
        <el-form-item label="银行卡号" prop="bankNo">
          <el-input v-model="form.bankNo" :disabled="disabled" placeholder="请输入开户行账号" />
        </el-form-item>
        <el-form-item label="税前(元)" prop="preTax">
          <el-input-number v-model="form.preTax" :disabled="disabled" style="width: 70%"
                           placeholder="请输入税前(元)"
                           :min="0" :precision="2" controls-position="right"/>
          <el-button type="primary"
                     size="small"
                     @click="setAftTax"
                     style="float: right;width: 20%"
                     :style="{ display: visible }">税后</el-button>
        </el-form-item>
        <el-form-item label="税后(元)" prop="aftTax">
          <el-input-number v-model="form.aftTax" :disabled="disabled" style="width: 70%" placeholder="请输入税后(元)"
                           :min="0" :precision="2" controls-position="right"/>
          <el-button type="primary"
                     size="small"
                     @click="setPreTax"
                     style="float: right;width: 20%"
                     :style="{ display: visible }">税前</el-button>
        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer"  :style="{textAlign:'center'}">
        <el-button type="primary" :style="{ display: visible }"@click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import { getPayeeExpert, addPayeeExpert, updatePayeeExpert } from "@/api/reimbursement/payeeExpert";
  import { uploadPdfToPng, getAttachment, delAttachment} from "@/api/system/attachment";

  export default {
    name: "PayeeExpertDetails",
    data() {
      let checkPhone = (rule, value, callback) => {
        if (!value) {
          return callback() ;
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
      let checkBankNo = (rule, value, callback) => {
        if (!value) {
          return callback(new Error("请输入银行卡号"));
        } else {
          callback();
          /*if (value.length<16||value.length>19) {
            callback(new Error("请输入正确16-19位银行卡号"));
          } else {
            callback();
          }*/
        }
      };
      let checkCardNo = (rule, value, callback) => {
        if (!value) {
          return callback(new Error("请输入证件号"));
        } else {
          if (value.length!=18&&this.form.area=='0') {
            callback(new Error("请输入18位有效身份证号"));
          } else {
            callback();
          }
        }
      };
      return {
        limitCount: 1,
        //上传附件
        fileList: [],
        //新增 修改 图片预览
        dialogImageUrl: '',
        dialogVisible: false,
        //隐藏上传按钮
        hideUpload: false,
        cardNoLabel:'身份证号',
        //专家所在区域 显示
        areaOut:false,
        // 是否可编辑
        disabled: false,
        //按钮是否显示
        visible: '',
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 专家所在区域  0：境内  1：境外字典
        areaOptions: [],
        // 表单参数
        form: {},
        // 表单校验
        rules: {
          name: [
            { required: true, message: "姓名不能为空", trigger: "blur" }
          ],
          cardNo: [
            { required: true, message: "身份证号不能为空", trigger: "blur" },
            {validator: checkCardNo, trigger: 'blur'}
          ],
          phone: [
            {validator: checkPhone, trigger: 'blur'}
          ],
          bank: [
            { required: true, message: "开户行不能为空", trigger: "blur" }
          ],
          bankNo: [
            { required: true, message: "银行卡号不能为空", trigger: "blur" },
            {validator: checkBankNo, trigger: 'change'}
          ],
          preTax: [
            { required: true, message: "税前(元)不能为空", trigger: "blur" }
          ],
          aftTax: [
            { required: true, message: "税后(元)不能为空", trigger: "blur" }
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
      //window.addEventListener('resize', this.getHeight);
    },
    created() {
      this.getDicts("payee_expert_area").then(response => {
        this.areaOptions = response.data;
      });
    },
    methods: {
      handleBeforeRemove(file, fileList) {
        if (this.form.sysAttId != '') {
          return this.$confirm('是否确认删除该票据图片项?', "警告", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning"
          })
        }
      },
      handleRemove(file, fileList) {
        this.fileList = fileList;
        if (this.form.sysAttId != '') {
          delAttachment(this.form.sysAttId).then(() => {
            this.msgSuccess("删除成功");
          }).catch(function () {
          });
        }
        this.hideUpload = this.fileList.length >= this.limitCount;
      },
      //附件改变
      handleChange(file, fileList) {
        this.fileList = fileList;
        this.hideUpload = this.fileList.length >= this.limitCount;
      },
      //证件照片预览
      handlePictureCardPreview(file) {
        this.dialogImageUrl = file.url;
        this.dialogVisible = true;
      },
      /**证件照片附件上传*/
      uploadAtt(file) {
        const loading = this.$loading({
          lock: true,
          text: '上传中……',
          spinner: 'el-icon-loading',
          background: 'rgba(0, 0, 0, 0.7)'
        });
        const formData = new FormData();
        formData.append('file', file.file);
        formData.append('uuid', this.form.uuid);
        formData.append('fileDictType', "payee_expert_card_pic");
        formData.append('fileDictValue', "1");
        formData.append('fileDicFlag', '1');
        uploadPdfToPng(formData).then(response => {
          if (response.code === 200) {
            this.form.sysAttId = response.data.id;
            this.showTicketPic();
            loading.close();
            this.msgSuccess("上传成功！")
          } else {
            loading.close();
            this.msgError(response.msg);
          }
        }).catch(function (err) {
          loading.close();
        });
      },
      /*显示票据图片*/
      showTicketPic() {
        this.fileList = [];
        if(this.form.sysAttId){
          getAttachment(this.form.sysAttId).then(response => {
            if (response.data != undefined) {
              let file = {};
              file.name = response.data.fileName;
              file.url = process.env.VUE_APP_BASE_API + response.data.filePath;
              this.fileList.push(file);
              this.hideUpload = this.fileList.length >= this.limitCount;
            }
          });
        }

      },
      //选择专家境内还是境外
      areaChange(){
        if(this.form.area == '0'){
          this.areaOut= false;
          this.cardNoLabel= "身份证号";
        }else{
          this.areaOut= true;
          this.cardNoLabel= "证件号";
        }
      },
      /*计算税前*/
      setPreTax(){
        if(this.form.aftTax != undefined){
          let preTax = this.toSellingPrice(this.form.aftTax,1);
          this.form.preTax = preTax;
        }else{
          this.msgInfo("请先输入税后费用!");
        }
      },
      /*计算税后*/
      setAftTax(){
        if(this.form.preTax != undefined){
          let aftTax = this.toPureIncome(this.form.preTax,1);
          this.form.aftTax = aftTax;
        }else{
          this.msgInfo("请先输入税前费用!");
        }
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
          uuid: undefined,
          applyUuid: undefined,
          name: undefined,
          company: undefined,
          area: "0",
          cardNo: undefined,
          sysAttId: undefined,
          phone: undefined,
          bank: undefined,
          bankNo: undefined,
          preTax: undefined,
          aftTax: undefined,
          createTime: undefined
        };
        this.resetForm("form");
      },
      /** 新增按钮操作 */
      handleAdd(value) {
        this.reset();
        this.open = true;
        this.form.uuid = this.uuid();
        this.form.applyUuid = value;
        this.title = "添加专家/劳务";
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.disabled = false;
        this.visible = '';
        this.reset();
        const id = row.id || this.ids
        getPayeeExpert(id).then(response => {
          this.form = response.data;
          this.open = true;
          this.title = "修改专家/劳务";
        }).then(()=>{
          this.areaChange();
          if(this.form.area =='1'){
            this.showTicketPic();
          }
        });
      },
      /** 查看按钮操作 */
      handleSee(row) {
        this.disabled = true;
        this.visible = 'none';
        this.reset();
        const id = row.id || this.ids
        getPayeeExpert(id).then(response => {
          this.form = response.data;
          this.open = true;
          this.title = "修改专家/劳务";
        }).then(()=>{
          this.areaChange();
          if(this.form.area =='1'){
            this.showTicketPic();
          }
        });
      },
      /** 提交按钮 */
      submitForm: function() {
        this.$refs["form"].validate(valid => {
          if (valid) {
            if (this.form.id != undefined) {
              updatePayeeExpert(this.form).then(response => {
                if (response.code === 200) {
                  this.msgSuccess("修改成功");
                  this.open = false;
                  this.$emit("ok");
                } else {
                  this.msgError(response.msg);
                }
              });
            } else {
              addPayeeExpert(this.form).then(response => {
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
    }
  };
</script>
