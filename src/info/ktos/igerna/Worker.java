/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package info.ktos.igerna;

import java.net.Socket;

/**
 *
 * @author stud109
 */
class Worker implements Runnable
{
    private Socket clientSocket;

    public Worker(Socket clientSocket)
    {
        this.clientSocket = clientSocket;
    }

    public void run()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
