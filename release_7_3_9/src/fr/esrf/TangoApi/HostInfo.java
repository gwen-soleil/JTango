//+======================================================================
// $Source$
//
// Project:   Tango
//
// Description:  java source code for the AccessProxy class definition .
//
// $Author$
//
// Copyright (C) :      2004,2005,2006,2007,2008,2009
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
// $Log$
//
//-======================================================================

package fr.esrf.TangoApi;


/** 
 *	This class manage the host information
 *	- name
 *	- address
 *
 * @author  verdier
 */

import fr.esrf.Tango.DevFailed;
import fr.esrf.TangoDs.Except;
import fr.esrf.TangoDs.TangoConst;

import java.net.*;
import java.util.*;


class  HostInfo
{
	private static String	name    = null;
	private static String	address = null;
	
	//===============================================================
	//===============================================================
	private HostInfo()  throws DevFailed
	{
		try {
			Enumeration<NetworkInterface> enet = NetworkInterface.getNetworkInterfaces();
			while ( enet.hasMoreElements() && (name == null)) {
				NetworkInterface net = enet.nextElement();

				//	Only on JDK 1.6
				//if ( net.isLoopback() )
				//	continue;

				Enumeration<InetAddress> eaddr = net.getInetAddresses();

				while ( eaddr.hasMoreElements() ) 	{
					InetAddress inet = eaddr.nextElement();
					if ( ! inet.getCanonicalHostName().equalsIgnoreCase(inet.getHostAddress()) ) {
						name    = inet.getCanonicalHostName();
						address = inet.getHostAddress();
						break;
					}
				}
			}
		}
		catch(SocketException e) {
			Except.throw_exception("TangoApi_SockectException",
				e.toString(), "HostInfo.HostInfo()");
		}
	}

	//===============================================================
	//===============================================================
	static String getName() throws DevFailed
	{
		if (name==null)
			new HostInfo();
		return name;
	}
	//===============================================================
	//===============================================================
	static String getAddress() throws DevFailed
	{
		if (address==null)
			new HostInfo();
		return address;
	}
	//===============================================================
	//===============================================================
	private static String toStaticString()
	{
		String	str = "";
		try {
			if (name==null)
				new HostInfo();
			str += "name:          " + name + "\n";
			str += "address:       " + address + "\n";
		}
		catch(DevFailed e) {
			str = e.errors[0].desc;
		}
		return str;
	}
	//===============================================================
	//===============================================================
	public static void main(String[] args)
	{
		System.out.println(toStaticString());
	}
	//===============================================================
	//===============================================================
}
