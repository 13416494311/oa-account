<template>
  <div>
    <el-card shadow="always" style="margin-bottom: 30px;">
      <div slot="header" style="height: 25px">
        <span style="font-weight: bold;font-size: 16px">专家/劳务信息</span>


        <el-button
          type="primary"
          icon="el-icon-plus"
          size="mini"
          @click="addPayeeExpert"
          style="float: right;margin-top: -5px;margin-left: 10px"
          :style="{ display: visible }"
        >新增
        </el-button>

        <el-upload action="#" :http-request="importDataByExcel"
                   :show-file-list="false"
                   accept=".xls,.xlsx"
                   style="float: right;margin-top: -5px;margin-left: 10px"
        >
          <el-button
            size="mini"
            type="primary"
            icon="el-icon-upload"
          >导入
          </el-button>
        </el-upload>

        <el-link type="info"
                 style="font-size:12px;float: right;margin-top: 5px;margin-left: 10px"
                 @click="importTemplate">批量导入模板</el-link>

      </div>
      <el-table v-loading="loading" :data="payeeExpertList"  @row-dblclick="seePayeeExpert"
                show-summary  :summary-method="getTotal">
        <el-table-column label="姓名" align="center" prop="name" />
        <el-table-column label="单位" align="center" prop="company" />
        <el-table-column label="身份证号" align="center" prop="cardNo" />
        <el-table-column label="手机号" align="center" prop="phone" />
        <el-table-column label="银行卡号" align="center" prop="bankNo" />
        <el-table-column label="开户行" align="center" prop="bank" />
        <el-table-column label="税前(元)" align="center" prop="preTax" :formatter="feeFormat"/>
        <el-table-column label="税后(元)" align="center" prop="aftTax" :formatter="feeFormat"/>
        <el-table-column label="操作" v-if='show' align="center" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="updatePayeeExpert(scope.row)"
            >修改</el-button>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-delete"
              @click="deletePayeeExpert(scope.row)"
            >删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <payee-expert-details  v-show="detailsShow" ref="payeeExpertDetails" @ok="refreshTable"/>
    </el-card>
  </div>
</template>


<script>
  import { listNoPagePayeeExpert, delPayeeExpert  ,importTemplate,importData } from "@/api/reimbursement/payeeExpert";
  import payeeExpertDetails from "./payeeExpertDetails";

  export default {
    name: "PayeeExpert",
    components: {
      payeeExpertDetails,
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
        loading: false,
        // 专家/劳务表格数据
        payeeExpertList: [],
        //关联业务表applyUuid
        applyUuid: '',
        //刷新合计金额
        refreshTotalMoney:false,
        //合计金额
        totalMoney:undefined,
        // 是否更新已经存在的用户数据
        updateSupport: 0,
      }
    },
    watch:{
      totalMoney(){
        if(this.refreshTotalMoney){
          this.$emit("setTotalMoney",this.totalMoney);
          this.refreshTotalMoney = false;
        }
      }

    },
    methods: {
      /** 下载模板操作 */
      importTemplate() {
        importTemplate().then(response => {
          this.download(response.msg);
        });
      },
      /**模板导入*/
      importDataByExcel(file) {
        const formData = new FormData();
        formData.append('file', file.file);
        formData.append('updateSupport', this.updateSupport);
        formData.append('applyUuid', this.applyUuid);
        importData(formData).then(response => {
          if (response.code === 200) {
            this.getList();
            this.msgSuccess(esponse.msg)
          } else {
            this.msgError(response.msg);
          }
        })
      },
      /**初始化关联业务applyUuid*/
      init(applyUuid,type) {
        this.applyUuid = applyUuid;
        if (type == 'see') {
          this.disabled = true;
          this.visible = 'none';
          this.show=false ;
        }else if(type == 'edit'){
          this.disabled = false;
          this.visible = '';
          this.show=true  ;
        }
        this.getList();

      },
      refreshTable(){
        this.refreshTotalMoney = true ;
        this.getList()
      },
      /** 查询专家/劳务列表 */
      getList() {
        this.loading = true;
        listNoPagePayeeExpert({"applyUuid": this.applyUuid}).then(response => {
          this.payeeExpertList = response.data;
          this.loading = false;
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
            sums[index] = this.payeeExpertList.length+' 人';
            return;
          }
          const values = data.map(item => Number(item[column.property]));
          if (column.property === 'preTax'|| column.property === 'aftTax') {
            sums[index] = values.reduce((prev, curr) => {
              const value = Number(curr);
              if (!isNaN(value)) {
                return prev + curr;
              } else {
                return prev;
              }
            }, 0);
            if(column.property === 'preTax'){
              this.totalMoney = sums[index].toFixed(2)
            }
            sums[index] = sums[index].toFixed(2)+' 元';
          }
        });
        return sums;
      },
      //保留两位小数
      feeFormat(row, column, cellValue) {
        if (cellValue != null && cellValue != undefined) {
          return cellValue.toFixed(2);
        }
      },
      seePayeeExpert(row){
        this.detailsShow = true;
        this.$refs.payeeExpertDetails.handleSee(row);
      },
      /**新增专家/劳务按钮操作*/
      addPayeeExpert() {
        this.detailsShow = true;
        this.$refs.payeeExpertDetails.handleAdd(this.applyUuid);
      },
      /**修改专家/劳务按钮操作*/
      updatePayeeExpert(row) {
        this.detailsShow = true;
        this.$refs.payeeExpertDetails.handleUpdate(row);
      },
      /** 删除专家/劳务按钮操作 */
      deletePayeeExpert(row) {
        const ids = row.id;
        this.$confirm('是否确认删除专家/劳务编号为"' + ids + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return delPayeeExpert(ids);
        }).then(() => {
          this.refreshTable();
          this.msgSuccess("删除成功");
        }).catch(function () {
        });
      },
    }

  }
</script>
