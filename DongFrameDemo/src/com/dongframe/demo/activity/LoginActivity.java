package com.dongframe.demo.activity;

import com.dong.frame.view.ViewAttacher;
import com.dongframe.demo.R;
import com.dongframe.demo.utils.SharedUtil;
import com.dongframe.demo.utils.StringUtils;
import com.dongframe.demo.utils.WifigxApUtil;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends BaseActivity implements OnClickListener
{
    private EditText userNmaeEidt, passwordEidt;
    
    private CheckBox rememberCheck;
    
    private TextView forgetPassText;
    
    private Button loginBtn, openGPSBtn;
    
    private String md5UserPass;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ViewAttacher.attach(this);
        initView();
        setListener();
        isGpsOpen();
    }
    
    private void initView()
    {
        boolean isMemoryPass = SharedUtil.isMemoryPass(this);
        rememberCheck.setChecked(isMemoryPass);
        userNmaeEidt.setText(SharedUtil.getLoginName(this));
        if (isMemoryPass)
        {
            md5UserPass = SharedUtil.getLoginPass(this);
            if (StringUtils.isEmpty(md5UserPass))
            {
                md5UserPass = "";
            }
            else
            {
                passwordEidt.setText("654321");
            }
        }
        else
        {
            md5UserPass = "";
            SharedUtil.setLoginPass(this, "");
        }
        
    }
    
    /** 判断gps
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    private void isGpsOpen()
    {
        if (WifigxApUtil.isGPSAvailable(this))
        {
            openGPSBtn.setVisibility(View.VISIBLE);
        }
        else
        {
            openGPSBtn.setVisibility(View.VISIBLE);
        }
    }
    
    private void setListener()
    {
        forgetPassText.setOnClickListener(this);
        loginBtn.setOnClickListener(this);
        openGPSBtn.setOnClickListener(this);
        rememberCheck.setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                SharedUtil.setMemoryPass(LoginActivity.this, isChecked);
                if (!isChecked)
                {
                    SharedUtil.setLoginPass(LoginActivity.this, "");
                }
            }
        });
        passwordEidt.addTextChangedListener(new TextWatcher()
        {
            
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                md5UserPass = "";
            }
            
            @Override
            public void afterTextChanged(Editable s)
            {
                // TODO Auto-generated method stub
                
            }
        });
    }
    
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.forgetPassText:
                Intent intent = new Intent();
                intent.setClass(this, ForgetPassActivity.class);
                startActivity(intent);
                break;
            case R.id.openGPSBtn:
                openGPS();
                break;
            case R.id.loginBtn:
                login();
                break;
            default:
                break;
        }
        
    }
    
    /** 打开gps
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    private void openGPS()
    {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try
        {
            this.startActivityForResult(intent, 1);
        }
        catch (ActivityNotFoundException ex)
        {
            intent.setAction(Settings.ACTION_SETTINGS);
            try
            {
                this.startActivityForResult(intent, 2);
            }
            catch (Exception e)
            {
            }
        }
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == 1 || requestCode == 2)
        {
            isGpsOpen();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    
    /** 登录
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    private void login()
    {
        if (!checkNetWork())
        {
            return;
        }
        if (!WifigxApUtil.isGPSAvailable(this))
        {
            showMessage(R.string.toast_no_open_gps);
            openGPSBtn.setVisibility(View.VISIBLE);
            return;
        }
        String nameStr = userNmaeEidt.getText().toString();
        if (StringUtils.isEmpty(nameStr))
        {
            showMessage(R.string.toast_user_empty);
            return;
        }
        String md5Pass = md5UserPass;
        if (StringUtils.isEmpty(md5Pass))
        {
            String passwordStr = passwordEidt.getText().toString();
            if (StringUtils.isEmpty(passwordStr))
            {
                showMessage(R.string.toast_password_empty);
                return;
            }
            //转换成md5
            md5Pass = WifigxApUtil.getMD5(passwordStr);
        }
        //登录请求
        Intent intent = new Intent();
        intent.setClass(this, HomeActivity.class);
        startActivity(intent);
        SharedUtil.setLoginName(this, nameStr);
        if (SharedUtil.isMemoryPass(this))
        {
            SharedUtil.setLoginPass(this, md5Pass);
        }
        this.finish();
    }
    
}
