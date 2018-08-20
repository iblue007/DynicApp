package com.felink.corelib.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.felink.corelib.R;
import com.felink.corelib.kitset.ScreenUtil;
import com.felink.corelib.kitset.TelephoneUtil;

/**
 * Created by linliangbin on 2016/10/17.
 */

public class CommonDialog extends Dialog {
    public CommonDialog(Context context, int theme) {
        super(context, theme);
    }

    public CommonDialog(Context context) {
        super(context);
    }

    public static class Builder {
        protected Context context;
        protected Drawable icon;
        protected CharSequence title;
        protected CharSequence message;
        protected CharSequence positiveButtonText;
        protected CharSequence negativeButtonText;
        protected View contentView;
        protected boolean showTitle = true;
        protected boolean showMessage = true;
        protected OnClickListener positiveButtonClickListener;
        protected OnClickListener negativeButtonClickListener;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setMessage(CharSequence message) {
            this.message = message;
            return this;
        }

        public Builder setMessage(int message) {
            this.message = (String)this.context.getText(message);
            return this;
        }

        public Builder setTitle(int title) {
            this.title = (String)this.context.getText(title);
            return this;
        }

        public Builder setTitle(CharSequence title) {
            this.title = title;
            return this;
        }

//        public CommonDialog.Builder setIcon(int icon) {
//            this.icon = this.context.getResources().getDrawable(icon);
//            return this;
//        }
//
//        public CommonDialog.Builder setIcon(Drawable icon) {
//            this.icon = icon;
//            return this;
//        }

        public Builder setContentView(View v) {
            this.contentView = v;
            return this;
        }

        public Builder setPositiveButton(int positiveButtonText, OnClickListener listener) {
            this.positiveButtonText = (String)this.context.getText(positiveButtonText);
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setPositiveButton(CharSequence positiveButtonText, OnClickListener listener) {
            this.positiveButtonText = positiveButtonText;
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(int negativeButtonText, OnClickListener listener) {
            this.negativeButtonText = (String)this.context.getText(negativeButtonText);
            this.negativeButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(CharSequence negativeButtonText, OnClickListener listener) {
            this.negativeButtonText = negativeButtonText;
            this.negativeButtonClickListener = listener;
            return this;
        }

        public void setContext(Context context) {
            this.context = context;
        }

        public Builder  setShowTitle(boolean showTitle) {
            this.showTitle = showTitle;
            return this;
        }

        public CommonDialog.Builder setIcon(int icon) {
            this.icon = this.context.getResources().getDrawable(icon);
            return this;
        }

        public void setShowMessage(boolean showMessage) {
            this.showMessage = showMessage;
        }


        public CommonDialog createEx() {
            // instantiate the dialog with the custom Theme
            final CommonDialog dialog = new CommonDialog(context, R.style.Dialog);
            dialog.setContentView(R.layout.common_dialog_layout_ex);
            if(!showTitle) {
                dialog.findViewById(R.id.common_dialog_top_layout).setVisibility(View.GONE);
            }
            ViewGroup layout = (ViewGroup) dialog.findViewById(R.id.common_dialog_layout);
            int width = (int) (ScreenUtil.getCurrentScreenWidth(context) * 0.9f);
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT);
            layout.setLayoutParams(lp);

            ((TextView) layout.findViewById(R.id.common_dialog_top_title)).setText(title);

            //TextView leftBtn = (TextView) layout.findViewById(R.id.common_dialog_left_button);
            //TextView rightBtn = (TextView) layout.findViewById(R.id.common_dialog_right_button);
            CharSequence dialogLeftBtnTxt = negativeButtonText;
            CharSequence dialogRightBtnTxt = positiveButtonText;
            OnClickListener dialogLeftBtnClickListener = negativeButtonClickListener;
            OnClickListener dialogRightBtnClickListener = positiveButtonClickListener;
            //leftBtn.setBackgroundResource(R.drawable.app_choose_l_btn_ex);
            //rightBtn.setBackgroundResource(R.drawable.app_choose_r_btn_ex);
            //leftBtn.setTextColor(context.getResources().getColorStateList(R.color.common_dialog_text_color_selector_ex));
            //rightBtn.setTextColor(android.graphics.Color.WHITE);
            if (TelephoneUtil.getApiLevel() <= 14) {
                dialogLeftBtnTxt = positiveButtonText;
                dialogRightBtnTxt = negativeButtonText;
                dialogLeftBtnClickListener = positiveButtonClickListener;
                dialogRightBtnClickListener = negativeButtonClickListener;
                //leftBtn.setBackgroundResource(R.drawable.app_choose_r_btn_ex);
                //rightBtn.setBackgroundResource(R.drawable.app_choose_l_btn_ex);
                //leftBtn.setTextColor(android.graphics.Color.WHITE);
                //rightBtn.setTextColor(context.getResources().getColorStateList(R.color.common_dialog_text_color_selector_ex));
            }

            final OnClickListener finalDialogLeftBtnClickListener = dialogLeftBtnClickListener;
            final OnClickListener finalDialogRightBtnClickListener = dialogRightBtnClickListener;
            // set the confirm button
            if (dialogLeftBtnTxt != null) {
                ((TextView) layout.findViewById(R.id.common_dialog_left_button)).setText(dialogLeftBtnTxt);
                if (finalDialogLeftBtnClickListener != null) {
                    ((TextView) layout.findViewById(R.id.common_dialog_left_button)).setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            finalDialogLeftBtnClickListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
                            dialog.dismiss();
                        }
                    });
                }
            } else {
                layout.findViewById(R.id.common_dialog_left_button).setVisibility(View.GONE);
            }

