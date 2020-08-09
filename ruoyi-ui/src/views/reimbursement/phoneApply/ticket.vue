<template>
  <div>
    <el-card shadow="always" style="margin-bottom: 30px;">
      <div slot="header" style="height: 25px">
        <span style="font-weight: bold;font-size: 16px">票据录入</span>

        <!--<el-dropdown
          style="float: right;margin-top: -5px;margin-left: 10px"
          :style="{ display: visible }">
          <el-button
            type="primary"
            size="mini">
            新增<i class="el-icon-arrow-down el-icon&#45;&#45;right"></i>
          </el-button>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item
              v-for="dict in ticketTypeOptions"
              :key="dict.dictValue"
              @click.native="handleAdd(dict.dictValue)"
            >{{dict.dictLabel}}</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>-->

        <el-dropdown
          style="float: right;margin-top: -5px;margin-left: 10px"
          :style="{ display: visible }">
          <el-button
            type="primary"
            size="mini">
            录入<i class="el-icon-arrow-down el-icon--right"></i>
          </el-button>
          <el-dropdown-menu slot="dropdown"  >
            <el-upload
              v-for="dict in ticketTypeOptions"
              :key="dict.dictValue"
              v-if="dict.dictValue != '6'"
              action="#"
              :show-file-list="false"
              :http-request="uploadAtt"
              accept="image/jpeg,image/jpg,image/gif,image/png,.pdf"
            >
              <div style="width: 110px;text-align: left">
                <el-dropdown-item @click.native="setTicketType(dict.dictValue)" >
                  {{dict.dictLabel}}
                </el-dropdown-item>
              </div>
            </el-upload>
            <el-dropdown-item
              v-for="dict in ticketTypeOptions"
              :key="dict.dictValue"
              v-if="dict.dictValue == '6'"
              @click.native="handleAdd(dict.dictValue)"
            >{{dict.dictLabel}}</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>

      </div>
      <el-table v-loading="loading" :data="ticketList"
                :row-class-name="tableRowClassName"
                @row-dblclick="handleSee" show-summary :summary-method="getTotal">
        <!--<el-table-column label="票据类型" align="center" prop="ticketType" :formatter="ticketTypeFormat"/>-->
        <el-table-column label="票据类型" align="center" prop="ticketSubType" />
        <el-table-column label="票据号码" align="center" prop="ticketNum"/>
        <el-table-column label="自动识别" align="center" prop="ticketScanNum"/>
        <el-table-column label="日期" align="center" prop="ticketDate" width="100px">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.ticketDate, '{y}-{m}-{d}') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="金额" align="center" prop="totalAmount" :formatter="feeFormat"/>
        <el-table-column label="购方名称" align="center" prop="purchaserName"/>
        <el-table-column label="购方纳税人识别号" align="center" prop="purchaserNum"/>
        <el-table-column label="发票内容" align="center" prop="content">
          <template slot-scope="scope">
            <div v-for="(item, index) in scope.row.content" :key="index">
              <p>{{item}}</p>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="销售方名称" align="center" prop="sellerName"/>
        <el-table-column label="销售方纳税人识别号" align="center" prop="sellerNum"/>
        <el-table-column label="附件" show-overflow-tooltip align="center" prop="sysAtt">
          <template v-if="scope.row.sysAtts != undefined && scope.row.sysAtts.length >0 " slot-scope="scope">
            <div v-for="(item, index) in scope.row.sysAtts" :key="index">
              <el-button
                size="mini"
                type="text"
                @click="onPreview(item)"
                style="float:left;"
              >{{item.fileName}}
              </el-button>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="操作" v-if="show" align="center" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="handleUpdate(scope.row)"
            >修改
            </el-button>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-delete"
              @click="handleDelete(scope.row)"
            >删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <ticket-scan v-show="detailsShow" ref="ticketScan" @ok="getList"/>
    <invoice-details  v-show="detailsShow" ref="invoiceDetails" @ok="refreshTable"/>
    <quota-invoice-details  v-show="detailsShow" ref="quotaInvoiceDetails" @ok="refreshTable"/>
    <train-ticket-details  v-show="detailsShow" ref="trainTicketDetails" @ok="refreshTable"/>
    <air-ticket-details  v-show="detailsShow" ref="airTicketDetails" @ok="refreshTable"/>
    <taxi-ticket-details  v-show="detailsShow" ref="taxiTicketDetails" @ok="refreshTable"/>
    <other-invoice-details  v-show="detailsShow" ref="otherInvoiceDetails" @ok="refreshTable"/>
    <el-image-viewer
      v-if="showViewer"
      :on-close="closeViewer"
      :url-list="[elImageViewerUrl]" />
  </div>
