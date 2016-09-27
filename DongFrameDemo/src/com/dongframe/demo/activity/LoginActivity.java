package com.dongframe.demo.activity;

import java.util.List;

import org.json.JSONObject;

import com.dong.frame.view.ViewAttacher;
import com.dongframe.demo.R;
import com.dongframe.demo.constant.APIServer;
import com.dongframe.demo.https.HttpCallback;
import com.dongframe.demo.https.OkHttpUtils;
import com.dongframe.demo.utils.SharedUtil;
import com.dongframe.demo.utils.StringUtils;
import com.dongframe.demo.utils.WifigxApUtil;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.Response;

public class LoginActivity extends BaseActivity implements OnClickListener
{
    protected static final String TAG = LoginActivity.class.getSimpleName();
    
    protected static final int HANDLER_OPEN_GPS = 0x00010;
    
    private TextView textView2;
    
    private EditText userNmaeEidt, passwordEidt;
    
    private CheckBox rememberCheck;
    
    private TextView forgetPassText;
    
    private Button loginBtn, openGPSBtn;
    
    //    private String md5UserPass;
    
    /**
     * 是否二次验证过
     */
    private boolean isTwoVer = false;
    
    private long delayMillis = 1000;
    
    private int number = 0;
    
    private Handler mHandler = new Handler()
    {
        
        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case HANDLER_OPEN_GPS:
                    if (WifigxApUtil.isGPSAvailable(LoginActivity.this))
                    {
                        openGPSBtn.setVisibility(View.VISIBLE);
                        mHandler.removeMessages(HANDLER_OPEN_GPS);
                        hideLoadDialog();
                    }
                    else
                    {
                        if (number < 3)
                        {
                            mHandler.sendEmptyMessageDelayed(HANDLER_OPEN_GPS, delayMillis);
                            number++;
                        }
                        else
                        {
                            hideLoadDialog();
                            mHandler.removeMessages(HANDLER_OPEN_GPS);
                        }
                        openGPSBtn.setVisibility(View.VISIBLE);
                    }
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
        
    };
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ViewAttacher.attach(this);
        initView();
        setListener();
        isGpsOpen();
        reqTwoVer(false);
    }
    
    private void initView()
    {
        textView2.setText("密    码：");
        boolean isMemoryPass = SharedUtil.isMemoryPass(this);
        rememberCheck.setChecked(isMemoryPass);
        userNmaeEidt.setText(SharedUtil.getLoginName(this));
        if (isMemoryPass)
        {
            //            md5UserPass = SharedUtil.getLoginPass(this);
            //            if (StringUtils.isEmpty(md5UserPass))
            //            {
            //                md5UserPass = "";
            //            }
            //            else
            //            {
            //                passwordEidt.setText("654321");
            //            }
            passwordEidt.setText(SharedUtil.getLoginPass(this));
        }
        else
        {
            //            md5UserPass = "";
            SharedUtil.setLoginPass(this, "");
        }
        userNmaeEidt.setText("administrator");
        passwordEidt.setText("111111");
        
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
            gpsDialog();
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
                //                md5UserPass = "";
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
    
    private void gpsDialog()
    {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(R.string.dialog_title);
        dialog.setMessage(R.string.dialog_message);
        dialog.setPositiveButton(R.string.dialog_open, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
                showLoadDialog();
                openGPS();
            }
        });
        dialog.setNegativeButton(R.string.dialog_cancel, null);
        dialog.show();
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
            number = 0;
            mHandler.sendEmptyMessageDelayed(HANDLER_OPEN_GPS, delayMillis);
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
            gpsDialog();
            return;
        }
        final String nameStr = userNmaeEidt.getText().toString();
        if (StringUtils.isEmpty(nameStr))
        {
            showMessage(R.string.toast_user_empty);
            return;
        }
        //        String md5Pass = md5UserPass;
        //        if (StringUtils.isEmpty(md5Pass))
        //        {
        String passwordStr = passwordEidt.getText().toString();
        if (StringUtils.isEmpty(passwordStr))
        {
            showMessage(R.string.toast_password_empty);
            return;
        }
        //转换成md5
        //            md5Pass = WifigxApUtil.getMD5(passwordStr);
        //        }
        if (isTwoVer)
        {
            showLoadDialog();
            //            final String password = md5Pass;
            final String password = passwordStr;
            //登录请求
            APIServer.reqLogin(this, nameStr, password, new HttpCallback()
            {
                @Override
                public void onSuccess(int statusCode, String msg, JSONObject jsonObject, Call call, Response response)
                {
                    APIServer.cookie = msg;
                    Intent intent = new Intent();
                    intent.setClass(LoginActivity.this, HomeActivity.class);
                    startActivity(intent);
                    SharedUtil.setLoginName(LoginActivity.this, nameStr);
                    if (SharedUtil.isMemoryPass(LoginActivity.this))
                    {
                        SharedUtil.setLoginPass(LoginActivity.this, password);
                    }
                    LoginActivity.this.finish();
                    hideLoadDialog();
                }
                
                @Override
                public void onError(Call call, Exception e)
                {
                    showMessage(getString(R.string.net_connect_right) + "(" + e.getMessage() + ")");
                    hideLoadDialog();
                }
                
                @Override
                public void onFailure(int statusCode, String msg, Call call, Response response)
                {
                    showMessage(msg);
                    hideLoadDialog();
                }
            });
        }
        else
        {
            reqTwoVer(true);
        }
    }
    
    /** 二次验证
     * <功能详细描述>
     * @param isLogin 验证成功是否登录
     * @see [类、类#方法、类#成员]
     */
    private void reqTwoVer(final boolean isLogin)
    {
        if (!checkNetWork())
        {
            return;
        }
        showLoadDialog();
        APIServer.reqTwoVerify(this, "654321", new HttpCallback()
        {
            
            @Override
            public void onSuccess(int statusCode, String msg, JSONObject jsonObject, Call call, Response response)
            {
                isTwoVer = true;
                hideLoadDialog();
                if (isLogin)
                {
                    login();
                }
            }
            
            @Override
            public void onError(Call call, Exception e)
            {
                isTwoVer = false;
                if (isLogin)
                {
                    showMessage(getString(R.string.net_connect_right) + "(" + e.getMessage() + ")");
                }
                hideLoadDialog();
            }
            
            @Override
            public void onFailure(int statusCode, String msg, Call call, Response response)
            {
                isTwoVer = false;
                if (isLogin)
                {
                    showMessage(msg);
                }
                hideLoadDialog();
            }
            
        });
    }
}
