package com.earlywarning.util;

import com.earlywarning.entity.system.PageData;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Set;

/**
 * 导出数据到xls
 */
public class ExportXlsDown {
    public void createXls(List<PageData> list, HttpServletResponse response) {
        try {
            //创建文件本地文件
            String filePath = "导出数据.xls";
            File dbfFile = new File(filePath);
            //首先要使用Workbook类的工厂方法创建一个可写入的工作薄(Workbook)对象
            WritableWorkbook wwb = Workbook.createWorkbook(dbfFile);
            if (!dbfFile.exists() || dbfFile.isDirectory()) {
                dbfFile.createNewFile();

            }
            WritableSheet ws = wwb.createSheet("列表 1", 0);  //创建一个可写入的工作表
            //添加excel表头
//            ws.addCell(new Label(0, 0, "序号"));
//            ws.addCell(new Label(1, 0, "接种时间"));
//            ws.addCell(new Label(2, 0, "接种人"));
//            ws.addCell(new Label(3, 0, "接种疫苗"));
//            ws.addCell(new Label(4, 0, "接种医生"));
            int index = 0;
            for (int i = 0; i < list.size(); i++) {
                PageData person = list.get(i);
                Set sss = person.keySet();
                int count = 0;
                for (Object str : sss) {
                    // 精准导出
//                    if(String.valueOf(str).equals("")){ }
                    ws.addCell(new Label(count, index + 1, String.valueOf(person.get(str))));
                    //System.out.println(str);
                    count++;
                }
                index++;
            }
            wwb.write();//从内存中写入文件中
            wwb.close();//关闭资源，释放内存
            String fileName = new String("导出数据.xlsx".getBytes(), "ISO-8859-1");
            response.addHeader("Content-Disposition", "filename=" + fileName);
            OutputStream os = response.getOutputStream();
            FileInputStream fis = new java.io.FileInputStream(filePath);
            byte[] b = new byte[1024];
            int j;
            while ((j = fis.read(b)) > 0) {
                os.write(b, 0, j);
            }
            fis.close();
            os.flush();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (RowsExceededException e) {
            e.printStackTrace();
        } catch (WriteException e) {
            e.printStackTrace();
        }
    }
}
