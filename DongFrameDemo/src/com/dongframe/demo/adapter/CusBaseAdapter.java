package com.dongframe.demo.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

public abstract class CusBaseAdapter<T> extends BaseAdapter
{
    protected Context mContext;
    
    protected LayoutInflater mInflater;
    
    protected List<T> mData;
    
    public CusBaseAdapter(Context context)
    {
        init(context);
    }
    
    public CusBaseAdapter(Context context, List<T> data)
    {
        init(context);
        this.mData = data;
    }
    
    private void init(Context context)
    {
        mContext = context;
        mInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    
    public List<T> getData()
    {
        return mData;
    }
    
    public void setData(List<T> data)
    {
        this.mData = data;
        this.notifyDataSetChanged();
    }
    
    /** 添加数据
     * <功能详细描述>
     * @param data
     * @see [类、类#方法、类#成员]
     */
    public void addData(List<T> data)
    {
        if (null == mData)
        {
            setData(data);
        }
        else
        {
            mData.addAll(data);
        }
    }
    
    /** 添加数据
     * <功能详细描述>
     * @param data
     * @param position
     * @return
     * @see [类、类#方法、类#成员]
     */
    public int addData(List<T> data, int position)
    {
        if (null == mData)
        {
            mData = new ArrayList<T>();
        }
        if (position <= getCount())
        {
            mData.addAll(position, data);
            return 1;
        }
        return -1;
    }
    
    /** 添加数据
     * <功能详细描述>
     * @param data
     * @see [类、类#方法、类#成员]
     */
    public void addData(T data)
    {
        if (null == mData)
        {
            mData = new ArrayList<T>();
        }
        mData.add(data);
    }
    
    /** 添加数据
     * <功能详细描述>
     * @param data
     * @param position
     * @return
     * @see [类、类#方法、类#成员]
     */
    public int addData(T data, int position)
    {
        if (null == mData)
        {
            mData = new ArrayList<T>();
        }
        if (position <= getCount())
        {
            mData.add(position, data);
            return 1;
        }
        return -1;
    }
    
    /** 清除所有数据
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    public void clearAll()
    {
        if (null != mData)
        {
            mData.clear();
        }
        
    }
    
    /** 移除数据
     * <功能详细描述>
     * @param position
     * @return
     * @see [类、类#方法、类#成员]
     */
    public int removeData(int position)
    {
        mData.remove(position);
        if (null != mData && position < getCount())
        {
            mData.remove(position);
            return 0;
        }
        return -1;
    }
    
    /** 移除数据
     * <功能详细描述>
     * @param data
     * @see [类、类#方法、类#成员]
     */
    public void removeData(T data)
    {
        if (null != mData)
        {
            mData.remove(data);
        }
    }
    
    @Override
    public int getCount()
    {
        // TODO Auto-generated method stub
        return null == mData ? 0 : mData.size();
    }
    
    @Override
    public Object getItem(int position)
    {
        // TODO Auto-generated method stub
        return null == mData ? null : mData.get(position);
    }
    
    @Override
    public long getItemId(int position)
    {
        // TODO Auto-generated method stub
        return position;
    }
    
}
