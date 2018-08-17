package cn.itcast.bos.web.action.take_delivery;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSON;

import cn.itcast.bos.utils.UUIDUtils;
import cn.itcast.bos.web.action.common.BaseAction;

@Namespace("/")
@ParentPackage("struts-default")
@Controller
@Scope("prototype")
public class ImageAction extends BaseAction<Object> {

	private File imgFile;
	private String imgFileFileName;
	private String imgFileContentType;

	public void setImgFile(File imgFile) {
		this.imgFile = imgFile;
	}

	public void setImgFileFileName(String imgFileFileName) {
		this.imgFileFileName = imgFileFileName;
	}

	public void setImgFileContentType(String imgFileContentType) {
		this.imgFileContentType = imgFileContentType;
	}

	@Action(value = "image_upload")
	public String upload() {
		try {

			// savepath,磁盘上的绝对路径
			String savePath = ServletActionContext.getServletContext().getRealPath("/upload");
			// 相对项目路径,
			String saveUrl = ServletActionContext.getRequest().getContextPath() + "/upload/";
			System.out.println("saveurl:" + saveUrl);
			System.out.println("savepath1" + savePath);

			// 新的文件名
			String uuidName = UUIDUtils.getUUID();

			String ext = imgFileFileName.substring(imgFileFileName.lastIndexOf("."));

			String randomName = uuidName + ext;

			FileUtils.copyFile(imgFile, new File(savePath +"/" + randomName));
			/**
			 * //成功时 { "error" : 0, "url" : "http://www.example.com/path/to/file.ext" }
			 * //失败时 { "error" : 1, "message" : "错误信息" }
			 */

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("error", 0);
			map.put("url", saveUrl + randomName);
			String jsonString = JSON.toJSONString(map);

			ServletActionContext.getResponse().getWriter().println(jsonString);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return NONE;
	}

	@Action(value = "image_manage")
	public String manage() throws IOException {

		// 根目录路径，可以指定绝对路径，比如 d:/xxx/upload/xxx.jpg
		String rootPath = ServletActionContext.getServletContext().getRealPath("/") + "upload/";
		// 根目录URL，可以指定绝对路径，比如 http://www.yoursite.com/attached/
		String rootUrl = ServletActionContext.getRequest().getContextPath() + "/upload/";

		// 遍历目录取的文件信息
		List<Map<String, Object>> fileList = new ArrayList<Map<String, Object>>();
		// 当前上传目录
		File currentPathFile = new File(rootPath);
		// 图片扩展名
		String[] fileTypes = new String[] { "gif", "jpg", "jpeg", "png", "bmp" };

		if (currentPathFile.listFiles() != null) {
			for (File file : currentPathFile.listFiles()) {
				Map<String, Object> hash = new HashMap<String, Object>();
				String fileName = file.getName();
				if (file.isDirectory()) {
					hash.put("is_dir", true);
					hash.put("has_file", (file.listFiles() != null));
					hash.put("filesize", 0L);
					hash.put("is_photo", false);
					hash.put("filetype", "");
				} else if (file.isFile()) {
					String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
					hash.put("is_dir", false);
					hash.put("has_file", false);
					hash.put("filesize", file.length());
					hash.put("is_photo", Arrays.<String>asList(fileTypes).contains(fileExt));
					hash.put("filetype", fileExt);
				}
				hash.put("filename", fileName);
				hash.put("datetime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(file.lastModified()));
				fileList.add(hash);
			}
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("moveup_dir_path", "");
		result.put("current_dir_path", rootPath);
		result.put("current_url", rootUrl);
		result.put("total_count", fileList.size());
		result.put("file_list", fileList);

		String jsonString = JSON.toJSONString(result);

		ServletActionContext.getResponse().getWriter().println(jsonString);
		return NONE;
	}

}
