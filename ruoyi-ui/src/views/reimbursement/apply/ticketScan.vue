<template>
  <div>
    <!-- 添加或修改附件对话框 -->
    <el-dialog :title="title" :visible.sync="open" :fullscreen="true"   append-to-body
               :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" :style="bodyStyle"  label-width="100px">
        <el-form-item label="票据类型" prop="ticketType">
          <el-select v-model="form.ticketType" style="width: 100%"
                     placeholder="请选择票据类型">
            <el-option
              v-for="dict in ticketTypeOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            ></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="票据照片" prop="file">
          <el-upload
            action="#"
            ref="upload"
            :auto-upload="false"
            :on-change="onChange"
            :http-request="uploadAtt"
            :on-preview="handlePictureCardPreview"
            list-type="picture-card"
            :limit="1"
            :on-exceed="handleExceed"
            accept="image/jpeg,image/jpg,image/gif,image/png"
            :file-list="fileList"
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
      </el-form>
      <div slot="footer" class="dialog-footer"  :style="{textAlign:'center'}">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import {addInvoiceByAtt} from "@/api/invoice/invoice";
  import {addQuotaByAtt} from "@/api/invoice/quota";
  import {addTrainTicketByAtt} from "@/api/invoice/trainTicket";
  import {addAirTicketByAtt} from "@/api/invoice/airTicket";

  export default {
    name: "TicketDetails",
    data() {
      return {

        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 表单参数
        form: {},
        // 表单校验
        rules: {
          ticketType: [
            { required: true, message: "票据类型不能为空", trigger: "blur" }
          ],
        },
        bodyStyle:{
          overflowY:'auto',
          height: '',
          marginLeft:'20%' ,
          paddingRight:'20%',
        },
        //上传附件
        fileList:[],
        // 票据类型字典
        ticketTypeOptions: [],
        dialogImageUrl: '',
        dialogVisible: false,
      };
    },
    mounted () {
      //window.addEventListener('resize', this.getHeight);
    },
    created() {
      this.getDicts("ticket_type").then(response => {
        this.ticketTypeOptions = response.data;
      });
    },
    methods: {
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
          ticketType:undefined,
          ticketDictType:'ticket_type'
        };
        this.resetForm("form");
      },
      /** 新增按钮操作 */
      handleAdd(value) {
        this.reset();
        this.form.uuid=value.uuid;
        this.fileList=[];
        this.open = true;
        this.title = "扫描票据照片录入票据信息";
      },
      onChange(file,fileList){
        this.fileList=fileList;
      },
      /** 提交按钮 */
      submitForm: function() {
        if(this.fileList.length<1){
          this.msgInfo("请选择要上传票据照片!");
          return;
        }
        this.$refs["form"].validate(valid => {
          if (valid) {
            this.$refs.upload.submit();
          }
        });
      },
      // 超出限制
      handleExceed(file, fileList) {
        this.msgInfo("只能上传一张票据图片!");
      },
      //图片预览
      handlePictureCardPreview(file) {
        this.dialogImageUrl = file.url;
        this.dialogVisible = true;
      },
      /**附件上传*/
      uploadAtt(file) {
        const formData = new FormData();
        formData.append('file', file.file);
        formData.append('uuid', this.form.uuid);
        formData.append('fileDictType', this.form.ticketDictType);
        formData.append('fileDictValue', this.form.ticketType);
        formData.append('fileDicFlag', '1');
        switch (this.form.ticketType) {
          case "1" :
            addInvoiceByAtt(formData).then(response => {
              if (response.code === 200) {
                this.open = false;
                this.$emit("ok");
                this.msgSuccess("上传成功！");
              } else {
                this.msgError(response.msg);
              }
            });
            break;
          case "2" :
            addQuotaByAtt(formData).then(response => {
              if (response.code === 200) {
                this.open = false;
                this.$emit("ok");
                this.msgSuccess("上传成功！");
              } else {
                this.msgError(response.msg);
              }
            });
            break;
          case "3" :

            break;
          case "4" :
            addTrainTicketByAtt(formData).then(response => {
              if (response.code === 200) {
                this.open = false;
                this.$emit("ok");
                this.msgSuccess("上传成功！");
              } else {
                this.msgError(response.msg);
              }
            });
            break;
          case "5" :
            addAirTicketByAtt(formData).then(response => {
              if (response.code === 200) {
                this.open = false;
                this.$emit("ok");
                this.msgSuccess("上传成功！");
              } else {
                this.msgError(response.msg);
              }
            });
            break;
          default : break;

        }

      },
    }
  };
</script>
