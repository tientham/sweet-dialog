package it.vn.tientham.sweetdialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by tientham on 30/09/17.
 */

public class SweetDialog extends Dialog implements View.OnClickListener {

    private View mDialogView;
    private AnimationSet mModalInAnim;
    private AnimationSet mModalOutAnim;
    private Animation mOverlayOutAnim;
    private Animation mErrorInAnim;
    private AnimationSet mErrorXInAnim;
    private AnimationSet mSuccessLayoutAnimSet;
    private Animation mSuccessBowAnim;

    private TextView mTitleTextView;
    private TextView mContentTextView;

    private String mTitleText;
    private String mContentText;

    private boolean mShowCancel;
    private boolean mShowContent;
    private boolean mShowNatural;

    private String mCancelText;
    private String mConfirmText;
    private String mNaturalText;

    private int mAlertType;
    private FrameLayout mErrorFrame;
    private FrameLayout mSuccessFrame;
    private FrameLayout mProgressFrame;
    //private SuccessTickView mSuccessTick; TODO: reserved for future use
    private ImageView mErrorX;
    private View mSuccessLeftMask;
    private View mSuccessRightMask;
    private Drawable mCustomImgDrawable;
    private ImageView mCustomImage;

    private Button mConfirmButton;
    private Button mCancelButton;
    private Button mNaturalButton;
    // private ProgressHelper mProgressHelper; TODO: reserved for future use

    private FrameLayout mWarningFrame;
    private OnSweetClickListener mCancelClickListener;
    private OnSweetClickListener mConfirmClickListener;
    private OnSweetClickListener mNaturalClickListener;

    private boolean mCloseFromCancel;

    public static final int NORMAL_TYPE = 0;
    public static final int ERROR_TYPE = 1;
    public static final int SUCCESS_TYPE = 2;
    public static final int WARNING_TYPE = 3;
    public static final int CUSTOM_IMAGE_TYPE = 4;
    public static final int PROGRESS_TYPE = 5;
    public static final int NATURAL_BUTTON_WITH_IMAGE_TYPE = 6;

//  -------------------------------------------------------------------------->

    public static interface OnSweetClickListener
    {
        public void onClick(SweetDialog sweetDialog);
    }

//  --------------------------------------------------------------------------->

    public SweetDialog(Context context)
    {
        this(context, NORMAL_TYPE);
    }

    public SweetDialog(Context context, int dialogType)
    {
        super(context, R.style.sweet_dialog);
        setCancelable(true);
        setCanceledOnTouchOutside(false);
        mAlertType = dialogType;

        mModalInAnim = (AnimationSet) OptAnimationLoader.loadAnimation(getContext(), R.anim.modal_in);
        mModalOutAnim = (AnimationSet) OptAnimationLoader.loadAnimation(getContext(), R.anim.modal_out);
        mModalOutAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mDialogView.setVisibility(View.GONE);
                mDialogView.post(new Runnable() {
                    @Override
                    public void run() {
                        if (mCloseFromCancel) {
                            SweetDialog.super.cancel();
                        } else {
                            SweetDialog.super.dismiss();
                        }
                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        // dialog overlay fade out

        mOverlayOutAnim = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t)
            {
                if (getWindow() != null)
                {
                    WindowManager.LayoutParams wlp = getWindow().getAttributes();
                    wlp.alpha = 1 - interpolatedTime;
                    getWindow().setAttributes(wlp);
                }
            }
        };
        mOverlayOutAnim.setDuration(120);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sweet_dialog);

        mDialogView = getWindow().getDecorView().findViewById(android.R.id.content);
        mTitleTextView = (TextView) findViewById(R.id.title_text);
        mContentTextView = (TextView) findViewById(R.id.content_text);
        mCustomImage = (ImageView) findViewById(R.id.custom_image);

        mConfirmButton = (Button) findViewById(R.id.confirm_button);
        mCancelButton = (Button) findViewById(R.id.cancel_button);
        mNaturalButton = (Button) findViewById(R.id.natural_button);

        mConfirmButton.setOnClickListener(this);
        mCancelButton.setOnClickListener(this);
        mNaturalButton.setOnClickListener(this);

        setTitleText(mTitleText);
        setContentText(mContentText);
        setCancelText(mCancelText);
        setConfirmText(mConfirmText);
        setNaturalText(mNaturalText);
        changeSweetType(mAlertType, true);

        if(mNaturalButton != null)
            System.out.println("natural button is null");
    }

    private void restore () {
        mCustomImage.setVisibility(View.GONE);
        mConfirmButton.setVisibility(View.VISIBLE);
        mConfirmButton.setBackgroundResource(R.drawable.blue_button_background);
        mNaturalButton.setVisibility(View.VISIBLE);
        mNaturalButton.setBackgroundResource(R.drawable.blue_button_background);
    }

