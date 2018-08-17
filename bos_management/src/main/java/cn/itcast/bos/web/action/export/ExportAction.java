package cn.itcast.bos.web.action.export;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletOutputStream;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.itcast.bos.domain.take_delivery.WayBill;
import cn.itcast.bos.service.take_delivery.WayBillService;
import cn.itcast.bos.utils.FileUtils;
import cn.itcast.bos.web.action.common.BaseAction;

@Namespace("/")
@ParentPackage("struts-default")
@Controller
@Scope("prototype")
public class ExportAction extends BaseAction<WayBill>{

	@Autowired
	private WayBillService wayBillService;
	
	
	@Action("report_exprotXls")
	public String exprotXls() throws IOException {
		
		List<WayBill> wayBills =wayBillService.findWayBills(model);
		//
		HSSFWorkbook hssfWorkbook =new HSSFWorkbook();
		HSSFSheet sheet = hssfWorkbook.createSheet("运单数据");
		//第一行,设置表头
		HSSFRow headRow = sheet.createRow(0);
		
		headRow.createCell(0).setCellValue("运单编号");
		
		headRow.createCell(1).setCellValue("寄件人");
		headRow.createCell(2).setCellValue("寄件人电话");
		headRow.createCell(3).setCellValue("寄件人公司");
		headRow.createCell(4).setCellValue("寄件人详细地址");
		headRow.createCell(5).setCellValue("收件人");
		headRow.createCell(6).setCellValue("收件人电话");
		headRow.createCell(7).setCellValue("收件人公司");
		headRow.createCell(8).setCellValue("收件人详细地址");;
		
		int i =1;
		for (WayBill wayBill : wayBills) {
			HSSFRow dataRow = sheet.createRow(i++);
			dataRow.createCell(0).setCellValue(wayBill.getWayBillNum());
			dataRow.createCell(1).setCellValue(wayBill.getSendName());
			dataRow.createCell(2).setCellValue(wayBill.getSendMobile());
			dataRow.createCell(3).setCellValue(wayBill.getSendCompany());
			dataRow.createCell(4).setCellValue(wayBill.getSendAddress());
			dataRow.createCell(5).setCellValue(wayBill.getRecName());
			dataRow.createCell(6).setCellValue(wayBill.getRecMobile());
			dataRow.createCell(7).setCellValue(wayBill.getRecCompany());
			dataRow.createCell(8).setCellValue(wayBill.getRecAddress());
		}
		
		
		//下载导出
		//设置头信息
		
		String fileName = "运单数据.xls";
		//1.mime类型
		ServletActionContext.getResponse().setContentType("application/vnd.ms-excel");
		
		//获得浏览器类型
		
		String agent = ServletActionContext.getRequest().getHeader("user-agent");
		//
		fileName = FileUtils.encodeDownloadFilename(fileName, agent);
		//设置浏览器打开的方式
		ServletActionContext.getResponse().setHeader("Content-Disposition", "attachment;filename="+fileName);
		 
		ServletOutputStream outputStream = ServletActionContext.getResponse().getOutputStream();
		hssfWorkbook.write(outputStream);
		hssfWorkbook.close();
		return NONE;
	}
	
	public String exportPDF() {
		return NONE;
	}
}
