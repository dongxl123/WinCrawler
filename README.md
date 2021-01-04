# WinCrawler

通用爬虫框架，设计参考八爪鱼、火车头等爬虫软件，采用“页面+接口”可以混用的方式进行采集数据。
任务因中断导致没采集完数据，后续执行不重复采集相同数据。

## 特色

1. 页面+接口采集混用
2. 支持图片验证码识别
3. 支持if、for、while逻辑判断
4. 数据导出
5. 步骤规则自定义
6. 技术框架采用springboot、freemarker、rest-assured、selenium、testNG

## 使用方法

1. 执行建表语句*src/main/resources/WinCrawler.sql* (**wincrawler-core**)
2. 修改项目配置文件*src/main/resources/application.yml* (**wincrawler-core**)
3. 执行testNG文件*src/test/resources/projectname/crawler.xml* (**example**)

## 步骤配置

### 逻辑步骤

> 通用属性
- needStore  是否需要存储，一个流程只允许出现一个
- storeKey  存储数据对应的key，对应数据的唯一性
- condition 匹配到条件执行操作， 当值为true时正确匹配
- stepList  步骤列表
- delayTime  执行步骤后延迟时间
- needUpdate 是否需要更新结果数据
- updateCondition 更新结果数据判断条件，匹配则更新数据

#### if(附加数据持久化)
```json
{
    "name":"if条件判断",
    "type":"ifGroup",
    "condition":"${(ret.body.success==true)?c}",
    "needStore": true,
    "storeKey": "age_${o.age}-sex_${o.sex}-payEndYear_${o.payEndYear}",
    "stepList":[
        {
            "name":"打开登录页面",
            "type":"openWebPage",
            "url":"http://www.baidu.com/"
        }
    ],
    "delayTime": 100
}
```

#### for(附加数据持久化)
```json
{
      "name":"条件loop",
      "type":"forGroup",
      "listStr":"${descartes('age',range(30,70),'sex','[\"Sex1\",\"Sex2\"]','payEndYear','[2,3,4]')}",
      "list": [],
      "iterAlias":"o",
      "needStore": true,
      "storeKey": "age_${o.age}-sex_${o.sex}-payEndYear_${o.payEndYear}",
      "stepList":[{},{}]
 }
```

#### while(附加数据持久化)
```json
{
     "name":"while循环",
     "type":"whileGroup",
     "breakCondition":"${(ret.body.success==true)?c}",
     "maxCount":3,
     "needStore": true,
     "storeKey": "age_${o.age}-sex_${o.sex}-payEndYear_${o.payEndYear}",
     "stepList":[]
}
```

#### ifSignal(同代码if逻辑, step_continue,step_break,step_throwe)
```json
{
    "name":"if条件判断",
    "type":"ifGroup",
    "condition":"${(ret.body.success==true)?c}",
    "signal": "step_continue",
    "signalMsg": "记录到数据库的信息"
}
```

##### setV
```json
 {
     "name":"设置常量",
     "type":"setV",
     "values":{
         "origin_ids":"${pid},<#list productList as p>${p.productId}<#if p?has_next>,</#if></#list>"
     },
     "alias":"init"
 }
```


### 页面步骤

> 通用属性
- frameXPathList 定位的xpath在iframe中，支持多层iframe
- waitElementXPath 等待出现的元素出现，执行操作；超时时间为系统设置的selenium_implicitly_wait
- waitTime 等待时间，优先级比waitElementXPath低
- matchXPath 该步骤对应的xpath

#### 点击
```json
{
    "name":"选择投保人性别",
    "type":"click",
    "frameXPathList":[
        "//[@id='page-content']",
        "//[@id='WorkFrame']"
    ],
    "matchXPath":"//[@id='appSex1']",
    "hasAlert": false
}
```

#### 输入文本
```json
{
    "name":"输入用户名",
    "type":"inputText",
    "waitElementXPath":"//[@id='frmInput']",
    "matchXPath":"//[@id='username']",
    "value":"33011082001025"
}
```

#### 打开网页
```json
{
    "name":"打开登录页面",
    "type":"openWebPage",
    "url":"http://ehome.e-chinalife.com/",
    "timeOut": 5000 ,"//":"(不配置，使用默认值)"
}
```

#### 验证码校验
```json
{
    "name":"输入验证码登录",
    "type":"validateCode",
    "waitElementXPath":"//[@id='frmInput']",
    "codeImageXPath":"//[@id='validateNum']",
    "inputTextXPath":"//[@id='inputValidateNum']",
    "ajaxFlag":true,
    "presentXPathOnSuccess":"//img[@id='rightId' and contains(@src,'tick_circle.png')]",
    "presentXPathOnFailure":"//img[@id='rightId' and contains(@src,'cross_circle.png')]",
    "submitXPath":"//[@id='frmInput']/div/table/tbody/tr[4]/td/input[1]",
    "retryTimesOnFailure":3
}
```

#### 下拉框单选（select）
```json
{
    "name":"选择缴费方式",
    "type":"select",
    "matchXPath":"//select[@name='common[pay_period]' and @data-key='pay_period']",
    "selectConfig":{
        "type":"byIndex",
        "value":"${o.payPeriod}"
    }
}
```

#### 下拉框多选（select）
```json
{
    "name":"选择缴费方式",
    "type":"multiSelect",
    "matchXPath":"//select[@name='common[pay_period]' and @data-key='pay_period']",
    "selectConfigList":[{
        "type":"byIndex",
        "value":"${o.payPeriod}"
    }]
}
```

