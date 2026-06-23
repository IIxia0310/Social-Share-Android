package com.example.registerapplication.Entity.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 省份城市实体类
 */


public class LocationData {
    public static List<String> getProvinceList() {
        List<String> provinceList = new ArrayList<>();
        provinceList.add("北京市");
        provinceList.add("天津市");
        provinceList.add("上海市");
        provinceList.add("重庆市");
        provinceList.add("河北省");
        provinceList.add("山西省");
        provinceList.add("辽宁省");
        provinceList.add("吉林省");
        provinceList.add("黑龙江省");
        provinceList.add("江苏省");
        provinceList.add("浙江省");
        provinceList.add("安徽省");
        provinceList.add("福建省");
        provinceList.add("江西省");
        provinceList.add("山东省");
        provinceList.add("河南省");
        provinceList.add("湖北省");
        provinceList.add("湖南省");
        provinceList.add("广东省");
        provinceList.add("海南省");
        provinceList.add("四川省");
        provinceList.add("贵州省");
        provinceList.add("云南省");
        provinceList.add("陕西省");
        provinceList.add("甘肃省");
        provinceList.add("青海省");
        provinceList.add("台湾省");
        provinceList.add("内蒙古自治区");
        provinceList.add("广西壮族自治区");
        provinceList.add("西藏自治区");
        provinceList.add("宁夏回族自治区");
        provinceList.add("新疆维吾尔自治区");
        provinceList.add("香港特别行政区");
        provinceList.add("澳门特别行政区");
        return provinceList;
    }

