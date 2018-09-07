package it.vn.tientham.sweetdialog.sd;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;

import it.vn.tientham.sweetdialog.R;

/**
 * Created by tientham on 01/10/17.
 * /**
 * Internal helper class to build dialog.
 *
 * @param <F> Fragment class
 * @param <A> Activity class
 */

abstract class InternalHelper<F, A extends Context> {

    public abstract A getActivity();

    public abstract F getTargetFragment();

    /**
     * Create a Simple Dialog based on content given inside Bundle
     * @param args Bundle which holds all information to create Dialog
     * @return A Simple dialog to be returned.
     */
    public Dialog createDialog(Bundle args)
    {
        final SimpleDialog dialog = newInstance(args);
        setTitle(args, dialog);
        setIcon(args, dialog);
        setMessage(args, dialog);
        setEditText(args, dialog);
        final int requestCode = getRequestCode(args);
        setView(args, dialog, requestCode);
        setItems(args, dialog, requestCode);
        setAdapter(args, dialog, requestCode);
        setSingleChoiceItems(args, dialog, requestCode);
        setPositiveButton(args, dialog, requestCode);
        setNeutralButton(args, dialog, requestCode);
        setNegativeButton(args, dialog, requestCode);
        setCancelable(args, dialog);
        return dialog;
    }

    public boolean hasItemClickListener()
    {
        return (fragmentImplements(SimpleDialog.OnItemClickListener.class)
                || activityImplements(SimpleDialog.OnItemClickListener.class));
    }

    public boolean hasListProvider(Bundle args)
    {
        boolean useAdapter = false;

        if (has(args, SimpleDialog.ARG_USE_ADAPTER)) {
            useAdapter = args.getBoolean(SimpleDialog.ARG_USE_ADAPTER);
        }
        return useAdapter && (fragmentImplements(SimpleDialog.ListProvider.class) || activityImplements(SimpleDialog.ListProvider.class));
    }

    public boolean hasSingleChoiceArrayItemProvider(Bundle args)
    {
        int singleChoiceCheckedItem = -1;

        if (has(args, SimpleDialog.ARG_SINGLE_CHOICE_CHECKED_ITEM)) {
            singleChoiceCheckedItem = args.getInt(SimpleDialog.ARG_SINGLE_CHOICE_CHECKED_ITEM);
        }

        return singleChoiceCheckedItem >= 0 && (fragmentImplements(SimpleDialog.SingleChoiceArrayItemProvider.class)
                || activityImplements(SimpleDialog.SingleChoiceArrayItemProvider.class));
    }

    private SimpleDialog newInstance(Bundle args)
    {
        return hasTheme(args) ? new SimpleDialog(getActivity(), args.getInt(SimpleDialog.ARG_THEME_RES_ID)) : new SimpleDialog(getActivity());
    }

    /**
     * Check the present of a Key as String inside a given Bundle
     * @param args Bundle is used to check Key's present
     * @param key String key
     * @return true if present, false otherwise.
     */
    private boolean has(Bundle args, String key) {
        return args != null && args.containsKey(key);
    }

    private boolean hasTheme(Bundle args) {
        return has(args, SimpleDialog.ARG_THEME_RES_ID);
    }

    private void setTitle(Bundle args, SimpleDialog dialog)
    {
        if (has(args, SimpleDialog.ARG_TITLE))
        {
            dialog.setTitle(args.getCharSequence(SimpleDialog.ARG_TITLE));
        } else if (has(args, SimpleDialog.ARG_TITLE_RES_ID)) {
            dialog.setTitle(args.getInt(SimpleDialog.ARG_TITLE_RES_ID));
        }
    }

    private void setIcon(Bundle args, SimpleDialog dialog) {
        if (has(args, SimpleDialog.ARG_ICON)) {
            dialog.setIcon(args.getInt(SimpleDialog.ARG_ICON));
        }
    }

    private void setMessage(Bundle args, SimpleDialog dialog) {
        if (has(args, SimpleDialog.ARG_MESSAGE)) {
            dialog.setMessage(args.getCharSequence(SimpleDialog.ARG_MESSAGE));
        } else if (has(args, SimpleDialog.ARG_MESSAGE_RES_ID)) {
            dialog.setMessage(args.getInt(SimpleDialog.ARG_MESSAGE_RES_ID));
        }
    }

    private int getRequestCode(Bundle args) {
        if (has(args, SimpleDialog.ARG_REQUEST_CODE)) {
            return args.getInt(SimpleDialog.ARG_REQUEST_CODE);
        }
        return 0;
    }

