/* Copyright 2012 the original author or authors.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package org.springframework.data.gemfire.examples.util;

/**
* @author Wayne Lund
* @author David Turanski
*
*/
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.security.SecureRandom;

public class ServerPortGenerator {
    public int generatePort() {
        SecureRandom random = new SecureRandom();
        int port = random.nextInt(10000);
        port += 40000;
        // implement a check to make sure port is not used.
        // on bind exception try again
        System.err.println("Server Port:" + port);
        return port;
    }

    public int generatePort(int min, int max) throws IOException {
        ServerSocket socket = new ServerSocket();
        int port = bind(socket, min, max - min);
        if (port>0) {
            socket.close();
            return port;
        } else {
            throw new IOException("Unable to bind on to a port between "+min+" and "+max);
        }
        
    }

    public int bind(ServerSocket socket, int portstart, int retries) throws IOException {
        InetSocketAddress addr = null;
        int port = portstart;
        while (retries > 0) {
            try {
                addr = new InetSocketAddress(port);
                socket.bind(addr);
                retries = 0;
                return port;
            } catch (IOException x) {
                retries--;
                if (retries <= 0) {
                    throw x;
                }
                port++;
            }
        }
        return -1;
    }

}
