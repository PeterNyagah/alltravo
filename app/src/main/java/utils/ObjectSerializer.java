/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package utils;

import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;


public class ObjectSerializer {

    //private static final Log log = LogFactory.getLog(ObjectSerializer.class);

    public static String serialize(Serializable obj){
        if (obj == null) return "";
        ByteArrayOutputStream serialObj = new ByteArrayOutputStream();
        try {
            ObjectOutputStream objStream = new ObjectOutputStream(serialObj);
            objStream.writeObject(obj);
            objStream.close();
        } catch (Exception e) {
            Log.d("Serialization error: " , e.getMessage());
        }
        return encodeBytes(serialObj.toByteArray());
    }

    public static Object deserialize(String str) throws IOException, ClassNotFoundException {
        if (str == null || str.length() == 0) return null;
        ByteArrayInputStream serialObj = new ByteArrayInputStream(decodeBytes(str));
        ObjectInputStream objStream = null;
        try {
            objStream = new ObjectInputStream(serialObj);
        } catch (Exception e) {
            Log.d("Deserialization error: " , e.getMessage());
        }
        return objStream.readObject();
    }

    public static String encodeBytes(byte[] bytes) {
        StringBuffer strBuf = new StringBuffer();

        for (int i = 0; i < bytes.length; i++) {
            strBuf.append((char) (((bytes[i] >> 4) & 0xF) + ((int) 'a')));
            strBuf.append((char) (((bytes[i]) & 0xF) + ((int) 'a')));
        }

        return strBuf.toString();
    }

    public static byte[] decodeBytes(String str) {
        byte[] bytes = new byte[str.length() / 2];
        for (int i = 0; i < str.length(); i+=2) {
            char c = str.charAt(i);
            bytes[i/2] = (byte) ((c - 'a') << 4);
            c = str.charAt(i+1);
            bytes[i/2] += (c - 'a');
        }
        return bytes;
    }

}