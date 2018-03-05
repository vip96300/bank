package org.cloud.bank.client.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.cloud.bank.client.annotation.UserAuthor;
import org.cloud.bank.client.dto.Result;
import org.cloud.bank.client.exception.IllegalDatumValueException;
import org.cloud.bank.client.model.Datum;
import org.cloud.bank.client.service.DatumService;
import org.cloud.bank.client.service.MenuService;
import org.cloud.bank.client.util.FileUtils;
import org.cloud.bank.client.util.TxtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@RestController
@RequestMapping(value="/bank/business/datum")
public class DatumController {
	
	private static final Logger logger = LoggerFactory.getLogger(DatumController.class);
	
	@Autowired
	private DatumService datumService;
	@Autowired
	private MenuService menuService;
	
	@RequestMapping(value="/list_busid_menid",method=RequestMethod.POST)
	public Result<Map> list_busid_menid(@RequestParam(value="busid",required=true)long busid,
			@RequestParam(value="menid",required=true)long menid){
		Map<String,List> datums=datumService.mapByBusidAndMenid(busid,menid);
		return new Result<Map>(200,null,datums);
	}
	
	@RequestMapping(value="/list_busid_menid_isget",method=RequestMethod.POST)
	public Result<Map> list_busid_menid_isget(@RequestParam(value="busid",required=true)long busid,
			@RequestParam(value="menid",required=true)long menid,
			@RequestParam(value="isget",required=true)int isget){
		Map<String,List> datums=datumService.listByBusidAndMenidAndIsget(busid,menid,isget);
		return new Result<Map>(200,null,datums);
	}
	
	@UserAuthor(level=1)
	@RequestMapping(value="/list_busid_menid_isrequired",method=RequestMethod.POST)
	public Result<Map> list_busid_menid_isrequired(@RequestParam(value="busid",required=true)long busid,
			@RequestParam(value="menid",required=true)long menid,
			@RequestParam(value="isrequired",required=true)int isrequired){
		Map<String,List> datums=datumService.listByBusidAndMenidAndIsrequired(busid,menid,isrequired);
		return new Result<Map>(200,null,datums);
	}
	
	/**
	 * 合作商修改数据（忽略业务状态且不修改业务状态）
	 * @return
	 */
	@RequestMapping(value="/upd_batch",method=RequestMethod.POST)
	public Result<Object> upd_batch(@RequestParam(value="busid",required=true)long busid,
			@RequestParam(value="menid",required=true)long menid,
			@RequestParam(value="datumsJSON",required=true)String datumsJSON){
		List<Datum> datums=new Gson().fromJson(datumsJSON,new TypeToken<List<Datum>>(){}.getType()); 
		try {
			datumService.updByBatch(busid, menid, datums);
		} catch (IllegalDatumValueException e) {
			return new Result<Object>(Integer.valueOf(e.getMessage().split("_")[1]),e.getMessage().split("_")[0],null);
		}
		return new Result<Object>(200,null,null);
	}
	/**
	 * 银行端修改数据
	 * @return
	 */
	@UserAuthor(level=1)
	@RequestMapping(value="/upd_batch_bank",method=RequestMethod.POST)
	public Result<Object> upd_batch_bank(@RequestParam(value="busid",required=true)long busid,
			@RequestParam(value="menid",required=true)long menid,
			@RequestParam(value="datumsJSON",required=true)String datumsJSON){
		List<Datum> datums=new Gson().fromJson(datumsJSON,new TypeToken<List<Datum>>(){}.getType()); 
		try {
			datumService.updByBatchBank(busid, menid, datums);
		} catch (IllegalDatumValueException e) {
			return new Result<Object>(Integer.valueOf(e.getMessage().split("_")[1]),e.getMessage().split("_")[0],null);
		}
		return new Result<Object>(200,null,null);
	}
	/**
	 * 根据业务编号获取数据列表
	 * @param busid
	 * @return
	 */
	@RequestMapping(value="/list_busid_isget",method=RequestMethod.POST)
	public Result<List> list_busid_isget(@RequestParam(value="busid",required=true)long busid){
		List<Datum> datums=datumService.listByBusidAndIsget(busid);
		return new Result<List>(200,null,datums);
	}
	/**
	 * 根据业务编号获取数据集合
	 * @return
	 */
	@RequestMapping(value="/list_code_isget",method=RequestMethod.POST)
	public Result<List> list_code_isget(@RequestParam(value="code",required=true)String code){
		List<Datum> datums=datumService.listByCodeAndIsget(code);
		return new Result<List>(200,null,datums);
	}
	/**
	 * 合作商导入数据
	 * @return
	 */
	@RequestMapping(value="/leadin",method=RequestMethod.POST)
	public Result<Object> leadin(@RequestParam(value="datumsJSON",required=true)String datumsJSON){
		try {
			datumService.leadin(datumsJSON);
		} catch (IllegalDatumValueException e) {
			return new Result<Object>(500,e.getMessage(),null);
		}
		return new Result<Object>(200,null,null);
	}
	/**
	 * 银行端导出txt数据
	 * @return
	 */
	@UserAuthor(level=0)
	@RequestMapping(value="/leadout",method=RequestMethod.POST)
	public Result<Object> leadout(@RequestParam(value="busids",required=true)String busids,
			@RequestParam(value="token",required=true)String token){
		String url=datumService.leadout(busids,token);
		return new Result<Object>(200,null,url);
	}
	/**
	 * 银行端倒回上传总行的结果只能分行使用
	 * @return
	 */
	@UserAuthor(level=0)
	@RequestMapping(value="/leadback",method=RequestMethod.POST)
	public Result<Object> leadback(MultipartHttpServletRequest request){
		List<MultipartFile> files=new ArrayList<MultipartFile>();
		for(Map.Entry<String, MultipartFile> fileMap:request.getMultiFileMap().toSingleValueMap().entrySet()){
			files.add(fileMap.getValue());
		}
		logger.info("filename={}",files.get(0).getOriginalFilename());
		if(!".txt".equals(FileUtils.getSuffix(files.get(0).getOriginalFilename()))){
			return new Result<Object>(500,"file format error",null);
		}
		File file=new File(File.listRoots()[0]+"file/"+files.get(0).getOriginalFilename());
		try {
			files.get(0).transferTo(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		List<String> lines=TxtUtils.read(file);
		datumService.leadback(lines,file.getName());
		return new Result<Object>(200,null,null);
	}
}
