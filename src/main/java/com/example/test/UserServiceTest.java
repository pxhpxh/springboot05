package com.example.test;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import com.example.Springboot05Application;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;


import java.io.*;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


@SpringBootTest(classes = Springboot05Application.class)
public class UserServiceTest {

    @Test
    public  void test1(){
        System.out.println(12);
    }

    @Test
    public void test2() {
        Vector v = new Vector(10);
        List list = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            Object o = new Object();
            v.add(o);
            list.add(o);
            o = null;
        }

        for(int i = 0 ; i < v.size() ; i++){
            System.out.println(v.get(i));
            System.out.println(list.get(i));
        }
    }

    @Test
    public void test3() {
        int a = 2;
        int b = 2;
        short c = 2;

/*
        System.out.println(a == b);

        System.out.println(a == c);*/


        Integer v = 5;
        Integer h = 5;
        Integer d = new Integer(5);
        Integer e = new Integer(5);


        Integer v1 = 500;
        Integer h1 = 500;


        System.out.println(v == d);
        System.out.println(d == e);
        System.out.println(v == h);
        System.out.println(v1 == h1);


        String c3 = "789";
        String d3 = "789";
        System.out.println(c3 == d3);




    }

    @Test
    public void test34() {
        String a = "123456";
        System.out.println(a.substring(3));
        Map<Object,Object> map = new HashMap<>();
        map.put(1,"ab");
        map.put("a",123);
        map.put("key","");
        System.out.println(JSON.toJSONString(map));
        String str = JSON.toJSONString(map);
        Map  mapJson = JSONObject.parseObject(str,Map.class);
        System.out.println(mapJson);
        String xmlData = "";
        Map<String, List<Map<String, List>>> xmlMap = JSONObject.parseObject(xmlData).entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey,
                        entry -> {
                            List<Map<String, String>> list1 = JSONArray.parseObject(String.valueOf(entry.getValue()), List.class);
                            List retList = list1.stream().map(childMap-> {
                                Map<String, List> retMap = new HashMap<>();
                                for(String key : childMap.keySet()){
                                    retMap.put(key,JSONArray.parseObject(String.valueOf(childMap.get(key)), List.class));
                                }
                                return  retMap;
                            }).collect(Collectors.toList());
                            List<Map<String, List>> list = retList;
                            //List<Map<String, List>> list = JSONArray.parseObject(String.valueOf(entry.getValue()), List.class);
                            return list;
                        }
                ));
    }


    @Test
    public void test35() {

        double [] a = new double[]{0,1,2};
        String[] b = new String[4];
        double[] c = new double[4];
        c[1] = 1;
        for (double v : a) {
            System.out.println(v);
        }
        for (String v : b){
            System.out.println(v);
        }
        for (double v : c){
            System.out.println(v);
        }



    }




    @Test
    public void test37() throws ParseException {

        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = ft.parse("2022-10-10");
        Date date2 = ft.parse("2022-11-10");
        long betweenMonth = DateUtil.betweenMonth(date1, date2, false);
        System.out.println(betweenMonth);

        long betweenMonth2 = DateUtil.betweenMonth(date1, date2, true);
        System.out.println(betweenMonth2);
    }


    @Test
    public void test38() throws ParseException {

        Integer[] arr = new Integer[]{1,2,3};
        List<Integer> list  = new ArrayList<Integer>(Arrays.asList(arr));
        Iterator<Integer> iterator = list.iterator();
        Iterator<Integer> iterator2 = list.iterator();
        System.out.println(iterator == iterator2);
        while (iterator.hasNext()){
            System.out.println(iterator.next());
        }
        Map a = new HashMap();

        ArrayList arrayList = new ArrayList();
        System.out.println(arrayList instanceof  Collection);

        List lista = new LinkedList();

    }

    @Test
    public void test39() throws ParseException {

        int a = 129;
        Integer b = 129;
        Integer c = 129;
        System.out.println(a == b);
        System.out.println(b ==  129);
        System.out.println(b == c );

    }

    @Test
    public void test40() throws ParseException {

        FileReader a = null;
        FileInputStream fis = null;
        FileOutputStream fis1 = null;

        InputStreamReader isr = null;
        OutputStreamWriter osr = null;
        BufferedReader br = null;
        BufferedInputStream bi = null;
        BufferedOutputStream bo = null;
        try {
            fis1 = new FileOutputStream("file1.txt");
            bo = new BufferedOutputStream(fis1);


            fis = new FileInputStream("file.txt");
            isr = new InputStreamReader(fis, "GBK");
            osr = new OutputStreamWriter(fis1,"UTF-8");

            br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
                osr.write(line+"\r\n");
            }
            /*bi =  new BufferedInputStream(fis);
            byte[] buffer = new byte[8192];
            int data ;
            while (( data = bi.read(buffer)) != -1) {
                System.out.println(new String(buffer,0,data));
                fis1.write(new  String (buffer,0,data).getBytes("UTF-8"));
            }*/
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            if(osr != null){
                try {
                    osr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if(isr != null){
                try {
                    isr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(br != null){
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

    }



    @Test
    public  void test4(){

        Map<String, String> map = new HashMap<>();
        System.out.println(map.get("1"));
        String jsonStr = "{\"code\":0,\"msg\":\"success\",\"data\":" +
                "[{\"id\":1,\"st_user_id\":\"000C3FBD069F4C2EB7419BD899C00C2B\",\"st_account\":\"5155113\",\"st_name\":\"凌云波\",\"st_team_id\":\"402881e657c1ad0f0157c2141b6c018f\",\"st_team_name\":\"新华所五大队\",\"org_id\":\"402881e657c1ad0f0157c1e3606800a4\",\"org_name\":\"四川省新华强制隔离戒毒所\",\"create_time\":\"2019-11-08 12:03:38\",\"update_time\":\"2021-02-22 02:09:48\",\"st_status_code\":\"101\",\"st_status_text\":\"在职\",\"st_sex_text\":\"男\",\"st_idnumber\":\"510703198204250217\",\"st_nation_code\":\"1\",\"st_nation_text\":\"汉族\",\"st_post_text\":\"主管民警\",\"st_political_text\":\"中共党员\",\"dt_birthday\":\"1982-04-25\",\"st_education_code\":\"20\",\"st_education_text\":\"大学本科\",\"dt_enterdate\":\"2008-07-14\",\"dt_workdate\":\"2005-01-01\",\"st_type_code\":\"RYLX_01\",\"st_type_text\":\"警察\",\"st_fulltimeedu_code\":\"JYXL_06\",\"st_fulltimeedu_text\":\"大学\",\"st_fulgradu_major\":\"西南科技大学法学专业\",\"dt_modifytime\":\"2019-05-06 16:04:32\",\"pic_modifytime\":\"2018-04-11 15:47:16\",\"st_pic\":\"1\",\"sq_del_status\":\"0\",\"pic_url\":\"http://30.29.0.3:9090/000C3FBD069F4C2EB7419BD899C00C2B.jpg\"}," +
                "{\"id\":2,\"st_user_id\":\"0017A562AEE6420A85D908D7BD2030FD\",\"st_account\":\"5160058\",\"st_name\":\"李敏\",\"st_team_id\":\"402881e657c1ad0f0157c2141b6c018f\",\"st_team_name\":\"新华所五大队\",\"org_id\":\"402881e657c1ad0f0157c1e3606800a4\",\"org_name\":\"四川省新华强制隔离戒毒所\",\"create_time\":\"2019-11-08 12:03:38\",\"update_time\":\"2021-02-22 02:09:48\",\"st_status_code\":\"101\",\"st_status_text\":\"在职\",\"st_sex_text\":\"女\",\"st_idnumber\":\"510702197504150225\",\"st_nation_code\":\"1\",\"st_nation_text\":\"汉族\",\"st_post_text\":\"内勤干事\",\"st_political_text\":\"群众\",\"dt_birthday\":\"1975-04-15\",\"st_education_code\":\"20\",\"st_education_text\":\"大学本科\",\"dt_enterdate\":\"1995-08-01\",\"dt_workdate\":\"1995-08-01\",\"st_type_code\":\"RYLX_01\",\"st_type_text\":\"警察\",\"st_fulltimeedu_code\":null,\"st_fulltimeedu_text\":null,\"st_fulgradu_major\":null,\"dt_modifytime\":\"2018-04-27 14:20:57\",\"pic_modifytime\":\"2018-04-17 10:12:40\",\"st_pic\":\"1\",\"sq_del_status\":\"0\",\"pic_url\":\"http://30.29.0.3:9090/0017A562AEE6420A85D908D7BD2030FD.jpg\"}," +
                "{\"id\":3,\"st_user_id\":\"0190FBF947014D40B9C6C0AB4BFA794F\",\"st_account\":\"5160011\",\"st_name\":\"赵柏林\",\"st_team_id\":\"402881e657c1ad0f0157c203620b0138\",\"st_team_name\":\"新华所所领导\",\"org_id\":\"402881e657c1ad0f0157c1e3606800a4\",\"org_name\":\"四川省新华强制隔离戒毒所\",\"create_time\":\"2019-11-08 12:03:38\",\"update_time\":\"2021-02-22 02:09:48\",\"st_status_code\":\"101\",\"st_status_text\":\"在职\",\"st_sex_text\":\"男\",\"st_idnumber\":\"510702196307020910\",\"st_nation_code\":\"1\",\"st_nation_text\":\"汉族\",\"st_post_text\":\"工会主席\",\"st_political_text\":\"中共党员\",\"dt_birthday\":\"1963-07-20\",\"st_education_code\":\"20\",\"st_education_text\":\"大学本科\",\"dt_enterdate\":\"1982-08-01\",\"dt_workdate\":\"1982-08-01\",\"st_type_code\":\"RYLX_01\",\"st_type_text\":\"警察\",\"st_fulltimeedu_code\":null,\"st_fulltimeedu_text\":null,\"st_fulgradu_major\":null,\"dt_modifytime\":\"2018-04-11 13:59:54\",\"pic_modifytime\":\"2018-04-11 13:59:52\",\"st_pic\":\"1\",\"sq_del_status\":\"0\",\"pic_url\":\"http://30.29.0.3:9090/0190FBF947014D40B9C6C0AB4BFA794F.jpg\"}]}";
        System.out.println(jsonStr);
    }


    @Test
    public  void test5() throws UnsupportedEncodingException {

        Map<String,Object> map2 = null;

        System.out.println(JSON.toJSONString(null));
        HttpPost httpPost = new HttpPost("127.0.0.1");
        // 设置ContentType
        httpPost.setHeader("Content-Type", "application/json;charset=utf8");
        httpPost.setHeader("Authorization", "123456");

        StringEntity entity = new StringEntity(JSON.toJSONString(null));
        httpPost.setEntity(entity);

        Map<String, String> map = new LinkedHashMap<>();
        map.put("e",null);
        map.put("b",null);
        map.put("r","12");
        map.put("d","12");
        map.put("b","32");
        System.out.println(map.containsKey("a"));
        System.out.println(map.containsKey("b"));

        System.out.println(new BigDecimal("-0.55"));

        for(String key : map.keySet()){
            System.out.println(key + ":" + map.get(key));

        }

    }


    @Test
    public  void test6() throws UnsupportedEncodingException {
        Long  a = 100l;
        Integer b = 100;
        long c = a* b;
        System.out.println(c);
        System.out.println(a * b);
        System.out.println("对双方了解".substring(3));
        JSON.toJSONString(null);

        Map<String,Long> map = new HashMap<>();
        map.put("a",5l);
        Long d = 5l;
        add(map);
        adds(map);
        System.out.println(map);

    }

    private void adds(Map<String, Long> map) {
        map.put("a",map.get("a") + 1);
    }

    private void add(Map<String,Long> map) {
        map.put("a",map.get("a") + 1);
    }

    @Test
    public void test67() {
        System.out.println(32);
        System.out.println("" + 33);
        try {
            add123();
        } catch (RuntimeException e) {
            e.printStackTrace();
            System.out.println(132);
        }
    }

    public void add123() throws RuntimeException{
        System.out.println(213);
        try {
            throw  new RuntimeException("132");
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw  new RuntimeException(e);
        }
    }

    @Test
    public void test68() {
        String json = "{\"title\":\"json在线解析（简版） -JSON在线解析\",\"json.url\":\"https://www.sojson.com/simple_json.html\",\"keywords\":\"json在线解析\",\"功能\":[\"JSON美化\",\"JSON数据类型显示\",\"JSON数组显示角标\",\"高亮显示\",\"错误提示\",{\"备注\":[\"www.sojson.com\",\"json.la\"]}],\"加入我们\":{\"qq群\":\"259217951\"}}";
       JSONObject jsonObject = JSONObject.parseObject(json);
       jsonObject.remove("title");
        jsonObject.remove("json.url");
        System.out.println(jsonObject.toJSONString());
        System.out.println(json);

        Map map = new HashMap();
        map.remove("ss1");
        System.out.println(map);

    }

    @Test
    public void test69() {
        System.out.println(JSON.toJSONString(null));

        Map map = new HashMap();
        map.put("ss","");
        map.put("ssd",null);

        System.out.println(JSON.toJSONString(map));

    }


    private  Boolean  isBlank(Object  obj){
        if(obj == null){
            return  true;
        }
        if("".equals(obj+"")){
            return  true;
        }
        return  false;
    }


    private Boolean isNotBlank(Object  obj){
        return !isBlank(obj);
    }

    private  Boolean  isBlankOr0(Object  obj){
        if(obj == null){
            return  true;
        }
        if("".equals(obj+"")){
            return  true;
        }
        if(NumberUtils.isNumber(obj+"")){
            if(BigDecimal.ZERO.compareTo(new BigDecimal(obj+"")) == 0){
                return true;
            }
        }
        return  false;
    }

    private Boolean isNotBlankAnd0(Object  obj){
        return !isBlankOr0(obj);
    }

    @Test
    public  void test11(){


        System.out.println(isBlankOr0(0));
        System.out.println(isBlankOr0("0.00"));
        System.out.println(isNotBlankAnd0("0.00"));
        System.out.println(isNotBlankAnd0(""));

    }


}
