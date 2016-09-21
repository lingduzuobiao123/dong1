package com.dongframe.demo.db;

import java.util.ArrayList;
import java.util.List;

import com.dongframe.demo.infos.DeviceInfo;
import com.dongframe.demo.utils.LogUtils;
import com.dongframe.demo.utils.StringUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

/**
 * @author dongxl
 * 
 */
public class MacDeviceDao extends BaseDBDao
{
    private static final String TAG = MacDeviceDao.class.getSimpleName();
    
    private static MacDeviceDao instance = null;
    
    public static MacDeviceDao getInstance(Context context)
    {
        if (instance == null)
        {
            instance = new MacDeviceDao(context);
        }
        return instance;
    }
    
    private MacDeviceDao(Context context)
    {
        super(context);
    }
    
    /**
     * 插入
     * 
     * @param macList
     */
    public void deviceInsert(List<DeviceInfo> macList)
    {
        LogUtils.LOGI(TAG, "==deviceInsert==" + macList.size());
        for (int i = 0; i < macList.size(); i++)
        {
            DeviceInfo info = macList.get(i);
            String mac = info.getMacAddr();
            if (isExistMac(mac))
            {
                itemUpdate(mac, info);
            }
            else
            {
                itemInsert(info);
            }
        }
    }
    
    /**
     * 单条插入
     * 
     * @param info
     */
    public void itemInsert(DeviceInfo info)
    {
        LogUtils.LOGI(TAG, "==itemInsert==");
        ContentValues values = new ContentValues();
        values.put(MacDeviceTable.MAC, info.getMacAddr());
        values.put(MacDeviceTable.DEVICE, info.getDevice());
        values.put(MacDeviceTable.BRAND, info.getBrand());
        values.put(MacDeviceTable.VENDOR, info.getVendor());
        values.put(MacDeviceTable.REMARK, info.getRemark());
        values.put(MacDeviceTable.DEVICETY, info.getDevicety());
        values.put(MacDeviceTable.ISMATCH, info.getIsMatch());
        insert(MacDeviceTable.TABLE_NAME, null, values);
    }
    
    /**
     * 添加备注
     * 
     * @param mac
     * @param remark
     */
    public void addRemark(String mac, String remark)
    {
        LogUtils.LOGI(TAG, "==addRemark==remark==" + remark);
        if (StringUtils.isEmpty(mac))
        {
            return;
        }
        ContentValues values = new ContentValues();
        values.put(MacDeviceTable.MAC, mac);
        values.put(MacDeviceTable.REMARK, remark);
        update(MacDeviceTable.TABLE_NAME, values, MacDeviceTable.MAC + " = ? ", new String[] {mac});
    }
    
    /**
     * 单条修改
     * 
     * @param info
     */
    public void itemUpdate(String mac, DeviceInfo info)
    {
        LogUtils.LOGI(TAG, "==itemUpdate==");
        if (StringUtils.isEmpty(mac))
        {
            return;
        }
        ContentValues values = new ContentValues();
        values.put(MacDeviceTable.MAC, info.getMacAddr());
        values.put(MacDeviceTable.DEVICE, info.getDevice());
        values.put(MacDeviceTable.BRAND, info.getBrand());
        values.put(MacDeviceTable.VENDOR, info.getVendor());
        values.put(MacDeviceTable.REMARK, info.getRemark());
        values.put(MacDeviceTable.DEVICETY, info.getDevicety());
        values.put(MacDeviceTable.ISMATCH, info.getIsMatch());
        update(MacDeviceTable.TABLE_NAME, values, MacDeviceTable.MAC + " = ?", new String[] {mac});
    }
    
    /**
     * 判断数据是否存在
     * 
     * @param mac
     * @return
     */
    private boolean isExistMac(String mac)
    {
        LogUtils.LOGI(TAG, "==isExistMac==");
        boolean isExist = false;
        if (StringUtils.isEmpty(mac))
        {
            return isExist;
        }
        Cursor cursor = query(MacDeviceTable.TABLE_NAME,
            null,
            MacDeviceTable.MAC + " = ?",
            new String[] {mac},
            null,
            null,
            null,
            null);
        if (null == cursor || cursor.getCount() == 0)
        {
            return isExist;
        }
        int count = cursor.getCount();
        cursor.close();
        if (count == 1)
        {
            isExist = true;
        }
        else
        {
            deteleItem(mac);
        }
        return isExist;
    }
    
