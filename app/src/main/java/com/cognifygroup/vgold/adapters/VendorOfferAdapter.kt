package com.cognifygroup.vgold.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import butterknife.ButterKnife
import com.cognifygroup.vgold.R
import com.cognifygroup.vgold.activities.OfferLetterActivity
import com.cognifygroup.vgold.getVendorOffer.VendorOfferModel
import com.nostra13.universalimageloader.core.DisplayImageOptions
import com.squareup.picasso.Picasso

class VendorOfferAdapter(
    Context: Context, mCategoryArray: ArrayList<VendorOfferModel.Data>, key: String
) : RecyclerView.Adapter<VendorOfferAdapter.UserViewHolder>() {
    private val mContext: Activity
    var mCategoryArray: ArrayList<VendorOfferModel.Data>
    private val area: String? = null
    private val options: DisplayImageOptions? = null

    //   private CategoryInterface mCategoryInterface;
    private var BaseUrl: String? = null
    private val key: String

    init {
        mContext = Context as Activity
        this.mCategoryArray = mCategoryArray
        this.key = key
        //  mCategoryInterface=mContext;
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): UserViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.adapter_our_business, null)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        //set font

        holder.rlayout_category.setOnClickListener {
            val intent = Intent(mContext, OfferLetterActivity::class.java)
            intent.putExtra("offer", mCategoryArray[position].letter_path)
            intent.putExtra("offer1", mCategoryArray[position].advertisement_path)
            intent.putExtra("venderId", mCategoryArray[position].vendor_id)
            intent.putExtra("logo_path", mCategoryArray[position].logo_path)
            mContext.startActivity(intent)
            mContext.finish()
        }


        /* if (key.equalsIgnoreCase("venders")) {
             BaseUrl = IMAGE_URL + mCategoryArray.get(position).getAdvertisement_path();
         } else if (key.equalsIgnoreCase("offers")) {
         }*/

        BaseUrl = "https://www.vgold.co.in" + mCategoryArray[position].logo_path
        if (BaseUrl != null && !TextUtils.isEmpty(BaseUrl)) {

            /* Glide.with(mContext).load(BaseUrl)
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into(holder.imv_category);*/
            Picasso.with(mContext).load(BaseUrl).placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher).into(holder.imv_category)


            /*Picasso.with(mContext)
                        .load(BaseUrl)
                        .fit()
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .into(holder.imv_category, new Callback() {
                            @Override
                            public void onSuccess() {
                                if (holder.progressbar_category != null) {
                                    holder.progressbar_category.setVisibility(View.GONE);
                                }
                            }
                            @Override
                            public void onError() {
                                Toast.makeText(mContext, "No Image Found",Toast.LENGTH_SHORT).show();
                            }
                        });*/

            /*if (holder.progressbar_category != null) {
                holder.progressbar_category.setVisibility(View.GONE);
            }*/
        }
    }

    override fun getItemCount(): Int {
        return mCategoryArray.size
    }

    inner class UserViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        val rlayout_category: RelativeLayout = itemView!!.findViewById(R.id.rlayout_category)
        val imv_category: ImageView = itemView!!.findViewById(R.id.imv_category)
        /*@InjectView(R.id.cardView)
        CardView cardView;*/
//        @InjectView(R.id.rlayout_category)
//        var rlayout_category: RelativeLayout? = null

//        @InjectView(R.id.imv_category)
//        var imv_category: ImageView? = null

        //        @InjectView(R.id.progressbar_category)
        //        ProgressBar progressbar_category;
        init {
            ButterKnife.inject(this, itemView)
        }
    } /*  public interface CategoryInterface {

        public void onGetPositionCategory(int position,String categoryId,String categoryName);
    }*/
}
