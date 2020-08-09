<template>
  <div>
    <el-card shadow="always" style="margin-bottom: 30px;">
      <div slot="header" style="height: 25px">
        <span style="font-weight: bold;font-size: 16px">报销明细</span>
      </div>
      <el-card shadow="always" style="margin-bottom: 30px;">
        <div slot="header" style="height: 25px">
          <span style="font-weight: bold;font-size: 16px">差旅信息</span>
          <el-button
            type="primary"
            icon="el-icon-plus"
            size="mini"
            @click="addDetails"
            style="float: right;margin-top: -5px"
            :style="{ display: visible }"
          >新增
          </el-button>
        </div>
        <el-table v-loading="loading" :data="travelDetailsList"
                  show-summary :summary-method="getTotal" @row-dblclick="seeDetails">
          <el-table-column label="起始时间" align="center" prop="startDate" width="100">
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.startDate, '{y}-{m}-{d}') }}</span>
            </template>
          </el-table-column>
          <el-table-column label="终止时间" align="center" prop="endDate" width="100">
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.endDate, '{y}-{m}-{d}') }}</span>
            </template>
          </el-table-column>
          <el-table-column label="出发地" align="center" prop="startRegion" :formatter="regionFormat"/>
          <el-table-column label="目的地" align="center" prop="endRegion" :formatter="regionFormat"/>
          <el-table-column label="交通工具" align="center" prop="tool" :formatter="toolFormat"/>
          <el-table-column label="票据张数" align="center" prop="trafficBillNum"/>
          <el-table-column label="交通费(元)" align="center" prop="trafficFee" :formatter="feeFormat"/>
          <el-table-column label="出差天数" align="center" prop="days"/>
          <el-table-column label="出差补贴(元)" align="center" prop="subsidyFee" :formatter="feeFormat"/>
          <el-table-column label="操作" v-if='show'
                           align="center" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="text"
                icon="el-icon-edit"
                @click="editDetails(scope.row)"
              >修改
              </el-button>
              <el-button
                size="mini"
                type="text"
                icon="el-icon-delete"
                @click="deleteDetails(scope.row)"
              >删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-card>

      <el-form ref="travelForm" :model="travelForm" :rules="travelRules" label-position="top" label-width="150px">
        <el-form-item label="住宿费(元)">
          <el-input-number v-model="travelForm.hotelFee" :disabled="disabled" style="width: 100%"
                           placeholder="请输入住宿费"
                           @change="setTotalMoney"
                           :min="0" :precision="2" controls-position="right"/>
        </el-form-item>
        <el-form-item label="票据数">
          <el-input-number v-model="travelForm.hotelBillNum" :disabled="disabled" style="width: 100%"
                           placeholder="请输入住宿费票据张数"
                           :min="0" :max="100" :precision="0" controls-position="right"></el-input-number>
        </el-form-item>
        <el-form-item label="市内车费(元)">
          <el-input-number v-model="travelForm.cityFareFee" :disabled="disabled" style="width: 100%"
                           placeholder="请输入市内车费"
                           @change="setTotalMoney"
                           :min="0" :precision="2" controls-position="right"/>
        </el-form-item>
        <el-form-item label="票据数">
          <el-input-number v-model="travelForm.cityFareBillNum" :disabled="disabled" style="width: 100%"
                           placeholder="请输入市内车费票据张数"
                           :min="0" :max="100" :precision="0" controls-position="right"></el-input-number>
        </el-form-item>
        <el-form-item label="邮电费(元)">
          <el-input-number v-model="travelForm.postageFee" :disabled="disabled" style="width: 100%"
                           placeholder="请输入邮电费"
                           @change="setTotalMoney"
                           :min="0" :precision="2" controls-position="right"/>
        </el-form-item>
        <el-form-item label="票据数">
          <el-input-number v-model="travelForm.postageBillNum" :disabled="disabled" style="width: 100%"
                           placeholder="请输入邮电费票据张数"
                           :min="0" :max="100" :precision="0" controls-position="right"></el-input-number>
        </el-form-item>
        <el-form-item label="办公用品费(元)">
          <el-input-number v-model="travelForm.officeSuppliesFee" :disabled="disabled" style="width: 100%"
                           placeholder="请输入办公用品费"
                           @change="setTotalMoney"
                           :min="0" :precision="2" controls-position="right"/>
        </el-form-item>
        <el-form-item label="票据数">
          <el-input-number v-model="travelForm.officeSuppliesBillNum" :disabled="disabled" style="width: 100%"
                           placeholder="请输入办公用品费票据张数"
                           :min="0" :max="100" :precision="0" controls-position="right"></el-input-number>
        </el-form-item>
        <el-form-item label="不买卧铺补贴(元)">
          <el-input-number v-model="travelForm.bedSubsidyFee" :disabled="disabled" style="width: 100%"
                           placeholder="请输入不买卧铺补贴"
                           @change="setTotalMoney"
                           :min="0" :precision="2" controls-position="right"/>
        </el-form-item>
        <el-form-item label="票据数">
          <el-input-number v-model="travelForm.bedSubsidyBillNum" :disabled="disabled" style="width: 100%"
                           placeholder="请输入不买卧铺补贴票据张数"
                           :min="0" :max="100" :precision="0" controls-position="right"></el-input-number>
        </el-form-item>
        <el-form-item label="其他费(元)">
          <el-input-number v-model="travelForm.otherFee" :disabled="disabled" style="width: 100%"
                           placeholder="请输入其他费"
                           @change="setTotalMoney"
                           :min="0" :precision="2" controls-position="right"/>
        </el-form-item>
        <el-form-item label="票据数">
          <el-input-number v-model="travelForm.otherBillNum" :disabled="disabled" style="width: 100%"
                           placeholder="请输入其他费票据张数"
                           :min="0" :max="100" :precision="0" controls-position="right"></el-input-number>
        </el-form-item>
      </el-form>
    </el-card>
    <travel-details v-show="detailsShow" ref="travelDetails" @ok="refreshTable"/>
  </div>