    /**
     * 条件查询
     * 
     * @param mac
     * @return
     */
    public DeviceInfo queryItem(String mac)
    {
        LogUtils.LOGI(TAG, "==queryItem==");
        if (StringUtils.isEmpty(mac))
        {
            return null;
        }
        // Cursor cursor = query(MacDeviceTable.TABLE_NAME, null,
        // MacDeviceTable.MAC + " = ? and " + MacDeviceTable.ISMATCH
        // + " = ? ", new String[] { mac, "1" }, null, null, null,
        // null);
        
        Cursor cursor = query(MacDeviceTable.TABLE_NAME,
            null,
            MacDeviceTable.MAC + " = ? ",
            new String[] {mac},
            null,
            null,
            null,
            null);
        
        if (null == cursor)
        {
            return null;
        }
        DeviceInfo item = null;
        while (cursor.moveToNext())
        {
            item = ergodicQuery(cursor);
        }
        if (cursor != null)
        {
            cursor.close();
        }
        return item;
    }
    
    /**
     * 查询
     * 
     * @return
     */
    public List<DeviceInfo> queryAll()
    {
        LogUtils.LOGI(TAG, "==queryAll==");
        Cursor cursor = query(MacDeviceTable.TABLE_NAME, null, null, null, null, null, null, null);
        List<DeviceInfo> macList = new ArrayList<DeviceInfo>();
        while (cursor != null && cursor.moveToNext())
        {
            DeviceInfo item = ergodicQuery(cursor);
            macList.add(item);
        }
        if (cursor != null)
        {
            cursor.close();
        }
        return macList;
    }
    
    /**
     * 遍历查询
     * 
     * @param cursor
     * @return
     */
    private DeviceInfo ergodicQuery(Cursor cursor)
    {
        LogUtils.LOGI(TAG, "==ergodicQuery==");
        DeviceInfo item = new DeviceInfo();
        item.setId(cursor.getInt(cursor.getColumnIndex(MacDeviceTable.ID)));
        item.setMacAddr(cursor.getString(cursor.getColumnIndex(MacDeviceTable.MAC)));
        item.setDevice(cursor.getString(cursor.getColumnIndex(MacDeviceTable.DEVICE)));
        item.setBrand(cursor.getString(cursor.getColumnIndex(MacDeviceTable.BRAND)));
        item.setVendor(cursor.getString(cursor.getColumnIndex(MacDeviceTable.VENDOR)));
        item.setDevicety(cursor.getString(cursor.getColumnIndex(MacDeviceTable.DEVICETY)));
        item.setRemark(cursor.getString(cursor.getColumnIndex(MacDeviceTable.REMARK)));
        item.setIsMatch(cursor.getInt(cursor.getColumnIndex(MacDeviceTable.ISMATCH)));
        return item;
    }
    
    /**
     * 删除
     */
    public void deteleAll()
    {
        LogUtils.LOGI(TAG, "==deteleAll==");
        delete(MacDeviceTable.TABLE_NAME, null, null);
    }
    
    /**
     * 删除
     */
    public void deteleItem(String mac)
    {
        LogUtils.LOGI(TAG, "==deteleItem==");
        if (StringUtils.isEmpty(mac))
        {
            return;
        }
        delete(MacDeviceTable.TABLE_NAME, MacDeviceTable.MAC + " = ?", new String[] {mac});
    }
    
    class MacDeviceTable
    {
        public static final String TABLE_NAME = "mac_device";
        
        public static final String ID = "id";
        
        public static final String MAC = "mac";
        
        public static final String REMARK = "remark";// 备注
        
        public static final String BRAND = "brand";// 厂商品牌
        
        public static final String VENDOR = "vendor";// 生产商
        
        public static final String DEVICE = "device";
        
        public static final String DEVICETY = "devicety";// 设备类型 1,手机 2,pad 3,电脑
        
        public static final String ISMATCH = "ismatch";// 是否mac与device匹配过，1:匹配
        
        public static final String EXP1 = "exp1";
        
        public static final String EXP2 = "exp2";
        
        public static final String EXP3 = "exp3";
    }
    
}
