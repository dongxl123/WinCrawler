{
"benefitFlag":"newIndex",
"proposalObj":{
"holderCustomer":${escapeString(toJSONString(init.holderCustomer))},
"insuredCustomer":${escapeString(toJSONString(init.insuredCustomer))},
"mains":{
"${productList[0].productId}":{
"periodDiff":${escapeString(toJSONString(productList[0].periodDiff))} ,
"mainId":"${productList[0].productId}",
"addId":"",
"saleType":2,
"saleTypeValue":${amount?c},
"chargePeriod":<#if o.chargeYear ==0>1<#else>2</#if>,
"chargeYear":${o.chargeYear},
"coveragePeriod":1,
"coverageYear":${coverageYear},
"premuim":${(('prenium_'+productList[0].productId)?eval).premuim?c},
"amountOrPremuim":${amount?c},
"productId":"${productList[0].productId}",
"internalId":"${productList[0].productId}",
"totalPremuim":${(('prenium_'+productList[0].productId)?eval).premuim?c},
"premiumContribution":${('contributionPrenium_'+productList[0].productId)?eval},
"index":0,
"productVulgo":"${productList[0].productName}",
"mainName":"${productList[0].productName}",
"Premuim":${(('prenium_'+productList[0].productId)?eval).premuim?c}
}
},
"adds":{
<#if productList?size gt 1>
    <#list productList as addp>
        <#if addp_index gt 0>
            "${productList[0].productId}_${addp.productId}":{
            "periodDiff":${escapeString(toJSONString(addp.periodDiff))},
            "mainId":"",
            "addId":"${addp.productId}",
            "mp":"${productList[0].productId}",
            "saleType":2,
            "saleTypeValue":${amount?c},
            "chargePeriod":<#if addp.chargePeriod??>${addp.chargePeriod}<#elseif o.chargeYear ==0>1<#else>2</#if>,
            "chargeYear":<#if addp.chargeYear??>${addp.chargeYear}<#else>${o.chargeYear}</#if>,
            "coveragePeriod":1,
            "coverageYear":${coverageYear},
            "premuim":${(('prenium_'+addp.productId)?eval).premuim?c},
            "amountOrPremuim":${amount?c},
            "premiumContribution":${('contributionPrenium_'+addp.productId)?eval},
            "productId":"${addp.productId}",
            "totalPremuim":${(('prenium_'+addp.productId)?eval).premuim?c},
            "productVulgo":"${addp.productName}",
            "addName":"${addp.productName}",
            "Premuim":${(('prenium_'+addp.productId)?eval).premuim?c}
            },
        </#if>
        <#if  book_has_next>,</#if>
    </#list>
</#if>
},
"proposalId":"",
"saveTypeId":"",
"holdDate":"${.now?date}",
"countTotalPremiumBtn":false,
"taxInfoVo":{

},
"printYear":"1"
}
}