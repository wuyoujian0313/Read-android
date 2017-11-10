package com.read.mobile.module.adapter;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ngc.corelib.env.BaseActivity;
import com.ngc.corelib.http.AsyncTaskListener;
import com.ngc.corelib.http.NetAsyncTask;
import com.ngc.corelib.http.bean.BaseResult;
import com.ngc.corelib.utils.Options;
import com.ngc.corelib.utils.Tips;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.read.mobile.R;
import com.read.mobile.beans.BookItem;
import com.read.mobile.beans.BookStoreBookRequest;
import com.read.mobile.beans.BookStoreBookResult;
import com.read.mobile.constants.Contacts;

public class BookListAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private List<BookItem> beans;

	private ImageLoader imageLoader;
	private Context context;
	private DisplayImageOptions options;

	public BookListAdapter(Context context, List<BookItem> beans) {
		inflater = LayoutInflater.from(context);
		this.beans = beans;
		this.context = context;
		imageLoader = ImageLoader.getInstance();
		options = Options.getListOptions();

	}

	@Override
	public int getCount() {
		return beans.size();
	}

	@Override
	public Object getItem(int arg0) {
		return beans.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		ViewHolder holder = null;
		if (arg1 == null) {
			holder = new ViewHolder();
			arg1 = inflater.inflate(R.layout.item_book_list, null);
			holder.imgIV = (ImageView) arg1.findViewById(R.id.item_book_list_img);
			holder.heartIV = (ImageView) arg1.findViewById(R.id.item_book_list_heart);

			holder.nameTV = (TextView) arg1.findViewById(R.id.item_book_list_name);
			holder.authorTV = (TextView) arg1.findViewById(R.id.item_book_list_author);
			holder.publicTV = (TextView) arg1.findViewById(R.id.item_book_list_public);
			holder.priceYuanTV = (TextView) arg1.findViewById(R.id.item_book_list_price_yuan);
			holder.priceFenTV = (TextView) arg1.findViewById(R.id.item_book_list_price_fen);

			arg1.setTag(holder);
		}
		holder = (ViewHolder) arg1.getTag();
		BookItem bean = beans.get(arg0);
		if (!TextUtils.isEmpty(bean.getPic_big())) {
			imageLoader.displayImage(bean.getPic_big(), holder.imgIV, options);
		}
		holder.nameTV.setText(bean.getName());
		holder.authorTV.setText("作者：" + bean.getAuthor());
		holder.publicTV.setText("出版社：" + bean.getPress());

		if (!TextUtils.isEmpty(bean.getPrice())) {
			int price = Integer.valueOf(bean.getPrice());
			int yuan = price / 100;
			int fen = price % 100;
			holder.priceYuanTV.setText(String.valueOf(yuan) + ".");
			holder.priceFenTV.setText(((fen >= 10) ? String.valueOf(fen) : ("0" + fen)) + "元");
		} else {
			holder.priceYuanTV.setText("暂无报价");
			holder.priceFenTV.setText("");
		}
		if (bean.getIsFavor().equals("yes")) {
			holder.heartIV.setImageResource(R.drawable.heart_read);
		} else {
			holder.heartIV.setImageResource(R.drawable.heart_empty);
		}
		setOnClick(holder, arg0);
		return arg1;
	}

	private void setOnClick(final ViewHolder holder, final int position) {
		holder.heartIV.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				final BookItem bean = beans.get(position);
				BookStoreBookRequest request = new BookStoreBookRequest();
				request.setAuthor(bean.getAuthor());
				request.setBookName(bean.getName());
				request.setIsbn(bean.getIsbn());
				request.setPic(bean.getPic_big());
				request.setPress(bean.getPress());
				request.setType((bean.getIsFavor().equals("yes")) ? "0" : "1");

				NetAsyncTask asyncTask = new NetAsyncTask(BookStoreBookResult.class, new AsyncTaskListener() {
					@Override
					public void onTaskSuccess(int requestId, BaseResult result) {
						((BaseActivity) context).dismissProgressDialog();
						if (result.getHead().getCode().equals(Contacts.RESULT_CODE_OK)) {
							if (bean.getIsFavor().equals("yes")) {
								bean.setIsFavor("no");
								holder.heartIV.setImageResource(R.drawable.heart_empty);
							} else {
								bean.setIsFavor("yes");
								holder.heartIV.setImageResource(R.drawable.heart_read);
							}
						} else {
							Tips.tipLong(context, result.getHead().getMessage());
						}
					}

					@Override
					public void onTaskStart(int requestId) {
						((BaseActivity) context).showProgressDialog("正在请求~");
					}

					@Override
					public void onTaskFail(int requestId, Exception e) {
						((BaseActivity) context).dismissProgressDialog();
						Tips.tipLong(context, e.getLocalizedMessage());
					}
				}, request);
				asyncTask.execute(Contacts.URL_BOOK_STOREBOOK);
			}
		});
	}

	class ViewHolder {
		ImageView imgIV;
		TextView nameTV;
		TextView authorTV;
		TextView publicTV;
		TextView priceYuanTV;
		TextView priceFenTV;
		ImageView heartIV;
	}

}
