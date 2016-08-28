package com.taoy3.freight.constant;


import com.taoy3.freight.bean.AddressBean;
import com.taoy3.freight.bean.Company;
import com.taoy3.freight.bean.ConsigneesBean;
import com.taoy3.freight.bean.CustomerBean;
import com.taoy3.freight.bean.NotifyBean;
import com.taoy3.freight.bean.OrderBean;
import com.taoy3.freight.bean.OrderEntity;
import com.taoy3.freight.bean.Port;
import com.taoy3.freight.bean.PriceEntity;
import com.taoy3.freight.bean.ShipperBean;
import com.taoy3.freight.bean.UserBean;
import com.taoy3.freight.bean.Voyage;
import com.taoy3.freight.db.PortDB;
import com.taoy3.freight.view.LockPatternView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by taoy2 on 15-9-24.
 */
public class CacheDataConstant {
    public final static List<Port> ports = new ArrayList<>();
    public static List<Company> companies = new ArrayList<>();
    public static List<LockPatternView.Cell> choosePattern;
    public static UserBean userBean;
    public static Voyage voyageEntity;
    public static List<CustomerBean> customers = new ArrayList<>();
    public static List<AddressBean> addressBeans = new ArrayList<>();
    public static HashMap<String, List<ShipperBean>> shippers = new HashMap<>();
    public static HashMap<String, List<ConsigneesBean>> consignees = new HashMap<>();
    public static HashMap<String, List<NotifyBean>> notify = new HashMap<>();
    public static HashMap<Integer, List<OrderEntity>> orderList = new HashMap<>();
    public static OrderBean orderBean;
    public static String address;
    public static PriceEntity price;
    public static List<String> currCodes = new ArrayList<>();
    private static String TAG = "CacheDataConstant";

    public static List<Port> getPorts(PortDB portDBAccess, int currentPage, int size) {
        if (portDBAccess.queryAll().size() == 0) {

            CacheDataConstant.ports.clear();
        }
        if (ports.size() == 0) {
            ports.addAll(portDBAccess.query(currentPage, size));
        }
        return ports;
    }
}
