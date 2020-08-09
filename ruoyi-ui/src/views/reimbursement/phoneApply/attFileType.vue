<template>
  <div class="app-container">
    <!-- 添加或修改附件对话框 -->
    <el-dialog :title="title" :visible.sync="open" :fullscreen="true" append-to-body
               :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-position="top"  label-width="100px">
        <el-form-item label="附件类型" prop="fileDictValue">
          <el-input v-model="form.fileDictValue" placeholder="请输入附件类型" />
        </el-form-item>
        <el-form-item label="附件" prop="file">
          <el-upload
            action="#"
            ref="upload"
            :on-change="onChange"
            :http-request="uploadAtt"
            accept=".jpeg,.gif,.png,.jpg,.pdf,.doc,.docx,.zip"
            :file-list="fileList"
            :auto-upload="false">
            <el-button slot="trigger" size="mini" type="primary">选取文件</el-button>
          </el-upload>
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
  import {  addAttachment ,upload} from "@/api/system/attachment";

  export default {
    name: "AttachmentFile",
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
          fileDictValue: [
            { required: true, message: "附件类型不能为空", trigger: "blur" }
          ],
        },
        bodyStyle:{
          overflowY:'auto',
          height: '',
          marginLeft:'20%' ,
          paddingRight:'20%',
        },
        //上传附件
        fileList:[]
      };
    },
    mounted () {
      //window.addEventListener('resize', this.getHeight);
    },
    created() {

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
          fileDictType: undefined,
          fileDictValue: undefined,
          fileDicFlag: '0',
          fileName: undefined,
          filePath: undefined,
          createTime: undefined
        };
        this.resetForm("form");
      },
      /** 新增按钮操作 */
      handleAdd(value) {
        this.reset();
        this.form.uuid=value.uuid;
        this.form.fileDictType=value.fileDictType;
        this.fileList=[];
        this.open = true;
        this.title = "添加其他类型附件";
      },
      onChange(file,fileList){
        this.fileList=fileList;
      },
      /** 提交按钮 */
      submitForm: function() {
        if(this.fileList.length<1){
          this.msgInfo("请选择要上传附件!");
          return;
        }
        this.$refs["form"].validate(valid => {
          if (valid) {
            this.$refs.upload.submit();
          }else{
            setTimeout(()=>{
              var isError= document.getElementsByClassName("is-error");
              isError[0].querySelector('input').focus();
            },100);
            return false;
          }
        });
      },
      /**附件上传*/
      uploadAtt(file) {
        const formData = new FormData();
        formData.append('file', file.file);
        formData.append('uuid', this.form.uuid);
        formData.append('fileDictType', this.form.fileDictType);
        formData.append('fileDictValue', this.form.fileDictValue);
        formData.append('fileDicFlag', this.form.fileDicFlag);
        upload(formData).then(response => {
          if (response.code === 200) {
            this.open = false;
            this.$emit("ok");
            this.msgSuccess("上传成功！");
          } else {
            this.msgError(response.msg);
          }
        })
      },
    }
  };
</script>
