package com.example.jh.guokrchoice.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.jh.guokrchoice.R;
import com.example.jh.guokrchoice.bean.Article;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.List;

public class ArticleListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_NORMAL = 1;
    public static final int TYPE_FOOTER = 2;
    private Context mContext;
    private int layoutId;
    private List<Article> mArticleList;
    DisplayImageOptions imageOptions = null;
    private View mHeaderView = null;
    private View mFooterView = null;

    private OnItemOnClickListener listener = null;

    public ArticleListAdapter(Context mContext, int layoutId, List<Article> mArticleList) {
        this.mContext = mContext;
        this.layoutId = layoutId;
        this.mArticleList = mArticleList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mHeaderView != null
                && viewType == TYPE_HEADER) return new ViewHolder(mHeaderView);
        if (mFooterView != null
                && viewType == TYPE_FOOTER) return new ViewHolder(mFooterView);
        View v = LayoutInflater.from(mContext).inflate(layoutId, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 && mHeaderView != null) return TYPE_HEADER;
        if (position == (getItemCount() - 1) && mFooterView != null) return TYPE_FOOTER;
        return TYPE_NORMAL;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if ((getItemViewType(position) == TYPE_HEADER)
                || (getItemViewType(position) == TYPE_FOOTER)) return;
        ViewHolder myHolder = (ViewHolder) holder;
        Article article = mArticleList.get(position - 1);
        myHolder.mArticleName.setText(article.getTitle());
        myHolder.mArticleSummary.setText(article.getSummary());
        displayImage(article.getHeadline_img(), myHolder.mArticleImage);
    }

    private void displayImage(String url, ImageView mIvArticle) {
        if (imageOptions == null) {
            imageOptions = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.drawable.no_image)
                    .showImageForEmptyUri(R.drawable.no_image)
                    .showImageOnFail(R.drawable.no_image)
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .considerExifParams(true)
                    .imageScaleType(ImageScaleType.EXACTLY)
                    .build();
        }
        ImageLoader.getInstance().displayImage(url, mIvArticle, imageOptions);
    }

    public void setHeaderView(View headerView) {
        mHeaderView = headerView;
        notifyItemInserted(0);
    }

    public void removeHeaderView() {
        if (mHeaderView != null) {
            notifyItemRemoved(0);
        }
    }


    public void setFooterView(View view) {
        this.mFooterView = view;

        notifyItemInserted(mArticleList.size() - 1);
    }

    public void removeFooterView() {
        if (mFooterView != null) {
            notifyItemRemoved(mArticleList.size());
        }
    }

    @Override
    public int getItemCount() {
        if (mHeaderView == null && mFooterView == null) {
            return mArticleList.size();
        } else if (mHeaderView != null && mFooterView != null) {
            return mArticleList.size() + 2;
        } else {
            return mArticleList.size() + 1;
        }
    }

    public void setOnItemClickListener(OnItemOnClickListener listener) {
        this.listener = listener;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView mArticleName, mArticleSummary;
        ImageView mArticleImage;

        public ViewHolder(View itemView) {
            super(itemView);
            if (itemView == mHeaderView) return;
            if (itemView == mFooterView) return;
            itemView.setOnClickListener(this);
            mArticleName = (TextView) itemView.findViewById(R.id.articleName);
            mArticleImage = (ImageView) itemView.findViewById(R.id.articleImage);
            mArticleSummary = (TextView) itemView.findViewById(R.id.articleSummary);
        }

        @Override
        public void onClick(View v) {
            if (listener != null) {
                listener.OnItemClick(v, getAdapterPosition());
            }
        }
    }

    public interface OnItemOnClickListener {
        void OnItemClick(View v, int position);
    }
}