</template>

<script>
  import {updateApply, addApply} from "@/api/reimbursement/apply";
  import {addTravel, updateTravel, getTravelByApplyUuid} from "@/api/reimbursement/travel";
  import {listTravelDetails, delTravelDetails} from "@/api/reimbursement/travelDetails";
  import {listRegion, getRegion} from "@/api/system/region";
  import invoice from "./invoice";
  import travelDetails from "./travelDetails";
  import attachment from "./attachment";

  export default {
    name: "Travel",
    components: {
      travelDetails,
      invoice,
      attachment
    },
    data() {
      return {
        // 是否可编辑
        disabled: false,
        //按钮是否显示
        visible: '',
        //是否显示操作列
        show: true,
        //是否显示
        detailsShow: false,
        //打开方式
        openType: '',
        // 遮罩层
        loading: false,
        // 交通工具字典
        toolOptions: [],
        //区域字典
        regionOptions: [],
        // 表单参数
        travelForm: {},
        // 表单校验
        travelRules: {},
        // 差旅报销明细表格数据
        travelDetailsList: [],
        //申请uuid
        applyUuid: '',
        //刷新合计金额
        refreshTotalMoney: false,
        //合计金额
        trafficFeeTotalMoney: undefined,
        subsidyFeeTotalMoney: undefined,
      };
    },
    watch: {
      subsidyFeeTotalMoney() {
        if (this.refreshTotalMoney) {
          let totalMoney = Number(this.trafficFeeTotalMoney) + Number(this.subsidyFeeTotalMoney);
          this.$emit("setTotalMoney", totalMoney);
          this.refreshTotalMoney = false;
        }
      }
    },
    mounted() {
    },
    created() {
      this.getDicts("travel_tool").then(response => {
        this.toolOptions = response.data;
      });
      listRegion().then(response => {
        this.regionOptions = this.regionTreeData(response.data);
      });

    },
    methods: {
      setTotalMoney() {
        this.$emit("setTotalMoney", undefined);
      },
      /**显示差旅信息*/
      init(uuid, type) {
        this.reset();
        this.applyUuid = uuid;
        this.openType = type;
        if (this.openType == 'see') {
          this.disabled = true;
          this.visible = 'none';
          this.show = false;
        } else if (this.openType == 'edit' || this.openType == 'add' || this.openType == 'audit') {
          this.disabled = false;
          this.visible = '';
          this.visible = '';
          this.show = true;
        }
        this.showTravel();
      },
      showTravel() {
        this.reset();
        this.travelForm.applyUuid = this.applyUuid;
        getTravelByApplyUuid(this.travelForm.applyUuid).then(response => {
          if (response.data != undefined) {
            this.travelForm = response.data;
            this.travelFormFormat();
          }
        });
        this.getTravelDetailsList();
      },
      refreshTable() {
        this.refreshTotalMoney = true;
        this.getTravelDetailsList();
      },
      /**差旅明细table*/
      getTravelDetailsList() {
        this.loading = true;
        listTravelDetails({"applyUuid": this.travelForm.applyUuid}).then(response => {
          this.travelDetailsList = response.data;
          this.loading = false;
          this.detailsShow = false;
        });
      },
      /**新增差旅明细按钮操作*/
      addDetails() {
        this.detailsShow = true;
        this.$refs.travelDetails.handleAdd(this.travelForm);
      },
      /**修改差旅明细按钮操作*/
      editDetails(row) {
        this.detailsShow = true;
        this.$refs.travelDetails.handleUpdate(row);
      },
      /**查看差旅明细*/
      seeDetails(row) {
        this.detailsShow = true;
        this.$refs.travelDetails.handleSee(row);
      },
      /*计算合计*/
      getTotal(param) {
        const {columns, data} = param;
        const sums = [];
        columns.forEach((column, index) => {
          if (index === 0) {
            sums[index] = '合计';
            return;
          }
          const values = data.map(item => Number(item[column.property]));
          if (column.property === 'trafficBillNum') {
            sums[index] = values.reduce((prev, curr) => {
              const value = Number(curr);
              if (!isNaN(value)) {
                return prev + curr;
              } else {
                return prev;
              }
            }, 0);
            sums[index] = sums[index] + ' 张';
          }
          if (column.property === 'days') {
            sums[index] = values.reduce((prev, curr) => {
              const value = Number(curr);
              if (!isNaN(value)) {
                return prev + curr;
              } else {
                return prev;
              }
            }, 0);
            sums[index] = sums[index] + ' 天';
          }
          if (column.property === 'trafficFee' || column.property === 'subsidyFee') {
            sums[index] = values.reduce((prev, curr) => {
              const value = Number(curr);
              if (!isNaN(value)) {
                return prev + curr;
              } else {
                return prev;
              }
            }, 0);
            if (column.property === 'trafficFee') {
              this.trafficFeeTotalMoney = sums[index].toFixed(2)
            }
            if (column.property === 'subsidyFee') {
              this.subsidyFeeTotalMoney = sums[index].toFixed(2)
            }
            sums[index] = sums[index].toFixed(2) + ' 元';
          }
        });
        return sums;
      },
      /** 删除差旅明细按钮操作 */
      deleteDetails(row) {
        const ids = row.id || this.ids;
        this.$confirm('是否确认删除差旅报销明细编号为"' + ids + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return delTravelDetails(ids);
        }).then(() => {
          this.refreshTable();
          this.msgSuccess("删除成功");
        }).catch(function () {
        });
      },
      // 交通工具字典翻译
      toolFormat(row, column) {
        let toolsLabel = [];
        let tools = row.tool.split("、")
        for (let i = 0; i < tools.length; i++) {
          toolsLabel.push(this.selectDictLabel(this.toolOptions, tools[i]))
        }
        return toolsLabel.join("、");
      },
      //区域翻译
      regionFormat(row, column, cellValue) {
        return this.regionLabel(this.regionOptions, cellValue);
      },
      //保留两位小数
      feeFormat(row, column, cellValue) {
        if (cellValue != null && cellValue != undefined) {
          return cellValue.toFixed(2);
        }
      },
      // 表单重置
      reset() {
        this.travelForm = {
          id: undefined,
          applyUuid: undefined,
          hotelBillNum: undefined,
          hotelFee: undefined,
          cityFareBillNum: undefined,
          cityFareFee: undefined,
          postageBillNum: undefined,
          postageFee: undefined,
          officeSuppliesBillNum: undefined,
          officeSuppliesFee: undefined,
          bedSubsidyBillNum: undefined,
          bedSubsidyFee: undefined,
          otherBillNum: undefined,
          otherFee: undefined,
          createTime: undefined,
        };
        this.resetForm("travelForm");
        this.travelDetailsList = [];
      },
      //表单格式化
      travelFormFormat() {
        if (this.travelForm.hotelFee == null) {
          this.travelForm.hotelFee = undefined;
        }
        if (this.travelForm.hotelBillNum == null) {
          this.travelForm.hotelBillNum = undefined;
        }
        if (this.travelForm.cityFareFee == null) {
          this.travelForm.cityFareFee = undefined;
        }
        if (this.travelForm.cityFareBillNum == null) {
          this.travelForm.cityFareBillNum = undefined;
        }
        if (this.travelForm.postageFee == null) {
          this.travelForm.postageFee = undefined;
        }
        if (this.travelForm.postageBillNum == null) {
          this.travelForm.postageBillNum = undefined;
        }
        if (this.travelForm.officeSuppliesFee == null) {
          this.travelForm.officeSuppliesFee = undefined;
        }
        if (this.travelForm.officeSuppliesBillNum == null) {
          this.travelForm.officeSuppliesBillNum = undefined;
        }
        if (this.travelForm.bedSubsidyFee == null) {
          this.travelForm.bedSubsidyFee = undefined;
        }
        if (this.travelForm.bedSubsidyBillNum == null) {
          this.travelForm.bedSubsidyBillNum = undefined;
        }
        if (this.travelForm.otherFee == null) {
          this.travelForm.otherFee = undefined;
        }
        if (this.travelForm.otherBillNum == null) {
          this.travelForm.otherBillNum = undefined;
        }
      },
      /*验证差旅表单*/
      formValidate() {
        this.$refs["travelForm"].validate(travelValid => {
          if (travelValid) {
            this.$emit("ok");
          } else {
            setTimeout(() => {
              var isError = document.getElementsByClassName("is-error");
              isError[0].querySelector('input').focus();
            }, 100);
            return false;
          }
        })
      },
      /** 提交按钮 */
      submitForm() {
        this.$refs["travelForm"].validate(travelValid => {
          if (travelValid) {
            if (this.travelForm.id != undefined) {
              updateTravel(this.travelForm).then(response => {
                if (response.code === 200) {
                  this.open = false;
                } else {
                  this.msgError(response.msg);
                }
              });
            } else {
              addTravel(this.travelForm).then(response => {
                if (response.code === 200) {
                  this.open = false;
                } else {
                  this.msgError(response.msg);
                }
              });
            }
          } else {
            setTimeout(() => {
              var isError = document.getElementsByClassName("is-error");
              isError[0].querySelector('input').focus();
            }, 100);
            return false;
          }
        })

      },

    }
  };
</script>
