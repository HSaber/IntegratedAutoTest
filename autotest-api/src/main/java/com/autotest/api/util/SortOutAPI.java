package com.autotest.api.util;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class SortOutAPI {

    private static RestHighLevelClient client ;
    private static String baseDir;

    static {
        baseDir = System.getProperty("user.dir") + "/src/main/resources/";
    }

    public static void initFile(Workbook wb,String sheetName,File file) throws Exception {
        Sheet sheet = wb.createSheet(sheetName);
        Row row = sheet.createRow(0);
        //表头字体样式
        Font headerFont = wb.createFont();
        headerFont.setBold(true);
        headerFont.setFontName("宋体");
        headerFont.setFontHeight((short) 220);
        //字体样式加在单元格样式里
        CellStyle headerCellStyle = wb.createCellStyle();
        headerCellStyle.setFont(headerFont);
        headerCellStyle.setAlignment(HorizontalAlignment.CENTER);
        String[] head={"API_Name","API_Method","Called_num","Ranking","Covered"};
        int[] column={40,15,15,15,15};
        for (int i = 0; i < head.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(head[i]);
            cell.setCellStyle(headerCellStyle);
            sheet.setColumnWidth(cell.getColumnIndex(),column[i]*256);

        }
        FileOutputStream os=new FileOutputStream(file);
        wb.write(os);
        //强制将输出流缓冲区的数据送出
        os.flush();
        os.close();

    }

    public static void createFile(String filePath,String sheetName) throws Exception {
        //filePath=SortOutAPI.class.getResource("/").getPath()+filePath;
        filePath=baseDir+filePath;
        File file=new File(filePath);
        String parent = file.getParent();
        File file2=new File(parent);
        Workbook wb=new XSSFWorkbook();

        //目录存在
        if(file2.exists()){
            //System.out.println("目录已经存在！");
            if(!file.exists()){
                //file.createNewFile();
                initFile(wb,sheetName,file);
            }
        }else{
            file2.mkdirs();
            //file.createNewFile();
            initFile(wb,sheetName,file);
        }

        wb.close();


    }

    public static void writeFile(String filePath,String sheetName,String apiName,String apiMethod,Long count,int rank) throws Exception {
        createFile(filePath,sheetName);
        //filePath=SortOutAPI.class.getResource("/"+filePath).getFile();
        filePath=new File(baseDir+filePath)+"";
        InputStream in=new FileInputStream(filePath);
        Workbook wb=new XSSFWorkbook(in);
        Sheet sheet = wb.getSheet(sheetName);
        if(sheet==null){
            initFile(wb,sheetName,new File(filePath));
            sheet = wb.getSheet(sheetName);
        }
        int rowNum=0;
        try{
             rowNum = sheet.getPhysicalNumberOfRows();
        }catch (NullPointerException e){
            System.out.println(filePath);
            System.out.println(sheetName);
            System.out.println(rank);
        }
        //int colnum=sheet.getRow(0).getPhysicalNumberOfCells();
        Row row = sheet.createRow(rowNum);
        row.createCell(0).setCellValue(apiName);
        row.createCell(1).setCellValue(apiMethod);
        row.createCell(2).setCellValue(count);
        row.createCell(3).setCellValue(rank);

        in.close();
        FileOutputStream out=new FileOutputStream(filePath);
        wb.write(out);
        out.flush();
        out.close();
        wb.close();




    }

    //1是按照功能划分excel  2是一个excel放所有api
    public static void sortOut(int type) throws Exception {

        //初始化HighLevel客户端
        final CredentialsProvider credentialsProvider =
                new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials("livetest", "yx6vV84i5sN8Fm"));

         client = new RestHighLevelClient(
                 RestClient.builder(
                new HttpHost("es-cn-4591n2ve7000derbx.elasticsearch.aliyuncs.com", 9200, "http"))
                .setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
                    @Override
                    public HttpAsyncClientBuilder customizeHttpClient(
                            HttpAsyncClientBuilder httpClientBuilder) {
                        return httpClientBuilder
                                .setDefaultCredentialsProvider(credentialsProvider);
                    }
                }
                )
         );

         //查询
        /*{"query":{"bool":{"must":[{"terms":{"kind.keyword":["SERVER","CLIENT"]}},{"term":
            {"tag_http_status_code":200}},{"range":{"timestamp":
            {"gte":1601481600000000,"lte":1617190107000000}}}],"must_not":[{"term":
            {"tag_http_method.keyword":"OPTIONS"}}]}},"aggs":{"name":{"terms":
            {"field":"name.keyword","size":"50"},"aggs":{"method":{"terms":
                {"field":"tag_http_method.keyword"}}}}},"size":0}*/
        SearchRequest searchRequest = new SearchRequest("zipkin:span:processed-*");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //查询结果条数，默认10条
        searchSourceBuilder.size(0);
        //query查询
        //searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        searchSourceBuilder.query(QueryBuilders.boolQuery()
            .must(QueryBuilders.termsQuery("kind.keyword","SERVER","CLIENT"))
                .must(QueryBuilders.termQuery("tag_http_status_code",200))
                .must(QueryBuilders.rangeQuery("timestamp")
                        .gte(1601481600000000L).lte(1617190107000000L))
                .mustNot(QueryBuilders.termQuery("tag_http_method.keyword","OPTIONS"))
        );
        //聚合查询
        TermsAggregationBuilder aggregation = AggregationBuilders.terms("name")
                .field("name.keyword").size(3000);
        //子聚合
        aggregation.subAggregation(AggregationBuilders.terms("method")
               .field("tag_http_method.keyword"));
        searchSourceBuilder.aggregation(aggregation);

        //执行查询
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest);

        //response hit数量获取
        SearchHits hits = searchResponse.getHits();
        long totalHits = hits.getTotalHits().value;
        System.out.println(totalHits);

        //获取response聚合结果
        Aggregations aggregations = searchResponse.getAggregations();
        //用来检测查询结果对不对，返回查询结果