            // set the cancel button
            if (dialogRightBtnTxt != null) {
                ((TextView) layout.findViewById(R.id.common_dialog_right_button)).setText(dialogRightBtnTxt);
                if (finalDialogRightBtnClickListener != null) {
                    ((TextView) layout.findViewById(R.id.common_dialog_right_button)).setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            finalDialogRightBtnClickListener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
                            dialog.dismiss();
                        }
                    });
                }
            } else {
                layout.findViewById(R.id.common_dialog_right_button).setVisibility(View.GONE);
                // layout.findViewById(R.id.separator).setVisibility(View.GONE);
            }

            TextView textView = (TextView) layout.findViewById(R.id.common_dialog_content);
            if(!showMessage){
                textView.setVisibility(View.GONE);
            }
            textView.setMovementMethod(ScrollingMovementMethod.getInstance());
            // set the content message
            if (message != null) {
                textView.setText(message);
            } else {
                textView.setVisibility(View.GONE);
            }

            if (contentView != null) {
                // add the contentView to the dialog body
                ((LinearLayout) layout.findViewById(R.id.common_dialog_custom_view_layout)).addView(contentView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            } else {
                layout.findViewById(R.id.common_dialog_custom_view_layout).setVisibility(View.GONE);
            }

            return dialog;
        }

        public CommonDialog create() {
            final CommonDialog dialog = new CommonDialog(this.context, R.style.Dialog);
            dialog.setContentView(R.layout.common_dialog_layout);
            ViewGroup layout = (ViewGroup)dialog.findViewById(R.id.common_dialog_layout);
            int width = (int)((float)ScreenUtil.getCurrentScreenWidth(this.context) * 0.9F);
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(width, -2);
            layout.setLayoutParams(lp);
            if(this.icon != null) {
                ((ImageView)layout.findViewById(R.id.common_dialog_top_icon)).setImageDrawable(this.icon);
            } else {
                layout.findViewById(R.id.common_dialog_top_icon).setVisibility(View.GONE);
            }

            ((TextView)layout.findViewById(R.id.common_dialog_top_title)).setText(this.title);
            CharSequence dialogLeftBtnTxt = this.positiveButtonText;
            CharSequence dialogRightBtnTxt = this.negativeButtonText;
            if(TelephoneUtil.getApiLevel() >= 14) {
                dialogLeftBtnTxt = this.negativeButtonText;
                dialogRightBtnTxt = this.positiveButtonText;
            }

            final OnClickListener dialogLeftBtnClickListener = TelephoneUtil.getApiLevel() >= 14?this.negativeButtonClickListener:this.positiveButtonClickListener;
            final OnClickListener dialogRightBtnClickListener = TelephoneUtil.getApiLevel() >= 14?this.positiveButtonClickListener:this.negativeButtonClickListener;
            boolean hasLeftButton = true;
            boolean hasRightButton = true;
            if(dialogLeftBtnTxt != null) {
                ((Button)layout.findViewById(R.id.common_dialog_left_button)).setText(dialogLeftBtnTxt);
                if(dialogLeftBtnClickListener != null) {
                    ((Button)layout.findViewById(R.id.common_dialog_left_button)).setOnClickListener(new android.view.View.OnClickListener() {
                        public void onClick(View v) {
                            dialogLeftBtnClickListener.onClick(dialog, -1);
                            dialog.dismiss();
                        }
                    });
                }
            } else {
                layout.findViewById(R.id.common_dialog_left_button).setVisibility(View.GONE);
                hasLeftButton = false;
            }

            if(dialogRightBtnTxt != null) {
                ((Button)layout.findViewById(R.id.common_dialog_right_button)).setText(dialogRightBtnTxt);
                if(dialogRightBtnClickListener != null) {
                    ((Button)layout.findViewById(R.id.common_dialog_right_button)).setOnClickListener(new android.view.View.OnClickListener() {
                        public void onClick(View v) {
                            dialogRightBtnClickListener.onClick(dialog, -2);
                            dialog.dismiss();
                        }
                    });
                }
            } else {
                layout.findViewById(R.id.common_dialog_right_button).setVisibility(View.GONE);
                hasRightButton = false;
            }

            if(!hasLeftButton && !hasRightButton) {
                layout.findViewById(R.id.common_dialog_bottom_layout).setVisibility(View.GONE);
            }

            TextView textView = (TextView)layout.findViewById(R.id.common_dialog_content);
            textView.setMovementMethod(ScrollingMovementMethod.getInstance());
            if(this.message != null) {
                textView.setText(this.message);
            } else {
                textView.setVisibility(View.GONE);
            }

            if(this.contentView != null) {
                ((LinearLayout)layout.findViewById(R.id.common_dialog_custom_view_layout)).addView(this.contentView, new android.view.ViewGroup.LayoutParams(-2, -2));
            } else {
                layout.findViewById(R.id.common_dialog_custom_view_layout).setVisibility(View.GONE);
            }

            return dialog;
        }

    }

}
