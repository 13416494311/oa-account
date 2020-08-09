/**
 * 通用js方法封装处理
 * Copyright (c) 2019 ruoyi
 */

const baseURL = process.env.VUE_APP_BASE_API

// 日期格式化
export function parseTime(time, pattern) {
	if (arguments.length === 0 || !time) {
		return null
	}
	const format = pattern || '{y}-{m}-{d} {h}:{i}:{s}'
	let date
	if (typeof time === 'object') {
		date = time
	} else {
		if ((typeof time === 'string') && (/^[0-9]+$/.test(time))) {
			time = parseInt(time)
		} else if (typeof time === 'string') {
			time = time.replace(new RegExp(/-/gm), '/');
		}
		if ((typeof time === 'number') && (time.toString().length === 10)) {
			time = time * 1000
		}
		date = new Date(time)
	}
	const formatObj = {
		y: date.getFullYear(),
		m: date.getMonth() + 1,
		d: date.getDate(),
		h: date.getHours(),
		i: date.getMinutes(),
		s: date.getSeconds(),
		a: date.getDay()
	}
	const time_str = format.replace(/{(y|m|d|h|i|s|a)+}/g, (result, key) => {
		let value = formatObj[key]
		// Note: getDay() returns 0 on Sunday
		if (key === 'a') { return ['日', '一', '二', '三', '四', '五', '六'][value] }
		if (result.length > 0 && value < 10) {
			value = '0' + value
		}
		return value || 0
	})
	return time_str
}

// 表单重置
export function resetForm(refName) {
	if (this.$refs[refName]) {
		this.$refs[refName].resetFields();
	}
}

// 添加日期范围
export function addDateRange(params, dateRange) {
	var search = params;
	search.beginTime = "";
	search.endTime = "";
	if (null != dateRange && '' != dateRange) {
		search.beginTime = this.dateRange[0];
		search.endTime = this.dateRange[1];
	}
	return search;
}

// 回显数据字典
export function selectDictLabel(datas, value) {
	var actions = [];
	Object.keys(datas).map((key) => {
		if (datas[key].dictValue == ('' + value)) {
			actions.push(datas[key].dictLabel);
			return false;
		}
	})
	return actions.join('');
}

// 通用下载方法
export function download(fileName) {
	window.location.href = baseURL + "/common/download?fileName=" + encodeURI(fileName) + "&delete=" + true;
}

// 字符串格式化(%s )
export function sprintf(str) {
	var args = arguments, flag = true, i = 1;
	str = str.replace(/%s/g, function () {
		var arg = args[i++];
		if (typeof arg === 'undefined') {
			flag = false;
			return '';
		}
		return arg;
	});
	return flag ? str : '';
}

// 转换字符串，undefined,null等转化为""
export function praseStrEmpty(str) {
    if (!str || str == "undefined" || str == "null") {
        return "";
    }
    return str;
}

/**
 * 构造树型结构数据
 * @param {*} data 数据源
 * @param {*} id id字段 默认 'id'
 * @param {*} parentId 父节点字段 默认 'parentId'
 * @param {*} children 孩子节点字段 默认 'children'
 * @param {*} rootId 根Id 默认 0
 */
export function handleTree(data, id, parentId, children, rootId) {
	id = id || 'id'
	parentId = parentId || 'parentId'
	children = children || 'children'
	rootId = rootId || 0
	//对源数据深度克隆
	const cloneData = JSON.parse(JSON.stringify(data))
	//循环所有项
	const treeData =  cloneData.filter(father => {
	  let branchArr = cloneData.filter(child => {
		//返回每一项的子级数组
		return father[id] === child[parentId]
	  });
	  branchArr.length > 0 ? father.children = branchArr : '';
	  //返回第一层
	  return father[parentId] === rootId;
	});
	return treeData != '' ? treeData : data;
  }

export function regionLabel(data,value){
  let values = value.split("-");
  let labels = [];
  for (let i = 0; i < data.length; i++) {
    if (data[i].value == values[0]) {
      labels[0] = data[i].label;
      if(values.length > 1){
        for (let j = 0; j < data[i].children.length; j++) {
          if (data[i].children[j].value == values[1]) {
            labels[1] = data[i].children[j].label;
            if (values.length > 2){
              for (let k = 0; k < data[i].children[j].children.length; k++) {
                if (data[i].children[j].children[k].value == values[2]) {
                  labels[2] = data[i].children[j].children[k].label;
                }
              }
            }
          }
        }
      }
    }
  }
  return labels.join("/");
}

