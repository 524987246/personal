package org.great.util.generate;

import java.io.File;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.great.util.FileUtil;
import org.great.util.NameRandom;
import org.great.util.myutil.MyZipUtil;
import org.great.web.jdbc.ColumnEntity;

public class AutoVelocity {

	//模板集合
	private List<Template> templatelist = new ArrayList<Template>();
	//文件路径集合
	private List<String> list = new ArrayList<String>();
	//生成代码路径
	private String filepath="D:\\";

	public AutoVelocity() {
		// 初始化模板引擎
		VelocityEngine ve = new VelocityEngine();
		ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
		ve.setProperty("classpath.resource.loader.class",
				ClasspathResourceLoader.class.getName());
		ve.init();
		// 获取模板文件
		//模板文件路径在src/main/resources下
		templatelist.add(ve.getTemplate("template\\hellovelocity.vm"));
		templatelist.add(ve.getTemplate("template\\hellovelocity2.vm"));
		//临时文件生成的目标路径,压缩完成后删除
		list.add("src\\test\\replaceflageEntity.java");
		list.add("src\\test\\replaceflageDao.java");
	}

	public String autocode(List<ColumnEntity> resultlist, String tbname) {
		ArrayList<String> filenamelist = new ArrayList<String>();
		//
		VelocityContext ctx = new VelocityContext();
		String path = "";
		// 生成模板文件
		tbname=FileUtil.setfilenam(tbname);
		setAttrType(resultlist);// 设置java属性
		ctx.put("name", tbname);
		ctx.put("list", resultlist);
		for (int i = 0; i < templatelist.size(); i++) {
			Template template = templatelist.get(i);
			String name = list.get(i).replace("replaceflage", tbname);
			// 输出
			StringWriter sw = new StringWriter();
			template.merge(ctx, sw);
			// System.out.println(sw.toString());
			path = FileUtil.writeFile(sw.toString(), name);
			filenamelist.add(name);
		}
		// 压缩
		String zipFileName = filepath + NameRandom.filename(null) + ".zip";
		try {
			boolean bo = MyZipUtil.zipCompression(zipFileName, filenamelist);
			if (bo) {
				// 删除文件
				for (String filename : filenamelist) {
					File file = new File(filename);
					FileUtil.deleteFile(file);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "成功";
	}

	public static void setAttrType(List<ColumnEntity> list) {
		// 设置数据与java类型匹配
		for (ColumnEntity temp : list) {
			String str = temp.getDataType();
			if (str.equals("int")) {
				temp.setAttrType("Integer");
			} else if (str.equals("varchar")) {
				temp.setAttrType("String");
			} else if (str.equals("double")) {
				temp.setAttrType("Double");
			}
			// 下划线名称进行更换

			// 属性首字符大写
			str = temp.getColumnName().substring(0, 1).toUpperCase();
			str += temp.getColumnName().substring(1);
			temp.setAttrName(str);
			// System.out.println(columnEntity.getAttrName());
		}

	}
}
