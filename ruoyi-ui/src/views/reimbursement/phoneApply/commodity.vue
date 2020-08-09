<template>
  <div>
    <el-card shadow="always" style="margin-bottom: 30px;">
      <div slot="header" style="height: 25px">
        <span style="font-weight: bold;font-size: 16px">商品信息</span>
        <el-button
          type="primary"
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          style="float: right;margin-top: -5px;margin-left: 10px"
          :style="{ display: visible }"
        >新增
        </el-button>
      </div>
      <el-table v-loading="loading" :data="commodityList"
                show-summary :summary-method="getTotal" @row-dblclick="handleSee">
        <el-table-column label="商品名称" align="center" prop="name"/>
        <el-table-column label="规格型号" align="center" prop="type"/>
        <el-table-column label="单位" align="center" prop="unit"/>
        <el-table-column label="数量" align="center" prop="num"/>
        <el-table-column label="单价" align="center" prop="price" :formatter="feeFormat"/>
        <el-table-column label="金额" align="center" prop="amount" :formatter="feeFormat"/>
        <el-table-column label="税率" align="center" prop="taxRate" :formatter="feeFormat"/>
        <el-table-column label="税额" align="center" prop="tax" :formatter="feeFormat"/>
        <el-table-column label="操作" v-if='show' align="center" class-name="small-padding fixed-width">
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
    <!-- 添加或修改增值税发票商品对话框 -->
    <el-dialog :title="title" :visible.sync="open" :fullscreen="true" append-to-body
               :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-position="top" label-width="100px">
        <el-form-item label="商品名称" prop="name">
          <el-input v-model="form.name" :disabled="disabled" placeholder="请输入商品名称"/>
        </el-form-item>
        <el-form-item label="规格型号">
          <el-input v-model="form.type" :disabled="disabled" placeholder="请输入规格型号"/>
        </el-form-item>
        <el-form-item label="单位" prop="unit">
          <el-input v-model="form.unit" :disabled="disabled" placeholder="请输入单位"/>
        </el-form-item>
        <el-form-item label="数量" prop="num">
          <el-input-number v-model="form.num" :disabled="disabled" style="width: 100%"
                           placeholder="请输入数量"
                           :min="0" :precision="0" controls-position="right"/>
        </el-form-item>
        <el-form-item label="单价" prop="price">
          <el-input-number v-model="form.price" :disabled="disabled" style="width: 100%"
                           placeholder="请输入单价"
                           :min="0" :precision="2" controls-position="right"/>
        </el-form-item>
        <el-form-item label="金额" prop="amount">
          <el-input-number v-model="form.amount" :disabled="disabled" style="width: 100%"
                           placeholder="请输入金额"
                           :min="0" :precision="2" controls-position="right"/>
        </el-form-item>
        <el-form-item label="税率" prop="taxRate">
          <el-input-number v-model="form.taxRate" :disabled="disabled" style="width: 100%"
                           placeholder="请输入税率"
                           :min="0" :precision="2" controls-position="right"/>
        </el-form-item>
        <el-form-item label="税额" prop="tax">
          <el-input-number v-model="form.tax" :disabled="disabled" style="width: 100%"
                           placeholder="请输入税额"
                           :min="0" :precision="2" controls-position="right"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer" :style="{textAlign:'center'}">
        <el-button type="primary":style="{ display: visible }" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import {
    listCommodity,
    listCommodityNoPage,
    getCommodity,
    delCommodity,
    addCommodity,
    updateCommodity,
    exportCommodity
  } from "@/api/invoice/commodity";

  export default {
    name: "Commodity",
    data() {
      return {
        // 是否可编辑
        disabled: false,
        //按钮是否显示
        visible: '',
        //是否显示操作列
        show: true,
        // 遮罩层
        loading: true,
        // 增值税发票商品表格数据
        commodityList: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 表单参数
        form: {},
        // 表单校验
        rules: {
          name: [
            {required: true, message: "商品名称不能为空", trigger: "blur"}
          ],
          amount: [
            {required: true, message: "金额不能为空", trigger: "blur"}
          ],
        },
        bodyStyle: {
          overflowY: 'auto',
          height: '',
          marginLeft: '20%',
          paddingRight: '20%',
        },
        invoiceUuid:'',
      };
    },
    mounted() {
      //window.addEventListener('resize', this.getHeight);
    },
    created() {

    },
    methods: {
      init(uuid,type){
        this.reset();
        this.form.invoiceUuid = uuid;
        this.invoiceUuid = uuid;
        if (type == 'see') {
          this.disabled = true;
          this.visible = 'none';
          this.show=false ;
        }else if(type == 'edit'|| type == 'add'|| type == 'audit'){
          this.disabled = false;
          this.visible = '';
          this.visible = '';
          this.show=true  ;
        }
        this.getList();
      },
      /** 对话框自适应高度 */
      getHeight() {
        this.bodyStyle.height = window.innerHeight - 281 + 'px';
      },
      /** 查询增值税发票商品列表 */
      getList() {
        this.loading = true;
        listCommodityNoPage({"invoiceUuid":this.form.invoiceUuid}).then(response => {
          this.commodityList = response.data;
          this.loading = false;
        });
      },
      // 取消按钮
      cancel() {
        this.open = false;
        this.reset();
      },
      //表单格式化
      formFormat() {
        if (this.form.num == null) {
          this.form.num = undefined;
        }
        if (this.form.price == null) {
          this.form.price = undefined;
        }
        if (this.form.amount == null) {
          this.form.amount = undefined;
        }
        if (this.form.taxRate == null) {
          this.form.taxRate = undefined;
        }
        if (this.form.tax == null) {
          this.form.tax = undefined;
        }
      },
      // 表单重置
      reset() {
        this.form = {
          id: undefined,
          invoiceUuid: undefined,
          name: undefined,
          type: undefined,
          unit: undefined,
          num: undefined,
          price: undefined,
          amount: undefined,
          taxRate: undefined,
          tax: undefined,
          createTime: undefined
        };
        this.resetForm("form");
      },
      /** 查看按钮操作 */
      handleSee(row){
        this.reset();
        const id = row.id || this.ids
        getCommodity(id).then(response => {
          this.form = response.data;
          this.formFormat();
          this.open = true;
          this.title = "查看增值税发票商品";
        });
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.reset();
        this.form.invoiceUuid = this.invoiceUuid ;
        this.open = true;
        this.title = "添加增值税发票商品";
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        const id = row.id || this.ids
        getCommodity(id).then(response => {
          this.form = response.data;
          this.formFormat();
          this.open = true;
          this.title = "修改增值税发票商品";
        });
      },
      /** 提交按钮 */
      submitForm: function () {
        this.$refs["form"].validate(valid => {
          if (valid) {
            if (this.form.id != undefined) {
              updateCommodity(this.form).then(response => {
                if (response.code === 200) {
                  this.msgSuccess("修改成功");
                  this.open = false;
                  this.getList();
                } else {
                  this.msgError(response.msg);
                }
              });
            } else {
              addCommodity(this.form).then(response => {
                if (response.code === 200) {
                  this.msgSuccess("新增成功");
                  this.open = false;
                  this.getList();
                } else {
                  this.msgError(response.msg);
                }
              });
            }
          }
        });
      },
      /** 删除按钮操作 */
      handleDelete(row) {
        const ids = row.id || this.ids;
        this.$confirm('是否确认删除增值税发票商品编号为"' + ids + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return delCommodity(ids);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(function () {
        });
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
          if (column.property === 'amount' || column.property === 'tax') {
            sums[index] = values.reduce((prev, curr) => {
              const value = Number(curr);
              if (!isNaN(value)) {
                return prev + curr;
              } else {
                return prev;
              }
            }, 0);
            sums[index] = sums[index].toFixed(2) + ' 元';
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
    }
  };
</script>
