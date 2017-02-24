package com.yang.eric.onestep.model.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.yang.eric.onestep.R;
import com.yang.eric.onestep.model.bean.ZhihuDailyNews;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yang on 2017/2/24.
 */

public class ZhihuDailyNewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

	private final Context context;
	private final LayoutInflater inflater;
	private List<ZhihuDailyNews.StoriesBean> list = new ArrayList<>();
	private OnRecyclerViewOnClickListener listener;

	private static final int TYPE_NORMAL = 0;
	private static final int TYPE_FOOTER = 1;

	public ZhihuDailyNewsAdapter(List<ZhihuDailyNews.StoriesBean> list, Context context) {
		this.list = list;
		this.inflater = LayoutInflater.from(context);
		this.context = context;
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		switch (viewType){
			case TYPE_NORMAL:
				return new NormalViewHolder(
						inflater.inflate(R.layout.home_list_item_layout, parent, false),listener);
			case TYPE_FOOTER:
				return new FooterViewHolder(
						inflater.inflate(R.layout.list_footer, parent, false));
		}
		return null;
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
		if(holder instanceof NormalViewHolder) {
			ZhihuDailyNews.StoriesBean storiesBean = list.get(position);
			if(storiesBean.getImages().get(0) == null){
				((NormalViewHolder) holder).itemImg.setImageResource(R.drawable.placeholder);
			}else {
				Glide.with(context)
						.load(storiesBean.getImages().get(0))
						.asBitmap()
						.placeholder(R.drawable.placeholder)
						.diskCacheStrategy(DiskCacheStrategy.SOURCE)
						.error(R.drawable.placeholder)
						.centerCrop()
						.into(((NormalViewHolder) holder).itemImg);
			}
			((NormalViewHolder) holder).textView.setText(storiesBean.getTitle());
		}
	}

	@Override
	public int getItemCount() {
		return list.size() + 1;
	}

	@Override
	public int getItemViewType(int position) {
		if (position == list.size()) {
			return ZhihuDailyNewsAdapter.TYPE_FOOTER;
		}
		return ZhihuDailyNewsAdapter.TYPE_NORMAL;
	}

	public void setItemListener(OnRecyclerViewOnClickListener listener) {
		this.listener = listener;
	}

	public interface OnRecyclerViewOnClickListener {

		void OnItemClick(View v, int position);

	}
	public class NormalViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		private ImageView itemImg;
		private TextView textView;
		private OnRecyclerViewOnClickListener listener;
		public NormalViewHolder(View itemView, OnRecyclerViewOnClickListener listener) {
			super(itemView);
			itemImg = (ImageView) itemView.findViewById(R.id.imageViewCover);
			textView = (TextView) itemView.findViewById(R.id.textViewTitle);
			this.listener = listener;
			itemView.setOnClickListener(this);
		}

		@Override
		public void onClick(View v) {
			if(listener != null){
				listener.OnItemClick(v, getLayoutPosition());
			}
		}
	}
	public class FooterViewHolder extends RecyclerView.ViewHolder{

		public FooterViewHolder(View itemView) {
			super(itemView);
		}
	}
}
