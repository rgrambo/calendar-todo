package layout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.Button;

import edu.uw.rgrambo.calendarto_do.Event;

/**
 * Created by rossgrambo on 3/3/16.
 */
public class EventButton extends Button {
    public Event event;

    public EventButton(Context context, AttributeSet attrs, Event event) {
        super(context, attrs);
        this.event = event;

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
