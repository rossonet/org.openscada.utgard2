/*
 * This file is part of the OpenSCADA project
 * Copyright (C) 2006 inavare GmbH (http://inavare.com)
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package org.openscada.opc.lib.test;

import java.util.LinkedList;
import java.util.List;

import org.jinterop.dcom.common.JIException;
import org.openscada.opc.lib.common.ConnectionInformation;
import org.openscada.opc.lib.da.Async20Access;
import org.openscada.opc.lib.da.Server;

/**
 * Another test showing the "Access" interface with the Async20Access implementation. Testing two connections at the same time.
 * 
 * @author Jens Reimann <jens.reimann@inavare.net>
 */
public class OPCTest5
{
    public static void main ( String[] args ) throws Throwable
    {
        // create connection information
        ConnectionInformation baseInfo = new ConnectionInformation ();
        baseInfo.setHost ( args[0] );
        baseInfo.setDomain ( args[1] );
        baseInfo.setUser ( args[2] );
        baseInfo.setPassword ( args[3] );

        List<TestInfo> testInfo = new LinkedList<TestInfo> ();
        int i = 0;

        try
        {

            while ( args.length > ( ( i * 2 ) + 4 ) )
            {
                ConnectionInformation ci = new ConnectionInformation ( baseInfo );
                ci.setClsid ( args[ ( i * 2 ) + 4] );
                TestInfo ti = new TestInfo ();
                ti._info = ci;
                ti._itemId = args[ ( i * 2 ) + 5];
                ti._server = new Server ( ci );

                ti._server.connect ();
                ti._access = new Async20Access ( ti._server, 100 );
                ti._access.addItem ( ti._itemId, new DataCallbackDumper () );
                ti._access.bind ();

                testInfo.add ( ti );
                i++;
            }

            // wait a little bit
            Thread.sleep ( 10 * 1000 );
        }
        catch ( JIException e )
        {
            System.out.println ( String.format ( "%08X", e.getErrorCode () ) );
        }
    }
}
