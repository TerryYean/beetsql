package org.beetl.sql.core;

import java.util.Stack;

import org.beetl.core.Event;
import org.beetl.core.Listener;
import org.beetl.core.statement.GeneralForStatement;
import org.beetl.core.statement.PlaceholderST;

public class PlaceHolderListener implements Listener
{

    @Override
    public Object onEvent(Event e)
    {
            Stack stack = (Stack) e.getEventTaget();
            Object o = stack.peek();
            if (o instanceof PlaceholderST)
            {
            			PlaceholderST gf = (PlaceholderST) o;
            			SQLPlaceholderST rf = new SQLPlaceholderST(gf);
                    return rf;
            }

            else
            {
                    return null;
            }
    }
}
