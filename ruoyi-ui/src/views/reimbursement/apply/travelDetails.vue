<template>
  <div class="app-container">
    <!-- 添加或修改差旅报销明细对话框 -->
    <el-dialog :title="title" :visible.sync="open" :fullscreen="true"  append-to-body
               :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" :style="bodyStyle" label-width="150px">
        <el-form-item label="起始时间" prop="startDate">
          <el-date-picker clearable size="small" :disabled="disabled" style="width: 100%"
                          v-model="form.startDate"
                          type="date"
                          value-format="yyyy-MM-dd"
                          @change="checkDays"
                          placeholder="选择起始时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="终止时间" prop="endDate">
          <el-date-picker clearable size="small" :disabled="disabled" style="width: 100%"
                          v-model="form.endDate"
                          type="date"
                          value-format="yyyy-MM-dd"
                          @change="checkDays"
                          placeholder="选择终止时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="出发地" prop="startRegion">
          <el-cascader :disabled="disabled" style="width: 100%"
                       v-model="form.startRegion"
                       ref="startRegion"
                       :options="regionOptions"
                       :props="{ checkStrictly: true }"
                       placeholder="请输入出发地">
          </el-cascader>
        </el-form-item>
        <el-form-item label="目的地" prop="endRegion">
          <el-cascader :disabled="disabled" style="width: 100%"
                       v-model="form.endRegion"
                       ref="endRegion"
                       :options="regionOptions"
                       :props="{ checkStrictly: true }"
                       placeholder="请输入目的地">
          </el-cascader>
        </el-form-item>
        <el-form-item label="交通工具">
          <el-checkbox-group v-model="tools">
            <el-checkbox :disabled="disabled"
                         v-for="dict in toolOptions"
                         :key="dict.dictValue"
                         :label="dict.dictValue"
            >{{dict.dictLabel}}
            </el-checkbox>
          </el-checkbox-group>
        </el-form-item>
        <el-form-item label="交通票据张数" prop="trafficBillNum">
          <el-input-number v-model="form.trafficBillNum" :disabled="disabled" style="width: 100%"
                           placeholder="请输入交通票据张数"
                           :min="0" :max="100" :precision="0" controls-position="right"></el-input-number>
        </el-form-item>
        <el-form-item label="交通费(元)" prop="trafficFee">
          <el-input-number v-model="form.trafficFee" :disabled="disabled" style="width: 100%" placeholder="请输入交通费(元)"
                           :min="0" :precision="2" controls-position="right"/>
        </el-form-item>
        <el-form-item label="出差天数" prop="days">
          <el-input-number v-model="form.days" style="width: 100%" placeholder="请输入出差天数"
                           :min="1" :max="100" :precision="0" :disabled="disabled"
                           controls-position="right"></el-input-number>
        </el-form-item>
        <el-form-item label="出差补贴(元)" prop="subsidyFee">
          <el-input-number v-model="form.subsidyFee" :disabled="disabled" style="width: 100%" placeholder="请输入交通费(元)"
                           :min="0" :precision="2" controls-position="right"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer" :style="{textAlign:'center'}">
        <el-button type="primary" :style="{ display: visible }" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import {getTravelDetails, addTravelDetails, updateTravelDetails} from "@/api/reimbursement/travelDetails";
  import {listRegion} from "@/api/system/region";

  export default {
    name: "TravelDetails",
    data() {
      return {
        // 是否可编辑
        disabled: false,
        //按钮是否显示
        visible: '',
        // 遮罩层
        loading: true,
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 交通工具字典
        toolOptions: [],
        //区域
        regionOptions: [],
        // 表单参数
        form: {},
        tools: [],
        // 表单校验
        rules: {
          startDate: [
            {required: true, message: "起始时间不能为空", trigger: "blur"}
          ],
          endDate: [
            {required: true, message: "终止时间不能为空", trigger: "blur"}
          ],
          startRegion: [
            {required: true, message: "出发地不能为空", trigger: "blur"}
          ],
          endRegion: [
            {required: true, message: "目的地不能为空", trigger: "blur"}
          ],
          tool: [
            {required: true, message: "交通工具（1：飞机；2：火车；3：汽车；4：轮船；5：其他）不能为空", trigger: "blur"}
          ],
          trafficBillNum: [
            {required: true, message: "交通票据张数不能为空", trigger: "blur"}
          ],
          trafficFee: [
            {required: true, message: "交通费(元)不能为空", trigger: "blur"}
          ],
          days: [
            {required: true, message: "出差天数不能为空", trigger: "blur"}
          ],
          subsidyFee: [
            {required: true, message: "出差补贴(元)不能为空", trigger: "blur"}
          ],
        },
        bodyStyle: {
          overflowY: 'auto',
          height: '',
          marginLeft: '20%',
          paddingRight: '20%',
        },
      };
    },
    watch:{
      'form.startRegion':{
        handler(newVal, oldVal) {
          if (this.$refs.startRegion) {
            var children = this.$refs.startRegion.getCheckedNodes();
            if(children[0]!= null && children[0].children.length < 1){   //判断有没有下级
              this.$refs.startRegion.dropDownVisible = false; //监听值发生变化就关闭它
            }
          }
        }
      },
      'form.endRegion':{
        handler(newVal, oldVal) {
          if (this.$refs.endRegion) {
            var children = this.$refs.endRegion.getCheckedNodes();
            if(children[0]!= null && children[0].children.length < 1){   //判断有没有下级
              this.$refs.endRegion.dropDownVisible = false; //监听值发生变化就关闭它
            }
          }
        }
      },
    },
    mounted() {
      //window.addEventListener('resize', this.getHeight);
    },
    created() {
      this.getDicts("travel_tool").then(response => {
        this.toolOptions = response.data;
      });

      listRegion().then(response => {
        this.regionOptions = this.regionTreeData(response.data,1);
      });
    },
    methods: {
      checkDays() {
        if (this.form.startDate != undefined && this.form.endDate != undefined) {
          let startDate = Date.parse(this.form.startDate);
          let endDate = Date.parse(this.form.endDate);
          if (startDate > endDate) {
            this.msgInfo("终止时间在起始时间之后!");
            this.form.days = undefined;
            return;
          }
        }
      },
      /** 对话框自适应高度 */
      getHeight() {
        this.bodyStyle.height = window.innerHeight - 281 + 'px';
      },
      // 取消按钮
      cancel() {
        this.open = false;
        this.reset();
        this.$emit("ok");
      },
      // 表单重置
      reset() {
        this.form = {
          id: undefined,
          applyUuid: undefined,
          startDate: undefined,
          endDate: undefined,
          startRegion: undefined,
          endRegion: undefined,
          tool: "0",
          trafficBillNum: undefined,
          trafficFee: undefined,
          days: undefined,
          subsidyFee: undefined,
          createTime: undefined
        };
        this.tools = [];
        this.resetForm("form");
      },

      /** 新增按钮操作 */
      handleAdd(value) {
        this.reset();
        this.open = true;
        this.form.applyUuid = value.applyUuid;
        this.title = "添加差旅报销明细";
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.disabled = false;
        this.visible = '';
        this.reset();
        const id = row.id || this.ids
        getTravelDetails(id).then(response => {
          this.form = response.data;
          this.tools = this.form.tool.split("、");
          this.form.startRegion = this.form.startRegion.split("-");
          this.form.endRegion = this.form.endRegion.split("-");
          this.open = true;
          this.title = "修改差旅报销明细";
        });
      },
      /** 查看按钮操作 */
      handleSee(row) {
        this.disabled = true;
        this.visible = 'none';
        this.reset();
        const id = row.id || this.ids
        getTravelDetails(id).then(response => {
          this.form = response.data;
          this.tools = this.form.tool.split("、");
          this.form.startRegion = this.form.startRegion.split("-");
          this.form.endRegion = this.form.endRegion.split("-");
          this.open = true;
          this.title = "查看差旅报销明细";
        });
      },
      /** 提交按钮 */
      submitForm: function () {
        this.checkDays();
        if (this.tools.length == 0) {
          this.msgInfo("请选择交通工具!");
          return;
        }
        this.form.tool = this.tools.join("、")
        this.$refs["form"].validate(valid => {
          if (valid) {
            this.form.startRegion = this.form.startRegion.join("-");
            this.form.endRegion = this.form.endRegion.join("-");
            if (this.form.id != undefined) {
              updateTravelDetails(this.form).then(response => {
                if (response.code === 200) {
                  this.msgSuccess("修改成功");
                  this.open = false;
                  this.$emit("ok");
                } else {
                  this.msgError(response.msg);
                }
              });
            } else {
              addTravelDetails(this.form).then(response => {
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
