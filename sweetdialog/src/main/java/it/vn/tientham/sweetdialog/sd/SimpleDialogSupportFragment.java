package it.vn.tientham.sweetdialog.sd;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

/**
 * Created by tientham on 01/10/17.
 */

/**
 * Simple dialog fragment based on the android.support.v4 library.<br/>
 * If you use normal {@code Activity} for API level 11 and later,
 * use {@link vn.tientham.simple_list_dialog.SimpleDialogFragment} instead.
 *
 * @author Specific thanks to Soichiro Kashima
 * @see vn.tientham.simple_list_dialog.SimpleDialogFragment
 */

public class SimpleDialogSupportFragment extends DialogFragment
{

    /**
     * Default constructor.
     */
    public SimpleDialogSupportFragment() {
    }

//  ------------------------------------------------------------------------------->

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        Bundle args = getArguments();
        // Cancelable must be set to DialogFragment
        if (args != null && args.containsKey(SimpleDialog.ARG_CANCELABLE))
        {
            setCancelable(args.getBoolean(SimpleDialog.ARG_CANCELABLE, true));
        }

        return new InternalHelper<Fragment, FragmentActivity>()
        {
            public FragmentActivity getActivity() {
                return SimpleDialogSupportFragment.this.getActivity();
            }

            public Fragment getTargetFragment() {
                return SimpleDialogSupportFragment.this.getTargetFragment();
            }
        }.createDialog(args);
    }

    @Override
    public void onCancel(DialogInterface dialog)
    {
        super.onCancel(dialog);
        int requestCode = 0;
        Bundle args = getArguments();

        if (args != null && args.containsKey(SimpleDialog.ARG_CANCELABLE))
        {
            requestCode = args.getInt(SimpleDialog.ARG_REQUEST_CODE);
        }
        Fragment targetFragment = getTargetFragment();

        if (targetFragment != null && targetFragment instanceof SimpleDialog.OnCancelListener)
        {
            ((SimpleDialog.OnCancelListener) targetFragment).onDialogCancel((SimpleDialog) dialog, requestCode, ((SimpleDialog) dialog).getView());
        }

        if (getActivity() != null && getActivity() instanceof SimpleDialog.OnCancelListener)
        {
            ((SimpleDialog.OnCancelListener) getActivity()).onDialogCancel((SimpleDialog) dialog, requestCode, ((SimpleDialog) dialog).getView());
        }
    }

//  --------------------------------------------------------------------------------->
    /**
     * Dialog builder for {@code android.support.v4.app.Fragment}.
     * <p/>
     * {@inheritDoc}
     */
    public static class Builder extends SimpleDialog.Builder<SimpleDialogSupportFragment, Fragment>
    {
        private Fragment mTargetFragment;

    //  --------------------------------------------------------------------------->

        @Override
        public Builder setTargetFragment(final Fragment targetFragment)
        {
            mTargetFragment = targetFragment;
            return this;
        }

        @Override
        public SimpleDialogSupportFragment create()
        {
            Bundle args = createArguments();
            SimpleDialogSupportFragment fragment = new SimpleDialogSupportFragment();
            fragment.setArguments(args);

            if (mTargetFragment != null)
            {
                fragment.setTargetFragment(mTargetFragment, 0);
            }
            return fragment;
        }
    }

}