    private void changeSweetType(int alertType, boolean fromCreate) {
        mAlertType = alertType;
        // call after created views
        if (mDialogView != null) {
            if (!fromCreate) {
                // restore all of views state before switching alert type
                restore();
            }
            switch (mAlertType) {
                case ERROR_TYPE:
                    break;
                case SUCCESS_TYPE:
                    break;
                case WARNING_TYPE:
                    break;
                case CUSTOM_IMAGE_TYPE:
                    setCustomImage(mCustomImgDrawable);
                    break;
                case PROGRESS_TYPE:
                    break;
                case NATURAL_BUTTON_WITH_IMAGE_TYPE:
                    mNaturalButton.setVisibility(View.VISIBLE);
                    mCancelButton.setVisibility(View.VISIBLE);
                    setCustomImage(mCustomImgDrawable);
                    break;
            }
            if (!fromCreate) {
                //playAnimation();
            }
        }
    }

    public int getAlerType () {
        return mAlertType;
    }

    public void changeSweetType(int alertType) {
        changeSweetType(alertType, false);
    }

    public SweetDialog setTitleText (String text) {
        mTitleText = text;
        if (mTitleTextView != null && mTitleText != null) {
            mTitleTextView.setText(mTitleText);
        }
        return this;
    }

    public SweetDialog setCustomImage (Drawable drawable) {
        mCustomImgDrawable = drawable;
        if (mCustomImage != null && mCustomImgDrawable != null) {
            mCustomImage.setVisibility(View.VISIBLE);
            mCustomImage.setImageDrawable(mCustomImgDrawable);
        }
        return this;
    }

    public SweetDialog setCustomImage (int resourceId) {
        return setCustomImage(getContext().getResources().getDrawable(resourceId));
    }

    public String getContentText () {
        return mContentText;
    }

    public SweetDialog setContentText (String text) {
        mContentText = text;
        if (mContentTextView != null && mContentText != null) {
            showContentText(true);
            mContentTextView.setText(mContentText);
        }
        return this;
    }

    public boolean isShowCancelButton () {
        return mShowCancel;
    }

    public SweetDialog showCancelButton (boolean isShow) {
        mShowCancel = isShow;
        if (mCancelButton != null) {
            mCancelButton.setVisibility(mShowCancel ? View.VISIBLE : View.GONE);
        } else
            System.out.println("Cancel button is null also");
        return this;
    }

    public boolean isShowContentText () {
        return mShowContent;
    }

    public SweetDialog showContentText (boolean isShow) {
        mShowContent = isShow;
        if (mContentTextView != null) {
            mContentTextView.setVisibility(mShowContent ? View.VISIBLE : View.GONE);
        }
        return this;
    }

    public String getCancelText () {
        return mCancelText;
    }

    public SweetDialog setCancelText (String text) {
        mCancelText = text;
        if (mCancelButton != null && mCancelText != null) {
            showCancelButton(true);
            mCancelButton.setText(mCancelText);
        }
        return this;
    }

    public String getConfirmText () {
        return mConfirmText;
    }

    public SweetDialog setConfirmText (String text) {
        mConfirmText = text;
        if (mConfirmButton != null && mConfirmText != null) {
            mConfirmButton.setText(mConfirmText);
        }
        return this;
    }

    public boolean isShowNaturalButton () {
        return mShowNatural;
    }

    public SweetDialog showNaturalButton(boolean isShow) {
        mShowNatural = isShow;
        if (mNaturalButton != null) {
            mNaturalButton.setVisibility(mShowNatural ? View.VISIBLE : View.GONE);
        } else
            System.out.println("sweet dialog is null");
        return this;
    }

    public SweetDialog setNaturalText(String text) {
        mNaturalText = text;
        if (mNaturalButton != null && mNaturalText != null )
            mNaturalButton.setText(mNaturalText);
        return this;
    }

    public SweetDialog setCancelClickListener (OnSweetClickListener listener) {
        mCancelClickListener = listener;
        return this;
    }

    public SweetDialog setConfirmClickListener (OnSweetClickListener listener) {
        mConfirmClickListener = listener;
        return this;
    }

    public SweetDialog setNaturalClickListener (OnSweetClickListener listener) {
        mNaturalClickListener = listener;
        return this;
    }

    protected void onStart() {
        mDialogView.startAnimation(mModalInAnim);
        //playAnimation();
    }

    /**
     * The real Dialog.cancel() will be invoked async-ly after the animation finishes.
     */
    @Override
    public void cancel() {
        dismissWithAnimation(true);
    }

    /**
     * The real Dialog.dismiss() will be invoked async-ly after the animation finishes.
     */
    public void dismissWithAnimation() {
        dismissWithAnimation(false);
    }

    private void dismissWithAnimation(boolean fromCancel) {
        mCloseFromCancel = fromCancel;
        // mConfirmButton.startAnimation(mOverlayOutAnim);
        mDialogView.startAnimation(mModalOutAnim);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.cancel_button) {
            if (mCancelClickListener != null) {
                mCancelClickListener.onClick(SweetDialog.this);
                //dismissWithAnimation(true);
            } else {
                dismissWithAnimation();
            }
        } else if (v.getId() == R.id.confirm_button) {
            if (mConfirmClickListener != null) {
                mConfirmClickListener.onClick(SweetDialog.this);
            } else {
                dismissWithAnimation();
            }
        } else if (v.getId() == R.id.natural_button) {
            if (mNaturalClickListener != null) {
                mNaturalClickListener.onClick(SweetDialog.this);
            } else  {
                dismissWithAnimation();
            }
        }
    }

}
