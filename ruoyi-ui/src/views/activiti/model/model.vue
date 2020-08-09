<template>
  <el-dialog
    :title="name"
    :visible="visible"
    :append-to-body="true"
    :show-close="true"
    :destroy-on-close="true"
    @close="closeModel"
    width="calc(100% - 40px)"
    style="height: calc(100% - 20px);top: -10px;bottom: 10px;overflow: hidden;">
    <iframe ref="iframe" :style="pageStyle" :src="src"></iframe>
  </el-dialog>
</template>

<script>
import {getToken} from '@/utils/auth';//登陆人的token

export default {
  components: {

  },
  data() {
    return {
      visible: false,
      id: '',
      name: '',
      url:'/modeler.html?modelId=',
      src: '',
      pageStyle:{
        overflow: 'auto',
        height: '',
        width: '',
      },
    }
  },
  mounted(){
    window.addEventListener('resize', this.setSize);
    this.setSize();
    window.getMyVue = this;//全局存入当前vue实例，供activiti调用

  },
  computed: {
    apiUrl(){
      return process.env.VUE_APP_BASE_API ;//后台部署的api服务
    },
    token() {
      return getToken();//token
    },
  },
  methods: {
    setSize(){
      this.pageStyle.height=window.innerHeight-180+'px';
      this.pageStyle.width=window.innerWidth-70+'px';
    },
    showModel(data){
      this.id = data.id;
      this.name = data.name;
      this.src='';
      this.src = this.url+data.id;
      this.visible = true;
    },
    closeModel() {
      this.src='';
      this.visible = false;
      this.$emit("close");
    },
  }
}
</script>