</template>


<script>
  import {listNoPage} from "@/api/invoice/ticket";
  import {downLoadZip} from "@/utils/zipdownload";
  import { delInvoice,addInvoiceByAtt } from "@/api/invoice/invoice";
  import { delQuota ,addQuotaByAtt} from "@/api/invoice/quota";
  import { delTrainTicket,addTrainTicketByAtt } from "@/api/invoice/trainTicket";
  import { delAirTicket ,addAirTicketByAtt} from "@/api/invoice/airTicket";
  import { delTaxiTicket ,addTaxiTicketByAtt} from "@/api/invoice/taxiTicket";
  import { delOtherInvoice } from "@/api/invoice/otherInvoice";
  import ticketScan from "./ticketScan";
  import invoiceDetails from "./invoiceDetails";
  import quotaInvoiceDetails from "./quotaInvoiceDetails";
  import trainTicketDetails from "./trainTicketDetails";
  import airTicketDetails from "./airTicketDetails";
  import taxiTicketDetails from "./taxiTicketDetails";
  import otherInvoiceDetails from "./otherInvoiceDetails";
  import elImageViewer from 'element-ui/packages/image/src/image-viewer'


  export default {
    name: "Ticket",
    components: {
      ticketScan,
      invoiceDetails,
      quotaInvoiceDetails,
      trainTicketDetails,
      airTicketDetails,
      otherInvoiceDetails,
      elImageViewer,
      taxiTicketDetails,
    },
    data() {
      return {
        // 显示查看器
        showViewer: false,
        elImageViewerUrl:'',
        //是否显示
        detailsShow: false,
        // 是否可编辑
        disabled: false,
        //按钮是否显示
        visible: '',
        //是否显示操作列
        show:true,
        // 遮罩层
        loading: true,
        // 票据信息表格数据
        ticketList: [],
        uuid: '',
        // 票据类型字典
        ticketTypeOptions: [],
        ticketType:'',
        //刷新合计金额
        refreshTotalMoney:false,
        //合计金额
        totalMoney:undefined,

      };
    },
    watch:{
      totalMoney(){
        if(this.refreshTotalMoney){
          this.$emit("setTotalMoney",this.totalMoney);
          this.refreshTotalMoney = false;
        }
      }
    },
    mounted() {
    },
    created() {
      this.getDicts("ticket_type").then(response => {
        this.ticketTypeOptions = response.data;
      });
    },
    methods: {
      /**初始化关联业务uuid*/
      init(uuid,type) {
        this.uuid = uuid;
        if (type == 'see') {
          this.disabled = true;
          this.visible = 'none';
          this.show=false ;
        }else if(type == 'edit'||type == 'add'||type == 'audit'){
          this.disabled = false;
          this.visible = '';
          this.show=true  ;
        }
        this.getList();

      },
      // 报销类型字典翻译
      ticketTypeFormat(row, column) {
        return this.selectDictLabel(this.ticketTypeOptions, row.ticketType);
      },
      refreshTable(){
        this.refreshTotalMoney = true ;
        this.getList();
      },
      /** 查询票据信息列表 */
      getList() {
        this.loading = true;
        listNoPage(this.uuid).then(response => {
          this.ticketList = response.data;
          this.loading = false;
        });
      },
      /** 查看按钮操作 */
      handleSee(row) {
        switch(row.ticketType){
          case "1" :this.$refs.invoiceDetails.handleSee(row); break;
          case "2" :this.$refs.quotaInvoiceDetails.handleSee(row); break;
          case "3" :; break;
          case "4" :this.$refs.trainTicketDetails.handleSee(row); break;
          case "5" :this.$refs.airTicketDetails.handleSee(row); break;
          case "6" :this.$refs.otherInvoiceDetails.handleSee(row); break;
          case "7" :this.$refs.taxiTicketDetails.handleSee(row); break;
          default :break;
        }
      },
      /** 新增按钮操作 */
      handleAdd(value) {
        switch(value){
          case "1" :this.$refs.invoiceDetails.handleAdd(this.uuid); break;
          case "2" :this.$refs.quotaInvoiceDetails.handleAdd(this.uuid); break;
          case "3" :; break;
          case "4" :this.$refs.trainTicketDetails.handleAdd(this.uuid); break;
          case "5" :this.$refs.airTicketDetails.handleAdd(this.uuid); break;
          case "6" :this.$refs.otherInvoiceDetails.handleAdd(this.uuid); break;
          case "7" :this.$refs.taxiTicketDetails.handleAdd(this.uuid); break;
          default :break;
        }
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        switch(row.ticketType){
          case "1" :this.$refs.invoiceDetails.handleUpdate(row); break;
          case "2" :this.$refs.quotaInvoiceDetails.handleUpdate(row); break;
          case "3" :; break;
          case "4" :this.$refs.trainTicketDetails.handleUpdate(row); break;
          case "5" :this.$refs.airTicketDetails.handleUpdate(row); break;
          case "6" :this.$refs.otherInvoiceDetails.handleUpdate(row); break;
          case "7" :this.$refs.taxiTicketDetails.handleUpdate(row); break;
          default :break;
        }


      },

      /** 删除按钮操作 */
      handleDelete(row) {
        switch(row.ticketType){
          case "1" :this.handleDeleteInvoice(row); break;
          case "2" :this.handleDeleteQuotaInvoice(row); break;
          case "3" :; break;
          case "4" :this.handleDeleteTrainTicket(row); break;
          case "5" :this.handleDeleteAirTicket(row); break;
          case "6" :this.handleDeleteOtherInvoice(row); break;
          case "7" :this.handleDeleteTaxiTicket(row); break;
          default :break;
        }
      },
      /*删除其他发票*/
      handleDeleteOtherInvoice(row){
        const ids = row.id || this.ids;
        this.$confirm('是否确认删除改发票的数据?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delOtherInvoice(ids);
        }).then(() => {
          this.refreshTable();
          this.msgSuccess("删除成功");
        }).catch(function() {});
      },
      /*删除机票行程单*/
      handleDeleteTaxiTicket(row){
        const ids = row.id || this.ids;
        this.$confirm('是否确认删除该出出租车发票数据?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delTaxiTicket(ids);
        }).then(() => {
          this.refreshTable();
          this.msgSuccess("删除成功");
        }).catch(function() {});
      },
      /*删除机票行程单*/
      handleDeleteAirTicket(row){
        const ids = row.id || this.ids;
        this.$confirm('是否确认删除该机票行程单数据?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delAirTicket(ids);
        }).then(() => {
          this.refreshTable();
          this.msgSuccess("删除成功");
        }).catch(function() {});
      },
      /*删除火车票*/
      handleDeleteTrainTicket(row){
        const ids = row.id || this.ids;
        this.$confirm('是否确认删除该火车票数据?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delTrainTicket(ids);
        }).then(() => {
          this.refreshTable();
          this.msgSuccess("删除成功");
        }).catch(function() {});
      },
      /*删除定额发票*/
      handleDeleteQuotaInvoice(row){
        const ids = row.id || this.ids;
        this.$confirm('是否确认删除该定额发票数据?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delQuota(ids);
        }).then(() => {
          this.refreshTable();
          this.msgSuccess("删除成功");
        }).catch(function() {});
      },
      /** 删除增值税按钮操作 */
      handleDeleteInvoice(row) {
        const ids = row.id ;
        this.$confirm('是否确认删除该增值税发票数据?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delInvoice(ids);
        }).then(() => {
          this.refreshTable();
          this.msgSuccess("删除成功");
        }).catch(function() {});
      },
      /** 扫描票据照片录入方式按钮操作 */
      addTicketByAtt(){
        this.$refs.ticketScan.handleAdd({"uuid":this.uuid});
      },
      //保留两位小数
      feeFormat(row, column, cellValue) {
        if (cellValue != null && cellValue != undefined) {
          return cellValue.toFixed(2);
        }
      },
      /*计算合计*/
      getTotal(param){
        const { columns, data } = param;
        const sums = [];
        columns.forEach((column, index) => {
          if (index === 0) {
            sums[index] = '合计';
            return;
          }
          if (index === 1) {
            sums[index] = this.ticketList.length+' 张';
            return;
          }
          const values = data.map(item => Number(item[column.property]));
          if (column.property === 'totalAmount') {
            sums[index] = values.reduce((prev, curr) => {
              const value = Number(curr);
              if (!isNaN(value)) {
                return prev + curr;
              } else {
                return prev;
              }
            }, 0);
            this.totalMoney = sums[index].toFixed(2)
            sums[index] = sums[index].toFixed(2)+' 元';
          }
        });
        return sums;
      },
      /**附件下载*/
      onDownload(attachment) {
        downLoadZip("/system/attachment/download/" + attachment.id);
      },
      //预览图片
      onPreview(attachment) {
        this.showViewer = true
        this.elImageViewerUrl = process.env.VUE_APP_BASE_API + attachment.filePath
      },
      // 关闭图片查看器
      closeViewer() {
        this.showViewer = false
      },
      //设置扫描票据类型
      setTicketType(value) {
        this.ticketType = "";
        this.ticketType = value;
      },
      /**附件上传*/
      uploadAtt(file) {
        const that = this ;
        const loading = this.$loading({
          lock: true,
          text: '发票识别中……',
          spinner: 'el-icon-loading',
          background: 'rgba(0, 0, 0, 0.7)'
        });

        const formData = new FormData();
        formData.append('file', file.file);
        formData.append('uuid', this.uuid);
        formData.append('fileDictType', 'ticket_type');
        formData.append('fileDictValue', this.ticketType);
        formData.append('fileDicFlag', '1');
        switch (this.ticketType) {
          case "1" :
            addInvoiceByAtt(formData).then(response => {
              if (response.code === 200) {
                loading.close();
                this.refreshTable();
                //this.$refs.invoiceDetails.handleUpdate(response.data);
                this.msgSuccess("上传成功！");
              } else {
                loading.close();
                this.$refs.invoiceDetails.handleAdd(this.uuid);
                this.msgError(response.msg);
              }
            }).catch(function (err) {
              that.$refs.invoiceDetails.handleAdd(that.uuid);
              loading.close();
            });
            break;
          case "2" :
            addQuotaByAtt(formData).then(response => {
              if (response.code === 200) {
                loading.close();
                this.refreshTable();
                //this.$refs.quotaInvoiceDetails.handleUpdate(response.data);
                this.msgSuccess("上传成功！");
              } else {
                loading.close();
                this.$refs.quotaInvoiceDetails.handleAdd(this.uuid);
                this.msgError(response.msg);
              }
            }).catch(function (err) {
              loading.close();
              that.$refs.quotaInvoiceDetails.handleAdd(that.uuid);
            });
            break;
          case "3" :

            break;
          case "4" :
            addTrainTicketByAtt(formData).then(response => {
              if (response.code === 200) {
                loading.close();
                this.refreshTable();
                //this.$refs.trainTicketDetails.handleUpdate(response.data);
                this.msgSuccess("上传成功！");
              } else {
                loading.close();
                this.$refs.trainTicketDetails.handleAdd(this.uuid);
                this.msgError(response.msg);
              }
            }).catch(function (err) {
              loading.close();
              that.$refs.trainTicketDetails.handleAdd(that.uuid);
            });
            break;
          case "5" :
            addAirTicketByAtt(formData).then(response => {
              if (response.code === 200) {
                loading.close();
                this.refreshTable();
                //this.$refs.airTicketDetails.handleUpdate(response.data);
                this.msgSuccess("上传成功！");
              } else {
                loading.close();
                this.$refs.airTicketDetails.handleAdd(this.uuid);
                this.msgError(response.msg);
              }
            }).catch(function (err) {
              loading.close();
              that.$refs.airTicketDetails.handleAdd(that.uuid);
            });
            break;
          case "7" :
            addTaxiTicketByAtt(formData).then(response => {
              if (response.code === 200) {
                loading.close();
                this.refreshTable();
                //this.$refs.airTicketDetails.handleUpdate(response.data);
                this.msgSuccess("上传成功！");
              } else {
                loading.close();
                this.$refs.taxiTicketDetails.handleAdd(this.uuid);
                this.msgError(response.msg);
              }
            }).catch(function (err) {
              loading.close();
              that.$refs.taxiTicketDetails.handleAdd(that.uuid);
            });
            break;
          default : break;

        }
      },
      /**行颜色*/
      tableRowClassName({row, rowIndex}) {
        if(row.abnormalFlag == '1' ){
          return 'important-row';
        }
        return '';
      },
    }
  };
</script>