#### 下拉框选择(扩展，逻辑上)
```json
{
    "name":"选择新增计划书",
    "type":"selectEx",
    "waitElementXPath":"//[@id='menu-content']",
    "matchXPath":"//[@id='menu-flag']",
    "selectXPath":"//[@id='menu-content']/div/ul/li[@menuid=508]"
}
```

#### 提取数据
```json
{
    "name":"提取数据",
    "type":"extractData",
    "frameXPathList":[
        "//[@id='page-content']",
        "//[@id='WorkFrame']"
    ],
    "matchXPath":"//[@id='riskcheck']/tbody/tr[position()>3]",
    "ruleList":[
        {
            "name":"c2",
            "rule":".//td[2]"
        },
        {
            "name":"c3",
            "rule":".//td[3]"
        },
        {
            "name":"c4",
            "rule":".//td[4]"
        },
        {
            "name":"c5",
            "rule":".//td[5]"
        },
        {
            "name":"c6",
            "rule":".//td[6]"
        },
        {
            "name":"c7",
            "rule":".//td[7]"
        },
        {
            "name":"c8",
            "rule":".//td[8]"
        }
    ]
}
```

#### 获取cookie
```json
{
    "name":"获取cookies",
    "type":"fetchCookie",
    "matchXPath":"//[@id='menu-flag']",
    "alias":"cookie"
}
```

#### 设置cookie
```json
{
    "name":"设置cookies",
    "type":"setCookie",
    "cookies":"cookieValue"
}
```

### AJAX步骤

> 通用属性
- requestUrl 请求地址
- requestMethod 请求方式, 参考`com.winbaoxian.crawler.enums.RequestMethod`
- RequestContentType 请求content-type，参考`com.winbaoxian.crawler.enums.RequestContentType`
- requestHeader 请求头, hashMap
- requestParams 请求参数, hashMap
- requestBody 请求体，string json格式
- tpl 请求返回结果后，经过该模板处理数据后，使用名称为alias存储到context中
- storeFilePath 返回结果文件保存路径
- alias 结果别名，存储到context中，提供数据给后面步骤；为空时，不存储
- delayTime 执行完该步骤延迟时间

#### http请求
```json
{
    "name":"登录",
    "type":"http",
    "requestUrl":"https://insurance-tech-api.winbaoxian.cn/api/group/login",
    "requestParams":{
        "mobile":"${a}",
        "validateCode":"${b}"
    },
    "requestMethod":"POST",
    "requestContentType":"application/x-www-form-urlencoded",
    "alias":"ret"
}
```

#### 识别图片验证码
```json
{
    "name":"识别验证吗",
    "type":"ocr",
    "requestUrl":"http://ehome.e-chinalife.com/passport/validateNum.jsp",
    "requestHeader":{
       "cookie":"${toJSONString(homePage.cookies)}"
    },
    "alias":"ret"
}
```

#### 提取http请求数据
```json
{
    "tpl":"${toJSONString(data)}",
    "requestUrl":"https://www.baiduc.com/video/getAudioUrlList?uuid=113f5651c1704ebda34fd5b88f86114903c",
    "name":"提取数据",
    "alias":"c",
    "type":"extractDataHttp"
}
```

## 导出配置
### 使用simple简单格式导出
```json
{
    "type":"simple",
    "fileName":"template3.csv//可不设置，使用task的name",
    "titles":[
        "年龄",
        "性别",
        "交费期间",
        "保单年度",
        "年末年龄",
        "年初保费",
        "累计保费",
        "年度最高身故保障",
        "生存给付",
        "满期给付",
        "退保金"
    ],
    "valueTplList":[
        "${params.o.age}",
        "<#if params.o.sex=='Sex1'>男<#else>女</#if>",
        "<#if params.o.payEndYear==2>3<#elseif params.o.payEndYear==3>5<#elseif params.o.payEndYear==4>10<#else>0</#if>",
        "${result.c2}",
        "${result.c3}",
        "${result.c4}",
        "${result.c5}",
        "${result.c6}",
        "${result.c7}",
        "${result.c8}"
    ]
}
```

###  使用tpl导出
```json
{
    "type":"tpl",
    "fileName":"p2.csv//可不设置，使用task的name",
    "tpl":"年龄,性别,交费期间,保单年度,年末年龄,年初保费,累计保费,年度最高身故保障,生存给付,满期给付,退保金 <#list list as entity > <#if entity.result?default('')?trim?length gt 0> <#assign p = toJSON(entity.params)> <#assign result = toJSON(entity.result)> <#list result as ret> ${p.o.age},<#if p.o.sex=='Sex1'>男<#else>女</#if>,<#if p.o.payEndYear==2>3<#elseif p.o.payEndYear==3>5<#elseif p.o.payEndYear==4>10<#else>0</#if>,${ret.c2},${ret.c3},${ret.c4},${ret.c5},${ret.c6},${ret.c7},${ret.c8} </#list> <#else> ${entity.keyName},error </#if> </#list>"
}
```
---
## 联系方式

- QQ: <span><a target="_blank" href="http://wpa.qq.com/msgrd?v=3&uin=245684715&site=qq&menu=yes"><img border="0" src="http://wpa.qq.com/pa?p=2:245684715:51" alt="点击这里给我发消息" title="点击这里给我发消息"/></a></span>
- Email: dongxl123@163.com