    public static Map<String, List<String>> getCityMap() {
        Map<String, List<String>> cityMap = new HashMap<>();

        // 北京市
        List<String> beijingCities = new ArrayList<>();
        beijingCities.add("北京市");
        cityMap.put("北京市", beijingCities);

        // 天津市
        List<String> tianjinCities = new ArrayList<>();
        tianjinCities.add("天津市");
        cityMap.put("天津市", tianjinCities);

        // 上海市
        List<String> shanghaiCities = new ArrayList<>();
        shanghaiCities.add("上海市");
        cityMap.put("上海市", shanghaiCities);

        // 重庆市
        List<String> chongqingCities = new ArrayList<>();
        chongqingCities.add("重庆市");
        cityMap.put("重庆市", chongqingCities);

        // 河北省
        List<String> hebeiCities = new ArrayList<>();
        hebeiCities.add("石家庄市");
        hebeiCities.add("唐山市");
        hebeiCities.add("秦皇岛市");
        hebeiCities.add("邯郸市");
        hebeiCities.add("邢台市");
        hebeiCities.add("保定市");
        hebeiCities.add("张家口市");
        hebeiCities.add("承德市");
        hebeiCities.add("沧州市");
        hebeiCities.add("廊坊市");
        hebeiCities.add("衡水市");
        cityMap.put("河北省", hebeiCities);

        // 山西省
        List<String> shanxiCities = new ArrayList<>();
        shanxiCities.add("太原市");
        shanxiCities.add("大同市");
        shanxiCities.add("阳泉市");
        shanxiCities.add("长治市");
        shanxiCities.add("晋城市");
        shanxiCities.add("朔州市");
        shanxiCities.add("晋中市");
        shanxiCities.add("运城市");
        shanxiCities.add("忻州市");
        shanxiCities.add("临汾市");
        shanxiCities.add("吕梁市");
        cityMap.put("山西省", shanxiCities);

        // 辽宁省
        List<String> liaoningCities = new ArrayList<>();
        liaoningCities.add("沈阳市");
        liaoningCities.add("大连市");
        liaoningCities.add("鞍山市");
        liaoningCities.add("抚顺市");
        liaoningCities.add("本溪市");
        liaoningCities.add("丹东市");
        liaoningCities.add("锦州市");
        liaoningCities.add("营口市");
        liaoningCities.add("阜新市");
        liaoningCities.add("辽阳市");
        liaoningCities.add("盘锦市");
        liaoningCities.add("铁岭市");
        liaoningCities.add("朝阳市");
        liaoningCities.add("葫芦岛市");
        cityMap.put("辽宁省", liaoningCities);

        // 吉林省
        List<String> jilinCities = new ArrayList<>();
        jilinCities.add("长春市");
        jilinCities.add("吉林市");
        jilinCities.add("四平市");
        jilinCities.add("辽源市");
        jilinCities.add("通化市");
        jilinCities.add("白山市");
        jilinCities.add("松原市");
        jilinCities.add("白城市");
        jilinCities.add("延边朝鲜族自治州");
        cityMap.put("吉林省", jilinCities);

        // 黑龙江省
        List<String> heilongjiangCities = new ArrayList<>();
        heilongjiangCities.add("哈尔滨市");
        heilongjiangCities.add("齐齐哈尔市");
        heilongjiangCities.add("鸡西市");
        heilongjiangCities.add("鹤岗市");
        heilongjiangCities.add("双鸭山市");
        heilongjiangCities.add("大庆市");
        heilongjiangCities.add("伊春市");
        heilongjiangCities.add("佳木斯市");
        heilongjiangCities.add("七台河市");
        heilongjiangCities.add("牡丹江市");
        heilongjiangCities.add("黑河市");
        heilongjiangCities.add("绥化市");
        heilongjiangCities.add("大兴安岭地区");
        cityMap.put("黑龙江省", heilongjiangCities);

        // 江苏省
        List<String> jiangsuCities = new ArrayList<>();
        jiangsuCities.add("南京市");
        jiangsuCities.add("无锡市");
        jiangsuCities.add("徐州市");
        jiangsuCities.add("常州市");
        jiangsuCities.add("苏州市");
        jiangsuCities.add("南通市");
        jiangsuCities.add("连云港市");
        jiangsuCities.add("淮安市");
        jiangsuCities.add("盐城市");
        jiangsuCities.add("扬州市");
        jiangsuCities.add("镇江市");
        jiangsuCities.add("泰州市");
        jiangsuCities.add("宿迁市");
        cityMap.put("江苏省", jiangsuCities);

        // 浙江省
        List<String> zhejiangCities = new ArrayList<>();
        zhejiangCities.add("杭州市");
        zhejiangCities.add("宁波市");
        zhejiangCities.add("温州市");
        zhejiangCities.add("嘉兴市");
        zhejiangCities.add("湖州市");
        zhejiangCities.add("绍兴市");
        zhejiangCities.add("金华市");
        zhejiangCities.add("衢州市");
        zhejiangCities.add("舟山市");
        zhejiangCities.add("台州市");
        zhejiangCities.add("丽水市");
        cityMap.put("浙江省", zhejiangCities);

        // 安徽省
        List<String> anhuiCities = new ArrayList<>();
        anhuiCities.add("合肥市");
        anhuiCities.add("芜湖市");
        anhuiCities.add("蚌埠市");
        anhuiCities.add("淮南市");
        anhuiCities.add("马鞍山市");
        anhuiCities.add("淮北市");
        anhuiCities.add("铜陵市");
        anhuiCities.add("安庆市");
        anhuiCities.add("黄山市");
        anhuiCities.add("滁州市");
        anhuiCities.add("阜阳市");
        anhuiCities.add("宿州市");
        anhuiCities.add("巢湖市");
        anhuiCities.add("六安市");
        anhuiCities.add("亳州市");
        anhuiCities.add("池州市");
        anhuiCities.add("宣城市");
        cityMap.put("安徽省", anhuiCities);

        // 福建省
        List<String> fujianCities = new ArrayList<>();
        fujianCities.add("福州市");
        fujianCities.add("厦门市");
        fujianCities.add("莆田市");
        fujianCities.add("三明市");
        fujianCities.add("泉州市");
        fujianCities.add("漳州市");
        fujianCities.add("南平市");
        fujianCities.add("龙岩市");
        fujianCities.add("宁德市");
        cityMap.put("福建省", fujianCities);

        // 江西省
        List<String> jiangxiCities = new ArrayList<>();
        jiangxiCities.add("南昌市");
        jiangxiCities.add("景德镇市");
        jiangxiCities.add("萍乡市");
        jiangxiCities.add("九江市");
        jiangxiCities.add("新余市");
        jiangxiCities.add("鹰潭市");
        jiangxiCities.add("赣州市");
        jiangxiCities.add("吉安市");
        jiangxiCities.add("宜春市");
        jiangxiCities.add("抚州市");
        jiangxiCities.add("上饶市");
        cityMap.put("江西省", jiangxiCities);

        // 山东省
        List<String> shandongCities = new ArrayList<>();
        shandongCities.add("济南市");
        shandongCities.add("青岛市");
        shandongCities.add("淄博市");
        shandongCities.add("枣庄市");
        shandongCities.add("东营市");
        shandongCities.add("烟台市");
        shandongCities.add("潍坊市");
        shandongCities.add("济宁市");
        shandongCities.add("泰安市");
        shandongCities.add("威海市");
        shandongCities.add("日照市");
        shandongCities.add("莱芜市");
        shandongCities.add("临沂市");
        shandongCities.add("德州市");
        shandongCities.add("聊城市");
        shandongCities.add("滨州市");
        shandongCities.add("菏泽市");
        cityMap.put("山东省", shandongCities);

        // 河南省
        List<String> henanCities = new ArrayList<>();
        henanCities.add("郑州市");
        henanCities.add("开封市");
        henanCities.add("洛阳市");
        henanCities.add("平顶山市");
        henanCities.add("安阳市");
        henanCities.add("鹤壁市");
        henanCities.add("新乡市");
        henanCities.add("焦作市");
        henanCities.add("濮阳市");
        henanCities.add("许昌市");
        henanCities.add("漯河市");
        henanCities.add("三门峡市");
        henanCities.add("南阳市");
        henanCities.add("商丘市");
        henanCities.add("信阳市");
        henanCities.add("周口市");
        henanCities.add("驻马店市");
        henanCities.add("济源市");
        cityMap.put("河南省", henanCities);

        // 湖北省
        List<String> hubeiCities = new ArrayList<>();
        hubeiCities.add("武汉市");
        hubeiCities.add("黄石市");
        hubeiCities.add("十堰市");
        hubeiCities.add("宜昌市");
        hubeiCities.add("襄阳市");
        hubeiCities.add("鄂州市");
        hubeiCities.add("荆门市");
        hubeiCities.add("孝感市");
        hubeiCities.add("荆州市");
        hubeiCities.add("黄冈市");
        hubeiCities.add("咸宁市");
        hubeiCities.add("随州市");
        hubeiCities.add("恩施土家族苗族自治州");
        hubeiCities.add("仙桃市");
        hubeiCities.add("潜江市");
        hubeiCities.add("天门市");
        hubeiCities.add("神农架林区");
        cityMap.put("湖北省", hubeiCities);

        // 湖南省
        List<String> hunanCities = new ArrayList<>();
        hunanCities.add("长沙市");
        hunanCities.add("株洲市");
        hunanCities.add("湘潭市");
        hunanCities.add("衡阳市");
        hunanCities.add("邵阳市");
        hunanCities.add("岳阳市");
        hunanCities.add("常德市");
        hunanCities.add("张家界市");
        hunanCities.add("益阳市");
        hunanCities.add("郴州市");
        hunanCities.add("永州市");
        hunanCities.add("怀化市");
        hunanCities.add("娄底市");
        hunanCities.add("湘西土家族苗族自治州");
        cityMap.put("湖南省", hunanCities);

        // 广东省
        List<String> guangdongCities = new ArrayList<>();
        guangdongCities.add("广州市");
        guangdongCities.add("深圳市");
        guangdongCities.add("珠海市");
        guangdongCities.add("汕头市");
        guangdongCities.add("佛山市");
        guangdongCities.add("韶关市");
        guangdongCities.add("湛江市");
        guangdongCities.add("肇庆市");
        guangdongCities.add("江门市");
        guangdongCities.add("茂名市");
        guangdongCities.add("惠州市");
        guangdongCities.add("梅州市");
        guangdongCities.add("汕尾市");
        guangdongCities.add("河源市");
        guangdongCities.add("阳江市");
        guangdongCities.add("清远市");
        guangdongCities.add("东莞市");
        guangdongCities.add("中山市");
        guangdongCities.add("潮州市");
        guangdongCities.add("揭阳市");
        guangdongCities.add("云浮市");
        cityMap.put("广东省", guangdongCities);

        // 海南省
        List<String> hainanCities = new ArrayList<>();
        hainanCities.add("海口市");
        hainanCities.add("三亚市");
        hainanCities.add("三沙市");
        hainanCities.add("儋州市");
        cityMap.put("海南省", hainanCities);

        // 四川省
        List<String> sichuanCities = new ArrayList<>();
        sichuanCities.add("成都市");
        sichuanCities.add("自贡市");
        sichuanCities.add("攀枝花市");
        sichuanCities.add("泸州市");
        sichuanCities.add("德阳市");
        sichuanCities.add("绵阳市");
        sichuanCities.add("广元市");
        sichuanCities.add("遂宁市");
        sichuanCities.add("内江市");
        sichuanCities.add("乐山市");
        sichuanCities.add("南充市");
        sichuanCities.add("眉山市");
        sichuanCities.add("宜宾市");
        sichuanCities.add("广安市");
        sichuanCities.add("达州市");
        sichuanCities.add("雅安市");
        sichuanCities.add("巴中市");
        sichuanCities.add("资阳市");
        sichuanCities.add("阿坝藏族羌族自治州");
        sichuanCities.add("甘孜藏族自治州");
        sichuanCities.add("凉山彝族自治州");
        cityMap.put("四川省", sichuanCities);

        // 贵州省
        List<String> guizhouCities = new ArrayList<>();
        guizhouCities.add("贵阳市");
        guizhouCities.add("六盘水市");
        guizhouCities.add("遵义市");
        guizhouCities.add("安顺市");
        guizhouCities.add("毕节市");
        guizhouCities.add("铜仁市");
        guizhouCities.add("黔西南布依族苗族自治州");
        guizhouCities.add("黔东南苗族侗族自治州");
        guizhouCities.add("黔南布依族苗族自治州");
        cityMap.put("贵州省", guizhouCities);

        // 云南省
        List<String> yunnanCities = new ArrayList<>();
        yunnanCities.add("昆明市");
        yunnanCities.add("曲靖市");
        yunnanCities.add("玉溪市");
        yunnanCities.add("保山市");
        yunnanCities.add("昭通市");
        yunnanCities.add("丽江市");
        yunnanCities.add("普洱市");
        yunnanCities.add("临沧市");
        yunnanCities.add("楚雄彝族自治州");
        yunnanCities.add("红河哈尼族彝族自治州");
        yunnanCities.add("文山壮族苗族自治州");
        yunnanCities.add("西双版纳傣族自治州");
        yunnanCities.add("大理白族自治州");
        yunnanCities.add("德宏傣族景颇族自治州");
        yunnanCities.add("怒江傈僳族自治州");
        yunnanCities.add("迪庆藏族自治州");
        cityMap.put("云南省", yunnanCities);

        // 陕西省
        List<String> shaanxiCities = new ArrayList<>();
        shaanxiCities.add("西安市");
        shaanxiCities.add("铜川市");
        shaanxiCities.add("宝鸡市");
        shaanxiCities.add("咸阳市");
        shaanxiCities.add("渭南市");
        shaanxiCities.add("延安市");
        shaanxiCities.add("汉中市");
        shaanxiCities.add("榆林市");
        shaanxiCities.add("安康市");
        shaanxiCities.add("商洛市");
        cityMap.put("陕西省", shaanxiCities);

        // 甘肃省
        List<String> gansuCities = new ArrayList<>();
        gansuCities.add("兰州市");
        gansuCities.add("嘉峪关市");
        gansuCities.add("金昌市");
        gansuCities.add("白银市");
        gansuCities.add("天水市");
        gansuCities.add("武威市");
        gansuCities.add("张掖市");
        gansuCities.add("平凉市");
        gansuCities.add("酒泉市");
        gansuCities.add("庆阳市");
        gansuCities.add("定西市");
        gansuCities.add("陇南市");
        gansuCities.add("临夏回族自治州");
        gansuCities.add("甘南藏族自治州");
        cityMap.put("甘肃省", gansuCities);

        // 青海省
        List<String> qinghaiCities = new ArrayList<>();
        qinghaiCities.add("西宁市");
        qinghaiCities.add("海东市");
        qinghaiCities.add("海北藏族自治州");
        qinghaiCities.add("黄南藏族自治州");
        qinghaiCities.add("海南藏族自治州");
        qinghaiCities.add("果洛藏族自治州");
        qinghaiCities.add("玉树藏族自治州");
        qinghaiCities.add("海西蒙古族藏族自治州");
        cityMap.put("青海省", qinghaiCities);

        // 台湾省
        List<String> taiwanCities = new ArrayList<>();
        taiwanCities.add("台北市");
        taiwanCities.add("高雄市");
        taiwanCities.add("基隆市");
        taiwanCities.add("台中市");
        taiwanCities.add("台南市");
        taiwanCities.add("新竹市");
        taiwanCities.add("嘉义市");
        cityMap.put("台湾省", taiwanCities);

        // 内蒙古自治区
        List<String> innerMongoliaCities = new ArrayList<>();
        innerMongoliaCities.add("呼和浩特市");
        innerMongoliaCities.add("包头市");
        innerMongoliaCities.add("乌海市");
        innerMongoliaCities.add("赤峰市");
        innerMongoliaCities.add("通辽市");
        innerMongoliaCities.add("鄂尔多斯市");
        innerMongoliaCities.add("呼伦贝尔市");
        innerMongoliaCities.add("巴彦淖尔市");
        innerMongoliaCities.add("乌兰察布市");
        innerMongoliaCities.add("兴安盟");
        innerMongoliaCities.add("锡林郭勒盟");
        innerMongoliaCities.add("阿拉善盟");
        cityMap.put("内蒙古自治区", innerMongoliaCities);

        // 广西壮族自治区
        List<String> guangxiCities = new ArrayList<>();
        guangxiCities.add("南宁市");
        guangxiCities.add("柳州市");
        guangxiCities.add("桂林市");
        guangxiCities.add("梧州市");
        guangxiCities.add("北海市");
        guangxiCities.add("防城港市");
        guangxiCities.add("钦州市");
        guangxiCities.add("贵港市");
        guangxiCities.add("玉林市");
        guangxiCities.add("百色市");
        guangxiCities.add("贺州市");
        guangxiCities.add("河池市");
        guangxiCities.add("来宾市");
        guangxiCities.add("崇左市");
        cityMap.put("广西壮族自治区", guangxiCities);

        // 西藏自治区
        List<String> tibetCities = new ArrayList<>();
        tibetCities.add("拉萨市");
        tibetCities.add("日喀则市");
        tibetCities.add("昌都市");
        tibetCities.add("林芝市");
        tibetCities.add("山南市");
        tibetCities.add("那曲市");
        tibetCities.add("阿里地区");
        cityMap.put("西藏自治区", tibetCities);

        // 宁夏回族自治区
        List<String> ningxiaCities = new ArrayList<>();
        ningxiaCities.add("银川市");
        ningxiaCities.add("石嘴山市");
        ningxiaCities.add("吴忠市");
        ningxiaCities.add("固原市");
        ningxiaCities.add("中卫市");
        cityMap.put("宁夏回族自治区", ningxiaCities);

        // 新疆维吾尔自治区
        List<String> xinjiangCities = new ArrayList<>();
        xinjiangCities.add("乌鲁木齐市");
        xinjiangCities.add("克拉玛依市");
        xinjiangCities.add("吐鲁番市");
        xinjiangCities.add("哈密市");
        xinjiangCities.add("昌吉回族自治州");
        xinjiangCities.add("博尔塔拉蒙古自治州");
        xinjiangCities.add("巴音郭楞蒙古自治州");
        xinjiangCities.add("阿克苏地区");
        xinjiangCities.add("克孜勒苏柯尔克孜自治州");
        xinjiangCities.add("喀什地区");
        xinjiangCities.add("和田地区");
        xinjiangCities.add("伊犁哈萨克自治州");
        xinjiangCities.add("塔城地区");
        xinjiangCities.add("阿勒泰地区");
        cityMap.put("新疆维吾尔自治区", xinjiangCities);

        return cityMap;
    }
}