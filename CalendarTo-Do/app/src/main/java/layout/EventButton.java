package layout;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import edu.uw.rgrambo.calendarto_do.Event;

/**
 * Created by rossgrambo on 3/3/16.
 */
public class EventButton extends Button {
    public Event event;

    private Context context;

    public EventButton(Context context, AttributeSet attrs, Event event) {
        super(context, attrs);
        this.event = event;
        this.context = context;

        setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        this.setText(event.getTitle());
    }

    public EventButton(Context context, Event event) {
        super(context);
        this.event = event;

        this.setText(event.getTitle());
    }

    public EventButton(Context context) {
        super(context);
    }
}
