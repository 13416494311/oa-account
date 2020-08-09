<template>
  <div>
  </div>
</template>
<script>
  import {  setToken } from '@/utils/auth'
  export default {
    name: 'weixin',
    data(){
      return{
      }
    },
    mounted() {
      let token = this.$route.query.token;
      setToken(token)
      this.$store.state.user.token = token;
      this.$router.push({ path: "/" });
    },
    methods: {
    }
  }
</script>
