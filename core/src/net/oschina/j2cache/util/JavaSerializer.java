/**
 * Copyright (c) 2015-2017, Winter Lau (javayou@gmail.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.oschina.j2cache.util;

import java.io.*;

import net.sf.ehcache.CacheException;

/**
 * 标准的 Java 序列化
 *
 * @author Winter Lau(javayou@gmail.com)
 */
public class JavaSerializer implements Serializer {

	@Override
	public String name() {
		return "java";
	}
	
	@Override
	public byte[] serialize(Object obj) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try (ObjectOutputStream oos = new ObjectOutputStream(baos)){
			oos.writeObject(obj);
			return baos.toByteArray();
		}
	}

	@Override
	public Object deserialize(byte[] bytes) throws IOException {
		return this.deserialize(bytes, Thread.currentThread().getContextClassLoader());
	}

    @Override
    public Object deserialize(byte[] bytes, ClassLoader classLoader) throws IOException {
        if(bytes == null || bytes.length == 0)
            return null;
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        try (ClassLoadingObjectInputStream ois = new ClassLoadingObjectInputStream(bais, classLoader)){
            return ois.readObject();
        } catch (ClassNotFoundException e) {
            throw new CacheException(e);
        }
    }
	
}