export function regionTreeData(data,level){
  // 循环遍历json数据
  for (var i = 0; i < data.length; i++) {
    if (data[i].children.length < 1||level==2) {
      // children若为空数组，则将children设为undefined
      data[i].children = undefined;
    } else {
      // children若不为空数组，则继续 递归调用 本方法
      this.regionTreeData(data[i].children,2);
    }
  }
  return data;
}


export function uuid(){
  var s = [];
  var hexDigits = "0123456789abcdef";
  for (var i = 0; i < 36; i++) {
    s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
  }
  s[14] = "4";
  s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1);
  s[8] = s[13] = s[18] = s[23] = "-";

  this.uuidA = s.join("");
  return this.uuidA;
}

export function isPC() {
  var userAgentInfo = navigator.userAgent;
  var Agents = ["Android", "iPhone",
    "SymbianOS", "Windows Phone",
    "iPad", "iPod"];
  let flag = true;
  for (let v = 0; v < Agents.length; v++) {
    if (userAgentInfo.indexOf(Agents[v]) > 0) {
      flag = false;
      break;
    }
  }
  return flag;
}

/*数字金额转大写*/
export function convertToChinaNum(numberValue){
  var numberValue=new String(Math.round(numberValue*100)); // 数字金额  
  var chineseValue=""; // 转换后的汉字金额  
  var String1 = "零壹贰叁肆伍陆柒捌玖"; // 汉字数字  
  var String2 = "万仟佰拾亿仟佰拾万仟佰拾元角分"; // 对应单位  
  var len=numberValue.length; // numberValue 的字符串长度  
  var Ch1; // 数字的汉语读法  
  var Ch2; // 数字位的汉字读法  
  var nZero=0; // 用来计算连续的零值的个数  
  var String3; // 指定位置的数值  
  if(len>15){
    alert("超出计算范围");
    return "";
  }
  if (numberValue==0){
    chineseValue = "零元整";
    return chineseValue;
  }
  String2 = String2.substr(String2.length-len, len); // 取出对应位数的STRING2的值  
  for(var i=0; i<len; i++){
    String3 = parseInt(numberValue.substr(i, 1),10); // 取出需转换的某一位的值  
    if ( i != (len - 3) && i != (len - 7) && i != (len - 11) && i !=(len - 15) ){
      if ( String3 == 0 ){
        Ch1 = "";
        Ch2 = "";
        nZero = nZero + 1;
      }
      else if ( String3 != 0 && nZero != 0 ){
        Ch1 = "零" + String1.substr(String3, 1);
        Ch2 = String2.substr(i, 1);
        nZero = 0;
      }
      else{
        Ch1 = String1.substr(String3, 1);
        Ch2 = String2.substr(i, 1);
        nZero = 0;
      }
    }
    else{ // 该位是万亿，亿，万，元位等关键位  
      if( String3 != 0 && nZero != 0 ){
        Ch1 = "零" + String1.substr(String3, 1);
        Ch2 = String2.substr(i, 1);
        nZero = 0;
      }
      else if ( String3 != 0 && nZero == 0 ){
        Ch1 = String1.substr(String3, 1);
        Ch2 = String2.substr(i, 1);
        nZero = 0;
      }
      else if( String3 == 0 && nZero >= 3 ){
        Ch1 = "";
        Ch2 = "";
        nZero = nZero + 1;
      }
      else{
        Ch1 = "";
        Ch2 = String2.substr(i, 1);
        nZero = nZero + 1;
      }
      if( i == (len - 11) || i == (len - 3)){ // 如果该位是亿位或元位，则必须写上  
        Ch2 = String2.substr(i, 1);
      }
    }
    chineseValue = chineseValue + Ch1 + Ch2;
  }
  if ( String3 == 0 ){ // 最后一位（分）为0时，加上“整”  
    chineseValue = chineseValue + "整";
  }
  return chineseValue;
} 