    private void setEditText(Bundle args, SimpleDialog dialog)
    {

        if (!has(args, SimpleDialog.ARG_EDIT_TEXT_INITIAL_TEXT)
                || !has(args, SimpleDialog.ARG_EDIT_TEXT_INPUT_TYPE)) {
            return;
        }

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.sld__dialog_view_editor, null);
        EditText editText = (EditText) view.findViewById(android.R.id.text1);
        editText.setText(args.getCharSequence(SimpleDialog.ARG_EDIT_TEXT_INITIAL_TEXT));
        editText.setInputType(args.getInt(SimpleDialog.ARG_EDIT_TEXT_INPUT_TYPE));
        dialog.setView(view);
    }

    private void setView(Bundle args, SimpleDialog dialog, int requestCode) {
        if (!has(args, SimpleDialog.ARG_USE_VIEW) || !args.getBoolean(SimpleDialog.ARG_USE_VIEW)) {
            return;
        }
        if (fragmentImplements(SimpleDialog.ViewProvider.class)) {
            dialog.setView(((SimpleDialog.ViewProvider) getTargetFragment())
                    .onCreateView(dialog, requestCode));
        }
        if (activityImplements(SimpleDialog.ViewProvider.class)) {
            dialog.setView(((SimpleDialog.ViewProvider) getActivity())
                    .onCreateView(dialog, requestCode));
        }
    }

    @TargetApi(Build.VERSION_CODES.FROYO)
    private void setItems(Bundle args, final SimpleDialog dialog, final int requestCode)
    {
        if (!hasItemClickListener()) {
            return;
        }

        CharSequence[] items;

        if (has(args, SimpleDialog.ARG_ITEMS) && Build.VERSION_CODES.ECLAIR <= Build.VERSION.SDK_INT) {
            items = args.getCharSequenceArray(SimpleDialog.ARG_ITEMS);
        } else if (has(args, SimpleDialog.ARG_ITEMS_RES_ID)) {
            items = getActivity().getResources().getTextArray(args.getInt(SimpleDialog.ARG_ITEMS_RES_ID));
        } else {
            return;
        }

        int[] icons = null;

        if (has(args, SimpleDialog.ARG_ICONS)) {
            icons = args.getIntArray(SimpleDialog.ARG_ICONS);
        }

        if (fragmentImplements(SimpleDialog.OnItemClickListener.class) || activityImplements(SimpleDialog.OnItemClickListener.class))
        {
            AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                {
                    if (fragmentImplements(SimpleDialog.OnItemClickListener.class)) {
                        ((SimpleDialog.OnItemClickListener) getTargetFragment())
                                .onItemClick(dialog, requestCode, position);
                    }

                    if (activityImplements(SimpleDialog.OnItemClickListener.class)) {
                        ((SimpleDialog.OnItemClickListener) getActivity())
                                .onItemClick(dialog, requestCode, position);
                    }
                }
            };

            if (icons == null) {
                dialog.setItems(items, listener);
            } else {
                dialog.setItems(items, icons, listener);
            }
        }
    }

    private void setAdapter(Bundle args, final SimpleDialog dialog, final int requestCode)
    {
        if (!hasListProvider(args)) {
            return;
        }

        if (fragmentImplements(SimpleDialog.ListProvider.class))
        {
            dialog.setAdapter(((SimpleDialog.ListProvider) getTargetFragment())
                            .onCreateList(dialog, requestCode),
                    new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            if (fragmentImplements(SimpleDialog.ListProvider.class)) {
                                ((SimpleDialog.ListProvider) getTargetFragment())
                                        .onListItemClick(dialog, requestCode, position);
                            }
                        }
                    });
        }

        if (activityImplements(SimpleDialog.ListProvider.class)) {
            dialog.setAdapter(((SimpleDialog.ListProvider) getActivity())
                            .onCreateList(dialog, requestCode),
                    new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            if (activityImplements(SimpleDialog.ListProvider.class)) {
                                ((SimpleDialog.ListProvider) getActivity())
                                        .onListItemClick(dialog, requestCode, position);
                            }
                        }
                    });
        }
    }

    private void setSingleChoiceItems(Bundle args, final SimpleDialog dialog, final int requestCode)
    {
        if (!hasSingleChoiceArrayItemProvider(args)) {
            return;
        }

        int checkedItem = args.getInt(SimpleDialog.ARG_SINGLE_CHOICE_CHECKED_ITEM);

        if (fragmentImplements(SimpleDialog.SingleChoiceArrayItemProvider.class)) {
            dialog.setSingleChoiceItems(
                    ((SimpleDialog.SingleChoiceArrayItemProvider) getTargetFragment())
                            .onCreateSingleChoiceArray(dialog, requestCode),
                    checkedItem,
                    new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            if (fragmentImplements(SimpleDialog.SingleChoiceArrayItemProvider.class)) {
                                ((SimpleDialog.SingleChoiceArrayItemProvider) getTargetFragment())
                                        .onSingleChoiceArrayItemClick(dialog, requestCode, position);
                            }
                        }
                    });
        }

        if (activityImplements(SimpleDialog.SingleChoiceArrayItemProvider.class)) {
            dialog.setSingleChoiceItems(
                    ((SimpleDialog.SingleChoiceArrayItemProvider) getActivity())
                            .onCreateSingleChoiceArray(dialog, requestCode),
                    checkedItem,
                    new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            if (activityImplements(SimpleDialog.SingleChoiceArrayItemProvider.class)) {
                                ((SimpleDialog.SingleChoiceArrayItemProvider) getActivity())
                                        .onSingleChoiceArrayItemClick(dialog, requestCode, position);
                            }
                        }
                    });
        }
    }

    private void setPositiveButton(Bundle args, SimpleDialog dialog, final int requestCode)
    {
        CharSequence positiveButton = null;

        if (has(args, SimpleDialog.ARG_POSITIVE_BUTTON)) {
            positiveButton = args.getCharSequence(SimpleDialog.ARG_POSITIVE_BUTTON);
        } else if (has(args, SimpleDialog.ARG_POSITIVE_BUTTON_RES_ID)) {
            positiveButton = getActivity().getString(args.getInt(SimpleDialog.ARG_POSITIVE_BUTTON_RES_ID));
        }

        if (positiveButton == null) {
            return;
        }

        dialog.setPositiveButton(positiveButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, final int which) {
                if (fragmentImplements(SimpleDialog.OnClickListener.class)) {
                    ((SimpleDialog.OnClickListener) getTargetFragment())
                            .onDialogPositiveButtonClicked((SimpleDialog) dialog,
                                    requestCode,
                                    ((SimpleDialog) dialog).getView());
                }
                if (activityImplements(SimpleDialog.OnClickListener.class)) {
                    ((SimpleDialog.OnClickListener) getActivity())
                            .onDialogPositiveButtonClicked((SimpleDialog) dialog,
                                    requestCode,
                                    ((SimpleDialog) dialog).getView());
                }
            }
        });
    }

    private void setNeutralButton(Bundle args, SimpleDialog dialog, final int requestCode)
    {
        CharSequence neutralButton = null;

        if (has(args, SimpleDialog.ARG_NEUTRAL_BUTTON)) {
            neutralButton = args.getCharSequence(SimpleDialog.ARG_NEUTRAL_BUTTON);
        } else if (has(args, SimpleDialog.ARG_NEUTRAL_BUTTON_RES_ID)) {
            neutralButton = getActivity().getString(args.getInt(SimpleDialog.ARG_NEUTRAL_BUTTON_RES_ID));
        }

        if (neutralButton == null) {
            return;
        }

        dialog.setNeutralButton(neutralButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, final int which) {
                if (fragmentImplements(SimpleDialog.OnNeutralButtonClickListener.class)) {
                    ((SimpleDialog.OnNeutralButtonClickListener) getTargetFragment())
                            .onDialogNeutralButtonClicked((SimpleDialog) dialog, requestCode, ((SimpleDialog) dialog).getView());
                }

                if (activityImplements(SimpleDialog.OnNeutralButtonClickListener.class)) {
                    ((SimpleDialog.OnNeutralButtonClickListener) getActivity())
                            .onDialogNeutralButtonClicked((SimpleDialog) dialog, requestCode, ((SimpleDialog) dialog).getView());
                }
            }
        });
    }

    private void setNegativeButton(Bundle args, SimpleDialog dialog, final int requestCode)
    {
        CharSequence negativeButton = null;

        if (has(args, SimpleDialog.ARG_NEGATIVE_BUTTON))
        {
            negativeButton = args.getCharSequence(SimpleDialog.ARG_NEGATIVE_BUTTON);
        } else if (has(args, SimpleDialog.ARG_NEGATIVE_BUTTON_RES_ID)) {
            negativeButton = getActivity().getString(args.getInt(SimpleDialog.ARG_NEGATIVE_BUTTON_RES_ID));
        }

        if (negativeButton == null) {
            return;
        }

        dialog.setNegativeButton(negativeButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, final int which) {
                if (fragmentImplements(SimpleDialog.OnClickListener.class)) {
                    ((SimpleDialog.OnClickListener) getTargetFragment())
                            .onDialogNegativeButtonClicked((SimpleDialog) dialog, requestCode, ((SimpleDialog) dialog).getView());
                }

                if (activityImplements(SimpleDialog.OnClickListener.class))
                {
                    ((SimpleDialog.OnClickListener) getActivity())
                            .onDialogNegativeButtonClicked((SimpleDialog) dialog, requestCode, ((SimpleDialog) dialog).getView());
                }
            }
        });
    }

    private void setCancelable(Bundle args, SimpleDialog dialog)
    {
        boolean cancelable = true;

        if (has(args, SimpleDialog.ARG_CANCELABLE)) {
            cancelable = args.getBoolean(SimpleDialog.ARG_CANCELABLE);
        }

        boolean canceledOnTouchOutside = cancelable;

        if (cancelable && has(args, SimpleDialog.ARG_CANCELED_ON_TOUCH_OUTSIDE)) {
            canceledOnTouchOutside = args.getBoolean(SimpleDialog.ARG_CANCELED_ON_TOUCH_OUTSIDE);
        }

        dialog.setCanceledOnTouchOutside(canceledOnTouchOutside);
    }

    private boolean fragmentImplements(Class<?> c)
    {
        return getTargetFragment() != null && c != null && c.isAssignableFrom(getTargetFragment().getClass());
    }

    private boolean activityImplements(Class<?> c) {
        return getActivity() != null && c != null && c.isAssignableFrom(getActivity().getClass());
    }

}
