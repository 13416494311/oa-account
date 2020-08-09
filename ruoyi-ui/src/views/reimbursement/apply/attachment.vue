<template>
  <div>
    <el-card shadow="always" style="margin-bottom: 30px;">
      <div slot="header" style="height: 25px">
        <span style="font-weight: bold;font-size: 16px">附件上传</span>
        <el-dropdown
          style="float: right;margin-top: -5px;margin-left: 10px"
          :style="{ display: visible }">
          <el-button
            type="primary"
            size="mini">
            新增<i class="el-icon-arrow-down el-icon--right"></i>
          </el-button>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item @click.native="handleAdd('1')">
              自定义
            </el-dropdown-item>
            <el-upload
              v-for="dict in allAttTypeOptions"
              v-if="dict.dictValue != 1"
              :key="dict.dictValue"
              action="#"
              :show-file-list="false"
              :http-request="uploadExcludeAtt"
              accept=".jpeg,.gif,.png,.jpg,.pdf,.doc,.docx,.zip"
            >
              <div style="width: 170px;text-align: left">
                <el-dropdown-item @click.native="handleAdd(dict.dictValue)">
                  {{dict.dictLabel}}
                </el-dropdown-item>
              </div>
            </el-upload>
          </el-dropdown-menu>
        </el-dropdown>
      </div>
      <el-table v-loading="attLoading" :data="dictAttList">
        <el-table-column label="附件类型" align="center" prop="dictLabel"/>
        <el-table-column label="附件" align="center">
          <template slot-scope="scope">
            <div v-for="(item, index) in scope.row.attList" :key="index">
              <el-row>
                <el-button
                  size="mini"
                  type="text"
                  @click="onDownload(item)"
                  style="float:left;"
                >{{item.fileName}}
                </el-button>
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-delete"
                  @click="doRemove(item)"
                  style="float:right;"
                  :style="{ display: visible }"
                >删除
                </el-button>
                <br/>
              </el-row>
            </div>
          </template>
        </el-table-column>
        <el-table-column v-if='show' label="操作" align="center" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-upload action="#" :http-request="uploadAtt"
                       :show-file-list="false"
                       accept=".jpeg,.gif,.png,.jpg,.pdf,.doc,.docx,.zip"
            >
              <el-button
                size="mini"
                type="text"
                icon="el-icon-upload"
                @click="onUpload(scope.row.dictValue)"
              >上传
              </el-button>
            </el-upload>
          </template>
        </el-table-column>
      </el-table>
      <att-file-type ref="attFileType" @ok="getAttList"/>
    </el-card>
  </div>
</template>


<script>
  import {delAttachment, download, listAttForReimbursement, upload} from "@/api/system/attachment";
  import {downLoadZip} from "@/utils/zipdownload";
  import {getExcludeDicts} from "@/api/system/dict/data";
  import attFileType from "./attFileType";

  export default {
    name: "Attachment",
    components: {
      attFileType
    },
    data() {
      return {
        // 是否可编辑
        disabled: false,
        //按钮是否显示
        visible: '',
        //是否显示操作列
        show: true,
        //遮罩层
        attLoading: false,
        //附件数据
        dictAttList: [],
        //附件字典类型
        fileDictType: '',
        //附件字典值
        fileDictValue: '',
        //关联业务表uuid
        uuid: '',
        allAttTypeOptions: [],

      }
    },
    created() {

    },
    methods: {
      /**初始化关联业务uuid&&附件类型*/
      init(uuid, fileDictType, type) {
        this.uuid = uuid;
        this.fileDictType = fileDictType;
        if (type == 'see') {
          this.disabled = true;
          this.visible = 'none';
          this.show = false;
        } else if (type == 'edit' || type == 'add' || type == 'audit') {
          this.disabled = false;
          this.visible = '';
          this.show = true;
        }
        this.getAttList();
        getExcludeDicts("all_att_type", this.fileDictType).then(response => {
          this.allAttTypeOptions = response.data;
        });
      },
      /**附件上传*/
      uploadAtt(file) {
        const formData = new FormData();
        formData.append('file', file.file);
        formData.append('uuid', this.uuid);
        formData.append('fileDictType', this.fileDictType);
        formData.append('fileDictValue', this.fileDictValue);
        formData.append('fileDicFlag', '1');
        upload(formData).then(response => {
          if (response.code === 200) {
            this.getAttList();
            this.msgSuccess("上传成功！")
          } else {
            this.msgError(response.msg);
          }
        })
      },
      /**附件上传*/
      uploadExcludeAtt(file) {
        const formData = new FormData();
        formData.append('file', file.file);
        formData.append('uuid', this.uuid);
        formData.append('fileDictType', this.fileDictType);
        formData.append('fileDictValue', this.fileDictValue);
        formData.append('fileDicFlag', '0');
        upload(formData).then(response => {
          if (response.code === 200) {
            this.getAttList();
            this.msgSuccess("上传成功！")
          } else {
            this.msgError(response.msg);
          }
        })
      },
      /**附件上传参数设置*/
      onUpload(dictValue) {
        this.fileDictValue = "";
        this.fileDictValue = dictValue;
      },
      /**附件下载*/
      onDownload(attachment) {
        downLoadZip("/system/attachment/download/" + attachment.id);
      },
      /**附件删除*/
      doRemove(attachment) {
        delAttachment(attachment.id).then(response => {
          this.msgSuccess(response.msg);
          this.getAttList();
        });
      },
      /**附件table*/
      getAttList() {
        this.attLoading = true;
        listAttForReimbursement({"fileDictType": this.fileDictType, "uuid": this.uuid}).then(response => {
          this.dictAttList = [];
          if (response.data != undefined) {
            this.dictAttList = response.data;
          }
          this.attLoading = false;
        });
      },
      //打开自定义添加附件
      addAttFileType() {
        this.$refs.attFileType.handleAdd({"uuid": this.uuid, "fileDictType": this.fileDictType});
      },
      //添加已有附件类型
      handleAdd(value) {
        if (value == '1') {
          this.addAttFileType();
        } else {
          this.onUpload(this.selectDictLabel(this.allAttTypeOptions, value));
        }
      },
    }
  }
</script>
