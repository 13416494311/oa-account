<template>
  <div>
    <el-card shadow="always" style="margin-bottom: 30px;">
      <div slot="header" style="height: 25px">
        <span style="font-weight: bold;font-size: 16px">票据录入</span>
        <el-button
          type="primary"
          icon="el-icon-plus"
          size="mini"
          @click="addInvoice"
          style="float: right;margin-top: -5px;margin-left: 10px"
          :style="{ display: visible }"
        >新增
        </el-button>
        <el-upload action="#" :http-request="addInvoiceByAtt"
                   :show-file-list="false"
                   accept="image/jpeg,image/jpg,image/gif,image/png"
                   style="float: right;margin-top: -5px"
        >
          <el-button
            size="mini"
            type="primary"
            icon="el-icon-upload"
            :style="{ display: visible }"
          >录入
          </el-button>
        </el-upload>
      </div>
      <el-table v-loading="invoiceLoading" :data="invoiceList"
                show-summary  :summary-method="getTotal" >
        <el-table-column label="发票种类" align="center" prop="invoiceType" width="150"/>
        <el-table-column label="发票名称" align="center" prop="invoiceTypeOrg" width="150"/>
        <el-table-column label="发票代码" align="center" prop="invoiceCode" width="150"/>
        <el-table-column label="发票号码" align="center" prop="invoiceNum" width="150"/>
        <el-table-column label="合计金额(元)" align="center" prop="totalAmount" width="100" :formatter="feeFormat"/>
        <el-table-column label="合计税额(元)" align="center" prop="totalTax" width="100" :formatter="feeFormat" />
        <el-table-column label="开票日期" align="center" prop="invoiceDate" width="180">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.invoiceDate, '{y}-{m}-{d}') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="购方名称" align="center" prop="purchaserName" width="200"/>
        <el-table-column label="购方纳税人识别号" align="center" prop="purchaserRegisterNum" width="150"/>
        <el-table-column label="销售方名称" align="center" prop="sellerName" width="150"/>
        <el-table-column label="销售方纳税人识别号" align="center" prop="sellerRegisterNum" width="150"/>
        <el-table-column label="收款人" align="center" prop="payee" width="100"/>
        <el-table-column label="复核人" align="center" prop="checker" width="100"/>
        <el-table-column label="开票人" align="center" prop="noteDrawer" width="100"/>
        <el-table-column label="操作" v-if='show' align="center"  width="100" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="updateInvoice(scope.row)"
            >修改</el-button>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-delete"
              @click="deleteInvoice(scope.row)"
            >删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <invoice-details  v-show="detailsShow" ref="invoiceDetails" @ok="getInvoiceList"/>
  </div>
</template>


<script>
  import {addInvoiceByAtt, listNoPageInvoice, delInvoice} from "@/api/invoice/invoice";
  import invoiceDetails from "./invoiceDetails";

  export default {
    name: "Invoice",
    components: {
      invoiceDetails,
    },
    data() {
      return {
        //是否显示
        detailsShow: false,
        // 是否可编辑
        disabled: false,
        //按钮是否显示
        visible: '',
        //是否显示操作列
        show:true,
        //遮罩层
        invoiceLoading: false,
        //发票数据
        invoiceList: [],
        //关联业务表uuid
        uuid: '',
      }
    },
    methods: {
      /**初始化关联业务uuid*/
      init(uuid,type) {
        this.uuid = uuid;
        if (type == 'see') {
          this.disabled = true;
          this.visible = 'none';
          this.show=false ;
        }else if(type == 'edit'){
          this.disabled = false;
          this.visible = '';
          this.show=true  ;
        }
        this.getInvoiceList();

      },
      /**新增发票按钮操作*/
      addInvoice() {
        this.$refs.invoiceDetails.handleAdd(this.uuid);
      },
      /**修改发票按钮操作*/
      updateInvoice(row) {
        this.$refs.invoiceDetails.handleUpdate(row);
      },
      /**上传发票照片解析发票内容*/
      addInvoiceByAtt(file) {
        const formData = new FormData();
        formData.append('file', file.file);
        formData.append('uuid', this.uuid);
        this.invoiceLoading = true ;
        addInvoiceByAtt(formData).then(response => {
          this.invoiceLoading = false ;
          if (response.code === 200) {
            this.getInvoiceList();
            this.msgSuccess(response.msg)
          } else {
            this.msgError(response.msg);
          }
        }).catch(error => {
          this.invoiceLoading = false ;
        })
      },
      //保留两位小数
      feeFormat(row, column, cellValue) {
        if (cellValue != null && cellValue != undefined) {
          return cellValue.toFixed(2);
        }
      },
      /** 获取发票列表 */
      getInvoiceList() {
        this.invoiceLoading = true;
        listNoPageInvoice({"uuid": this.uuid}).then(response => {
          this.invoiceList = response.data;
          this.invoiceLoading = false;
        });
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
            sums[index] = this.invoiceList.length+' 张';
            return;
          }
          const values = data.map(item => Number(item[column.property]));
          if (column.property === 'totalAmount'|| column.property === 'totalTax') {
            sums[index] = values.reduce((prev, curr) => {
              const value = Number(curr);
              if (!isNaN(value)) {
                return prev + curr;
              } else {
                return prev;
              }
            }, 0);
            sums[index] = sums[index].toFixed(2)+' 元';
          }
        });
        return sums;
      },
      seeInvoice(){},
      /** 删除发票按钮操作 */
      deleteInvoice(row) {
        const ids = row.id;
        this.$confirm('是否确认删除增值税发票编号为"' + ids + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return delInvoice(ids);
        }).then(() => {
          this.getInvoiceList();
          this.msgSuccess("删除成功");
        }).catch(function () {
        });
      },
    }
  }
</script>
