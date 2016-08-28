package com.taoy3.freight.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by taoy2 on 15-9-21.
 */
public class ShipCompanyBean implements Serializable {

    /**
     * result : [{"name_zh":"æ¾³å¤§åˆ©äºšèˆªè¿\u0090","code":"ANL","type_bk":0,"desc_sut":"https://www.anl.com.au","tel_sutc":"NIL","name_sutc":"NIL","valid":1,"type_sut":1,"id":"495cf9bc-9925-459d-a125-fdec887f2330","desc_bk":"NIL","tel_bkc":"NIL","name_en":"ANL CONTAINER LINE","name_bkc":"NIL"},{"name_zh":"ç¾Žå\u203a½æ\u20ac»ç»Ÿè½®èˆ¹æœ\u2030é™\u0090å\u2026¬å\u008f¸","code":"APL","type_bk":0,"desc_sut":"racsihk@apl.com","tel_sutc":"NIL","name_sutc":"NIL","valid":1,"type_sut":0,"id":"362b1dfb-3ae9-4641-aeb2-d41aa357af76","desc_bk":"NIL","tel_bkc":"NIL","name_en":"PRESIDENT LINE CO.,LTD","name_bkc":"NIL"},{"name_zh":"è¾¹èˆª","code":"BENLINE","type_bk":0,"desc_sut":"NIL","tel_sutc":"0755-25181866","name_sutc":"NIL","valid":1,"type_sut":0,"id":"dad59151-4274-4f2a-847d-9b0c932f6cc9","desc_bk":"NIL","tel_bkc":"0755-25181866","name_en":"BEN LINE AGENCIES LTD.","name_bkc":"NIL"},{"name_zh":"æ™ºåˆ©èˆªè¿\u0090å\u203a½é™\u2026æœ\u2030é™\u0090å\u2026¬å\u008f¸","code":"CCNI","type_bk":0,"desc_sut":"NIL","tel_sutc":"0755-33392671","name_sutc":"NIL","valid":1,"type_sut":0,"id":"f5154fff-0c7a-45aa-97bb-6ca14d4293d3","desc_bk":"NIL","tel_bkc":"0755-33392671","name_en":"COMPANIA CHILENA DE NAVEGACION INTEROCEANICA S.A","name_bkc":"NIL"}]
     * totalCount : 60
     */
    private List<Company> result;
    private int totalCount;

    public void setResult(List<Company> result) {
        this.result = result;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<Company> getResult() {
        return result;
    }

    public int getTotalCount() {
        return totalCount;
    }


}
