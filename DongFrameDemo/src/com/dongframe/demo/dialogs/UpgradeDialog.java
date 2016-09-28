package com.dongframe.demo.dialogs;

import com.dongframe.demo.R;
import com.dongframe.demo.infos.Software;
import com.dongframe.demo.utils.SharedUtil;
import com.dongframe.demo.utils.WifigxApUtil;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class UpgradeDialog extends AlertDialog
{
    private Context mContext;
    
    private Software software;
    
    public UpgradeDialog(Context context)
    {
        super(context);
        init(context);
    }
    
    private void init(Context context)
    {
        mContext = context;
        software = SharedUtil.getSoftUpdate(mContext);
        AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
        dialog.setTitle(R.string.dialog_title);
        dialog.setMessage(software.getInfo());
        dialog.setPositiveButton(R.string.dialog_upgrade, new DialogInterface.OnClickListener()
        {
            
            @Override
            public void onClick(DialogInterface arg0, int arg1)
            {
                WifigxApUtil.startDownLoadApkServer(mContext);
                arg0.dismiss();
            }
        });
        dialog.setNegativeButton(R.string.dialog_cancel, null);
        dialog.show();
    }
    
}
