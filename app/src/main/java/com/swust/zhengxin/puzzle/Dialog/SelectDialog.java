package com.swust.zhengxin.puzzle.Dialog;


import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.GridLayoutManager;

import androidx.annotation.Nullable;

import com.swust.zhengxin.puzzle.Module.ImageSource;
import com.swust.zhengxin.puzzle.R;
import com.swust.zhengxin.puzzle.Utils.Utils;

import static androidx.recyclerview.widget.DiffUtil.DiffResult.NO_POSITION;


public class SelectDialog extends DialogFragment {

    private View view;
    private RecyclerView imageList;
    private OnItemClickListener itemClickListener;
    private Activity activity;
    private int selectRes;
    private ImageListAdapter imageListAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        view = inflater.inflate(R.layout.dialog_select_image, container);
        imageList = (RecyclerView) view.findViewById(R.id.list);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = getActivity();
        imageList.setLayoutManager(new GridLayoutManager(getActivity().getApplicationContext(), 2));
        imageListAdapter = new ImageListAdapter();
        imageList.setAdapter(imageListAdapter);
    }


    //滑动选择图片列表适配
    public class ImageListAdapter extends RecyclerView.Adapter {
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ImageViewHolder(LayoutInflater.from(activity.getApplicationContext()).inflate(R.layout.item_list, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ImageViewHolder viewHolder = (ImageViewHolder) holder;
            Bitmap bitmap = Utils.ReadBitmap(activity.getApplicationContext(), ImageSource.imagesource[position], 3);
            viewHolder.imageView.setImageBitmap(bitmap);
        }

        @Override
        public int getItemCount() {
            return ImageSource.imagesource.length;
        }
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;

        public ImageViewHolder(View itemView) {
            super(itemView);
            //获取元素图片
            imageView = (ImageView) itemView.findViewById(R.id.itemImg);

            //选择图片点击监听
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getBindingAdapterPosition() != NO_POSITION  && itemClickListener != null) {
                        itemClickListener.itemClick(getBindingAdapterPosition(), ImageSource.imagesource[getBindingAdapterPosition()]);
                        dismiss();
                    }
                }
            });

        }
    }


    public void showDialog(FragmentManager fragmentManager, String tag, int res) {
        show(fragmentManager, "tag");
        selectRes = res;
    }

    //为图片添加点击监听器
    public void addItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    //图片选择点击监听器接口
    public interface OnItemClickListener {
        public void itemClick(int position, int res);
    }

}

