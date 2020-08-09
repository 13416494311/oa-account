package com.ruoyi.common.utils;


import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * @author:万登枫
 * @date: 2020/6/5
 */

public class WordUtils {
    private static final Logger log = LoggerFactory.getLogger(WordUtils.class);


    public static void main(String[] args) {
        String xmlTemplate = "test.xml";
        String imageXmlTemplate ="test.xml.rels";

        String docxTemplate = "test.docx";
        String uuid = UUID.randomUUID().toString();
        String fileOnlyName = "freemarker" + uuid + ".docx";
        String toDocxFilePath = "D:\\wdf\\"+ fileOnlyName;
        Map<String, Object> dataMap = new HashMap<>();

        List<Map<String,String>> test = new ArrayList<Map<String,String>>();
        Map<String,String> map1= new HashMap<String,String>();
        map1.put("wdf","万登枫");
        Map<String,String> map2= new HashMap<String,String>();
        map2.put("wdf","万登枫");
        test.add(map1);
        test.add(map2);
        dataMap.put("test",test);



        List<File> imageFileList=new ArrayList<File>();
        File file = new File("C:\\Users\\万登枫\\Desktop\\CPXG1]FH])TQTHT])~6FGY6.png");
        byte[] fileByte = FileUtil.getBytesByFile(file);
        BASE64Encoder encoder = new BASE64Encoder();
        String imageData = encoder.encode(fileByte);
        String fileName = "123.png";  //不能中文
        dataMap.put("wdfp", fileName);
        File targetFile = FileUtil.byteToFile(fileByte,fileName);
        imageFileList.add(targetFile);

        try {
            //带图片
            //createWordDocx(xmlTemplate, dataMap,imageXmlTemplate, imageFileList,docxTemplate,toDocxFilePath);
            //不带图片
            createWordDocx(xmlTemplate, dataMap,null, null,docxTemplate,toDocxFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(targetFile.exists()){
                targetFile.delete();
            }
        }
    }




    /**
     *
     * @param xmlTemplate   动态生成数据的docunment.xml的模板
     * @param dataMap     替换模板中的数据
     * @param imageXmlTemplate  动态生成图片的docunment.xml.rels的模板
     * @param imageFileList  放入docx中的图片文件
     * @param docxTemplate docx的模板
     * @param toDocxFilePath 生成的docx文件路径
     * @throws ZipException
     * @throws IOException
     * @Description  xmlTemplate不为空---替换docx中文字部分，imageXmlTemplate不为空--替换docx中图片
     */
    public  static void createWordDocx(String xmlTemplate,Map dataMap,String imageXmlTemplate,List<File> imageFileList,
                                       String docxTemplate,String toDocxFilePath) throws ZipException, IOException {
        //docx的模板不能为空
        if(docxTemplate==null&&"".equals(docxTemplate)){
            log.info("docx模板不能为空");
            return;
        }
        //生成docx文件路径不能为空
        if(toDocxFilePath==null&&"".equals(toDocxFilePath)){
            log.info("生成docx文件路径不能为空");
            return;
        }

        try {
            String uuid = UUID.randomUUID().toString();
            //临时文件生成的位置
            String toFilePath = WordUtils.class.getClassLoader().
                    getResource("ftl").getPath();
            File wordRoot = new File( toFilePath + File.separator+"word");
            if (!wordRoot.exists()) {
                wordRoot.mkdirs();
            }
            //创建根据数据xml模板生成数据document.xml文件,用于替换docx中数据xml文件
            File documentXml=null;
            if(xmlTemplate!=null&&!"".equals(xmlTemplate)){
                String toXmlFilePath = wordRoot +File.separator+ uuid+".xml";
                createWordDocxXml(dataMap,xmlTemplate,toXmlFilePath);
                documentXml=new File(toXmlFilePath);
            }


            //创建根据图片xml模板生成图片document.xml.rels文件,用于替换docx中图片xml文件
            File documentXmlRels=null;
            if(imageXmlTemplate!=null&&!"".equals(imageXmlTemplate)){
                String toImageXmlFilePath = wordRoot +File.separator+ uuid+".xml.rels";
                createWordDocxXml(dataMap,imageXmlTemplate,toImageXmlFilePath);
                documentXmlRels=new File(toImageXmlFilePath);
            }



            /*//获取docx文件模板
            String docxTemplatePath = WordUtils.class.getClassLoader().
                    getResource("ftl").getPath();
            String docxFileName = docxTemplatePath +File.separator+ docxTemplate;
            File docxFile = new File(docxFileName);*/

            //springboot以jar形式启动无法直接获取文件，只能以文件流方式获取  所以注释上面获取模板方法
            InputStream stream = WordUtils.class.getClassLoader().getResourceAsStream("ftl/"+docxTemplate);
            File docxFile = new File(docxTemplate);
            FileUtils.copyInputStreamToFile(stream, docxFile);
            stream.close();

            //根据docx模板创建docx临时文件  使用上面结合数据及模板生成的document.xml临时文件和document.xml.rels临时文件替换docx压缩包中的对应文件
            ZipFile zipFile = new ZipFile(docxFile);
            Enumeration<? extends ZipEntry> zipEntrys = zipFile.entries();
            ZipOutputStream zipout = new ZipOutputStream(new FileOutputStream(toDocxFilePath));
            int len=-1;
            byte[] buffer=new byte[1024];
            while(zipEntrys.hasMoreElements()) {
                ZipEntry next = zipEntrys.nextElement();
                InputStream is = zipFile.getInputStream(next);
                zipout.putNextEntry(new ZipEntry(next.toString()));
                if("word/document.xml".equals(next.toString())){     // docx临时文件采用上面生成的数据配置document.xml临时文件
                    if(documentXml!=null){
                        InputStream in = new FileInputStream(documentXml);
                        while((len = in.read(buffer))!=-1){
                            zipout.write(buffer,0,len);
                        }
                        in.close();
                        if(documentXml != null){
                            documentXml.delete();
                        }
                    }else{
                        while((len = is.read(buffer))!=-1){
                            zipout.write(buffer,0,len);
                        }
                        is.close();
                    }
                }else if("word/_rels/document.xml.rels".equals(next.toString())){   // docx临时文件采用上面生成的图片配置document.xml.rels临时文件
                    if(documentXmlRels!=null){
                        InputStream in = new FileInputStream(documentXmlRels);
                        while((len = in.read(buffer))!=-1){
                            zipout.write(buffer,0,len);
                        }
                        in.close();
                        if(documentXmlRels != null){
                            documentXmlRels.delete();
                        }
                    }else{
                        while((len = is.read(buffer))!=-1){
                            zipout.write(buffer,0,len);
                        }
                        is.close();
                    }
                }else {           //docx其他文件采用docx模板文件中的其他文件
                    while((len = is.read(buffer))!=-1){
                        zipout.write(buffer,0,len);
                    }
                    is.close();
                }
            }
            //将图片存于图片存放路径 word/media/
            if(imageFileList!=null && imageFileList.size()>0){
                for(File file : imageFileList){
                    if(file!=null && file.exists()){
                        ZipEntry next = new ZipEntry("word/media/" + file.getName().toString());
                        zipout.putNextEntry(new ZipEntry(next.toString()));
                        InputStream in = new FileInputStream(file);
                        while ((len = in.read(buffer)) != -1) {
                            zipout.write(buffer, 0, len);
                        }
                        in.close();
                        // 关闭此 entry
                        zipout.closeEntry();
                        file.delete();//删除临时图片文件
                    }
                }
            }
            zipout.close();
            docxFile.delete();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private   static void createWordDocxXml(Map dataMap, String xmlTemplate,String toXmlFilePath){
        try {
            //创建配置实例
            Configuration configuration = new Configuration();
            //设置编码
            configuration.setDefaultEncoding("UTF-8");
            //ftl模板文件统一放至 ftl 下面
            configuration.setClassForTemplateLoading(WordUtils.class,"/ftl");
            //获取模板
            Template template = configuration.getTemplate(xmlTemplate);
            File outFile = new File(toXmlFilePath);
            //如果输出目标文件夹不存在，则创建
            if (!outFile.exists()) {
                outFile.createNewFile();
            } else {
                outFile.delete();
                outFile.createNewFile();
            }
            //将模板和数据模型合并生成文件
            Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile),"UTF-8"));
            //生成文件
            template.process(dataMap, out);
            //关闭流
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




}
