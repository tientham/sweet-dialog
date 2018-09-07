package it.vn.tientham.sweetdialog;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.util.AttributeSet;
import android.util.Xml;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Created by tientham on 30/09/17.
 */

public class OptAnimationLoader {

    public static Animation loadAnimation (Context context, int id) throws Resources.NotFoundException
    {
        XmlResourceParser parser = null;
        try
        {
            parser = context.getResources().getAnimation(id);
            return createAnimationFromXml(context, parser);
        }
        catch (XmlPullParserException e)
        {
            Resources.NotFoundException rnfe = new Resources.NotFoundException("Can't load animation resource ID #0x" + Integer.toHexString(id));
            rnfe.initCause(e);
            throw rnfe;
        }
        catch (IOException ioe)
        {
            Resources.NotFoundException rnf = new Resources.NotFoundException("Can't load animation resource ID #0x" + Integer.toHexString(id));
            rnf.initCause(ioe);
            throw rnf;
        }
        finally {
            if (parser != null) parser.close();
        }
    }

    private static Animation createAnimationFromXml (Context context, XmlPullParser parser) throws XmlPullParserException, IOException {
        return createAnimationFromXml(context, parser, null, Xml.asAttributeSet(parser));
    }

    private static Animation createAnimationFromXml (Context c, XmlPullParser parser, AnimationSet parent, AttributeSet attributeSet)
            throws XmlPullParserException, IOException
    {
        Animation animation = null;

        // Make sure we are on a start tag.
        int type;
        int depth = parser.getDepth();

        while (((type = parser.next()) != XmlPullParser.END_TAG || parser.getDepth() > depth) && type != XmlPullParser.END_DOCUMENT)
        {
            if (type != XmlPullParser.START_TAG) {
                continue;
            }

            String name = parser.getName();

            if (name.equals("set")) {
                animation = new AnimationSet(c, attributeSet);
                createAnimationFromXml(c, parser, (AnimationSet) animation, attributeSet);
            } else if (name.equals("alpha")) {
                animation = new AlphaAnimation(c, attributeSet);
            } else if (name.equals("scale")) {
                animation = new ScaleAnimation(c, attributeSet);
            }  else if (name.equals("rotate")) {
                animation = new RotateAnimation(c, attributeSet);
            }  else if (name.equals("translate")) {
                animation = new TranslateAnimation(c, attributeSet);
            } else {
                try {
                    animation = (Animation) Class.forName(name).getConstructor(Context.class, AttributeSet.class).newInstance(c, attributeSet);
                } catch (Exception te) {
                    throw new RuntimeException("Unknown animation name: " + parser.getName() + " error:" + te.getMessage());
                }
            }

            if (parent != null)
                parent.addAnimation(animation);
        }

        return animation;
    }

}
