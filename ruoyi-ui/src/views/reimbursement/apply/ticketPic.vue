<template>
  <div>
    <el-card shadow="always" style="margin-bottom: 30px;">
      <div slot="header" style="height: 25px">
        <span style="font-weight: bold;font-size: 16px">票据图片</span>
      </div>
      <div v-if="uploadShow">
        <el-upload
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
      </div>
      <div v-if="imageShow" class="demo-image__preview">
        <el-image
          style="width: 100px; height: 100px"
          :src="url"
          :preview-src-list="srcList">
          <div slot="error" class="image-slot">
            <i class="el-icon-picture-outline"></i>
          </div>
        </el-image>
      </div>
    </el-card>
  </div>
</template>


<script>
  import {uploadPdfToPng, getAttachment, delAttachment} from "@/api/system/attachment";
  import {addInvoiceByAtt} from "@/api/invoice/invoice";

  export default {
    data() {
      return {
        // 查看 文件
        url: '',
        srcList: [],
        // 查看 图片预览
        imageShow: false,
        // 上传
        uploadShow: false,
        //新增 修改 图片预览
        dialogImageUrl: '',
        dialogVisible: false,
        //上传附件
        fileList: [],
        // 图片附件表单参数
        form: {},
        uploadFlieChange: false,
        hideUpload: false,
        limitCount: 1,
      }
    },
    methods: {
      //初始化页面
      init(value, fileDictType, fileDictValue, openType) {
        this.limitCount = 1;
        if(fileDictValue== '5'){
          this.limitCount = 2;
        }
        this.format();
        this.reset();
        this.form.id = value.sysAttId;
        this.form.uuid = value.invoiceUuid;
        this.form.fileDictType = fileDictType;
        this.form.fileDictValue = fileDictValue;
        switch (openType) {
          case "add" :
            this.handleAdd();
            break;
          case "edit" :
            this.handleUpdate();
            break;
          case "see" :
            this.handleSee();
            break;
          default :
            break;
        }
      },
      //重置数据
      format() {
        this.url = '';
        this.srcList = [];
        this.imageShow = false;
        this.uploadShow = false;
        this.dialogImageUrl = '';
        this.dialogImageUrl = '';
        this.fileList = [];
        this.uploadFlieChange = false;
        this.hideUpload = false;
      },
      /*新增*/
      handleAdd() {
        this.uploadShow = true;
      },
      /*修改*/
      handleUpdate() {
        this.uploadShow = true;
        if (this.form.id != undefined) {
          this.showTicketPic();
        } else {
          this.imageShow = false;
        }
      },
      /*查看*/
      handleSee() {
        this.imageShow = true;
        if (this.form.id != undefined) {
          getAttachment(this.form.id).then(response => {
            this.url = process.env.VUE_APP_BASE_API + response.data.filePath;
            this.srcList.push(process.env.VUE_APP_BASE_API + response.data.filePath);
          });
        } else {
          this.imageShow = false;
        }
      },
      /*显示票据图片*/
      showTicketPic() {
        let ids = this.form.id+"";
        let  attIds = ids.split(",");
        this.fileList = [];
        for(let i=0;i<attIds.length;i++){
          getAttachment(attIds[i]).then(response => {
            if (response.data != undefined) {
              let file = {};
              file.name = response.data.fileName;
              file.url = process.env.VUE_APP_BASE_API + response.data.filePath;
              this.fileList.push(file);
              this.hideUpload = this.fileList.length >= this.limitCount;
            }/* else {
              this.form.id = undefined;
            }*/
          });
        }
      },
      handleChange(file, fileList) {
        this.uploadFlieChange = true;
        this.fileList = fileList;
        this.hideUpload = this.fileList.length >= this.limitCount;
      },
      handleBeforeRemove(file, fileList) {
        if (this.form.id != '') {
          return this.$confirm('是否确认删除该票据图片项?', "警告", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning"
          })
        }
      },
      handleRemove(file, fileList) {
        this.fileList = fileList;
        if (this.form.id != '') {
          delAttachment(this.form.id).then(() => {
            this.msgSuccess("删除成功");
          }).catch(function () {
          });
        }
        this.hideUpload = this.fileList.length >= this.limitCount;
      },
      // 超出限制
      handleExceed(file, fileList) {
        this.msgInfo("只能上传一张票据图片!");
      },
      handlePictureCardPreview(file) {
        this.dialogImageUrl = file.url;
        this.dialogVisible = true;
      },
      // 表单重置
      reset() {
        this.form = {
          id: "",
          uuid: undefined,
          fileDictType: undefined,
          fileDictValue: undefined,
          fileDictFlag: undefined,
          fileName: undefined,
          filePath: undefined,
          createTime: undefined
        };
      },
      /*父组件提交 获取附件id  */
      submit() {
        this.$emit("ok", this.form.id);
      },
      /**附件上传*/
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
        formData.append('fileDictType', this.form.fileDictType);
        formData.append('fileDictValue', this.form.fileDictValue);
        formData.append('fileDicFlag', '1');
        uploadPdfToPng(formData).then(response => {
          if (response.code === 200) {
            this.form.id = response.data.id+","+this.form.id;
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

    }

  }
</script>
