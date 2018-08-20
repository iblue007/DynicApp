//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.nd.hilauncherdev.framework.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.graphics.drawable.Drawable;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.FrameLayout.LayoutParams;

import com.nd.hilauncherdev.dynamic.R;
import com.nd.hilauncherdev.dynamic.R.id;
import com.nd.hilauncherdev.dynamic.R.layout;
import com.nd.hilauncherdev.dynamic.R.style;
import com.nd.hilauncherdev.kitset.util.ScreenUtil;
import com.nd.hilauncherdev.kitset.util.TelephoneUtil;

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

        public CommonDialog.Builder setMessage(CharSequence message) {
            this.message = message;
            return this;
        }

        public CommonDialog.Builder setMessage(int message) {
            this.message = (String)this.context.getText(message);
            return this;
        }

        public CommonDialog.Builder setTitle(int title) {
            this.title = (String)this.context.getText(title);
            return this;
        }

        public CommonDialog.Builder setTitle(CharSequence title) {
            this.title = title;
            return this;
        }

        public CommonDialog.Builder setIcon(int icon) {
            this.icon = this.context.getResources().getDrawable(icon);
            return this;
        }

        public CommonDialog.Builder setIcon(Drawable icon) {
            this.icon = icon;
            return this;
        }

        public CommonDialog.Builder setContentView(View v) {
            this.contentView = v;
            return this;
        }

        public CommonDialog.Builder setPositiveButton(int positiveButtonText, OnClickListener listener) {
            this.positiveButtonText = (String)this.context.getText(positiveButtonText);
            this.positiveButtonClickListener = listener;
            return this;
        }

        public CommonDialog.Builder setPositiveButton(CharSequence positiveButtonText, OnClickListener listener) {
            this.positiveButtonText = positiveButtonText;
            this.positiveButtonClickListener = listener;
            return this;
        }

        public CommonDialog.Builder setNegativeButton(int negativeButtonText, OnClickListener listener) {
            this.negativeButtonText = (String)this.context.getText(negativeButtonText);
            this.negativeButtonClickListener = listener;
            return this;
        }

        public CommonDialog.Builder setNegativeButton(CharSequence negativeButtonText, OnClickListener listener) {
            this.negativeButtonText = negativeButtonText;
            this.negativeButtonClickListener = listener;
            return this;
        }

        public void setContext(Context context) {
            this.context = context;
        }

        public void setShowTitle(boolean showTitle) {
            this.showTitle = showTitle;
        }

        public void setShowMessage(boolean showMessage) {
            this.showMessage = showMessage;
        }

        public CommonDialog create() {
            final CommonDialog dialog = new CommonDialog(this.context, style.Dialog);
            dialog.setContentView(R.layout.common_dialog_layout);
            ViewGroup layout = (ViewGroup)dialog.findViewById(id.common_dialog_layout);
            int width = (int)((float)ScreenUtil.getCurrentScreenWidth(this.context) * 0.9F);
            LayoutParams lp = new LayoutParams(width, -2);
            layout.setLayoutParams(lp);
            if(this.icon != null) {
                ((ImageView)layout.findViewById(id.common_dialog_top_icon)).setImageDrawable(this.icon);
            } else {
                layout.findViewById(id.common_dialog_top_icon).setVisibility(View.GONE);
            }

            ((TextView)layout.findViewById(id.common_dialog_top_title)).setText(this.title);
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
                ((Button)layout.findViewById(id.common_dialog_left_button)).setText(dialogLeftBtnTxt);
                if(dialogLeftBtnClickListener != null) {
                    ((Button)layout.findViewById(id.common_dialog_left_button)).setOnClickListener(new android.view.View.OnClickListener() {
                        public void onClick(View v) {
                            dialogLeftBtnClickListener.onClick(dialog, -1);
                            dialog.dismiss();
                        }
                    });
                }
            } else {
                layout.findViewById(id.common_dialog_left_button).setVisibility(View.GONE);
                hasLeftButton = false;
            }

            if(dialogRightBtnTxt != null) {
                ((Button)layout.findViewById(id.common_dialog_right_button)).setText(dialogRightBtnTxt);
                if(dialogRightBtnClickListener != null) {
                    ((Button)layout.findViewById(id.common_dialog_right_button)).setOnClickListener(new android.view.View.OnClickListener() {
                        public void onClick(View v) {
                            dialogRightBtnClickListener.onClick(dialog, -2);
                            dialog.dismiss();
                        }
                    });
                }
            } else {
                layout.findViewById(id.common_dialog_right_button).setVisibility(View.GONE);
                hasRightButton = false;
            }

            if(!hasLeftButton && !hasRightButton) {
                layout.findViewById(id.common_dialog_bottom_layout).setVisibility(View.GONE);
            }

            TextView textView = (TextView)layout.findViewById(id.common_dialog_content);
            textView.setMovementMethod(ScrollingMovementMethod.getInstance());
            if(this.message != null) {
                textView.setText(this.message);
            } else {
                textView.setVisibility(View.GONE);
            }

            if(this.contentView != null) {
                ((LinearLayout)layout.findViewById(id.common_dialog_custom_view_layout)).addView(this.contentView, new android.view.ViewGroup.LayoutParams(-2, -2));
            } else {
                layout.findViewById(id.common_dialog_custom_view_layout).setVisibility(View.GONE);
            }

            return dialog;
        }

        public CommonDialog createEx() {
            final CommonDialog dialog = new CommonDialog(this.context, style.Dialog);
            dialog.setContentView(layout.common_dialog_layout_ex);
            if(!this.showTitle) {
                dialog.findViewById(id.common_dialog_top_layout).setVisibility(View.GONE);
                dialog.findViewById(id.common_dialog_layout).setBackgroundDrawable((Drawable)null);
            }

            ViewGroup layout = (ViewGroup)dialog.findViewById(id.common_dialog_layout);
            int width = (int)((float)ScreenUtil.getCurrentScreenWidth(this.context) * 0.9F);
            LayoutParams lp = new LayoutParams(width, -2);
            layout.setLayoutParams(lp);
            ((TextView)layout.findViewById(id.common_dialog_top_title)).setText(this.title);
            CharSequence dialogLeftBtnTxt = this.negativeButtonText;
            CharSequence dialogRightBtnTxt = this.positiveButtonText;
            final OnClickListener dialogLeftBtnClickListener = this.negativeButtonClickListener;
            final OnClickListener dialogRightBtnClickListener = this.positiveButtonClickListener;
            
            if(dialogLeftBtnTxt != null) {
                ((TextView)layout.findViewById(id.common_dialog_left_button)).setText(dialogLeftBtnTxt);
                if(dialogLeftBtnClickListener != null) {
                    ((TextView)layout.findViewById(id.common_dialog_left_button)).setOnClickListener(new android.view.View.OnClickListener() {
                        public void onClick(View v) {
                            dialogLeftBtnClickListener.onClick(dialog, -1);
                            dialog.dismiss();
                        }
                    });
                }
            } else {
                layout.findViewById(id.common_dialog_left_button).setVisibility(View.GONE);
            }

            if(dialogRightBtnTxt != null) {
                ((TextView)layout.findViewById(id.common_dialog_right_button)).setText(dialogRightBtnTxt);
                if(dialogRightBtnClickListener != null) {
                    ((TextView)layout.findViewById(id.common_dialog_right_button)).setOnClickListener(new android.view.View.OnClickListener() {
                        public void onClick(View v) {
                            dialogRightBtnClickListener.onClick(dialog, -2);
                            dialog.dismiss();
                        }
                    });
                }
            } else {
                layout.findViewById(id.common_dialog_right_button).setVisibility(View.GONE);
            }

            TextView textView = (TextView)layout.findViewById(id.common_dialog_content);
            if(!this.showMessage) {
                textView.setVisibility(View.GONE);
            }

            textView.setMovementMethod(ScrollingMovementMethod.getInstance());
            if(this.message != null) {
                textView.setText(this.message);
            } else {
                textView.setVisibility(View.GONE);
            }

            if(this.contentView != null) {
                ((LinearLayout)layout.findViewById(id.common_dialog_custom_view_layout)).addView(this.contentView, new android.view.ViewGroup.LayoutParams(-2, -2));
            } else {
                layout.findViewById(id.common_dialog_custom_view_layout).setVisibility(View.GONE);
            }

            return dialog;
        }
    }
}
