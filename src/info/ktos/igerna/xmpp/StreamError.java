/*
 * Igerna, version 0.1
 *
 * Copyright (C) Marcin Badurowicz 2009
 *
 *
 * This file is part of Igerna.
 *
 * Igerna is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Igerna is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Igerna.  If not, see <http://www.gnu.org/licenses/>.
 */

package info.ktos.igerna.xmpp;

import info.ktos.igerna.IgernaServer;


/**
 *
 */
public class StreamError
{
    public static String invalidXML()
    {
        return err("<invalid-xml />");
    }

    public static String internalServerError()
    {
        return err("<internal-server-error />");
    }

    public static String internalServerError2()
    {
        return err2("<internal-server-error />");
    }

    private static String err(String err)
    {
        return String.format("%s%s<stream:error>%s</stream:error>%s",
                Stream.xmlPrologue(),
                Stream.start(IgernaServer.getBindHost(), Stream.streamId()),
                err,
                Stream.end());
    }

    private static String err2(String err)
    {
        return String.format("<stream:error>%s</stream:error>%s",
                err,
                Stream.end());
    }
}
