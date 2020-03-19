package com.example.logindemo;

import com.example.logindemo.entity.Item;
import com.example.logindemo.repository.EsItemRepository;
import com.example.logindemo.repository.ItemRepository;
import com.example.logindemo.service.IPackageService;
import com.example.logindemo.service.ItemService;
import com.example.logindemo.util.PageInfo;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.index.query.DisMaxQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LogindemoApplication.class)
class LogindemoApplicationTests {

    @Autowired
    ItemService itemService;

    @Autowired
    IPackageService iPackageService;

    @Autowired
    EsItemRepository esItemRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    RedissonClient redissonClient;

    @Test
    public void testItem(){
        List<Item> list = itemService.list();
        RBucket rBucket = redissonClient.getBucket("allItems");
        rBucket.set(list);
        rBucket.expire(60,TimeUnit.SECONDS);
        String result = rBucket.get().toString();
        JSONArray array = JSONArray.fromObject(result);
        Object[] resultlist = array.toArray();
        log.info(rBucket.get().toString());
    }

    @Test
    public void testpeak() {
        CloseableHttpClient httpclient = HttpClients.createDefault();

        //CloseableHttpClient httpclient = HttpClientBuilder.create().build();
        try {
            //创建post方式的http请求，包获请求的url
            HttpPost httppost = new HttpPost("http://localhost:8088/MobileServer/api/values/upLoadReqPicAndVideo");
            //配置请求相关参数
            RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(200000).setSocketTimeout(200000).build();
            //设置请求头
            httppost.setHeader("token","eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIxMDQifQ.1U_nJgrcuaroC9-OtsNoohdd89teHUeukWX9kFrPLhA");
            httppost.setConfig(requestConfig);
            //要传入的文件
            FileBody bin = new FileBody(new File("D:\\params.txt"));
            StringBody comment = new StringBody("This is comment", ContentType.MULTIPART_FORM_DATA);

            List<NameValuePair> list = new ArrayList<>();
            list.add(new BasicNameValuePair("requirementsId","1"));
            list.add(new BasicNameValuePair("measurePerson","1"));
            list.add(new BasicNameValuePair("recordPerson","1"));
            HttpEntity reqEntity = MultipartEntityBuilder.create().addPart("file", bin).addPart("comment", comment).build();
            httppost.setEntity(reqEntity);
            httppost.setEntity(new UrlEncodedFormEntity(list, HTTP.UTF_8));

            System.out.println("executing request " + httppost.getRequestLine());
            CloseableHttpResponse response = httpclient.execute(httppost);
            try {
                System.out.println(response.getStatusLine());
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    String responseEntityStr = EntityUtils.toString(response.getEntity());
                    System.out.println(responseEntityStr);
                    System.out.println("Response content length: " + resEntity.getContentLength());
                }
                EntityUtils.consume(resEntity);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                response.close();
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @Test
    public void testEsImportAll(){
        int count = itemService.importAll();
        System.out.println(count);
    }

    @Test
    public void testSaveOne(){
        Item item = new Item();
        item.setTitle("果然翁");
        esItemRepository.save(item);
    }

    @Test
    public void getOne(){
       Page<Item> page = esItemRepository.findByTitle("坚果", PageRequest.of(1,4));
       System.out.println(page);
    }

    @Test
    public void getByKeyWorld(){
        PageInfo pageInfo=new PageInfo();
        pageInfo.setPageNumber(1);
        pageInfo.setPageSize(6);
        Page<Item> page = esItemRepository.search(QueryBuilders.matchQuery("title","坚果"),pageInfo);
        System.out.println(page);
    }

    @Test
    public void testEsFindAll(){
        List<Item> list = esItemRepository.findItemsByTitle("jianguo");

        String keywords = "坚果";
        DisMaxQueryBuilder disMaxQueryBuilder = QueryBuilders.disMaxQuery();
        QueryBuilder queryBuilder1 = QueryBuilders.matchQuery("title",keywords).boost(2f);
        QueryBuilder queryBuilder2 = QueryBuilders.matchQuery("name.pinyin",keywords).boost(0.5f);
        disMaxQueryBuilder.add(queryBuilder1);
        disMaxQueryBuilder.add(queryBuilder2);
        SearchQuery searchQuery = new NativeSearchQuery(disMaxQueryBuilder);
        Page<Item> itemsPage = (Page<Item>) esItemRepository.search(disMaxQueryBuilder);
        System.out.println(itemsPage.getContent());
    }

    @Test
    public void testIsNumber(){
        String str="123d";
        Pattern digit = Pattern.compile("\\d+");
        if(digit.matcher(str).matches()){
            System.out.println("yes");
        }else{
            System.out.println("no");
        }
    }

    @Test
    public void createIndex(){
        Iterable<Item> iterable = esItemRepository.findAll(PageRequest.of(1,20,Sort.by(Sort.Direction.DESC,"id")));
        List<Item> list = new ArrayList<>();
        for (Item item : iterable) {
            list.add(item);
        }
        System.out.println(list);
    }

    @Test
    public void testFormula() {
        //假设A,B,C三个数  满足 A > 0, B < 5, C = 7， A + B < C
        //A = 1, B = 4, C = 7
        //List<Map<String, Integer>> listNum = new ArrayList();
        Map<String, Integer> map = new HashMap();
        map.put("A", -1);
        map.put("B", 4);
        map.put("C", 7); //保存值
        map.put("D",8);
        //stNum.add(map);

        //输出值
        for (Map.Entry<String, Integer> var : map.entrySet()) {
            //变量名
            String key = var.getKey();
            //变量值
            Integer value = var.getValue();
            System.out.print(key + " : " + value + " ");
        }
        System.out.println("");
        System.out.println("################################");

        //规则
        List<String> rules = new ArrayList();
/*        rules.add("A > 0");
        rules.add("B < 5");
        rules.add("C == 7");*/
        rules.add("(A + B + D) * 2 < C");
/*        rules.add("(A + B) * (2 + C) <= 20");
        rules.add("A > B");
        rules.add("A < B");*/

        //遍历规则
        for (String rule : rules) {
            Binding binding = new Binding();
            //设置变量, 遍历map
            for (Map.Entry<String, Integer> var : map.entrySet()) {
                //变量名
                String key = var.getKey();
                //变量值
                Integer value = var.getValue();
                binding.setVariable(key, value);
            }
            GroovyShell shell = new GroovyShell(binding);
            Object rst = shell.evaluate(rule);
            System.out.println("规则: " + rule + "  -  " + "返回: " +  rst);
        }
    }

    @Test
    public void findAllPackages(){
        int result = iPackageService.importAll();
        System.out.println(result);


    }
}