//				for (Aggregation a:aggregations)
//				{
//					StringTerms stringTerms= (StringTerms)a;
//					System.out.println(stringTerms);
//				}

        Terms byCompanyAggregation = aggregations.get("name");
        List<? extends Terms.Bucket> buckets = byCompanyAggregation.getBuckets();
        //Set<String> groupSet=new HashSet<>();
        int num=1;
        for (Terms.Bucket bucket : buckets) {
            String apiName=bucket.getKeyAsString();
            if(apiName.startsWith("/")){
                apiName=apiName.substring(1);
            }
            String[] split = apiName.split("/");
            String apiCategory="";
            String excelName="";
            String sheetName="";
            String filePath="";
           if(type==1) {
               if (split.length < 3) {
                   System.out.println("不处理的api：" + apiName);
                   filePath = "topAPI/other/other.xlsx";
                   sheetName = "other";
               } else {
                   //api  internal文件夹
                   apiCategory = split[0];
                   //excel name
                   excelName = split[1];
                   //sheet name
                   sheetName = split[2];
                   if (sheetName.equals("") || excelName.equals("") || apiCategory.equals("")) {
                       System.out.println("不处理的api：" + apiName);
                       filePath = "topAPI/other/other.xlsx";
                       sheetName = "other";
                   } else if (sheetName.length() > 30) {
                       //sheetname太长创建不了，会自动截断，匹配不上
                       sheetName = sheetName.substring(0, 29);
                       filePath = "topAPI/" + apiCategory + "/" + excelName + ".xlsx";
                   } else {
                       filePath = "topAPI/" + apiCategory + "/" + excelName + ".xlsx";
                   }
                   //groupSet.add(apiCategory+"/"+apiProject+"/"+apiFunc+"/");
               }
           }else {
               filePath = "topAPI/all/all.xlsx";
               sheetName = "all";
           }

          /* Pattern pattern=Pattern.compile("(.+?/){3}");
            Matcher matcher = pattern.matcher(apiName);
            if(matcher.find()){
                String group=matcher.group();
                groupSet.add(group);
            }*/
            Map<String, Aggregation> aggregationMap = bucket.getAggregations().getAsMap();
            Terms categoryAggregation = (Terms) aggregationMap.get("method");

            for (Terms.Bucket method_buck : categoryAggregation.getBuckets())
            {
                String method = method_buck.getKeyAsString();
                Long method_docCount = method_buck.getDocCount();
                //System.out.println(method_docCount);
                writeFile(filePath,sheetName,apiName,method,method_docCount,num);
                System.out.println(num);
                num++;

            }
        }
        System.out.println("写入数据行数"+num);


        //关闭client
        client.close();

    }

    public static void main(String[] args) throws Exception {

        sortOut(2);
        //String filePath="topApi/api/jfintegration/jfthirdparty.xlsx";
        //createFile(filePath,"test1");
        // /api/jfintegration/jfthirdparty/pcdSendTplMsg
        //writeFile(filePath,"jfthirdparty","api/jfintegration/jfthirdparty/pcdSendTplMsg","POST",123L);
    }
}
