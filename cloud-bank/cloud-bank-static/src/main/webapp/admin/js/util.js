/**
 * ajax
 * @param url
 * @param data
 * @returns
 */
function request(url,data){
	var hostname="http://127.0.0.1";
	data.append("token",$.cookie("token"));
	var result=null;
	$.ajax({
		url:hostname+url,
		type:"post",
		dataType:"json",
		data:data,
		async:false,
		contentType:false,  
		processData:false,
		success:function(result){
			result=eval(result);
		},
		error:function(){
			alert("system error!");
		}
	});
	return result;
}
/**
 * 获取主机名
 * @returns
 */
function getRootPath(){  
    var curWwwPath = window.document.location.href;  
    var pathName = window.document.location.pathname;  
    var pos = curWwwPath.indexOf(pathName);  
    var localhostPath = curWwwPath.substring(0, pos);  
    var projectName = pathName.substring(0, pathName.substr(1).indexOf('/')+1); 
    return localhostPath;  
}
/**
 * 获取参数
 */
function getParam(paramName){
	var reg = new RegExp("(^|&)"+ paramName +"=([^&]*)(&|$)");
	 	var r = window.location.search.substr(1).match(reg);
	if(r!=null){
		return unescape(r[2]); 
	}
	return 0;
}
/**
 * 时间戳转日期
 * @param m
 * @returns
 */
function add0(m){
	return m<10?'0'+m:m ;
}
function format(timestamp){
	var time = new Date(timestamp);
	var year = time.getFullYear();
	var month = time.getMonth()+1;
	var date = time.getDate();
	var hours = time.getHours();
	var minutes = time.getMinutes();
	var seconds = time.getSeconds();
	return year+'-'+add0(month)+'-'+add0(date)+' '+add0(hours)+':'+add0(minutes)+':'+add0(seconds);
}