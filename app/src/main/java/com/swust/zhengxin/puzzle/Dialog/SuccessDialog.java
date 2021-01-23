package com.swust.zhengxin.puzzle.Dialog;



import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.swust.zhengxin.puzzle.R;


public class SuccessDialog extends DialogFragment  {
    public OnButtonClickListener buttonClickListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle=getArguments();
        String str_Step =bundle.getString("step");
        String str_Time= bundle.getString("time");
        String str_Date =bundle.getString("date");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()).setTitle(getString(R.string.success));
        builder.setMessage(str_Date+"\n"+"总时间："+str_Time+"\n"+"总步数："+str_Step+"\n"+getString(R.string.success_description))
                .setPositiveButton(getString(R.string.next_level), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (buttonClickListener != null) {
                            buttonClickListener.nextLevelClick();
                        }
                    }
                })
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (buttonClickListener != null) {
                            buttonClickListener.cancelClick();
                        }
                    }
                });

        return builder.create();
    }

    public void addButtonClickListener(OnButtonClickListener listener) {
        this.buttonClickListener = listener;
    }

    //对话框按钮接口，在游戏界面中实现
    public interface OnButtonClickListener {
        public void nextLevelClick();
        public void cancelClick();
    }

}

