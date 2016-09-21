package com.dongframe.demo.db;

import com.dongframe.demo.db.MacDeviceDao.MacDeviceTable;
import com.dongframe.demo.utils.LogUtils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author dongxl
 * 
 */
public class WifiApDBHelper extends SQLiteOpenHelper
{
    private static final String TAG = WifiApDBHelper.class.getSimpleName();
    
    private static final String DATABASE_NAME = "dongframe.db";// 数据库名称
    
    private static final int DATABASE_VERSION = 1;// 数据库版本号
    
    private Context mContext;
    
    public WifiApDBHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }
    
    public WifiApDBHelper(Context context, String name, CursorFactory factory, int version)
    {
        super(context, name, factory, version);
        mContext = context;
    }
    
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        createMacDevice(db);
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        if (newVersion > oldVersion)
        {
            String macSql = " DROP TABLE IF EXISTS " + MacDeviceTable.TABLE_NAME;
            db.execSQL(macSql);// 先删除，在创建
            createMacDevice(db);
        }
    }
    
    /**
     * 创建mac 设备对应表
     * 
     * @param db
     */
    private void createMacDevice(SQLiteDatabase db)
    {
        StringBuilder builder = new StringBuilder();
        builder.append(" CREATE TABLE IF NOT EXISTS ");
        builder.append(MacDeviceTable.TABLE_NAME);
        builder.append(" ( ");
        builder.append(MacDeviceTable.ID);
        builder.append(" INTEGER PRIMARY KEY AUTOINCREMENT, ");
        builder.append(MacDeviceTable.MAC);
        builder.append(" TEXT, ");
        builder.append(MacDeviceTable.REMARK);
        builder.append(" TEXT, ");
        builder.append(MacDeviceTable.BRAND);
        builder.append(" TEXT, ");
        builder.append(MacDeviceTable.VENDOR);
        builder.append(" TEXT, ");
        builder.append(MacDeviceTable.DEVICE);
        builder.append(" TEXT, ");
        builder.append(MacDeviceTable.DEVICETY);
        builder.append(" TEXT, ");
        builder.append(MacDeviceTable.ISMATCH);
        builder.append(" INTEGER, ");
        builder.append(MacDeviceTable.EXP1);
        builder.append(" TEXT, ");
        builder.append(MacDeviceTable.EXP2);
        builder.append(" TEXT, ");
        builder.append(MacDeviceTable.EXP3);
        builder.append(" TEXT );");
        
        db.execSQL(builder.toString());
        LogUtils.LOGI(TAG, " db execSQL create createMacDevice");
    }
}
