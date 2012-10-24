//+======================================================================
// $Source$
//
// Project:   Tango
//
// Description:  java source code for the TANGO client/server API.
//
// $Author$
//
// Copyright (C) :      2004,2005,2006,2007,2008,2009,2010,2011,2012
//						European Synchrotron Radiation Facility
//                      BP 220, Grenoble 38043
//                      FRANCE
//
// This file is part of Tango.
//
// Tango is free software: you can redistribute it and/or modify
// it under the terms of the GNU Lesser General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
// 
// Tango is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU Lesser General Public License for more details.
// 
// You should have received a copy of the GNU Lesser General Public License
// along with Tango.  If not, see <http://www.gnu.org/licenses/>.
//
// $Revision$
//
//-======================================================================


package fr.esrf.TangoApi.events;

import fr.esrf.Tango.DevFailed;
import fr.esrf.TangoApi.DeviceProxy;

import javax.swing.*;


/**
 * @author pascal_verdier
 */
public class TangoChange extends EventDispatcher implements java.io.Serializable {

    String      attr_name;
    int         event_identifier;
    String[]    filters;

    //=======================================================================
    /**
     * Creates a new instance of AttrAbsoluteChange
     *
     * @param device_proxy device proxy object.
     * @param attr_name    attribute name.
     * @param filters      filter array
     */
    //=======================================================================
    public TangoChange(DeviceProxy device_proxy, String attr_name, String[] filters) {
        super(device_proxy);
        this.attr_name = attr_name;
        this.filters = filters;
        event_identifier = -1;
    }

    //=======================================================================
    //=======================================================================
    public void addTangoChangeListener(ITangoChangeListener listener, boolean stateless)
            throws DevFailed {
        event_listeners.add(ITangoChangeListener.class, listener);
        event_identifier = subscribe_change_event(attr_name, filters, stateless);
    }

    //=======================================================================
    //=======================================================================
    public void removeTangoChangeListener(ITangoChangeListener listener)
            throws DevFailed {
        event_listeners.remove(ITangoChangeListener.class, listener);
        if (event_listeners.getListenerCount() == 0)
            unsubscribe_event(event_identifier);
    }

    //=======================================================================
    //=======================================================================
    public void dispatch_event(final EventData event_data) {
        final TangoChange tg = this;
        Runnable do_work_later = new Runnable() {
            public void run() {
                TangoChangeEvent change_event = new TangoChangeEvent(tg, event_data);
                fireTangoChangeEvent(change_event);
            }
        };
        SwingUtilities.invokeLater(do_work_later);
    }

    //=======================================================================
    //=======================================================================
    private void fireTangoChangeEvent(TangoChangeEvent change_event) {
        // Guaranteed to return a non null array
        Object[] listeners = event_listeners.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ITangoChangeListener.class) {
                ((ITangoChangeListener) listeners[i + 1]).change(change_event);
            }
        }
    }
    //==============================================================
    //==============================================================
}