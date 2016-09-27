package com.dongframe.demo.infos;

import java.io.Serializable;
import java.util.Date;

public class User implements Serializable
{
    
    public static final long serialVersionUID = 1L;
    
    private Integer id;
    
    public void setId(Integer id)
    {
        this.id = id;
    }
    
    public Integer getId()
    {
        return id;
    }
    
    private String username;
    
    public void setUsername(String username)
    {
        this.username = username;
    }
    
    public String getUsername()
    {
        return username;
    }
    
    private String password;
    
    public void setPassword(String password)
    {
        this.password = password;
    }
    
    public String getPassword()
    {
        return password;
    }
    
    private Integer usertype;
    
    public void setUsertype(Integer usertype)
    {
        this.usertype = usertype;
    }
    
    public Integer getUsertype()
    {
        return usertype;
    }
    
    private long addtime;
    
    public void setAddtime(long addtime)
    {
        this.addtime = addtime;
    }
    
    public long getAddtime()
    {
        return addtime;
    }
    
    private String areacode;
    
    public void setAreacode(String areacode)
    {
        this.areacode = areacode;
    }
    
    public String getAreacode()
    {
        return areacode;
    }
    
    private String areaname;
    
    public void setAreaname(String areaname)
    {
        this.areaname = areaname;
    }
    
    public String getAreaname()
    {
        return areaname;
    }
    
    private String mobile;
    
    public void setMobile(String mobile)
    {
        this.mobile = mobile;
    }
    
    public String getMobile()
    {
        return mobile;
    }
    
    private String company;
    
    public void setCompany(String company)
    {
        this.company = company;
    }
    
    public String getCompany()
    {
        return company;
    }
    
    private String remark;
    
    public void setRemark(String remark)
    {
        this.remark = remark;
    }
    
    public String getRemark()
    {
        return remark;
    }
    
    private Integer state;
    
    public void setState(Integer state)
    {
        this.state = state;
    }
    
    public Integer getState()
    {
        return state;
    }
    
    private String aliasname;
    
    public void setAliasname(String aliasname)
    {
        this.aliasname = aliasname;
    }
    
    public String getAliasname()
    {
        return aliasname;
    }
    
    private Integer parentUserId;
    
    public Integer getParentUserId()
    {
        return parentUserId;
    }
    
    public void setParentUserId(Integer parentUserId)
    {
        this.parentUserId = parentUserId;
    }
    
    private String conditionUserName;
    
    public String getConditionUserName()
    {
        return conditionUserName;
    }
    
    public void setConditionUserName(String conditionUserName)
    {
        this.conditionUserName = conditionUserName;
    }
}
